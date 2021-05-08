package mod.patrigan.slimierslimes.network.datasync;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModDataSerializers {
    public static final IDataSerializer<List<BlockState>> BLOCK_STATE_LIST = new IDataSerializer<List<BlockState>>() {
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
}
