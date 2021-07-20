package net.helydev.com.commands.misc;

import net.helydev.com.utils.Color;
import net.helydev.com.utils.CountryUtil;
import net.helydev.com.utils.commands.Command;
import net.helydev.com.utils.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GeoIPCommand {
    @Command(name = "geoip", permission = "steria.command.geoip", inGameOnly = true)

    public boolean sendMessage(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        Player target = Bukkit.getPlayer(args[0]);

        if (args.length < 1) {
            player.sendMessage(Color.translate("&cCorrect Usage: /geoip <player>"));
            return true;
        }

        if (target == null) {
            player.sendMessage(Color.translate("&6&n" + args[0] + "&6 is currently not connected to the &3&lSteria Network&6."));
            return true;
        }

        String IP = target.getPlayer().getAddress().getAddress().getHostAddress();
        String country = CountryUtil.getCountry(IP);
        player.sendMessage(Color.translate("&6" + target.getName() + " &6is currently connected from &6&n" + country + "&6."));

        return false;
    }
}
