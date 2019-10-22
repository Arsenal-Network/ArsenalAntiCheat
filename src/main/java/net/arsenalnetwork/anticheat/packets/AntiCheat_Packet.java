package net.arsenalnetwork.anticheat.packets;

import net.arsenalnetwork.anticheat.AntiCheat;
import net.arsenalnetwork.anticheat.configs.WatchDogServerConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class AntiCheat_Packet implements IMessage {

    private int messageID;
    private static String data;
    private static ArrayList<String> clientList;

    public AntiCheat_Packet() {}

    public AntiCheat_Packet(int number) {
        this.messageID = number;
        this.data = "NONE";
    }

    public AntiCheat_Packet(int number, String modList) {
        this.messageID = number;
        data = modList;
        System.out.println("List: " + modList);
    }

    public void fromBytes(ByteBuf buf) {
        this.messageID = buf.readInt();
        data = ByteBufUtils.readUTF8String(buf);
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.messageID);
        ByteBufUtils.writeUTF8String(buf, data);
    }

    public static class Handler implements IMessageHandler<AntiCheat_Packet, IMessage> {

        @Override
        public IMessage onMessage(AntiCheat_Packet message, MessageContext ctx) {
            if(ctx != null) {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if(player != null) {
                    String playerName = player.getDisplayName().toString();
                    if(WatchDogServerConfig.useAntiCheat) {
                        // Mods
                        if(message.messageID == 0) {
                            if(!data.equals("NONE")) {
                                clientList = new ArrayList<String>(Arrays.asList(data.split(",")));
                                Collection<String> extraMods = getExtraBadItems(clientList);
                                if(userHasExtraBadItems(clientList, true)) {
                                    if(!canUserConnectWithExtraBadItems(playerName)) {
                                        ctx.getServerHandler().disconnect(new TextComponentString(TextFormatting.GREEN + TextFormatting.BOLD.toString() + "[" + AntiCheat.ANTICHEATNAME + "]\n" + TextFormatting.RESET  +"You have been kicked.\n" + TextFormatting.RED + "You have mods that this server does not support! Please remove them before connecting again.\nExtra mods: " + extraMods));
                                        WatchDogServerConfig.addToCheaterList(playerName + " kicked due to extra mods: " + extraMods);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
    }

    private static boolean userHasExtraBadItems(ArrayList<String> badItems, boolean checkMods) {
        if(checkMods){
            ArrayList<String> serverSideMods = WatchDogServerConfig.getWhitelistedMods();
            Collection<String> listOne = new ArrayList<>(serverSideMods);
            Collection<String> listTwo = new ArrayList<>(badItems);
            listTwo.removeAll(listOne);
            if(listTwo.isEmpty()) {
                return false;
            }
        } else {
            Collection<String> listTwo = new ArrayList<>(badItems);
            if(listTwo.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static Collection<String> getExtraBadItems(ArrayList<String> clientMods) {
        ArrayList<String> serverSideMods = WatchDogServerConfig.getWhitelistedMods();
        Collection<String> listOne = new ArrayList<>(serverSideMods);
        Collection<String> listTwo = new ArrayList<>(clientMods);
        listTwo.removeAll(listOne);
        return listTwo;
    }

    private static boolean canUserConnectWithExtraBadItems(String username) {
        ArrayList<String> playerModWhitelist = WatchDogServerConfig.getModWhitelistedPlayers();
        return playerModWhitelist.contains(username);
    }
}