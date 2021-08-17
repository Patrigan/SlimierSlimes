package mod.patrigan.slimier_slimes.network.datasync;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModDataSerializers {

    public static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, SlimierSlimes.MOD_ID);

    public static final EntityDataSerializer<List<BlockState>> BLOCK_STATE_LIST = getBlockStateListSerializer();

    private static EntityDataSerializer<List<BlockState>> getBlockStateListSerializer() {
        EntityDataSerializer<List<BlockState>> serializer = new EntityDataSerializer<List<BlockState>>() {
            public void write(FriendlyByteBuf packetBuffer, List<BlockState> blockStateList) {
                packetBuffer.writeVarIntArray(blockStateList.stream().map(Block::getId).mapToInt(x -> x).toArray());
            }

            public List<BlockState> read(FriendlyByteBuf packetBuffer) {
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
