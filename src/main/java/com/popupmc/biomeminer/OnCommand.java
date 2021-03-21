package com.popupmc.biomeminer;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;

public class OnCommand implements CommandExecutor {

    public OnCommand(BiomeMiner plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player to access biome miner");
            return true;
        }

        Player player = (Player)sender;
        if(!player.hasPermission("biomeminer.use")) {
            sender.sendMessage("You don't have permission to use this command");
            return true;
        }

        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
        meta.addStoredEnchant(plugin.biomeMinerEnchant, 1, true);
        book.setItemMeta(meta);

        player.getWorld().dropItemNaturally(player.getLocation(), book);

        return true;
    }

    public BiomeMiner plugin;
}
