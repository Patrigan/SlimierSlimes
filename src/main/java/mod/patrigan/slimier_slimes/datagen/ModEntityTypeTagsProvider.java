package mod.patrigan.slimier_slimes.datagen;

import mod.patrigan.slimier_slimes.init.ModEntityTypes;
import mod.patrigan.slimier_slimes.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static mod.patrigan.slimier_slimes.SlimierSlimes.MOD_ID;

public class ModEntityTypeTagsProvider extends net.minecraft.data.tags.EntityTypeTagsProvider {


    public ModEntityTypeTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        ModEntityTypes.SLIMES.forEach(slimeEntityType -> this.tag(ModTags.EntityTypes.SLIMES).add(slimeEntityType));
    }
}
