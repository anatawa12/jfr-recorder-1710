package com.anatawa12.jfrRecorder.command;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;

public class RequestStart implements IMessage {
    public RequestStart() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static final IMessageHandler<RequestStart, IMessage> HANDLER = (message, ctx) -> {
        CommandJFRCore.runStart(ctx.getServerHandler().playerEntity);
        return null;
    };
}
