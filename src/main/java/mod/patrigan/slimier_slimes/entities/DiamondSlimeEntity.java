package mod.patrigan.slimier_slimes.entities;

import mod.patrigan.slimier_slimes.entities.ai.goal.FaceRandomGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.FleeGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.FloatGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.HopGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class DiamondSlimeEntity extends AbstractSlimeEntity {
    private static final int FLEE_TIME = 100;

    protected FleeGoal fleeGoal;

    public DiamondSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.fleeGoal = new FleeGoal(this, FLEE_TIME, () -> true, this::split);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, fleeGoal);
        this.goalSelector.addGoal(3, new FaceRandomGoal(this));
        this.goalSelector.addGoal(4, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder getMutableAttributes() {
        return Monster.createMonsterAttributes();
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.getType().getDefaultLootTable();
    }

    public void split() {
        this.setHealth(0F);
        this.discard();
    }
}