package mod.patrigan.slimierslimes.init.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.minecraftforge.common.BiomeDictionary.Type.getType;

public class SlimeSpawnData {
    public static final Codec<SlimeSpawnData> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.INT.fieldOf("minPackSize").forGetter(data -> data.minPackSize),
                    Codec.INT.fieldOf("maxPackSize").forGetter(data -> data.maxPackSize),
                    Codec.INT.fieldOf("spawnWeight").forGetter(data -> data.spawnWeight),
                    Codec.STRING.listOf().fieldOf("biomes").forGetter(data -> data.biomes)
            ).apply(builder, SlimeSpawnData::new));

    private final int minPackSize;
    private final int maxPackSize;
    private final int spawnWeight;
    private final List<String> biomes;
    private final List<ResourceLocation> biomeResourceLocations = new ArrayList<>();
    private final List<BiomeDictionary.Type> biomeTypes = new ArrayList<>();
    private final List<BiomeDictionary.Type> biomeTypesToExclude = new ArrayList<>();

    public SlimeSpawnData(int minPackSize, int maxPackSize, int spawnWeight, List<String> biomes) {
        this.minPackSize = minPackSize;
        this.maxPackSize = maxPackSize;
        this.spawnWeight = spawnWeight;
        this.biomes = biomes;
        biomes.forEach(biomeString -> {
            if(biomeString.contains(":")){
                biomeResourceLocations.add(new ResourceLocation(biomeString));
            }else{
                if(biomeString.contains("!")){
                    biomeTypesToExclude.add(getType(biomeString.substring(1)));
                }else{
                    biomeTypes.add(getType(biomeString));
                }
            }
        });
    }

    public boolean isMatch(BiomeLoadingEvent event){
        RegistryKey<Biome> key = RegistryKey.create(Registry.BIOME_REGISTRY, event.getName());
        final Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);
        return biomeResourceLocations.contains(event.getName()) ||
                (types.containsAll(biomeTypes) &&
                        biomeTypesToExclude.stream().noneMatch(types::contains));
    }

    public MobSpawnInfo.Spawners getSpawner(EntityType<? extends Entity> type){
        return new MobSpawnInfo.Spawners(type, spawnWeight, minPackSize, maxPackSize);
    }
}
