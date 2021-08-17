package mod.patrigan.slimier_slimes.tileentities;

import mod.patrigan.slimier_slimes.entities.LavaSlimeEntity;
import mod.patrigan.slimier_slimes.init.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import static mod.patrigan.slimier_slimes.init.ModEntityTypes.LAVA_SLIME;

public class LavaSlimeSpawnerBlockEntity extends BlockEntity {
    private int spawnDelay = 200;
    private int minSpawnDelay = 600;
    private int maxSpawnDelay = 2000;
    private int spawnRange = 2;
    private int maxNearbyEntities = 2;
    private int activityRange = 50;

    public LavaSlimeSpawnerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntityTypes.LAVA_SLIME_SPAWNER.get(), p_155229_, p_155230_);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, LavaSlimeSpawnerBlockEntity blockEntity) {
        blockEntity.doServerTick((ServerLevel) level, blockPos);
    }


    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, LavaSlimeSpawnerBlockEntity blockEntity) {
        blockEntity.doClientTick(level, blockPos);
    }

    public void doClientTick(Level level, BlockPos blockPos) {
        double d3 = (double) blockPos.getX() + this.level.random.nextDouble();
        double d4 = (double) blockPos.getY() + this.level.random.nextDouble();
        double d5 = (double) blockPos.getZ() + this.level.random.nextDouble();
        this.level.addParticle(ParticleTypes.SMOKE, d3, d4, d5, 0.0D, 0.0D, 0.0D);
        this.level.addParticle(ParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D);
        if (this.spawnDelay > 0) {
            --this.spawnDelay;
        }
    }

    public void doServerTick(ServerLevel level, BlockPos blockPos) {
        if (this.spawnDelay == -1) {
            this.resetTimer();
        }

        if (this.spawnDelay > 0) {
            --this.spawnDelay;
            return;
        }

        doSpawn(level, blockPos);
    }

    private void doSpawn(Level world, BlockPos blockpos) {
        double d0 = (double) blockpos.getX() + (world.random.nextDouble() - world.random.nextDouble()) * (double) this.spawnRange + 0.5D;
        double d1 = (double) (blockpos.getY() + world.random.nextInt(4) + 1);
        double d2 = (double) blockpos.getZ() + (world.random.nextDouble() - world.random.nextDouble()) * (double) this.spawnRange + 0.5D;
        if (world.noCollision(LAVA_SLIME.get().getAABB(d0, d1, d2))) {
            ServerLevel serverworld = (ServerLevel) world;
            if (SpawnPlacements.checkSpawnRules(LAVA_SLIME.get(), serverworld, MobSpawnType.SPAWNER, new BlockPos(d0, d1, d2), world.getRandom())) {

                int k = world.getEntitiesOfClass(LavaSlimeEntity.class, new AABB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), (blockpos.getX() + 1), (blockpos.getY() + 1), (blockpos.getZ() + 1)).inflate(this.activityRange)).size();
                if (k >= this.maxNearbyEntities) {
                    this.resetTimer();
                    return;
                }

                LavaSlimeEntity entity = LAVA_SLIME.get().spawn(serverworld, null, null, null, new BlockPos(d0, d1, d2), MobSpawnType.SPAWNER, false, false);
                if (entity == null) {
                    this.resetTimer();
                    return;
                }
                entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), world.random.nextFloat() * 360.0F, 0.0F);

                this.resetTimer();
            }
        }
    }

    private void resetTimer() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getLevel().random.nextInt(i);
        }
    }
}
