package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.tileentities.MonsterLightAirTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static mod.patrigan.slimierslimes.init.ModBlocks.LIGHT_AIR;

public class ModTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SlimierSlimes.MOD_ID);

    public static final RegistryObject<TileEntityType<MonsterLightAirTileEntity>> MONSTER_LIGHT_AIR = TILE_ENTITY_TYPES.register("monster_light_air",
            () -> TileEntityType.Builder.create(MonsterLightAirTileEntity::new, LIGHT_AIR.get()).build(null));
}
