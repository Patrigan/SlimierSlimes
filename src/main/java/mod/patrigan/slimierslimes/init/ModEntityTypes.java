package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.entities.*;
import mod.patrigan.slimierslimes.entities.projectile.AmethystProjectileEntity;
import mod.patrigan.slimierslimes.entities.projectile.SlimeballProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SlimierSlimes.MOD_ID);
    public static final DeferredRegister<Item> SPAWN_EGGS = DeferredRegister.create(ForgeRegistries.ITEMS, SlimierSlimes.MOD_ID);
    public static final List<String> ENTITY_IDS = new ArrayList<>();
    public static final List<String> PROJECTILE_ENTITY_IDS = new ArrayList<>();

    // Entity Types
    public static final RegistryObject<EntityType<CommonSlimeEntity>> COMMON_SLIME = getSlimeRegistryObject("common_slime", CommonSlimeEntity::new);
    public static final RegistryObject<EntityType<PinkSlimeEntity>> PINK_SLIME = getSlimeRegistryObject("pink_slime", PinkSlimeEntity::new);
    public static final RegistryObject<EntityType<DiamondSlimeEntity>> DIAMOND_SLIME = getSlimeRegistryObject("diamond_slime", DiamondSlimeEntity::new);
    public static final RegistryObject<EntityType<RockSlimeEntity>> ROCK_SLIME = getSlimeRegistryObject("rock_slime", RockSlimeEntity::new);
    public static final RegistryObject<EntityType<CrystalSlimeEntity>> CRYSTAL_SLIME = getSlimeRegistryObject("crystal_slime", CrystalSlimeEntity::new);
    public static final RegistryObject<EntityType<GlowSlimeEntity>> GLOW_SLIME = getSlimeRegistryObject("glow_slime", GlowSlimeEntity::new);
    public static final RegistryObject<EntityType<CreeperSlimeEntity>> CREEPER_SLIME = getSlimeRegistryObject("creeper_slime", CreeperSlimeEntity::new);
    public static final RegistryObject<EntityType<SnowSlimeEntity>> SNOW_SLIME = getSlimeRegistryObject("snow_slime", SnowSlimeEntity::new);
    public static final RegistryObject<EntityType<CamoSlimeEntity>> CAMO_SLIME = getSlimeRegistryObject("camo_slime", CamoSlimeEntity::new);
    public static final RegistryObject<EntityType<LavaSlimeEntity>> LAVA_SLIME = getFireResistantSlimeRegistryObject("lava_slime", LavaSlimeEntity::new);
    //Projectiles
    public static final RegistryObject<EntityType<AmethystProjectileEntity>> AMETYST_PROJECTILE = getAmethystProjectileRegistryObject("amethyst_projectile", AmethystProjectileEntity::new);
    public static final RegistryObject<EntityType<SlimeballProjectileEntity>> SLIMEBALL_PROJECTILE = getSlimeballProjectileRegistryObject("slimeball_projectile", SlimeballProjectileEntity::new);

    private static <T extends AbstractSlimeEntity> RegistryObject<EntityType<T>> getSlimeRegistryObject(String key, final EntityType.IFactory<T> sup) {
        ENTITY_IDS.add(key);

        EntityType<T> entityType = EntityType.Builder.create(sup, EntityClassification.MONSTER)
                .size(2.04f, 2.04f) // Hitbox Size
                .build(new ResourceLocation(SlimierSlimes.MOD_ID, key).toString());

        SPAWN_EGGS.register(key + "_spawn_egg" , () -> new SpawnEggItem(entityType, 0xFFCC00, 0xFCA800, new Item.Properties().group(SlimierSlimes.TAB)));

        return ENTITY_TYPES.register(key, () -> entityType);
    }

    private static <T extends AbstractSlimeEntity> RegistryObject<EntityType<T>> getFireResistantSlimeRegistryObject(String key, final EntityType.IFactory<T> sup) {
        ENTITY_IDS.add(key);

        EntityType<T> entityType = EntityType.Builder.create(sup, EntityClassification.MONSTER)
                .immuneToFire()
                .size(2.04f, 2.04f) // Hitbox Size
                .build(new ResourceLocation(SlimierSlimes.MOD_ID, key).toString());

        SPAWN_EGGS.register(key + "_spawn_egg" , () -> new SpawnEggItem(entityType, 0xFFCC00, 0xFCA800, new Item.Properties().group(SlimierSlimes.TAB)));

        return ENTITY_TYPES.register(key, () -> entityType);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> getAmethystProjectileRegistryObject(String key, final EntityType.IFactory<T> sup) {
        PROJECTILE_ENTITY_IDS.add(key);
        return ENTITY_TYPES.register(key,
                () -> EntityType.Builder.create(sup, EntityClassification.MISC)
                        .size(0.6f, 0.6f)
                        .setUpdateInterval(20)
                        .setTrackingRange(120)
                        .build(new ResourceLocation(SlimierSlimes.MOD_ID, key).toString()));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> getSlimeballProjectileRegistryObject(String key, final EntityType.IFactory<T> sup) {
        PROJECTILE_ENTITY_IDS.add(key);
        return ENTITY_TYPES.register(key,
                () -> EntityType.Builder.create(sup, EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).func_233608_b_(10)
                        .build(new ResourceLocation(SlimierSlimes.MOD_ID, key).toString()));
    }

    public static void registerAdditionalEntityInformation() {
        registerEntityAttributes();
    }

    private static void registerEntityAttributes() {
        GlobalEntityTypeAttributes.put(ModEntityTypes.COMMON_SLIME.get(), CommonSlimeEntity.getMutableAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.SNOW_SLIME.get(), SnowSlimeEntity.getMutableAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.PINK_SLIME.get(), PinkSlimeEntity.getMutableAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.ROCK_SLIME.get(), RockSlimeEntity.getMutableAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.CRYSTAL_SLIME.get(), CrystalSlimeEntity.getMutableAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.GLOW_SLIME.get(), GlowSlimeEntity.getMutableAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.CREEPER_SLIME.get(), CreeperSlimeEntity.getMutableAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.CAMO_SLIME.get(), CamoSlimeEntity.getMutableAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.DIAMOND_SLIME.get(), DiamondSlimeEntity.getMutableAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.LAVA_SLIME.get(), DiamondSlimeEntity.getMutableAttributes().create());
    }

}