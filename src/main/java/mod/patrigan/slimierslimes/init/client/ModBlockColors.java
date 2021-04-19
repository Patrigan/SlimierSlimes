package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.util.ColorUtils;
import mod.patrigan.slimierslimes.util.ModBlockColor;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

import static mod.patrigan.slimierslimes.init.ModBlocks.*;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBlockColors {
    @SubscribeEvent
    public static void init(ColorHandlerEvent.Block event){
        BlockColors blockColors = event.getBlockColors();
        blockColors.register(
                (state, reader, pos, color) -> DyeColor.RED.getColorValue(),
                STONE_LAVA_SLIME_SPAWNER.get(), NETHERRACK_LAVA_SLIME_SPAWNER.get());
        BLOCK_HELPERS.forEach(buildingBlockHelper -> {
            if(buildingBlockHelper.getDyeColor() != null) {
                blockColors.register(
                        (state, reader, pos, color) -> ((ModBlockColor) state.getBlock()).getColor(state, reader, pos, color),
                        buildingBlockHelper.getBlock().get());
                blockColors.register(
                        ColorUtils::getColor,
                        buildingBlockHelper.getSlab().get(),
                        buildingBlockHelper.getStairs().get(),
                        buildingBlockHelper.getWall().get(),
                        buildingBlockHelper.getButton().get(),
                        buildingBlockHelper.getPressurePlate().get());
            }
        });
        Arrays.stream(DyeColor.values()).forEach(dyeColor ->
                blockColors.register(
                        (state, reader, pos, tintIndex) -> tintIndex == 0 ? dyeColor.getColorValue() : 0xFFFFFFFF,
                        GOO_LAYER_BLOCKS.get(dyeColor).get())
        );
    }
}
