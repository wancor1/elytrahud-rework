# ElytraHud (MFS 2020)

A Fabric mod for Minecraft that adds a Microsoft Flight Simulator-style HUD for elytra flight.

![License](https://img.shields.io/badge/license-MIT-blue)

## Features

- **Microsoft Flight Simulator-style HUD**: Professional aviation-style display for elytra flight
- **Customizable HUD Elements**: Toggle individual HUD components on/off
- **ModMenu Integration**: Configure through in-game Mod Menu
- **Always Display Option**: Show HUD even when not flying (optional)

## HUD Components

The mod displays the following flight information when using an elytra:

- **Air Speed**: Current flying speed
- **Altitude**: Height above ground/sea level
- **Vertical Speed**: Rate of ascent/descent
- **Durability**: Elytra durability remaining
- **Horizon Line**: Visual horizon indicator
- **Compass**: Directional heading
- **Pitch/Roll Indicators**: Aircraft-style attitude indicators

### Available Settings

| Setting | Description | Default |
|---------|-------------|---------|
| `modEnabled` | Enable/disable the mod | true |
| `alwaysDisplayHud` | Show HUD even when not flying | false |
| `renderTitles` | Show HUD element titles | true |
| `renderValues` | Show numerical values | true |
| `renderAirspeed` | Show air speed indicator | true |
| `renderHorizon` | Show horizon line | true |
| `renderDurability` | Show elytra durability | true |
| `renderAltitude` | Show altitude indicator | true |
| `renderVertical` | Show vertical speed indicator | true |
| `renderCompass` | Show compass | true |

## Compatibility

- **Minecraft**: 26.1
- **Loader**: Fabric Loader ≥0.18.4
- **Java**: Java 25+
- **Dependencies**:
  - Fabric API
  - Fabric Language Kotlin
  - YACL (Yet Another Config Lib) for configuration GUI
  - Mod Menu (optional, for in-game configuration)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits

- **Author**: inorganic
- **Original Author**: jewtvet
- **Original Concept**: Microsoft Flight Simulator 2020 HUD

## Links

- [GitHub Repository](https://github.com/wancor1/elytrahud-rework)
- [Modrinth Page](https://modrinth.com/mod/elytra-hud-rework)
- [Issue Tracker](https://github.com/wancor1/elytrahud-rework/issues)
