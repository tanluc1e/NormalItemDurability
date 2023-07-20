/* Decompiler 41ms, total 938ms, lines 54 */
package com.github.normalitemdurability;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class NormalItemDurability extends JavaPlugin implements Listener {
    public static Config config;

    public void onEnable() {
        Updater.init();
        this.saveDefaultConfig();
        this.reloadConfig();
        config = new Config(this.getConfig());
        Bukkit.getPluginManager().registerEvents(new DurabilityListener(), this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Chỉ người chơi mới có thể sử dụng lệnh này！");
            return true;
        } else {
            Player player = (Player)sender;
            if (args.length == 0) {
                return false;
            } else if (args[0].equals("create")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.AIR) {
                    player.sendMessage(ChatColor.RED + "Bạn cần giữ vật phẩm để sử dụng lệnh này！");
                    return true;
                } else {
                    int maxDurability = Integer.parseInt(args[2]);
                    int durability = Integer.parseInt(args[1]);
                    NormalDurabilityItem.addDurabilityToItem(item, durability, maxDurability);
                    player.sendMessage(ChatColor.GREEN + "Đã thêm thành công độ bền cho vật phẩm！");
                    return true;
                }
            } else {
                if (args[0].equals("reload-plugin")) {
                    this.reloadConfig();
                }

                return false;
            }
        }
    }
}