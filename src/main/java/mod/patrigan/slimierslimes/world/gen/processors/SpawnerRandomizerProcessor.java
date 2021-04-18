package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.util.GeneralUtils;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.List;
import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModProcessors.SPAWNER_RANDOMIZER_PROCESSOR;

public class SpawnerRandomizerProcessor extends StructureProcessor {

    public static final Codec<SpawnerRandomizerProcessor> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.mapPair(Registry.ENTITY_TYPE.fieldOf("resourcelocation"), Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight")).codec().listOf().fieldOf("spawner_mob_entries").forGetter(processor -> processor.entityWeightMap),
            Codec.SHORT.optionalFieldOf("delay", (short) 20).forGetter(processor -> processor.delay),
            Codec.SHORT.optionalFieldOf("min_spawn_delay", (short) 200).forGetter(processor -> processor.minSpawnDelay),
            Codec.SHORT.optionalFieldOf("max_spawn_delay", (short) 800).forGetter(processor -> processor.maxSpawnDelay),
            Codec.SHORT.optionalFieldOf("spawn_count", (short) 4).forGetter(processor -> processor.spawnCount),
            Codec.SHORT.optionalFieldOf("max_nearby_entities", (short) 6).forGetter(processor -> processor.maxNearbyEntities),
            Codec.SHORT.optionalFieldOf("required_player_range", (short) 16).forGetter(processor -> processor.requiredPlayerRange),
            Codec.SHORT.optionalFieldOf("spawn_range", (short) 4).forGetter(processor -> processor.spawnRange)
    ).apply(builder, builder.stable(SpawnerRandomizerProcessor::new)));
    private static final long SEED = 531498L;

    public final List<Pair<EntityType<?>, Integer>> entityWeightMap;
    public final Short delay;
    public final Short minSpawnDelay;
    public final Short maxSpawnDelay;
    public final Short spawnCount;
    public final Short maxNearbyEntities;
    public final Short requiredPlayerRange;
    public final Short spawnRange;

    public SpawnerRandomizerProcessor(List<Pair<EntityType<?>, Integer>> entityWeightMap, Short delay, Short minSpawnDelay, Short maxSpawnDelay, Short spawnCount, Short maxNearbyEntities, Short requiredPlayerRange, Short spawnRange) {
        this.entityWeightMap = entityWeightMap;
        this.delay = delay;
        this.minSpawnDelay = minSpawnDelay;
        this.maxSpawnDelay = maxSpawnDelay;
        this.spawnCount = spawnCount;
        this.maxNearbyEntities = maxNearbyEntities;
        this.requiredPlayerRange = requiredPlayerRange;
        this.spawnRange = spawnRange;
    }


    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        if (blockInfo.state.getBlock() instanceof SpawnerBlock) {
            Random random = ProcessorUtil.getRandom(blockInfo.pos, SEED);
            return new Template.BlockInfo(
                    blockInfo.pos,
                    blockInfo.state,
                    setMobSpawnerEntity(random, blockInfo.nbt));
        }
        return blockInfo;
    }

    /**
     * Makes the given block entity now have the correct spawner mob
     */
    private CompoundNBT setMobSpawnerEntity(Random random, CompoundNBT nbt) {
        EntityType<?> entity = GeneralUtils.getRandomEntry(entityWeightMap, random);
        if (entity != null) {
            CompoundNBT compound = new CompoundNBT();
            compound.putShort("Delay", delay);
            compound.putShort("MinSpawnDelay", minSpawnDelay);
            compound.putShort("MaxSpawnDelay", maxSpawnDelay);
            compound.putShort("SpawnCount", spawnCount);
            compound.putShort("MaxNearbyEntities", maxNearbyEntities);
            compound.putShort("RequiredPlayerRange", requiredPlayerRange);
            compound.putShort("SpawnRange", spawnRange);

            CompoundNBT spawnData = new CompoundNBT();
            spawnData.putString("id", Registry.ENTITY_TYPE.getKey(entity).toString());
            compound.put("SpawnData", spawnData);

            CompoundNBT entityData = new CompoundNBT();
            entityData.putString("id", Registry.ENTITY_TYPE.getKey(entity).toString());

            CompoundNBT listEntry = new CompoundNBT();
            listEntry.put("Entity", entityData);
            listEntry.putInt("Weight", 1);

            ListNBT listnbt = new ListNBT();
            listnbt.add(listEntry);

            compound.put("SpawnPotentials", listnbt);

            return compound;
        }
        else {
            SlimierSlimes.LOGGER.warn("EntityType in a dungeon does not exist in registry! : {}", entityWeightMap);
        }

        return nbt;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return SPAWNER_RANDOMIZER_PROCESSOR;
    }
}