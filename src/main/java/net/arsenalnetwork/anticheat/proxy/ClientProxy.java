package net.arsenalnetwork.anticheat.proxy;

import net.arsenalnetwork.anticheat.configs.WatchDogConfig;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{

    public static WatchDogConfig watchDogConfig;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        watchDogConfig = new WatchDogConfig();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);

    }

}