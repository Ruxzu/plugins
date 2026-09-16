# Customization Guide - Make Button Perform Commands & More

This guide shows how to make your ESC button and menu perform ANY command, open menus, etc.

## 1. GUI Menu Buttons (Chest Inventory)

Location: `TriakMenu.sk` -> `on load:` section

### Structure:
```skript
set {triak::menu::slot::10::material} to emerald
set {triak::menu::slot::10::name} to "&a&lShop"
set {triak::menu::slot::10::lore} to "&7Click to open shop|&7|&e▶ Click"
set {triak::menu::slot::10::action} to "command"
set {triak::menu::slot::10::value} to "shop"
set {triak::menu::slot::10::perm} to "triak.menu.shop"
```

### Action Types:

#### `command` - Run as player
```skript
set {triak::menu::slot::10::action} to "command"
set {triak::menu::slot::10::value} to "warp spawn"
# Player will execute /warp spawn
```

#### `console` - Run as console (with {player} placeholder)
```skript
set {triak::menu::slot::10::action} to "console"
set {triak::menu::slot::10::value} to "give {player} diamond 5"
# Console runs: give TriakPlayer diamond 5
```

More console examples:
```skript
set {triak::menu::slot::11::value} to "eco give {player} 1000"
set {triak::menu::slot::12::value} to "kit daily {player}"
set {triak::menu::slot::13::value} to "lp user {player} parent set vip"
```

#### `dialog` - Open another dialog/menu (handled in function)
```skript
set {triak::menu::slot::10::action} to "dialog"
set {triak::menu::slot::10::value} to "stats"
# Then in function triak_handleAction, add handling for "stats"
```

#### `close` - Close menu
```skript
set {triak::menu::slot::10::action} to "close"
```

#### `url` - Send link
```skript
set {triak::menu::slot::10::action} to "url"
set {triak::menu::slot::10::value} to "https://discord.gg/triak"
```

### Add New Slot:
Slots 0-26 for 3 rows, 0-53 for 6 rows.
```skript
# Slot 20 - Free example
set {triak::menu::slot::20::material} to nether star
set {triak::menu::slot::20::name} to "&d&lMy Button"
set {triak::menu::slot::20::lore} to "&7Description|&eClick!"
set {triak::menu::slot::20::action} to "command"
set {triak::menu::slot::20::value} to "heal"
set {triak::menu::slot::20::perm} to "triak.menu.heal"
```

---

## 2. Dialog Menu (ESC Native Dialog - SkBee)

Location: `TriakMenu.sk` -> `on load:` + `function triak_handleAction`

### Define Button:
```skript
set {triak::dialog::8::label} to "&c&lHeal Me"
set {triak::dialog::8::tooltip} to "&7Click to heal yourself"
set {triak::dialog::8::action} to "heal"
set {triak::dialog::8::perm} to "triak.menu.heal"
```

### Handle Action:
In `function triak_handleAction(p: player, action: string):`
```skript
else if {_action} is "heal":
    make {_p} execute command "heal"
    send "&aHealed!" to {_p}

else if {_action} is "feed":
    feed {_p}
    send "&aFed!" to {_p}

else if {_action} is "kitpvp":
    execute console command "kit pvp %{_p}%"
```

You can make it do ANYTHING Skript can do!

### Advanced: Dynamic with NBT
```skript
open multi action dialog to {_p}:
    title: "My Menu"
    actions:
        set {_n} to empty nbt compound
        set string tag "mydata" of {_n} to "somevalue"
        add dynamic action button:
            label: "Click"
            id: "triak:myaction"
            additions: {_n}

on custom click:
    if "%event-namespacedkey%" is "triak:myaction":
        set {_data} to string tag "mydata" of event-nbt
        send "Data: %{_data}%" to player
```

---

## 3. Datapack Dialog (Direct JSON, No Skript Needed)

Location: `datapack/triak_esc_button/data/triak/dialog/main.json`

### Run Command:
```json
{
  "label": {"text": "Heal"},
  "action": {
    "type": "minecraft:run_command",
    "command": "heal"
  }
}
```

### Open URL:
```json
{
  "label": {"text": "Discord"},
  "action": {
    "type": "open_url",
    "url": "https://discord.gg/triak"
  }
}
```

### Show Another Dialog:
```json
{
  "label": {"text": "Shop"},
  "action": {
    "type": "minecraft:show_dialog",
    "dialog": "triak:shop"
  }
}
```

