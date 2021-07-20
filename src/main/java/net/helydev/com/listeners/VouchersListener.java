package net.helydev.com.listeners;

import net.helydev.com.SteriaUtils;
import net.helydev.com.utils.Color;
import net.helydev.com.utils.Cooldowns;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class VouchersListener implements Listener {
    @EventHandler
    @Deprecated
    public boolean onInteractVouch(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        for (final String vouchItem : SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getConfigurationSection("vouchers").getKeys(false)) {
            final ItemStack vouchItemStackCustom = new ItemStack(Material.valueOf(SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getString("vouchers." + vouchItem + ".item")), 1, (short) SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getInt("vouchers." + vouchItem + ".item-data"));
            final ItemMeta vouchItemMetaCustom = vouchItemStackCustom.getItemMeta();
            vouchItemMetaCustom.setDisplayName(SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getString("vouchers." + vouchItem + ".name"));
            final List<String> lore = new ArrayList<>();
            for (String string : SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getStringList("vouchers." + vouchItem + ".lores")) {
                string = string.replace("&", "§");
                string = string.replace("%d_arrow%", "»");
                lore.add(string);
            }
            vouchItemMetaCustom.setLore(lore);
            vouchItemStackCustom.setItemMeta(vouchItemMetaCustom);
            if (player.getItemInHand().getItemMeta() == null || player.getItemInHand() == null || player.getItemInHand().getItemMeta().getDisplayName() == null || player.getItemInHand().getItemMeta().getLore() == null) {
                return false;
            }
            if (Cooldowns.isOnCooldown("voucher", player)) {
                player.sendMessage(ChatColor.RED + "You are still on a cooldown. Please wait before opening another voucher.");
                return false;
            }
            if (!player.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(Color.translate(SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getString("vouchers." + vouchItem + ".name")))) {
                continue;
            }
            final List<String> commands = SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getStringList("vouchers." + vouchItem + ".commands");
            for (final String str : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str.replace("%player%", player.getName()));
            }
            if (player.getItemInHand().getAmount() > 1) {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            } else {
                player.setItemInHand(null);
            }
            final List<String> message = SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getStringList("vouchers." + vouchItem + ".opened");
            for (final String str2 : message) {
                player.sendMessage(Color.translate(str2));
            }

            ///////////////////////////////////////// LISTENERS /////////////////////////////////////////////////////
            //Plays a sound when a player opens a voucher.
            if (SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getBoolean("vouchers." + vouchItem + ".sound.enabled")) {
                player.playSound(player.getLocation(), Sound.valueOf(SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getString("vouchers." + vouchItem + ".sound.sound-name").toUpperCase()), 1.0F, 1.0F);
            }
            //Plays a particle effect around the player.
            if (SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getBoolean("vouchers." + vouchItem + ".particles.enabled")) {
                player.playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
            }

            //You shitters... do not spam fucking vouchers! - Andrew <3
            Cooldowns.addCooldown("voucher", player, 60);

            ///////////////////////////////////////// LISTENERS /////////////////////////////////////////////////////
            player.updateInventory();
        }
        return false;
    }
}
