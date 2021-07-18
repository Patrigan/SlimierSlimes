package mod.patrigan.slimierslimes.entities;

import mod.patrigan.slimierslimes.entities.ai.goal.FaceRandomGoal;
import mod.patrigan.slimierslimes.entities.ai.goal.FleeGoal;
import mod.patrigan.slimierslimes.entities.ai.goal.FloatGoal;
import mod.patrigan.slimierslimes.entities.ai.goal.HopGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class DiamondSlimeEntity extends AbstractSlimeEntity {
    private static final int FLEE_TIME = 100;

    protected FleeGoal fleeGoal;

    public DiamondSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.fleeGoal = new FleeGoal(this, FLEE_TIME, () -> true, this::split);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, fleeGoal);
        this.goalSelector.addGoal(3, new FaceRandomGoal(this));
        this.goalSelector.addGoal(4, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute getMutableAttributes() {
        return MonsterEntity.createMonsterAttributes();
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.getType().getDefaultLootTable();
    }

    public void split() {
        this.setHealth(0F);
        this.remove(false);
    }
}