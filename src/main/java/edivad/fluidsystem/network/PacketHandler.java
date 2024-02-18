package edivad.fluidsystem.network;

import edivad.edivadlib.network.BasePacketHandler;
import edivad.edivadlib.network.EdivadLibPacket;
import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.network.packet.UpdateControllerTankBlock;
import edivad.fluidsystem.network.packet.UpdateFilterablePipeBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;

public class PacketHandler extends BasePacketHandler {

  public PacketHandler(IEventBus modEventBus) {
    super(modEventBus, FluidSystem.ID, "1");
  }

  @Override
  protected void registerServerToClient(PacketRegistrar registrar) {
    registrar.play(UpdateControllerTankBlock.ID, UpdateControllerTankBlock::read);
    registrar.play(UpdateFilterablePipeBlock.ID, UpdateFilterablePipeBlock::read);
  }

  public static void sendToAll(EdivadLibPacket packet) {
    PacketDistributor.ALL.noArg().send(packet);
  }
}
