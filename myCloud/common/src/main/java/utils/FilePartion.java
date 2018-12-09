package utils;

import DB.User;
import io.netty.channel.ChannelHandlerContext;
import javafx.scene.Parent;
import messages.FileMessage;

import java.io.*;
import java.nio.channels.Channel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FilePartion {

    private final static int PART_SIZE = 1024;

    public static void sendFile(Path path, ChannelHandlerContext out, User user) {
        try {
            byte[] fileData = Files.readAllBytes(path);
            int partsCount = fileData.length / PART_SIZE;
            if (fileData.length % PART_SIZE != 0) {
                partsCount++;
            }
            for (int i = 0; i < partsCount; i++) {
                int startPosition = i * PART_SIZE;
                int endPosition = (i + 1) * PART_SIZE;
                FileMessage fm = new FileMessage(path.getFileName().toString(), Arrays.copyOfRange(fileData, startPosition, endPosition), user);
                out.writeAndFlush(fm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
