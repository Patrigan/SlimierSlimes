package mod.patrigan.slimierslimes.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;

public class LavaSpawnerConfig implements IFeatureConfig {
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
