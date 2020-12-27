package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.client.particles.DrippingSlimeParticle;
import mod.patrigan.slimierslimes.client.particles.FallingSlimeParticle;
import mod.patrigan.slimierslimes.client.particles.LandingSlimeParticle;
import mod.patrigan.slimierslimes.init.ModParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSideRegistry {

    @SubscribeEvent
    public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ModParticleTypes.DRIPPING_SLIME.get(), DrippingSlimeParticle.DrippingSlimeFactory::new);
        Minecraft.getInstance().particles.registerFactory(ModParticleTypes.FALLING_SLIME.get(), FallingSlimeParticle.FallingSlimeFactory::new);
        Minecraft.getInstance().particles.registerFactory(ModParticleTypes.LANDING_SLIME.get(), LandingSlimeParticle.LandingSlimeFactory::new);
    }

}
