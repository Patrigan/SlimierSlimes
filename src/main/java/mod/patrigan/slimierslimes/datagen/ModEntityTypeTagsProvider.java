package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.init.ModEntityTypes;
import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.Arrays;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;
import static mod.patrigan.slimierslimes.init.ModItems.JELLY;
import static mod.patrigan.slimierslimes.init.ModItems.SLIME_BALL;
import static net.minecraftforge.common.Tags.Items.SLIMEBALLS;

public class ModEntityTypeTagsProvider extends net.minecraft.data.EntityTypeTagsProvider {


    public ModEntityTypeTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        ModEntityTypes.SLIMES.forEach(slimeEntityType -> this.tag(ModTags.EntityTypes.SLIMES).add(slimeEntityType));
    }
}
