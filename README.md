# Triak Menu - DonutSMP Style ESC / Pause Menu Button

### Paper 1.21.11 | Skript + SkBee | No Mod Required | Bedrock Compatible

A fully customizable Minecraft Paper plugin **Skript** that adds a custom button to the player's **ESC / Pause menu**, just like DonutSMP. Works without requiring players to install any client-side mod, and supports both Java and Geyser/Floodgate Bedrock players.

---

## ✨ Features

- **✅ ESC Button Visible in Pause Menu** - Custom "Triak Menu" button appears when you press ESC (DonutSMP style)
- **✅ Fully Customizable Name** - Change to "Triak Menu" or any name, colors, MiniMessage
- **✅ Configurable Icon** - Chest, Nether Star, or any item (shown in dialog body - Minecraft client supports it)
- **✅ Opens Custom Server Menu** - Clicking opens a native Minecraft dialog OR chest GUI menu
- **✅ No Client Mod Required** - Uses vanilla Minecraft 1.21.6+ Dialogs API + Paper ServerLinks API
- **✅ Java & Bedrock Support** - Geyser translates dialogs to Bedrock forms (2.5+), plus `/triakmenu` command for Bedrock
- **✅ Perform Commands** - Every button can run player commands, console commands, open URLs, or open sub-menus
- **✅ DonutSMP Style** - Same technique as DonutSMP: pause_screen_additions tag + run_command actions

---

## 📦 What's Included

```
plugins/
├── skript/
│   ├── TriakMenu.sk              # Main script - DonutSMP ESC button + GUI menu
│   ├── TriakMenu-Config.sk       # Easy config - customize without touching main
│   ├── TriakServerLinks.sk       # Fallback ServerLinks for 1.21-1.21.5
│   └── TriakBedrock.sk           # Geyser/Floodgate Bedrock support
└── datapack/
    └── triak_esc_button/
        ├── pack.mcmeta
        └── data/
            ├── minecraft/tags/dialog/pause_screen_additions.json
            └── triak/dialog/
                ├── main.json      # Main ESC dialog (Triak Menu button)
                ├── shop.json      # Sub-dialog example
                └── teleport.json  # Sub-dialog example
```

---

## 🚀 Installation (Paper 1.21.11)

### Requirements
- **Paper 1.21.11** (or 1.21.6+ for Dialogs, 1.21+ for ServerLinks)
- **Skript** 2.10+ - https://github.com/SkriptLang/Skript
- **SkBee** 3.16.0+ - https://modrinth.com/plugin/skbee
- **Optional**: `skript-reflect` for advanced ServerLinks & Floodgate detection
- **Optional**: `Floodgate` + `Geyser` for Bedrock support

### Steps

1. **Install Dependencies**
   ```
   Put Skript.jar and SkBee.jar in plugins/
   Restart server
   ```

2. **Install TriakMenu Skripts**
   ```
   Copy TriakMenu.sk, TriakMenu-Config.sk, TriakBedrock.sk
   to plugins/Skript/scripts/
   Then run: /sk reload TriakMenu
   ```

3. **Install Datapack (IMPORTANT for ESC button)**
   ```
   Copy folder triak_esc_button
   to world/datapacks/
   Then run:
   /datapack enable "file/triak_esc_button"
   OR restart server
   Check: /datapack list -> should show triak_esc_button enabled
   ```

4. **Test**
   - Join server
   - Press **ESC**
   - You should see **"Triak Menu"** button in pause menu!
   - Click it -> Opens native dialog with buttons
   - Buttons run `/triakmenu <action>` which opens GUI

---

## 🎮 How It Works

### Java Edition (1.21.11)
Minecraft 1.21.6 added **Dialogs API**. Paper 1.21.7+ exposes it.

1. **Datapack** registers dialog `triak:main` and adds it to tag `#minecraft:pause_screen_additions`
2. Minecraft automatically shows a button in ESC menu for each dialog in that tag
3. Button's `external_title` is what you see in ESC: **"Triak Menu"**
4. Clicking opens the dialog `triak:main` which has buttons like Shop, Warps, etc.
5. Each button uses `minecraft:run_command` to run `/triakmenu shop` etc.
6. Skript handles those commands and opens chest GUI or other dialogs

**This is exactly how DonutSMP does it!** (They use custom Dialogs API, not mods)

