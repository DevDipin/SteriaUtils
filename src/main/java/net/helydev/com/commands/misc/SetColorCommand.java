package net.helydev.com.commands.misc;

import net.helydev.com.SteriaUtils;
import net.helydev.com.utils.Color;
import net.helydev.com.utils.commands.Command;
import net.helydev.com.utils.commands.CommandArgs;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetColorCommand {
    public static List<Material> list = Arrays.asList(Material.GLASS, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE, Material.THIN_GLASS, Material.HARD_CLAY, Material.STAINED_CLAY, Material.WOOL, Material.CARPET);

    @Command(name = "setcolor", permission = "steria.command.setcolor", inGameOnly = true)

    public void sendMessage(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) {
            player.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("misc-commands.setcolor-command.messages.cannot-color")));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("misc-commands.setcolor-command.messages.usage")));
        }

        if (args[0].equalsIgnoreCase("reset")) {
            if (player.getItemInHand().getType() == Material.STAINED_CLAY) {
                player.getItemInHand().setType(Material.HARD_CLAY);
            }
            else if (player.getItemInHand().getType() == Material.STAINED_GLASS) {
                player.getItemInHand().setType(Material.GLASS);
            }
            else if (player.getItemInHand().getType() == Material.STAINED_GLASS_PANE) {
                player.getItemInHand().setType(Material.THIN_GLASS);
            }
            player.getItemInHand().setDurability((short)0);
            player.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("misc-commands.setcolor-command.messages.reset")));
            return;
        }

        if (getColor(args[0]) == null) {
            final StringBuilder sb = new StringBuilder();
            DyeColor[] values;
            for (int length = (values = DyeColor.values()).length, i = 0; i < length; ++i) {
                final DyeColor d = values[i];
                sb.append(d + "§7, §f");
            }
            player.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("misc-commands.setcolor-command.messages.invalid-color")));
            return;
        }

        if (list.contains(player.getItemInHand().getType())) {
            if (player.getItemInHand().getType() == Material.HARD_CLAY) {
                player.getItemInHand().setType(Material.STAINED_CLAY);
            } else if (player.getItemInHand().getType() == Material.GLASS) {
                player.getItemInHand().setType(Material.STAINED_GLASS);
            } else if (player.getItemInHand().getType() == Material.THIN_GLASS) {
                player.getItemInHand().setType(Material.STAINED_GLASS_PANE);
            }
            player.getItemInHand().setDurability(getColor(args[0]).getData());
            player.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("misc-commands.setcolor-command.messages.colored").replace("%item%", player.getItemInHand().getType().name()).replace("%color%", String.valueOf(this.getColor(args[0])))));
            return;
        }
        player.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("misc-commands.setcolor-command.messages.cannot-color")));
    }

    public static DyeColor getColor(final String s) {
        DyeColor c;
        try {
            c = DyeColor.valueOf(s.toUpperCase());
        } catch (Exception e) {
            c = null;
        }
        return c;
    }
}
