package net.danh.mythicenchantsaddon.events;

import de.tr7zw.changeme.nbtapi.NBT;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythicenchants.MythicEnchants;
import io.lumine.mythicenchants.enchants.EnchantManager;
import io.lumine.mythicenchants.enchants.MythicEnchant;
import net.danh.mythicenchantsaddon.resources.Chat;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantsMEA implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEnchants(@NotNull InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p) {
            if (e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)) {
                ItemStack enchantBook = e.getCursor();
                NBTItem nbtItem = NBTItem.get(e.getCurrentItem());
                EnchantManager enchantManager = MythicEnchants.inst().getEnchantManager();
                if (NBT.get(enchantBook, readableItemNBT -> {
                    return readableItemNBT.hasTag("mythicenchantsaddon_enchant_id");
                })
                        && enchantBook.getType() == Material.valueOf(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material"))
                        && e.getCurrentItem() != null) {
                    int enchantLimits = nbtItem.hasTag("MMOITEMS_MYTHIC_LIMIT_ENCHANTS")
                            ? nbtItem.getInteger("MMOITEMS_MYTHIC_LIMIT_ENCHANTS")
                            : MythicEnchants.inst().getConfig().getInt("MythicEnchantsAddon.Settings.DefaultLimitEnchants", 10);
                    int enchants = e.getCurrentItem().getEnchantments().isEmpty() ? 0 : e.getCurrentItem().getEnchantments().size();
                    int preEnchants = enchantBook.getEnchantments().size();
                    if (preEnchants + enchants <= enchantLimits) {
                        String enchantID = NBT.modify(enchantBook, readableItemNBT -> {
                            return readableItemNBT.getString("mythicenchantsaddon_enchant_id");
                        });
                        int level = NBT.modify(enchantBook, readableItemNBT -> {
                            return readableItemNBT.getInteger("mythicenchantsaddon_enchant_level");
                        });
                        int successChance = NBT.modify(enchantBook, readableItemNBT -> {
                            return readableItemNBT.getInteger("mythicenchantsaddon_success_chance");
                        });

                        MythicEnchant enchant = enchantManager.getEnchantments().get(enchantID);
                        if (enchant != null) {
                            if (ThreadLocalRandom.current().nextInt(101) < successChance) {
                                if (enchant.canEnchantItem(e.getCurrentItem())) {
                                    NBT.modify(e.getCurrentItem(), readableItemNBT -> {
                                        readableItemNBT.setInteger("mythicenchantsaddon_enchant_" + enchantID, level);
                                    });
                                    p.sendMessage(Chat.colorize(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.Message.EnchantSuccess"))
                                            .replace("<enchantment>", enchant.getDisplayName())
                                            .replace("<level>", String.valueOf(level))));
                                    e.setCancelled(true);
                                    e.setCursor(new ItemStack(Material.AIR));
                                } else
                                    p.sendMessage(Chat.colorize(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.Message.CannotEnchant"))
                                            .replace("<enchant>", enchant.getDisplayName())));
                            } else {
                                p.sendMessage(Chat.colorize(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.Message.EnchantFailed"))
                                        .replace("<enchantment>", enchant.getDisplayName())
                                        .replace("<level>", String.valueOf(level))));
                                e.setCancelled(true);
                                e.setCursor(new ItemStack(Material.AIR));
                            }
                        } else if (Enchantment.getByKey(NamespacedKey.minecraft(enchantID)) != null) {
                            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantID));
                            if (enchantment != null) {
                                if (ThreadLocalRandom.current().nextInt(101) < successChance) {
                                    if (enchantment.canEnchantItem(e.getCurrentItem())) {
                                        NBT.modify(e.getCurrentItem(), readableItemNBT -> {
                                            readableItemNBT.setInteger("mythicenchantsaddon_enchant_" + enchantID, level);
                                        });
                                        p.sendMessage(Chat.colorize(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.Message.EnchantSuccess"))
                                                .replace("<enchantment>", Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " ")))
                                                .replace("<level>", String.valueOf(level))));
                                        e.setCancelled(true);
                                        e.setCursor(new ItemStack(Material.AIR));
                                    } else
                                        p.sendMessage(Chat.colorize(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.Message.CannotEnchant"))
                                                .replace("<enchant>", Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " ")))));
                                } else {
                                    p.sendMessage(Chat.colorize(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.Message.EnchantFailed"))
                                            .replace("<enchantment>", Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " ")))
                                            .replace("<level>", String.valueOf(level))));
                                    e.setCancelled(true);
                                    e.setCursor(new ItemStack(Material.AIR));
                                }
                            }
                        }
                    } else {
                        p.sendMessage(Chat.colorize(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.Message.LimitEnchants"))
                                .replace("<limit>", String.valueOf(enchantLimits))));
                    }
                }
            }
        }
    }
}
