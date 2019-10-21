package net.arsenalnetwork.anticheat.handlers;

import net.arsenalnetwork.anticheat.AntiCheat;
import net.arsenalnetwork.anticheat.packets.AntiCheat_Packet;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

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
        if(mc.theWorld == null) {
            hasSentModsList = false;
        }

        if(!hasSentModsList && mc.theWorld != null && mc.getIntegratedServer() == null && !mc.isSingleplayer()) {
            AntiCheat.INSTANCE.sendToServer(new AntiCheat_Packet(0, AntiCheatHandler.getModMD5s()));
            hasSentModsList = true;
        }
    }
}
