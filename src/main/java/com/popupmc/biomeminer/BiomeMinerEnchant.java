package com.popupmc.biomeminer;

import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BiomeMinerEnchant extends CustomEnchantmentWrapper {
    public BiomeMinerEnchant(BiomeMiner plugin) {
        super("BiomeMiner", plugin);
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return true;
    }
}
