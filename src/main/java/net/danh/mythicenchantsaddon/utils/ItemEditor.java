package net.danh.mythicenchantsaddon.utils;


import net.danh.mythicenchantsaddon.resources.Chat;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemEditor {
    private ItemStack is;
    private ItemMeta itemMeta;

    public ItemEditor(Material m) {
        this(m, 1);
    }

    public ItemEditor(@NotNull ItemStack is) {
        this.is = is;
        itemMeta = is.getItemMeta();
    }

    public ItemEditor(Material m, int amount) {
        is = new ItemStack(m, amount);
        itemMeta = is.getItemMeta();
    }

    public ItemEditor setMaterial(Material material) {
        is.setType(material);
        return this;
    }

    public ItemEditor clone() {
        return new ItemEditor(is);
    }

    public String getName() {
        return itemMeta.getDisplayName();
    }

    public ItemEditor setName(String name) {
        itemMeta.setDisplayName(Chat.normalColorize(name));
        return this;
    }

    public ItemEditor addLoreLines(String... lines) {
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) lore = new ArrayList<>(itemMeta.getLore());
        for (String line : lines) {
            lore.add(Chat.normalColorize(line));
        }
        itemMeta.setLore(lore);
        return this;
    }

    public ItemEditor addLoreLines(int line, String lore2) {
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) lore = new ArrayList<>(itemMeta.getLore());
        lore.add(line, Chat.normalColorize(lore2));
        itemMeta.setLore(lore);
        return this;
    }

    public ItemEditor addHideFlagID(String itemFlagID) {
        this.addHideFlagsID(itemFlagID);
        return this;
    }

    public ItemEditor addHideFlagsID(String... itemFlagID) {
        for (String flagID : itemFlagID) {
            if (ItemFlag.valueOf(flagID) != null)
                addHideFlag(ItemFlag.valueOf(flagID));
        }
        return this;
    }

    public ItemEditor addHideFlag(ItemFlag... itemFlag) {
        itemMeta.addItemFlags(itemFlag);
        return this;
    }

    public ItemEditor hideFlagAll() {
        itemMeta.addItemFlags(ItemFlag.values());
        return this;
    }

    public ItemEditor setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemEditor addEnchant(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemEditor addAllLore(List<String> lore2) {
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) lore = new ArrayList<>(itemMeta.getLore());
        for (String line : lore2) {
            lore.add(Chat.normalColorize(line));
        }
        itemMeta.setLore(lore);
        return this;
    }

    public ItemStack toItemStack() {
        is.setItemMeta(itemMeta);
        return is;
    }
}
