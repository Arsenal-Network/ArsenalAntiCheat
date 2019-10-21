package net.arsenalnetwork.anticheat;

import net.arsenalnetwork.anticheat.handlers.AntiCheatHandler;
import net.arsenalnetwork.anticheat.handlers.TickHandler;
import net.arsenalnetwork.anticheat.packets.AntiCheat_Packet;
import net.arsenalnetwork.anticheat.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.logging.Logger;

@Mod(modid = AntiCheat.MODID, name = AntiCheat.MODNAME, version = AntiCheat.VERSION)
public class AntiCheat {

    public static final String MODID = "watchdog";
    public static final String MODNAME = "WatchDog Anti-Cheat";
    public static final String VERSION = "1.7.10 - 1.0.0";

    public static final String ANTICHEATNAME = "WatchDog Anti-Cheat";
    public static final String ANTICHEAT_SHORTNAME = "WAC";

    private static Logger logger;

    private static TickHandler tickHandler;

    @Mod.Instance(MODID)
    public static AntiCheat instance;

    @SidedProxy(clientSide="net.arsenalnetwork.anitcheat.proxy.ClientProxy", serverSide="net.arsenalnetwork.anitcheat.proxy.ServerProxy")
    public static CommonProxy proxy;

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("phoenixx");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
        // Register packets

        INSTANCE.registerMessage(AntiCheat_Packet.Handler.class, AntiCheat_Packet.class, 0, Side.SERVER);
        System.out.println("Packets initialized");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);

        Side side = FMLCommonHandler.instance().getEffectiveSide();

        tickHandler = new TickHandler();
        FMLCommonHandler.instance().bus().register(tickHandler);

        FMLCommonHandler.instance().bus().register(instance);
        MinecraftForge.EVENT_BUS.register(instance);

        logger = Logger.getLogger("WatchDog");
        logger.info("[MaxIsH0t] Pssttt....I like easter eggs, don't you?");
        logger.info("[MaxIsH0t] Contact me on discord at Phoenix#5518 or on twitter @Golden4Phoenix, if any issues occur :)");

        if (side.isClient())
        {
            clientSide();

            System.out.println(AntiCheatHandler.getModIDs());
            System.out.println(AntiCheatHandler.getTexturePacks());
        }
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
        System.out.println( AntiCheat.MODNAME + " | Server Started");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
        proxy.postInit(e);

    }

    @SideOnly(Side.CLIENT)
    private void clientSide()
    {
        File configDirectory = new File(Loader.instance().getConfigDir(), "/WatchDog/");
        String loader = FMLCommonHandler.instance().getModName();

        if ((loader.contains("lite")) || (loader.contains("liteloader")))
        {
            System.out.println("[" + ANTICHEATNAME + " Anti-Cheat] Detected Lite-Loader | Shutting down...");
            FMLCommonHandler.instance().exitJava(0,true);
        }

        if(!configDirectory.exists())
        {
            configDirectory.mkdir();
            System.out.println(ANTICHEATNAME+" Anti-Cheat Config directory does not exist! Creating one now.");
        }
    }
}