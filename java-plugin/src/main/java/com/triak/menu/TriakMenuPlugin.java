package com.triak.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.ServerLinks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * TriakMenu - DonutSMP style ESC button
 * Paper 1.21.11 | Java version (optional, Skript version is main)
 * 
 * Features:
 * - ESC button via Dialogs API (bootstrap)
 * - ServerLinks fallback
 * - Custom GUI menu
 * - Geyser/Floodgate support
 */
public class TriakMenuPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Save config
        saveDefaultConfig();

        // Register events
        Bukkit.getPluginManager().registerEvents(this, this);

        // Setup ServerLinks (fallback for 1.21-1.21.5, still works in 1.21.11)
        setupServerLinks();

        // Register command
        getCommand("triakmenu").setExecutor(this);

        getLogger().info("TriakMenu enabled! ESC button: Triak Menu | Paper 1.21.11");
        getLogger().info("Datapack OR bootstrap dialog should make button appear in ESC");
    }

    private void setupServerLinks() {
        try {
            ServerLinks links = Bukkit.getServer().getServerLinks();
            // Clear old? Keep built-in
            String name = getConfig().getString("esc-button.name", "Triak Menu");
            String url = getConfig().getString("server-links.url", "https://discord.gg/triak");

            // Add custom link
            links.addLink(name, URI.create(url));
            // Add with type
            links.addLink(ServerLinks.Type.WEBSITE, URI.create(url));

            getLogger().info("ServerLinks registered: " + name + " -> " + url);
        } catch (Exception e) {
            getLogger().warning("Failed to setup ServerLinks: " + e.getMessage());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            // Check if Bedrock via Floodgate
            boolean isBedrock = isBedrockPlayer(player);
            if (isBedrock) {
                player.sendMessage(Component.text("[Triak] Bedrock detected! Use /triakmenu for menu", NamedTextColor.GRAY));
            } else {
                player.sendMessage(Component.text("[Triak] Press ESC to see Triak Menu button! (DonutSMP style)", NamedTextColor.GRAY));
            }
        }, 60L);
    }

    private boolean isBedrockPlayer(Player player) {
        // Try Floodgate API via reflection (avoid hard dependency)
        try {
            Class<?> apiClass = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            Object api = apiClass.getMethod("getInstance").invoke(null);
            Boolean isFloodgate = (Boolean) apiClass.getMethod("isFloodgatePlayer", java.util.UUID.class).invoke(api, player.getUniqueId());
            return isFloodgate != null && isFloodgate;
        } catch (Exception ignored) {
            // Fallback to prefix
            return player.getName().startsWith(".");
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this!");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("open") || args[0].equalsIgnoreCase("gui")) {
            openMainMenu(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("dialog")) {
            // Open dialog via command - uses Paper's dialog API
            player.showDialog(Bukkit.getServer().getServerLinks().getLinks().isEmpty() ? null : null);
            // Actually open our registered dialog
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dialog show " + player.getName() + " triak:main");
            return true;
        }

        // Handle subcommands - fully customizable
        String action = args[0].toLowerCase();
        switch (action) {
            case "shop" -> player.performCommand("shop");
            case "warps" -> player.performCommand("warps");
            case "stats" -> player.sendMessage(Component.text("Your stats: ...", NamedTextColor.GOLD));
            case "settings" -> player.performCommand("settings");
            case "pay" -> player.performCommand("pay");
            case "links" -> player.sendMessage(Component.text("Discord: https://discord.gg/triak", NamedTextColor.AQUA));
            case "close" -> player.closeInventory();
            default -> player.performCommand(String.join(" ", args));
        }
        return true;
    }

    private void openMainMenu(Player player) {
        String title = getConfig().getString("menu.title", "Triak Menu");
        int rows = getConfig().getInt("menu.rows", 3);
        Inventory inv = Bukkit.createInventory(null, rows * 9, Component.text(title));

        // Example items - fully customizable via config
        // Slot 10 - Shop
        inv.setItem(10, createItem(Material.EMERALD, "&a&lShop", Arrays.asList("&7Click to open shop", "", "&e▶ Click")));
        inv.setItem(11, createItem(Material.ENDER_PEARL, "&b&lWarps", Arrays.asList("&7Teleport around")));
        inv.setItem(12, createItem(Material.PLAYER_HEAD, "&6&lMy Stats", Arrays.asList("&7View stats")));
        inv.setItem(13, createItem(Material.CHEST, "&6&lTriak Menu", Arrays.asList("&7DonutSMP style ESC button!")));
        inv.setItem(14, createItem(Material.COMPARATOR, "&e&lSettings", Arrays.asList("&7Configure")));
        inv.setItem(15, createItem(Material.GOLD_INGOT, "&6&lPay", Arrays.asList("&7Pay players")));
        inv.setItem(16, createItem(Material.NETHER_STAR, "&d&lLeaderboards", Arrays.asList("&7Top players")));

        // Fill rest with glass
        ItemStack glass = createItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) inv.setItem(i, glass);
        }

        player.openInventory(inv);
    }

    private ItemStack createItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name.replace("&", "§")));
            if (lore != null) {
                meta.lore(lore.stream().map(l -> Component.text(l.replace("&", "§"))).toList());
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
