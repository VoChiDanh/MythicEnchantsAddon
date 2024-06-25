package net.danh.mythicenchantsaddon.events;

import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythicenchants.MythicEnchants;
import io.lumine.mythicenchants.enchants.EnchantManager;
import net.danh.mythicenchantsaddon.resources.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class EnchantsMEA implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEnchants(@NotNull InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p) {
            if (e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)) {
                ItemStack enchantBook = e.getCursor();
                AtomicReference<ItemStack> itemStack = new AtomicReference<>(e.getCurrentItem());
                NBTItem nbtItem = NBTItem.get(itemStack.get());
                EnchantManager enchantManager = MythicEnchants.inst().getEnchantManager();
                if (!enchantManager.getMythicEnchants(enchantBook).isEmpty()
                        && enchantBook.getType() == Material.valueOf(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material"))
                        && e.getCurrentItem() != null) {
                    int enchantLimits = nbtItem.hasTag("MMOITEMS_MYTHIC_LIMIT_ENCHANTS") ? nbtItem.getInteger("MMOITEMS_MYTHIC_LIMIT_ENCHANTS") : MythicEnchants.inst().getConfig().getInt("MythicEnchantsAddon.Settings.DefaultLimitEnchants", 10);
                    int enchants = itemStack.get().getEnchantments().isEmpty() ? 0 : itemStack.get().getEnchantments().size();
                    int preEnchants = enchantManager.getMythicEnchants(enchantBook).size();
                    if (preEnchants + enchants <= enchantLimits) {
                        enchantManager.getMythicEnchants(enchantBook).forEach((mythicEnchant, integer) -> {
                            if (mythicEnchant.canEnchantItem(itemStack.get())) {
                                itemStack.set(enchantManager.applyToItem(itemStack.get(), mythicEnchant, integer));
                                e.setCursor(new ItemStack(Material.AIR));
                            } else
                                p.sendMessage(Chat.colorize(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.Message.CannotEnchant"))
                                        .replace("<enchant>", mythicEnchant.getDisplayName())));
                        });
                    } else {
                        p.sendMessage(Chat.colorize(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.Message.LimitEnchants"))
                                .replace("<limit>", String.valueOf(enchantLimits))));
                    }
                }
            }
        }
    }
}
