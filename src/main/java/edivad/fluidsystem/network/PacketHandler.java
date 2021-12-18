package edivad.fluidsystem.network;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.network.packet.UpdateControllerTankBlock;
import edivad.fluidsystem.network.packet.UpdateFilterablePipeBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Main.MODID, "net"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(id++, UpdateControllerTankBlock.class, UpdateControllerTankBlock::toBytes, UpdateControllerTankBlock::new, UpdateControllerTankBlock::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, UpdateFilterablePipeBlock.class, UpdateFilterablePipeBlock::toBytes, UpdateFilterablePipeBlock::new, UpdateFilterablePipeBlock::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

}
