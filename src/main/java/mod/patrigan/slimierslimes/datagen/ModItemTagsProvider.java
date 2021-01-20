package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.Arrays;

import static mod.patrigan.slimierslimes.init.ModItems.JELLY;

public class ModItemTagsProvider extends net.minecraft.data.ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> this.getOrCreateBuilder(ModTags.Items.JELLY).add(JELLY.get(dyeColor).get()));
    }
}
