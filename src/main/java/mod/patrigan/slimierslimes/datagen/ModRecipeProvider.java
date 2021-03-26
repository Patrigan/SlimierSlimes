package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.function.Consumer;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;
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

    private ResourceLocation modRecipe(String id){
        return new ResourceLocation(MOD_ID, id);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        DYE_ITEMS.forEach(dye -> ShapelessRecipeBuilder.shapeless(dye)
                .requires(JELLY.get(((DyeItem) dye).getDyeColor()).get())
                .unlockedBy("has_jelly", has(ModTags.Items.JELLIES))
                .save(consumer, modRecipe(dye.getRegistryName().getPath())));
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> dyeColorRecipes(dyeColor, consumer));
        cleanSlimeBallRecipes(consumer);
        BLOCK_HELPERS.forEach(buildingBlockHelper -> buildingBlockRecipes(buildingBlockHelper, consumer));
    }

    private void cleanSlimeBallRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(Items.LEAD, 2).define('~', Items.STRING).define('O', SLIMEBALLS).pattern("~~ ").pattern("~O ").pattern("  ~").unlockedBy("has_slime_ball", has(SLIMEBALLS)).save(consumer);
        ShapelessRecipeBuilder.shapeless(Items.MAGMA_CREAM).requires(Items.BLAZE_POWDER).requires(SLIMEBALLS).unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER)).save(consumer);
        ShapedRecipeBuilder.shaped(Blocks.STICKY_PISTON).define('P', Blocks.PISTON).define('S', SLIMEBALLS).pattern("S").pattern("P").unlockedBy("has_slime_ball", has(SLIMEBALLS)).save(consumer);
    }

    private void dyeColorRecipes(DyeColor dyeColor, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(SLIME_BALL.get(dyeColor).get(), 2).define('#', JELLY.get(dyeColor).get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_jelly", has(ModTags.Items.JELLIES))
                .save(consumer);
        ShapedRecipeBuilder.shaped(SLIME_BLOCK_HELPERS.get(dyeColor).getBlock().get(), 1).define('#', SLIME_BALL.get(dyeColor).get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_slime_ball", has(SLIMEBALLS))
                .save(consumer);
    }

    private void buildingBlockRecipes(BuildingBlockHelper blockHelper, Consumer<IFinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(blockHelper.getSlab().get(), 6).define('#', blockHelper.getBlock().get())
                .pattern("###")
                .unlockedBy("has_" + blockHelper.getId(), has(blockHelper.getBlock().get())).save(consumer);
        ShapedRecipeBuilder.shaped(blockHelper.getStairs().get(), 4).define('#', blockHelper.getBlock().get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy("has_" +  blockHelper.getId(), has(blockHelper.getBlock().get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(blockHelper.getButton().get()).requires(blockHelper.getBlock().get())
                .unlockedBy("has_" +  blockHelper.getId(), has(blockHelper.getBlock().get())).save(consumer);
        ShapedRecipeBuilder.shaped(blockHelper.getPressurePlate().get())
                .define('#', blockHelper.getBlock().get())
                .pattern("##")
                .unlockedBy("has_" +  blockHelper.getId(), has(blockHelper.getBlock().get())).save(consumer);
        ShapedRecipeBuilder.shaped(blockHelper.getWall().get(), 6).define('#', blockHelper.getBlock().get())
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_" + blockHelper.getId(), has(blockHelper.getBlock().get())).save(consumer);
        stoneCutterRecipes(blockHelper, consumer);
    }

    private void stoneCutterRecipes(BuildingBlockHelper blockHelper, Consumer<IFinishedRecipe> consumer) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(blockHelper.getBlock().get()), blockHelper.getStairs().get()).unlocks("has_" + blockHelper.getId(), has(blockHelper.getBlock().get()))
                .save(consumer, modRecipe(blockHelper.getId() + "_stairs_from_" + blockHelper.getId() + "_stonecutting"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(blockHelper.getBlock().get()), blockHelper.getSlab().get(), 2).unlocks("has_" + blockHelper.getId(), has(blockHelper.getBlock().get()))
                .save(consumer, modRecipe(blockHelper.getId() + "_slab_from_" + blockHelper.getId() + "_stonecutting"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(blockHelper.getBlock().get()), blockHelper.getWall().get()).unlocks("has_" + blockHelper.getId(), has(blockHelper.getBlock().get()))
                .save(consumer, modRecipe(blockHelper.getId() + "_wall_from_" + blockHelper.getId() + "_stonecutting"));
    }

}