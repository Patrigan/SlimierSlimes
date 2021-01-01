package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimierslimes.init.ModBlocks;
import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModBlocks.*;

public class ModBlockLootTables extends BlockLootTables {

    @Override
    protected void addTables() {
        this.registerBuildingBlockLootTable(ModBlocks.SLIMY_STONE_BLOCK, slimyStoneBlock ->
                droppingWithSilkTouch(slimyStoneBlock, withSurvivesExplosion(slimyStoneBlock, ItemLootEntry.builder(ModItems.GREEN_JELLY.get()).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F)).alternatively(ItemLootEntry.builder(Blocks.COBBLESTONE)))));
        this.registerBuildingBlockLootTable(ModBlocks.SLIMY_COBBLESTONE_BLOCK, slimyStoneBlock ->
                droppingWithSilkTouch(slimyStoneBlock, withSurvivesExplosion(slimyStoneBlock, ItemLootEntry.builder(ModItems.GREEN_JELLY.get()).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F)).alternatively(ItemLootEntry.builder(Blocks.COBBLESTONE)))));
        this.registerLootTable(ModBlocks.AMETHYST_CLUSTER.get(), block -> blockNoDrop());
        this.registerLootTable(ModBlocks.SMALL_AMETHYST_BUD.get(), block -> blockNoDrop());
        this.registerLootTable(ModBlocks.MEDIUM_AMETHYST_BUD.get(), block -> blockNoDrop());
        this.registerLootTable(ModBlocks.LARGE_AMETHYST_BUD.get(), block -> blockNoDrop());
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