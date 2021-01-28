package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.world.gen.feature.SlimeSpawnerFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SlimierSlimes.MOD_ID);

    public static final RegistryObject<Feature<NoFeatureConfig>> SLIME_SPAWNER_FEATURE = registerFeature("slime_spawner_feature", () -> new SlimeSpawnerFeature(NoFeatureConfig.field_236558_a_));

    public static <T extends Feature<?>> RegistryObject<T> registerFeature(String name, Supplier<T> feature) {
        return FEATURES.register(name, feature);
    }
}
