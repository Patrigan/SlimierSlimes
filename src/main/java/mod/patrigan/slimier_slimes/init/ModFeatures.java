package mod.patrigan.slimier_slimes.init;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.world.gen.feature.LavaSpawnerConfig;
import mod.patrigan.slimier_slimes.world.gen.feature.LavaSpawnerFeature;
import mod.patrigan.slimier_slimes.world.gen.feature.SewerShaftFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SlimierSlimes.MOD_ID);

    public static final RegistryObject<LavaSpawnerFeature> LAVA_SLIME_SPAWNER_FEATURE = registerFeature("lava_slime_spawner_feature", () -> new LavaSpawnerFeature(LavaSpawnerConfig.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SEWER_SHAFT_FEATURE = registerFeature("sewer_shaft", () -> new SewerShaftFeature(NoneFeatureConfiguration.CODEC));

    public static <T extends Feature<?>> RegistryObject<T> registerFeature(String name, Supplier<T> feature) {
        return FEATURES.register(name, feature);
    }
}
