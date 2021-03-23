package mod.patrigan.slimierslimes.datagen;

import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.*;
import static mod.patrigan.slimierslimes.init.ModItems.JELLY;
import static net.minecraft.item.DyeColor.*;
import static net.minecraft.item.Items.*;

public class ModEntityLootTables extends EntityLootTables {

    @Override
    protected void addTables() {
        this.add(COMMON_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(LIME)));
        this.add(PINK_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(PINK)).withPool(getJellyLootPool(PINK)));
        this.add(CLOUD_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(LIGHT_GRAY)));
        this.add(DIAMOND_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(CYAN)).withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(DIAMOND).apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F))).apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
        this.add(ROCK_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(BLUE)).withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(COBBLESTONE).apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F))).apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
        this.add(CRYSTAL_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(MAGENTA)).withPool(getJellyLootPool(LIGHT_BLUE)));
        this.add(GLOW_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(PURPLE)).withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(GLOWSTONE_DUST).apply(SetCount.setCount(RandomValueRange.between(0.0F, 2.0F))).apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
        this.add(CREEPER_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(GREEN)).withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(GUNPOWDER).apply(SetCount.setCount(RandomValueRange.between(0.0F, 2.0F))).apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
        this.add(SNOW_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(WHITE)).withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(SNOWBALL).apply(SetCount.setCount(RandomValueRange.between(0.0F, 2.0F))).apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
        this.add(CAMO_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(GREEN)).withPool(getJellyLootPool(GREEN)));
        this.add(LAVA_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(RED)).withPool(getJellyLootPool(RED)));
        this.add(OBSIDIAN_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(BLACK)).withPool(getJellyLootPool(PURPLE)).withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(OBSIDIAN).apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F))).apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
    }

    @Override
    protected Iterable<EntityType<?>> getKnownEntities() {
        return ENTITY_TYPES.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    private LootPool.Builder getJellyLootPool(DyeColor dyeColor){
        return LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(JELLY.get(dyeColor).get()).apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F))).apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))));
    }
}
