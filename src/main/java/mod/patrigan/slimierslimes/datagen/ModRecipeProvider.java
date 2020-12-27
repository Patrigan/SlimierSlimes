package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.DyeItem;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

import static mod.patrigan.slimierslimes.datagen.DataGenerators.DYE_ITEMS;
import static mod.patrigan.slimierslimes.init.ModItems.GREEN_JELLY;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        //List<Item> allElements = Tags.Items.DYES.getAllElements();
        DYE_ITEMS.forEach(dye -> ShapelessRecipeBuilder.shapelessRecipe(dye)
                .addIngredient(ITEMS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, ((DyeItem) dye).getDyeColor().getTranslationKey() + "_jelly")))
                .addCriterion("has_jelly", hasItem(GREEN_JELLY.get()))
                .build(consumer));
    }
}