### Fallback (1.21 - 1.21.5)
Before 1.21.6, ESC menu had **ServerLinks**. Paper API `ServerLinks` allows adding links that appear in ESC > Server Links...

- Our `TriakServerLinks.sk` shows how to add ServerLinks (requires skript-reflect)
- Or use plugins like AdvancedLinks / mServerLinks
- ServerLinks only opens URLs, not commands - so Dialogs method is better for 1.21.11

### Bedrock (Geyser/Floodgate)
- **Geyser 2.5+** automatically translates Java dialogs to Bedrock forms!
- So Bedrock players pressing ESC (pause) will see a Bedrock form version of Triak Menu
- For older Geyser or if translation fails, Bedrock players can use `/triakmenu` command
- We also detect Bedrock via prefix `.` or Floodgate API and give compass item

---

## ⚙️ Customization

### Change ESC Button Name
**File:** `datapack/triak_esc_button/data/triak/dialog/main.json`
```json
{
  "external_title": {"text": "Your Custom Name", "color": "gold", "bold": true},
  "title": {"text": "Your Custom Name"}
}
```
Then reload datapack: `/datapack disable` + `/datapack enable` or restart.

**Also in Skript:** Edit `options: esc-button-name` in `TriakMenu.sk`

### Change Icon
**Datapack:** In `main.json`, find:
```json
"item": {"id": "minecraft:chest"}
```
Change to any item: `minecraft:nether_star`, `minecraft:emerald`, `minecraft:player_head`, etc.

**Skript GUI:** In `TriakMenu.sk` on load section, change material:
```skript
set {triak::menu::slot::13::material} to nether star
```

### Make Button Perform Commands

#### GUI Menu (Chest Inventory)
Edit `TriakMenu.sk` in `on load:` section:

```skript
# Slot 20 - Custom command
set {triak::menu::slot::20::material} to diamond
set {triak::menu::slot::20::name} to "&b&lMy Button"
set {triak::menu::slot::20::lore} to "&7Click to run command"
set {triak::menu::slot::20::action} to "command"  # or console, dialog, close, url
set {triak::menu::slot::20::value} to "warp spawn"  # command without /
set {triak::menu::slot::20::perm} to "triak.menu"

# Console command example:
set {triak::menu::slot::21::action} to "console"
set {triak::menu::slot::21::value} to "give {player} diamond 5"

# URL example:
set {triak::menu::slot::22::action} to "url"
set {triak::menu::slot::22::value} to "https://discord.gg/triak"
```

#### Dialog Menu (ESC Native Dialog)
Edit `TriakMenu.sk` variables + function `triak_handleAction`:

```skript
# In on load:
set {triak::dialog::8::label} to "&c&lHeal"
set {triak::dialog::8::tooltip} to "&7Click to heal"
set {triak::dialog::8::action} to "heal"
set {triak::dialog::8::perm} to "triak.menu.heal"

# In function triak_handleAction:
else if {_action} is "heal":
    make {_p} execute command "heal"
    send "&aHealed!" to {_p}
```

#### Datapack Dialog (Direct, no Skript)
Edit `main.json` actions:
```json
{
  "label": {"text": "Heal"},
  "action": {
    "type": "minecraft:run_command",
    "command": "heal"
  }
}
```
Or open URL:
```json
{
  "label": {"text": "Discord"},
  "action": {
    "type": "open_url",
    "url": "https://discord.gg/triak"
  }
}
```

### Add More Buttons
- **GUI**: Use new slot numbers 0-53 (for 6 rows). Edit `TriakMenu-Config.sk` for easy adding.
- **Dialog**: Add new entries in `main.json` actions array, or add to `{triak::dialog::*}` variables.

---

## 🔧 Commands & Permissions

### Commands
- `/triakmenu` - Open main GUI menu
- `/triakmenu dialog` - Open native dialog menu (DonutSMP style)
- `/triakmenu shop` - Open shop (customizable)
- `/triakmenu warps` - Warps menu
- `/triakmenu stats` - Stats dialog
- `/triakmenu settings` - Settings
- `/triakmenu pay` - Pay dialog with inputs
- `/triakmenu links` - Server links
- `/triakmenu reload` - Reload script (admin)
- `/triakmenu help` - Help

Aliases: `/tm`, `/trmenu`, `/escmenu`, `/pause`

