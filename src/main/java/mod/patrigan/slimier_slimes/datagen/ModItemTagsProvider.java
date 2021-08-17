package mod.patrigan.slimier_slimes.datagen;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.init.ModTags;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.Arrays;

import static mod.patrigan.slimier_slimes.init.ModItems.*;
import static net.minecraftforge.common.Tags.Items.SLIMEBALLS;

public class ModItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider {

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
