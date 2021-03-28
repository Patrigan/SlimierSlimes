package mod.patrigan.slimierslimes.init.data;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.client.packet.SlimeDataSyncPacket;
import mod.patrigan.slimierslimes.data.CodecJsonDataManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.stream.Collectors;

public class SlimeDatas {
    public static final CodecJsonDataManager<SlimeData> SLIME_DATA = new CodecJsonDataManager<>("slime_data", SlimeData.CODEC, SlimierSlimes.LOGGER);

    public static SlimeDataSyncPacket toPacket(Map<ResourceLocation, SlimeData> map){
        Map<ResourceLocation, SquishParticleData> packetMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getSquishParticleData()));
        return new SlimeDataSyncPacket(packetMap);
    }
}
