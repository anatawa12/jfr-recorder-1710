package com.anatawa12.jfrRecorder.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CommandJFRServer extends CommandJFRBase {
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        //noinspection ResultOfMethodCallIgnored
        ((MinecraftServer) sender).getClass();

        if (args.length < 1)
            throw new WrongUsageException(CommandJFRCore.usage());
        switch (args[0]) {
            case "config":
                if (args.length < 2)
                    throw new WrongUsageException(CommandJFRCore.configUsage());
                String configContent = CommandJFRCore.preRunConfig(sender, args[1]);
                if (configContent == null) return;
                CommandJFRCore.runConfig(sender, configContent);
                return;
            case "start":
                CommandJFRCore.runStart(sender);
                return;
            case "stop":
                byte[] data = CommandJFRCore.runStop(sender);
                if (data == null) return;
                CommandJFRCore.runStop2(sender, data);
                return;
            default:
                throw new WrongUsageException(CommandJFRCore.usage());
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof MinecraftServer;
    }
}
