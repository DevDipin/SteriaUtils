package net.helydev.com.commands;

import net.helydev.com.SteriaUtils;
import net.helydev.com.utils.Color;
import net.helydev.com.utils.JavaUtils;
import net.helydev.com.utils.Utils;
import net.helydev.com.utils.commands.Command;
import net.helydev.com.utils.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class VouchersCommand {
    @Command(name = "voucher", permission = "core.command.voucher")
    public boolean giveVoucher(final CommandArgs command) {
        final Player player = command.getPlayer();
        final String[] args = command.getArgs();
        if (args.length < 1) {
            player.sendMessage(Color.translate("&7&l&m-------------------------------------"));
            player.sendMessage(Color.translate("&b&lVouchers"));
            player.sendMessage(Color.translate("&7"));
            player.sendMessage(Color.translate("&a/voucher give <player> <amount> <voucher> &8- &7Give a player a voucher."));
            player.sendMessage(Color.translate("&a/voucher giveall <amount> <voucher> &8- &7Give all online players a voucher."));
            player.sendMessage(Color.translate("&a/voucher reload &8- &7Reloads all vouchers."));
            player.sendMessage(Color.translate("&7&l&m-------------------------------------"));
        }
        else if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 3) {
                command.getSender().sendMessage(Color.translate("&7&l&m-------------------------------------"));
                command.getSender().sendMessage(Color.translate("&b&lVouchers"));
                command.getSender().sendMessage(Color.translate("&7"));
                command.getSender().sendMessage(Color.translate("&a/voucher give <player> <amount> <voucher> &8- &7Give a player a voucher."));
                command.getSender().sendMessage(Color.translate("&a/voucher giveall <amount> <voucher> &8- &7Give all online players a voucher."));
                command.getSender().sendMessage(Color.translate("&a/voucher reload &8- &7Reloads all vouchers."));
                command.getSender().sendMessage(Color.translate("&7&l&m-------------------------------------"));
                return true;
            }
            else {
                final Player target = Bukkit.getPlayer(args[1]);
                if (!Utils.isOnline(player, target)) {
                    command.getSender().sendMessage(Color.translate("&cThat player is not currently online."));
                }
                else {
                    final Integer amount = JavaUtils.tryParseInt(args[2]);
                    if (amount == null) {
                        command.getSender().sendMessage(Color.translate("&cYou provided a invalid number."));
                    }
                    else if (amount <= 0) {
                        command.getSender().sendMessage(Color.translate("&cYou can only use positive numbers."));
                    }
                    else if (amount > 64) {
                        command.getSender().sendMessage(Color.translate("&cYou can only give 64 vouchers at a time."));
                    }
                    else {
                        int j = 0;
                        int h = 0;
                        for (final String Voucher : SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getConfigurationSection("vouchers").getKeys(false)) {
                            ++j;
                            if (!Voucher.equals(args[3])) {
                                ++h;
                            }
                        }
                        if (j != h) {
                            final String Voucher2 = args[3];
                            this.giveVoucherItem(target, amount, Voucher2);
                            command.getSender().sendMessage(Color.translate("&b&l[!] &aYou have just given &a&n" + target.getName() + "&a a voucher."));
                            target.sendMessage(Color.translate("&b&l[!] &bYou have just received a voucher."));
                        }
                        else {
                            command.getSender().sendMessage(Color.translate("&cThat is not a valid voucher."));
                        }
                    }
                }
            }
        }
        else if (args[0].equalsIgnoreCase("giveall")) {
            if (args.length < 2) {
                command.getSender().sendMessage(Color.translate("&7&l&m-------------------------------------"));
                command.getSender().sendMessage(Color.translate("&b&lVouchers"));
                command.getSender().sendMessage(Color.translate("&7"));
                command.getSender().sendMessage(Color.translate("&a/voucher give <player> <amount> <voucher> &8- &7Give a player a voucher."));
                command.getSender().sendMessage(Color.translate("&a/voucher giveall <amount> <voucher> &8- &7Give all online players a voucher."));
                command.getSender().sendMessage(Color.translate("&a/voucher reload &8- &7Reloads all vouchers."));
                command.getSender().sendMessage(Color.translate("&7&l&m-------------------------------------"));
                return true;
            }
            else {
                final Integer amount2 = JavaUtils.tryParseInt(args[1]);
                if (amount2 == null) {
                    command.getSender().sendMessage(Color.translate("&cYou provided a invalid number."));
                    return false;
                }
                if (amount2 <= 0) {
                    command.getSender().sendMessage(Color.translate("&cYou can only use positive numbers."));
                    return false;
                }
                if (amount2 > 64) {
                    command.getSender().sendMessage(Color.translate("&cYou can only give 64 vouchers at a time."));
                    return false;
                }
                int i = 0;
                int h2 = 0;
                for (final String Voucher2 : SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getConfigurationSection("vouchers").getKeys(false)) {
                    ++i;
                    if (!Voucher2.equals(args[2])) {
                        ++h2;
                    }
                }
                if (i != h2) {
                    final String Voucher3 = args[2];
                    for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
                        this.giveVoucherItem(online, amount2, Voucher3);
                    }
                }
                else {
                    command.getSender().sendMessage(Color.translate("&cThat is not a valid voucher."));
                }
            }
        }
        return false;
    }

    private void giveVoucherItem(final Player player, final int amount, final String vouchName) {
        final ItemStack stack = new ItemStack(Material.valueOf(SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getString("vouchers." + vouchName + ".item")), amount, (short) SteriaUtils.getPlugin().getConfig().getInt("vouchers." + vouchName + ".item-data"));
        final ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Color.translate(SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getString("vouchers." + vouchName + ".name")));
        final List<String> lore = new ArrayList<>();
        for (final String string : SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getStringList("vouchers." + vouchName + ".lores")) {
            lore.add(Color.translate(string));
        }
        meta.setLore(lore);
        stack.setItemMeta(meta);
        player.getInventory().addItem(stack);
    }
}