### Permissions
- `triak.menu` - Use menu (default true)
- `triak.menu.shop` - Shop button
- `triak.menu.warps` - Warps button
- `triak.menu.settings` - Settings button
- `triak.menu.pay` - Pay button
- `triak.admin` - Reload
- `triak.esc` - See ESC button (optional, for per-player ESC button if you implement PlayerLinksSendEvent)

---

## 🌐 Geyser / Floodgate Bedrock Support

### How Bedrock Sees ESC Button
1. **Geyser 2.5+**: Translates Java dialogs to Bedrock forms automatically. When Java player sees ESC > Triak Menu > dialog, Bedrock sees a form popup!
2. **Fallback**: Bedrock players use `/triakmenu` command or compass item

### Setup for Bedrock
- Install Floodgate + Geyser
- Enable `enable-bedrock-support: true` in `TriakMenu.sk` options
- Bedrock players joining with prefix `.` (or detected via Floodgate API) get message and compass
- They can use `/triakmenu` or `/bedrockmenu`

### Custom Bedrock Form (Optional)
If you have Floodgate forms addon or SkBee with form support, edit `TriakBedrock.sk` to create custom forms.

---

## 📚 Advanced: SkBee Dialog Syntax

We use SkBee's Dialog API (3.16.0+) for native dialogs:

```skript
open multi action dialog to player:
    title: "Triak Menu"
    external_title: "Triak Menu"
    columns: 2
    body:
        add item body:
            item: chest
        add plain message body:
            contents: "Welcome!"
    actions:
        add dynamic action button:
            label: "Shop"
            id: "triak:menu_action"
            additions: nbt from "{action:""shop""}"
```

Handle clicks:
```skript
on custom click:
    if "%event-namespacedkey%" is "triak:menu_action":
        set {_action} to string tag "action" of event-nbt
        # Do something
```

See `TriakMenu.sk` for full examples.

---

## 🐛 Troubleshooting

**ESC button not showing?**
- Check Paper version is 1.21.6+ (for Dialogs) or 1.21+ (for ServerLinks)
- Check datapack is enabled: `/datapack list`
- Check file path: `world/datapacks/triak_esc_button/...` correct
- Try `/datapack disable "file/triak_esc_button"` then `/datapack enable`
- Restart server (dialogs.main needs restart sometimes)
- Check Skript loaded: `/sk list`

**Dialog not opening?**
- Check SkBee version 3.16.0+
- Check console for errors: `/sk reload TriakMenu`
- Make sure command `/triakmenu` works first

**Bedrock not seeing?**
- Update Geyser to 2.5+
- Bedrock pause menu is different - they need to use `/triakmenu` command
- Check Floodgate installed and prefix detection

**ServerLinks not showing?**
- ServerLinks only shows if you have links registered
- For 1.21.11, use Dialogs method (datapack) instead - it's better
- If you need ServerLinks, install skript-reflect and uncomment code in TriakServerLinks.sk

---

## 🎨 DonutSMP Style Explained

DonutSMP's custom ESC button:
- Uses **Dialogs API** with `#minecraft:pause_screen_additions` tag
- Button appears directly in ESC, not inside "Server Links..."
- Opens a `multi_action` dialog with many buttons
- Each button runs command like `/shop`, `/pay`, etc.
- No resource pack, no mod, pure vanilla client

Our implementation does **exactly same**:
- Datapack adds `triak:main` to `pause_screen_additions`
- ESC shows "Triak Menu"
- Click opens dialog with configurable buttons
- Buttons run `/triakmenu <action>` -> Skript opens GUI or runs command

You can even make it look identical to DonutSMP by editing colors, icons, and adding more sub-dialogs!

---

## 📝 License & Credits

- Created for Triak server
- Uses Paper Dialogs API & ServerLinks API
- Skript + SkBee community
- Inspired by DonutSMP ESC menu

---

## 💡 Ideas for Customization

- Add **Shop** with categories (Blocks, Tools, Food)
- Add **Warps** with teleport buttons
- Add **Leaderboards** showing top players
- Add **Settings** with toggles (using SkBee boolean inputs)
- Add **Pay** with text inputs for player name + amount
- Add **Discord** link that opens URL
- Add **Rules** dialog with plain message body
- Make buttons permission-based for ranks
- Add PlaceholderAPI support for stats

All possible with this system! Edit `TriakMenu.sk` and datapack JSONs.

---

Enjoy your DonutSMP-style ESC menu! 🎉
```

