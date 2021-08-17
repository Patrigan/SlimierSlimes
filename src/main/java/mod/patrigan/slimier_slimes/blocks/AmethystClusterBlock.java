package mod.patrigan.slimier_slimes.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class AmethystClusterBlock  extends Block {
    public AmethystClusterBlock() {
        super(Properties
                .of(Material.PLANT, MaterialColor.COLOR_MAGENTA)
                .instabreak()
                .noCollission());
    }
}