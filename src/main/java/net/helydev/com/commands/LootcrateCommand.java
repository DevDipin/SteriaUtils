package net.helydev.com.commands;

import net.helydev.com.SteriaUtils;
import net.helydev.com.utils.Color;
import net.helydev.com.utils.LootBuilder;
import net.helydev.com.utils.commands.Command;
import net.helydev.com.utils.commands.CommandArgs;
import net.minecraft.util.com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LootcrateCommand {
    @Command(name = "lootcrate", permission = "core.command.lootcrate", inGameOnly = false)
    public boolean openCraft(CommandArgs command) {
        Player sender = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                if (args[1].equalsIgnoreCase("all")) {
                    if (Ints.tryParse(args[2]) == null) {
                        command.getSender().sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.invalid-number")));
                        return true;
                    }

                    int amount = Ints.tryParse(args[2]);

                    ItemStack box = LootBuilder.nameItem(Material.valueOf(SteriaUtils.getPlugin().getConfig().getString("loot-crate.item.MATERIAL")), SteriaUtils.getPlugin().getConfig().getString("loot-crate.item.DISPLAY-NAME"), (short) SteriaUtils.getPlugin().getConfig().getInt("loot-crate.item.ITEM-META"), amount, SteriaUtils.getPlugin().getConfig().getStringList("loot-crate.item.LORES"));

                    for (Player target : Bukkit.getOnlinePlayers()) {
                        if (target.getInventory().firstEmpty() == -1) {
                            target.getWorld().dropItemNaturally(target.getLocation(), box);
                            target.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.inventory-full")));
                        } else {
                            target.getInventory().addItem(box);
                            target.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.given").replace("%amount%", args[2])));
                        }
                    }
                } else {
                    if (Bukkit.getPlayer(args[1]) == null) {
                        command.getSender().sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.no-player")));
                        return true;
                    }

                    if (Ints.tryParse(args[2]) == null) {
                        command.getSender().sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.invalid-number")));
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[1]);
                    int amount = Ints.tryParse(args[2]);

                    ItemStack box = LootBuilder.nameItem(Material.valueOf(SteriaUtils.getPlugin().getConfig().getString("loot-crate.item.MATERIAL")), SteriaUtils.getPlugin().getConfig().getString("loot-crate.item.DISPLAY-NAME"), (short) SteriaUtils.getPlugin().getConfig().getInt("loot-crate.item.ITEM-META"), amount, SteriaUtils.getPlugin().getConfig().getStringList("loot-crate.item.LORES"));

                    if (target.getInventory().firstEmpty() == -1) {
                        target.getWorld().dropItemNaturally(target.getLocation(), box);
                        target.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.inventory-full")));
                    } else {
                        target.getInventory().addItem(box);
                        target.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.given").replace("%amount%", args[2])));
                    }
                }
                return true;
            }
        }

        command.getSender().sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.usage")));

        return false;
    }
}