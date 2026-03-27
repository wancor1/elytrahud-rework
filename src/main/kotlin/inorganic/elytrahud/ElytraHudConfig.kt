package inorganic.elytrahud

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.nio.file.Files
import java.nio.file.Path

data class ElytraHudConfig(
    @JvmField var modEnabled: Boolean = true,
    @JvmField var renderTitles: Boolean = true,
    @JvmField var renderValues: Boolean = false,
    @JvmField var renderAirspeed: Boolean = true,
    @JvmField var renderHorizon: Boolean = true,
    @JvmField var renderDurability: Boolean = true,
    @JvmField var renderAltitude: Boolean = true,
    @JvmField var renderVertical: Boolean = true,
    @JvmField var renderCompass: Boolean = true,
    @JvmField var alwaysDisplayHud: Boolean = false
)

object ConfigManager {
    private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()
    private val CONFIG_PATH: Path = configPath("elytrahud.json")

    private var _config: ElytraHudConfig? = null

    @JvmStatic
    fun getConfig(): ElytraHudConfig {
        if (_config == null) {
            _config = load()
        }
        return _config!!
    }

    @JvmStatic
    fun save() {
        Files.writeString(CONFIG_PATH, GSON.toJson(getConfig()))
    }

    private fun load(): ElytraHudConfig {
        return if (Files.exists(CONFIG_PATH)) {
            GSON.fromJson(Files.readString(CONFIG_PATH), ElytraHudConfig::class.java)
        } else {
            ElytraHudConfig()
        }
    }

    private fun configPath(name: String): Path {
        return net.fabricmc.loader.api.FabricLoader.getInstance().configDir.resolve(name)
    }
}
