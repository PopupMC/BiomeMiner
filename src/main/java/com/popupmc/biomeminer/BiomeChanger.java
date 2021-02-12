package com.popupmc.biomeminer;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BiomeChanger {
    public BiomeChanger(BiomeMiner plugin) {
        this.plugin = plugin;
    }

    public void mineBiome(Block block) {

        // Get Info
        Location location = block.getLocation();
        World world = block.getWorld();
        Biome curBiome = block.getBiome();

        // If it's the void don't do anything further, the void is not mineable
        if(curBiome == Biome.THE_VOID) {
            return;
        }

        // Get item representing biome
        ItemStack biomeItem = plugin.biomeItem.getItemForBiome(curBiome);

        // Spawn it
        world.dropItemNaturally(location, biomeItem);

        // Clear biome column
        for(int i = 0; i < 256; i++) {
            Location center = new Location(world, location.getX(), i, location.getZ());
            world.getBlockAt(location.getBlockX(), i, location.getBlockZ()).setBiome(Biome.THE_VOID);
            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, center, 5);
        }
    }

    public void placeBiome(Biome biome, Block block) {
        // Because nobody knows how to do this from the players hand with a canceled event
        new BukkitRunnable() {
            @Override
            public void run() {
                // Remove block
                // Because nobody knows how to do this from the players hand with a canceled event
                block.setType(Material.AIR);

                // Change biome
                // It seems this is a bit complicated
                for(int i = 0; i < 256; i++) {
                    Location center = new Location(block.getWorld(), block.getX(), i, block.getZ());
                    block.getWorld().getBlockAt(block.getX(), i, block.getZ()).setBiome(biome);
                    block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, center, 5);
                }
            }
        }.runTaskLater(plugin, 1);
    }

    public final BiomeMiner plugin;
}
