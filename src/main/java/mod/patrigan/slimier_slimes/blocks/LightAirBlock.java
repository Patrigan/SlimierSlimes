package mod.patrigan.slimier_slimes.blocks;

import mod.patrigan.slimier_slimes.entities.GlowSlimeEntity;
import mod.patrigan.slimier_slimes.tileentities.LightAirBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import java.util.List;

import static mod.patrigan.slimier_slimes.init.ModBlockEntityTypes.MONSTER_LIGHT_AIR;

public class LightAirBlock extends BaseEntityBlock {
    public LightAirBlock() {
        super(Properties.of(Material.AIR)
                .noCollission()
                .air()
                .noDrops()
                .lightLevel(lightLevel -> 15));
    }

    public RenderShape getRenderShape(BlockState p_48758_) {
        return RenderShape.INVISIBLE;
    }

    public VoxelShape getShape(BlockState p_48760_, BlockGetter p_48761_, BlockPos p_48762_, CollisionContext p_48763_) {
        return Shapes.empty();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return MONSTER_LIGHT_AIR.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return !level.isClientSide ? createTickerHelper(blockEntityType, MONSTER_LIGHT_AIR.get(), LightAirBlock::tickEntity) : null;
    }

    private static void tickEntity(Level level, BlockPos blockPos, BlockState blockState, LightAirBlockEntity daylightDetectorBlockEntity) {
        AABB bounds = new AABB(blockPos).inflate(1);
        List<GlowSlimeEntity> entitiesWithinAABB = level.getEntitiesOfClass(GlowSlimeEntity.class, bounds);
        if(entitiesWithinAABB.isEmpty()){
            level.removeBlock(blockPos, false);
        }
    }
}
