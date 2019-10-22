package net.arsenalnetwork.anticheat.handlers;

import net.arsenalnetwork.anticheat.configs.WatchDogConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class AntiCheatHandler
{
    private static Minecraft mc = Minecraft.getMinecraft();

    /**
     * Gets all the mods hashmaps (MD5)
     * @return
     */
    public static String getModMD5s() {
        ArrayList<String> modMD5s = new ArrayList<String>();
        List<ModContainer> mods = Loader.instance().getModList();

        for (ModContainer modContainer : mods) {
            String name = modContainer.getName();
            String modID = getMD5Hash(modContainer.getModId());
            modMD5s.add(modID);
            if (WatchDogConfig.debugClient) {
                WatchDogConfig.addToModList(name, modID);
            }
        }
        String allModsIDsToString = String.join(",", modMD5s);

        if(WatchDogConfig.debugClient) {
            WatchDogConfig.addEntireListToModFile(allModsIDsToString);
        }
        return allModsIDsToString;
    }

    /**
     * Returns a hexadecimal encoded MD5 hash for the input String.
     * @param data
     * @return
     */
    private static String getMD5Hash(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Use javax.xml.bind.DatatypeConverter class in JDK to convert byte array
     * to a hexadecimal string. Note that this generates hexadecimal in upper case.
     * @param hash
     * @return
     */
    private static String bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
}