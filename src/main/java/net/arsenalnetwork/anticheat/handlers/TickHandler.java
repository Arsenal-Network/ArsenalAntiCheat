package net.arsenalnetwork.anticheat.handlers;

import net.arsenalnetwork.anticheat.AntiCheat;
import net.arsenalnetwork.anticheat.packets.AntiCheat_Packet;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TickHandler {

    @SideOnly(Side.CLIENT)
    Minecraft mc;

    public static boolean hasSentModsList = false;

    public TickHandler() {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side.isClient()) {
            this.mc = FMLClientHandler.instance().getClient();
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void clientTick(TickEvent.ClientTickEvent event) {
        if(mc.world == null) {
            hasSentModsList = false;
        }

        if(!hasSentModsList && mc.world != null && mc.getIntegratedServer() == null && !mc.isSingleplayer()) {
            AntiCheat.INSTANCE.sendToServer(new AntiCheat_Packet(0, AntiCheatHandler.getModMD5s()));
            hasSentModsList = true;
        }
    }
}
