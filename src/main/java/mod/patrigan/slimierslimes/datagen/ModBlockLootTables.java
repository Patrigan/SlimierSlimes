package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.init.ModBlocks;
import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModBlocks.BLOCKS;
import static mod.patrigan.slimierslimes.init.ModBlocks.LIGHT_AIR;

public class ModBlockLootTables extends BlockLootTables {

    @Override
    protected void addTables() {
        this.registerLootTable(ModBlocks.SLIMY_STONE_BLOCK.get(), slimyStoneBlock ->
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
}
