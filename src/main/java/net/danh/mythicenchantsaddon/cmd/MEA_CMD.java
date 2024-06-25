package net.danh.mythicenchantsaddon.cmd;


import io.lumine.mythicenchants.MythicEnchants;
import io.lumine.mythicenchants.enchants.EnchantManager;
import io.lumine.mythicenchants.enchants.MythicEnchant;
import net.danh.mythicenchantsaddon.resources.Chat;
import net.danh.mythicenchantsaddon.resources.Number;
import net.danh.mythicenchantsaddon.utils.ItemEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
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
                    MythicEnchants.inst().getConfig().getStringList("MythicEnchantsAddon.Message.Command").forEach(s -> c.sendMessage(Chat.colorize(s)));
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
                                StringBuilder mapAsString = new StringBuilder("&7");
                                AtomicInteger enchant = new AtomicInteger(0);
                                if (!p.getInventory().getItemInMainHand().getEnchantments().isEmpty()) {
                                    p.getInventory().getItemInMainHand().getEnchantments().forEach((enchantment, integer) -> {
                                        if (enchantManager.toMythicEnchantment(enchantment).isPresent()) {
                                            enchantManager.toMythicEnchantment(enchantment).ifPresent(mythicEnchant -> {
                                                mapAsString.append(mythicEnchant.getDisplayName()).append(" ").append(integer).append(", ");
                                                enchant.getAndIncrement();
                                                if (enchant.get() > 0 && enchant.get() % 2 == 0)
                                                    mapAsString.append("<newline>&7");
                                            });
                                        }
                                    });
                                }
                                MythicEnchants.inst().getServer().dispatchCommand(Bukkit.getConsoleSender(), "goop nbt removeLore top " + p.getName() + " hand");
                                MythicEnchants.inst().getServer().dispatchCommand(Bukkit.getConsoleSender(), "goop nbt addLore top " + p.getName() + " hand " + mapAsString.toString());
                            }
                        }.runTask(MythicEnchants.inst());
                    }
                }
            }
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    Player p = Bukkit.getPlayer(args[1]);
                    MythicEnchant enchant = MythicEnchants.inst().getEnchantManager().getEnchantments().get(args[2]);
                    if (p != null && enchant != null) {
                        ItemStack enchantBook;
                        if (MythicEnchants.inst().getConfig().contains("MythicEnchantsAddon.EnchantInfo.CustomLore." + args[2])) {
                            enchantBook = MythicEnchants.inst().getEnchantManager().applyToItem(new ItemEditor(
                                    Material.valueOf(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                                    .addAllLore((MythicEnchants.inst().getConfig().getStringList("MythicEnchantsAddon.EnchantInfo.CustomLore." + args[2])))
                                    .setName(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                            .replace("<enchant>", enchant.getDisplayName())
                                            .replace("<level>", args[3]))
                                    .toItemStack(), enchant, Number.getInteger(args[3]));
                        } else if (MythicEnchants.inst().getConfig().contains("MythicEnchantsAddon.EnchantInfo.DefaultLore")) {
                            enchantBook = MythicEnchants.inst().getEnchantManager().applyToItem(new ItemEditor(
                                    Material.valueOf(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                                    .addAllLore((MythicEnchants.inst().getConfig().getStringList("MythicEnchantsAddon.EnchantInfo.DefaultLore")))
                                    .setName(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                            .replace("<enchant>", enchant.getDisplayName())
                                            .replace("<level>", args[3])).toItemStack(), enchant, Number.getInteger(args[3]));
                        } else {
                            enchantBook = MythicEnchants.inst().getEnchantManager().applyToItem(new ItemEditor(
                                    Material.valueOf(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Material")))
                                    .setName(Objects.requireNonNull(MythicEnchants.inst().getConfig().getString("MythicEnchantsAddon.EnchantedBook.Display"))
                                            .replace("<enchant>", enchant.getDisplayName())
                                            .replace("<level>", args[3])).toItemStack(), enchant, Number.getInteger(args[3]));
                        }
                        p.getInventory().addItem(enchantBook);

                    }
                }
            }
        }
    }

    @Override
    public List<String> TabComplete(CommandSender sender, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("meaddon.admin")) {
                commands.add("give");
                commands.add("help");
                commands.add("format-lore");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if (sender.hasPermission("meaddon.admin")) {
                if (args[0].equalsIgnoreCase("give")
                        || args[0].equalsIgnoreCase("format-lore")) {
                    Bukkit.getOnlinePlayers().forEach(player -> commands.add(player.getName()));
                }
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        if (args.length == 3) {
            if (sender.hasPermission("meaddon.admin")) {
                if (args[0].equalsIgnoreCase("give")) {
                    MythicEnchants.inst().getEnchantManager().getEnchantments().forEach((s, mythicEnchant) -> commands.add(s));
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
        Collections.sort(completions);
        return completions;
    }
}
