package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.entities.*;
import mod.patrigan.slimierslimes.entities.projectile.AmethystProjectileEntity;
import mod.patrigan.slimierslimes.entities.projectile.SlimeBallEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SlimierSlimes.MOD_ID);
    public static final DeferredRegister<Item> SPAWN_EGGS = DeferredRegister.create(ForgeRegistries.ITEMS, SlimierSlimes.MOD_ID);
    public static final List<EntityType<?>> SLIMES = new ArrayList<>();
    public static final List<String> ENTITY_IDS = new ArrayList<>();
    public static final List<String> PROJECTILE_ENTITY_IDS = new ArrayList<>();
    public static final List<EntityType<?>> SPAWNER_ENTITY_TYPES = new ArrayList<>();

    // Slimes
    public static final RegistryObject<EntityType<AbstractSlimeEntity>> COMMON_SLIME = getSlimeRegistryObject("common_slime", AbstractSlimeEntity::new, 0x7EBF6E, true);
    public static final RegistryObject<EntityType<AbstractSlimeEntity>> PINK_SLIME = getSlimeRegistryObject("pink_slime", AbstractSlimeEntity::new, DyeColor.PINK.getColorValue(), false);
    public static final RegistryObject<EntityType<DiamondSlimeEntity>> DIAMOND_SLIME = getSlimeRegistryObject("diamond_slime", DiamondSlimeEntity::new, DyeColor.CYAN.getColorValue(), false);
    public static final RegistryObject<EntityType<AbstractSlimeEntity>> ROCK_SLIME = getSlimeRegistryObject("rock_slime", AbstractSlimeEntity::new, DyeColor.BLUE.getColorValue(), true);
    public static final RegistryObject<EntityType<AbstractSlimeEntity>> CLOUD_SLIME = getSlimeRegistryObject("cloud_slime", AbstractSlimeEntity::new, DyeColor.LIGHT_GRAY.getColorValue(), true);
    public static final RegistryObject<EntityType<CrystalSlimeEntity>> CRYSTAL_SLIME = getSlimeRegistryObject("crystal_slime", CrystalSlimeEntity::new, DyeColor.MAGENTA.getColorValue(), true);
    public static final RegistryObject<EntityType<GlowSlimeEntity>> GLOW_SLIME = getSlimeRegistryObject("glow_slime", GlowSlimeEntity::new, DyeColor.PURPLE.getColorValue(), true);
    public static final RegistryObject<EntityType<GooSlimeEntity>> BROWN_GOO_SLIME = getSlimeRegistryObject("brown_goo_slime", GooSlimeEntity::new, DyeColor.BROWN.getColorValue(), true);
    public static final RegistryObject<EntityType<CreeperSlimeEntity>> CREEPER_SLIME = getSlimeRegistryObject("creeper_slime", CreeperSlimeEntity::new, DyeColor.GREEN.getColorValue(), true);
    public static final RegistryObject<EntityType<SnowSlimeEntity>> SNOW_SLIME = getSlimeRegistryObject("snow_slime", SnowSlimeEntity::new, DyeColor.WHITE.getColorValue(), true);
    public static final RegistryObject<EntityType<CamoSlimeEntity>> CAMO_SLIME = getSlimeRegistryObject("camo_slime", CamoSlimeEntity::new, DyeColor.BROWN.getColorValue(), true);
    public static final RegistryObject<EntityType<LavaSlimeEntity>> LAVA_SLIME = getFireResistantSlimeRegistryObject("lava_slime", LavaSlimeEntity::new, DyeColor.RED.getColorValue(), false);
    public static final RegistryObject<EntityType<AbstractSlimeEntity>> OBSIDIAN_SLIME = getFireResistantSlimeRegistryObject("obsidian_slime", AbstractSlimeEntity::new, DyeColor.BLACK.getColorValue(), false);
    public static final RegistryObject<EntityType<ShroomSlimeEntity>> SHROOM_SLIME = getSlimeRegistryObject("shroom_slime", ShroomSlimeEntity::new, DyeColor.RED.getColorValue(), true);
    //Projectiles
    public static final RegistryObject<EntityType<AmethystProjectileEntity>> AMETYST_PROJECTILE = getAmethystProjectileRegistryObject("amethyst_projectile", AmethystProjectileEntity::new);
    public static final Map<DyeColor, RegistryObject<EntityType<SlimeBallEntity>>> SLIME_BALL_PROJECTILE = getSlimeballProjectileRegistryObjects("slimeball_projectile", SlimeBallEntity::new);
    //Other


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

    private static <T extends Entity> Map<DyeColor, RegistryObject<EntityType<T>>> getSlimeballProjectileRegistryObjects(String key, EntityType.IFactory<T> sup) {
        return Arrays.stream(DyeColor.values()).collect(Collectors.toMap(dyeColor -> dyeColor, dyeColor -> getSlimeballProjectileRegistryObject(key, dyeColor, sup)));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> getSlimeballProjectileRegistryObject(String key, DyeColor dyeColor, final EntityType.IFactory<T> sup) {
        String id = dyeColor.getName() + "_" + key;
        PROJECTILE_ENTITY_IDS.add(id);
        return ENTITY_TYPES.register(id,
                () -> EntityType.Builder.of(sup, EntityClassification.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                        .build(new ResourceLocation(SlimierSlimes.MOD_ID, id).toString()));
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.COMMON_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.SNOW_SLIME.get(), SnowSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.PINK_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.CLOUD_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.ROCK_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.CRYSTAL_SLIME.get(), CrystalSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.GLOW_SLIME.get(), GlowSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.CREEPER_SLIME.get(), CreeperSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.CAMO_SLIME.get(), CamoSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.DIAMOND_SLIME.get(), DiamondSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.LAVA_SLIME.get(), LavaSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.OBSIDIAN_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.BROWN_GOO_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
        event.put(ModEntityTypes.SHROOM_SLIME.get(), AbstractSlimeEntity.getMutableAttributes().build());
    }

}