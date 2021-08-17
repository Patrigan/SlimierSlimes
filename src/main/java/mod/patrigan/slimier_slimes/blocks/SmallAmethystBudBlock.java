package mod.patrigan.slimier_slimes.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class SmallAmethystBudBlock extends Block {
    public SmallAmethystBudBlock() {
        super(Properties
                .of(Material.PLANT, MaterialColor.COLOR_MAGENTA)
                .instabreak()
                .noCollission());
    }
}