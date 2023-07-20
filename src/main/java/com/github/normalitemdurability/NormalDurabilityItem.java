/* Decompiler 63ms, total 528ms, lines 212 */
package com.github.normalitemdurability;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NormalDurabilityItem {
    private ItemStack item;
    private int durability;
    private int maxDurability;
    private Player player;

    public NormalDurabilityItem(ItemStack item, int durability, int maxDurability, Player player) {
        this.item = item;
        this.durability = durability;
        this.maxDurability = maxDurability;
        this.player = player;
        ItemMeta meta = item.getItemMeta();
        if (item.getItemMeta().isUnbreakable()) {
            meta.setUnbreakable(true);
        }

        item.setItemMeta(meta);
    }

    public static boolean isDurabilityItem(ItemStack item) {
        if (item == null) {
            return false; // Item is null, not a durability item
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false; // Item doesn't have metadata, not a durability item
        }

        List<String> lore = meta.getLore();
        if (lore == null) {
            return false; // Item's lore is null, not a durability item
        }

        for (String loreLine : lore) {
            if (loreLine.contains(NormalItemDurability.config.getDurabilityKeyword())) {
                return true; // Found the durability keyword in the lore, it's a durability item
            }
        }

        return false; // Did not find the durability keyword in the lore, not a durability item
    }


    public static void addDurabilityToItem(ItemStack item, int durability, int maxDurability) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList();
        }

        String durabilityLine = NormalItemDurability.config.getDurabilityPrefixColor() + NormalItemDurability.config.getDurabilityKeyword() + ": ";
        String durabilityBar = "";
        int usedCount = (int)Math.ceil((double)durability / (double)maxDurability * (double)NormalItemDurability.config.getDurabilityBarLength());
        int unusedCount = NormalItemDurability.config.getDurabilityBarLength() - usedCount;

        int i;
        for(i = 0; i < usedCount; ++i) {
            durabilityBar = durabilityBar + NormalItemDurability.config.getUsedColor() + NormalItemDurability.config.getUsedSymbol();
        }

        for(i = 0; i < unusedCount; ++i) {
            durabilityBar = durabilityBar + NormalItemDurability.config.getUnusedColor() + NormalItemDurability.config.getUnusedSymbol();
        }

        String percentage = NormalItemDurability.config.getPercentageColor() + "(" + (int)Math.ceil((double)durability / (double)maxDurability * 100.0D) + "%)";
        String durabilityText = durabilityLine + durability + "/" + maxDurability + " " + NormalItemDurability.config.getDurabilityBarPrefixColor() + "[" + durabilityBar + NormalItemDurability.config.getDurabilityBarSuffixColor() + "]" + NormalItemDurability.config.getDurabilityBarSuffixColor() + " " + percentage;
        ((List)lore).add(durabilityText);
        meta.setLore((List)lore);
        item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getDurability() {
        return this.durability;
    }

    public void setDurability(int durability, int slot) {
        if (durability > this.maxDurability) {
            durability = this.maxDurability;
        }

        this.durability = durability;
        this.updateLore();
        this.updateItem(slot);
    }

    public void addDurability(int amount, int slot) {
        if (amount + this.durability > this.maxDurability) {
            this.durability = this.maxDurability;
        } else {
            this.durability += amount;
        }

        this.updateLore();
        this.updateItem(slot);
    }

    public boolean takeDurability(int amount, int slot) {
        if (this.durability - amount >= 0) {
            if (amount > this.durability) {
                this.durability = 0;
            } else {
                this.durability -= amount;
            }

            this.updateLore();
            this.updateItem(slot);
            return true;
        } else {
            return false;
        }
    }

    private void updateLore() {
        ItemMeta meta = this.item.getItemMeta();
        meta.setUnbreakable(true);
        List<String> lore = new ArrayList(meta.getLore());
        lore.replaceAll((line) -> {
            if (!line.startsWith(NormalItemDurability.config.getDurabilityPrefixColor() + NormalItemDurability.config.getDurabilityKeyword())) {
                return line;
            } else {
                int currentDurability = this.durability;
                int usedCount = (int)Math.ceil((double)currentDurability / (double)this.maxDurability * (double)NormalItemDurability.config.getDurabilityBarLength());
                int unusedCount = NormalItemDurability.config.getDurabilityBarLength() - usedCount;
                String durabilityBar = NormalItemDurability.config.getDurabilityBarPrefixColor() + "[";

                int i;
                for(i = 0; i < usedCount; ++i) {
                    durabilityBar = durabilityBar + NormalItemDurability.config.getUsedColor() + NormalItemDurability.config.getUsedSymbol();
                }

                for(i = 0; i < unusedCount; ++i) {
                    durabilityBar = durabilityBar + NormalItemDurability.config.getUnusedColor() + NormalItemDurability.config.getUnusedSymbol();
                }

                durabilityBar = durabilityBar + NormalItemDurability.config.getDurabilityBarSuffixColor() + "]";
                String percentage = NormalItemDurability.config.getPercentageColor() + "(" + (int)Math.ceil((double)this.durability / (double)this.maxDurability * 100.0D) + "%)";
                return NormalItemDurability.config.getDurabilityPrefixColor() + NormalItemDurability.config.getDurabilityKeyword() + ": " + this.durability + "/" + this.maxDurability + " " + durabilityBar + " " + percentage;
            }
        });
        meta.setLore(lore);
        this.item.setItemMeta(meta);
    }

    private void updateItem(int slot) {
        this.player.getInventory().setItem(slot, this.item);
    }

    public int getMaxDurability() {
        return this.maxDurability;
    }

    public void setMaxDurability(int maxDurability, int slot) {
        if (maxDurability <= 0) {
            maxDurability = 1;
        }

        this.maxDurability = maxDurability;
        this.updateLore();
        this.updateItem(slot);
    }

    public static int getCurrentDurabilityFromItem(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            Iterator var2 = lore.iterator();

            while(var2.hasNext()) {
                String line = (String)var2.next();
                if (line.contains(NormalItemDurability.config.getDurabilityKeyword())) {
                    Pattern pattern = Pattern.compile(":[ ]*(\\d+)");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        return Integer.parseInt(matcher.group(1));
                    }
                }
            }
        }

        return 0;
    }

    public static int getMaxDurabilityFromItem(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            Iterator var2 = lore.iterator();

            while(var2.hasNext()) {
                String line = (String)var2.next();
                if (line.contains(NormalItemDurability.config.getDurabilityKeyword())) {
                    Pattern pattern = Pattern.compile("/(\\d+)");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        return Integer.parseInt(matcher.group(1));
                    }
                }
            }
        }

        return 0;
    }
}