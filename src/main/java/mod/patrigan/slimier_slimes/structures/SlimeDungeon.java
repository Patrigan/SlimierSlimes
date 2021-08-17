package mod.patrigan.slimier_slimes.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import mod.patrigan.slimier_slimes.SlimierSlimes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;

import static mod.patrigan.slimier_slimes.init.ModEntityTypes.*;

public class SlimeDungeon extends StructureFeature<NoneFeatureConfiguration> {

    public static final String STRUCTURE_ID = "slime_dungeon";

    public SlimeDungeon(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return SlimeDungeon.Start::new;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }


    /**
     * || ONLY WORKS IN FORGE 34.1.12+ ||
     * <p>
     * This method allows us to have mobs that spawn naturally over time in our structure.
     * No other mobs will spawn in the structure of the same entity classification.
     * The reason you want to match the classifications is so that your structure's mob
     * will contribute to that classification's cap. Otherwise, it may cause a runaway
     * spawning of the mob that will never stop.
     * <p>
     * NOTE: getDefaultSpawnList is for monsters only and getDefaultCreatureSpawnList is
     * for creatures only. If you want to add entities of another classification,
     * use the StructureSpawnListGatherEvent to add water_creatures, water_ambient,
     * ambient, or misc mobs. Use that event to add/remove mobs from structures
     * that are not your own.
     */
    private static final List<MobSpawnSettings.SpawnerData> STRUCTURE_MONSTERS = ImmutableList.of(
            new MobSpawnSettings.SpawnerData(COMMON_SLIME.get(), 100, 1, 1),
            new MobSpawnSettings.SpawnerData(PINK_SLIME.get(), 10, 1, 1),
            new MobSpawnSettings.SpawnerData(ROCK_SLIME.get(), 75, 1, 1),
            new MobSpawnSettings.SpawnerData(CRYSTAL_SLIME.get(), 20, 1, 1),
            new MobSpawnSettings.SpawnerData(GLOW_SLIME.get(), 30, 1, 1),
            new MobSpawnSettings.SpawnerData(CREEPER_SLIME.get(), 75, 1, 1),
            new MobSpawnSettings.SpawnerData(CAMO_SLIME.get(), 50, 1, 1),
            new MobSpawnSettings.SpawnerData(SNOW_SLIME.get(), 80, 1, 1),
            new MobSpawnSettings.SpawnerData(LAVA_SLIME.get(), 5, 1, 1),
            new MobSpawnSettings.SpawnerData(DIAMOND_SLIME.get(), 1, 1, 1)
    );

    @Override
    public List<MobSpawnSettings.SpawnerData> getDefaultSpawnList() {
        return STRUCTURE_MONSTERS;
    }

    private static final List<MobSpawnSettings.SpawnerData> STRUCTURE_CREATURES = ImmutableList.of(
    );

    @Override
    public List<MobSpawnSettings.SpawnerData> getDefaultCreatureSpawnList() {
        return STRUCTURE_CREATURES;
    }


