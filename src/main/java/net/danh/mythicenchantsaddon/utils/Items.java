package net.danh.mythicenchantsaddon.utils;

import de.tr7zw.changeme.nbtapi.NBT;
import io.lumine.mythicenchants.MythicEnchants;
import io.lumine.mythicenchants.enchants.MythicEnchant;
import net.danh.mythicenchantsaddon.resources.Chat;
import net.danh.mythicenchantsaddon.resources.Files;
import net.danh.mythicenchantsaddon.resources.Number;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Collectors;

public class Items {

    public static ItemStack getSuccessChanceBook(int successChance) {
        ItemStack itemStack = new ItemEditor(
                Material.valueOf(Files.getConfig().getString("MythicEnchantsAddon.SuccessChance.ItemSetting.Material"))
        )
                .setName(Files.getConfig().getString("MythicEnchantsAddon.SuccessChance.ItemSetting.Display"))
                .addAllLore(Files.getConfig().getStringList("MythicEnchantsAddon.SuccessChance.ItemSetting.Lore")
                        .stream().map(s -> s.replace("<chance>", String.valueOf(successChance))).collect(Collectors.toList()))
                .setCustomModelData(Files.getConfig().getInt("MythicEnchantsAddon.SuccessChance.ItemSetting.CustomModelData"))
                .hideFlagAll()
                .setUnbreakable(true)
                .toItemStack();
        NBT.modify(itemStack, readWriteItemNBT -> {
            readWriteItemNBT.setInteger("mythicenchantsaddon_increase_success_chance", successChance);
            readWriteItemNBT.setString("mythicenchantsaddon_item_type", "success_chance_book");
        });
        return itemStack;
    }

