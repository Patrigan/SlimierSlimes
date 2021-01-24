package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.tileentities.LavaSlimeSpawnerTileEntity;
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

    public static final RegistryObject<TileEntityType<LavaSlimeSpawnerTileEntity>> LAVA_SLIME_SPAWNER = TILE_ENTITY_TYPES.register("lava_slime_spawner",
            () -> TileEntityType.Builder.create(LavaSlimeSpawnerTileEntity::new, ModBlocks.STONE_LAVA_SLIME_SPAWNER.get(), ModBlocks.NETHERRACK_LAVA_SLIME_SPAWNER.get()).build(null));
}
