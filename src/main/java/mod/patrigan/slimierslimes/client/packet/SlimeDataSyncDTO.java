package mod.patrigan.slimierslimes.client.packet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.slimierslimes.init.data.SquishParticleData;
import net.minecraft.item.DyeColor;

import static mod.patrigan.slimierslimes.util.ColorUtils.DYE_COLOR_CODEC;

public class SlimeDataSyncDTO {
    public static final Codec<SlimeDataSyncDTO> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    DYE_COLOR_CODEC.fieldOf("dyeColor").forGetter(processor -> processor.dyeColor),
                    SquishParticleData.CODEC.fieldOf("squishParticleData").forGetter(data -> data.squishParticleData)
            ).apply(builder, SlimeDataSyncDTO::new));
    private final DyeColor dyeColor;
    private final SquishParticleData squishParticleData;

    public SlimeDataSyncDTO(DyeColor dyeColor, SquishParticleData squishParticleData) {
        this.dyeColor = dyeColor;
        this.squishParticleData = squishParticleData;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public SquishParticleData getSquishParticleData() {
        return squishParticleData;
    }
}
