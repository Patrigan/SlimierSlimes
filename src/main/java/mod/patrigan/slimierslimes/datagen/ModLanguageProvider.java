package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

import static mod.patrigan.slimierslimes.init.ModBlocks.BLOCK_IDS;
import static mod.patrigan.slimierslimes.init.ModEntityTypes.ENTITY_IDS;
import static mod.patrigan.slimierslimes.init.ModEntityTypes.SPAWN_EGGS;
import static mod.patrigan.slimierslimes.init.ModItems.ITEM_IDS;
import static net.minecraftforge.registries.ForgeRegistries.*;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen, SlimierSlimes.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        BLOCK_IDS.forEach(this::addBlock);
        ITEM_IDS.forEach(this::addItem);
        ENTITY_IDS.forEach(this::addEntity);
        add("itemGroup.SlimierSlimesTab", "Slimier Slimes");
    }

    private void addBlock(String blockId) {
        Block block = BLOCKS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, blockId));
        add(block, getNameFromId(blockId));
    }

    private void addItem(String itemId) {
        Item item = ITEMS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, itemId));
        add(item, getNameFromId(itemId));
    }

    private void addEntity(String entityId) {
        EntityType<?> entityType = ENTITIES.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, entityId));
        add(entityType, getNameFromId(entityId));
        String entitySpawnEggItem = entityId + "_spawn_egg";
        Item spawnEgg = ITEMS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, entitySpawnEggItem));
        add(spawnEgg, getNameFromId(entitySpawnEggItem));
    }

    private String getNameFromId(String idString) {
        StringBuilder sb = new StringBuilder();
        for(String word : idString.toLowerCase().split("_") )
        {
            sb.append(word.substring(0,1).toUpperCase() );
            sb.append(word.substring(1) );
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}
