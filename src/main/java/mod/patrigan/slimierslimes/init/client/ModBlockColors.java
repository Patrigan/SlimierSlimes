package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.init.ModBlocks;
import mod.patrigan.slimierslimes.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.DyeColor;

import java.util.Arrays;

public class ModBlockColors {
    public static void init(){
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> {
            Minecraft.getInstance().getBlockColors().register(
                    (state, reader, pos, color) -> ((IBlockColor) state.getBlock()).getColor(state, reader, pos, color),
                    ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(dyeColor).getBlock().get(),
                    ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor).getBlock().get());
            Minecraft.getInstance().getBlockColors().register(
                    ColorUtils::getColor ,
                    ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(dyeColor).getSlab().get(),
                    ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(dyeColor).getStairs().get(),
                    ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(dyeColor).getWall().get(),
                    ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(dyeColor).getButton().get(),
                    ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(dyeColor).getPressurePlate().get(),
                    ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor).getSlab().get(),
                    ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor).getStairs().get(),
                    ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor).getWall().get(),
                    ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor).getButton().get(),
                    ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor).getPressurePlate().get());
        });
    }
}
