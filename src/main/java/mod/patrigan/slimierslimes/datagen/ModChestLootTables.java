package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;

public class ModChestLootTables extends ChestLootTables {

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> p_accept_1_) {
        p_accept_1_.accept(new ResourceLocation(MOD_ID, "chests/pillager_slime_lab"), LootTable.builder().addLootPool(LootPool.builder().rolls(RandomValueRange.of(6.0F, 10.0F)).addEntry(TagLootEntry.getBuilder(ModTags.Items.JELLY).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4.0F)))).addEntry(ItemLootEntry.builder(Items.BREAD).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4.0F))))));
    }
}
