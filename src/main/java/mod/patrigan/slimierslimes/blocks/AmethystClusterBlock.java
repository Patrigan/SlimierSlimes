package mod.patrigan.slimierslimes.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class AmethystClusterBlock  extends Block {
    public AmethystClusterBlock() {
        super(Properties
                .create(Material.PLANTS, MaterialColor.MAGENTA)
                .zeroHardnessAndResistance()
                .doesNotBlockMovement());
    }
}