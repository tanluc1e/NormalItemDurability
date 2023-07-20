/* Decompiler 21ms, total 353ms, lines 52 */
package com.github.normalitemdurability;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private final FileConfiguration config;

    public Config(FileConfiguration config) {
        this.config = config;
    }

    public String getDurabilityPrefixColor() {
        return this.config.getString("durability-prefix-color", "§a");
    }

    public String getDurabilityKeyword() {
        return this.config.getString("durability-keyword", "耐久度");
    }

    public int getDurabilityBarLength() {
        return this.config.getInt("durability-bar-length", 20);
    }

    public String getDurabilityBarPrefixColor() {
        return this.config.getString("durability-bar-prefix-color", "§c");
    }

    public String getDurabilityBarSuffixColor() {
        return this.config.getString("durability-bar-suffix-color", "§c");
    }

    public String getUsedSymbol() {
        return this.config.getString("used-symbol", "|");
    }

    public String getUnusedSymbol() {
        return this.config.getString("unused-symbol", "|");
    }

    public String getUsedColor() {
        return this.config.getString("used-color", "§6");
    }

    public String getUnusedColor() {
        return this.config.getString("unused-color", "§7");
    }

    public String getPercentageColor() {
        return this.config.getString("percentage-color", "§b");
    }
}