    public static @Nullable ItemStack getEnchantedBook(String enchantID, String level, int success) {
        MythicEnchant enchant = MythicEnchants.inst().getEnchantManager().getEnchantments().get(enchantID);
        if (enchant != null) {
            ItemStack enchantBook;
            int successChance = success >= 0 ? success : Math.min(100, Files.getConfig().contains("MythicEnchantsAddon.EnchantInfo." + enchantID + ".SuccessChance")
                    ? Files.getConfig().getInt("MythicEnchantsAddon.EnchantInfo." + enchantID + ".SuccessChance") :
                    Files.getConfig().getInt("MythicEnchantsAddon.EnchantInfo.DefaultSettings.SuccessChance", 70));
            if (Files.getConfig().contains("MythicEnchantsAddon.EnchantInfo." + enchantID + ".CustomLore")) {
                enchantBook = MythicEnchants.inst().getEnchantManager().applyToItem(new ItemEditor(
                        Material.valueOf(Files.getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                        .addAllLore((Files.getConfig().getStringList("MythicEnchantsAddon.EnchantInfo." + enchantID + ".CustomLore")
                                .stream().map(s -> s.replace("<enchant>", enchant.getDisplayName())
                                        .replace("<level>", level)
                                        .replace("<success_chance>", String.valueOf(successChance))
                                        .replace("<fail_chance>", String.valueOf(100 - successChance))
                                ).collect(Collectors.toList())))
                        .setName(Objects.requireNonNull(Files.getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                .replace("<enchant>", enchant.getDisplayName())
                                .replace("<level>", level)
                                .replace("<success_chance>", String.valueOf(successChance))
                                .replace("<fail_chance>", String.valueOf(100 - successChance)))
                        .hideFlagAll()
                        .setUnbreakable(true)
                        .toItemStack(), enchant, Number.getInteger(level));
            } else if (Files.getConfig().contains("MythicEnchantsAddon.EnchantInfo.DefaultSettings.DefaultLore")) {
                enchantBook = MythicEnchants.inst().getEnchantManager().applyToItem(new ItemEditor(
                        Material.valueOf(Files.getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                        .addAllLore((Files.getConfig().getStringList("MythicEnchantsAddon.EnchantInfo.DefaultSettings.DefaultLore")
                                .stream().map(s -> s.replace("<enchant>", enchant.getDisplayName())
                                        .replace("<level>", level)
                                        .replace("<success_chance>", String.valueOf(successChance))
                                        .replace("<fail_chance>", String.valueOf(100 - successChance))
                                ).collect(Collectors.toList())))
                        .setName(Objects.requireNonNull(Files.getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                .replace("<enchant>", enchant.getDisplayName())
                                .replace("<level>", level)
                                .replace("<success_chance>", String.valueOf(successChance))
                                .replace("<fail_chance>", String.valueOf(100 - successChance)))
                        .hideFlagAll()
                        .setUnbreakable(true)
                        .toItemStack(), enchant, Number.getInteger(level));
            } else {
                enchantBook = MythicEnchants.inst().getEnchantManager().applyToItem(new ItemEditor(
                        Material.valueOf(Files.getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                        .setName(Objects.requireNonNull(Files.getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                .replace("<enchant>", enchant.getDisplayName())
                                .replace("<level>", level)
                                .replace("<success_chance>", String.valueOf(successChance))
                                .replace("<fail_chance>", String.valueOf(100 - successChance)))
                        .hideFlagAll()
                        .setUnbreakable(true)
                        .toItemStack(), enchant, Number.getInteger(level));
            }
            if (Files.getConfig().contains("MythicEnchantsAddon.EnchantedBook.CustomModelData"))
                enchantBook = new ItemEditor(enchantBook)
                        .setCustomModelData(Files.getConfig().getInt("MythicEnchantsAddon.EnchantedBook.CustomModelData"))
                        .toItemStack();
            NBT.modify(enchantBook, readWriteItemNBT -> {
                readWriteItemNBT.setString("mythicenchantsaddon_enchant_id", enchantID);
                readWriteItemNBT.setInteger("mythicenchantsaddon_enchant_level", Number.getInteger(level));
                readWriteItemNBT.setInteger("mythicenchantsaddon_success_chance", successChance);
                readWriteItemNBT.setString("mythicenchantsaddon_item_type", "enchanted_book");
            });
            return enchantBook;
        } else if (Enchantment.getByKey(NamespacedKey.minecraft(enchantID)) != null) {
            ItemStack itemStack;
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantID));
            int successChance = success >= 0 ? Math.min(success, 100) : 100;
            if (enchantment != null) {
                if (Files.getConfig().contains("MythicEnchantsAddon.EnchantInfo.DefaultSettings.DefaultLore")) {
                    itemStack = new ItemEditor(
                            Material.valueOf(Files.getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                            .addAllLore((Files.getConfig().getStringList("MythicEnchantsAddon.EnchantInfo.DefaultSettings.DefaultLore")
                                    .stream().map(s -> s.replace("<enchant>", Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " ")))
                                            .replace("<level>", level)
                                            .replace("<success_chance>", String.valueOf(successChance))
                                            .replace("<fail_chance>", String.valueOf(100 - successChance))
                                    ).collect(Collectors.toList())))
                            .setName(Objects.requireNonNull(Files.getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                    .replace("<enchant>", Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " ")))
                                    .replace("<level>", level)
                                    .replace("<success_chance>", String.valueOf(successChance))
                                    .replace("<fail_chance>", String.valueOf(100 - successChance)))
                            .hideFlagAll()
                            .setUnbreakable(true)
                            .toItemStack();
                } else {
                    itemStack = new ItemEditor(
                            Material.valueOf(Files.getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                            .setName(Objects.requireNonNull(Files.getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                    .replace("<enchant>", Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " ")))
                                    .replace("<level>", level)
                                    .replace("<success_chance>", String.valueOf(successChance))
                                    .replace("<fail_chance>", String.valueOf(100 - successChance)))
                            .hideFlagAll()
                            .setUnbreakable(true)
                            .toItemStack();
                }
                if (Files.getConfig().contains("MythicEnchantsAddon.EnchantedBook.CustomModelData"))
                    itemStack = new ItemEditor(itemStack)
                            .setCustomModelData(Files.getConfig().getInt("MythicEnchantsAddon.EnchantedBook.CustomModelData"))
                            .toItemStack();
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.addEnchant(enchantment, Number.getInteger(level), true);
                itemStack.setItemMeta(itemMeta);
                NBT.modify(itemStack, readWriteItemNBT -> {
                    readWriteItemNBT.setString("mythicenchantsaddon_enchant_id", enchantID);
                    readWriteItemNBT.setInteger("mythicenchantsaddon_enchant_level", Number.getInteger(level));
                    readWriteItemNBT.setInteger("mythicenchantsaddon_success_chance", successChance);
                    readWriteItemNBT.setString("mythicenchantsaddon_item_type", "enchanted_book");
                });
                return itemStack;
            }
        }
        return null;
    }
}
