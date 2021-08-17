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
import net.minecraft.core.particles.ParticleOptions;

import java.awt.*;

import static mod.patrigan.slimier_slimes.init.ModParticleTypes.FALLING_SLIME;

public class DripHangSlimeParticle extends DripParticle.DripHangParticle {

    public DripHangSlimeParticle(ClientLevel world, double x, double y, double z, Fluid fluid, ParticleOptions particleOptions) {
        super(world, x, y, z, fluid, particleOptions);
    }

    public static class DrippingSlimeFactory implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet spriteSet;
        protected final DyeColor dyeColor;

        public DrippingSlimeFactory(SpriteSet spriteSet, DyeColor dyeColor) {
            this.spriteSet = spriteSet;
            this.dyeColor = dyeColor;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DripHangSlimeParticle dripparticle = new DripHangSlimeParticle(worldIn, x, y, z, Fluids.EMPTY, FALLING_SLIME.get(dyeColor).get());
            dripparticle.gravity *= 0.01F;
            dripparticle.lifetime = 100;
            Color color = new Color(dyeColor.getMaterialColor().col);
            dripparticle.setColor(color.getRed()/256F, color.getGreen()/256F, color.getBlue()/256F);
            dripparticle.pickSprite(this.spriteSet);
            return dripparticle;
        }
    }
}