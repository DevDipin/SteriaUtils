package net.helydev.com.utils;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StarterBuilder {
    private ItemStack stack;
    private ItemMeta meta;

    public StarterBuilder(final Material material) {
        this(material, 1);
    }

    public StarterBuilder(final Material material, final int amount) {
        this(material, amount, (byte)0);
    }

    public StarterBuilder(final ItemStack stack) {
        Preconditions.checkNotNull((Object)stack, (Object)"ItemStack cannot be null");
        this.stack = stack;
    }

    public StarterBuilder(final Material material, final int amount, final byte data) {
        Preconditions.checkNotNull((Object)material, (Object)"Material cannot be null");
        Preconditions.checkArgument(amount > 0, (Object)"Amount must be positive");
        this.stack = new ItemStack(material, amount, (short)data);
    }

    public StarterBuilder displayName(final String name) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }
        this.meta.setDisplayName(name);
        return this;
    }

    public StarterBuilder loreLine(final String line) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }
        final boolean hasLore = this.meta.hasLore();
        final List<String> lore = hasLore ? this.meta.getLore() : new ArrayList<String>();
        lore.add(hasLore ? lore.size() : 0, line);
        this.lore(line);
        return this;
    }

    public StarterBuilder lore(final String... lore) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }
        this.meta.setLore((List) Arrays.asList(lore));
        return this;
    }

    public StarterBuilder setLore(final List<String> lore) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }
        this.meta.setLore((List)lore);
        return this;
    }

    public StarterBuilder enchant(final Enchantment enchantment, final int level) {
        return this.enchant(enchantment, level, true);
    }

    public StarterBuilder enchant(final Enchantment enchantment, final int level, final boolean unsafe) {
        if (unsafe && level >= enchantment.getMaxLevel()) {
            this.stack.addUnsafeEnchantment(enchantment, level);
        }
        else {
            this.stack.addEnchantment(enchantment, level);
        }
        return this;
    }

    public StarterBuilder data(final short data) {
        this.stack.setDurability(data);
        return this;
    }

    public ItemStack build() {
        if (this.meta != null) {
            this.stack.setItemMeta(this.meta);
        }
        return this.stack;
    }
}
