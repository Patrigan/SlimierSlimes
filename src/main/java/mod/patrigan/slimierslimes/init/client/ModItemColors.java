package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.util.ColorUtils;
import mod.patrigan.slimierslimes.util.ModItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

import static mod.patrigan.slimierslimes.init.ModBlocks.BLOCK_HELPERS;
import static mod.patrigan.slimierslimes.init.ModBlocks.GOO_LAYER_BLOCKS;
import static mod.patrigan.slimierslimes.init.ModItems.JELLY;
import static mod.patrigan.slimierslimes.init.ModItems.SLIME_BALL;
import static mod.patrigan.slimierslimes.init.ModItems.*;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModItemColors {
    @SubscribeEvent
    public static void init(ColorHandlerEvent.Item event){
        ItemColors itemColors = event.getItemColors();
        Arrays.stream(DyeColor.values()).forEach(dyeColor ->
            itemColors.register(
                    (stack, tintIndex) -> tintIndex == 0 ? dyeColor.getColorValue() : 0xFFFFFFFF,
                    JELLY.get(dyeColor).get(),
                    SLIME_BALL.get(dyeColor).get(),
                    SLIME_CHESTPLATE.get(dyeColor).get(),
                    SLIME_BOOTS.get(dyeColor).get(),
                    SLIME_LEGGINGS.get(dyeColor).get(),
                    SLIME_HELMET.get(dyeColor).get(),
                    GOO_LAYER_BLOCKS.get(dyeColor).get())
        );
        BLOCK_HELPERS.forEach(buildingBlockHelper -> {
            if(buildingBlockHelper.getDyeColor() != null) {
                itemColors.register(
                        (stack, tintIndex) ->  tintIndex == 0 ? ((ModItemColor) ((BlockItem) stack.getItem()).getBlock()).getColor(stack, tintIndex) : 0xFFFFFFFF,
                        buildingBlockHelper.getBlock().get());
                itemColors.register(
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
