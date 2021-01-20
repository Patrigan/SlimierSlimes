package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;

import static mod.patrigan.slimierslimes.init.ModBlocks.BLOCK_HELPERS;

public class ModBlockColors {
    public static void init(){
        BLOCK_HELPERS.forEach(buildingBlockHelper -> {
            if(buildingBlockHelper.isSlimy()) {
                Minecraft.getInstance().getBlockColors().register(
                        (state, reader, pos, color) -> ((IBlockColor) state.getBlock()).getColor(state, reader, pos, color),
                        buildingBlockHelper.getBlock().get());
                Minecraft.getInstance().getBlockColors().register(
                        ColorUtils::getColor,
                        buildingBlockHelper.getSlab().get(),
                        buildingBlockHelper.getStairs().get(),
                        buildingBlockHelper.getWall().get(),
                        buildingBlockHelper.getButton().get(),
                        buildingBlockHelper.getPressurePlate().get());
            }
        });
    }
}
