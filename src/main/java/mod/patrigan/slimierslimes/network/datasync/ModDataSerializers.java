package mod.patrigan.slimierslimes.network.datasync;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModDataSerializers {

    public static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, SlimierSlimes.MOD_ID);

    public static final IDataSerializer<List<BlockState>> BLOCK_STATE_LIST = getBlockStateListSerializer();

    private static IDataSerializer<List<BlockState>> getBlockStateListSerializer() {
        IDataSerializer<List<BlockState>> serializer = new IDataSerializer<List<BlockState>>() {
            public void write(PacketBuffer packetBuffer, List<BlockState> blockStateList) {
                packetBuffer.writeVarIntArray(blockStateList.stream().map(Block::getId).mapToInt(x -> x).toArray());
            }

            public List<BlockState> read(PacketBuffer packetBuffer) {
                int[] intStream = packetBuffer.readVarIntArray();
                return Arrays.stream(intStream).boxed().map(Block::stateById).collect(Collectors.toList());
            }

            public List<BlockState> copy(List<BlockState> blockStateList) {
                return blockStateList;
            }
        };
        DATA_SERIALIZERS.register("block_state_list", () -> new DataSerializerEntry(serializer));
        return serializer;
    }
}
