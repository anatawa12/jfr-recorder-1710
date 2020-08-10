package com.anatawa12.jfrRecorder.command;

import com.anatawa12.jfrRecorder.IJvmEnvironment;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandJFRCore {
    public static String usage() {
        return "/jfr [config|start|stop] <arguments>";
    }

    public static String configUsage() {
        return "/jfr config <config file path> or /jfr config [default|profile]";
    }

    /**
     * @return file contents
     */
    public static String preRunConfig(ICommandSender sender, String filePath) {
        if (filePath.equals("default")) return filePath;
        if (filePath.equals("profile")) return filePath;
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();

            sender.addChatMessage(error("unexpected io occurred during reading file"));
            return null;
        }
        if (encoded.length >= 32700) {
            sender.addChatMessage(error("your file is too big so cannot be used"));
            return null;
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public static void runConfig(ICommandSender sender, String configFile) {
        if (!checkPermission(sender)) return;
        try {
            env.onConfig(configFile);
        } catch (ParseException e) {
            new BufferedReader(new StringReader(e.getMessage())).lines()
                    .forEach(line -> sender.addChatMessage(new ChatComponentText(line)));
            e.printStackTrace();
        }
        sender.addChatMessage(new ChatComponentText("updated config to " + configFile));
    }

    public static void runStart(ICommandSender sender) {
        if (!checkPermission(sender)) return;
        env.onStart("record start at " + new Date());
        sender.addChatMessage(new ChatComponentText("record started!"));
    }

    public static byte[] runStop(ICommandSender sender) {
        if (!checkPermission(sender)) return null;
        return env.onStop();
    }

    public static void runStop2(ICommandSender sender, byte[] file) {
        File file1 = new File("jfr-reports");
        //noinspection ResultOfMethodCallIgnored
        file1.mkdirs();
        File file2 = new File(file1, "report-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".jfr");
        try {
            Files.write(file2.toPath(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sender.addChatMessage(new ChatComponentText("record was saved to " + file2.getName() + "!"));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean checkPermission(ICommandSender sender) {
        boolean permission = sender.canCommandSenderUseCommand(3, "jfr");
        if (!permission) {
            sender.addChatMessage(new ChatComponentText("you does not have much permission to run jfr").setChatStyle(errorStyle));
        }
        return permission;
    }

    public static IJvmEnvironment env;
    private static final ChatStyle errorStyle = new ChatStyle().setColor(EnumChatFormatting.RED);

    public static IChatComponent error(String message) {
        return new ChatComponentText(message).setChatStyle(errorStyle);
    }
}
