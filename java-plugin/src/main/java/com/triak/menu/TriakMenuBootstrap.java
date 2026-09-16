package com.triak.menu;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.DialogKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class TriakMenuBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // Register dialog for ESC pause menu - DonutSMP style
        // This makes "Triak Menu" button appear in ESC > Pause menu
        context.getLifecycleManager().registerEventHandler(RegistryEvents.DIALOG.compose()
            .newHandler(event -> {
                // Main dialog - appears in ESC menu via pause_screen_additions tag OR directly registered
                event.registry().register(
                    DialogKeys.create(Key.key("triak:main")),
                    builder -> builder
                        .base(DialogBase.builder(Component.text("Triak Menu", NamedTextColor.GOLD).decorate(TextDecoration.BOLD))
                            .body(List.of(
                                DialogBody.item(new ItemStack(Material.CHEST))
                                    .description(DialogBody.plainMessage(
                                        Component.text("DonutSMP style ESC menu\nClick a button below!", NamedTextColor.GRAY)
                                    )).build(),
                                DialogBody.plainMessage(Component.text("Welcome to Triak! Select an option:", NamedTextColor.YELLOW))
                            ))
                            .canCloseWithEscape(true)
                            .build()
                        )
                        .type(DialogType.multiAction(List.of(
                            ActionButton.builder(Component.text("Shop", NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
                                .tooltip(Component.text("Open server shop"))
                                .width(150)
                                .action(DialogAction.customClick(Key.key("triak:shop"), null))
                                .build(),
                            ActionButton.builder(Component.text("Warps", NamedTextColor.AQUA).decorate(TextDecoration.BOLD))
                                .tooltip(Component.text("Teleport around"))
                                .width(150)
                                .action(DialogAction.customClick(Key.key("triak:warps"), null))
                                .build(),
                            ActionButton.builder(Component.text("My Stats", NamedTextColor.GOLD).decorate(TextDecoration.BOLD))
                                .width(150)
                                .action(DialogAction.customClick(Key.key("triak:stats"), null))
                                .build(),
                            ActionButton.builder(Component.text("Settings", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD))
                                .width(150)
                                .action(DialogAction.customClick(Key.key("triak:settings"), null))
                                .build(),
                            ActionButton.builder(Component.text("Pay Player", NamedTextColor.GOLD))
                                .width(150)
                                .action(DialogAction.customClick(Key.key("triak:pay"), null))
                                .build(),
                            ActionButton.builder(Component.text("Discord & Links", NamedTextColor.BLUE))
                                .width(150)
                                .action(DialogAction.customClick(Key.key("triak:links"), null))
                                .build()
                        )).columns(2).build())
                );

                // Shop sub-dialog
                event.registry().register(
                    DialogKeys.create(Key.key("triak:shop")),
                    builder -> builder
                        .base(DialogBase.builder(Component.text("Shop", NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
                            .body(List.of(DialogBody.plainMessage(Component.text("Server shop - Buy items!"))))
                            .build()
                        )
                        .type(DialogType.multiAction(List.of(
                            ActionButton.builder(Component.text("Buy Blocks"))
                                .action(DialogAction.runCommand("shop blocks")).build(),
                            ActionButton.builder(Component.text("Buy Tools"))
                                .action(DialogAction.runCommand("shop tools")).build(),
                            ActionButton.builder(Component.text("Back", NamedTextColor.GRAY))
                                .action(DialogAction.showDialog(RegistryKey.DIALOG, Key.key("triak:main"))).build()
                        )).columns(2).build())
                );
            })
        );
    }
}
