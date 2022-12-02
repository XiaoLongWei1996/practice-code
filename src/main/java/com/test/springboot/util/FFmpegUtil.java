package com.test.springboot.util;

import org.apache.commons.lang3.ObjectUtils;
import org.bytedeco.javacpp.Loader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 操作图片工具类
 * @author 肖龙威
 * @date 2022/03/16 11:17
 */
public class FFmpegUtil {

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

    /**
     * windows执行ffmpeg命令
     * @param command
     * @throws Exception
     */
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

    /**
     * linux执行ffmpeg命令
     * @param command
     */
    public static void linuxffmpegExecute(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
            errorGobbler.start();//  kick  off  stderr
            StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
            outGobbler.start();//  kick  off  stdout
            process.waitFor();
        } catch (Exception e) {
            //do some thing
            e.printStackTrace();
        }
    }

    public static void execCommand(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{command});
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
            errorGobbler.start();//  kick  off  stderr
            StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
            outGobbler.start();//  kick  off  stdout
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行shell脚本
     */
    public static void executeShell(String shellUrl) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(shellUrl);
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
            errorGobbler.start();//  kick  off  stderr
            StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
            outGobbler.start();//  kick  off  stdout
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class StreamGobbler extends Thread {
    InputStream is;
    String type;
    OutputStream os;

    public StreamGobbler(InputStream is, String type) {
        this(is, type, null);
    }

    public StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    @Override
    public void run() {
        PrintWriter pw = null;
        try {
            if (os != null)
                pw = new PrintWriter(os);

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (pw != null)
                    pw.println(line);
                System.out.println(type + ">" + line);
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (ObjectUtils.isNotEmpty(is)) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (ObjectUtils.isNotEmpty(os)) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ObjectUtils.isNotEmpty(pw)) {
                pw.close();
            }
        }
    }
}
