package mod.patrigan.slimierslimes.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class SlimeIllagerEntity extends SpellcastingIllagerEntity {
   private SheepEntity wololoTarget;

   public SlimeIllagerEntity(EntityType<? extends SlimeIllagerEntity> type, World worldIn) {
      super(type, worldIn);
      this.xpReward = 10;
   }

   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(0, new SwimGoal(this));
      this.goalSelector.addGoal(1, new SlimeIllagerEntity.CastingSpellGoal());
      this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
      this.goalSelector.addGoal(4, new SlimeIllagerEntity.SummonSpellGoal());
      //this.goalSelector.addGoal(5, new SlimerIllagerEntity.AttackSpellGoal());
      this.goalSelector.addGoal(6, new SlimeIllagerEntity.WololoSpellGoal());
      this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
      this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
      this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
      this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
      this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
      this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
   }

   public static AttributeModifierMap.MutableAttribute createAttributes() {
      return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.5D).add(Attributes.FOLLOW_RANGE, 12.0D).add(Attributes.MAX_HEALTH, 24.0D);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   public void readAdditionalSaveData(CompoundNBT compound) {
      super.readAdditionalSaveData(compound);
   }

   public SoundEvent getCelebrateSound() {
      return SoundEvents.EVOKER_CELEBRATE;
   }

   public void addAdditionalSaveData(CompoundNBT compound) {
      super.addAdditionalSaveData(compound);
   }

   protected void customServerAiStep() {
      super.customServerAiStep();
   }

   /**
    * Returns whether this Entity is on the same team as the given Entity.
    */
   public boolean isAlliedTo(Entity entityIn) {
      if (entityIn == null) {
         return false;
      } else if (entityIn == this) {
         return true;
      } else if (super.isAlliedTo(entityIn)) {
         return true;
      } else if (entityIn instanceof VexEntity) {
         return this.isAlliedTo(((VexEntity)entityIn).getOwner());
      } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == CreatureAttribute.ILLAGER) {
         return this.getTeam() == null && entityIn.getTeam() == null;
      } else {
         return false;
      }
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.EVOKER_AMBIENT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.EVOKER_DEATH;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.EVOKER_HURT;
   }

   private void setWololoTarget(@Nullable SheepEntity wololoTargetIn) {
      this.wololoTarget = wololoTargetIn;
   }

   @Nullable
   private SheepEntity getWololoTarget() {
      return this.wololoTarget;
   }

   protected SoundEvent getCastingSoundEvent() {
      return SoundEvents.EVOKER_CAST_SPELL;
   }

   public void applyRaidBuffs(int wave, boolean p_213660_2_) {
   }

   class AttackSpellGoal extends UseSpellGoal {
      private AttackSpellGoal() {
      }

      protected int getCastingTime() {
         return 40;
      }

      protected int getCastingInterval() {
         return 100;
      }

      protected void performSpellCasting() {
         LivingEntity livingentity = SlimeIllagerEntity.this.getTarget();
         double d0 = Math.min(livingentity.getY(), SlimeIllagerEntity.this.getY());
         double d1 = Math.max(livingentity.getY(), SlimeIllagerEntity.this.getY()) + 1.0D;
         float f = (float)MathHelper.atan2(livingentity.getZ() - SlimeIllagerEntity.this.getZ(), livingentity.getX() - SlimeIllagerEntity.this.getX());
         if (SlimeIllagerEntity.this.distanceToSqr(livingentity) < 9.0D) {
            for(int i = 0; i < 5; ++i) {
               float f1 = f + (float)i * (float)Math.PI * 0.4F;
               this.spawnFangs(SlimeIllagerEntity.this.getX() + (double)MathHelper.cos(f1) * 1.5D, SlimeIllagerEntity.this.getZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
            }

            for(int k = 0; k < 8; ++k) {
               float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
               this.spawnFangs(SlimeIllagerEntity.this.getX() + (double)MathHelper.cos(f2) * 2.5D, SlimeIllagerEntity.this.getZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
            }
         } else {
            for(int l = 0; l < 16; ++l) {
               double d2 = 1.25D * (double)(l + 1);
               int j = 1 * l;
               this.spawnFangs(SlimeIllagerEntity.this.getX() + (double)MathHelper.cos(f) * d2, SlimeIllagerEntity.this.getZ() + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
            }
         }

      }

      private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
         BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
         boolean flag = false;
         double d0 = 0.0D;

         do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = SlimeIllagerEntity.this.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(SlimeIllagerEntity.this.level, blockpos1, Direction.UP)) {
               if (!SlimeIllagerEntity.this.level.isEmptyBlock(blockpos)) {
                  BlockState blockstate1 = SlimeIllagerEntity.this.level.getBlockState(blockpos);
                  VoxelShape voxelshape = blockstate1.getBlockSupportShape(SlimeIllagerEntity.this.level, blockpos);
                  if (!voxelshape.isEmpty()) {
                     d0 = voxelshape.max(Direction.Axis.Y);
                  }
               }

               flag = true;
               break;
            }

            blockpos = blockpos.below();
         } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

         if (flag) {
            SlimeIllagerEntity.this.level.addFreshEntity(new EvokerFangsEntity(SlimeIllagerEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, SlimeIllagerEntity.this));
         }

      }

      protected SoundEvent getSpellPrepareSound() {
         return SoundEvents.EVOKER_PREPARE_ATTACK;
      }

      protected SpellType getSpell() {
         return SpellType.FANGS;
      }
   }

   class CastingSpellGoal extends CastingASpellGoal {
      private CastingSpellGoal() {
      }

      /**
       * Keep ticking a continuous task that has already been started
       */
      public void tick() {
         if (SlimeIllagerEntity.this.getTarget() != null) {
            SlimeIllagerEntity.this.getLookControl().setLookAt(SlimeIllagerEntity.this.getTarget(), (float) SlimeIllagerEntity.this.getMaxHeadYRot(), (float) SlimeIllagerEntity.this.getMaxHeadXRot());
         } else if (SlimeIllagerEntity.this.getWololoTarget() != null) {
            SlimeIllagerEntity.this.getLookControl().setLookAt(SlimeIllagerEntity.this.getWololoTarget(), (float) SlimeIllagerEntity.this.getMaxHeadYRot(), (float) SlimeIllagerEntity.this.getMaxHeadXRot());
         }

      }
   }

   class SummonSpellGoal extends UseSpellGoal {
      private final EntityPredicate vexCountTargeting = (new EntityPredicate()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

      private SummonSpellGoal() {
      }

      /**
       * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
       * method as well.
       */
      public boolean canUse() {
         if (!super.canUse()) {
            return false;
         } else {
            int i = SlimeIllagerEntity.this.level.getNearbyEntities(VexEntity.class, this.vexCountTargeting, SlimeIllagerEntity.this, SlimeIllagerEntity.this.getBoundingBox().inflate(16.0D)).size();
            return SlimeIllagerEntity.this.random.nextInt(8) + 1 > i;
         }
      }

      protected int getCastingTime() {
         return 100;
      }

      protected int getCastingInterval() {
         return 340;
      }

      protected void performSpellCasting() {
         ServerWorld serverworld = (ServerWorld) SlimeIllagerEntity.this.level;

         for(int i = 0; i < 3; ++i) {
            BlockPos blockpos = SlimeIllagerEntity.this.blockPosition().offset(-2 + SlimeIllagerEntity.this.random.nextInt(5), 1, -2 + SlimeIllagerEntity.this.random.nextInt(5));
            VexEntity vexentity = EntityType.VEX.create(SlimeIllagerEntity.this.level);
            vexentity.moveTo(blockpos, 0.0F, 0.0F);
            vexentity.finalizeSpawn(serverworld, SlimeIllagerEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
            vexentity.setOwner(SlimeIllagerEntity.this);
            vexentity.setBoundOrigin(blockpos);
            vexentity.setLimitedLife(20 * (30 + SlimeIllagerEntity.this.random.nextInt(90)));
            serverworld.addFreshEntityWithPassengers(vexentity);
         }

      }

      protected SoundEvent getSpellPrepareSound() {
         return SoundEvents.EVOKER_PREPARE_SUMMON;
      }

      protected SpellType getSpell() {
         return SpellType.SUMMON_VEX;
      }
   }

   public class WololoSpellGoal extends UseSpellGoal {
      private final EntityPredicate wololoTargetFlags = (new EntityPredicate()).range(16.0D).allowInvulnerable().selector((p_220844_0_) -> {
         return ((SheepEntity)p_220844_0_).getColor() == DyeColor.GREEN;
      });

      /**
       * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
       * method as well.
       */
      public boolean canUse() {
         if (SlimeIllagerEntity.this.getTarget() != null) {
            return false;
         } else if (SlimeIllagerEntity.this.isCastingSpell()) {
            return false;
         } else if (SlimeIllagerEntity.this.tickCount < this.nextAttackTickCount) {
            return false;
         } else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(SlimeIllagerEntity.this.level, SlimeIllagerEntity.this)) {
            return false;
         } else {
            List<SheepEntity> list = SlimeIllagerEntity.this.level.getNearbyEntities(SheepEntity.class, this.wololoTargetFlags, SlimeIllagerEntity.this, SlimeIllagerEntity.this.getBoundingBox().inflate(16.0D, 4.0D, 16.0D));
            if (list.isEmpty()) {
               return false;
            } else {
               SlimeIllagerEntity.this.setWololoTarget(list.get(SlimeIllagerEntity.this.random.nextInt(list.size())));
               return true;
            }
         }
      }

      /**
       * Returns whether an in-progress EntityAIBase should continue executing
       */
      public boolean canContinueToUse() {
         return SlimeIllagerEntity.this.getWololoTarget() != null && this.attackWarmupDelay > 0;
      }

      /**
       * Reset the task's internal state. Called when this task is interrupted by another one
       */
      public void stop() {
         super.stop();
         SlimeIllagerEntity.this.setWololoTarget((SheepEntity)null);
      }

      protected void performSpellCasting() {
         SheepEntity sheepentity = SlimeIllagerEntity.this.getWololoTarget();
         if (sheepentity != null && sheepentity.isAlive()) {
            sheepentity.setColor(DyeColor.RED);
         }

      }

      protected int getCastWarmupTime() {
         return 40;
      }

      protected int getCastingTime() {
         return 60;
      }

      protected int getCastingInterval() {
         return 140;
      }

      protected SoundEvent getSpellPrepareSound() {
         return SoundEvents.EVOKER_PREPARE_WOLOLO;
      }

      protected SpellType getSpell() {
         return SpellType.WOLOLO;
      }
   }
}
