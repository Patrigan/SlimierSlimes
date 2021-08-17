package mod.patrigan.slimier_slimes.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class LavaSpawnerConfig implements FeatureConfiguration {
    public static final Codec<LavaSpawnerConfig> CODEC = RecordCodecBuilder.create(builder ->
        builder.group(
                RuleTest.CODEC.fieldOf("target").forGetter(config -> config.target),
                BlockState.CODEC.fieldOf("state").forGetter(config -> config.state),
                Codec.intRange(0, 64).fieldOf("size").forGetter(config -> config.size)
        ).apply(builder, LavaSpawnerConfig::new)
    );
    public final RuleTest target;
    public final int size;
    public final BlockState state;

    public LavaSpawnerConfig(RuleTest target, BlockState state, int size) {
        this.size = size;
        this.state = state;
        this.target = target;
    }
}
