package net.helydev.com.listeners;

import net.helydev.com.Void;
import net.helydev.com.faction.type.Faction;
import net.helydev.com.listener.settings.SettingsMenu;
import net.minecraft.util.com.google.common.collect.ImmutableSet;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

public class SteriaListener implements Listener {
    public static Set<PotionEffectType> DEBUFFS;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onProjectileInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getItem() != null && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem().getType() == Material.POTION) {
            try {
                final ItemStack i = event.getItem();
                if (i.getDurability() != 0) {
                    final Potion pot = Potion.fromItemStack(i);
                    if (pot != null && pot.isSplash() && pot.getType() != null && SteriaListener.DEBUFFS.contains(pot.getType().getEffectType())) {
                        if (Void.getPlugin().getFactionManager().getFactionAt(player.getLocation()).isSafezone()) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(ChatColor.RED + "You cannot throw debuffs from inside spawn!");
                            event.getPlayer().updateInventory();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if ((Void.getPlugin().getConfig().getBoolean("server.mode.kitmap") && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Sign)) {
            final Sign sign = (Sign)event.getClickedBlock().getState();
            if (sign.getLine(1).equalsIgnoreCase(ChatColor.DARK_GREEN + "Settings Menu") && sign.getLine(2).equalsIgnoreCase(ChatColor.GREEN + "Click to open")) {
                new SettingsMenu().openMenu(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        final Location location = event.getEntity().getLocation();
        final Faction factionAt = Void.getPlugin().getFactionManager().getFactionAt(location);
        if (event.getEntity() instanceof Arrow && !factionAt.isSafezone()) {
            event.setCancelled(true);
        }
    }

    static {
        SteriaListener.DEBUFFS = ImmutableSet.of(PotionEffectType.POISON,  PotionEffectType.SLOW, PotionEffectType.WEAKNESS, PotionEffectType.HARM, PotionEffectType.WITHER);
    }
}
