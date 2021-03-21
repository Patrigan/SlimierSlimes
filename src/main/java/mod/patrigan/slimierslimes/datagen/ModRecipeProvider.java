package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.function.Consumer;

import static mod.patrigan.slimierslimes.datagen.DataGenerators.DYE_ITEMS;
import static mod.patrigan.slimierslimes.init.ModBlocks.BLOCK_HELPERS;
import static mod.patrigan.slimierslimes.init.ModBlocks.SLIME_BLOCK_HELPERS;
import static mod.patrigan.slimierslimes.init.ModItems.JELLY;
import static mod.patrigan.slimierslimes.init.ModItems.SLIME_BALL;
import static net.minecraftforge.common.Tags.Items.SLIMEBALLS;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        DYE_ITEMS.forEach(dye -> ShapelessRecipeBuilder.shapelessRecipe(dye)
                .addIngredient(JELLY.get(((DyeItem) dye).getDyeColor()).get())
                .addCriterion("has_jelly", hasItem(ModTags.Items.JELLIES))
                .build(consumer));
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> dyeColorRecipes(dyeColor, consumer));
        cleanSlimeBallRecipes(consumer);
        BLOCK_HELPERS.forEach(buildingBlockHelper -> buildingBlockRecipes(buildingBlockHelper, consumer));
    }

    private void cleanSlimeBallRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(Items.LEAD, 2).key('~', Items.STRING).key('O', SLIMEBALLS).patternLine("~~ ").patternLine("~O ").patternLine("  ~").addCriterion("has_slime_ball", hasItem(SLIMEBALLS)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(Items.MAGMA_CREAM).addIngredient(Items.BLAZE_POWDER).addIngredient(SLIMEBALLS).addCriterion("has_blaze_powder", hasItem(Items.BLAZE_POWDER)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Blocks.STICKY_PISTON).key('P', Blocks.PISTON).key('S', SLIMEBALLS).patternLine("S").patternLine("P").addCriterion("has_slime_ball", hasItem(SLIMEBALLS)).build(consumer);
    }

    private void dyeColorRecipes(DyeColor dyeColor, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(SLIME_BALL.get(dyeColor).get(), 2).key('#', JELLY.get(dyeColor).get())
                .patternLine("###")
                .patternLine("###")
                .patternLine("###")
                .addCriterion("has_jelly", hasItem(ModTags.Items.JELLIES))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(SLIME_BLOCK_HELPERS.get(dyeColor).getBlock().get(), 1).key('#', SLIME_BALL.get(dyeColor).get())
                .patternLine("###")
                .patternLine("###")
                .patternLine("###")
                .addCriterion("has_slime_ball", hasItem(SLIMEBALLS))
                .build(consumer);
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