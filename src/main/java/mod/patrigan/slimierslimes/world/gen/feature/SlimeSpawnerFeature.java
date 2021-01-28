package mod.patrigan.slimierslimes.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.BitSet;
import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.SPAWNER_ENTITY_TYPES;
import static net.minecraft.block.Blocks.LAVA;

public class SlimeSpawnerFeature extends Feature<NoFeatureConfig> {
    public SlimeSpawnerFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        BlockState blockState = Blocks.SPAWNER.getDefaultState();
        reader.setBlockState(pos, blockState, 2);
        TileEntity tileentity = reader.getTileEntity(pos);
        ((MobSpawnerTileEntity)tileentity).getSpawnerBaseLogic().setEntityType(
                    SPAWNER_ENTITY_TYPES.get(rand.nextInt(SPAWNER_ENTITY_TYPES.size())));
        return true;
    }
}