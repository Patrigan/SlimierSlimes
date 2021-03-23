package mod.patrigan.slimierslimes.entities.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class SlimeSnowballEntity extends SnowballEntity {

    public SlimeSnowballEntity(World worldIn, LivingEntity throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        super.onHitEntity(entityRayTraceResult);
        Entity entity = entityRayTraceResult.getEntity();
        int i = entity instanceof PlayerEntity ? 1 : 0;
        entity.hurt(DamageSource.thrown(this, this.getOwner()), (float)i);
    }
}
