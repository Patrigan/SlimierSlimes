package mod.patrigan.slimierslimes.datagen;

import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.*;
import static mod.patrigan.slimierslimes.init.ModItems.*;
import static net.minecraft.item.Items.*;

public class ModEntityLootTables extends EntityLootTables {

    @Override
    protected void addTables() {
        this.registerLootTable(COMMON_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(GREEN_JELLY.get())));
        this.registerLootTable(PINK_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(PINK_JELLY.get())).addLootPool(getJellyLootPool(PINK_JELLY.get())));
        this.registerLootTable(DIAMOND_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(CYAN_JELLY.get())).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(DIAMOND).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
        this.registerLootTable(ROCK_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(BLUE_JELLY.get())).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(COBBLESTONE).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
        this.registerLootTable(CRYSTAL_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(MAGENTA_JELLY.get())).addLootPool(getJellyLootPool(LIGHT_BLUE_JELLY.get())));
        this.registerLootTable(GLOW_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(PURPLE_JELLY.get())).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(GLOWSTONE_DUST).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
        this.registerLootTable(CREEPER_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(GREEN_JELLY.get())).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(GUNPOWDER).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
        this.registerLootTable(SNOW_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(WHITE_JELLY.get())).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(SNOWBALL).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
    }

    @Override
    protected Iterable<EntityType<?>> getKnownEntities() {
        return ENTITY_TYPES.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    private LootPool.Builder getJellyLootPool(IItemProvider jelly){
        return LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(jelly).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))));
    }
}
