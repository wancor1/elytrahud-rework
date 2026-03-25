package jewtvet.elytrahud

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip

@Config(name = "elytrahud")
class ElytraHudConfig : ConfigData {
    @Tooltip
    @JvmField
    var modEnabled = true

    @Tooltip
    @JvmField
    var thirdPersonEnabled = false

    @Tooltip
    @JvmField
    var highFovEnabled = false

    @Tooltip
    @JvmField
    var pumpkinEnabled = false

    @Tooltip
    @JvmField
    var disableRiptideAnim = false

    @Tooltip
    @JvmField
    var renderTitles = true

    @Tooltip
    @JvmField
    var renderValues = false

    @Tooltip
    @JvmField
    var renderAirspeed = true

    @Tooltip
    @JvmField
    var renderHorizon = true

    @Tooltip
    @JvmField
    var renderDurability = true

    @Tooltip
    @JvmField
    var renderAltitude = true

    @Tooltip
    @JvmField
    var renderVertical = true

    @Tooltip
    @JvmField
    var renderCompass = true

    @Tooltip
    @BoundedDiscrete(min = 0L, max = 100L)
    @JvmField
    var compassDefaultX = 0

    @Tooltip
    @BoundedDiscrete(min = 30L, max = 110L)
    @JvmField
    var defaultFov = 32
}
