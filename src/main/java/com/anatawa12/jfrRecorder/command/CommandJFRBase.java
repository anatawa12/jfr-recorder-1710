package com.anatawa12.jfrRecorder.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public abstract class CommandJFRBase implements ICommand {
    @Override
    public String getCommandName() {
        return "jfr";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return CommandJFRCore.usage();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getCommandAliases() {
        return Collections.emptyList();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "config", "start", "stop");
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    public int compareTo(ICommand command) {
        return this.getCommandName().compareTo(command.getCommandName());
    }

    public int compareTo(@SuppressWarnings("NullableProblems") Object command) {
        return this.compareTo((ICommand) command);
    }
}
