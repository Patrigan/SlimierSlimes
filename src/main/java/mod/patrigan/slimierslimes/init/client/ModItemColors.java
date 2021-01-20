package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.init.ModBlocks;
import mod.patrigan.slimierslimes.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;

import java.util.Arrays;

import static mod.patrigan.slimierslimes.init.ModItems.JELLY;

public class ModItemColors {
    public static void init(){
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> {
            Minecraft.getInstance().getItemColors().register(
                    (stack, tintIndex) -> ((IItemColor) stack.getItem()).getColor(stack, tintIndex),
                    JELLY.get(dyeColor).get());
            Minecraft.getInstance().getItemColors().register(
                    (stack, tintIndex) -> ((IItemColor) ((BlockItem) stack.getItem()).getBlock()).getColor(stack, tintIndex)
                    , ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(dyeColor).getBlock().get(), ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor).getBlock().get());
            Minecraft.getInstance().getItemColors().register(
                    (stack, tintIndex) -> ColorUtils.getColor(((BlockItem) stack.getItem()).getBlock(), tintIndex),
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
