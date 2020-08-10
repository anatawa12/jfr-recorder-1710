package com.anatawa12.jfrRecorder.command;

import com.anatawa12.jfrRecorder.Hooks;
import com.anatawa12.jfrRecorder.JFRRecorder;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandJFRClient extends CommandJFRBase {
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!Hooks.getServerModList().containsKey(JFRRecorder.MODID))
            throw new WrongUsageException("your server does not installed JFR.");
        if (args.length < 1)
            throw new WrongUsageException(CommandJFRCore.usage());
        switch (args[0]) {
            case "config":
                if (args.length < 2)
                    throw new WrongUsageException(CommandJFRCore.configUsage());
                String configContent = CommandJFRCore.preRunConfig(sender, args[1]);
                if (configContent == null) return;
                JFRRecorder.NETWORK.sendToServer(new RequestSetConfig(configContent));
                return;
            case "start":
                JFRRecorder.NETWORK.sendToServer(new RequestStart());
                return;
            case "stop":
                JFRRecorder.NETWORK.sendToServer(new RequestStop());
                return;
            default:
                throw new WrongUsageException(CommandJFRCore.usage());
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
