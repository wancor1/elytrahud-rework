package jewtvet.elytrahud

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
class ElytraHudModMenu : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            val builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Text.literal("ElytraHud (MFS 2020)"))
            builder.setSavingRunnable { AutoConfig.getConfigHolder(ElytraHudConfig::class.java).save() }
            builder.transparentBackground()
            val entryBuilder = builder.entryBuilder()
            val general = builder.getOrCreateCategory(Text.literal("general_category"))
            
            val config = Common.CONFIG!!

            general.addEntry(
                entryBuilder.startBooleanToggle(Text.literal("Enable mod"), config.modEnabled)
                    .setDefaultValue(true)
                    .setSaveConsumer { newValue -> config.modEnabled = newValue }
                    .setTooltip(Text.literal("Enable/Disable mod"))
                    .build()
            )
            general.addEntry(entryBuilder.startTextDescription(Text.literal("\u00a7lAdvanced:")).build())
            general.addEntry(
                entryBuilder.startBooleanToggle(
                    Text.literal("Enable switching to third person view"), config.thirdPersonEnabled
                )
                    .setDefaultValue(false)
                    .setSaveConsumer { newValue -> config.thirdPersonEnabled = newValue }
                    .setTooltip(Text.literal("Enable/Disable Automatic switching to 3rd-person view"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startBooleanToggle(
                    Text.literal("Enable switching to high FOV"), config.highFovEnabled
                )
                    .setDefaultValue(false)
                    .setSaveConsumer { newValue -> config.highFovEnabled = newValue }
                    .setTooltip(Text.literal("Enable/Disable Automatic switching to Quake Pro FOV"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startBooleanToggle(
                    Text.literal("Enable equipping custom model"), config.pumpkinEnabled
                )
                    .setDefaultValue(false)
                    .setSaveConsumer { newValue -> config.pumpkinEnabled = newValue }
                    .setTooltip(Text.literal("Enable/Disable Automatic carved pumpkin equipping"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startBooleanToggle(
                    Text.literal("Disable spin riptide animation"), config.disableRiptideAnim
                )
                    .setDefaultValue(false)
                    .setSaveConsumer { newValue -> config.disableRiptideAnim = newValue }
                    .setTooltip(
                        Text.literal("Enable/Disable 3rd person's riptide animation during elytra flying. May cause some bugs with trident (or not, idk)")
                    )
                    .build()
            )
            general.addEntry(entryBuilder.startTextDescription(Text.literal("\u00a7lRender:")).build())
            general.addEntry(
                entryBuilder.startBooleanToggle(Text.literal("Render titles"), config.renderTitles)
                    .setDefaultValue(true)
                    .setSaveConsumer { newValue -> config.renderTitles = newValue }
                    .setTooltip(Text.literal("Enable/Disable titles rendering"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startBooleanToggle(Text.literal("Render numeric values"), config.renderValues)
                    .setDefaultValue(false)
                    .setSaveConsumer { newValue -> config.renderValues = newValue }
                    .setTooltip(Text.literal("Enable/Disable numeric values rendering"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startBooleanToggle(Text.literal("Render airspeed"), config.renderAirspeed)
                    .setDefaultValue(true)
                    .setSaveConsumer { newValue -> config.renderAirspeed = newValue }
                    .setTooltip(Text.literal("Enable/Disable airspeed rendering"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startBooleanToggle(Text.literal("Render artificial horizon"), config.renderHorizon)
                    .setDefaultValue(true)
                    .setSaveConsumer { newValue -> config.renderHorizon = newValue }
                    .setTooltip(Text.literal("Enable/Disable artificial horizon rendering"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startBooleanToggle(
                    Text.literal("Render elytra durability"), config.renderDurability
                )
                    .setDefaultValue(true)
                    .setSaveConsumer { newValue -> config.renderDurability = newValue }
                    .setTooltip(Text.literal("Enable/Disable elytra durability rendering"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startBooleanToggle(Text.literal("Render altitude"), config.renderAltitude)
                    .setDefaultValue(true)
                    .setSaveConsumer { newValue -> config.renderAltitude = newValue }
                    .setTooltip(Text.literal("Enable/Disable altitude rendering"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startBooleanToggle(Text.literal("Render vertical speed"), config.renderVertical)
                    .setDefaultValue(true)
                    .setSaveConsumer { newValue -> config.renderVertical = newValue }
                    .setTooltip(Text.literal("Enable/Disable vertical speed rendering"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startBooleanToggle(Text.literal("Render compass"), config.renderCompass)
                    .setDefaultValue(true)
                    .setSaveConsumer { newValue -> config.renderCompass = newValue }
                    .setTooltip(Text.literal("Enable/Disable compass rendering"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startIntSlider(Text.literal("Compass's x coordinate"), config.compassDefaultX, 0, 100)
                    .setDefaultValue(0)
                    .setSaveConsumer { newValue -> config.compassDefaultX = newValue }
                    .setTooltip(Text.literal("Select compass's x coordinate (0 is left edge + 10 pixels, 100 is right edge - 10 pixels)"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startTextDescription(Text.literal("\u00a7lDefault in-game setting:"))
                    .setTooltip(Text.literal("Change these, if yours are different"))
                    .build()
            )
            general.addEntry(
                entryBuilder.startIntSlider(Text.literal("Default FOV"), config.defaultFov, 30, 110)
                    .setDefaultValue(32)
                    .setSaveConsumer { newValue -> config.defaultFov = newValue }
                    .setTooltip(Text.literal("Change it, if your FOV is different"))
                    .build()
            )
            builder.build()
        }
    }
}
