# Quick Install Guide - Triak Menu ESC Button

## For Paper 1.21.11 Server

### Step 1: Dependencies
Download and put in `plugins/`:
- Skript 2.10+ (https://github.com/SkriptLang/Skript/releases)
- SkBee 3.16.0+ (https://modrinth.com/plugin/skbee)
- Optional: skript-reflect (https://github.com/TPGamesNL/skript-reflect)
- Optional: Floodgate + Geyser for Bedrock

Restart server after installing.

### Step 2: Skript Files
Copy from `skript/` folder:
- `TriakMenu.sk`
- `TriakMenu-Config.sk`
- `TriakBedrock.sk`
- `TriakServerLinks.sk` (optional)

To: `plugins/Skript/scripts/`

Then run in console or in-game (as op):
```
/sk reload TriakMenu
```

If no errors, you're good!

### Step 3: Datapack (CRITICAL for ESC button)

The ESC button will NOT show without this datapack!

Copy folder `triak_esc_button` from `datapack/` folder
To: `world/datapacks/`

Structure should be:
```
world/
  datapacks/
    triak_esc_button/
      pack.mcmeta
      data/
        minecraft/tags/dialog/pause_screen_additions.json
        triak/dialog/main.json
```

Then either:
- Restart server (recommended)
- Or run:
```
/datapack enable "file/triak_esc_button"
```

Verify:
```
/datapack list
```
Should show `triak_esc_button` enabled.

### Step 4: Test

1. Join server
2. Press ESC
3. You should see "Triak Menu" button!

If you don't see it:
- Check Paper version is 1.21.6+ (`/version`)
- Check datapack enabled (`/datapack list`)
- Check Skript loaded (`/sk list`)
- Check console for errors
- Try restart, not just reload (dialogs registry needs restart)

### Step 5: Customize

Edit `TriakMenu.sk` options at top:
- `esc-button-name` - ESC button name
- `menu-title` - GUI title
- `menu-rows` - 1-6

Edit menu buttons in `on load:` section.

Edit dialog buttons in same section.

Edit datapack `main.json` for ESC button external_title and icon.

See README.md for full customization guide!

---

## Troubleshooting

**ESC button not showing?**
- Paper 1.21.6+ required for Dialogs
- Datapack must be in `world/datapacks/` (or your main world folder, check server.properties level-name)
- Must restart or enable datapack
- If you have multiple worlds, put datapack in each world folder or use global datapack folder (Paper: `world/datapacks/` is usually enough)

**Skript errors?**
- Update Skript and SkBee to latest
- Check you have Paper, not Spigot (SkBee needs Paper for dialogs)
- Run `/sk reload TriakMenu` and check console

**Bedrock players?**
- Install Geyser + Floodgate
- Geyser 2.5+ translates dialogs automatically
- Bedrock can always use `/triakmenu` command

---

## For Developers

You can also implement ESC button via Java plugin using Paper Dialog API:

```java
// In bootstrap
context.getLifecycleManager().registerEventHandler(RegistryEvents.DIALOG.compose()
    .newHandler(event -> event.registry().register(
        DialogKeys.create(Key.key("triak:main")),
        builder -> builder
            .base(DialogBase.builder(Component.text("Triak Menu")).build())
            .type(DialogType.multiAction(...))
    )));
```

But Skript + Datapack method is easier and doesn't require coding!

---

Done! Enjoy DonutSMP style ESC menu!
