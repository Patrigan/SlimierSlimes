package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.client.particles.DrippingSlimeParticle;
import mod.patrigan.slimierslimes.client.particles.FallingSlimeParticle;
import mod.patrigan.slimierslimes.client.particles.LandingSlimeParticle;
import mod.patrigan.slimierslimes.init.ModParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSideRegistry {

    @SubscribeEvent
    public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> {
            Minecraft.getInstance().particles.registerFactory(ModParticleTypes.DRIPPING_SLIME.get(dyeColor).get(), spriteSet -> new DrippingSlimeParticle.DrippingSlimeFactory(spriteSet, dyeColor));
            Minecraft.getInstance().particles.registerFactory(ModParticleTypes.FALLING_SLIME.get(dyeColor).get(), spriteSet -> new FallingSlimeParticle.FallingSlimeFactory(spriteSet, dyeColor));
            Minecraft.getInstance().particles.registerFactory(ModParticleTypes.LANDING_SLIME.get(dyeColor).get(), spriteSet -> new LandingSlimeParticle.LandingSlimeFactory(spriteSet, dyeColor));
        });
    }

}
