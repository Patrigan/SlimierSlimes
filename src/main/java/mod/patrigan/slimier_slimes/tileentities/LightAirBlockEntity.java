package mod.patrigan.slimier_slimes.tileentities;

import mod.patrigan.slimier_slimes.init.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LightAirBlockEntity extends BlockEntity {

    public LightAirBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.MONSTER_LIGHT_AIR.get(), blockPos, blockState);
    }
}
