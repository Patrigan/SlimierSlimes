package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.world.gen.feature.LavaSpawnerConfig;
import mod.patrigan.slimierslimes.world.gen.feature.LavaSpawnerFeature;
import mod.patrigan.slimierslimes.world.gen.feature.SewerShaftFeature;
import mod.patrigan.slimierslimes.world.gen.feature.SlimeSpawnerFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SlimierSlimes.MOD_ID);

    public static final RegistryObject<Feature<NoFeatureConfig>> SLIME_SPAWNER_FEATURE = registerFeature("slime_spawner_feature", () -> new SlimeSpawnerFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<LavaSpawnerFeature> LAVA_SLIME_SPAWNER_FEATURE = registerFeature("lava_slime_spawner_feature", () -> new LavaSpawnerFeature(LavaSpawnerConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> SEWER_SHAFT_FEATURE = registerFeature("sewer_shaft", () -> new SewerShaftFeature(NoFeatureConfig.CODEC));

    public static <T extends Feature<?>> RegistryObject<T> registerFeature(String name, Supplier<T> feature) {
        return FEATURES.register(name, feature);
    }
}
