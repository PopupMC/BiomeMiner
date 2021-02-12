package com.popupmc.biomeminer;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BiomeItem {

    public BiomeItem(BiomeMiner plugin) {
        this.plugin = plugin;
    }

    // Returns a specialized biome block designed to convert to a particular biome
    public ItemStack getItemForBiome(@NotNull Biome biome) {

        // Create head with custom texture
        ItemStack item = SkullCreator.itemFromBase64(plugin.biomeItemTextures.getTexture(biome));

        // Convert something like
        // "SWAMP_HILLS" to "Swamp Hills"
        String biomeReadableName = convertToTitleCaseIteratingChars(biome.name().toLowerCase().replaceAll("_", " "));

        // Get skull meta
        ItemMeta meta = item.getItemMeta();

        // Set Display Name
        meta.setDisplayName(biomeReadableName + displaySuffix);

        // Set Lore
        ArrayList<String> lore = new ArrayList<>();

        // Add 1 line of lore containing the lore prefix and lowercase readable biome name
        lore.add(lorePrefix + biomeReadableName.toLowerCase());

        // Set lore
        meta.setLore(lore);

        // Update Changed Meta
        item.setItemMeta(meta);

        // Return specialzed item
        return item;
    }

    // Verifies the legitmacy of an item to be a shifter item and returns the associated biome if valid
    public Biome verifyItem(ItemStack item) throws InvalidBiomeNameException {

        // Must be a player head
        if(item.getType() != Material.PLAYER_HEAD)
            return null;

        ItemMeta meta = item.getItemMeta();

        // Display name has to end with suffix, stop here if not
        if(!meta.getDisplayName().endsWith(displaySuffix))
            return null;

        List<String> lore = meta.getLore();

        // The lore has to be valid and exactly 1 line, no more, no less
        if(lore == null || lore.size() != 1)
            return null;

        // get that 1 lore line
        String loreLine = lore.get(0);

        // Make sure the lore prefix is valid
        if(!loreLine.startsWith(lorePrefix))
            return null;

        // The lore must also make room for at least 1 character after the prefix
        if(loreLine.length() < lorePrefix.length() + 1)
            return null;

        // This is a legitimate item, extract the biome
        String biomeName = loreLine.substring(lorePrefix.length()).replaceAll(" ", "_").toUpperCase();

        try {
            return Biome.valueOf(biomeName);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("BiomeMiner ERROR: Unknown Material: " + biomeName);
            throw new InvalidBiomeNameException();
        }
    }

    public static String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    // Item Display Name
    public static final String displaySuffix = " Biome";

    // Item Lore Prefix
    public static final String lorePrefix = "Holds the biome ";

    // The plugin
    public final BiomeMiner plugin;
}
