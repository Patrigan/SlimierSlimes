package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.item.DyeColor;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SlimierSlimes.MOD_ID);

    public static final Map<DyeColor, RegistryObject<BasicParticleType>> DRIPPING_SLIME = registerSlimeParticle("_dripping_slime");
    public static final Map<DyeColor, RegistryObject<BasicParticleType>> FALLING_SLIME = registerSlimeParticle("_falling_slime");
    public static final Map<DyeColor, RegistryObject<BasicParticleType>> LANDING_SLIME = registerSlimeParticle("_landing_slime");

    private static Map<DyeColor, RegistryObject<BasicParticleType>> registerSlimeParticle(String baseId){
        return Arrays.stream(DyeColor.values()).collect(Collectors.toMap(dyeColor -> dyeColor, dyeColor -> PARTICLES.register(dyeColor + baseId, () -> new BasicParticleType(false))));

    }
}
