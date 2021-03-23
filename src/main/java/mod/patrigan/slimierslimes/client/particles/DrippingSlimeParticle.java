package mod.patrigan.slimierslimes.client.particles;

import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

import static mod.patrigan.slimierslimes.init.ModParticleTypes.FALLING_SLIME;

public class DrippingSlimeParticle extends DripParticle.Dripping {

    public DrippingSlimeParticle(ClientWorld world, double x, double y, double z, Fluid fluid, IParticleData particleData) {
        super(world, x, y, z, fluid, particleData);
    }

    public static class DrippingSlimeFactory implements IParticleFactory<BasicParticleType> {
        protected final IAnimatedSprite spriteSet;
        protected final DyeColor dyeColor;

        public DrippingSlimeFactory(IAnimatedSprite spriteSet, DyeColor dyeColor) {
            this.spriteSet = spriteSet;
            this.dyeColor = dyeColor;
        }

        public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DrippingSlimeParticle dripparticle = new DrippingSlimeParticle(worldIn, x, y, z, Fluids.EMPTY, FALLING_SLIME.get(dyeColor).get());
            dripparticle.gravity *= 0.01F;
            dripparticle.lifetime = 100;
            Color color = new Color(dyeColor.getColorValue());
            dripparticle.setColor(color.getRed()/256F, color.getGreen()/256F, color.getBlue()/256F);
            dripparticle.pickSprite(this.spriteSet);
            return dripparticle;
        }
    }
}