package net.helydev.com.listeners;

import net.helydev.com.SteriaUtils;
import net.helydev.com.utils.Color;
import net.helydev.com.utils.LootBuilder;
import net.minecraft.util.com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LootCrateListener implements Listener {
    public static HashMap<UUID, Integer> OPENED_BOXES = new HashMap<>();
    public static ArrayList<String> REGULAR_ITEMS = new ArrayList<>();
    public static ArrayList<String> FINAL_ITEMS = new ArrayList<>();
    public static Set<UUID> NO_CLOSE = new HashSet<>();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        OPENED_BOXES.put(event.getPlayer().getUniqueId(), 1);
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() == null) return;
        if (event.getItem().getType() != Material.valueOf(SteriaUtils.getPlugin().getConfig().getString("loot-crate.item.MATERIAL")))
            return;
        if (event.getItem().getItemMeta() == null) return;
        if (event.getItem().getItemMeta().getDisplayName() == null) return;
        if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.item.DISPLAY-NAME"))))
            return;

        setupChances();

        Inventory inv = Bukkit.createInventory(null, 54, Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.inventory.chest-name")));

        final Player player = event.getPlayer();
        ItemStack finalReward = LootBuilder.nameItem(Material.ENDER_CHEST, Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.inventory.final-reward.unclicked-name")), (short) 0, 1, Arrays.asList(SteriaUtils.getPlugin().getConfig().getString("loot-crate.inventory.final-reward.lore")));
        ItemStack basicReward = LootBuilder.nameItem(Material.CHEST, Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.inventory.regular-reward.unclicked-name")), (short) 0, 1, Arrays.asList(SteriaUtils.getPlugin().getConfig().getString("loot-crate.inventory.regular-reward.lore")));
        ItemStack spacer = LootBuilder.nameItem(Material.STAINED_GLASS_PANE, Color.translate(" "), (short) 8, 1, Arrays.asList());

        for (int i = 0; i < 54; i++) {
            inv.setItem(i, spacer);
        }

        //next row (row 1)
        inv.setItem(12, basicReward);
        inv.setItem(13, basicReward);
        inv.setItem(14, basicReward);

        //next row (row 2)
        inv.setItem(21, basicReward);
        inv.setItem(22, basicReward);
        inv.setItem(23, basicReward);

        //next row (row 3)
        inv.setItem(30, basicReward);
        inv.setItem(31, basicReward);
        inv.setItem(32, basicReward);

        //Final reward pog
        inv.setItem(40, finalReward);

        ItemStack box = LootBuilder.nameItem(Material.valueOf(SteriaUtils.getPlugin().getConfig().getString("loot-crate.item.MATERIAL")), SteriaUtils.getPlugin().getConfig().getString("loot-crate.item.DISPLAY-NAME"), (short) SteriaUtils.getPlugin().getConfig().getInt("loot-crate.item.ITEM-META"), 1, SteriaUtils.getPlugin().getConfig().getStringList("loot-crate.item.LORES"));

        event.getPlayer().getInventory().removeItem(box);

        event.getPlayer().openInventory(inv);
        NO_CLOSE.add(player.getUniqueId());
        final List<String> broadcast = SteriaUtils.getPlugin().getVoucherConfig().getConfiguration().getStringList("loot-crate.messages.broadcast");
        for (final String str2 : broadcast) {
            Bukkit.broadcastMessage(Color.translate(str2));
        }

    }

    @EventHandler
    public boolean click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) return false;
        if (!event.getClickedInventory().getName().equalsIgnoreCase(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.inventory.chest-name"))))
            return false;
        if (event.getCurrentItem() == null) return false;
        if (event.getCurrentItem().getType() == null) return false;
        if (event.getCurrentItem().getItemMeta() == null) return false;
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return false;

        if (event.getCurrentItem().getType() == Material.CHEST) {
            event.setCancelled(true);
            Random ran = new Random();
            int rewardCount = REGULAR_ITEMS.size();
            int random = ran.nextInt(rewardCount);

            int chance = SteriaUtils.getPlugin().getConfig().getInt("rewards.REGULAR-ITEMS." + REGULAR_ITEMS.get(random) + ".CHANCE");
            String displayName = SteriaUtils.getPlugin().getConfig().getString("rewards.REGULAR-ITEMS." + REGULAR_ITEMS.get(random) + ".DISPLAY-NAME");
            Material material = Material.valueOf(SteriaUtils.getPlugin().getConfig().getString("rewards.REGULAR-ITEMS." + REGULAR_ITEMS.get(random) + ".MATERIAL"));
            List<String> commands = SteriaUtils.getPlugin().getConfig().getStringList("rewards.REGULAR-ITEMS." + REGULAR_ITEMS.get(random) + ".COMMANDS");
            int amount = SteriaUtils.getPlugin().getConfig().getInt("rewards.REGULAR-ITEMS." + REGULAR_ITEMS.get(random) + ".AMOUNT");
            short meta = (short) SteriaUtils.getPlugin().getConfig().getInt("rewards.REGULAR-ITEMS." + REGULAR_ITEMS.get(random) + ".ITEM-META");
            List<String> enchants = SteriaUtils.getPlugin().getConfig().getStringList("rewards.REGULAR-ITEMS." + REGULAR_ITEMS.get(random) + ".ENCHANTS");
            List<String> lores = SteriaUtils.getPlugin().getConfig().getStringList("rewards.REGULAR-ITEMS." + REGULAR_ITEMS.get(random) + ".LORES");

            ItemStack reward = LootBuilder.nameItem(material, displayName, meta, amount, lores);

            if (enchants.size() != 0) {
                for (String str : enchants) {
                    Enchantment enchantment = Enchantment.getByName(str.split(":")[0]);
                    int level = Ints.tryParse(str.split(":")[1]);

                    reward.addUnsafeEnchantment(enchantment, level);
                }
            }

            event.getClickedInventory().setItem(event.getSlot(), reward);

            for (String str : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str.replace("%player%", event.getWhoClicked().getName()));
            }

            player.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.recieved").replace("%item%", displayName)));

            player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
            if (OPENED_BOXES.containsKey(event.getWhoClicked().getUniqueId())) {
                OPENED_BOXES.put(event.getWhoClicked().getUniqueId(), OPENED_BOXES.get(event.getWhoClicked().getUniqueId()) + 1);
            } else {
                OPENED_BOXES.put(event.getWhoClicked().getUniqueId(), 1);
            }


        } else if (event.getCurrentItem().getType() == Material.ENDER_CHEST) {
            event.setCancelled(true);
            if (OPENED_BOXES.containsKey(event.getWhoClicked().getUniqueId()) && OPENED_BOXES.get(event.getWhoClicked().getUniqueId()) < 9) {
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.other.cannot-redeem.message")));
                if (SteriaUtils.getPlugin().getConfig().getBoolean("loot-crate.other.cannot-redeem.sound")) {
                    ((Player) event.getWhoClicked()).getPlayer().playSound(((Player) event.getWhoClicked()).getPlayer().getLocation(), Sound.valueOf(SteriaUtils.getPlugin().getConfig().getString("loot-crate.other.cannot-redeem.name").toUpperCase()), 1.0F, 1.0F);
                }
                return false;
            }
            if (OPENED_BOXES.containsKey(event.getWhoClicked().getUniqueId()) && OPENED_BOXES.get(event.getWhoClicked().getUniqueId()) == 1) {
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.other.cannot-redeem.message")));
                if (SteriaUtils.getPlugin().getConfig().getBoolean("loot-crate.other.cannot-redeem.sound")) {
                    ((Player) event.getWhoClicked()).getPlayer().playSound(((Player) event.getWhoClicked()).getPlayer().getLocation(), Sound.valueOf(SteriaUtils.getPlugin().getConfig().getString("loot-crate.other.cannot-redeem.name").toUpperCase()), 1.0F, 1.0F);
                }
                return false;
            }

            Random ran = new Random();
            int rewardCount = FINAL_ITEMS.size();
            int random = ran.nextInt(rewardCount);

            int chance = SteriaUtils.getPlugin().getConfig().getInt("rewards.FINAL-ITEMS." + FINAL_ITEMS.get(random) + ".CHANCE");
            String displayName = SteriaUtils.getPlugin().getConfig().getString("rewards.FINAL-ITEMS." + FINAL_ITEMS.get(random) + ".DISPLAY-NAME");
            Material material = Material.valueOf(SteriaUtils.getPlugin().getConfig().getString("rewards.FINAL-ITEMS." + FINAL_ITEMS.get(random) + ".MATERIAL"));
            List<String> commands = SteriaUtils.getPlugin().getConfig().getStringList("rewards.FINAL-ITEMS." + FINAL_ITEMS.get(random) + ".COMMANDS");
            int amount = SteriaUtils.getPlugin().getConfig().getInt("rewards.FINAL-ITEMS." + FINAL_ITEMS.get(random) + ".AMOUNT");
            short meta = (short) SteriaUtils.getPlugin().getConfig().getInt("rewards.FINAL-ITEMS." + FINAL_ITEMS.get(random) + ".ITEM-META");
            List<String> enchants = SteriaUtils.getPlugin().getConfig().getStringList("rewards.FINAL-ITEMS." + FINAL_ITEMS.get(random) + ".ENCHANTS");
            List<String> lores = SteriaUtils.getPlugin().getConfig().getStringList("rewards.FINAL-ITEMS." + FINAL_ITEMS.get(random) + ".LORES");

            ItemStack reward = LootBuilder.nameItem(material, displayName, meta, amount, lores);

            if (enchants.size() != 0) {
                for (String str : enchants) {
                    Enchantment enchantment = Enchantment.getByName(str.split(":")[0]);
                    int level = Ints.tryParse(str.split(":")[1]);

                    reward.addUnsafeEnchantment(enchantment, level);
                }
            }

            event.getClickedInventory().setItem(event.getSlot(), reward);

            for (String str : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str.replace("%player%", event.getWhoClicked().getName()));
            }

            player.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.recieved").replace("%item%", displayName)));
            OPENED_BOXES.remove(player.getUniqueId());
            NO_CLOSE.remove(player.getUniqueId());
            player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
        } else {
            event.setCancelled(true);
        }

        return false;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();
        if (event.getInventory().getName().equalsIgnoreCase(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.inventory.chest-name")))) {

            if (NO_CLOSE.contains(event.getPlayer().getUniqueId())) {
                    player.sendMessage(Color.translate(SteriaUtils.getPlugin().getConfig().getString("loot-crate.messages.closed")));
            }
        }
    }

    public void setupChances() {
        for (String str : SteriaUtils.getPlugin().getConfig().getConfigurationSection("rewards.REGULAR-ITEMS").getKeys(false)) {
            int chance = SteriaUtils.getPlugin().getConfig().getInt("rewards.REGULAR-ITEMS." + str + ".CHANCE");

            for (int i = 0; i < chance; i++) {
                REGULAR_ITEMS.add(str);
            }
        }

        for (String str : SteriaUtils.getPlugin().getConfig().getConfigurationSection("rewards.FINAL-ITEMS").getKeys(false)) {
            int chance = SteriaUtils.getPlugin().getConfig().getInt("rewards.FINAL-ITEMS." + str + ".CHANCE");

            for (int i = 0; i < chance; i++) {
                FINAL_ITEMS.add(str);
            }
        }

    }

}