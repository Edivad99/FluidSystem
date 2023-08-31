package edivad.fluidsystem.network;

import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.network.packet.UpdateControllerTankBlock;
import edivad.fluidsystem.network.packet.UpdateFilterablePipeBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

  private static final String PROTOCOL_VERSION = "1";
  public static SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
      .named(new ResourceLocation(FluidSystem.ID, "network"))
      .clientAcceptedVersions(PROTOCOL_VERSION::equals)
      .serverAcceptedVersions(PROTOCOL_VERSION::equals)
      .networkProtocolVersion(() -> PROTOCOL_VERSION)
      .simpleChannel();


  public static void init() {
    INSTANCE
        .messageBuilder(UpdateControllerTankBlock.class, 0, NetworkDirection.PLAY_TO_CLIENT)
        .encoder(UpdateControllerTankBlock::encode)
        .decoder(UpdateControllerTankBlock::decode)
        .consumerMainThread(UpdateControllerTankBlock::handle)
        .add();
    INSTANCE
        .messageBuilder(UpdateFilterablePipeBlock.class, 1, NetworkDirection.PLAY_TO_CLIENT)
        .encoder(UpdateFilterablePipeBlock::encode)
        .decoder(UpdateFilterablePipeBlock::decode)
        .consumerMainThread(UpdateFilterablePipeBlock::handle)
        .add();
  }
}
