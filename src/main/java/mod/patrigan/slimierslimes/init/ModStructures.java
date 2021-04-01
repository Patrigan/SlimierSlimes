package mod.patrigan.slimierslimes.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.structures.PillagerSlimeLab;
import mod.patrigan.slimierslimes.structures.SlimeDungeon;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModStructures {

    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, SlimierSlimes.MOD_ID);

    /**
     * Registers the structure itself and sets what its path is. In this case, the
     * structure will have the resourcelocation of structure_tutorial:run_down_house.
     *
     * It is always a good idea to register your Structures so that other mods and datapacks can
     * use them too directly from the registries. It great for mod/datapacks compatibility.
     *
     * IMPORTANT: Once you have set the name for your structure below and distributed your mod,
     * it should NEVER be changed or else it can cause worlds to become corrupted if they generated
     * any chunks with your mod with the old structure name. See MC-194811 in Mojang's bug tracker for details.
     *
     * Forge has an issue report here: https://github.com/MinecraftForge/MinecraftForge/issues/7363
     * Keep watch on that to know when it is safe to remove or change structure's registry names
     */
    public static final RegistryObject<Structure<NoFeatureConfig>> PILLAGER_SLIME_LAB = registerStructure(PillagerSlimeLab.STRUCTURE_ID, () -> (new PillagerSlimeLab(NoFeatureConfig.CODEC)));
    public static final RegistryObject<Structure<NoFeatureConfig>> SLIME_DUNGEON = registerStructure(SlimeDungeon.STRUCTURE_ID, () -> (new SlimeDungeon(NoFeatureConfig.CODEC)));

    /**
     * Helper method for registering all structures
     */
    private static <T extends Structure<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return STRUCTURES.register(name, structure);
    }

    /**
     * This is where we set the rarity of your structures and determine if land conforms to it.
     * See the comments in below for more details.
     */
    public static void setupStructures() {
        setupMapSpacingAndLand(
                PILLAGER_SLIME_LAB.get(), /* The instance of the structure */
                new StructureSeparationSettings(18 /* maximum distance apart in chunks between spawn attempts */,
                        13 /* minimum distance apart in chunks between spawn attempts */,
                        671483951 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                true);
        setupMapSpacingAndLand(
                SLIME_DUNGEON.get(), /* The instance of the structure */
                new StructureSeparationSettings(6 /* average distance apart in chunks between spawn attempts */,
                        4 /* minimum distance apart in chunks between spawn attempts */,
                        768149314 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                false);
    }

    /**
     * Adds the provided structure to the registry, and adds the separation settings.
     * The rarity of the structure is determined based on the values passed into
     * this method in the structureSeparationSettings argument. Called by registerFeatures.
     */
    public static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand)
    {
        /*
         * We need to add our structures into the map in Structure alongside vanilla
         * structures or else it will cause errors. Called by registerStructure.
         *
         * If the registration is setup properly for the structure,
         * getRegistryName() should never return null.
         */
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        /*
         * Whether surrounding land will be modified automatically to conform to the bottom of the structure.
         * Basically, it adds land at the base of the structure like it does for Villages and Outposts.
         * Doesn't work well on structure that have pieces stacked vertically or change in heights.
         *
         * Note: The air space this method will create will be filled with water if the structure is below sealevel.
         * This means this is best for structure above sealevel so keep that in mind.
         */
        if(transformSurroundingLand){
            Structure.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        /*
         * Adds the structure's spacing into several places so that the structure's spacing remains
         * correct in any dimension or worldtype instead of not spawning.
         *
         * However, it seems it doesn't always work for code made dimensions as they read from
         * this list beforehand. Use the WorldEvent.Load event in StructureTutorialMain to add
         * the structure spacing from this list into that dimension.
         */
        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();
    }
}
