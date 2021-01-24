package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;

import java.util.Arrays;

import static mod.patrigan.slimierslimes.init.ModBlocks.BLOCK_HELPERS;
import static mod.patrigan.slimierslimes.init.ModItems.JELLY;
import static mod.patrigan.slimierslimes.init.ModItems.SLIME_BALL;

public class ModItemColors {
    public static void init(){
        Arrays.stream(DyeColor.values()).forEach(dyeColor ->
            Minecraft.getInstance().getItemColors().register(
                    (stack, tintIndex) -> dyeColor.getColorValue(),
                    JELLY.get(dyeColor).get(),
                    SLIME_BALL.get(dyeColor).get())
        );
        BLOCK_HELPERS.forEach(buildingBlockHelper -> {
            if(buildingBlockHelper.isSlimy()) {
                Minecraft.getInstance().getItemColors().register(
                        (stack, tintIndex) -> ((IItemColor) ((BlockItem) stack.getItem()).getBlock()).getColor(stack, tintIndex),
                        buildingBlockHelper.getBlock().get());
                Minecraft.getInstance().getItemColors().register(
                        (stack, tintIndex) -> ColorUtils.getColor(((BlockItem) stack.getItem()).getBlock(), tintIndex),
                        buildingBlockHelper.getSlab().get(),
                        buildingBlockHelper.getStairs().get(),
                        buildingBlockHelper.getWall().get(),
                        buildingBlockHelper.getButton().get(),
                        buildingBlockHelper.getPressurePlate().get());
            }
        });
    }
}
