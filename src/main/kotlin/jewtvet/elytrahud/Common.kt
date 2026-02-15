package jewtvet.elytrahud

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.option.Perspective
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.screen.slot.SlotActionType

class Common : ClientModInitializer {
    private var hasEnteredWorld = false

    override fun onInitializeClient() {
        AutoConfig.register(ElytraHudConfig::class.java, ::GsonConfigSerializer)
        val configHolder = AutoConfig.getConfigHolder(ElytraHudConfig::class.java)
        CONFIG = configHolder.config
        
        originalFov = CONFIG!!.defaultFov
        client = MinecraftClient.getInstance()
        hudRenderer = HudRenderer(client!!)

        ClientTickEvents.START_CLIENT_TICK.register(ClientTickEvents.StartTick {
            val config = CONFIG ?: return@StartTick
            if (config.modEnabled) {
                if (client!!.world != null && !hasEnteredWorld) {
                    hasEnteredWorld = true
                    if (client!!.player != null) {
                        setDefaultSetting(client!!)
                    }
                } else if (client!!.world == null && hasEnteredWorld) {
                    hasEnteredWorld = false
                }
            }
        })

        ClientTickEvents.END_WORLD_TICK.register(ClientTickEvents.EndWorldTick {
            val player = client!!.player
            val config = CONFIG ?: return@EndWorldTick
            
            if (player != null && config.modEnabled) {
                if (player.isGliding) {
                    hudData.update()
                } else if (isFlying) {
                    isFlying = false
                    val options = client!!.options
                    if (config.thirdPersonEnabled) {
                        options.perspective = originalPerspective
                    }

                    if (config.highFovEnabled) {
                        options.fov.value = originalFov
                    }

                    if (config.pumpkinEnabled) {
                        val itemsToFind = arrayOf(
                            Items.CARVED_PUMPKIN,
                            Items.LEATHER_HELMET,
                            Items.CHAINMAIL_HELMET,
                            Items.IRON_HELMET,
                            Items.GOLDEN_HELMET,
                            Items.DIAMOND_HELMET,
                            Items.NETHERITE_HELMET
                        )
                        findInHotbar(client!!, itemsToFind)
                    }
                }
            }
        })

        HudRenderCallback.EVENT.register(HudRenderCallback { graphics, tickDelta ->
            val config = CONFIG ?: return@HudRenderCallback
            if (config.modEnabled && isFlying && client!!.currentScreen !is InventoryScreen) {
                hudRenderer!!.render(graphics, tickDelta.getTickProgress(true))
            }
        })
    }

    private fun setDefaultSetting(client: MinecraftClient) {
        val options = client.options
        val config = CONFIG ?: return
        if (client.player != null && client.player!!.isGliding && config.defaultFov != 32) {
            options.fov.value = config.defaultFov
        }

        if (config.defaultFov == 32) {
            config.defaultFov = options.fov.value
        }
    }

    companion object {
        @JvmField
        var CONFIG: ElytraHudConfig? = null
        const val MODID = "elytrahud"
        @JvmField
        var hudData = HudData()
        @JvmField
        var client: MinecraftClient? = null
        @JvmField
        var isFlying = false
        @JvmField
        var hudRenderer: HudRenderer? = null
        @JvmField
        var originalFov = 90
        @JvmField
        var originalPerspective = Perspective.FIRST_PERSON

        @JvmStatic
        fun findInHotbar(client: MinecraftClient, items: Array<Item>) {
            val player = client.player ?: return

            for (item in items) {
                for (i in 0..8) {
                    val stack = player.inventory.getStack(i)
                    if (stack.item === item) {
                        val interactionManager = MinecraftClient.getInstance().interactionManager
                        if (interactionManager != null) {
                            interactionManager.clickSlot(
                                player.currentScreenHandler.syncId,
                                5,
                                i,
                                SlotActionType.SWAP,
                                player
                            )
                        }
                        return
                    }
                }
            }
        }
    }
}
