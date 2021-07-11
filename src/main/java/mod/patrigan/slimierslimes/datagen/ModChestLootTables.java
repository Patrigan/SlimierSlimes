package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.Tags;

import java.util.function.BiConsumer;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;
import static net.minecraftforge.common.Tags.Items.SLIMEBALLS;

public class ModChestLootTables extends ChestLootTables {

    private static final ResourceLocation SLIME_LOOT_TABLE = new ResourceLocation(MOD_ID, "chests/abstract_slime_table");

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        createAbstractSlimeLootTable(consumer);
        consumer.accept(new ResourceLocation(MOD_ID, "chests/pillager_slime_lab"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(6.0F, 10.0F))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(45))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(15).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.BREAD).setWeight(25).apply(SetCount.setCount(RandomValueRange.between(1.0F, 4.0F))))
                                .add(ItemLootEntry.lootTableItem(Items.COAL).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F))))));
        consumer.accept(new ResourceLocation(MOD_ID, "processors/saplings"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(TagLootEntry.expandTag(ItemTags.SAPLINGS).setWeight(1))));
        createBooksLootTable(consumer);

    }

    private void createAbstractSlimeLootTable(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        ResourceLocation jellies = new ResourceLocation(MOD_ID, "chests/abstract_slime_table/jelly");
        ResourceLocation slimeballs = new ResourceLocation(MOD_ID, "chests/abstract_slime_table/slimeball");
        consumer.accept(jellies,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(TagLootEntry.expandTag(ModTags.Items.JELLIES).setWeight(1)
                        )));
        consumer.accept(slimeballs,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(TagLootEntry.expandTag(SLIMEBALLS).setWeight(1)
                                )));
        consumer.accept(SLIME_LOOT_TABLE,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(TableLootEntry.lootTableReference(jellies).setWeight(48).apply(SetCount.setCount(RandomValueRange.between(2.0F, 5.0F))))
                                .add(TableLootEntry.lootTableReference(slimeballs).setWeight(20).apply(SetCount.setCount(RandomValueRange.between(1.0F, 5.0F))))
                        ));
    }

    private void createBooksLootTable(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putString("author", "Patrigan");
        compoundNBT.putString("title", "TestSuccess");
        ListNBT listNBT = new ListNBT();
        listNBT.add(StringNBT.valueOf("This is a success"));
        compoundNBT.put("pages", listNBT);
        consumer.accept(new ResourceLocation(MOD_ID, "processors/books_test"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(ItemLootEntry.lootTableItem(Items.WRITTEN_BOOK).setWeight(1).apply(SetNBT.setTag(compoundNBT)))));
    }


}
