package mod.patrigan.slimier_slimes.blocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import static mod.patrigan.slimier_slimes.SlimierSlimes.MOD_ID;

public class BuildingBlockHelper {

    private String blockId;
    private DyeColor dyeColor;
    private RegistryObject<Block> block;
    private RegistryObject<Block> slab;
    private RegistryObject<Block> stairs;
    private RegistryObject<Block> button;
    private RegistryObject<Block> pressurePlate;
    private RegistryObject<Block> wall;
    private boolean slimy;
    private boolean translucent;

    public BuildingBlockHelper(String blockId, DyeColor dyeColor, RegistryObject<Block> block, RegistryObject<Block> slab, RegistryObject<Block> stairs, RegistryObject<Block> button, RegistryObject<Block> pressurePlate, RegistryObject<Block> wall, boolean slimy, boolean translucent) {
        this.blockId = blockId;
        this.dyeColor = dyeColor;
        this.block = block;
        this.slab = slab;
        this.stairs = stairs;
        this.button = button;
        this.pressurePlate = pressurePlate;
        this.wall = wall;
        this.slimy = slimy;
        this.translucent = translucent;
    }

    public String getBlockId() {
        return blockId;
    }

    public String getId() {
        if(dyeColor == null) {
            return blockId;
        }else{
            return dyeColor + "_" + blockId;
        }
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

    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public boolean isSlimy() {
        return slimy;
    }

    public boolean isTranslucent() {
        return translucent;
    }

    public static class Builder {

        private String blockId;
        private DyeColor dyeColor;
        private RegistryObject<Block> block;
        private RegistryObject<Block> slab;
        private RegistryObject<Block> stairs;
        private RegistryObject<Block> button;
        private RegistryObject<Block> pressurePlate;
        private RegistryObject<Block> wall;
        private boolean slimy;
        private boolean translucent;

        public Builder withBlockId(String blockId) {
            this.blockId = blockId;
            return this;
        }

        public Builder withDyeColor(DyeColor dyeColor) {
            this.dyeColor = dyeColor;
            return this;
        }

        public Builder withBlock(RegistryObject<Block> block) {
            this.block = block;
            return this;
        }

        public Builder withSlab(RegistryObject<Block> slab) {
            this.slab = slab;
            return this;
        }

        public Builder withStairs(RegistryObject<Block> stairs) {
            this.stairs = stairs;
            return this;
        }

        public Builder withButton(RegistryObject<Block> button) {
            this.button = button;
            return this;
        }

        public Builder withPressurePlate(RegistryObject<Block> pressurePlate) {
            this.pressurePlate = pressurePlate;
            return this;
        }

        public Builder withWall(RegistryObject<Block> wall) {
            this.wall = wall;
            return this;
        }

        public Builder withSlimy(boolean slimy) {
            this.slimy = slimy;
            return this;
        }

        public BuildingBlockHelper createBuildingBlockHelper() {
            return new BuildingBlockHelper(blockId, dyeColor, block, slab, stairs, button, pressurePlate, wall, slimy, translucent);
        }

        public Builder withTranslucent(boolean translucent) {
            this.translucent = translucent;
            return this;
        }
    }


}
