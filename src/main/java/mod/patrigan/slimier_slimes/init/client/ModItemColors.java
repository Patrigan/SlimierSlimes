package mod.patrigan.slimier_slimes.init.client;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.util.ColorUtils;
import mod.patrigan.slimier_slimes.util.ModItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

import static mod.patrigan.slimier_slimes.init.ModBlocks.BLOCK_HELPERS;
import static mod.patrigan.slimier_slimes.init.ModBlocks.GOO_LAYER_BLOCKS;
import static mod.patrigan.slimier_slimes.init.ModItems.*;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModItemColors {
    @SubscribeEvent
    public static void init(ColorHandlerEvent.Item event){
        ItemColors itemColors = event.getItemColors();
        Arrays.stream(DyeColor.values()).forEach(dyeColor ->
            itemColors.register(
                    (stack, tintIndex) -> tintIndex == 0 ? dyeColor.getMaterialColor().col : 0xFFFFFFFF,
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
