package mod.patrigan.slimierslimes.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

import static mod.patrigan.slimierslimes.init.ModTileEntityTypes.LAVA_SLIME_SPAWNER;

public class LavaSlimeSpawnerBlock extends SlimyStoneBlock {

    public LavaSlimeSpawnerBlock() {
        super(DyeColor.RED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        return 20 + RANDOM.nextInt(20) + RANDOM.nextInt(20);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return LAVA_SLIME_SPAWNER.get().create();
    }
}