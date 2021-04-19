package mod.patrigan.slimierslimes.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.SPAWNER_ENTITY_TYPES;

public class SlimeSpawnerFeature extends Feature<NoFeatureConfig> {
    public SlimeSpawnerFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        BlockState blockState = Blocks.SPAWNER.defaultBlockState();
        reader.setBlock(pos, blockState, 2);
        TileEntity tileentity = reader.getBlockEntity(pos);
        ((MobSpawnerTileEntity)tileentity).getSpawner().setEntityId(
                    SPAWNER_ENTITY_TYPES.get(rand.nextInt(SPAWNER_ENTITY_TYPES.size())));
        return true;
    }
}