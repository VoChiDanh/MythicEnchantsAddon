package net.danh.mythicenchantsaddon.resources;

import io.lumine.mythicenchants.MythicEnchants;
import net.danh.mythicenchantsaddon.utils.ConfigFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Files {

    public static FileConfiguration getConfig() {
        return MythicEnchants.inst().getConfig();
    }

    public static void loadFileAddon() {
        File configFile = new File(MythicEnchants.inst().getDataFolder(), "config.yml");
        FileConfiguration fileConfiguration = getConfig();
        if (!fileConfiguration.contains("MythicEnchantsAddon.EnchantedBook.Material"))
            fileConfiguration.set("MythicEnchantsAddon.EnchantedBook.Material", "ENCHANTED_BOOK");
        if (!fileConfiguration.contains("MythicEnchantsAddon.EnchantedBook.Display"))
            fileConfiguration.set("MythicEnchantsAddon.EnchantedBook.Display", "&6Enchanted Mythic Book");
        if (!fileConfiguration.contains("MythicEnchantsAddon.EnchantedBook.CustomModelData"))
            fileConfiguration.set("MythicEnchantsAddon.EnchantedBook.CustomModelData", 0);
        if (!fileConfiguration.contains("MythicEnchantsAddon.Message.CannotEnchant"))
            fileConfiguration.set("MythicEnchantsAddon.Message.CannotEnchant", "&cEnchant <enchant> can't be used on this item");
        if (!fileConfiguration.contains("MythicEnchantsAddon.Message.LimitEnchants"))
            fileConfiguration.set("MythicEnchantsAddon.Message.LimitEnchants", "&cThis item has been reached the limit of enchants (<limit>)");
        if (!fileConfiguration.contains("MythicEnchantsAddon.Message.EnchantFailed"))
            fileConfiguration.set("MythicEnchantsAddon.Message.EnchantFailed", "&cThe enchantment <enchantment> <level> has failed!");
        if (!fileConfiguration.contains("MythicEnchantsAddon.Message.EnchantSuccess"))
            fileConfiguration.set("MythicEnchantsAddon.Message.EnchantSuccess", "&aThe enchantment <enchantment> <level> is successful and applied to the item!");
        if (!fileConfiguration.contains("MythicEnchantsAddon.Message.ReceiveEnchantedBook"))
            fileConfiguration.set("MythicEnchantsAddon.Message.ReceiveEnchantedBook", "&aYou got Enchanted Book <name> <level> with <chance>% success!");
        if (!fileConfiguration.contains("MythicEnchantsAddon.Settings.DefaultLimitEnchants"))
            fileConfiguration.set("MythicEnchantsAddon.Settings.DefaultLimitEnchants", 10);
        if (!fileConfiguration.contains("MythicEnchantsAddon.Message.Command")) {
            List<String> listCMD = new ArrayList<>();
            listCMD.add("&6/meaddon give <player> <enchantID> <level> [successChance]- Give Enchantments Books");
            listCMD.add("&6/meaddon successChanceBook <player> <successChance>- Give Success Chance Books");
            listCMD.add("&6/meaddon format-lore [player] - Format lore for item");
            listCMD.add("&6/meaddon reload - Reload Config");
            fileConfiguration.set("MythicEnchantsAddon.Message.Command", listCMD);
        }
        if (!fileConfiguration.contains("MythicEnchantsAddon.EnchantInfo")) {
            List<String> listLore = new ArrayList<>();
            listLore.add("&8");
            listLore.add("&7Makes your target slow down and poisoned");
            listLore.add("&8");
            listLore.add("&aSuccess Chance: <success_chance>%");
            listLore.add("&cFail Chance: <fail_chance>%");
            listLore.add("&8");
            listLore.add("&7Drag and drop onto the item to apply");
            fileConfiguration.set("MythicEnchantsAddon.EnchantInfo.VENOMOUS.CustomLore", listLore);
        }
        for (String enchantID : MythicEnchants.inst().getEnchantManager().getEnchantments().keySet()) {
            if (!fileConfiguration.contains("MythicEnchantsAddon.EnchantInfo." + enchantID + ".SuccessChance")) {
                fileConfiguration.set("MythicEnchantsAddon.EnchantInfo." + enchantID + ".SuccessChance", ThreadLocalRandom.current().nextInt(101));
            }
        }
        if (!fileConfiguration.contains("MythicEnchantsAddon.EnchantInfo.DefaultSettings.DefaultLore")) {
            List<String> listLore = new ArrayList<>();
            listLore.add("&8");
            listLore.add("&aSuccess Chance: <success_chance>%");
            listLore.add("&cFail Chance: <fail_chance>%");
            listLore.add("&8");
            listLore.add("&7Drag and drop onto the item to apply");
            fileConfiguration.set("MythicEnchantsAddon.EnchantInfo.DefaultSettings.DefaultLore", listLore);
            fileConfiguration.set("MythicEnchantsAddon.EnchantInfo.DefaultSettings.SuccessChance", ThreadLocalRandom.current().nextInt(101));
        }
        if (!fileConfiguration.contains("MythicEnchantsAddon.SuccessChance.ItemSetting.Material"))
            fileConfiguration.set("MythicEnchantsAddon.SuccessChance.ItemSetting.Material", "BOOK");
        if (!fileConfiguration.contains("MythicEnchantsAddon.SuccessChance.ItemSetting.Display"))
            fileConfiguration.set("MythicEnchantsAddon.SuccessChance.ItemSetting.Display", "&6Increase Success Chance Book");
        if (!fileConfiguration.contains("MythicEnchantsAddon.SuccessChance.ItemSetting.CustomModelData"))
            fileConfiguration.set("MythicEnchantsAddon.SuccessChance.ItemSetting.CustomModelData", 0);
        if (!fileConfiguration.contains("MythicEnchantsAddon.SuccessChance.ItemSetting.Message.Receive"))
            fileConfiguration.set("MythicEnchantsAddon.SuccessChance.ItemSetting.Message.Receive", "&aYou got <name>!");
        if (!fileConfiguration.contains("MythicEnchantsAddon.SuccessChance.ItemSetting.Message.Success"))
            fileConfiguration.set("MythicEnchantsAddon.SuccessChance.ItemSetting.Message.Success", "&aEnchant <enchant> <level> was increase success chance from <old_chance>% to <chance>%!");
        if (!fileConfiguration.contains("MythicEnchantsAddon.SuccessChance.ItemSetting.Lore")) {
            List<String> listLore = new ArrayList<>();
            listLore.add("&8");
            listLore.add("&7Use to increase <chance>% success chance of enchant book");
            listLore.add("&8");
            listLore.add("&7Drag and drop onto the enchant book to apply");
            fileConfiguration.set("MythicEnchantsAddon.SuccessChance.ItemSetting.Lore", listLore);
        }
        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new ConfigFile().reload();
        YamlConfiguration.loadConfiguration(configFile);
        MythicEnchants.inst().getLogger().info("Added addon settings to config.yml !");
    }
}
