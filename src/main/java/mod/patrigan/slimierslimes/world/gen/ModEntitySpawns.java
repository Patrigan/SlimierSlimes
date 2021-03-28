package mod.patrigan.slimierslimes.world.gen;


import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.entities.*;
import mod.patrigan.slimierslimes.init.data.SlimeSpawnData;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;
import static mod.patrigan.slimierslimes.init.data.SlimeDatas.SLIME_DATA;
import static mod.patrigan.slimierslimes.init.ModEntityTypes.*;
import static net.minecraft.entity.EntityClassification.MONSTER;
import static net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType.ON_GROUND;
import static net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING;
import static net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEntitySpawns {

    public static void init(){
        EntitySpawnPlacementRegistry.register(COMMON_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
        EntitySpawnPlacementRegistry.register(PINK_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
        EntitySpawnPlacementRegistry.register(CLOUD_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
        EntitySpawnPlacementRegistry.register(ROCK_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
        EntitySpawnPlacementRegistry.register(CRYSTAL_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, CrystalSlimeEntity::checkSlimeSpawnRules);
        EntitySpawnPlacementRegistry.register(GLOW_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, GlowSlimeEntity::checkSlimeSpawnRules);
        EntitySpawnPlacementRegistry.register(CREEPER_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, CreeperSlimeEntity::checkSlimeSpawnRules);
        EntitySpawnPlacementRegistry.register(SNOW_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, SnowSlimeEntity::checkSlimeSpawnRules);
        EntitySpawnPlacementRegistry.register(CAMO_SLIME.get(), ON_GROUND, MOTION_BLOCKING, CamoSlimeEntity::checkSlimeSpawnRules);
        EntitySpawnPlacementRegistry.register(DIAMOND_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, DiamondSlimeEntity::checkSlimeSpawnRules);
        EntitySpawnPlacementRegistry.register(LAVA_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, LavaSlimeEntity::spawnable);
        EntitySpawnPlacementRegistry.register(OBSIDIAN_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
    }


    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event)
    {
        int slimeConfigTotalWeight = SlimierSlimes.MAIN_CONFIG.totalSlimeSpawnWeight.get();

        RegistryKey<Biome> key = RegistryKey.create(Registry.BIOME_REGISTRY, event.getName());
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        List<MobSpawnInfo.Spawners> slimeWeights = SLIMES.stream()
                .map(entityType -> getSpawner(event, entityType))
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());
        if(types.contains(BiomeDictionary.Type.SWAMP)){
            slimeConfigTotalWeight = slimeConfigTotalWeight * 3;
        }
        Integer currentTotal = event.getSpawns().getSpawner(MONSTER).stream().map(spawners -> spawners.weight).reduce(0, Integer::sum);
        if(TRUE.equals(SlimierSlimes.MAIN_CONFIG.useTotalSlimeSpawnWeight.get()) && slimeConfigTotalWeight < currentTotal) {
            slimeWeights = boundWeights(slimeWeights, slimeConfigTotalWeight);
        }
        addSlimeSpawners(event, slimeWeights);
    }

    private static Optional<MobSpawnInfo.Spawners> getSpawner(BiomeLoadingEvent event, EntityType<?> entityType) {
        if(SLIME_DATA.hasData()) {
            List<SlimeSpawnData> slimeSpawnDatas = SLIME_DATA.getData(entityType.getRegistryName()).getSlimeSpawnData();
            return slimeSpawnDatas.stream()
                    .filter(slimeSpawnData -> slimeSpawnData.isMatch(event)).findFirst()
                    .map(slimeSpawnData -> slimeSpawnData.getSpawner(entityType));
        }else{
            return Optional.empty();
        }

    }

    private static List<MobSpawnInfo.Spawners> boundWeights(List<MobSpawnInfo.Spawners> slimeWeights, final int totalWeight) {
        final double totalOriginal = slimeWeights.stream().map(spawner -> spawner.weight).reduce(0, Integer::sum);
        return slimeWeights.stream().map(spawner -> new MobSpawnInfo.Spawners(spawner.type, (int) Math.ceil((spawner.weight/totalOriginal)*totalWeight), spawner.minCount, spawner.maxCount)).collect(Collectors.toList());
    }

    private static void addSlimeSpawners(BiomeLoadingEvent event, List<MobSpawnInfo.Spawners> slimeWeights) {
        slimeWeights.forEach(slimeSpawner -> event.getSpawns().addSpawn(MONSTER, slimeSpawner));
    }
}