### Change Icon:
```json
{
  "type": "minecraft:item",
  "item": {
    "id": "minecraft:nether_star"
  }
}
```
Change `minecraft:chest` to any item ID.

### Add Body Text:
```json
{
  "type": "minecraft:plain_message",
  "contents": {"text": "Welcome!", "color": "gold"}
}
```

---

## 4. Make ESC Button Itself Customizable

The ESC button text is defined in two places:

1. **Datapack**: `main.json` -> `external_title`
   This is what shows in ESC menu.
   ```json
   "external_title": {"text": "Triak Menu", "color": "gold", "bold": true}
   ```

2. **Skript**: `options: esc-button-name`
   This is used for messages and dialog titles.

Change both to keep consistent!

### MiniMessage Support:
If you have MiniMessage via SkBee or Paper, you can use:
```json
"external_title": "<gold><bold>Triak Menu</bold></gold>"
```
Or in Skript, use text components.

---

## 5. Permission-Based Buttons

Each button has `::perm` - only players with that permission see it.

```skript
set {triak::menu::slot::10::perm} to "triak.vip"
# Only VIP sees this button

set {triak::dialog::1::perm} to "triak.admin"
# Only admin sees in ESC dialog
```

You can use LuckPerms to assign permissions.

---

## 6. Icon Customization

### GUI Menu:
Use any Material:
- `emerald`, `diamond`, `nether star`, `player head`, `chest`, `ender pearl`, `book`, `compass`, etc.
- For custom heads: Use SkBee head syntax or give player head with texture

### Dialog:
Use any Minecraft item ID:
- `minecraft:chest`, `minecraft:emerald`, `minecraft:player_head`, `minecraft:nether_star`
- For player head with custom texture, use components

Example with custom head in dialog:
```json
{
  "type": "minecraft:item",
  "item": {
    "id": "minecraft:player_head",
    "components": {
      "minecraft:profile": {
        "properties": [{
          "name": "textures",
          "value": "base64texture"
        }]
      }
    }
  }
}
```

---

## 7. Examples - Ready to Use

### Example: Warp Menu
```skript
# In on load:
set {triak::menu::slot::30::material} to compass
set {triak::menu::slot::30::name} to "&aSpawn"
set {triak::menu::slot::30::lore} to "&7Go to spawn"
set {triak::menu::slot::30::action} to "command"
set {triak::menu::slot::30::value} to "warp spawn"
set {triak::menu::slot::30::perm} to "triak.menu"

set {triak::menu::slot::31::material} to grass block
set {triak::menu::slot::31::name} to "&aWild"
set {triak::menu::slot::31::lore} to "&7Random teleport"
set {triak::menu::slot::31::action} to "command"
set {triak::menu::slot::31::value} to "wild"
set {triak::menu::slot::31::perm} to "triak.menu"
```

### Example: Kit with Cooldown Check
In `triak_handleAction`:
```skript
else if {_action} is "dailykit":
    if {kit::daily::%uuid of {_p}%} is set:
        if difference between {kit::daily::%uuid of {_p}%} and now is less than 24 hours:
            send "&cYou already claimed daily kit! Wait 24h" to {_p}
            stop
    execute console command "kit daily %{_p}%"
    set {kit::daily::%uuid of {_p}%} to now
```

### Example: Open URL + Command Combo
```skript
else if {_action} is "discord":
    send "&7Discord: &9https://discord.gg/triak" to {_p}
    # Also try to open URL via dialog
    open multi action dialog to {_p}:
        title: "Discord"
        actions:
            add static action button:
                label: "Open Discord"
                action: open url "https://discord.gg/triak"
```

---

## 8. Geyser/Bedrock Customization

Bedrock players use same menu but via forms (if Geyser 2.5+).

You can detect Bedrock and give different menu:
```skript
function triak_handleAction(p: player, action: string):
    if {triak::bedrock::%{_p}%} is set:
        # Bedrock specific handling
        if {_action} is "shop":
            send "&7Opening shop for Bedrock..." to {_p}
            # Maybe use different command that works better on Bedrock
            make {_p} execute command "shop bedrock"
            stop
    # Normal Java handling
    ...
```

---

That's it! You can make the ESC button do literally anything.

- Run commands
- Give items
- Teleport
- Open other menus
- Open URLs
- Run console commands with {player} placeholder
- Check permissions
- Use variables, cooldowns, economy, etc.

Fully customizable like DonutSMP!
