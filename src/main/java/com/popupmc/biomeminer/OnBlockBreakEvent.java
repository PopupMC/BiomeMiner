package com.popupmc.biomeminer;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Damageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class OnBlockBreakEvent implements Listener {
    public OnBlockBreakEvent(BiomeMiner plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(BlockBreakEvent e) {

        // Do nothing if the event is canceled
        if(e.isCancelled())
            return;

        // Get item in hand
        ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();

        // Check to see if it has the enchant we're looking for
        if(!itemInHand.containsEnchantment(plugin.biomeMinerEnchant))
            return;

        // Mine biome
        plugin.biomeChanger.mineBiome(e.getBlock());
    }

    BiomeMiner plugin;
}
