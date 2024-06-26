package net.danh.mythicenchantsaddon;

import io.lumine.mythicenchants.MythicEnchants;
import net.Indyuce.mmoitems.MMOItems;
import net.danh.mythicenchantsaddon.cmd.MEA_CMD;
import net.danh.mythicenchantsaddon.events.EnchantsMEA;
import net.danh.mythicenchantsaddon.resources.Files;
import net.danh.mythicenchantsaddon.stats.LimitMythicEnchants;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
        Files.loadFileAddon();
    }

    @Override
    public void onDisable() {
    }
}
