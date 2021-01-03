package mod.patrigan.slimierslimes.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;

public class BuildingBlockHelper {

    private String blockId;
    private RegistryObject<Block> block;
    private RegistryObject<Block> slab;
    private RegistryObject<Block> stairs;
    private RegistryObject<Block> button;
    private RegistryObject<Block> pressurePlate;
    private RegistryObject<Block> wall;

    public BuildingBlockHelper(String blockId, RegistryObject<Block> block, RegistryObject<Block> slab, RegistryObject<Block> stairs, RegistryObject<Block> button, RegistryObject<Block> pressurePlate, RegistryObject<Block> wall) {
        this.blockId = blockId;
        this.block = block;
        this.slab = slab;
        this.stairs = stairs;
        this.button = button;
        this.pressurePlate = pressurePlate;
        this.wall = wall;
    }

    public String getBlockId() {
        return blockId;
    }

    public RegistryObject<Block> getBlock() {
        return block;
    }

    public RegistryObject<Block> getSlab() {
        return slab;
    }

    public RegistryObject<Block> getStairs() {
        return stairs;
    }

    public RegistryObject<Block> getButton() {
        return button;
    }

    public RegistryObject<Block> getPressurePlate() {
        return pressurePlate;
    }

    public RegistryObject<Block> getWall() {
        return wall;
    }

    public ResourceLocation getResourceLocation(){
        return new ResourceLocation(MOD_ID, blockId);
    }
}
