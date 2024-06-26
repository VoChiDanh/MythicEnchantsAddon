package net.danh.mythicenchantsaddon.utils;

import de.tr7zw.changeme.nbtapi.NBT;
import io.lumine.mythicenchants.MythicEnchants;
import io.lumine.mythicenchants.enchants.MythicEnchant;
import net.danh.mythicenchantsaddon.resources.Chat;
import net.danh.mythicenchantsaddon.resources.Number;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Collectors;

public class EnchantedBook {

    public static @Nullable ItemStack getEnchantedBook(String enchantID, String level) {
        MythicEnchant enchant = MythicEnchants.inst().getEnchantManager().getEnchantments().get(enchantID);
        if (enchant != null) {
            ItemStack enchantBook;
            int successChance = Math.min(100, MythicEnchants.inst().getConfig().contains("MythicEnchantsAddon.EnchantInfo." + enchantID + ".SuccessChance")
                    ? MythicEnchants.inst().getConfig().getInt("MythicEnchantsAddon.EnchantInfo." + enchantID + ".SuccessChance") :
                    MythicEnchants.inst().getConfig().getInt("MythicEnchantsAddon.EnchantInfo.DefaultSettings.SuccessChance", 70));
            if (MythicEnchants.inst().getConfig().contains("MythicEnchantsAddon.EnchantInfo." + enchantID + ".CustomLore")) {
                enchantBook = MythicEnchants.inst().getEnchantManager().applyToItem(new ItemEditor(
                        Material.valueOf(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                        .addAllLore((MythicEnchants.inst().getConfig().getStringList("MythicEnchantsAddon.EnchantInfo." + enchantID + ".CustomLore")
                                .stream().map(s -> s.replace("<enchant>", enchant.getDisplayName())
                                        .replace("<level>", level)
                                        .replace("<success_chance>", String.valueOf(successChance))
                                        .replace("<fail_chance>", String.valueOf(100 - successChance))
                                ).collect(Collectors.toList())))
                        .setName(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                .replace("<enchant>", enchant.getDisplayName())
                                .replace("<level>", level)
                                .replace("<success_chance>", String.valueOf(successChance))
                                .replace("<fail_chance>", String.valueOf(100 - successChance)))
                        .toItemStack(), enchant, Number.getInteger(level));
            } else if (MythicEnchants.inst().getConfig().contains("MythicEnchantsAddon.EnchantInfo.DefaultSettings.DefaultLore")) {
                enchantBook = MythicEnchants.inst().getEnchantManager().applyToItem(new ItemEditor(
                        Material.valueOf(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                        .addAllLore((MythicEnchants.inst().getConfig().getStringList("MythicEnchantsAddon.EnchantInfo.DefaultSettings.DefaultLore")
                                .stream().map(s -> s.replace("<enchant>", enchant.getDisplayName())
                                        .replace("<level>", level)
                                        .replace("<success_chance>", String.valueOf(successChance))
                                        .replace("<fail_chance>", String.valueOf(100 - successChance))
                                ).collect(Collectors.toList())))
                        .setName(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                .replace("<enchant>", enchant.getDisplayName())
                                .replace("<level>", level)
                                .replace("<success_chance>", String.valueOf(successChance))
                                .replace("<fail_chance>", String.valueOf(100 - successChance)))
                        .toItemStack(), enchant, Number.getInteger(level));
            } else {
                enchantBook = MythicEnchants.inst().getEnchantManager().applyToItem(new ItemEditor(
                        Material.valueOf(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                        .setName(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                .replace("<enchant>", enchant.getDisplayName())
                                .replace("<level>", level)
                                .replace("<success_chance>", String.valueOf(successChance))
                                .replace("<fail_chance>", String.valueOf(100 - successChance)))
                        .toItemStack(), enchant, Number.getInteger(level));
            }
            NBT.modify(enchantBook, readWriteItemNBT -> {
                readWriteItemNBT.setString("mythicenchantsaddon_enchant_id", enchantID);
                readWriteItemNBT.setInteger("mythicenchantsaddon_enchant_level", Number.getInteger(level));
                readWriteItemNBT.setInteger("mythicenchantsaddon_success_chance", successChance);
            });
            return enchantBook;
        } else if (Enchantment.getByKey(NamespacedKey.minecraft(enchantID)) != null) {
            ItemStack itemStack;
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantID));
            if (enchantment != null) {
                if (MythicEnchants.inst().getConfig().contains("MythicEnchantsAddon.EnchantInfo.DefaultSettings.DefaultLore")) {
                    itemStack = new ItemEditor(
                            Material.valueOf(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                            .addAllLore((MythicEnchants.inst().getConfig().getStringList("MythicEnchantsAddon.EnchantInfo.DefaultSettings.DefaultLore")
                                    .stream().map(s -> s.replace("<enchant>", Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " ")))
                                            .replace("<level>", level)
                                            .replace("<success_chance>", String.valueOf(100))
                                            .replace("<fail_chance>", String.valueOf(0))
                                    ).collect(Collectors.toList())))
                            .setName(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                    .replace("<enchant>", Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " ")))
                                    .replace("<level>", level)
                                    .replace("<success_chance>", String.valueOf(100))
                                    .replace("<fail_chance>", String.valueOf(0)))
                            .toItemStack();
                } else {
                    itemStack = new ItemEditor(
                            Material.valueOf(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                            .setName(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                    .replace("<enchant>", Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " ")))
                                    .replace("<level>", level)
                                    .replace("<success_chance>", String.valueOf(100))
                                    .replace("<fail_chance>", String.valueOf(0))).toItemStack();
                }
                itemStack.addUnsafeEnchantment(enchantment, Number.getInteger(level));
                NBT.modify(itemStack, readWriteItemNBT -> {
                    readWriteItemNBT.setString("mythicenchantsaddon_enchant_id", enchantID);
                    readWriteItemNBT.setInteger("mythicenchantsaddon_enchant_level", Number.getInteger(level));
                    readWriteItemNBT.setInteger("mythicenchantsaddon_success_chance", 100);
                });
                return itemStack;
            }
        }
        return null;
    }
}
