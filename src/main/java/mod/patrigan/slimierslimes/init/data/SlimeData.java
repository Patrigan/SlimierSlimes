package mod.patrigan.slimierslimes.init.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class SlimeData {
    public static final Codec<SlimeData> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.INT.fieldOf("maxInChunk").forGetter(data -> data.maxInChunk),
                    Codec.BOOL.fieldOf("spawnOnSurface").forGetter(data -> data.spawnOnSurface),
                    SquishParticleData.CODEC.fieldOf("squishParticleData").forGetter(data -> data.squishParticleData),
                    SlimeSpawnData.CODEC.listOf().fieldOf("slimeSpawnData").forGetter(data -> data.slimeSpawnData)
            ).apply(builder, SlimeData::new));
    //private static final Map<String, Type> byName = ReflectionHelper.getPrivateValue(BiomeDictionary.Type.class, null, "byName");

    //private final String id;
    //private final Class<? extends LivingEntity> entityClass;
    //private final EntityClassification mobType;

    //private final double maxHealthDefault;
    //private final double speedDefault;
    //private final double armorDefault;
    //private final double attackDefault;
    private final int maxInChunk;
    private final boolean spawnOnSurface;
    private final SquishParticleData squishParticleData;
    private final List<SlimeSpawnData> slimeSpawnData;
    //private final int baseEXP;


    public SlimeData(int maxInChunk, boolean spawnOnSurface, SquishParticleData squishParticleData, List<SlimeSpawnData> slimeSpawnData) {
        this.maxInChunk = maxInChunk;
        this.spawnOnSurface = spawnOnSurface;
        this.squishParticleData = squishParticleData;
        this.slimeSpawnData = slimeSpawnData;
    }

    public int getMaxInChunk() {
        return maxInChunk;
    }

    public boolean isSpawnOnSurface() {
        return spawnOnSurface;
    }

    public List<SlimeSpawnData> getSlimeSpawnData() {
        return slimeSpawnData;
    }

    public SquishParticleData getSquishParticleData() {
        return squishParticleData;
    }
}
