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

import java.awt.*;

import static mod.patrigan.slimierslimes.init.ModParticleTypes.LANDING_SLIME;

public class FallingSlimeParticle extends DripParticle.FallingLiquidParticle {

    public FallingSlimeParticle(ClientWorld world, double x, double y, double z, Fluid fluid, IParticleData particleData) {
        super(world, x, y, z, fluid, particleData);
    }

    public static class FallingSlimeFactory implements IParticleFactory<BasicParticleType> {
        protected final IAnimatedSprite spriteSet;
        protected final DyeColor dyeColor;

        public FallingSlimeFactory(IAnimatedSprite spriteSet, DyeColor dyeColor) {
            this.spriteSet = spriteSet;
            this.dyeColor = dyeColor;
        }

        public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingSlimeParticle dripparticle = new FallingSlimeParticle(worldIn, x, y, z, Fluids.EMPTY, LANDING_SLIME.get(dyeColor).get());
            dripparticle.gravity = 0.01F;
            Color color = new Color(dyeColor.getColorValue());
            dripparticle.setColor(color.getRed()/256F, color.getGreen()/256F, color.getBlue()/256F);
            dripparticle.pickSprite(this.spriteSet);
            return dripparticle;
        }
    }
}