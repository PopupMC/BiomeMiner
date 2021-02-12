package com.popupmc.biomeminer;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public abstract class CustomEnchantmentWrapper extends Enchantment {
    public CustomEnchantmentWrapper(String namespace, BiomeMiner plugin) {
        super(new NamespacedKey(plugin, namespace));
        this.namespace = namespace;

        // Auto register
        doRegister();
    }

    // Thanks https://www.spigotmc.org/threads/custom-enchantments-1-13.346538/
    public void doRegister() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    // If treasure then it cant be enchanted with an enchanting table
    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack arg0) {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment arg0) {
        return false;
    }

    @Override
    public @NotNull String getName() {
        return namespace;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    public final String namespace;
}
