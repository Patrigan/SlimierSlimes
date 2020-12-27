package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.blocks.*;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SlimierSlimes.MOD_ID);
    public static final List<String> BLOCK_IDS = new ArrayList<>();

    public static final RegistryObject<Block> LIGHT_AIR = registerBlock("light_air",  LightAirBlock::new);
    public static final RegistryObject<Block> SLIMY_STONE_BLOCK = registerBlock("slimy_stone", SlimyStoneBlock::new);
    public static final RegistryObject<Block> AMETHYST_CLUSTER = registerBlock("amethyst_cluster", AmethystClusterBlock::new);
    public static final RegistryObject<Block> SMALL_AMETHYST_BUD = registerBlock("small_amethyst_bud", SmallAmethystBudBlock::new);
    public static final RegistryObject<Block> MEDIUM_AMETHYST_BUD = registerBlock("medium_amethyst_bud", MediumAmethystBudBlock::new);
    public static final RegistryObject<Block> LARGE_AMETHYST_BUD = registerBlock("large_amethyst_bud", LargeAmethystBudBlock::new);

    private static RegistryObject<Block> registerBlock(String id, Supplier<Block> sup) {
        BLOCK_IDS.add(id);
        return BLOCKS.register(id, sup);
    }
}
