package mod.patrigan.slimier_slimes.blocks.slimeblock;

import mod.patrigan.slimier_slimes.util.ModBlockColor;
import mod.patrigan.slimier_slimes.util.ModItemColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

import javax.annotation.Nullable;

public class WhiteSlimeBlock extends SlimeBlock implements ModBlockColor, ModItemColor {


    public WhiteSlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter iBlockDisplayReader, @Nullable BlockPos blockPos, int tintIndex) {
        return getColor(tintIndex);
    }

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        return getColor(tintIndex);
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? DyeColor.WHITE.getMaterialColor().col : -1;
    }
}
