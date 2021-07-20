package net.helydev.com.commands;

import net.helydev.com.SteriaUtils;
import net.helydev.com.utils.Color;
import net.helydev.com.utils.commands.Command;
import net.helydev.com.utils.commands.CommandArgs;
import org.bukkit.entity.Player;

public class SteriaUtilsCommand {
    @Command(name = "steriavouchers", permission = "", inGameOnly = false)
    public void sendMessage(final CommandArgs command) {
        final Player player = command.getPlayer();
        final String[] args = command.getArgs();
        if (args.length == 0) {
            command.getSender().sendMessage(Color.translate("&7"));
            command.getSender().sendMessage(Color.translate("&b&lSteria Utils"));
            command.getSender().sendMessage(Color.translate("&7&oThis plugin is made for the Steria Network."));
            command.getSender().sendMessage(Color.translate("&7"));
            command.getSender().sendMessage(Color.translate("&8»&e Authors&7: &f" + SteriaUtils.getPlugin().getDescription().getAuthors()));
            command.getSender().sendMessage(Color.translate("&8»&e Discord&7: &fAndrew!!#4468 (discord.gg/hely)"));
            command.getSender().sendMessage(Color.translate("&7"));
        }
        else if (command.getSender().hasPermission("steria.command.steriavouchers") && args[0].equalsIgnoreCase("reload")) {
            final long time = System.currentTimeMillis();

            SteriaUtils.getPlugin().reloadConfig();
            SteriaUtils.getPlugin().getVoucherConfig().reload();
            command.getSender().sendMessage(Color.translate("&b&lSteria Uitls &ahas been reloaded successfully. &7(" + (System.currentTimeMillis() - time) + "ms)"));
        }

    }
}
