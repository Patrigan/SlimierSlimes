package mod.patrigan.slimier_slimes.init;

import com.mojang.serialization.Codec;
import mod.patrigan.slimier_slimes.world.gen.processors.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

import static mod.patrigan.slimier_slimes.SlimierSlimes.MOD_ID;

public class ModProcessors {
    public static StructureProcessorType<BlockSlimifyProcessor> BLOCK_SLIMIFY;
    public static StructureProcessorType<GooProcessor> GOO_BLOCKS;


    public static void init(){
        BLOCK_SLIMIFY = register("block_slimify", BlockSlimifyProcessor.CODEC);
        GOO_BLOCKS = register("goo_blocks", GooProcessor.CODEC);
    }

    static <P extends StructureProcessor> StructureProcessorType<P> register(String name, Codec<P> codec) {
        return Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(MOD_ID, name), () -> {
            return codec;
        });
    }
}
