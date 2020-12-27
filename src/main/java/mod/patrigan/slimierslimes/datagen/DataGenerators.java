package mod.patrigan.slimierslimes.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.item.Items.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    public static final List<Item> DYE_ITEMS = Arrays.asList(WHITE_DYE, LIGHT_GRAY_DYE, GRAY_DYE, BLACK_DYE, RED_DYE, ORANGE_DYE, YELLOW_DYE, LIME_DYE, GREEN_DYE, LIGHT_BLUE_DYE, CYAN_DYE, BLUE_DYE, PURPLE_DYE, MAGENTA_DYE, PINK_DYE, BROWN_DYE);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeClient()) {
            generator.addProvider(new ModBlockStateProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new ModItemModelProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new ModLanguageProvider(generator, "en_us"));
        }
        if (event.includeServer()) {
            ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(generator, event.getExistingFileHelper());
            generator.addProvider(new ModItemTagsProvider(generator, modBlockTagsProvider, event.getExistingFileHelper()));
            generator.addProvider(new ModRecipeProvider(generator));
            generator.addProvider(new ModEntityLootTablesProvider(generator));
            generator.addProvider(new ModBlockLootTablesProvider(generator));
        }
    }
}