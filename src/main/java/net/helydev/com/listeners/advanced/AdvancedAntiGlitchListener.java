package net.helydev.com.listeners.advanced;

import net.helydev.com.SteriaUtils;
import net.helydev.com.utils.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdvancedAntiGlitchListener implements Listener {
    private final Map<UUID, Long> cooldowns;

    public AdvancedAntiGlitchListener() {
        this.cooldowns = new HashMap<UUID, Long>();
    }

    public void disable() {
        this.cooldowns.clear();
    }

    private void check(final Player player, final Cancellable event) {
        if (this.cooldowns.containsKey(player.getUniqueId()) && System.currentTimeMillis() - this.cooldowns.get(player.getUniqueId()) < 500L) {
            event.setCancelled(true);
            player.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("anti-glitch.messages.cannot-use")));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (!event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        final Block block = event.getBlock();
        if (block.getType().isSolid()) {
            this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        }
        final Location location = player.getLocation().clone();
        if (!SteriaUtils.getPlugin().getConfig().getBoolean("anti-glitch.phase-fix") || location.getBlock().getType() == Material.AIR) {
            return;
        }
        location.setX(location.getBlockX() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);
        player.teleport(location);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!SteriaUtils.getPlugin().getConfig().getBoolean("anti-glitch.entity-fix") || !(event.getDamager() instanceof Player)) {
            return;
        }
        this.check((Player)event.getDamager(), (Cancellable)event);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!SteriaUtils.getPlugin().getConfig().getBoolean("anti-glitch.sign-glitch-fix") || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        final Block block = event.getClickedBlock();
        if (block == null || (block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN)) {
            return;
        }
        this.check(event.getPlayer(), (Cancellable)event);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.cooldowns.remove(event.getPlayer().getUniqueId());
    }
}

