package com.anatawa12.jfrRecorder.command;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class RequestSetConfig implements IMessage {
    private String fileContent;

    public RequestSetConfig() {
    }

    public RequestSetConfig(String fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        fileContent = new String(buf.readBytes(buf.readableBytes()).array(), StandardCharsets.UTF_8);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBytes(fileContent.getBytes(StandardCharsets.UTF_8));
    }

    public static final IMessageHandler<RequestSetConfig, IMessage> HANDLER = (message, ctx) -> {
        CommandJFRCore.runConfig(ctx.getServerHandler().playerEntity, message.fileContent);
        return null;
    };
}
