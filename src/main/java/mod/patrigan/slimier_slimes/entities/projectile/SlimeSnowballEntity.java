package mod.patrigan.slimier_slimes.entities.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;

public class SlimeSnowballEntity extends Snowball {

    public SlimeSnowballEntity(Level worldIn, LivingEntity throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityRayTraceResult) {
        super.onHitEntity(entityRayTraceResult);
        Entity entity = entityRayTraceResult.getEntity();
        int i = entity instanceof Player ? 1 : 0;
        entity.hurt(DamageSource.thrown(this, this.getOwner()), (float)i);
    }
}
