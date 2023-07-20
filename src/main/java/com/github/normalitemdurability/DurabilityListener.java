/* Decompiler 20ms, total 568ms, lines 71 */
package com.github.normalitemdurability;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class DurabilityListener implements Listener {
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        if (attacker instanceof Player) {
            Player player = (Player) attacker;
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (weapon != null && weapon.getType() != Material.AIR && NormalDurabilityItem.isDurabilityItem(weapon)) {
                this.takeDurability(player, weapon, 1, player.getInventory().getHeldItemSlot());
            }
        }
    }


    @EventHandler
    public void onDefense(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)event.getEntity();
            ItemStack offhand = player.getInventory().getItemInOffHand();
            ItemStack helmet = player.getInventory().getHelmet();
            ItemStack chestplate = player.getInventory().getChestplate();
            ItemStack leggins = player.getInventory().getLeggings();
            ItemStack boots = player.getInventory().getBoots();
            if (NormalDurabilityItem.isDurabilityItem(helmet)) {
                this.takeDurability(player, helmet, 1, 39);
            }

            if (NormalDurabilityItem.isDurabilityItem(chestplate)) {
                this.takeDurability(player, chestplate, 1, 38);
            }

            if (NormalDurabilityItem.isDurabilityItem(leggins)) {
                this.takeDurability(player, leggins, 1, 37);
            }

            if (NormalDurabilityItem.isDurabilityItem(boots)) {
                this.takeDurability(player, boots, 1, 36);
            }

            if (NormalDurabilityItem.isDurabilityItem(offhand)) {
                this.takeDurability(player, offhand, 1, 40);
            }

        }
    }

    private void takeDurability(Player player, ItemStack item, int amount, int slot) {
        if (item != null) {
            NormalDurabilityItem durabilityItem = new NormalDurabilityItem(item, NormalDurabilityItem.getCurrentDurabilityFromItem(item), NormalDurabilityItem.getMaxDurabilityFromItem(item), player);
            if (NormalDurabilityItem.getCurrentDurabilityFromItem(item) > 0) {
                durabilityItem.setDurability(NormalDurabilityItem.getCurrentDurabilityFromItem(item) - amount, slot);
            } else {
                durabilityItem.getItem().setAmount(0);
            }

        }
    }
}