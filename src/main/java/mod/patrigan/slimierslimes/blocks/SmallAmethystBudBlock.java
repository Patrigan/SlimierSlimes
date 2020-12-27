package mod.patrigan.slimierslimes.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class SmallAmethystBudBlock extends Block {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D);

    public SmallAmethystBudBlock() {
        super(Properties
                .create(Material.PLANTS, MaterialColor.MAGENTA)
                .zeroHardnessAndResistance()
                .doesNotBlockMovement());
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

}