package inorganic.elytrahud

import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.*
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

fun createConfigScreen(parent: Screen? = null): Screen {
    val config = ConfigManager.getConfig()
    
    return YetAnotherConfigLib.createBuilder()
        .title(Component.literal("ElytraHud Settings"))
        .save(ConfigManager::save)
        .category(
            ConfigCategory.createBuilder()
                .name(Component.literal("General"))
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Mod Enabled"))
                        .description(OptionDescription.of(Component.literal("Enable or disable the entire mod")))
                        .binding(
                            true,
                            { config.modEnabled },
                            { config.modEnabled = it }
                        )
                        .controller { opt ->
                            BooleanControllerBuilder.create(opt)
                                .yesNoFormatter()
                                .coloured(true)
                        }
                        .build()
                )
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Always Display HUD"))
                        .description(OptionDescription.of(Component.literal("Show HUD even when not flying")))
                        .binding(
                            false,
                            { config.alwaysDisplayHud },
                            { config.alwaysDisplayHud = it }
                        )
                        .controller { opt ->
                            BooleanControllerBuilder.create(opt)
                                .yesNoFormatter()
                                .coloured(true)
                        }
                        .build()
                )
                .build()
        )
        .category(
            ConfigCategory.createBuilder()
                .name(Component.literal("HUD Elements"))
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Show Titles"))
                        .description(OptionDescription.of(Component.literal("Show element titles")))
                        .binding(
                            true,
                            { config.renderTitles },
                            { config.renderTitles = it }
                        )
                        .controller { opt ->
                            BooleanControllerBuilder.create(opt)
                                .yesNoFormatter()
                                .coloured(true)
                        }
                        .build()
                )
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Show Values"))
                        .description(OptionDescription.of(Component.literal("Show numerical values")))
                        .binding(
                            true,
                            { config.renderValues },
                            { config.renderValues = it }
                        )
                        .controller { opt ->
                            BooleanControllerBuilder.create(opt)
                                .yesNoFormatter()
                                .coloured(true)
                        }
                        .build()
                )
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Airspeed"))
                        .description(OptionDescription.of(Component.literal("Show airspeed indicator")))
                        .binding(
                            true,
                            { config.renderAirspeed },
                            { config.renderAirspeed = it }
                        )
                        .controller { opt ->
                            BooleanControllerBuilder.create(opt)
                                .yesNoFormatter()
                                .coloured(true)
                        }
                        .build()
                )
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Horizon"))
                        .description(OptionDescription.of(Component.literal("Show horizon indicator")))
                        .binding(
                            true,
                            { config.renderHorizon },
                            { config.renderHorizon = it }
                        )
                        .controller { opt ->
                            BooleanControllerBuilder.create(opt)
                                .yesNoFormatter()
                                .coloured(true)
                        }
                        .build()
                )
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Durability"))
                        .description(OptionDescription.of(Component.literal("Show elytra durability")))
                        .binding(
                            true,
                            { config.renderDurability },
                            { config.renderDurability = it }
                        )
                        .controller { opt ->
                            BooleanControllerBuilder.create(opt)
                                .yesNoFormatter()
                                .coloured(true)
                        }
                        .build()
                )
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Altitude"))
                        .description(OptionDescription.of(Component.literal("Show altitude indicator")))
                        .binding(
                            true,
                            { config.renderAltitude },
                            { config.renderAltitude = it }
                        )
                        .controller { opt ->
                            BooleanControllerBuilder.create(opt)
                                .yesNoFormatter()
                                .coloured(true)
                        }
                        .build()
                )
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Vertical Speed"))
                        .description(OptionDescription.of(Component.literal("Show vertical speed indicator")))
                        .binding(
                            true,
                            { config.renderVertical },
                            { config.renderVertical = it }
                        )
                        .controller { opt ->
                            BooleanControllerBuilder.create(opt)
                                .yesNoFormatter()
                                .coloured(true)
                        }
                        .build()
                )
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Compass"))
                        .description(OptionDescription.of(Component.literal("Show compass")))
                        .binding(
                            true,
                            { config.renderCompass },
                            { config.renderCompass = it }
                        )
                        .controller { opt ->
                            BooleanControllerBuilder.create(opt)
                                .yesNoFormatter()
                                .coloured(true)
                        }
                        .build()
                )
                .build()
        )
        .build()
        .generateScreen(parent)
}