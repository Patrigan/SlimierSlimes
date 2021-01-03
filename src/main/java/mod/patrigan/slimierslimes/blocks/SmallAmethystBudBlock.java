package mod.patrigan.slimierslimes.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class SmallAmethystBudBlock extends Block {
    public SmallAmethystBudBlock() {
        super(Properties
                .create(Material.PLANTS, MaterialColor.MAGENTA)
                .zeroHardnessAndResistance()
                .doesNotBlockMovement());
    }
}