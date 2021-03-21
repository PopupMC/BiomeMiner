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
        Biome curBiome = getBiome(location);

        // If it's the void don't do anything further, the void is not mineable
        if(curBiome == Biome.THE_VOID) {
            return;
        }

        // Get item representing biome
        ItemStack biomeItem = plugin.biomeItem.getItemForBiome(curBiome);

        // Spawn it
        world.dropItemNaturally(location, biomeItem);

        // Clear Biome
        setBiome(location, Biome.THE_VOID);

        world.playEffect(location.toCenterLocation(), Effect.EXTINGUISH, 0, 5);

        // Add particle effects
        for(int i = 0; i <= 1; i++) {
            Location center = new Location(world, location.getX(), location.getY() + i, location.getZ());
            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, center, 5);
        }
    }

    public void placeBiome(Biome biome, Block block) {
        // Because nobody knows how to do this from the players hand with a canceled event
        new BukkitRunnable() {
            @Override
            public void run() {

                // Apply new biome
                setBiome(block.getLocation(), biome);

                // Remove block
                // Because nobody knows how to do this from the players hand with a canceled event
                block.setType(Material.AIR);

                // Sound Effect
                block.getWorld().playEffect(block.getLocation().toCenterLocation(), Effect.EXTINGUISH, 0, 5);

                // Particle Effect
                for(int i = 0; i  <= 1; i++) {
                    Location center = new Location(block.getWorld(), block.getX(), block.getY() + i, block.getZ());
                    block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, center, 5);
                }
            }
        }.runTaskLater(plugin, 1);
    }

    public Biome getBiome(Location location) {
        World world = location.getWorld();
        if(world.getEnvironment() == World.Environment.NETHER)
            return world.getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        // This should not be deprecated as 3D biomes dont apply to all worlds
        return world.getBiome(location.getBlockX(), location.getBlockZ());
    }

    public void setBiome(Location location, Biome biome) {
        World world = location.getWorld();
        if(world.getEnvironment() == World.Environment.NETHER)
            world.setBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ(), biome);

        // This should not be deprecated as 3D biomes dont apply to all worlds
        world.setBiome(location.getBlockX(), location.getBlockZ(), biome);
    }

    public final BiomeMiner plugin;
}
