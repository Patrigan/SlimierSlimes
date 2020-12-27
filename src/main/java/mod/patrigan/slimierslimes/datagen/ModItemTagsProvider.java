package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends net.minecraft.data.ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(ModTags.Items.JELLY).add(
                ModItems.WHITE_JELLY.get(), ModItems.LIGHT_GRAY_JELLY.get(), ModItems.GRAY_JELLY.get(), ModItems.BLACK_JELLY.get(),
                ModItems.RED_JELLY.get(), ModItems.ORANGE_JELLY.get(), ModItems.YELLOW_JELLY.get(), ModItems.LIME_JELLY.get(),
                ModItems.GREEN_JELLY.get(), ModItems.LIGHT_BLUE_JELLY.get(), ModItems.CYAN_JELLY.get(), ModItems.BLUE_JELLY.get(),
                ModItems.PURPLE_JELLY.get(), ModItems.MAGENTA_JELLY.get(), ModItems.PINK_JELLY.get(), ModItems.BROWN_JELLY.get());
    }
}
