package net.arsenalnetwork.anticheat.proxy;

import net.arsenalnetwork.anticheat.configs.WatchDogServerConfig;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends CommonProxy
{
    public static WatchDogServerConfig watchDogServerConfig;
    @Override
    public void preInit(FMLPreInitializationEvent e)
    {
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e)
    {
        super.init(e);
        watchDogServerConfig = new WatchDogServerConfig();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e)
    {
        super.postInit(e);
    }
}
