package mod.patrigan.slimierslimes.blocks.slimeblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import mod.patrigan.slimierslimes.util.ModBlockColor;
import mod.patrigan.slimierslimes.util.ModItemColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class BlackSlimeBlock extends SlimeBlock implements ModBlockColor, ModItemColor {


    public BlackSlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getColor(BlockState blockState, @Nullable IBlockDisplayReader iBlockDisplayReader, @Nullable BlockPos blockPos, int tintIndex) {
        return getColor(tintIndex);
    }

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        return getColor(tintIndex);
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? DyeColor.BLACK.getColorValue() : -1;
    }
}
