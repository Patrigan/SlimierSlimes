package mod.patrigan.slimierslimes.init;

import com.mojang.serialization.Codec;
import mod.patrigan.slimierslimes.world.gen.processors.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.StructureProcessor;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;

public class ModProcessors {
    public static IStructureProcessorType<BlockSlimifyProcessor> BLOCK_SLIMIFY;
    public static IStructureProcessorType<GooProcessor> GOO_BLOCKS;


    public static void init(){
        BLOCK_SLIMIFY = register("block_slimify", BlockSlimifyProcessor.CODEC);
        GOO_BLOCKS = register("goo_blocks", GooProcessor.CODEC);
    }

    static <P extends StructureProcessor> IStructureProcessorType<P> register(String name, Codec<P> codec) {
        return Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(MOD_ID, name), () -> {
            return codec;
        });
    }
}
