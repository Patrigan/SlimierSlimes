package mod.patrigan.slimier_slimes.client.packet;

import com.mojang.serialization.Codec;
import mod.patrigan.slimier_slimes.init.data.SlimeData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static mod.patrigan.slimier_slimes.init.data.SlimeDatas.SLIME_DATA;

public class SlimeDataSyncPacket {
    private static final Codec<Map<ResourceLocation, SlimeDataSyncDTO>> MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, SlimeDataSyncDTO.CODEC);

    private final Map<ResourceLocation, SlimeDataSyncDTO> map;

    public SlimeDataSyncPacket(Map<ResourceLocation, SlimeDataSyncDTO> map) {
        this.map = map;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt((CompoundTag) (MAPPER.encodeStart(NbtOps.INSTANCE, this.map).result().orElse(new CompoundTag())));
    }

    public static SlimeDataSyncPacket decode(FriendlyByteBuf buffer) {
        return new SlimeDataSyncPacket(MAPPER.parse(NbtOps.INSTANCE, buffer.readNbt()).result().orElse(new HashMap<>()));
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

    private SlimeData getSlimeData(Map.Entry<ResourceLocation, SlimeDataSyncDTO> entry) {
        SlimeData data = SLIME_DATA.getData(entry.getKey());
        if(data != null) {
            data.setSquishParticleData(entry.getValue().getSquishParticleData());
            data.setDyeColor(entry.getValue().getDyeColor());
            return data;
        }else{
            return new SlimeData(entry.getValue());

        }
    }

}
