package net.danh.mythicenchantsaddon;

import io.lumine.mythicenchants.MythicEnchants;
import net.Indyuce.mmoitems.MMOItems;
import net.danh.mythicenchantsaddon.cmd.MEA_CMD;
import net.danh.mythicenchantsaddon.events.EnchantsMEA;
import net.danh.mythicenchantsaddon.stats.LimitMythicEnchants;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MythicEnchantsAddon extends JavaPlugin {

    private static MythicEnchantsAddon mythicEnchantsAddon;

    public static MythicEnchantsAddon getMythicEnchantsAddon() {
        return mythicEnchantsAddon;
    }

    @Override
    public void onLoad() {
        mythicEnchantsAddon = this;
        MMOItems.plugin.getStats().register(new LimitMythicEnchants());
    }

    @Override
    public void onEnable() {
        new MEA_CMD();
        Bukkit.getPluginManager().registerEvents(new EnchantsMEA(), MythicEnchants.inst());
        File configFile = new File(MythicEnchants.inst().getDataFolder(), "config.yml");
        FileConfiguration fileConfiguration = MythicEnchants.inst().getConfig();
        if (!fileConfiguration.contains("MythicEnchantsAddon.EnchantedBook.Material"))
            fileConfiguration.set("MythicEnchantsAddon.EnchantedBook.Material", "ENCHANTED_BOOK");
        if (!fileConfiguration.contains("MythicEnchantsAddon.EnchantedBook.Display"))
            fileConfiguration.set("MythicEnchantsAddon.EnchantedBook.Display", "&6Enchanted Mythic Book");
        if (!fileConfiguration.contains("MythicEnchantsAddon.Message.CannotEnchant"))
            fileConfiguration.set("MythicEnchantsAddon.Message.CannotEnchant", "&cEnchant <enchant> can't be used on this item");
        if (!fileConfiguration.contains("MythicEnchantsAddon.Message.LimitEnchants"))
            fileConfiguration.set("MythicEnchantsAddon.Message.LimitEnchants", "&cThis item has been reached the limit of enchants (<limit>)");
        if (!fileConfiguration.contains("MythicEnchantsAddon.Settings.DefaultLimitEnchants"))
            fileConfiguration.set("MythicEnchantsAddon.Settings.DefaultLimitEnchants", 10);
        if (!fileConfiguration.contains("MythicEnchantsAddon.Message.Command")) {
            List<String> listCMD = new ArrayList<>();
            listCMD.add("&6/meaddon give <player> <enchantID> <level> - Give Enchantments Books");
            listCMD.add("&6/meaddon format-lore [player] - Format lore for item");
            fileConfiguration.set("MythicEnchantsAddon.Message.Command", listCMD);
        }
        if (!fileConfiguration.contains("MythicEnchantsAddon.EnchantInfo.DefaultLore")) {
            List<String> listLore = new ArrayList<>();
            listLore.add("&8");
            listLore.add("&7Drag and drop onto the item to apply");
            fileConfiguration.set("MythicEnchantsAddon.EnchantInfo.DefaultLore", listLore);
        }
        if (!fileConfiguration.contains("MythicEnchantsAddon.EnchantInfo.CustomLore")) {
            List<String> listLore = new ArrayList<>();
            listLore.add("&8");
            listLore.add("&7Makes your target slow down and poisoned");
            listLore.add("&8");
            listLore.add("&7This enchant can be applied for sword");
            listLore.add("&8");
            listLore.add("&7Drag and drop onto the item to apply");
            fileConfiguration.set("MythicEnchantsAddon.EnchantInfo.CustomLore.VENOMOUS", listLore);
        }
        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        YamlConfiguration.loadConfiguration(configFile);
        MythicEnchants.inst().getLogger().info("Added addon settings to config.yml !");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
