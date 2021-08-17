package mod.patrigan.slimier_slimes.init;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SlimierSlimes.MOD_ID);

    public static final Map<DyeColor, RegistryObject<SimpleParticleType>> DRIPPING_SLIME = registerSlimeParticle("_dripping_slime");
    public static final Map<DyeColor, RegistryObject<SimpleParticleType>> FALLING_SLIME = registerSlimeParticle("_falling_slime");
    public static final Map<DyeColor, RegistryObject<SimpleParticleType>> LANDING_SLIME = registerSlimeParticle("_landing_slime");

    private static Map<DyeColor, RegistryObject<SimpleParticleType>> registerSlimeParticle(String baseId){
        return Arrays.stream(DyeColor.values()).collect(Collectors.toMap(dyeColor -> dyeColor, dyeColor -> PARTICLES.register(dyeColor + baseId, () -> new SimpleParticleType(false))));

    }
}
