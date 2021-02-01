package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.DyeColor;

import static mod.patrigan.slimierslimes.init.ModBlocks.*;

public class ModBlockColors {
    public static void init(){
        Minecraft.getInstance().getBlockColors().register(
                (state, reader, pos, color) -> DyeColor.RED.getColorValue(),
                STONE_LAVA_SLIME_SPAWNER.get(), NETHERRACK_LAVA_SLIME_SPAWNER.get());
        BLOCK_HELPERS.forEach(buildingBlockHelper -> {
            if(buildingBlockHelper.getDyeColor() != null) {
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
