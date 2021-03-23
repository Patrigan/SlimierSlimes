package mod.patrigan.slimierslimes.tileentities;

import mod.patrigan.slimierslimes.entities.GlowSlimeEntity;
import mod.patrigan.slimierslimes.init.ModTileEntityTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class MonsterLightAirTileEntity extends TileEntity implements ITickableTileEntity {

    public MonsterLightAirTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public MonsterLightAirTileEntity() {
        this(ModTileEntityTypes.MONSTER_LIGHT_AIR.get());
    }

    @Override
    public void tick() {
        AxisAlignedBB bounds = new AxisAlignedBB(worldPosition).inflate(1);
        List<GlowSlimeEntity> entitiesWithinAABB = this.level.getEntitiesOfClass(GlowSlimeEntity.class, bounds);
        if(entitiesWithinAABB.isEmpty()){
            this.level.removeBlock(worldPosition, false);
        }
    }
}
