package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.function.Consumer;

import static mod.patrigan.slimierslimes.datagen.DataGenerators.DYE_ITEMS;
import static mod.patrigan.slimierslimes.init.ModBlocks.SLIMY_COBBLESTONE_BLOCK;
import static mod.patrigan.slimierslimes.init.ModBlocks.SLIMY_STONE_BLOCK;
import static mod.patrigan.slimierslimes.init.ModItems.JELLY;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        DYE_ITEMS.forEach(dye -> ShapelessRecipeBuilder.shapelessRecipe(dye)
                .addIngredient(JELLY.get(((DyeItem) dye).getDyeColor()).get())
                .addCriterion("has_jelly", hasItem(ModTags.Items.JELLY))
                .build(consumer));
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> {
            buildingBlockRecipes(SLIMY_STONE_BLOCK.get(dyeColor), consumer);
            buildingBlockRecipes(SLIMY_COBBLESTONE_BLOCK.get(dyeColor), consumer);
        });
    }

    private void buildingBlockRecipes(BuildingBlockHelper blockHelper, Consumer<IFinishedRecipe> consumer){
        ShapedRecipeBuilder.shapedRecipe(blockHelper.getSlab().get(), 6).key('#', blockHelper.getBlock().get())
                .patternLine("###")
                .addCriterion("has_" + blockHelper.getId(), hasItem(blockHelper.getBlock().get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(blockHelper.getStairs().get(), 4).key('#', blockHelper.getBlock().get())
                .patternLine("#  ")
                .patternLine("## ")
                .patternLine("###")
                .addCriterion("has_" +  blockHelper.getId(), hasItem(blockHelper.getBlock().get())).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(blockHelper.getButton().get()).addIngredient(blockHelper.getBlock().get())
                .addCriterion("has_" +  blockHelper.getId(), hasItem(blockHelper.getBlock().get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(blockHelper.getPressurePlate().get())
                .key('#', blockHelper.getBlock().get())
                .patternLine("##")
                .addCriterion("has_" +  blockHelper.getId(), hasItem(blockHelper.getBlock().get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(blockHelper.getWall().get(), 6).key('#', blockHelper.getBlock().get())
                .patternLine("###")
                .patternLine("###")
                .addCriterion("has_" + blockHelper.getId(), hasItem(blockHelper.getBlock().get())).build(consumer);
        stoneCutterRecipes(blockHelper, consumer);
    }

    private void stoneCutterRecipes(BuildingBlockHelper blockHelper, Consumer<IFinishedRecipe> consumer) {
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(blockHelper.getBlock().get()), blockHelper.getStairs().get()).addCriterion("has_" + blockHelper.getId(), hasItem(blockHelper.getBlock().get()))
                .build(consumer, blockHelper.getId() + "_stairs_from_" + blockHelper.getId() + "_stonecutting");
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(blockHelper.getBlock().get()), blockHelper.getSlab().get(), 2).addCriterion("has_" + blockHelper.getId(), hasItem(blockHelper.getBlock().get()))
                .build(consumer, blockHelper.getId() + "_slab_from_" + blockHelper.getId() + "_stonecutting");
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(blockHelper.getBlock().get()), blockHelper.getWall().get()).addCriterion("has_" + blockHelper.getId(), hasItem(blockHelper.getBlock().get()))
                .build(consumer, blockHelper.getId() + "_wall_from_" + blockHelper.getId() + "_stonecutting");
    }

}