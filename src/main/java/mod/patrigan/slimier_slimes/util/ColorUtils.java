package mod.patrigan.slimier_slimes.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import mod.patrigan.slimier_slimes.SlimierSlimes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

public class ColorUtils {
    public static final Codec<DyeColor> DYE_COLOR_CODEC = Codec.STRING.flatComapMap(s -> DyeColor.byName(s, null), d -> DataResult.success(d.getName()));

    private enum DyeColorAdjustment {
        WHITE(DyeColor.WHITE, 16580606),
        ORANGE(DyeColor.ORANGE, 16235402),
        MAGENTA(DyeColor.MAGENTA, 13011906),
        LIGHT_BLUE(DyeColor.LIGHT_BLUE, 9094872),
        YELLOW(DyeColor.YELLOW, 16574879),
        LIME(DyeColor.LIME, 10798707),
        PINK(DyeColor.PINK, 15908814),
        GRAY(DyeColor.GRAY, 5001297),
        LIGHT_GRAY(DyeColor.LIGHT_GRAY, 10197913),
        CYAN(DyeColor.CYAN, 5938075),
        PURPLE(DyeColor.PURPLE, 10450359),
        BLUE(DyeColor.BLUE, 7370149),
        BROWN(DyeColor.BROWN, 8547163),
        GREEN(DyeColor.GREEN, 7109192),
        RED(DyeColor.RED, 11497067),
        BLACK(DyeColor.BLACK, 1842206);
        private final DyeColor dyeColor;
        private final int desaturated;
        private DyeColorAdjustment(DyeColor dyeColorIn, int desaturatedIn){
            this.dyeColor = dyeColorIn;
            this.desaturated = desaturatedIn;
        }
        public static DyeColorAdjustment byDyeColor(DyeColor dyeColor) {
            return Arrays.stream(DyeColorAdjustment.values()).filter(dyeColorAdjustment -> dyeColorAdjustment.getDyeColor().equals(dyeColor)).findFirst().orElse(LIME);
        }

        public DyeColor getDyeColor() {
            return dyeColor;
        }

        public int getDesaturated() {
            return desaturated;
        }
    }
    public static int getColor(BlockState blockState, BlockAndTintGetter reader, BlockPos blockPos, int tintIndex) {
        String[] idParts = blockState.getBlock().getRegistryName().getPath().split("_");
        String blockId = getBlockId(idParts);
        return ((ModBlockColor) ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, blockId))).getColor(blockState, reader, blockPos, tintIndex);
    }

    public static int getColor(ItemStack blockIn, int tintIndex) {
        String[] idParts = blockIn.getItem().getRegistryName().getPath().split("_");
        String blockId = getBlockId(idParts);
        return ((ModItemColor) ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, blockId))).getColor(blockIn, tintIndex);
    }

    private static String getBlockId(String[] idParts) {
        StringBuilder blockId = new StringBuilder(idParts[0]);
        int newLength = idParts.length - 1;
        if (idParts[idParts.length - 1].equals("plate")) {
            newLength = idParts.length - 2;
        }
        for (int part = 1; part < newLength; part++) {
            blockId.append("_").append(idParts[part]);
        }
        return blockId.toString();
    }

    public static int getSlimyBlockColor(DyeColor dyeColor) {
        return DyeColorAdjustment.byDyeColor(dyeColor).getDesaturated();
    }
}
