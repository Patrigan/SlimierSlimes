package mod.patrigan.slimierslimes.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

import net.minecraft.block.AbstractBlock.Properties;

public class AmethystClusterBlock  extends Block {
    public AmethystClusterBlock() {
        super(Properties
                .of(Material.PLANT, MaterialColor.COLOR_MAGENTA)
                .instabreak()
                .noCollission());
    }
}