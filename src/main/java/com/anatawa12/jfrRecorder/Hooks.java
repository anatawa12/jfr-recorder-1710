package com.anatawa12.jfrRecorder;

import cpw.mods.fml.common.network.handshake.FMLHandshakeMessage;
import cpw.mods.fml.common.network.handshake.NetworkDispatcher;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Map;

public class Hooks {
    private static Map<String, String> serverModList;

    private Hooks() {
    }

    public static Map<String, String> modListOf(EntityPlayerMP player) {
        throw new AssertionError("implemented by class transformer");
    }

    private static void setModListOf(EntityPlayerMP player, Map<String, String> modList) {
        throw new AssertionError("implemented by class transformer");
    }

    public static EntityPlayerMP playerOf(NetworkDispatcher player) {
        throw new AssertionError("implemented by class transformer");
    }

    @SuppressWarnings("unused")
    public static void onAcceptOnHelloStateOnHandshakeOnServer(ChannelHandlerContext ctx, FMLHandshakeMessage msg) {
        if (!(msg instanceof FMLHandshakeMessage.ModList)) {
            return;
        }
        FMLHandshakeMessage.ModList list = (FMLHandshakeMessage.ModList) msg;
        NetworkDispatcher dispatcher = ctx.channel().attr(NetworkDispatcher.FML_DISPATCHER).get();
        EntityPlayerMP player = playerOf(dispatcher);
        setModListOf(player, list.modList());
    }

    public static Map<String, String> getServerModList() {
        if (serverModList == null) throw new IllegalStateException("you aren't on server");
        return serverModList;
    }

    @SuppressWarnings("unused")
    public static void onAcceptOnWaitingServerDataStateOnHandshakeOnClient(ChannelHandlerContext ctx, FMLHandshakeMessage msg) {
        FMLHandshakeMessage.ModList list = (FMLHandshakeMessage.ModList) msg;
        serverModList = list.modList();
    }
}
