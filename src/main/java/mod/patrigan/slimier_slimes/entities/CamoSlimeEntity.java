package mod.patrigan.slimier_slimes.entities;

import mod.patrigan.slimier_slimes.entities.ai.goal.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

public class CamoSlimeEntity extends AbstractSlimeEntity {
    private static final int FLEE_TIME = 120;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new FleeGoal(this, FLEE_TIME, () -> !this.isInvisible(), this::hide));
        this.goalSelector.addGoal(3, new AttackGoal(this));
        this.goalSelector.addGoal(4, new HideGoal(this));
        this.goalSelector.addGoal(5, new FaceRandomGoal(this));
        this.goalSelector.addGoal(6, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public CamoSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public void hide() {
        this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE));
    }

    @Override
    protected boolean dealDamage(LivingEntity entityIn) {
        boolean dealtDamage = super.dealDamage(entityIn);
        if(dealtDamage) {
            this.removeEffect(MobEffects.INVISIBILITY);
        }
        return dealtDamage;
    }
}
