package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.entities.*;
import mod.patrigan.slimierslimes.entities.projectile.AmethystProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SlimierSlimes.MOD_ID);
    public static final List<String> ENTITY_IDS = new ArrayList<>();

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
    public static final RegistryObject<EntityType<AmethystProjectileEntity>> AMETYST_PROJECTILE = getMiscRegistryObject("amethyst_projectile", AmethystProjectileEntity::new);

    private static <T extends AbstractSlimeEntity> RegistryObject<EntityType<T>> getSlimeRegistryObject(String key, final EntityType.IFactory<T> sup) {
        ENTITY_IDS.add(key);
        return ENTITY_TYPES.register(key,
                () -> EntityType.Builder.create(sup, EntityClassification.MONSTER)
                        .size(2.04f, 2.04f) // Hitbox Size
                        .build(new ResourceLocation(SlimierSlimes.MOD_ID, key).toString()));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> getMiscRegistryObject(String key, final EntityType.IFactory<T> sup) {
        ENTITY_IDS.add(key);
        return ENTITY_TYPES.register(key,
                () -> EntityType.Builder.create(sup, EntityClassification.MISC)
                        .size(0.05f, 0.08f)
                        .func_233608_b_(20) // updateInterval
                        .trackingRange(120) // trackingRange
                        .build(new ResourceLocation(SlimierSlimes.MOD_ID, key).toString()));
    }
}