package mod.patrigan.slimierslimes.client.packet;

import com.mojang.serialization.Codec;
import mod.patrigan.slimierslimes.init.data.SlimeData;
import mod.patrigan.slimierslimes.init.data.SquishParticleData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.data.SlimeDatas.SLIME_DATA;

public class SlimeDataSyncPacket {
    private static final Codec<Map<ResourceLocation, SquishParticleData>> MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, SquishParticleData.CODEC);

    private final Map<ResourceLocation, SquishParticleData> map;

    public SlimeDataSyncPacket(Map<ResourceLocation, SquishParticleData> map) {
        this.map = map;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeNbt((CompoundNBT) (MAPPER.encodeStart(NBTDynamicOps.INSTANCE, this.map).result().orElse(new CompoundNBT())));
    }

    public static SlimeDataSyncPacket decode(PacketBuffer buffer) {
        return new SlimeDataSyncPacket(MAPPER.parse(NBTDynamicOps.INSTANCE, buffer.readNbt()).result().orElse(new HashMap<>()));
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    // Add a check on existence of the original slime data object instead of overwriting with a default one
    private void handlePacketOnMainThread() {
        
        SLIME_DATA.data = this.map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, this::getSlimeData));
    }

    private SlimeData getSlimeData(Map.Entry<ResourceLocation, SquishParticleData> entry) {
        SlimeData data = SLIME_DATA.getData(entry.getKey());
        if(data != null) {
            data.setSquishParticleData(entry.getValue());
            return data;
        }else{
            return new SlimeData(entry.getValue());

        }
    }

}
