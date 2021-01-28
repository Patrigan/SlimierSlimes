package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;
import static net.minecraftforge.common.Tags.Items.SLIMEBALLS;

public class ModChestLootTables extends ChestLootTables {

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(new ResourceLocation(MOD_ID, "chests/pillager_slime_lab"),
                LootTable.builder().
                        addLootPool(LootPool.builder().rolls(RandomValueRange.of(5.0F, 8.0F))
                                .addEntry(TagLootEntry.getBuilder(ModTags.Items.JELLY).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 5.0F))))
                                .addEntry(TagLootEntry.getBuilder(SLIMEBALLS).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 5.0F))))
                                .addEntry(ItemLootEntry.builder(Items.BREAD).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4.0F))))
                                .addEntry(ItemLootEntry.builder(Items.COAL).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 3.0F))))));
    }
}
