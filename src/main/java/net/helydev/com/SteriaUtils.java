package net.helydev.com;


import net.helydev.com.commands.LootcrateCommand;
import net.helydev.com.commands.SteriaUtilsCommand;
import net.helydev.com.commands.VouchersCommand;
import net.helydev.com.commands.misc.FightCommand;
import net.helydev.com.commands.misc.GeoIPCommand;
import net.helydev.com.commands.misc.SetColorCommand;
import net.helydev.com.commands.misc.SteriaCommand;
import net.helydev.com.configs.Config;
import net.helydev.com.listeners.LootCrateListener;
import net.helydev.com.listeners.SafetyListener;
import net.helydev.com.listeners.VouchersListener;
import net.helydev.com.listeners.advanced.AdvancedAntiGlitchListener;
import net.helydev.com.listeners.advanced.AdvancedPearlGlitchListener;
import net.helydev.com.listeners.advanced.AntiTabHandler;
import net.helydev.com.utils.Cooldowns;
import net.helydev.com.utils.chat.ChatUtil;
import net.helydev.com.utils.commands.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
        commandFramework.registerCommands(new SteriaCommand());
        commandFramework.registerCommands(new SetColorCommand());
        commandFramework.registerCommands(new LootcrateCommand());
        commandFramework.registerCommands(new FightCommand());
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
        manager.registerEvents(new AdvancedAntiGlitchListener(), this);
        manager.registerEvents(new AdvancedPearlGlitchListener(), this);
        manager.registerEvents(new LootCrateListener(), this);
        ChatUtil.sendMessage(Bukkit.getConsoleSender(),"&b[SteriaUtils] &aRegistered listeners.");
    }

    public Config getVoucherConfig() {
        return this.voucherConfig;
    }
}

