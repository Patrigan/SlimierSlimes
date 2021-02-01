package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimierslimes.init.ModBlocks;
import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.DyeColor;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraftforge.fml.RegistryObject;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModBlocks.*;

public class ModBlockLootTables extends BlockLootTables {

    @Override
    protected void addTables() {
        Arrays.stream(DyeColor.values()).forEach(this::registerBuildingBlocks);
        this.registerLootTable(ModBlocks.AMETHYST_CLUSTER.get(), block -> blockNoDrop());
        this.registerLootTable(ModBlocks.SMALL_AMETHYST_BUD.get(), block -> blockNoDrop());
        this.registerLootTable(ModBlocks.MEDIUM_AMETHYST_BUD.get(), block -> blockNoDrop());
        this.registerLootTable(ModBlocks.LARGE_AMETHYST_BUD.get(), block -> blockNoDrop());
        this.registerLootTable(STONE_LAVA_SLIME_SPAWNER.get(), block -> droppingWithSilkTouch(SLIMY_STONE_BLOCK.get(DyeColor.RED).getBlock().get(), withSurvivesExplosion(SLIMY_STONE_BLOCK.get(DyeColor.RED).getBlock().get(), ItemLootEntry.builder(ModItems.JELLY.get(DyeColor.RED).get()).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F)).alternatively(ItemLootEntry.builder(Blocks.COBBLESTONE)))));
        this.registerLootTable(NETHERRACK_LAVA_SLIME_SPAWNER.get(), block -> droppingWithSilkTouch(SLIMY_NETHERRACK_BLOCK.get(DyeColor.RED).getBlock().get(), withSurvivesExplosion(SLIMY_NETHERRACK_BLOCK.get(DyeColor.RED).getBlock().get(), ItemLootEntry.builder(ModItems.JELLY.get(DyeColor.RED).get()).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F)).alternatively(ItemLootEntry.builder(Blocks.NETHERRACK)))));
    }

    private void registerBuildingBlocks(DyeColor dyeColor){
        this.registerBuildingBlockLootTable(ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(dyeColor), slimyStoneBlock ->
                droppingWithSilkTouch(slimyStoneBlock, withSurvivesExplosion(slimyStoneBlock, ItemLootEntry.builder(ModItems.JELLY.get(dyeColor).get()).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F)).alternatively(ItemLootEntry.builder(Blocks.COBBLESTONE)))));
        this.registerBuildingBlockLootTable(ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor), slimyStoneBlock ->
                droppingWithSilkTouch(slimyStoneBlock, withSurvivesExplosion(slimyStoneBlock, ItemLootEntry.builder(ModItems.JELLY.get(dyeColor).get()).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F)).alternatively(ItemLootEntry.builder(Blocks.COBBLESTONE)))));
        this.registerBuildingBlockLootTable(ModBlocks.SLIMY_NETHERRACK_BLOCK.get(dyeColor), slimyStoneBlock ->
                droppingWithSilkTouch(slimyStoneBlock, withSurvivesExplosion(slimyStoneBlock, ItemLootEntry.builder(ModItems.JELLY.get(dyeColor).get()).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F)).alternatively(ItemLootEntry.builder(Blocks.NETHERRACK)))));
        this.registerBuildingBlockLootTable(LIME_SLIME_BLOCK, block ->
                dropping(block));

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BLOCKS.getEntries().stream().filter(block -> !block.equals(LIGHT_AIR)).map(RegistryObject::get).collect(Collectors.toList());
    }

    protected void registerBuildingBlockLootTable(BuildingBlockHelper blockHelper, Function<Block, LootTable.Builder> baseFactory) {
        this.registerLootTable(blockHelper.getBlock().get(), baseFactory);
        this.registerLootTable(blockHelper.getSlab().get(), droppingSlab(blockHelper.getSlab().get()));
        this.registerLootTable(blockHelper.getStairs().get(), dropping(blockHelper.getStairs().get()));
        this.registerLootTable(blockHelper.getButton().get(), dropping(blockHelper.getButton().get()));
        this.registerLootTable(blockHelper.getPressurePlate().get(), dropping(blockHelper.getPressurePlate().get()));
        this.registerLootTable(blockHelper.getWall().get(), dropping(blockHelper.getWall().get()));
    }
}