package mod.patrigan.slimierslimes.tileentities;

import mod.patrigan.slimierslimes.entities.LavaSlimeEntity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.SpawnReason;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.LAVA_SLIME;
import static mod.patrigan.slimierslimes.init.ModTileEntityTypes.LAVA_SLIME_SPAWNER;

public class LavaSlimeSpawnerTileEntity extends TileEntity implements ITickableTileEntity {
    private int spawnDelay = 200;
    private int minSpawnDelay = 600;
    private int maxSpawnDelay = 2000;
    private int spawnRange = 2;
    private int maxNearbyEntities = 2;
    private int activityRange = 50;

    public LavaSlimeSpawnerTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public LavaSlimeSpawnerTileEntity() {
        this(LAVA_SLIME_SPAWNER.get());
    }

    @Override
    public void tick() {
        World world = this.getLevel();
        BlockPos blockpos = this.worldPosition;
        if (!(world instanceof ServerWorld)) {
            double d3 = (double) blockpos.getX() + world.random.nextDouble();
            double d4 = (double) blockpos.getY() + world.random.nextDouble();
            double d5 = (double) blockpos.getZ() + world.random.nextDouble();
            world.addParticle(ParticleTypes.SMOKE, d3, d4, d5, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D);
            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            }
        } else {
            if (this.spawnDelay == -1) {
                this.resetTimer();
            }

            if (this.spawnDelay > 0) {
                --this.spawnDelay;
                return;
            }

            doSpawn(world, blockpos);
        }
    }

    private void doSpawn(World world, BlockPos blockpos) {
        double d0 = (double) blockpos.getX() + (world.random.nextDouble() - world.random.nextDouble()) * (double) this.spawnRange + 0.5D;
        double d1 = (double) (blockpos.getY() + world.random.nextInt(4) + 1);
        double d2 = (double) blockpos.getZ() + (world.random.nextDouble() - world.random.nextDouble()) * (double) this.spawnRange + 0.5D;
        if (world.noCollision(LAVA_SLIME.get().getAABB(d0, d1, d2))) {
            ServerWorld serverworld = (ServerWorld) world;
            if (EntitySpawnPlacementRegistry.checkSpawnRules(LAVA_SLIME.get(), serverworld, SpawnReason.SPAWNER, new BlockPos(d0, d1, d2), world.getRandom())) {

                int k = world.getEntitiesOfClass(LavaSlimeEntity.class, new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), (blockpos.getX() + 1), (blockpos.getY() + 1), (blockpos.getZ() + 1)).inflate(this.activityRange)).size();
                if (k >= this.maxNearbyEntities) {
                    this.resetTimer();
                    return;
                }

                LavaSlimeEntity entity = LAVA_SLIME.get().spawn(serverworld, null, null, null, new BlockPos(d0, d1, d2), SpawnReason.SPAWNER, false, false);
                if (entity == null) {
                    this.resetTimer();
                    return;
                }
                entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), world.random.nextFloat() * 360.0F, 0.0F);

                this.resetTimer();
            }
        }
    }

    @SuppressWarnings("resource")
    private void resetTimer() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getLevel().random.nextInt(i);
        }
    }
}
