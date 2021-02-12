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

        // Make sure biome is void first
        if(block.getBiome() != Biome.THE_VOID) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GOLD + "You can't place a biome on top of another biome.");
            return;
        }

        // Nether blocks can only be placed in the nether
        if(biome == Biome.NETHER && !block.getWorld().getName().endsWith("_nether")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GOLD + "Nether biomes are too powerful outside the nether.");
            return;
        }

        plugin.biomeChanger.placeBiome(biome, block);
    }

    BiomeMiner plugin;
}
