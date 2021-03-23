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
import static mod.patrigan.slimierslimes.init.ModItems.SLIME_BALL;
import static net.minecraftforge.common.Tags.Items.SLIMEBALLS;

public class ModItemTagsProvider extends net.minecraft.data.ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> this.tag(ModTags.Items.JELLIES).add(JELLY.get(dyeColor).get()));
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> this.tag(SLIMEBALLS).add(SLIME_BALL.get(dyeColor).get()));
    }
}
