package com.popupmc.biomeminer;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class OnBlockPlaceEvent implements Listener {
    public OnBlockPlaceEvent(BiomeMiner plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(BlockPlaceEvent e) {

        // Do nothing if the event is canceled
        if(e.isCancelled())
            return;

        Block block = e.getBlock();

        // Get block to be placed
        ItemStack itemInHand = e.getItemInHand();

        World world = block.getWorld();
        Location location = block.getLocation();

        // Grab biome from block
        Biome biome;
        try {
            biome = plugin.biomeItem.verifyItem(itemInHand);
            if(biome == null)
                return;
        } catch (InvalidBiomeNameException ignored) {
            return;
        }

        // Nether blocks can only be placed in the nether
        if(isNetherBiome(biome) && !(block.getWorld().getEnvironment() == World.Environment.NETHER)) {
            e.setCancelled(true);
            world.spawnParticle(Particle.SMOKE_LARGE, location.toCenterLocation(), 10);
            world.playEffect(location, Effect.EXTINGUISH, 0, 5);
            return;
        }

        // Make sure biome is void first, otherwise drop biome before replacing it
        if(plugin.biomeChanger.getBiome(location) != Biome.THE_VOID) {
            plugin.biomeChanger.mineBiome(e.getBlock());
        }

        plugin.biomeChanger.placeBiome(biome, block);
    }

    public boolean isNetherBiome(Biome biome) {
        return biome == Biome.NETHER_WASTES ||
                biome == Biome.SOUL_SAND_VALLEY ||
                biome == Biome.CRIMSON_FOREST ||
                biome == Biome.WARPED_FOREST ||
                biome == Biome.BASALT_DELTAS;
    }

    BiomeMiner plugin;
}
