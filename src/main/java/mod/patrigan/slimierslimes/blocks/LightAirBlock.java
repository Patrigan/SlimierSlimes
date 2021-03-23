package mod.patrigan.slimierslimes.blocks;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

import static mod.patrigan.slimierslimes.init.ModTileEntityTypes.MONSTER_LIGHT_AIR;

import net.minecraft.block.AbstractBlock.Properties;

public class LightAirBlock extends AirBlock {
    public LightAirBlock() {
        super(Properties.of(Material.AIR)
                .noCollission()
                .air()
                .noDrops()
                .lightLevel(lightLevel -> 15));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MONSTER_LIGHT_AIR.get().create();
    }
}
