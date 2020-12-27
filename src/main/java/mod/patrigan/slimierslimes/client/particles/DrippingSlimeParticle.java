package mod.patrigan.slimierslimes.client.particles;

import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static mod.patrigan.slimierslimes.init.ModParticleTypes.FALLING_SLIME;

@OnlyIn(Dist.CLIENT)
public class DrippingSlimeParticle extends DripParticle.Dripping {

    public DrippingSlimeParticle(ClientWorld world, double x, double y, double z, Fluid fluid, IParticleData particleData) {
        super(world, x, y, z, fluid, particleData);
    }

    @OnlyIn(Dist.CLIENT)
    public static class DrippingSlimeFactory implements IParticleFactory<BasicParticleType> {
        protected final IAnimatedSprite spriteSet;

        public DrippingSlimeFactory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DrippingSlimeParticle dripparticle = new DrippingSlimeParticle(worldIn, x, y, z, Fluids.EMPTY, FALLING_SLIME.get());
            dripparticle.particleGravity *= 0.01F;
            dripparticle.maxAge = 100;
            dripparticle.setColor(0.384F, 0.713F, 0.290F);
            dripparticle.selectSpriteRandomly(this.spriteSet);
            return dripparticle;
        }
    }
}