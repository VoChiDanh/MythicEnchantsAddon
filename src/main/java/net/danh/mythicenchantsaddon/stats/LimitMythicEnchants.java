package net.danh.mythicenchantsaddon.stats;

import net.Indyuce.mmoitems.stat.type.DoubleStat;
import org.bukkit.Material;

public class LimitMythicEnchants extends DoubleStat {
    public LimitMythicEnchants() {
        super("MYTHIC_LIMIT_ENCHANTS",
                Material.ENCHANTED_BOOK,
                "Limit MythicEnchants",
                new String[]{"Add number to limit enchants"},
                new String[]{"!block", "all"});
    }
}
