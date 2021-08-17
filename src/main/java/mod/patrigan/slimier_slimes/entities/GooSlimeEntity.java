package mod.patrigan.slimier_slimes.entities;

import mod.patrigan.slimier_slimes.entities.ai.goal.FaceRandomGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.FloatGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.HopGoal;
import mod.patrigan.slimier_slimes.entities.projectile.SlimeBallEntity;
import mod.patrigan.slimier_slimes.items.SlimeBallItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;

import net.minecraft.world.level.Level;

import static mod.patrigan.slimier_slimes.blocks.GooLayerBlock.LAYERS;
import static mod.patrigan.slimier_slimes.init.ModBlocks.GOO_LAYER_BLOCKS;
import static mod.patrigan.slimier_slimes.init.ModItems.SLIME_BALL;

public class GooSlimeEntity extends AbstractSlimeEntity implements RangedAttackMob {

    public GooSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected void land(){
        super.land();
        if (!this.level.isClientSide && random.nextFloat() < 0.1F && !this.isTiny()) {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                return;
            }

            BlockState blockstate = GOO_LAYER_BLOCKS.get(this.getDyeColor()).get().defaultBlockState();

            for(int l = 0; l < 4; ++l) {
                int i = (int) Math.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                int j = (int) Math.floor(this.getY());
                int k = (int) Math.floor(this.getZ() + (double) ((l / 2F % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos = new BlockPos(i, j, k);
                if (this.level.isEmptyBlock(blockpos) && blockstate.canSurvive(this.level, blockpos)) {
                    this.level.setBlockAndUpdate(blockpos, blockstate);
                }else if(this.level.getBlockState(blockpos).is(blockstate.getBlock()) && this.level.getBlockState(blockpos).getValue(LAYERS) < 8){
                    this.level.setBlockAndUpdate(blockpos, blockstate.setValue(LAYERS, this.level.getBlockState(blockpos).getValue(LAYERS)+1));
                }
            }
        }
    }

    public void performRangedAttack(LivingEntity livingEntity, float p_82196_2_) {
        SlimeBallEntity slimeBallentity = new SlimeBallEntity((SlimeBallItem) SLIME_BALL.get(this.getDyeColor()).get(), this.level, this);
        slimeBallentity.setItem(SLIME_BALL.get(this.getDyeColor()).get().getDefaultInstance());
        double d0 = livingEntity.getEyeY() - (double)1.1F;
        double d1 = livingEntity.getX() - this.getX();
        double d2 = d0 - slimeBallentity.getY();
        double d3 = livingEntity.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        slimeBallentity.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(slimeBallentity);
    }

    @Override
    public void setSize(int size, boolean resetHealth) {
        super.setSize(size, resetHealth);
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.25D, 40-(getSize()*5), 45-(getSize()*5), 10.0F));
    }
}