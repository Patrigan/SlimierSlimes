package mod.patrigan.slimierslimes.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class RockSlimeEntity extends AbstractSlimeEntity {

    public RockSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute getMutableAttributes() {
        return MonsterEntity.func_234295_eP_();
    }

    @Override
    protected void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
        this.getAttribute(Attributes.ARMOR).setBaseValue((double)(size * 2));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) (0.1F + 0.1F * (float) size));
    }

    @Override
    public int getJumpDelay() {
        return super.getJumpDelay() * 5;
    }

    @Override
    protected void alterSquishAmount() {
        this.squishAmount *= 0.9F;
    }

    @Override
    protected void jump() {
        Vector3d vector3d = this.getMotion();
        this.setMotion(vector3d.x, this.getJumpUpwardsMotion(), vector3d.z);
        this.isAirBorne = true;
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return super.getJumpUpwardsMotion() * 0.5F;
    }

    @Override
    protected float getAttackDamage() {
        return super.getAttackDamage() + 1.0F;
    }

}
