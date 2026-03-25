package inorganic.elytrahud

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.client.player.LocalPlayer
import net.minecraft.resources.Identifier

object Common : ClientModInitializer {
    @JvmField var CONFIG: ElytraHudConfig? = null
    const val MODID = "elytrahud"
    @JvmField var hudData = HudData()
    @JvmField var client: Minecraft? = null
    @JvmField var isFlying = false
    @JvmField var hudRenderer: HudRenderer? = null

    private var hasEnteredWorld = false

    override fun onInitializeClient() {
        CONFIG = ElytraHudConfig()
        val config = ConfigManager.getConfig()
        config.modEnabled = CONFIG!!.modEnabled
        config.renderTitles = CONFIG!!.renderTitles
        config.renderValues = CONFIG!!.renderValues
        config.renderAirspeed = CONFIG!!.renderAirspeed
        config.renderHorizon = CONFIG!!.renderHorizon
        config.renderDurability = CONFIG!!.renderDurability
        config.renderAltitude = CONFIG!!.renderAltitude
        config.renderVertical = CONFIG!!.renderVertical
        config.renderCompass = CONFIG!!.renderCompass
        config.alwaysDisplayHud = CONFIG!!.alwaysDisplayHud
        ConfigManager.save()

        hudData = HudData()
        client = Minecraft.getInstance()
        hudRenderer = HudRenderer(client!!)

        ClientTickEvents.START_CLIENT_TICK.register { c ->
            if (CONFIG == null || !ConfigManager.getConfig().modEnabled) {
                return@register
            }
            if (c.level != null && !hasEnteredWorld) {
                hasEnteredWorld = true
                if (c.player != null) {
                    setDefaultSetting(c)
                }
            } else if (c.level == null && hasEnteredWorld) {
                hasEnteredWorld = false
            }
        }

        ClientTickEvents.END_LEVEL_TICK.register { _ ->
            val mc = Minecraft.getInstance()
            val player = mc.player
            if (player == null || CONFIG == null || !ConfigManager.getConfig().modEnabled) {
                return@register
            }

            if (player.isFallFlying()) {
                hudData.update()
                isFlying = true
            } else if (isFlying) {
                isFlying = false
            }
            
            // Always update HUD data if alwaysDisplayHud is enabled
            if (ConfigManager.getConfig().alwaysDisplayHud) {
                hudData.update()
            }
        }

        HudElementRegistry.attachElementBefore(
            VanillaHudElements.HOTBAR,
            Identifier.fromNamespaceAndPath(MODID, "hud")
        ) { graphics, tickDelta ->
            if (CONFIG == null || !ConfigManager.getConfig().modEnabled || client!!.screen is InventoryScreen) {
                return@attachElementBefore
            }
            val player = client!!.player
            if (player == null) {
                return@attachElementBefore
            }
            if (!ConfigManager.getConfig().alwaysDisplayHud && !player.isFallFlying()) {
                return@attachElementBefore
            }
            hudRenderer!!.render(graphics, tickDelta)
        }
    }

    private fun setDefaultSetting(client: Minecraft) {
        // No camera effects to set
    }
}
