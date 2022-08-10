package com.test.springboot.util;

import org.bytedeco.javacpp.Loader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 操作图片工具类
 * @author 肖龙威
 * @date 2022/03/16 11:17
 */
public class PhotoUtils {

    /**
     * 照片流转换(其它格式转换"jpg"格式)
     * @param source
     * @return
     * @throws IOException
     */
    public static InputStream othersConvertJpg(InputStream source) throws IOException {
        Image image = ImageIO.read(source);
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        g.dispose();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //the `png` can use `jpg`
        ImageIO.write(bufferedImage, "jpg", outputStream);
        outputStream.flush();
        outputStream.close();
        source.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return inputStream;
    }

    public static void ffmpegExecute(String command) throws Exception {
        String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
        String[] commands = command.trim().split(" ");
        if ("ffmpeg".equals(commands[0])) {
            commands[0] = ffmpeg;
        } else {
            String[] newCommands = new String[commands.length + 1];
            System.arraycopy(commands, 0, newCommands, 1, commands.length);
            newCommands[0] = ffmpeg;
            commands = newCommands;
        }
        ProcessBuilder builder = new ProcessBuilder(commands);
        Process process = builder.inheritIO().start();
        process.waitFor();
        process.destroy();
    }

}
