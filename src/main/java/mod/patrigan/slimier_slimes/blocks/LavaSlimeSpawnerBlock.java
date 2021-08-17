package mod.patrigan.slimier_slimes.blocks;

import mod.patrigan.slimier_slimes.tileentities.LavaSlimeSpawnerBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

import static mod.patrigan.slimier_slimes.init.ModBlockEntityTypes.LAVA_SLIME_SPAWNER;

public class LavaSlimeSpawnerBlock extends SlimyStoneBlock implements EntityBlock {

    public LavaSlimeSpawnerBlock() {
        super(DyeColor.RED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return LAVA_SLIME_SPAWNER.get().create(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, LAVA_SLIME_SPAWNER.get(),  level.isClientSide ? LavaSlimeSpawnerBlockEntity::clientTick : LavaSlimeSpawnerBlockEntity::serverTick);
    }

    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader world, BlockPos pos, int fortune, int silktouch) {
        return 20 + RANDOM.nextInt(20) + RANDOM.nextInt(20);
    }
}