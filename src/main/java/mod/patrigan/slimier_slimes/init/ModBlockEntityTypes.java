package mod.patrigan.slimier_slimes.init;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.tileentities.LavaSlimeSpawnerBlockEntity;
import mod.patrigan.slimier_slimes.tileentities.LightAirBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static mod.patrigan.slimier_slimes.init.ModBlocks.LIGHT_AIR;

public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SlimierSlimes.MOD_ID);

    public static final RegistryObject<BlockEntityType<LightAirBlockEntity>> MONSTER_LIGHT_AIR = BLOCK_ENTITY_TYPES.register("monster_light_air",
            () -> BlockEntityType.Builder.of(LightAirBlockEntity::new, LIGHT_AIR.get()).build(null));

    public static final RegistryObject<BlockEntityType<LavaSlimeSpawnerBlockEntity>> LAVA_SLIME_SPAWNER = BLOCK_ENTITY_TYPES.register("lava_slime_spawner",
            () -> BlockEntityType.Builder.of(LavaSlimeSpawnerBlockEntity::new, ModBlocks.STONE_LAVA_SLIME_SPAWNER.get(), ModBlocks.NETHERRACK_LAVA_SLIME_SPAWNER.get()).build(null));
}
