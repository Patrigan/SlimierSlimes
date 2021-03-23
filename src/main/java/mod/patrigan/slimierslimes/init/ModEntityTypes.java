package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.entities.*;
import mod.patrigan.slimierslimes.entities.projectile.AmethystProjectileEntity;
import mod.patrigan.slimierslimes.entities.projectile.SlimeballProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.DyeColor;
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
    public static final List<EntityType<?>> SLIMES = new ArrayList<>();
    public static final List<String> ENTITY_IDS = new ArrayList<>();
    public static final List<String> PROJECTILE_ENTITY_IDS = new ArrayList<>();
    public static final List<EntityType<?>> SPAWNER_ENTITY_TYPES = new ArrayList<>();

    // Entity Types
    public static final RegistryObject<EntityType<AbstractSlimeEntity>> COMMON_SLIME = getSlimeRegistryObject("common_slime", AbstractSlimeEntity::new, 0x7EBF6E, true);
    public static final RegistryObject<EntityType<AbstractSlimeEntity>> PINK_SLIME = getSlimeRegistryObject("pink_slime", AbstractSlimeEntity::new, DyeColor.PINK.getColorValue(), false);
    public static final RegistryObject<EntityType<DiamondSlimeEntity>> DIAMOND_SLIME = getSlimeRegistryObject("diamond_slime", DiamondSlimeEntity::new, DyeColor.CYAN.getColorValue(), false);
    public static final RegistryObject<EntityType<AbstractSlimeEntity>> ROCK_SLIME = getSlimeRegistryObject("rock_slime", AbstractSlimeEntity::new, DyeColor.BLUE.getColorValue(), true);
    public static final RegistryObject<EntityType<AbstractSlimeEntity>> CLOUD_SLIME = getSlimeRegistryObject("cloud_slime", AbstractSlimeEntity::new, DyeColor.LIGHT_GRAY.getColorValue(), true);
    public static final RegistryObject<EntityType<CrystalSlimeEntity>> CRYSTAL_SLIME = getSlimeRegistryObject("crystal_slime", CrystalSlimeEntity::new, DyeColor.MAGENTA.getColorValue(), true);
    public static final RegistryObject<EntityType<GlowSlimeEntity>> GLOW_SLIME = getSlimeRegistryObject("glow_slime", GlowSlimeEntity::new, DyeColor.PURPLE.getColorValue(), true);
    public static final RegistryObject<EntityType<CreeperSlimeEntity>> CREEPER_SLIME = getSlimeRegistryObject("creeper_slime", CreeperSlimeEntity::new, DyeColor.GREEN.getColorValue(), true);
    public static final RegistryObject<EntityType<SnowSlimeEntity>> SNOW_SLIME = getSlimeRegistryObject("snow_slime", SnowSlimeEntity::new, DyeColor.WHITE.getColorValue(), true);
    public static final RegistryObject<EntityType<CamoSlimeEntity>> CAMO_SLIME = getSlimeRegistryObject("camo_slime", CamoSlimeEntity::new, DyeColor.BROWN.getColorValue(), true);
    public static final RegistryObject<EntityType<LavaSlimeEntity>> LAVA_SLIME = getFireResistantSlimeRegistryObject("lava_slime", LavaSlimeEntity::new, DyeColor.RED.getColorValue(), false);
    public static final RegistryObject<EntityType<AbstractSlimeEntity>> OBSIDIAN_SLIME = getFireResistantSlimeRegistryObject("obsidian_slime", AbstractSlimeEntity::new, DyeColor.BLACK.getColorValue(), false);
    //Projectiles
    public static final RegistryObject<EntityType<AmethystProjectileEntity>> AMETYST_PROJECTILE = getAmethystProjectileRegistryObject("amethyst_projectile", AmethystProjectileEntity::new);
    public static final RegistryObject<EntityType<SlimeballProjectileEntity>> SLIMEBALL_PROJECTILE = getSlimeballProjectileRegistryObject("slimeball_projectile", SlimeballProjectileEntity::new);

    private static <T extends AbstractSlimeEntity> RegistryObject<EntityType<T>> getSlimeRegistryObject(String key, final EntityType.IFactory<T> sup, int secondaryColor, boolean isSpawnerEntity) {
        ENTITY_IDS.add(key);

        EntityType<T> entityType = EntityType.Builder.of(sup, EntityClassification.MONSTER)
                .sized(2.04f, 2.04f) // Hitbox Size
                .build(new ResourceLocation(SlimierSlimes.MOD_ID, key).toString());

        SPAWN_EGGS.register(key + "_spawn_egg" , () -> new SpawnEggItem(entityType, 5349438, secondaryColor, new Item.Properties().tab(SlimierSlimes.TAB)));
        addToSpawnerEntities(entityType, isSpawnerEntity);
        SLIMES.add(entityType);

        return ENTITY_TYPES.register(key, () -> entityType);
    }

    private static <T extends AbstractSlimeEntity> RegistryObject<EntityType<T>> getFireResistantSlimeRegistryObject(String key, final EntityType.IFactory<T> sup, int secondaryColor, boolean isSpawnerEntity) {
        ENTITY_IDS.add(key);

        EntityType<T> entityType = EntityType.Builder.of(sup, EntityClassification.MONSTER)
                .fireImmune()
                .sized(2.04f, 2.04f) // Hitbox Size
                .build(new ResourceLocation(SlimierSlimes.MOD_ID, key).toString());

        SPAWN_EGGS.register(key + "_spawn_egg" , () -> new SpawnEggItem(entityType, 5349438, secondaryColor, new Item.Properties().tab(SlimierSlimes.TAB)));
        addToSpawnerEntities(entityType, isSpawnerEntity);
        SLIMES.add(entityType);

        return ENTITY_TYPES.register(key, () -> entityType);
    }

    private static void addToSpawnerEntities(EntityType<?> entityType, Boolean isSpawnerEntity){
        if(isSpawnerEntity){
            SPAWNER_ENTITY_TYPES.add(entityType);
        }
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> getAmethystProjectileRegistryObject(String key, final EntityType.IFactory<T> sup) {
        PROJECTILE_ENTITY_IDS.add(key);
        return ENTITY_TYPES.register(key,
                () -> EntityType.Builder.of(sup, EntityClassification.MISC)
                        .sized(0.6f, 0.6f)
                        .setUpdateInterval(20)
                        .setTrackingRange(120)
                        .build(new ResourceLocation(SlimierSlimes.MOD_ID, key).toString()));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> getSlimeballProjectileRegistryObject(String key, final EntityType.IFactory<T> sup) {
        PROJECTILE_ENTITY_IDS.add(key);
        return ENTITY_TYPES.register(key,
                () -> EntityType.Builder.of(sup, EntityClassification.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                        .build(new ResourceLocation(SlimierSlimes.MOD_ID, key).toString()));
    }

    public static void registerAdditionalEntityInformation() {
        registerEntityAttributes();
    }

    private static void registerEntityAttributes() {
        GlobalEntityTypeAttributes.put(ModEntityTypes.COMMON_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.SNOW_SLIME.get(), SnowSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.PINK_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.CLOUD_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.ROCK_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.CRYSTAL_SLIME.get(), CrystalSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.GLOW_SLIME.get(), GlowSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.CREEPER_SLIME.get(), CreeperSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.CAMO_SLIME.get(), CamoSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.DIAMOND_SLIME.get(), DiamondSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.LAVA_SLIME.get(), LavaSlimeEntity.getMutableAttributes().build());
        GlobalEntityTypeAttributes.put(ModEntityTypes.OBSIDIAN_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
    }

}