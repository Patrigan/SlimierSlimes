package mod.patrigan.slimierslimes.entities;

import mod.patrigan.slimierslimes.entities.ai.goal.FaceRandomGoal;
import mod.patrigan.slimierslimes.entities.ai.goal.FloatGoal;
import mod.patrigan.slimierslimes.entities.ai.goal.HopGoal;
import mod.patrigan.slimierslimes.entities.projectile.SlimeBallEntity;
import mod.patrigan.slimierslimes.entities.projectile.SlimeSnowballEntity;
import mod.patrigan.slimierslimes.items.SlimeBallItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import static mod.patrigan.slimierslimes.blocks.GooLayerBlock.LAYERS;
import static mod.patrigan.slimierslimes.init.ModBlocks.GOO_LAYER_BLOCKS;
import static mod.patrigan.slimierslimes.init.ModItems.SLIME_BALL;

public class GooSlimeEntity extends AbstractSlimeEntity implements IRangedAttackMob {
    DyeColor dyeColor = DyeColor.BROWN;

    public GooSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.25D, 20, 10.0F));
        this.goalSelector.addGoal(3, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    protected void land(){
        super.land();
        if (!this.level.isClientSide) {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                return;
            }

            BlockState blockstate = GOO_LAYER_BLOCKS.get(dyeColor).get().defaultBlockState();

            for(int l = 0; l < 4; ++l) {
                int i = MathHelper.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                int j = MathHelper.floor(this.getY());
                int k = MathHelper.floor(this.getZ() + (double) ((l / 2F % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos = new BlockPos(i, j, k);
                if (this.level.isEmptyBlock(blockpos) && blockstate.canSurvive(this.level, blockpos)) {
                    this.level.setBlockAndUpdate(blockpos, blockstate);
                }else if(this.level.getBlockState(blockpos).is(blockstate.getBlock()) && this.level.getBlockState(blockpos).getValue(LAYERS) < 8){
                    this.level.setBlockAndUpdate(blockpos, blockstate.setValue(LAYERS, this.level.getBlockState(blockpos).getValue(LAYERS)+1));
                }
            }
        }
    }

    public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {
        SlimeBallEntity slimeBallentity = new SlimeBallEntity((SlimeBallItem) SLIME_BALL.get(dyeColor).get(), this.level, this);
        slimeBallentity.setItem(SLIME_BALL.get(dyeColor).get().getDefaultInstance());
        double d0 = p_82196_1_.getEyeY() - (double)1.1F;
        double d1 = p_82196_1_.getX() - this.getX();
        double d2 = d0 - slimeBallentity.getY();
        double d3 = p_82196_1_.getZ() - this.getZ();
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        slimeBallentity.shoot(d1, d2 + (double)f, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(slimeBallentity);
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }
}