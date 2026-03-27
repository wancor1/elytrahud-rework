package inorganic.elytrahud

import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import kotlin.reflect.KMutableProperty0

fun createConfigScreen(parent: Screen? = null): Screen {
    val config = ConfigManager.getConfig()

    return YetAnotherConfigLib.createBuilder()
        .title(Component.translatable("elytrahud.config.title"))
        .save(ConfigManager::save)
        .category(ConfigCategory.createBuilder()
            .name(Component.translatable("elytrahud.config.category.general"))
            .addBooleanOption(
                name = "elytrahud.config.option.modEnabled",
                description = "elytrahud.config.option.modEnabled.description",
                property = config::modEnabled,
                defaultValue = true
            )
            .addBooleanOption(
                name = "elytrahud.config.option.alwaysDisplayHud",
                description = "elytrahud.config.option.alwaysDisplayHud.description",
                property = config::alwaysDisplayHud,
                defaultValue = false
            )
            .build()
        )
        .category(ConfigCategory.createBuilder()
            .name(Component.translatable("elytrahud.config.category.hudElements"))
            .addBooleanOption("elytrahud.config.option.showTitles", "elytrahud.config.option.showTitles.description", config::renderTitles, true)
            .addBooleanOption("elytrahud.config.option.showValues", "elytrahud.config.option.showValues.description", config::renderValues, true)
            .addBooleanOption("elytrahud.config.option.airspeed", "elytrahud.config.option.airspeed.description", config::renderAirspeed, true)
            .addBooleanOption("elytrahud.config.option.horizon", "elytrahud.config.option.horizon.description", config::renderHorizon, true)
            .addBooleanOption("elytrahud.config.option.durability", "elytrahud.config.option.durability.description", config::renderDurability, true)
            .addBooleanOption("elytrahud.config.option.altitude", "elytrahud.config.option.altitude.description", config::renderAltitude, true)
            .addBooleanOption("elytrahud.config.option.verticalSpeed", "elytrahud.config.option.verticalSpeed.description", config::renderVertical, true)
            .addBooleanOption("elytrahud.config.option.compass", "elytrahud.config.option.compass.description", config::renderCompass, true)
            .build()
        )
        .build()
        .generateScreen(parent)
}

private fun ConfigCategory.Builder.addBooleanOption(
    name: String,
    description: String,
    property: KMutableProperty0<Boolean>,
    defaultValue: Boolean
): ConfigCategory.Builder {
    return this.option(
        Option.createBuilder<Boolean>()
            .name(Component.translatable(name))
            .description(OptionDescription.of(Component.translatable(description)))
            .binding(
                defaultValue,
                { property.get() },
                { property.set(it) }
            )
            .listener { _, value ->
                property.set(value)
                ConfigManager.save()
                syncCommonConfig()
            }
            .controller { opt ->
                BooleanControllerBuilder.create(opt)
                    .yesNoFormatter()
                    .coloured(true)
            }
            .build()
    )
}

private fun syncCommonConfig() {
    val yaclConfig = ConfigManager.getConfig()
    val commonConfig = Common.CONFIG ?: return
    
    commonConfig.modEnabled = yaclConfig.modEnabled
    commonConfig.renderTitles = yaclConfig.renderTitles
    commonConfig.renderValues = yaclConfig.renderValues
    commonConfig.renderAirspeed = yaclConfig.renderAirspeed
    commonConfig.renderHorizon = yaclConfig.renderHorizon
    commonConfig.renderDurability = yaclConfig.renderDurability
    commonConfig.renderAltitude = yaclConfig.renderAltitude
    commonConfig.renderVertical = yaclConfig.renderVertical
    commonConfig.renderCompass = yaclConfig.renderCompass
    commonConfig.alwaysDisplayHud = yaclConfig.alwaysDisplayHud
}
