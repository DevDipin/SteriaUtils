package net.helydev.com;


import net.helydev.com.commands.LootcrateCommand;
import net.helydev.com.commands.SteriaUtilsCommand;
import net.helydev.com.commands.VouchersCommand;
import net.helydev.com.commands.misc.*;
import net.helydev.com.configs.Config;
import net.helydev.com.listeners.LootCrateListener;
import net.helydev.com.listeners.SafetyListener;
import net.helydev.com.listeners.SteriaListener;
import net.helydev.com.listeners.VouchersListener;
import net.helydev.com.listeners.advanced.AdvancedAntiGlitchListener;
import net.helydev.com.listeners.advanced.AdvancedPearlGlitchListener;
import net.helydev.com.utils.Cooldowns;
import net.helydev.com.utils.chat.ChatUtil;
import net.helydev.com.utils.commands.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SteriaUtils extends JavaPlugin {

    private static SteriaUtils plugin;

    private Config voucherConfig;


    public static SteriaUtils getPlugin() {
        return plugin;
    }


    /**
     * When the core first loads...
     */

    @Override
    public void onEnable() {
        plugin = this;
        //START OF LOADING MESSAGE
        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&7&l&m---------------------------------");
        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&b&lSteria Utils");
        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&7");
        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&7The server has registered &aSteria Utils&7.");
        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&7Plugin made by &aDevDipin&7.");
        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&7&l&m---------------------------------");
        //END OF LOADING MESSAGE
        Cooldowns.createCooldown("voucher");
        SteriaUtils.getPlugin().TipsMessager();
        SteriaUtils.getPlugin().saveDefaultConfig();
        SteriaUtils.getPlugin().reloadConfig();
        SteriaUtils.getPlugin().registerconfig();
        SteriaUtils.getPlugin().registervouchers();
        SteriaUtils.getPlugin().registermanagers();
        SteriaUtils.getPlugin().registerCommand();
        SteriaUtils.getPlugin().registerListeners();
    }

    /**
     * On plugin disable...
     */

    @Override
    public void onDisable() {
    }

    public void registerconfig() {
        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&b[SteriaUtils] &aRegistering &a&lconfig.yml&a..");
    }

    /**
     * Registering config.yml...
     */

    public void registervouchers() {
        this.voucherConfig=new Config(SteriaUtils.getPlugin(), "vouchers", SteriaUtils.getPlugin().getDataFolder().getAbsolutePath());
        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&b[SteriaUtils] &aRegistering &a&lvouchers.yml&a..");
    }

    /**
     * Registering managers...
     */

    public void registermanagers() {
    }

    /**
     * Registering commands...
     */

    public void registerCommand() {
        CommandFramework commandFramework = new CommandFramework(this);
        commandFramework.registerCommands(new SteriaUtilsCommand());
        if (SteriaUtils.getPlugin().getConfig().getBoolean("misc-commands.steria-command.enabled")) {
            commandFramework.registerCommands(new SteriaCommand());
        }
        if (SteriaUtils.getPlugin().getConfig().getBoolean("misc-commands.setcolor-command.enabled")) {
            commandFramework.registerCommands(new SetColorCommand());
        }
        if (SteriaUtils.getPlugin().getConfig().getBoolean("loot-crate.enabled")) {
            commandFramework.registerCommands(new LootcrateCommand());
        }
        commandFramework.registerCommands(new GeoIPCommand());
        commandFramework.registerCommands(new VouchersCommand());

        commandFramework.registerHelp();
        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&b[SteriaUtils] &aRegistered commands.");

    }

    /**
     * Registering listeners...
     */

    private void registerListeners() {
        PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new VouchersListener(), this);
        manager.registerEvents(new SafetyListener(), this);
        manager.registerEvents(new AntiTabHandler(), this);
        manager.registerEvents(new SteriaListener(), this);
        manager.registerEvents(new AdvancedAntiGlitchListener(), this);
        manager.registerEvents(new AdvancedPearlGlitchListener(), this);
        if (SteriaUtils.getPlugin().getConfig().getBoolean("loot-crate.enabled")) {
            manager.registerEvents(new LootCrateListener(), this);
        }

        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&b[SteriaUtils] &aRegistered listeners.");
    }

    /**
     * Reloading configs...
     */

    public void reload(){
        this.reloadConfig();
        this.voucherConfig.reload();
    }

    public Config getVoucherConfig() {
        return this.voucherConfig;
    }

    public void TipsMessager() {
        new BukkitRunnable() {
            final List<String> messages = SteriaUtils.getPlugin().getConfig().getStringList("tips.broadcast-messages");
            final List<String> clonedMessages = new ArrayList<>(messages);
            Iterator<String> iterator = clonedMessages.iterator();

            public void run() {
                if (iterator.hasNext()) {
                    for (Player player : SteriaUtils.getPlugin().getServer().getOnlinePlayers()) {
                        try {
                            if (SteriaUtils.getPlugin().getConfig().getBoolean("tips.sound")) {
                                player.playSound(player.getLocation(), Sound.valueOf(SteriaUtils.getPlugin().getConfig().getString("tips.sound-name").toUpperCase()), 1.0F, 1.0F);
                            }
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', iterator.next()));
                        } catch (NoSuchElementException ignored) {
                        }
                    }
                } else {
                    iterator = new ArrayList<>(messages).iterator();
                }
            }
        }.runTaskTimer(SteriaUtils.getPlugin(), 20L, 20L * SteriaUtils.getPlugin().getConfig().getInt("tips.message-delay"));
    }

    public static void runTaskTimer(Runnable runnable, long delay, long timer) {
        SteriaUtils.getPlugin().getServer().getScheduler().runTaskTimerAsynchronously(SteriaUtils.getPlugin(), runnable, delay, timer);
    }
}

