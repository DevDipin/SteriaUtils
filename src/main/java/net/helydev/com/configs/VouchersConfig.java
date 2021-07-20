package net.helydev.com.configs;

import net.helydev.com.SteriaUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class VouchersConfig {
    public static File file;
    public static YamlConfiguration configuration;

    public static File getLicenceFile() {
        return new File(SteriaUtils.getPlugin().getDataFolder(), "vouchers.yml");
    }

    public static FileConfiguration getLicenceFileConfig() {
        return YamlConfiguration.loadConfiguration(getLicenceFile());
    }

    public static void setStandard() {
        VouchersConfig.file = new File(SteriaUtils.getPlugin().getDataFolder(), "vouchers.yml");
        if (!VouchersConfig.file.exists()) {
            SteriaUtils.getPlugin().saveResource("vouchers.yml", false);
        }
        VouchersConfig.configuration = YamlConfiguration.loadConfiguration(VouchersConfig.file);
    }
}
