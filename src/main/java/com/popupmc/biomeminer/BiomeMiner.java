package com.popupmc.biomeminer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomeMiner extends JavaPlugin {
    @Override
    public void onEnable() {

        // Setup textures for every biome and fallback texture and make sure they're all unique
        // This is SUPER SLOW, don't ever call this again
        biomeItemTextures = new BiomeItemTextures(this);

        // Create biome item, this is the item that represents a biome
        biomeItem = new BiomeItem(this);

        // Create miner Enchant, this is the custom enchantment for mining biomes
        biomeMinerEnchant = new BiomeMinerEnchant(this);

        // Create Biome Changer
        biomeChanger = new BiomeChanger(this);

        // Register events
        Bukkit.getPluginManager().registerEvents(new OnBlockBreakEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new OnBlockPlaceEvent(this), this);

        getLogger().info("BiomeMiner is enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BiomeMiner is disabled.");
    }

    BiomeItem biomeItem;
    BiomeMinerEnchant biomeMinerEnchant;
    BiomeItemTextures biomeItemTextures;
    BiomeChanger biomeChanger;
}
