package mod.patrigan.slimier_slimes.client.particles;

import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.particles.SimpleParticleType;

import java.awt.*;

public class DripLandSlimeParticle extends DripParticle.DripLandParticle {

    public DripLandSlimeParticle(ClientLevel world, double x, double y, double z, Fluid fluid) {
        super(world, x, y, z, fluid);
    }

    public static class LandingSlimeFactory implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet spriteSet;
        protected final DyeColor dyeColor;

        public LandingSlimeFactory(SpriteSet spriteSet, DyeColor dyeColor) {
            this.spriteSet = spriteSet;
            this.dyeColor = dyeColor;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DripLandSlimeParticle dripparticle = new DripLandSlimeParticle(worldIn, x, y, z, Fluids.EMPTY);
            dripparticle.lifetime = (int)(128.0D / (worldIn.random.nextDouble() * 0.8D + 0.2D));
            Color color = new Color(dyeColor.getMaterialColor().col);
            dripparticle.setColor(color.getRed()/256F, color.getGreen()/256F, color.getBlue()/256F);
            dripparticle.pickSprite(this.spriteSet);
            return dripparticle;
        }
    }
}