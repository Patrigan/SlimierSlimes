package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SlimierSlimes.MOD_ID);

    public static final RegistryObject<BasicParticleType> DRIPPING_SLIME = PARTICLES.register("dripping_slime", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> FALLING_SLIME = PARTICLES.register("falling_slime", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> LANDING_SLIME = PARTICLES.register("landing_slime", () -> new BasicParticleType(false));
}
