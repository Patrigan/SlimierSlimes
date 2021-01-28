package mod.patrigan.slimierslimes.init;

import com.mojang.serialization.Codec;
import mod.patrigan.slimierslimes.world.gen.processors.BlockMossifyProcessor;
import mod.patrigan.slimierslimes.world.gen.processors.BlockSlimifyProcessor;
import mod.patrigan.slimierslimes.world.gen.processors.SlimeSpawnerProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.StructureProcessor;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;

public class ModProcessors {
    public static IStructureProcessorType<BlockMossifyProcessor> BLOCK_MOSSIFY;
    public static IStructureProcessorType<BlockSlimifyProcessor> BLOCK_SLIMIFY;
    public static IStructureProcessorType<SlimeSpawnerProcessor> SLIME_SPAWNER_RANDOM;

    public static void init(){
        BLOCK_MOSSIFY = register("block_mossify", BlockMossifyProcessor.CODEC);
        BLOCK_SLIMIFY = register("block_slimify", BlockSlimifyProcessor.CODEC);
        SLIME_SPAWNER_RANDOM = register("slime_spawner_random", SlimeSpawnerProcessor.CODEC);
    }

    static <P extends StructureProcessor> IStructureProcessorType<P> register(String name, Codec<P> codec) {
        return Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(MOD_ID, name), () -> {
            return codec;
        });
    }
}
