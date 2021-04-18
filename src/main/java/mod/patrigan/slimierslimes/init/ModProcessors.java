package mod.patrigan.slimierslimes.init;

import com.mojang.serialization.Codec;
import mod.patrigan.slimierslimes.world.gen.processors.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.StructureProcessor;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;

public class ModProcessors {
    public static IStructureProcessorType<BlockMossifyProcessor> BLOCK_MOSSIFY;
    public static IStructureProcessorType<BlockSlimifyProcessor> BLOCK_SLIMIFY;
    public static IStructureProcessorType<AirRetainerProcessor> AIR_RETAINER;
    public static IStructureProcessorType<GradientSpotReplaceProcessor> GRADIENT_SPOT_REPLACE;
    public static IStructureProcessorType<CeilingAttachmentProcessor> CEILING_ATTACHMENT;
    public static IStructureProcessorType<VineProcessor> VINES;
    public static IStructureProcessorType<MushroomProcessor> MUSHROOMS;
    public static IStructureProcessorType<LilyPadProcessor> LILY_PADS;
    public static IStructureProcessorType<GooProcessor> GOO_BLOCKS;
    public static IStructureProcessorType<SpawnerRandomizerProcessor> SPAWNER_RANDOMIZER_PROCESSOR;

    public static void init(){
        BLOCK_MOSSIFY = register("block_mossify", BlockMossifyProcessor.CODEC);
        BLOCK_SLIMIFY = register("block_slimify", BlockSlimifyProcessor.CODEC);
        AIR_RETAINER = register("air_retainer", AirRetainerProcessor.CODEC);
        GRADIENT_SPOT_REPLACE = register("gradient_spot_replace", GradientSpotReplaceProcessor.CODEC);
        CEILING_ATTACHMENT = register("ceiling_attachment", CeilingAttachmentProcessor.CODEC);
        VINES = register("vines", VineProcessor.CODEC);
        MUSHROOMS = register("mushrooms", MushroomProcessor.CODEC);
        LILY_PADS = register("lily_pads", LilyPadProcessor.CODEC);
        GOO_BLOCKS = register("goo_blocks", GooProcessor.CODEC);
        SPAWNER_RANDOMIZER_PROCESSOR = register("spawner_randomizer", SpawnerRandomizerProcessor.CODEC);
    }

    static <P extends StructureProcessor> IStructureProcessorType<P> register(String name, Codec<P> codec) {
        return Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(MOD_ID, name), () -> {
            return codec;
        });
    }
}
