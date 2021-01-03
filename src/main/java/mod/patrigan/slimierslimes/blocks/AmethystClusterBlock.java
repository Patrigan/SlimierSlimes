package mod.patrigan.slimierslimes.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class AmethystClusterBlock  extends Block {
    public AmethystClusterBlock() {
        super(Properties
                .create(Material.PLANTS, MaterialColor.MAGENTA)
                .zeroHardnessAndResistance()
                .doesNotBlockMovement());
    }
}