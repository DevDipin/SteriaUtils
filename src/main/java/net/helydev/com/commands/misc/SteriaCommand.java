package net.helydev.com.commands.misc;

import net.helydev.com.SteriaUtils;
import net.helydev.com.utils.Color;
import net.helydev.com.utils.commands.Command;
import net.helydev.com.utils.commands.CommandArgs;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SteriaCommand {
    @Command(name = "steria")
    public void giveVoucher(final CommandArgs command) {
        final Player player = command.getPlayer();
        Location FUCKINGLOCATION = player.getLocation();
        final String[] args = command.getArgs();

        player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 2.0F, 1.0F);
        player.getWorld().playEffect(FUCKINGLOCATION, Effect.MOBSPAWNER_FLAMES, 4);
        for (String message : SteriaUtils.getPlugin().getConfig().getStringList("misc-commands.steria-command.message")) {
            player.sendMessage(Color.translate(message));
        }
    }
}
