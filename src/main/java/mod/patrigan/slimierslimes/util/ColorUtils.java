package mod.patrigan.slimierslimes.util;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.blocks.SlimyStoneBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.registries.ForgeRegistries;

public class ColorUtils {
    public static int getColor(BlockState blockState, IBlockDisplayReader reader, BlockPos blockPos, int tintIndex) {
        String[] idParts = blockState.getBlock().getRegistryName().getPath().split("_");
        String blockId = getBlockId(idParts);
        return ((IBlockColor) ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, blockId))).getColor(blockState, reader, blockPos, tintIndex);
    }

    public static int getColor(ItemStack blockIn, int tintIndex) {
        String[] idParts = blockIn.getItem().getRegistryName().getPath().split("_");
        String blockId = getBlockId(idParts);
        return ((IItemColor) ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, blockId))).getColor(blockIn, tintIndex);
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
}
