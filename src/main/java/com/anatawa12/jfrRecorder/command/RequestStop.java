package com.anatawa12.jfrRecorder.command;

import com.anatawa12.jfrRecorder.JFRRecorder;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandException;

public class RequestStop implements IMessage {
    public RequestStop() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static final IMessageHandler<RequestStop, IMessage> HANDLER = (message, ctx) -> {
        try {
            byte[] data = CommandJFRCore.runStop(ctx.getServerHandler().playerEntity);
            if (data == null) return null;
            for (int start = 0; start < data.length; start += 32700) {
                int length = Math.min(32700, data.length - start);
                JFRRecorder.NETWORK.sendTo(new ResponseStop(data, start, length), ctx.getServerHandler().playerEntity);
            }
        } catch (CommandException e) {
            ctx.getServerHandler().playerEntity.addChatComponentMessage(CommandJFRCore.error(e.getMessage()));
        }
        return null;
    };
}
