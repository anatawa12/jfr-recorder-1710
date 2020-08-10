package com.anatawa12.jfrRecorder.command;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import java.io.ByteArrayOutputStream;

public class ResponseStop implements IMessage {
    /**
     * bit 0x01: is end packet
     * bit 0x02: is start packet
     */
    private int flags;
    private byte[] data;
    private int start;
    private int length;

    public ResponseStop() {
    }

    public ResponseStop(byte[] data, int start, int length) {
        this.flags = 0;
        if (data.length == start + length)
            flags |= 0x01;
        if (start == 0)
            flags |= 0x02;
        this.data = data;
        this.start = start;
        this.length = length;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        flags = buf.readByte();
        data = buf.readBytes(buf.readableBytes()).array();
        start = 0;
        length = data.length;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(flags);
        buf.writeBytes(
                data,
                start,
                length
        );
    }

    private static ByteArrayOutputStream stream;
    public static final IMessageHandler<ResponseStop, IMessage> HANDLER = (message, ctx) -> {
        if ((message.flags & 0x02) != 0)
            stream = new ByteArrayOutputStream();

        stream.write(message.data, message.start, message.length);

        if ((message.flags & 0x01) != 0) {
            CommandJFRCore.runStop2(Minecraft.getMinecraft().thePlayer, stream.toByteArray());
        }
        return null;
    };
}
