package mod.patrigan.slimierslimes.world.gen;


import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.entities.*;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.*;
import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.*;
import static net.minecraft.entity.EntityClassification.MONSTER;
import static net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType.ON_GROUND;
import static net.minecraft.entity.EntityType.SLIME;
import static net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING;
import static net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEntitySpawns {

    private static final Map<EntityType<? extends AbstractSlimeEntity>, Integer> SLIME_BASE_WEIGHTS = new HashMap<>();
    private static final int SLIME_TOTAL_WEIGHT = 400;

    public static void initBaseWeights(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            SLIME_BASE_WEIGHTS.put(COMMON_SLIME.get(), 100);
            SLIME_BASE_WEIGHTS.put(PINK_SLIME.get(), 10);
            SLIME_BASE_WEIGHTS.put(ROCK_SLIME.get(), 75);
            SLIME_BASE_WEIGHTS.put(CRYSTAL_SLIME.get(), 20);
            SLIME_BASE_WEIGHTS.put(GLOW_SLIME.get(), 30);
            SLIME_BASE_WEIGHTS.put(CREEPER_SLIME.get(), 75);
            SLIME_BASE_WEIGHTS.put(CAMO_SLIME.get(), 50);
            SLIME_BASE_WEIGHTS.put(SNOW_SLIME.get(), 80);
            SLIME_BASE_WEIGHTS.put(DIAMOND_SLIME.get(), 1);
        });
    }

    public static void init(){
        EntitySpawnPlacementRegistry.register(COMMON_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, CommonSlimeEntity::spawnable);
        EntitySpawnPlacementRegistry.register(PINK_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PinkSlimeEntity::spawnable);
        EntitySpawnPlacementRegistry.register(ROCK_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, RockSlimeEntity::spawnable);
        EntitySpawnPlacementRegistry.register(CRYSTAL_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, CrystalSlimeEntity::spawnable);
        EntitySpawnPlacementRegistry.register(GLOW_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, GlowSlimeEntity::spawnable);
        EntitySpawnPlacementRegistry.register(CREEPER_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, CreeperSlimeEntity::spawnable);
        EntitySpawnPlacementRegistry.register(SNOW_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, SnowSlimeEntity::spawnable);
        EntitySpawnPlacementRegistry.register(CAMO_SLIME.get(), ON_GROUND, MOTION_BLOCKING, CamoSlimeEntity::spawnable);
        EntitySpawnPlacementRegistry.register(DIAMOND_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, DiamondSlimeEntity::spawnable);
    }


    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event)
    {
        RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
        removeSlime(event);

        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        Map<EntityType<? extends AbstractSlimeEntity>, Integer> slimeWeights = null;

        if(types.contains(BiomeDictionary.Type.OVERWORLD)) {
            slimeWeights = new HashMap<>(SLIME_BASE_WEIGHTS);
        }else{
            slimeWeights = new HashMap<>();
        }
        if(types.contains(BiomeDictionary.Type.SWAMP)){
            slimeWeights = boundWeights(slimeWeights, SLIME_TOTAL_WEIGHT*3);
            addSlimeSpawners(event, slimeWeights);
            return;
        }
        if (!types.contains(BiomeDictionary.Type.COLD)
                && !types.contains(BiomeDictionary.Type.OCEAN)
                && types.contains(BiomeDictionary.Type.OVERWORLD)) {
            slimeWeights.remove(SNOW_SLIME.get());
        }else if(types.contains(BiomeDictionary.Type.COLD)
                && !types.contains(BiomeDictionary.Type.OCEAN)
                && types.contains(BiomeDictionary.Type.OVERWORLD)) {
            slimeWeights.put(COMMON_SLIME.get(), SLIME_BASE_WEIGHTS.get(COMMON_SLIME.get())/2);
            slimeWeights.put(SNOW_SLIME.get(), SLIME_BASE_WEIGHTS.get(SNOW_SLIME.get())*2);
        }
        if(!types.contains(BiomeDictionary.Type.FOREST) && !types.contains(BiomeDictionary.Type.JUNGLE)){
            slimeWeights.remove(CAMO_SLIME.get());
        }
        slimeWeights = boundWeights(slimeWeights, SLIME_TOTAL_WEIGHT);
        addSlimeSpawners(event, slimeWeights);
    }

    private static void removeSlime(BiomeLoadingEvent event) {
        List<MobSpawnInfo.Spawners> spawners = new ArrayList<>(event.getSpawns().getSpawner(SLIME.getClassification()));
        event.getSpawns().getSpawner(SLIME.getClassification()).clear();
        event.getSpawns().getSpawner(SLIME.getClassification()).addAll(spawners.stream().filter(spawner -> !spawner.type.equals(SLIME)).collect(Collectors.toList()));
    }

    private static void addSlimeSpawners(BiomeLoadingEvent event, Map<EntityType<? extends AbstractSlimeEntity>, Integer> slimeWeights) {
        slimeWeights.forEach((slimeEntity, weight) -> event.getSpawns().withSpawner(MONSTER, new MobSpawnInfo.Spawners(slimeEntity, weight, 1, 1)));
    }

    /**
     *
     * @param slimeWeights
     * @param totalWeight
     * @return Map of bounded weights
     *
     */
    private static Map<EntityType<? extends AbstractSlimeEntity>, Integer> boundWeights(Map<EntityType<? extends AbstractSlimeEntity>, Integer> slimeWeights, final int totalWeight) {
        final double totalOriginal = slimeWeights.values().stream().reduce(0, (x, y) -> x+y);
        return slimeWeights.entrySet().stream()
                .collect(Collectors.<Map.Entry<EntityType<? extends AbstractSlimeEntity>, Integer>, EntityType<? extends AbstractSlimeEntity>, Integer>toMap(Map.Entry::getKey, e -> (int) Math.ceil((e.getValue()/totalOriginal)*totalWeight)));
    }
}