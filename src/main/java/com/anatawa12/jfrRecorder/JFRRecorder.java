package com.anatawa12.jfrRecorder;

import com.anatawa12.jfrRecorder.command.CommandJFRClient;
import com.anatawa12.jfrRecorder.command.CommandJFRCore;
import com.anatawa12.jfrRecorder.command.CommandJFRServer;
import com.anatawa12.jfrRecorder.command.RequestSetConfig;
import com.anatawa12.jfrRecorder.command.RequestStart;
import com.anatawa12.jfrRecorder.command.RequestStop;
import com.anatawa12.jfrRecorder.command.ResponseStop;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.Map;

@Mod(modid = JFRRecorder.MODID)
public class JFRRecorder {
    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandJFRServer());
        try {
            Class.forName("jdk.jfr.Configuration");
            CommandJFRCore.env = new SupportedJvmEnvironmentImpl();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            CommandJFRCore.env = new UnsupportedJvmEnvironmentImpl();
        }
        if (CommandJFRCore.env.isSupported()) {
            e.getServer().addChatMessage(new ChatComponentText("the server supports JFR"));
        } else {
            e.getServer().addChatMessage(new ChatComponentText("the server does not supports JFR"));
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.player instanceof EntityPlayerMP) {
            if (Hooks.modListOf((EntityPlayerMP) e.player).containsKey(MODID)) {
                if (CommandJFRCore.env.isSupported()) {
                    e.player.addChatMessage(new ChatComponentText("the server supports JFR"));
                } else {
                    e.player.addChatMessage(new ChatComponentText("the server does not supports JFR"));
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent e) {
        ClientCommandHandler.instance.registerCommand(new CommandJFRClient());

        FMLCommonHandler.instance().bus().register(this);

        NETWORK.registerMessage(RequestSetConfig.HANDLER, RequestSetConfig.class, 0, Side.SERVER);
        NETWORK.registerMessage(RequestStart.HANDLER, RequestStart.class, 1, Side.SERVER);
        NETWORK.registerMessage(RequestStop.HANDLER, RequestStop.class, 2, Side.SERVER);
        NETWORK.registerMessage(ResponseStop.HANDLER, ResponseStop.class, 3, Side.CLIENT);
    }

    @SuppressWarnings("unused")
    @NetworkCheckHandler
    public boolean networkHandler(Map<String, String> map, Side side) {
        return true;
    }

    public static final String MODID = "jfr-recorder";

    public static SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("AegisSystemMod");
}
