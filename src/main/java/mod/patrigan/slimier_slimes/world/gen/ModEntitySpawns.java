package mod.patrigan.slimier_slimes.world.gen;


import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.entities.*;
import mod.patrigan.slimier_slimes.init.data.SlimeSpawnData;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;
import static mod.patrigan.slimier_slimes.init.ModEntityTypes.*;
import static mod.patrigan.slimier_slimes.init.data.SlimeDatas.SLIME_DATA;
import static net.minecraft.world.entity.MobCategory.MONSTER;
import static net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND;
import static net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING;
import static net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEntitySpawns {

    public static void init(){
        SpawnPlacements.register(COMMON_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(PINK_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(CLOUD_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(ROCK_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(CRYSTAL_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, CrystalSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(GLOW_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, GlowSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(CREEPER_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, CreeperSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(SNOW_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, SnowSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(CAMO_SLIME.get(), ON_GROUND, MOTION_BLOCKING, CamoSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(DIAMOND_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, DiamondSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(LAVA_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, LavaSlimeEntity::spawnable);
        SpawnPlacements.register(OBSIDIAN_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(BROWN_GOO_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
        SpawnPlacements.register(SHROOM_SLIME.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, AbstractSlimeEntity::checkSlimeSpawnRules);
    }


    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event)
    {
        int slimeConfigTotalWeight = SlimierSlimes.MAIN_CONFIG.totalSlimeSpawnWeight.get();

        ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        List<MobSpawnSettings.SpawnerData> slimeWeights = SLIMES.stream()
                .map(entityType -> getSpawner(event, entityType))
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());
        if(types.contains(BiomeDictionary.Type.SWAMP)){
            slimeConfigTotalWeight = slimeConfigTotalWeight * 3;
        }
        Integer currentTotal = event.getSpawns().getSpawner(MONSTER).stream().map(WeightedEntry.IntrusiveBase::getWeight).map(Weight::asInt).reduce(0, Integer::sum);
        if(TRUE.equals(SlimierSlimes.MAIN_CONFIG.useTotalSlimeSpawnWeight.get()) && slimeConfigTotalWeight < currentTotal) {
            slimeWeights = boundWeights(slimeWeights, slimeConfigTotalWeight);
        }
        addSlimeSpawners(event, slimeWeights);
    }

    private static Optional<MobSpawnSettings.SpawnerData> getSpawner(BiomeLoadingEvent event, EntityType<?> entityType) {
        if(SLIME_DATA.hasData()) {
            List<SlimeSpawnData> slimeSpawnDatas = SLIME_DATA.getData(entityType.getRegistryName()).getSlimeSpawnData();
            return slimeSpawnDatas.stream()
                    .filter(slimeSpawnData -> slimeSpawnData.isMatch(event)).findFirst()
                    .map(slimeSpawnData -> slimeSpawnData.getSpawner(entityType));
        }else{
            return Optional.empty();
        }

    }

    private static List<MobSpawnSettings.SpawnerData> boundWeights(List<MobSpawnSettings.SpawnerData> slimeWeights, final int totalWeight) {
        final double totalOriginal = slimeWeights.stream().map(WeightedEntry.IntrusiveBase::getWeight).map(Weight::asInt).reduce(0, Integer::sum);
        return slimeWeights.stream().map(spawner -> new MobSpawnSettings.SpawnerData(spawner.type, (int) Math.ceil((spawner.getWeight().asInt()/totalOriginal)*totalWeight), spawner.minCount, spawner.maxCount)).collect(Collectors.toList());
    }

    private static void addSlimeSpawners(BiomeLoadingEvent event, List<MobSpawnSettings.SpawnerData> slimeWeights) {
        slimeWeights.forEach(slimeSpawner -> event.getSpawns().addSpawn(MONSTER, slimeSpawner));
    }
}