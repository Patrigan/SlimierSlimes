package mod.patrigan.slimier_slimes.init.data;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.client.packet.SlimeDataSyncDTO;
import mod.patrigan.slimier_slimes.client.packet.SlimeDataSyncPacket;
import mod.patrigan.slimier_slimes.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.stream.Collectors;

public class SlimeDatas {
    public static final CodecJsonDataManager<SlimeData> SLIME_DATA = new CodecJsonDataManager<>("slime_data", SlimeData.CODEC, SlimierSlimes.LOGGER);

    public static SlimeDataSyncPacket toPacket(Map<ResourceLocation, SlimeData> map){
        Map<ResourceLocation, SlimeDataSyncDTO> packetMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toSlimeDataSyncDTO()));
        return new SlimeDataSyncPacket(packetMap);
    }
}
