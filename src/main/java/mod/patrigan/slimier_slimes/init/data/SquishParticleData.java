package mod.patrigan.slimier_slimes.init.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public class SquishParticleData {
    public static final Codec<SquishParticleData> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.STRING.fieldOf("typeString").forGetter(data -> data.typeString),
                    ResourceLocation.CODEC.fieldOf("resourceLocation").forGetter(data -> data.resourceLocation)
            ).apply(builder, SquishParticleData::new));

    public enum SquishParticleType{
        ITEM,
        BLOCK,
        PARTICLE
    }
    private final String typeString;
    private final SquishParticleType type;
    private final ResourceLocation resourceLocation;

    public SquishParticleData(String typeString, ResourceLocation resourceLocation) {
        this.typeString = typeString;
        this.type = SquishParticleType.valueOf(typeString);
        this.resourceLocation = resourceLocation;
    }

    public SquishParticleType getType() {
        return type;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }
}