    /*
     * This is where extra checks can be done to determine if the structure can spawn here.
     * This only needs to be overridden if you're adding additional spawn conditions.
     *
     * Notice how the biome is also passed in. Though, you are not going to
     * do any biome checking here as you should've added this structure to
     * the biomes you wanted already with the biome load event.
     *
     * Basically, this method is used for determining if the land is at a suitable height,
     * if certain other structures are too close or not, or some other restrictive condition.
     *
     * For example, Pillager Outposts added a check to make sure it cannot spawn within 10 chunk of a Village.
     * (Bedrock Edition seems to not have the same check)
     *
     *
     * Also, please for the love of god, do not do dimension checking here. If you do and
     * another mod's dimension is trying to spawn your structure, the locate
     * command will make minecraft hang forever and break the game.
     *
     * Instead, use the addDimensionalSpacing method in StructureTutorialMain class.
     * If you check for the dimension there and do not add your structure's
     * spacing into the chunk generator, the structure will not spawn in that dimension!
     */
    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, WorldgenRandom chunkRandom, ChunkPos chunkPos, Biome biome, ChunkPos chunkPos1, NoneFeatureConfiguration featureConfig, LevelHeightAccessor levelHeightAccessor) {
        int x = (chunkPos.x << 4) + 7;
        int z = (chunkPos.z << 4) + 7;
        return chunkGenerator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor) > 20;
    }


    /**
     * Handles calling up the structure's pieces class and height that structure will spawn at.
     */
    public static class Start extends StructureStart<NoneFeatureConfiguration> {

        public Start(StructureFeature<NoneFeatureConfiguration> p_163595_, ChunkPos p_163596_, int p_163597_, long p_163598_) {
            super(p_163595_, p_163596_, p_163597_, p_163598_);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager templateManagerIn, ChunkPos chunkPos, Biome biomeIn, NoneFeatureConfiguration config, LevelHeightAccessor levelHeightAccessor) {
            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkPos.x << 4) + 7;
            int z = (chunkPos.z << 4) + 7;

            /*
             * We pass this into addPieces to tell it where to generate the structure.
             * If addPieces's last parameter is true, blockpos's Y value is ignored and the
             * structure will spawn at terrain height instead. Set that parameter to false to
             * force the structure to spawn at blockpos's Y value instead. You got options here!
             */
            int heightY = chunkGenerator.getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, levelHeightAccessor);
            int randY = random.nextInt(heightY - 20) + 10;
            BlockPos blockpos = new BlockPos(x, randY, z);

            // All a structure has to do is call this method to turn it into a jigsaw based structure!
            JigsawPlacement.addPieces(
                    dynamicRegistryManager,
                    new JigsawConfiguration(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            // The path to the starting Template Pool JSON file to read.
                            //
                            // Note, this is "structure_tutorial:run_down_house/start_pool" which means
                            // the game will automatically look into the following path for the template pool:
                            // "resources/data/structure_tutorial/worldgen/template_pool/run_down_house/start_pool.json"
                            // This is why your pool files must be in "data/<modid>/worldgen/template_pool/<the path to the pool here>"
                            // because the game automatically will check in worldgen/template_pool for the pools.
                            .get(new ResourceLocation(SlimierSlimes.MOD_ID, STRUCTURE_ID + "/start_pool")),

                            // How many pieces outward from center can a recursive jigsaw structure spawn.
                            // Our structure is only 1 piece outward and isn't recursive so any value of 1 or more doesn't change anything.
                            // However, I recommend you keep this a decent value like 10 so people can use datapacks to add additional pieces to your structure easily.
                            // But don't make it too large for recursive structures like villages or you'll crash server due to hundreds of pieces attempting to generate!
                            10),
                    PoolElementStructurePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
                    this, // The list that will be populated with the jigsaw pieces after this method.
                    this.random,
                    false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                    // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                    false,
                    levelHeightAccessor); // Place at heightmap (top land). Set this to false for structure to be place at blockpos's Y value instead


            // **THE FOLLOWING TWO LINES ARE OPTIONAL**
            //
            // Right here, you can do interesting stuff with the pieces in this.components such as offset the
            // center piece by 50 blocks up for no reason, remove repeats of a piece or add a new piece so
            // only 1 of that piece exists, etc. But you do not have access to the piece's blocks as this list
            // holds just the piece's size and positions. Blocks will be placed later in JigsawManager.
            //
            // In this case, we do `piece.offset` to raise pieces up by 1 block so that the house is not right on
            // the surface of water or sunken into land a bit.
            //
            // Then we extend the bounding box down by 1 by doing `piece.getBoundingBox().minY` which will cause the
            // land formed around the structure to be lowered and not cover the doorstep. You can raise the bounding
            // box to force the structure to be buried as well. This bounding box stuff with land is only for structures
            // that you added to Structure.NOISE_AFFECTING_FEATURES field handles adding land around the base of structures.
            //
            // By lifting the house up by 1 and lowering the bounding box, the land at bottom of house will now be
            // flush with the surrounding terrain without blocking off the doorstep.
            //this.components.forEach(piece -> piece.offset(0, 1, 0));
            //this.components.forEach(piece -> piece.getBoundingBox().minY += 1);


            // Sets the bounds of the structure once you are finished.
            this.createBoundingBox();

            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
            /*SlimierSlimes.LOGGER.log(Level.DEBUG, "Slime Dungeon at " +
                    this.pieces.get(0).getBoundingBox().x0 + " " +
                    this.pieces.get(0).getBoundingBox().y0 + " " +
                    this.pieces.get(0).getBoundingBox().z0);*/
        }

    }
}
