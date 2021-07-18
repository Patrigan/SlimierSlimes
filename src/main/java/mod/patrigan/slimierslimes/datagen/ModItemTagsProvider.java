package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.Arrays;

import static mod.patrigan.slimierslimes.init.ModItems.*;
import static net.minecraftforge.common.Tags.Items.SLIMEBALLS;

public class ModItemTagsProvider extends net.minecraft.data.ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> {
            this.tag(ModTags.Items.JELLIES).add(JELLY.get(dyeColor).get());
            this.tag(SLIMEBALLS).add(SLIME_BALL.get(dyeColor).get());
            this.tag(ModTags.Items.ARMOR_SLIME_CHESTPLATE).add(SLIME_CHESTPLATE.get(dyeColor).get());
            this.tag(ModTags.Items.ARMOR_SLIME_BOOTS).add(SLIME_BOOTS.get(dyeColor).get());
            this.tag(ModTags.Items.ARMOR_SLIME_HELMET).add(SLIME_HELMET.get(dyeColor).get());
            this.tag(ModTags.Items.ARMOR_SLIME_LEGGINGS).add(SLIME_LEGGINGS.get(dyeColor).get());
        });
    }
}
