# No Tree Punching Compatibility (NTPC)

Ever found a beautiful modded biome but realized you couldn't get **Plant Fibers** from the grass? This mod bridges the gap between **No Tree Punching** and modded flora!

## 🌿 What does this mod do?

By default, **No Tree Punching** only allows you to get Plant Fibers from vanilla grass. This mod dynamically scans modded blocks (like those in **Regions Unexplored**) and adds the appropriate Plant Fiber drops when broken with a knife.

#### ✨ Features:

*   **Dynamic Support:** Automatically works with most modded grasses, ferns, and flowers.
*   **Balanced:** Comes with a default **15% drop chance** (configurable!) to keep the early game challenging.
*   **Universal Compatibility:** Works with almost all modded biomes.

#### ⚙️ Configuration:

NTPC generates a config file at `config/ntpc.json`. You can:

*   **Adjust the Drop Chance:** Change the percentage from 15% to whatever fits your pack.
*   **Manual Overrides:** Add specific block IDs that the mod might have missed.
*   **Blacklist:** Prevent specific plants from dropping fibers.

#### 🛠️ Requirements:

*   **No Tree Punching** (Required)
*   **Fabric API** (Required)

#### 📜 FAQ:

*   **Can I use this in my modpack?** Yes!
*   **Does it work with other mods?** If the mod follows standard naming (contains "grass", "fern", etc.), it works automatically! If not, just add the ID to the config.