package mod.patrigan.slimier_slimes.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static mod.patrigan.slimier_slimes.init.ModEntityTypes.SPAWNER_ENTITY_TYPES;

public class SlimeSpawnerFeature extends Feature<NoneFeatureConfiguration> {
    public SlimeSpawnerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        BlockState blockState = Blocks.SPAWNER.defaultBlockState();
        featurePlaceContext.level().setBlock(featurePlaceContext.origin(), blockState, 2);
        BlockEntity blockEntity = featurePlaceContext.level().getBlockEntity(featurePlaceContext.origin());
        ((SpawnerBlockEntity)blockEntity).getSpawner().setEntityId(
                    SPAWNER_ENTITY_TYPES.get(featurePlaceContext.random().nextInt(SPAWNER_ENTITY_TYPES.size())));
        return true;
    }
}