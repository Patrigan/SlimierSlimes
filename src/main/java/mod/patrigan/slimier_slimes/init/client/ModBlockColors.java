package mod.patrigan.slimier_slimes.init.client;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.util.ColorUtils;
import mod.patrigan.slimier_slimes.util.ModBlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

import static mod.patrigan.slimier_slimes.init.ModBlocks.*;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBlockColors {
    @SubscribeEvent
    public static void init(ColorHandlerEvent.Block event){
        BlockColors blockColors = event.getBlockColors();
        blockColors.register(
                (state, reader, pos, color) -> DyeColor.RED.getMaterialColor().col,
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
                        (state, reader, pos, tintIndex) -> tintIndex == 0 ? dyeColor.getMaterialColor().col : 0xFFFFFFFF,
                        GOO_LAYER_BLOCKS.get(dyeColor).get())
        );
    }
}
