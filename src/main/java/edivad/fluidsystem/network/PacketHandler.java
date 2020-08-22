package edivad.fluidsystem.network;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.network.packet.UpdateBlockFilterablePipe;
import edivad.fluidsystem.network.packet.UpdateTankBlockController;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class PacketHandler
{

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Main.MODID, "net"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void init()
    {
        int id = 0;
        INSTANCE.registerMessage(id++, UpdateTankBlockController.class, UpdateTankBlockController::toBytes, UpdateTankBlockController::new, UpdateTankBlockController::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, UpdateBlockFilterablePipe.class, UpdateBlockFilterablePipe::toBytes, UpdateBlockFilterablePipe::new, UpdateBlockFilterablePipe::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

}
