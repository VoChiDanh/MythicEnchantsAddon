package net.danh.mythicenchantsaddon.utils;

import io.lumine.mythicenchants.MythicEnchants;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile {
    
    private File playerdataFile;
    private FileConfiguration playerdata;

    public FileConfiguration get() {
        return playerdata;
    }

    public void reload() {
        playerdataFile = new File(MythicEnchants.inst().getDataFolder(), "config.yml");
        playerdata = new YamlConfiguration();
        try {
            playerdata.load(playerdataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        playerdata = YamlConfiguration.loadConfiguration(playerdataFile);
    }

    public void save() {
        try {
            playerdata.save(playerdataFile);
        } catch (IOException ignored) {
        }
    }
}
