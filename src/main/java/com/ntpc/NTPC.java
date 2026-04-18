package com.ntpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class NTPC implements ModInitializer {
    public static final String MOD_ID = "ntpc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static final TagKey<Item> KNIVES_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "tools/knives"));
    private static ModConfig CONFIG;

    @Override
    public void onInitialize() {
        loadConfig();

        if (!FabricLoader.getInstance().isModLoaded("notreepunching")) {
            LOGGER.info("NTPC: No Tree Punching not detected. Compatibility disabled.");
            return;
        }

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.getPath().startsWith("blocks/") && !id.getNamespace().equals("minecraft")) {
                
                String path = id.getPath().replace("blocks/", "").toLowerCase();
                String fullId = id.getNamespace() + ":" + path;

                boolean matchesGeneral = isGrassy(path);
                boolean matchesManual = CONFIG.manual_overrides.contains(fullId);
                boolean blacklisted = CONFIG.blacklist.contains(fullId);

                if ((matchesGeneral || matchesManual) && !blacklisted) {
                    Item plantFiber = Registries.ITEM.get(new Identifier("notreepunching", "plant_fiber"));

                    tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(KNIVES_TAG)))
                        .conditionally(RandomChanceLootCondition.builder(CONFIG.drop_chance))
                        .with(ItemEntry.builder(plantFiber))
                    );
                }
            }
        });
    }

    private boolean isGrassy(String path) {
        if (path.contains("dead_bush") || path.endsWith("_block") || path.contains("dirt") || path.contains("leaves")) {
            return false;
        }
        return path.contains("grass") || path.contains("fern") || path.contains("plant") || 
               path.contains("shrub") || path.contains("weed") || path.contains("clover") ||
               path.contains("flower") || path.contains("petal") || path.contains("reeds");
    }

    private void loadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "ntpc.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                CONFIG = gson.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                CONFIG = new ModConfig();
            }
        } else {
            CONFIG = new ModConfig();
            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(CONFIG, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ModConfig {
        String _comment = "Add modded plant IDs here (e.g., 'modid:plant_name') to force them to drop fibers."; 
        
        float drop_chance = 0.15F;
        List<String> manual_overrides = new ArrayList<>();
        List<String> blacklist = new ArrayList<>();
        
        public ModConfig() {
            manual_overrides.add("biomesoplenty:cattail");
        }
    }
}