package net.danh.mythicenchantsaddon.cmd;


import de.tr7zw.changeme.nbtapi.NBT;
import io.lumine.mythicenchants.MythicEnchants;
import io.lumine.mythicenchants.enchants.EnchantManager;
import net.danh.mythicenchantsaddon.resources.Chat;
import net.danh.mythicenchantsaddon.resources.Files;
import net.danh.mythicenchantsaddon.resources.Number;
import net.danh.mythicenchantsaddon.utils.ConfigFile;
import net.danh.mythicenchantsaddon.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MEA_CMD extends CMDBase {
    public MEA_CMD() {
        super("meaddon");
    }

    @Override
    public void execute(@NotNull CommandSender c, String[] args) {
        if (c.hasPermission("meaddon.admin")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    Files.getConfig().getStringList("MythicEnchantsAddon.Message.Command").forEach(s -> c.sendMessage(Chat.colorize(s)));
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    new ConfigFile().reload();
                    c.sendMessage(Chat.colorize("<aqua>Reload completed"));
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("successChanceBook")) {
                    Player p = Bukkit.getPlayer(args[1]);
                    int chance = Number.getInteger(args[2]);
                    if (p != null && chance > 0) {
                        p.getInventory().addItem(Items.getSuccessChanceBook(chance));
                        p.sendMessage(Chat.colorize(Files.getConfig().getString(
                                        "MythicEnchantsAddon.SuccessChance.ItemSetting.Message.Receive"
                                )
                                .replace("<name>", Objects.requireNonNull(
                                        Files.getConfig().getString("MythicEnchantsAddon.SuccessChance.ItemSetting.Display")))));
                    }
                }
            }
            if (args.length == 1 || args.length == 2) {
                if (args[0].equalsIgnoreCase("format-lore")) {
                    Player p = args.length == 2 ? Bukkit.getPlayer(args[1]) : c instanceof Player ? ((Player) c).getPlayer() : null;
                    if (p != null) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                EnchantManager enchantManager = MythicEnchants.inst().getEnchantManager();
                                ItemStack itemStack = p.getInventory().getItemInMainHand();
                                for (String mythicEnchantID : enchantManager.getEnchantments().keySet()) {
                                    int level = NBT.get(itemStack, readableItemNBT -> {
                                        return readableItemNBT.getInteger("mythicenchantsaddon_enchant_" + mythicEnchantID);
                                    });
                                    if (level > 0) {
                                        itemStack.setItemMeta(enchantManager.applyToItem(itemStack, enchantManager.getEnchantments().get(mythicEnchantID), level).getItemMeta());
                                    }
                                }
                                for (Enchantment enchantment : Enchantment.values()) {
                                    String enchantID = enchantment.getKey().getKey();
                                    int level = NBT.get(itemStack, readableItemNBT -> {
                                        return readableItemNBT.getInteger("mythicenchantsaddon_enchant_" + enchantID);
                                    });
                                    if (level > 0) {
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "goop nbt enchantment " + p.getName() + " hand " + enchantID + " " + level);
                                    }
                                }
                                StringBuilder mapAsString = new StringBuilder("&7");
                                AtomicInteger enchant = new AtomicInteger(0);
                                if (!itemStack.getEnchantments().isEmpty()) {
                                    itemStack.getEnchantments().forEach((enchantment, integer) -> enchantManager.toMythicEnchantment(enchantment).ifPresentOrElse(mythicEnchant -> {
                                        mapAsString.append(mythicEnchant.getDisplayName()).append(" ").append(integer).append(", ");
                                        enchant.getAndIncrement();
                                        if (enchant.get() > 0 && enchant.get() % 2 == 0)
                                            mapAsString.append("<newline>&7");
                                    }, () -> {
                                        mapAsString.append(Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " "))).append(" ").append(integer).append(", ");
                                        enchant.getAndIncrement();
                                        if (enchant.get() > 0 && enchant.get() % 2 == 0)
                                            mapAsString.append("<newline>&7");
                                    }));
                                }
                                String lore = mapAsString.toString();
                                if (lore.endsWith("<newline>&7")) lore = lore.trim().substring(0, lore.length() - 13);
                                if (lore.endsWith(", ")) lore = lore.trim().substring(0, lore.length() - 2);
                                MythicEnchants.inst().getServer().dispatchCommand(Bukkit.getConsoleSender(), "goop nbt removeLore top " + p.getName() + " hand");
                                MythicEnchants.inst().getServer().dispatchCommand(Bukkit.getConsoleSender(), "goop nbt addLore top " + p.getName() + " hand " + lore);
                            }
                        }.runTask(MythicEnchants.inst());
                    }
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("format-lore")) {
                    Player p = Bukkit.getPlayer(args[1]);
                    int slot = Number.getInteger(args[2]);
                    if (p != null) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                EnchantManager enchantManager = MythicEnchants.inst().getEnchantManager();
                                ItemStack itemStack = p.getInventory().getItem(slot);
                                if (itemStack != null) {
                                    for (String mythicEnchantID : enchantManager.getEnchantments().keySet()) {
                                        int level = NBT.get(itemStack, readableItemNBT -> {
                                            return readableItemNBT.getInteger("mythicenchantsaddon_enchant_" + mythicEnchantID);
                                        });
                                        if (level > 0) {
                                            itemStack.setItemMeta(enchantManager.applyToItem(itemStack, enchantManager.getEnchantments().get(mythicEnchantID), level).getItemMeta());
                                        }
                                    }
                                    for (Enchantment enchantment : Enchantment.values()) {
                                        String enchantID = enchantment.getKey().getKey();
                                        int level = NBT.get(itemStack, readableItemNBT -> {
                                            return readableItemNBT.getInteger("mythicenchantsaddon_enchant_" + enchantID);
                                        });
                                        if (level > 0) {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "goop nbt enchantment " + p.getName() + " " + slot + " " + enchantID + " " + level);
                                        }
                                    }
                                    StringBuilder mapAsString = new StringBuilder("&7");
                                    AtomicInteger enchant = new AtomicInteger(0);
                                    if (!itemStack.getEnchantments().isEmpty()) {
                                        itemStack.getEnchantments().forEach((enchantment, integer) -> enchantManager.toMythicEnchantment(enchantment).ifPresentOrElse(mythicEnchant -> {
                                            mapAsString.append(mythicEnchant.getDisplayName()).append(" ").append(integer).append(", ");
                                            enchant.getAndIncrement();
                                            if (enchant.get() > 0 && enchant.get() % 2 == 0)
                                                mapAsString.append("<newline>&7");
                                        }, () -> {
                                            mapAsString.append(Chat.caseOnWords(enchantment.getKey().getKey().replace("_", " "))).append(" ").append(integer).append(", ");
                                            enchant.getAndIncrement();
                                            if (enchant.get() > 0 && enchant.get() % 2 == 0)
                                                mapAsString.append("<newline>&7");
                                        }));
                                    }
                                    String lore = mapAsString.toString();
                                    if (lore.endsWith("<newline>&7"))
                                        lore = lore.trim().substring(0, lore.length() - 13);
                                    if (lore.endsWith(", ")) lore = lore.trim().substring(0, lore.length() - 2);
                                    MythicEnchants.inst().getServer().dispatchCommand(Bukkit.getConsoleSender(), "goop nbt removeLore top " + p.getName() + " " + slot);
                                    MythicEnchants.inst().getServer().dispatchCommand(Bukkit.getConsoleSender(), "goop nbt addLore top " + p.getName() + " " + slot + " " + lore);
                                }
                            }
                        }.runTask(MythicEnchants.inst());
                    }
                }
            }
            if (args.length == 4 || args.length == 5) {
                if (args[0].equalsIgnoreCase("give")) {
                    Player p = Bukkit.getPlayer(args[1]);
                    int success = args.length == 5 ? Number.getInteger(args[4]) : -1;
                    if (p != null) {
                        ItemStack enchantBook = Items.getEnchantedBook(args[2], args[3], success);
                        if (enchantBook != null) {
                            String enchantID = NBT.get(enchantBook, readableItemNBT -> {
                                return readableItemNBT.getString("mythicenchantsaddon_enchant_id");
                            });
                            int level = NBT.get(enchantBook, readableItemNBT -> {
                                return readableItemNBT.getInteger("mythicenchantsaddon_enchant_level");
                            });
                            int successChance = NBT.get(enchantBook, readableItemNBT -> {
                                return readableItemNBT.getInteger("mythicenchantsaddon_success_chance");
                            });
                            EnchantManager enchantManager = MythicEnchants.inst().getEnchantManager();
                            p.getInventory().addItem(enchantBook);
                            p.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getConfig().getString("MythicEnchantsAddon.Message.ReceiveEnchantedBook"))
                                    .replace("<name>",
                                            enchantManager.getEnchantments().get(args[2]) != null ? enchantManager.getEnchantments().get(enchantID).getDisplayName()
                                                    : Chat.caseOnWords(args[2].replace("_", " ")))
                                    .replace("<level>", String.valueOf(level))
                                    .replace("<chance>", String.valueOf(successChance))));
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> TabComplete(CommandSender sender, @NotNull String @NotNull [] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("meaddon.admin")) {
                commands.add("reload");
                commands.add("give");
                commands.add("help");
                commands.add("successChanceBook");
                commands.add("format-lore");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if (sender.hasPermission("meaddon.admin")) {
                if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("format-lore")
                        || args[0].equalsIgnoreCase("successChanceBook")) {
                    Bukkit.getOnlinePlayers().forEach(player -> commands.add(player.getName()));
                }
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        if (args.length == 3) {
            if (sender.hasPermission("meaddon.admin")) {
                if (args[0].equalsIgnoreCase("give")) {
                    MythicEnchants.inst().getEnchantManager().getEnchantments().forEach((s, mythicEnchant) -> commands.add(s));
                    for (Enchantment enchantment : Enchantment.values()) {
                        if (!commands.contains(enchantment.getKey().getKey().toUpperCase()))
                            commands.add(enchantment.getKey().getKey());
                    }
                }
                if (args[0].equalsIgnoreCase("successChanceBook")) {
                    for (int i = 0; i <= 100; i += 25)
                        commands.add(String.valueOf(i));
                }
            }
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }
        if (args.length == 4) {
            if (sender.hasPermission("meaddon.admin")) {
                if (args[0].equalsIgnoreCase("give")) {
                    for (int i = 1; i <= 10; i++)
                        commands.add(String.valueOf(i));
                }
            }
            StringUtil.copyPartialMatches(args[3], commands, completions);
        }
        if (args.length == 5) {
            if (sender.hasPermission("meaddon.admin")) {
                if (args[0].equalsIgnoreCase("give")) {
                    for (int i = 0; i <= 100; i += 25)
                        commands.add(String.valueOf(i));
                }
            }
            StringUtil.copyPartialMatches(args[4], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
