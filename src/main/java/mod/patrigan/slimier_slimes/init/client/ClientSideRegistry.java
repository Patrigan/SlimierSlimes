package mod.patrigan.slimier_slimes.init.client;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.client.particles.DripHangSlimeParticle;
import mod.patrigan.slimier_slimes.client.particles.FallAndLandSlimeParticle;
import mod.patrigan.slimier_slimes.client.particles.DripLandSlimeParticle;
import mod.patrigan.slimier_slimes.init.ModParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.DyeColor;
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
            Minecraft.getInstance().particleEngine.register(ModParticleTypes.DRIPPING_SLIME.get(dyeColor).get(), spriteSet -> new DripHangSlimeParticle.DrippingSlimeFactory(spriteSet, dyeColor));
            Minecraft.getInstance().particleEngine.register(ModParticleTypes.FALLING_SLIME.get(dyeColor).get(), spriteSet -> new FallAndLandSlimeParticle.FallingSlimeFactory(spriteSet, dyeColor));
            Minecraft.getInstance().particleEngine.register(ModParticleTypes.LANDING_SLIME.get(dyeColor).get(), spriteSet -> new DripLandSlimeParticle.LandingSlimeFactory(spriteSet, dyeColor));
        });
    }

}
