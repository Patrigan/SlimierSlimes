package mod.patrigan.slimierslimes.entities;

import mod.patrigan.slimierslimes.entities.ai.goal.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static net.minecraft.item.Items.DIAMOND;
import static net.minecraft.particles.ParticleTypes.ITEM;

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
                livingEntity -> Math.abs(livingEntity.getPosY() - this.getPosY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute getMutableAttributes() {
        return MonsterEntity.func_234295_eP_();
    }

    @Override
    protected IParticleData getSquishParticle() {
        return new ItemParticleData(ITEM, new ItemStack(DIAMOND));
    }

    @Override
    protected void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
        this.getAttribute(Attributes.ARMOR).setBaseValue((double)(size * 2F));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) (0.3F + 0.1F * (float) size));
    }

    @Override
    protected ResourceLocation getLootTable() {
        return this.getType().getLootTable();
    }

    public void split() {
        this.setHealth(0F);
        this.remove(false);
    }
}