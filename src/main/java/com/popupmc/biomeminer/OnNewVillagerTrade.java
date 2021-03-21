package com.popupmc.biomeminer;

import com.popupmc.customtradeevent.CustomTradeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.List;
import java.util.Random;

public class OnNewVillagerTrade implements Listener {

    public OnNewVillagerTrade(BiomeMiner plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(CustomTradeEvent event) {

        // Must not be at spawn
        if (event.getRelayEvent().getEntity().getWorld().getName().equalsIgnoreCase("imperial_city"))
            return;

        // 10% chance
        if (random.nextInt(100 + 1) > 10)
            return;

        boolean isResultItem = random.nextInt(100 + 1) <= 50;
        boolean isEnchantBook = random.nextInt(100 + 1) <= 50;

        ItemStack item;

        if(isEnchantBook) {
            item = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            meta.addStoredEnchant(plugin.biomeMinerEnchant, 1, true);
            meta.displayName(Component.text("Biome Miner (Enchant)"));
            item.setItemMeta(meta);

            plugin.getLogger().info("Villager Trade: Biome Miner Enchant Book");
        }
        else {
            Biome[] biomes = Biome.values();
            Biome biome = biomes[random.nextInt(biomes.length)];
            item = plugin.biomeItem.getItemForBiome(biome);

            plugin.getLogger().info("Villager Trade: Biome Miner Biome " + biome);
        }

        // Get current recipe
        MerchantRecipe recipe = event.getRelayEvent().getRecipe();

        // Replace result item if needs replacing
        ItemStack result = (isResultItem)
                ? item
                : recipe.getResult();

        // Ensure amount matches
        result.setAmount(recipe.getResult().getAmount());

        // Create new recipe thats a copy of the old one
        MerchantRecipe newRecipe = new MerchantRecipe(result,
                recipe.getUses(),
                recipe.getMaxUses(),
                recipe.hasExperienceReward(),
                recipe.getVillagerExperience(),
                recipe.getPriceMultiplier(),
                false);

        // get ingridients
        List<ItemStack> ings = recipe.getIngredients();

        // Stop if none, this is an error
        if(ings.size() == 0)
            return;

        // Set item as ing1 if not result
        if(!isResultItem) {
            item.setAmount(ings.get(0).getAmount());
            ings.set(0, item);
        }

        // Update new recipe with modified ings
        newRecipe.setIngredients(ings);

        // Set recipe
        event.getRelayEvent().setRecipe(newRecipe);
    }

    public Random random = new Random();
    public BiomeMiner plugin;
}
