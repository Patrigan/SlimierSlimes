package mod.patrigan.slimierslimes.world.gen;


import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.entities.*;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.*;
import static net.minecraft.entity.EntityClassification.MONSTER;
import static net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType.ON_GROUND;
import static net.minecraft.entity.EntityType.SLIME;
import static net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING;
import static net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEntitySpawns {

    private static final List<MobSpawnInfo.Spawners> SLIME_BASE_SPAWNERS = new ArrayList<>();
    private static final int SLIME_TOTAL_WEIGHT = 400;

    public static void initBaseWeights(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(COMMON_SLIME.get(), 100, 1, 1));
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(PINK_SLIME.get(), 10, 1, 1));
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(ROCK_SLIME.get(), 75, 1, 1));
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(CRYSTAL_SLIME.get(), 20, 1, 1));
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(GLOW_SLIME.get(), 30, 1, 1));
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(CREEPER_SLIME.get(), 75, 1, 1));
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(CAMO_SLIME.get(), 50, 1, 1));
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(SNOW_SLIME.get(), 80, 1, 1));
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(LAVA_SLIME.get(), 0, 1, 1));
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(OBSIDIAN_SLIME.get(), 0, 1, 1));
            SLIME_BASE_SPAWNERS.add(new MobSpawnInfo.Spawners(DIAMOND_SLIME.get(), 1, 1, 1));
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
        EntitySpawnPlacementRegistry.register(LAVA_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, LavaSlimeEntity::spawnable);
    }


    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event)
    {
        RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
        removeSlime(event);

        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        List<MobSpawnInfo.Spawners> slimeWeights = null;

        if(types.contains(BiomeDictionary.Type.OVERWORLD)) {
            slimeWeights = SLIME_BASE_SPAWNERS.stream().map(spawner -> new MobSpawnInfo.Spawners(spawner.type, spawner.itemWeight, spawner.minCount, spawner.maxCount)).collect(Collectors.toList());
        }else if(types.contains(BiomeDictionary.Type.END)){
            slimeWeights = SLIME_BASE_SPAWNERS.stream().filter(spawner -> spawner.type.equals(OBSIDIAN_SLIME.get())).map(spawner -> new MobSpawnInfo.Spawners(spawner.type, 1, spawner.minCount, spawner.maxCount)).collect(Collectors.toList());
            addSlimeSpawners(event, slimeWeights);
        }else {
            slimeWeights = new ArrayList<>();
        }
        if(types.contains(BiomeDictionary.Type.SWAMP)){
            slimeWeights = boundWeights(slimeWeights, SLIME_TOTAL_WEIGHT*3);
            addSlimeSpawners(event, slimeWeights);
            return;
        }else{
            slimeWeights = slimeWeights.stream().filter(spawner -> !spawner.type.equals(LAVA_SLIME.get())).collect(Collectors.toList());
        }
        if (!types.contains(BiomeDictionary.Type.COLD)
                && !types.contains(BiomeDictionary.Type.OCEAN)
                && types.contains(BiomeDictionary.Type.OVERWORLD)) {
            slimeWeights = slimeWeights.stream().filter(spawner -> !spawner.type.equals(SNOW_SLIME.get())).collect(Collectors.toList());
        }else if(types.contains(BiomeDictionary.Type.COLD)
                && !types.contains(BiomeDictionary.Type.OCEAN)
                && types.contains(BiomeDictionary.Type.OVERWORLD)) {
            slimeWeights = slimeWeights.stream().map(ModEntitySpawns::biomeOverworldColdNotOcean).collect(Collectors.toList());
        }
        if(!types.contains(BiomeDictionary.Type.FOREST) && !types.contains(BiomeDictionary.Type.JUNGLE)){
            slimeWeights = slimeWeights.stream().filter(spawner -> !spawner.type.equals(CAMO_SLIME.get())).collect(Collectors.toList());
        }
        slimeWeights = boundWeights(slimeWeights, SLIME_TOTAL_WEIGHT);
        addSlimeSpawners(event, slimeWeights);
    }

    private static void removeSlime(BiomeLoadingEvent event) {
        List<MobSpawnInfo.Spawners> spawners = new ArrayList<>(event.getSpawns().getSpawner(SLIME.getClassification()));
        event.getSpawns().getSpawner(SLIME.getClassification()).clear();
        event.getSpawns().getSpawner(SLIME.getClassification()).addAll(spawners.stream().filter(spawner -> !spawner.type.equals(SLIME)).collect(Collectors.toList()));
    }

    private static void addSlimeSpawners(BiomeLoadingEvent event, List<MobSpawnInfo.Spawners> slimeWeights) {
        slimeWeights.forEach(slimeSpawner -> event.getSpawns().withSpawner(MONSTER, slimeSpawner));
    }

    /**
     *
     * @param slimeWeights
     * @param totalWeight
     * @return Map of bounded weights
     *
     */
    private static List<MobSpawnInfo.Spawners> boundWeights(List<MobSpawnInfo.Spawners> slimeWeights, final int totalWeight) {
        final double totalOriginal = slimeWeights.stream().map(spawner -> spawner.itemWeight).reduce(0, Integer::sum);
        return slimeWeights.stream().map(spawner -> new MobSpawnInfo.Spawners(spawner.type, (int) Math.ceil((spawner.itemWeight/totalOriginal)*totalWeight), spawner.minCount, spawner.maxCount)).collect(Collectors.toList());
    }

    //Biome Specific Mappings
    private static MobSpawnInfo.Spawners biomeOverworldColdNotOcean(MobSpawnInfo.Spawners spawner){
        if(spawner.type.equals(COMMON_SLIME.get())){
            return new MobSpawnInfo.Spawners(spawner.type, spawner.itemWeight/2, spawner.minCount, spawner.maxCount);
        }else if(spawner.type.equals(SNOW_SLIME.get())){
            return new MobSpawnInfo.Spawners(spawner.type, spawner.itemWeight*3, spawner.minCount, spawner.maxCount);
        }else{
            return new MobSpawnInfo.Spawners(spawner.type, spawner.itemWeight, spawner.minCount, spawner.maxCount);
        }
    }
}