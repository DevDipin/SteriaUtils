package net.helydev.com.commands.misc;

import net.helydev.com.utils.Color;
import net.helydev.com.utils.commands.Command;
import net.helydev.com.utils.commands.CommandArgs;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FightCommand {
    public HashMap<UUID, Long> cooldown;

    public FightCommand() {
        this.cooldown = new HashMap<>();
    }
    @Command(name = "fight", permission = "steria.command.fight", inGameOnly = true)

    public void fight(CommandArgs command) {
        Player player = command.getPlayer();

        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) != null) {
            final long remaining = this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
            if (remaining > 0L) {
                player.sendMessage(Color.translate("&cYou cannot use this command for another &c&l" + DurationFormatUtils.formatDurationWords(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), true, true)) + "&c.");
                return;
            }
        }

        this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(Long.parseLong("10")));
        Bukkit.broadcastMessage("&7&l&m----------------------------------------------");
        Bukkit.broadcastMessage("&b&lPlayer Fights");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("&a" + command.getSender() + " &7is currently looking for someone to fight!");
        Bukkit.broadcastMessage("&aCoordinates: &a&l" + player.getLocation().getBlockX() + "&a&l, " + player.getLocation().getBlockZ());
        Bukkit.broadcastMessage("&7&l&m----------------------------------------------");
    }
}
