package com.popupmc.biomeminer;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

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
    public @NotNull Component displayName(int i) {

        String displayName = convertToTitleCaseSplitting(namespace.toLowerCase().replaceAll("_", " "));

        if(i > getMaxLevel() || i < getStartLevel())
            return Component.text(displayName);

        return Component.text(displayName + " " + i);
    }

    @Override
    public int getStartLevel() {
        return 1;
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
        return 1;
    }

    // Convert words with spaces to words with the first letter in each word capitalized
    public static String convertToTitleCaseSplitting(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        return Arrays
                .stream(text.split(" "))
                .map(word -> word.isEmpty()
                        ? word
                        : Character.toTitleCase(word.charAt(0)) + word
                        .substring(1)
                        .toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public final String namespace;
}
