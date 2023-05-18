package com.test.springboot.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.test.springboot.domain.FileDetail;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 操作视频,图片
 *
 * @author 肖龙威
 * @date 2022/03/14 16:12
 */
@Service
public class MediaService {

    @Value("${temp-dir}")
    private String tempDir;

    @Value("${ffmpeg.pool-size}")
    private Integer poolSize;

    private ExecutorService pool;

    @PostConstruct
    public void init() {
        pool = new ThreadPoolExecutor(poolSize
                , poolSize
                , 5000
                , TimeUnit.SECONDS
                , new LinkedBlockingQueue<>(10)
                , Executors.defaultThreadFactory()
                , new ThreadPoolExecutor.AbortPolicy());
    }

    public File fontMark(File file, String text, Font font, Color color, Integer w, Integer h) throws IOException {
        File target = createFile("jpg");
        Image image = ImageIO.read(file);
        Assert.notNull(image, "文件不存在");
        int height = image.getHeight(null);
        int width = image.getWidth(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        mark(bufferedImage, image, text, font, color, w, h);
        ImageIO.write(bufferedImage, "jpg", target);
        return target;
    }

    public File imgMark(File img, File markImg, int width, int heigth) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(markImg);
        File jpg = createFile("jpg");
        Thumbnails.of(img)
                .forceSize(width, heigth)
                .watermark(Positions.BOTTOM_CENTER, bufferedImage, 0.8f)
                .outputQuality(0.8f)
                .toFile(jpg);
        return jpg;
    }

    /**
     * 添加文字水印
     *
     * @param source 源文件流
     * @param target 输出文件流
     * @param text   文字
     * @param font   字体
     * @param color  颜色
     * @param x      坐标x
     * @param y      坐标y
     */
    public void mark(InputStream source, OutputStream target, String text, Font font, Color color, int x, int y) throws IOException {
        try {
            Image image = ImageIO.read(source);
            Assert.notNull(image, "文件不存在");
            int height = image.getHeight(null);
            int width = image.getWidth(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            mark(bufferedImage, image, text, font, color, x, y);
            ImageIO.write(bufferedImage, "jpg", target);
        } finally {
            IOUtils.closeQuietly(source);
            IOUtils.closeQuietly(target);
        }
    }

    public File fontMark1(File file, String text, Font font, Color color, int x, int y) throws IOException {
        File target = createFile("jpg");
        Image image = ImageIO.read(file);
        Assert.notNull(image, "文件不存在");
        int height = image.getHeight(null);
        int width = image.getWidth(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        mark(bufferedImage, image, text, font, color, x, y);
        ImageIO.write(bufferedImage, "png", target);
        return target;
    }

    /**
     * 添加图片水印
     *
     * @param source     源文件流
     * @param markSource 水印文件流
     * @param target     输出文件流
     * @param x          坐标x
     * @param y          左边y
     * @throws IOException
     */
    public void mark(InputStream source, InputStream markSource, OutputStream target, int x, int y) throws IOException {
        try {
            Image image = ImageIO.read(source);
            Image markImage = ImageIO.read(markSource);
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            mark(bufferedImage, image, markImage, x, y);
            ImageIO.write(bufferedImage, "jpg", target);
        } finally {
            IOUtils.closeQuietly(source);
            IOUtils.closeQuietly(markSource);
            IOUtils.closeQuietly(target);
        }
    }

    /**
     * 多张图片合成视频
     *
     * @param imgFiles 照片流数组
     * @param taget    输出流
     * @param width    视频的宽
     * @param height   视频的高
     */
    public void imgCompositeVideo(List<File> imgFiles, OutputStream taget, String[] trasitions, int width, int height) throws Exception {
        Assert.notNull(imgFiles, "文件为空");
        //视频宽高最好是按照常见的视频的宽高  16：9  或者 9：16
        File file = new File("D:\\v01.mp4");
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(file, width, height);
        FFmpegFrameGrabber vGrabber = null;
        //设置视频编码层模式
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        //设置视频为25帧每秒
        recorder.setFrameRate(25);
        //设置视频图像数据格式
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);//avutil.AV_PIX_FMT_RGB32_1
        //设置视频的输出格式
        recorder.setFormat("mp4");
        //设置清晰度
        recorder.setVideoQuality(25);
        //8000kb/s 这个说明视频每秒大小，值越大图片转过来的压缩率就越小质量就会越高
        recorder.setVideoBitrate(8000000);
        try {
            recorder.start();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            for (int i = 0, len = imgFiles.size(); i < len; i++) {
                File f1 = imgFiles.get(i);
                BufferedImage image = ImageIO.read(f1);
                //一张图片录制2秒
                for (int j = 0; j < 1; j++) {
                    //一秒是25帧 所以要记录25次
                    for (int k = 0; k < 25; k++) {
                        recorder.record(converter.convert(image));
                    }
                }
                //添加过渡效果
                if (ArrayUtils.isNotEmpty(trasitions) && (i + 1) < len) {
                    String trasition = trasitions[i];
                    if (StringUtils.isNotBlank(trasition)) {
                        File f2 = imgFiles.get(i + 1);
                        File video = mergeTransitionVideo(f1.getAbsolutePath(), f2.getAbsolutePath(), trasition);
                        vGrabber = new FFmpegFrameGrabber(video);
                        vGrabber.start();
                        recordVideo(recorder, vGrabber, false);
                        closeGrabber(vGrabber);
                        //删除临时数据
                        video.delete();
                    }
                }
                //删除临时数据
                f1.delete();
            }
        } finally {
            closeRecorder(recorder);
            IOUtils.closeQuietly(taget);
            closeGrabber(vGrabber);
        }
    }

    /**
     * 添加图片滤镜
     *
     * @param imgInput 图片io
     * @param target   输出io
     * @throws IOException
     */
    public void addImageFilter(InputStream imgInput, OutputStream target) throws IOException {
        try {
            BufferedImage image = ImageIO.read(imgInput);
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            ImageIO.write(bufferedImage, "png", target);
        } finally {
            IOUtils.closeQuietly(imgInput);
            IOUtils.closeQuietly(target);
        }
    }

    /**
     * 设置图片尺寸
     *
     * @param imgInput
     * @param target
     * @param height
     * @param width
     */
    public void imageZoom(InputStream imgInput, OutputStream target, Integer height, Integer width, String format) throws IOException {
        try {
            BufferedImage image = ImageIO.read(imgInput);
            Thumbnails.of(image).forceSize(width, height).outputFormat(format).toOutputStream(target);
        } finally {
            IOUtils.closeQuietly(imgInput);
            IOUtils.closeQuietly(target);
        }
    }

    /**
     * 设置视频的大小
     *
     * @param video
     * @param target
     * @param height
     * @param width
     * @throws IOException
     */
    public void videoZoom(InputStream video, OutputStream target, Integer height, Integer width) throws IOException {
        File file = new File("D:\\v01.mp4");
        FFmpegFrameRecorder recorder = null;
        FFmpegFrameGrabber vGrabber = null;
        Frame frame = null;
        try {
            vGrabber = new FFmpegFrameGrabber(video);
            vGrabber.start();
            recorder = new FFmpegFrameRecorder(file, width, height, vGrabber.getAudioChannels());
            //设置视频编码层模式
            recorder.setVideoCodec(vGrabber.getVideoCodec());
            //设置视频为25帧每秒
            recorder.setFrameRate(vGrabber.getFrameRate());
            //设置视频图像数据格式
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);//avutil.AV_PIX_FMT_YUV420P
            //设置视频的输出格式
            recorder.setFormat(vGrabber.getFormat());
            //设置清晰度25-30最清晰,范围是0-40
            recorder.setVideoQuality(25);
            //8000kb/s 这个说明视频每秒大小，值越大图片转过来的压缩率就越小质量就会越高
            recorder.setVideoBitrate(vGrabber.getVideoBitrate());
            //录制视频
            recorder.start();
            while ((frame = vGrabber.grabFrame()) != null) {
                recorder.record(frame);
            }
        } finally {
            closeGrabber(vGrabber);
            closeRecorder(recorder);
            IOUtils.closeQuietly(video);
            IOUtils.closeQuietly(target);
        }
    }

    /**
     * 视频融合音频
     *
     * @param video
     * @param audio
     * @param target
     */
    public void mergeAudioAndVideo(InputStream video, InputStream audio, OutputStream target) throws FrameGrabber.Exception, FrameRecorder.Exception {
        FFmpegFrameRecorder recorder = null;
        FrameGrabber vGrabber = null;
        FrameGrabber aGrabber = null;
        try {
            //视频抓取器
            vGrabber = new FFmpegFrameGrabber(video);
            //音频抓取器
            aGrabber = new FFmpegFrameGrabber(audio);
            vGrabber.start();
            aGrabber.start();

            //录制器
            File file = new File("D:\\v01.mp4");
            recorder = new FFmpegFrameRecorder(file, vGrabber.getImageWidth(), vGrabber.getImageHeight(), aGrabber.getAudioChannels());
            //设置视频编码层模式
            recorder.setVideoCodec(vGrabber.getVideoCodec());
            //设置视频为25帧每秒
            recorder.setFrameRate(vGrabber.getFrameRate());
            //设置视频图像数据格式
            //recorder.setPixelFormat(vGrabber.getPixelFormat());//avutil.AV_PIX_FMT_YUV420P
            //System.out.println(vGrabber.getPixelFormat());
            //设置视频的输出格式
            recorder.setFormat(vGrabber.getFormat());
            //设置清晰度25-30最清晰,范围是0-40
            recorder.setVideoQuality(25);
            //8000kb/s 这个说明视频每秒大小，值越大图片转过来的压缩率就越小质量就会越高
            recorder.setVideoBitrate(vGrabber.getVideoBitrate());
            recorder.start();
            //先录制视频
            Frame vFrame = null;
            Frame aFrame = null;
            while ((vFrame = vGrabber.grabFrame()) != null) {
                if (vFrame.image != null) {
                    recorder.record(vFrame);
                    vFrame.close();
                    aFrame = aGrabber.grabFrame();
                    if (aFrame != null) {
                        while (aFrame.samples == null) {
                            aFrame = aGrabber.grabFrame();
                        }
                        recorder.recordSamples(aFrame.sampleRate, aFrame.audioChannels, aFrame.samples);
                        aFrame.close();
                    }
                }
            }
        } finally {
            closeGrabber(vGrabber);
            closeGrabber(aGrabber);
            closeRecorder(recorder);
            IOUtils.closeQuietly(target);
        }
    }

    /**
     * 截取视频
     *
     * @param video  视频
     * @param target 输出对象
     * @param start  开始时间
     * @param end    结束时间
     * @throws IOException
     */
    public void videoClip(MultipartFile video, OutputStream target, Integer start, Integer end) throws Exception {
        File file = storeLocalTempDir(video);
        File file1 = clipVideo(file, start, end - start);
        target.close();
    }

    public void audioClip(MultipartFile audio, OutputStream target, Integer start, Integer end) throws Exception {
        File file = storeLocalTempDir(audio);
        File file1 = clipAudio(file, start, end - start);
        target.close();
    }

    /**
     * 合并视频
     *
     * @param files  视频文件数组
     * @param target 输出
     * @param width  宽
     * @param height 高
     * @throws IOException
     */
    public void mergeVideos(List<File> files, OutputStream target, String[] trasitions, int width, int height) throws Exception {
        File file = new File("D:\\v01.mp4");
        FFmpegFrameRecorder recorder = null;
        FFmpegFrameGrabber vGrabber = null;
        FFmpegFrameGrabber vGrabber1 = null;
        File transition = null;
        try {
            // 流媒体输出地址，分辨率（长，高），是否录制音频（0:单声道/1:立体声）
            recorder = new FFmpegFrameRecorder(file, width, height, 1);
            //设置视频编码层模式
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            //设置视频为25帧每秒
            recorder.setFrameRate(25);
            //设置视频图像数据格式
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);//avutil.AV_PIX_FMT_RGB32_1
            //设置视频的输出格式
            recorder.setFormat("mp4");
            //设置清晰度
            recorder.setVideoQuality(25);
            //8000kb/s 这个说明视频每秒大小，值越大图片转过来的压缩率就越小质量就会越高
            recorder.setVideoBitrate(8000000);
            recorder.start();
            for (int i = 0, len = files.size(); i < len; i++) {
                File f1 = files.get(i);
                vGrabber = new FFmpegFrameGrabber(f1);
                vGrabber.start();
                //录制视频
                recordVideo(recorder, vGrabber, false);
                //合成过渡效果
                if ((i + 1) < len && ArrayUtils.isNotEmpty(trasitions)) {
                    String trasition = trasitions[i];
                    if (StringUtils.isNotBlank(trasition)) {
                        File f2 = files.get(i + 1);
                        transition = videoTransition(f1, f2, width, height, trasition);
                        vGrabber1 = new FFmpegFrameGrabber(transition);
                        vGrabber1.start();
                        recordVideo(recorder, vGrabber1, false);
                        closeGrabber(vGrabber1);
                    }
                }
                closeGrabber(vGrabber);
                f1.delete();
            }
        } finally {
            closeRecorder(recorder);
            IOUtils.closeQuietly(target);
            closeGrabber(vGrabber);
            closeGrabber(vGrabber1);
            if (ObjectUtils.isNotEmpty(transition)) {
                transition.delete();
            }
        }
    }

    /**
     * 画中画
     *
     * @param source 源文件
     * @param nested 嵌套文件
     * @param target 输出
     * @throws IOException
     */
    public void nestedVideo(InputStream source, InputStream nested, Integer x, Integer y, Integer width, Integer height, OutputStream target) throws IOException {
        File file = new File("D:\\v01.mp4");
        FFmpegFrameRecorder recorder = null;
        FFmpegFrameGrabber sGrabber = null;
        FFmpegFrameGrabber nGrabber = null;
        Frame frame = null;
        Frame mergeFrame = null;
        try {
            sGrabber = new FFmpegFrameGrabber(source);
            nGrabber = new FFmpegFrameGrabber(nested);
            sGrabber.start();
            nGrabber.start();
            recorder = new FFmpegFrameRecorder(file, sGrabber.getImageWidth(), sGrabber.getImageHeight(), sGrabber.getAudioChannels());
            //设置视频编码层模式
            recorder.setVideoCodec(sGrabber.getVideoCodec());
            //设置视频为25帧每秒
            recorder.setFrameRate(sGrabber.getFrameRate());
            //设置视频图像数据格式
            //recorder.setPixelFormat(vGrabber.getPixelFormat());//avutil.AV_PIX_FMT_YUV420P
            //System.out.println(vGrabber.getPixelFormat());
            //设置视频的输出格式
            recorder.setFormat(sGrabber.getFormat());
            //设置清晰度25-30最清晰,范围是0-40
            recorder.setVideoQuality(25);
            //8000kb/s 这个说明视频每秒大小，值越大图片转过来的压缩率就越小质量就会越高
            recorder.setVideoBitrate(sGrabber.getVideoBitrate());
            //录制视频
            recorder.start();
            while ((frame = sGrabber.grabFrame()) != null) {
                if (frame.image != null) {
                    BufferedImage buffImg = Java2DFrameUtils.toBufferedImage(frame);
                    mergeFrame = nGrabber.grabFrame();
                    if (mergeFrame != null) {
                        //判断获取图片帧是否为空，如果为空就在获取一帧，循环判断
                        while (mergeFrame.image == null) {
                            mergeFrame = nGrabber.grabFrame();
                        }
                        Graphics2D graphics = buffImg.createGraphics();
                        Image mergeBuffer = Java2DFrameUtils.toBufferedImage(mergeFrame);
                        graphics.drawImage(mergeBuffer, x, y, width, height, null);
                        graphics.dispose();
                    }
                    //新的一帧
                    Frame newFrame = Java2DFrameUtils.toFrame(buffImg);
                    recorder.record(newFrame);
                }
                //设置音频
                if (frame.samples != null) {
                    recorder.recordSamples(frame.sampleRate, frame.audioChannels, frame.samples);
                }
            }
        } finally {
            closeRecorder(recorder);
            closeGrabber(sGrabber);
            closeGrabber(nGrabber);
            IOUtils.closeQuietly(source);
            IOUtils.closeQuietly(nested);
            IOUtils.closeQuietly(target);
        }
    }

    /**
     * 视频添加滤镜
     *
     * @param video  视频
     * @param target 目标
     */
    public void addVideoFilter(InputStream video, OutputStream target) throws FrameGrabber.Exception, FrameRecorder.Exception {
        File file = new File("D:\\v01.mp4");
        FFmpegFrameRecorder recorder = null;
        FFmpegFrameGrabber vGrabber = null;
        Frame frame = null;
        try {
            vGrabber = new FFmpegFrameGrabber(video);
            vGrabber.start();
            recorder = new FFmpegFrameRecorder(file, vGrabber.getImageWidth(), vGrabber.getImageHeight(), vGrabber.getAudioChannels());
            //设置视频编码层模式
            recorder.setVideoCodec(vGrabber.getVideoCodec());
            //设置视频为25帧每秒
            recorder.setFrameRate(vGrabber.getFrameRate());
            //设置视频图像数据格式
            //recorder.setPixelFormat(vGrabber.getPixelFormat());//avutil.AV_PIX_FMT_YUV420P
            //System.out.println(vGrabber.getPixelFormat());
            //设置视频的输出格式
            recorder.setFormat(vGrabber.getFormat());
            //设置清晰度25-30最清晰,范围是0-40
            recorder.setVideoQuality(25);
            //8000kb/s 这个说明视频每秒大小，值越大图片转过来的压缩率就越小质量就会越高
            recorder.setVideoBitrate(vGrabber.getVideoBitrate());
            //录制视频
            recorder.start();
            while ((frame = vGrabber.grabFrame()) != null) {
                if (frame.image != null) {
                    BufferedImage image = Java2DFrameUtils.toBufferedImage(frame);
                    frame = Java2DFrameUtils.toFrame(image);
                }
                recorder.record(frame);
            }
        } finally {
            closeGrabber(vGrabber);
            closeRecorder(recorder);
            IOUtils.closeQuietly(video);
            IOUtils.closeQuietly(target);
        }
    }

    /**
     * 视频添加文字水印
     *
     * @param video  视频
     * @param target 输出
     * @param text   文字
     * @param font   字体
     * @param color  颜色
     * @param x      x坐标
     * @param y      y坐标
     * @throws FrameGrabber.Exception
     * @throws FrameRecorder.Exception
     */
    public void addVideoFontMark(InputStream video, OutputStream target, String text, Font font, Color color, int x, int y) throws FrameGrabber.Exception, FrameRecorder.Exception {
        File file = new File("D:\\v01.mp4");
        FFmpegFrameRecorder recorder = null;
        FFmpegFrameGrabber vGrabber = null;
        Frame frame = null;
        try {
            vGrabber = new FFmpegFrameGrabber(video);
            vGrabber.start();
            recorder = new FFmpegFrameRecorder(file, vGrabber.getImageWidth(), vGrabber.getImageHeight(), vGrabber.getAudioChannels());
            //设置视频编码层模式
            recorder.setVideoCodec(vGrabber.getVideoCodec());
            //设置视频为25帧每秒
            recorder.setFrameRate(vGrabber.getFrameRate());
            //设置视频图像数据格式
            //recorder.setPixelFormat(vGrabber.getPixelFormat());//avutil.AV_PIX_FMT_YUV420P
            //设置视频的输出格式
            recorder.setFormat(vGrabber.getFormat());
            //设置清晰度25-30最清晰,范围是0-40
            recorder.setVideoQuality(25);
            //8000kb/s 这个说明视频每秒大小，值越大图片转过来的压缩率就越小质量就会越高
            recorder.setVideoBitrate(vGrabber.getVideoBitrate());
            //录制视频
            recorder.start();
            while ((frame = vGrabber.grabFrame()) != null) {
                if (frame.image != null) {
                    BufferedImage image = Java2DFrameUtils.toBufferedImage(frame);
                    mark(image, image, text, font, color, x, y);
                    frame = Java2DFrameUtils.toFrame(image);
                }
                recorder.record(frame);
            }
        } finally {
            closeRecorder(recorder);
            closeGrabber(vGrabber);
            IOUtils.closeQuietly(video);
            IOUtils.closeQuietly(target);
        }
    }

    /**
     * 添加图片水印
     *
     * @param video      视频
     * @param markSource 水印照片
     * @param target     输出
     * @param x          x坐标
     * @param y          y坐标
     * @throws IOException
     */
    public void addVideoImgMark(InputStream video, InputStream markSource, OutputStream target, int x, int y) throws IOException {
        File file = new File("D:\\v01.mp4");
        FFmpegFrameRecorder recorder = null;
        FFmpegFrameGrabber vGrabber = null;
        Frame frame = null;
        try {
            vGrabber = new FFmpegFrameGrabber(video);
            vGrabber.start();
            recorder = new FFmpegFrameRecorder(file, vGrabber.getImageWidth(), vGrabber.getImageHeight(), vGrabber.getAudioChannels());
            //设置视频编码层模式
            recorder.setVideoCodec(vGrabber.getVideoCodec());
            //设置视频为25帧每秒
            recorder.setFrameRate(vGrabber.getFrameRate());
            //设置视频图像数据格式
            //recorder.setPixelFormat(vGrabber.getPixelFormat());//avutil.AV_PIX_FMT_YUV420P
            //设置视频的输出格式
            recorder.setFormat(vGrabber.getFormat());
            //设置清晰度25-30最清晰,范围是0-40
            recorder.setVideoQuality(25);
            //8000kb/s 这个说明视频每秒大小，值越大图片转过来的压缩率就越小质量就会越高
            recorder.setVideoBitrate(vGrabber.getVideoBitrate());
            //录制视频
            recorder.start();
            //获取水印图片
            BufferedImage markImage = ImageIO.read(markSource);
            while ((frame = vGrabber.grabFrame()) != null) {
                if (frame.image != null) {
                    BufferedImage image = Java2DFrameUtils.toBufferedImage(frame);
                    mark(image, image, markImage, x, y);
                    frame = Java2DFrameUtils.toFrame(image);
                }
                recorder.record(frame);
            }
        } finally {
            closeRecorder(recorder);
            closeGrabber(vGrabber);
            IOUtils.closeQuietly(video);
            IOUtils.closeQuietly(target);
            IOUtils.closeQuietly(markSource);
        }
    }

    public static String acquireResolutionRatio(InputStream in, int type) throws IOException {
        String s = null;
        if (type == 1) {
            //处理图片
            BufferedImage img = ImageIO.read(in);
            int width = img.getWidth();
            int height = img.getHeight();
            s = height + "*" + width;
        } else {
            FFmpegFrameGrabber vGrabber = new FFmpegFrameGrabber(in);
            vGrabber.start();
            int width = vGrabber.getImageWidth();
            int height = vGrabber.getImageHeight();
            s = height + "*" + width;
            vGrabber.stop();
        }
        return s;
    }

    /**
     * 合成过滤视频,两张图片尺寸大小要一样
     *
     * @param imgPath1   图片路径1
     * @param imgPath2   图片路径2
     * @param transition 过滤参数
     *                   fade : 淡入淡出
     *                   fadeblack : 褪黑
     *                   fadewhite : 褪白
     *                   distance : 渐入
     *                   wipeleft : 向左擦除
     *                   wiperight : 向右擦除
     *                   wipeup : 向上擦除
     *                   wipedown : 向下擦除
     *                   slideleft : 左滑
     *                   slideright : 右滑
     *                   slideup : 上滑
     *                   slidedown : 下滑
     *                   smoothleft : 平滑左
     *                   smoothright : 平滑右
     *                   smoothup : 平滑上
     *                   smoothdown : 平滑下
     *                   rectcrop : 矩形切入
     *                   circlecrop : 圆形切入
     *                   circleclose : 圆形关闭
     *                   circleopen : 圆形打开
     *                   horzclose : 水平关闭
     *                   horzopen : 水平打开
     *                   vertclose : 垂直关闭
     *                   vertopen : 垂直打开
     *                   diagbl : 右上对角膜
     *                   diagbr : 左上对角膜
     *                   diagtl : 右下对角膜
     *                   diagtr : 左下对角膜
     *                   hlslice : 向左切片
     *                   hrslice : 向右切片
     *                   vuslice : 向上切片
     *                   vdslice : 向下切片
     *                   dissolve : 溶解
     *                   pixelize : 像素化
     *                   radial : 径向
     *                   hblur : 模糊
     *                   wipetl : 向左上角擦拭
     *                   wipetr : 向右上角擦拭
     *                   wipebl : 向左下角擦拭
     *                   wipebr : 向右下角擦拭
     *                   fadegrays : 淡灰色
     *                   squeezev : 压缩
     *                   squeezeh : 挤压
     *                   zoomin : 放大
     * @return
     * @throws Exception
     */
    private File mergeTransitionVideo(String imgPath1, String imgPath2, String transition) throws Exception {
        String fileName = tempDir + UUID.randomUUID() + ".mp4";
        String ffmpeg = "ffmpeg -loop 1 -t 1 -i " + imgPath1 + " " +
                "-loop 1 -t 1 -i " + imgPath2 + " " +
                "-filter_complex \"[0][1]xfade=transition=" + transition + ":duration=0.5:offset=0.5,format=yuv420p\" " +
                "" + fileName + "";
        FFmpegUtil.ffmpegExecute(ffmpeg);
        File file = new File(fileName);
        return file;
    }

    /**
     * 改变视频帧率
     *
     * @param file       文件
     * @param rate       率
     * @param resolution 分辨率
     * @return {@link File}
     * @throws Exception 异常
     */
    private File changeVideoFrameRate(File file, Integer rate, String resolution) throws Exception {
        File mp4 = createFile("mp4");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-i");
        command.add(file.getAbsolutePath());
        command.add("-qscale 0");
        command.add("-r");
        command.add(String.valueOf(rate));
        if (StrUtil.isNotBlank(resolution)) {
            command.add("-s");
            command.add(resolution);
        }
        command.add("-y");
        command.add("-c copy");
        command.add(mp4.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp4;
    }

    /**
     * 剪辑视频
     *
     * @param file     文件
     * @param start    开始
     * @param duration 持续时间
     * @return {@link File}
     * @throws Exception 异常
     */
    private File clipVideo(File file, Integer start, Integer duration) throws Exception {
        File mp4 = createFile("mp4");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-ss");
        command.add(String.valueOf(start));
        command.add("-t");
        command.add(String.valueOf(duration));
        command.add("-accurate_seek");
        command.add("-i");
        command.add(file.getAbsolutePath());
        command.add("-codec copy");
        command.add(mp4.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp4;
    }

    /**
     * 视频音频
     *
     * @param audio    音频
     * @param start    开始
     * @param duration 持续时间
     * @return {@link File}
     * @throws Exception 异常
     */
    public File clipAudio(File audio, Integer start, Integer duration) throws Exception {
        File mp3 = createFile("mp3");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-i");
        command.add(audio.getAbsolutePath());
        command.add("-ss");
        command.add(String.valueOf(start));
        command.add("-t");
        command.add(String.valueOf(duration));
        command.add(mp3.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp3;
    }

    /**
     * img过渡
     *
     * @param f1         f1
     * @param f2         f2
     * @param width      宽度
     * @param height     高度
     * @param transition 过渡
     * @return {@link File}
     * @throws Exception 异常
     */
    private File imgTransition(File f1, File f2, int width, int height, String transition) throws Exception {
        File f3 = null;
        File f4 = null;
        try {
            f3 = changeImg("jpg", width, height, f1);
            f4 = changeImg("jpg", width, height, f2);
            File file = mergeTransitionVideo(f3.getAbsolutePath(), f4.getAbsolutePath(), transition);
            return file;
        } finally {
            if (f3 != null) {
                f3.delete();
            }

            if (f4 != null) {
                f4.delete();
            }
        }
    }

    private File videoTransition(File f1, File f2, int width, int height, String transition) throws Exception {
        FFmpegFrameGrabber vg1 = new FFmpegFrameGrabber(f1);
        FFmpegFrameGrabber vg2 = new FFmpegFrameGrabber(f2);
        Frame frame = null;
        File img1 = null;
        File img2 = null;
        try {
            vg1.start();
            vg2.start();
            vg1.setFrameNumber(vg1.getLengthInFrames() - 10);
            while ((frame = vg1.grabFrame()).image == null) {
                frame = vg1.grabFrame();
            }
            img1 = storeFrameAsImg(frame, "jpg", width, height);
            frame.close();
            vg2.setFrameNumber(5);
            while ((frame = vg2.grabFrame()).image == null) {
                frame = vg2.grabFrame();
            }
            img2 = storeFrameAsImg(frame, "jpg", width, height);
            frame.close();
            File transitionVideo = mergeTransitionVideo(img1.getAbsolutePath(), img2.getAbsolutePath(), transition);
            return transitionVideo;
        } finally {
            closeGrabber(vg1);
            closeGrabber(vg2);
            if (ObjectUtils.isNotEmpty(frame)) {
                frame.close();
            }
            if (ObjectUtils.isNotEmpty(img1)) {
                img1.delete();
            }
            if (ObjectUtils.isNotEmpty(img1)) {
                img2.delete();
            }
        }
    }

    private File imgVideoTransition(File img, File video, int width, int height, String transition) throws Exception {
        FFmpegFrameGrabber vg2 = new FFmpegFrameGrabber(video);
        Frame frame = null;
        File img1 = null;
        File img2 = null;
        try {
            img1 = changeImg("jpg", width, height, img);

            vg2.start();
            vg2.setFrameNumber(5);
            while ((frame = vg2.grabFrame()).image == null) {
                frame = vg2.grabFrame();
            }
            img2 = storeFrameAsImg(frame, "jpg", width, height);
            frame.close();
            File transitionVideo = mergeTransitionVideo(img1.getAbsolutePath(), img2.getAbsolutePath(), transition);
            return transitionVideo;
        } finally {
            closeGrabber(vg2);
            if (ObjectUtils.isNotEmpty(frame)) {
                frame.close();
            }
            if (ObjectUtils.isNotEmpty(img1)) {
                img1.delete();
            }
            if (ObjectUtils.isNotEmpty(img1)) {
                img2.delete();
            }
        }
    }

    private File videoImgTransition(File video, File img, int width, int height, String transition) throws Exception {
        FFmpegFrameGrabber vg1 = new FFmpegFrameGrabber(video);
        Frame frame = null;
        File img1 = null;
        File img2 = null;
        try {
            vg1.start();
            vg1.setFrameNumber(vg1.getLengthInFrames() - 10);
            while ((frame = vg1.grabFrame()).image == null) {
                frame = vg1.grabFrame();
            }
            img1 = storeFrameAsImg(frame, "jpg", width, height);
            frame.close();

            img2 = changeImg("jpg", width, height, img);
            File transitionVideo = mergeTransitionVideo(img1.getAbsolutePath(), img2.getAbsolutePath(), transition);
            return transitionVideo;
        } finally {
            closeGrabber(vg1);
            if (ObjectUtils.isNotEmpty(frame)) {
                frame.close();
            }
            if (ObjectUtils.isNotEmpty(img1)) {
                img1.delete();
            }
            if (ObjectUtils.isNotEmpty(img1)) {
                img2.delete();
            }
        }
    }

    /**
     * 关闭录制对象
     *
     * @param recorder
     * @throws FrameRecorder.Exception
     */
    private void closeRecorder(FrameRecorder recorder) throws FrameRecorder.Exception {
        if (ObjectUtils.isNotEmpty(recorder)) {
            recorder.stop();
            recorder.release();
            recorder.close();
        }
    }

    /**
     * 关闭抓取对象
     *
     * @param sGrabber
     * @throws FrameGrabber.Exception
     */
    private void closeGrabber(FrameGrabber sGrabber) throws FrameGrabber.Exception {
        if (ObjectUtils.isNotEmpty(sGrabber)) {
            sGrabber.stop();
            sGrabber.release();
            sGrabber.close();
        }
    }

    /**
     * 录制转场
     *
     * @param recorder 录制对象
     * @param vGrabber 视频抓取对象
     */
    private void recordVideo(FFmpegFrameRecorder recorder, FFmpegFrameGrabber vGrabber, boolean b) throws FrameGrabber.Exception, FFmpegFrameRecorder.Exception, FileNotFoundException {
        Assert.notNull(recorder, "FFmpegFrameRecorder is null");
        Assert.notNull(vGrabber, "FrameGrabber is null");
        File f = ResourceUtils.getFile("classpath:static/5.mp3");
        FFmpegFrameGrabber aGrabber = new FFmpegFrameGrabber(f);
        aGrabber.start();
        Frame frame = null;
        Frame aFrame = null;
        while ((frame = vGrabber.grab()) != null) {
            if (frame.image != null) {
                recorder.record(frame);
            }
            if (b) {
                continue;
            }
            aFrame = aGrabber.grabFrame();
            if (aFrame != null) {
                while (aFrame.samples == null) {
                    aFrame = aGrabber.grabFrame();
                }
                recorder.recordSamples(aFrame.sampleRate, aFrame.audioChannels, aFrame.samples);
                recorder.recordSamples(aFrame.sampleRate, aFrame.audioChannels, aFrame.samples);
            }
        }
        closeGrabber(aGrabber);
    }

    /**
     * 录制照片
     *
     * @param recorder 录制对象
     * @param frame    frame
     */
    private void recordImg(FFmpegFrameRecorder recorder, Frame frame, boolean b) throws FrameGrabber.Exception, FFmpegFrameRecorder.Exception, FileNotFoundException {
        File f = ResourceUtils.getFile("classpath:static/5.mp3");
        FFmpegFrameGrabber aGrabber = new FFmpegFrameGrabber(f);
        aGrabber.start();
        Frame aFrame = null;
        for (int k = 0; k < 25; k++) {
            if (frame.image != null) {
                recorder.record(frame);
            }
            aFrame = aGrabber.grabFrame();
            if (b) {
                continue;
            }
            if (aFrame != null) {
                while (aFrame.samples == null) {
                    aFrame = aGrabber.grabFrame();
                }
                recorder.recordSamples(aFrame.sampleRate, aFrame.audioChannels, aFrame.samples);
                recorder.recordSamples(aFrame.sampleRate, aFrame.audioChannels, aFrame.samples);
            }
        }
        closeGrabber(aGrabber);
    }

    /**
     * 录制音频
     *
     * @param recorder 录制对象
     * @param vGrabber 视频抓取对象
     */
    private void recordAudio(FFmpegFrameRecorder recorder, FrameGrabber vGrabber) throws FrameGrabber.Exception, FFmpegFrameRecorder.Exception {
        Frame frame = null;
        long t = recorder.getTimestamp();
        while ((frame = vGrabber.grabFrame()) != null && vGrabber.getTimestamp() <= t) {
            if (frame.samples != null) {
                recorder.recordSamples(frame.sampleRate, frame.audioChannels, frame.samples);
            }
        }
    }

    /**
     * 录制视频
     *
     * @param recorder 录制对象
     * @param vGrabber 视频抓取对象
     */
    private void recordVideo01(FFmpegFrameRecorder recorder, FFmpegFrameGrabber vGrabber, boolean b) throws FrameGrabber.Exception, FFmpegFrameRecorder.Exception, FileNotFoundException {
        Assert.notNull(recorder, "FFmpegFrameRecorder is null");
        Assert.notNull(vGrabber, "FrameGrabber is null");
        Frame frame = null;
        while ((frame = vGrabber.grab()) != null) {
            if (frame.image != null) {
                recorder.record(frame);
            }
            if (!b && frame.samples != null) {
                recorder.recordSamples(frame.sampleRate, frame.audioChannels, frame.samples);
            }
        }
    }

    /**
     * 获取视频时长，单位为秒
     *
     * @param video 源视频文件
     * @return 时长（s）
     */
    public int getVideoDuration(File video) throws IOException {
        int duration = 0;
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        try {
            ff.start();
            duration = (int) ff.getLengthInTime() / (1000 * 1000);
            ff.stop();
        } finally {
            if (ObjectUtils.isNotEmpty(ff)) {
                ff.close();
            }
        }
        return duration;
    }

    /**
     * javacv 检测图片清晰度
     * 标准差越大说明图像质量越好
     */
    public double imgClarityAnalyse(File file) {
        File file1 = new File(tempDir + "/gray-" + file.getName());
        File file2 = new File(tempDir + "/laplacian-" + file.getName());
        try {
            org.bytedeco.opencv.opencv_core.Mat srcImage = opencv_imgcodecs.imread(file.getAbsolutePath());
            org.bytedeco.opencv.opencv_core.Mat dstImage = new org.bytedeco.opencv.opencv_core.Mat();
            //转化为灰度图
            opencv_imgproc.cvtColor(srcImage, dstImage, opencv_imgproc.COLOR_BGR2GRAY);
            //在gray目录下生成灰度图片
            opencv_imgcodecs.imwrite(file1.getAbsolutePath(), dstImage);
            org.bytedeco.opencv.opencv_core.Mat laplacianDstImage = new org.bytedeco.opencv.opencv_core.Mat();
            //阈值太低会导致正常图片被误断为模糊图片，阈值太高会导致模糊图片被误判为正常图片
            opencv_imgproc.Laplacian(dstImage, laplacianDstImage, opencv_core.CV_64F);
            //在laplacian目录下升成经过拉普拉斯掩模做卷积运算的图片
            opencv_imgcodecs.imwrite(file2.getAbsolutePath(), laplacianDstImage);
            //矩阵标准差
            org.bytedeco.opencv.opencv_core.Mat stddev = new org.bytedeco.opencv.opencv_core.Mat();
            //求矩阵的均值与标准差
            opencv_core.meanStdDev(laplacianDstImage, new org.bytedeco.opencv.opencv_core.Mat(), stddev);
            return stddev.createIndexer().getDouble();
        } finally {
            file1.delete();
            file2.delete();
            file.delete();
        }
    }

    public static Boolean colorException(File jpegFile) {
        Mat srcImage = Imgcodecs.imread(jpegFile.getAbsolutePath());
        Mat dstImage = new Mat();
        //  将RGB图像转变到CIE L*a*b*
        Imgproc.cvtColor(srcImage, dstImage, Imgproc.COLOR_BGR2Lab);
        float a = 0, b = 0;
        int HistA[] = new int[256], HistB[] = new int[256];
        for (int i = 0; i < 256; i++) {
            HistA[i] = 0;
            HistB[i] = 0;
        }
        int size = (int) dstImage.total() * dstImage.channels();
        for (int i = 0; i < dstImage.rows(); i++) {
            for (int j = 0; j < dstImage.cols(); j++) {
                //在计算过程中，要考虑将CIEL*a*b*空间还原后同
                a += (float) (dstImage.get(i, j)[1] - 128);
                b += (float) (dstImage.get(i, j)[2] - 128);
//                int x=Math.abs(dstImage.ptr(i,j).get(1));
//                int y=Math.abs(dstImage.ptr(i,j).get(2));
                int x = (int) dstImage.get(i, j)[1];
                int y = (int) dstImage.get(i, j)[2];
                HistA[x]++;
                HistB[y]++;
            }
        }
        float da = a / (float) (dstImage.rows() * dstImage.cols());
        float db = b / (float) (dstImage.rows() * dstImage.cols());
        float D = (float) Math.sqrt(da * da + db * db);
        float Ma = 0, Mb = 0;
        for (int i = 0; i < 256; i++) {
            //计算范围-128～127
            Ma += Math.abs(i - 128 - da) * HistA[i];
            Mb += Math.abs(i - 128 - db) * HistB[i];
        }
        Ma /= (float) (dstImage.rows() * dstImage.cols());
        Mb /= (float) (dstImage.rows() * dstImage.cols());
        float M = (float) Math.sqrt(Ma * Ma + Mb * Mb);
        float K = D / M;
        float cast = K;
        System.out.printf("色偏指数： %f\n", cast);
        if (cast > 1.1) {
            System.out.printf("存在色偏\n");
            return true;
        } else {
            System.out.printf("不存在色偏\n");
            return false;
        }
    }

    public File storeLocalTempDir(MultipartFile multipartFile) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf(".") - 1);
            String storePath = tempDir + fileName;
            outputStream = new FileOutputStream(storePath);
            inputStream = multipartFile.getInputStream();
            IOUtils.copy(inputStream, outputStream);
            file = new File(storePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
        return file;
    }

    private File storeFrameAsImg(Frame frame, String format, int width, int height) throws IOException {
        BufferedImage img = Java2DFrameUtils.toBufferedImage(frame);
        String fileName = UUID.randomUUID() + "." + format;
        String storePath = tempDir + fileName;
        File file = new File(storePath);
        Thumbnails.of(img).forceSize(width, height).outputFormat(format).toFile(file);
        return file;
    }

    public File changeImg(String format, Integer width, Integer height, File file) throws IOException {
        String fileName = UUID.randomUUID() + "." + format;
        String storePath = tempDir + fileName;
        File f = new File(storePath);
        Thumbnails.of(file).forceSize(width, height).outputFormat(format).toFile(f);
        return f;
    }

    public File changeImg2(String format, Integer width, Integer height, File file) throws IOException {
        String fileName = UUID.randomUUID() + "." + format;
        String storePath = tempDir + fileName;
        File f = new File(storePath);
        Thumbnails.of(file).size(width, height).outputFormat(format).toFile(f);
        return f;
    }

    /**
     * 添加文字水印
     *
     * @param bufImg
     * @param text
     * @param font
     * @param color
     * @param x
     * @param y
     */
    private void mark(BufferedImage bufImg, Image img, String text, Font font, Color color, int x, int y) {
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(img, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
        g.dispose();
    }

    /**
     * @param bufImg
     * @param source
     * @param markImg
     * @param x
     * @param y
     */
    private void mark(BufferedImage bufImg, Image source, Image markImg, int x, int y) {
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(source, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        g.drawImage(markImg, x, y, markImg.getWidth(null), markImg.getHeight(null), null);
        g.dispose();
    }

//    public File generateFile(List<FileDetail> list, int width, int height) throws Exception {
//        //视频宽高最好是按照常见的视频的宽高  16：9  或者 9：16
//        File mp4 = createFile("mp4");
//        FileDetail f1 = null;
//        FileDetail f2 = null;
//        File transitionVideo = null;
//        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(mp4, width, height, 2);
//        FFmpegFrameGrabber vGrabber1 = null;
//        FFmpegFrameGrabber vGrabber2 = null;
//        //设置视频编码层模式
//        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
//        //设置视频为25帧每秒
//        recorder.setFrameRate(25);
//        //设置视频图像数据格式
//        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);//avutil.AV_PIX_FMT_RGB32_1
//        //设置视频的输出格式
//        recorder.setFormat("mp4");
//        //设置清晰度
//        recorder.setVideoQuality(25);
//        //8000kb/s 这个说明视频每秒大小，值越大图片转过来的压缩率就越小质量就会越高
//        recorder.setVideoBitrate(8000000);
//        recorder.setAudioBitrate(1000);
//        try {
//            recorder.start();
//            for (int i = 0, len = list.size(); i < len; i++) {
//                f1 = list.get(i);
//                //录制视频
//                if (Objects.equals(f1.getFormat(), "mp4")) {
//                    File file = changeVideoFrameRate(f1.getFile(), 25, null);
//                    vGrabber1 = new FFmpegFrameGrabber(file);
//                    vGrabber1.start();
//                    recordVideo01(recorder, vGrabber1, Objects.equals(f1.getMute(), 1));
//                    closeGrabber(vGrabber1);
//                } else if (Objects.equals(f1.getFormat(), "jpg")) { //录制图片
//                    BufferedImage image = ImageIO.read(f1.getFile());
//                    //一张图片录制2秒
//                    for (int j = 1; j <= 1; j++) {
//                        //一秒是25帧 所以要记录25次
//                        recordImg(recorder, Java2DFrameUtils.toFrame(image), isAudio);
//                    }
//                } else if (Objects.equals(f1.getFormat(), "mp3")) {
//                    vGrabber1 = new FFmpegFrameGrabber(f1.getFile());
//                    vGrabber1.start();
//                    recordAudio(recorder, vGrabber1);
//                    closeGrabber(vGrabber1);
//                }
//                //合成过渡效果
//                if ((i + 1) < list.size() && ObjectUtils.isNotEmpty(effect)) {
//                    f2 = list.get(i + 1);
//                    if (f1.getName().endsWith("jpg") && f2.getName().endsWith("jpg")) {
//                        //照片和照片
//                        transitionVideo = imgTransition(f1, f2, width, height, effect);
//                    } else if (f1.getName().endsWith("jpg") && f2.getName().endsWith("mp4")) {
//                        //照片和视频
//                        transitionVideo = imgVideoTransition(f1, f2, width, height, effect);
//                    } else if (f1.getName().endsWith("mp4") && f2.getName().endsWith("mp4")) {
//                        //视频和视频
//                        transitionVideo = videoTransition(f1, f2, width, height, effect);
//                    } else if (f1.getName().endsWith("mp4") && f2.getName().endsWith("jpg")) {
//                        //视频和照片
//                        transitionVideo = videoImgTransition(f1, f2, width, height, effect);
//                    }
//                    if (transitionVideo != null) {
//                        vGrabber2 = new FFmpegFrameGrabber(transitionVideo);
//                        vGrabber2.start();
//                        recordVideo(recorder, vGrabber2, isAudio);
//                        closeGrabber(vGrabber2);
//                        //删除临时数据
//                        transitionVideo.delete();
//                        transitionVideo = null;
//                    }
//                }
//                f1.delete();
//            }
//        } finally {
//            closeRecorder(recorder);
//            closeGrabber(vGrabber1);
//            closeGrabber(vGrabber2);
//        }
//        return file;
//    }

    public File produceAudio(List<FileDetail> fileList) throws Exception {
        Assert.noNullElements(fileList, "文件列表为空");
        List<File> list = fileList.stream().map(FileDetail::getFile).collect(Collectors.toList());
        File file = mergeAudio(list.toArray(new File[list.size()]));
        return file;
    }

    private File handleAudio(FileDetail fileDetail) throws Exception {
        File result = null;
        if (Objects.equals("mp4", fileDetail.getFormat())) {
            result = splitAudio(fileDetail.getFile());
        } else {
            result = fileDetail.getFile();
        }
        return result;
    }

    public File produceVideo(List<FileDetail> fileList, int width, int height) {
        Assert.noNullElements(fileList, "文件列表为空");
        final int w = width == 0 ? 1208 : width;
        final int h = height == 0 ? 720 : height;
        File result = null;
        CountDownLatch latch1 = new CountDownLatch(fileList.size());
        CountDownLatch latch2 = new CountDownLatch(fileList.size());
        ConcurrentHashMap<Integer, File> map = new ConcurrentHashMap<>(fileList.size());
        for (FileDetail fileDetail : fileList) {
            pool.execute(() -> {
                File file = null;
                try {
                    if (Objects.equals(fileDetail.getFormat(), "mp4")) {
                        file = changeVideoFrameRate(fileDetail.getFile(), 25, w + "x" + h);
                        if (Objects.equals(fileDetail.getMute(), 1)) {
                            file = splitVideo(file);
                        }
                    } else if (Objects.equals(fileDetail.getFormat(), "jpg")) {
                        file = changeImg("jpg", w, h, fileDetail.getFile());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //FileUtil.del(fileDetail.getFile());
                fileDetail.setFile(file);
                latch1.countDown();
            });
        }
        try {
            latch1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<List<FileDetail>> groups = groups(fileList);
        for (int i = 0; i < groups.size(); i++) {
            final int ii = i;
            List<FileDetail> group = groups.get(i);
            pool.execute(() -> {
                try {
                    File file = handleGroup(group, ii, w, h);
                    System.out.println(file);
                    map.put(ii, file);
                    latch2.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            latch2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File[] fArray = new File[fileList.size()];
        for (int i = 0; i < fileList.size(); i++) {
            File file = map.get(i);
            fArray[i] = file;
        }
        try {
            result = mergeVideo(null, fArray);
            delFileList(Arrays.asList(fArray));
            System.out.println("最终:" + result.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 文件分组处理
     *
     * @param fileList 文件列表
     * @return {@link List}<{@link List}<{@link FileDetail}>>
     */
    private List<List<FileDetail>> groups(List<FileDetail> fileList) {
        List<List<FileDetail>> list = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            FileDetail f1 = fileList.get(i);
            if (i + 1 >= fileList.size()) {
                list.add(ListUtil.toList(f1));
                break;
            }
            FileDetail f2 = fileList.get(i + 1);
            list.add(ListUtil.toList(f1, f2));
        }
        return list;
    }

    private File handleGroup(List<FileDetail> group, int index, int width, int height) throws Exception {
        int size = group.size();
        File file = null;
        String resolution = width + "x" + height;
        //处理大于1的组
        if (size > 1) {
            FileDetail f1 = group.get(0);
            FileDetail f2 = group.get(1);
            double time1 = ObjectUtils.isEmpty(f1.getTime()) ? 2 : f1.getTime();
            if (Objects.equals(f1.getFormat(), "jpg") && Objects.equals(f2.getFormat(), "jpg")) {
                //图片 + 图片
                File transition = mergeImgsToVideo(f1.getFile(), f2.getFile(), time1, 0.5, StrUtil.isNotBlank(f1.getEffect()) ? f1.getEffect() : null, resolution);
                file = setAudioTrack(transition);
                FileUtil.del(transition);
                FileUtil.del(f1.getFile());
            } else if (Objects.equals(f1.getFormat(), "jpg") && Objects.equals(f2.getFormat(), "mp4")) {
                //图片 + 视频
                File o2 = videoSnapshot(f2.getFile(), 1, resolution);
                File transition = mergeImgsToVideo(f1.getFile(), o2, time1, 0.5, StrUtil.isNotBlank(f1.getEffect()) ? f1.getEffect() : null, resolution);
                file = setAudioTrack(transition);
                FileUtil.del(transition);
                FileUtil.del(f1.getFile());
                FileUtil.del(o2);
            } else if (Objects.equals(f1.getFormat(), "mp4") && Objects.equals(f2.getFormat(), "mp4")) {
                //视频 + 视频
                int videoDuration = getVideoDuration(f1.getFile());
                File img1 = videoSnapshot(f1.getFile(), videoDuration, resolution);
                File img2 = videoSnapshot(f2.getFile(), 1, resolution);
                File transition = mergeImgsToVideo(img1, img2, 0.5, 0.5, StrUtil.isNotBlank(f1.getEffect()) ? f1.getEffect() : null, resolution);
                File o1 = setAudioTrack(transition);
                file = mergeVideo(resolution, f1.getFile(), o1);
                FileUtil.del(o1);
                FileUtil.del(f1.getFile());
                FileUtil.del(img1);
                FileUtil.del(img2);
                FileUtil.del(transition);
            } else if (Objects.equals(f1.getFormat(), "mp4") && Objects.equals(f2.getFormat(), "jpg")) {
                //视频 + 图片
                int videoDuration = getVideoDuration(f1.getFile());
                File img1 = videoSnapshot(f1.getFile(), videoDuration, resolution);
                File transition = mergeImgsToVideo(img1, f2.getFile(), 0.5, 0.5, StrUtil.isNotBlank(f1.getEffect()) ? f1.getEffect() : null, resolution);
                File o1 = setAudioTrack(transition);
                file = mergeVideo(resolution, f1.getFile(), o1);
                FileUtil.del(f1.getFile());
                FileUtil.del(img1);
                FileUtil.del(transition);
                FileUtil.del(o1);
            }
            return file;
        }
        //处理小于1的组
        FileDetail fileDetail = group.get(0);
        if (Objects.equals(fileDetail.getFormat(), "jpg")) {
            file = mergeImgsToVideo(fileDetail.getFile(), null, fileDetail.getTime(), 0, null, resolution);
            FileUtil.del(fileDetail.getFile());
            return file;
        }
        if (Objects.equals(fileDetail.getFormat(), "mp4")) {
            file = fileDetail.getFile();
        }
        return file;
    }

    public List<File> parseThumbnails(File video) throws IOException {
        Assert.notNull(video, "视频为空");
        List<File> list = new ArrayList<>();
        FFmpegFrameGrabber grabber = null;
        try {
            grabber = new FFmpegFrameGrabber(video);
            grabber.start();
            //视频时长,秒
            int time = (int) grabber.getLengthInTime() / (1000 * 1000);
            //每一秒取一张图片
            Frame frame = null;
            for (int i = 1; i <= time; i++) {
                grabber.setTimestamp(i * 1000 * 1000);
                while (frame == null || frame.image == null) {
                    frame = grabber.grabImage();
                }
                File file = storeFrameAsImg(frame, "jpg", 160, 90);
                list.add(file);
                frame = null;
            }
        } finally {
            closeGrabber(grabber);
        }
        return list;
    }

    public File createFile(String format) {
        String name = UUID.randomUUID() + "." + format;
        File file = new File(tempDir + name);
        return file;
    }

    public List<File> generateThumbnails(File video) throws Exception {
        List<File> list = new ArrayList<>();
        StringBuffer command = new StringBuffer();
        String img = "D:\\img\\test" + UUID.randomUUID();
        int videoDuration = getVideoDuration(video);
        System.out.println("视频长度" + videoDuration);
        command.append("ffmpeg -i");
        command.append(" ");
        command.append(video.getAbsolutePath());
        command.append(" ");
        command.append("-vf fps=1/1:round=zero:start_time=0,scale=160x90,tile=10x10");
        command.append(" ");
        command.append(img + "_%d.jpg");
        System.out.println(command);
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        int count = videoDuration % 100 == 0 ? videoDuration / 100 : videoDuration / 100 + 1;
        System.out.println("个数" + count);
        File file = null;
        for (int i = 1; i <= count; i++) {
            file = new File(img + "_" + i + ".jpg");
            if (file.exists()) {
                list.add(file);
            }
        }
        return list;
    }

    /**
     * 分割音频
     *
     * @param video 视频
     * @return {@link File}
     */
    public File splitAudio(File video) throws Exception {
        File mp3 = createFile("mp3");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-i");
        command.add(video.getAbsolutePath());
        command.add("-q:a 1");
        command.add("-ac 2");
        command.add("-map a");
        command.add("-threads 4");
        command.add(mp3.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp3;
    }

    /**
     * 分割视频
     *
     * @param video 视频
     * @return {@link File}
     */
    public File splitVideo(File video) throws Exception {
        File mp4 = createFile("mp4");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-i");
        command.add(video.getAbsolutePath());
        command.add("-vcodec");
        command.add("copy");
        command.add("-an");
        command.add(mp4.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp4;
    }

    /**
     * 合并音频
     *
     * @param audio 音频
     * @return {@link File}
     */
    public File mergeAudio(File... audio) throws Exception {
        Assert.notNull(audio, "音频文件为空");
        String paths = Stream.of(audio).map(File::getAbsolutePath).collect(Collectors.joining("|"));
        File mp3 = createFile("mp3");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-i");
        command.add("concat:" + paths);
        command.add("-q:a 1");
        command.add("-ac 2");
        command.add("-threads 4");
        command.add("-acodec");
        command.add("copy");
        command.add("-vn");
        command.add(mp3.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp3;
    }

    /**
     * 混合音频
     *
     * @param audio 音频
     * @return {@link File}
     * @throws Exception 异常
     */
    public File mixAudio(File... audio) throws Exception {
        Assert.notNull(audio, "操作文件为空");
        File mp3 = createFile("mp3");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        for (File a : audio) {
            command.add("-i");
            command.add(a.getAbsolutePath());
        }
        command.add("-filter_complex");
        command.add("amix=inputs=" + audio.length + ":duration=longest");
        command.add("-vn");
        command.add(mp3.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp3;
    }

    /**
     * 混合视频和音频
     *
     * @param video 视频
     * @param audio 音频
     * @return {@link File}
     * @throws Exception 异常
     */
    public File mixVideoAndAudio(File video, File audio) throws Exception {
        Assert.isTrue(FileUtil.exist(video), "操作视频为空");
        Assert.isTrue(FileUtil.exist(audio), "操作音频为空");
        File mp4 = createFile("mp4");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-i");
        command.add(video.getAbsolutePath());
        command.add("-i");
        command.add(audio.getAbsolutePath());
        command.add("-c:v copy");
        command.add("-map 0:v:0");
        command.add("-filter_complex");
        command.add("\"[0:a][1:a]amerge=inputs=2[aout]\"");
        command.add("-map \"[aout]\"");
        command.add("-ac 2");
        command.add(mp4.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp4;
    }

    /**
     * 合并一个视频
     * ffmpeg -loop 1 -t 5 -i 1.png -loop 1 -t 5 -i 2.png -filter_complex "[0][1]xfade=transition=fade:duration=1:offset=4,format=yuv420p
     * output.mp4
     * <p>
     * ffmpeg -f concat -safe 0 -i 4826b1f4-a3a5-473c-a17e-d053a0b12a46.txt -s 1208x720 m1.mp4
     *
     * @param img1       img1
     * @param img2       img2
     * @param duration1  duration1
     * @param duration2  duration2
     * @param transition 过渡
     * @param resolution 决议
     * @return {@link File}
     * @throws Exception 异常
     */
    public File mergeImgsToVideo(File img1, File img2, double duration1, double duration2, String transition, String resolution) throws Exception {
        File mp4 = createFile("mp4");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-loop 1");
        command.add("-i");
        command.add(img1.getAbsolutePath());
        if (ObjectUtils.isNotEmpty(img2)) {
            command.add("-loop 1");
            command.add("-i");
            command.add(img2.getAbsolutePath());
        }
        command.add("-s");
        if (StrUtil.isNotBlank(resolution)) {
            command.add(resolution);
        } else {
            command.add("1208x720");
        }
        command.add("-r 25");
        command.add("-t");
        command.add(String.valueOf(duration1 + duration2));
        command.add("-an");
        if (StrUtil.isNotBlank(transition)) {
            command.add("-filter_complex \"[0][1]xfade=transition=" + transition + ":duration=0.5:offset=" + duration1 + ",format=yuv420p\"");
        }
        command.add(mp4.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp4;
    }

    public File imgToImgTransition(File img1, File img2, String effect) throws Exception {
        File mp4 = createFile("mp4");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-loop 1");
        command.add("-i");
        command.add(img1.getAbsolutePath());
        command.add("-loop 1");
        command.add("-i");
        command.add(img2.getAbsolutePath());
        command.add("-r 25");
        command.add("-t");
        command.add(String.valueOf(1));
        command.add("-an");
        command.add("-filter_complex \"[0][1]xfade=transition=" + effect + ":duration=0.5:offset=0.5,format=yuv420p\"");
        command.add(mp4.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp4;
    }

    /**
     * img视频
     *
     * @param img        img
     * @param resolution 决议
     * @param duration   持续时间
     * @return {@link File}
     * @throws Exception 异常
     */
    public File imgToVideo(File img, String resolution, double duration) throws Exception {
        File mp4 = createFile("mp4");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-f image2");
        command.add("-loop 1");
        command.add("-i");
        command.add(img.getAbsolutePath());
        command.add("-t");
        command.add(String.valueOf(duration));
        command.add("-r");
        command.add("25");
        command.add("-s");
        command.add(resolution);
        command.add(mp4.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp4;
    }

    /**
     * 合并视频
     *
     * @param video 视频
     * @return {@link File}
     */
    public File mergeVideo(String resolution, File... video) throws Exception {
        Assert.notNull(video, "操作视频文件为空");
        File mp4 = createFile("mp4");
        File concat = createVideoConcatFile(video);
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-f concat");
        command.add("-safe 0");
        command.add("-i");
        command.add(concat.getAbsolutePath());
        command.add("-s");
        if (StrUtil.isNotBlank(resolution)) {
            command.add(resolution);
        } else {
            command.add("1208x720");
        }
        command.add("-r 25");
        command.add("-c copy");
        command.add(mp4.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        FileUtil.del(concat);
        return mp4;
    }

    /**
     * 视频快照
     *
     * @param video   视频
     * @param seconds 秒
     * @return {@link File}
     */
    public File videoSnapshot(File video, Integer seconds, String resolution) throws Exception {
        File jpg = createFile("jpg");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-ss");
        command.add(String.valueOf(seconds));
        command.add("-i");
        command.add(video.getAbsolutePath());
        command.add("-f image2");
        command.add("-vframes 1");
        command.add("-r 1");
        if (StrUtil.isNotBlank(resolution)) {
            command.add("-s");
            command.add(resolution);
        }
        command.add("-an");
        command.add(jpg.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return jpg;
    }

    /**
     * 合并视频和音频
     *
     * @param video 视频
     * @param audio 音频
     * @return {@link File}
     * @throws Exception 异常
     */
    public File mergeVideoAndAudio(File video, File audio) throws Exception {
        File mp4 = createFile("mp4");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-i");
        command.add(video.getAbsolutePath());
        command.add("-i");
        command.add(audio.getAbsolutePath());
        command.add("-c:v copy -c:a aac -strict experimental -map 0:v:0 -map 1:a:0");
        command.add("-t");
        int videoDuration = getVideoDuration(video);
        command.add(String.valueOf(videoDuration));
        command.add(mp4.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp4;
    }

    /**
     * 设置视频静音音轨
     *
     * @param video 视频
     * @return {@link File}
     * @throws Exception 异常
     */
    public File setAudioTrack(File video) throws Exception {
        File mp4 = createFile("mp4");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-i");
        command.add(video.getAbsolutePath());
        command.add("-f");
        command.add("lavfi");
        command.add("-i");
        command.add("aevalsrc=0");
        command.add("-shortest");
        command.add("-y");
        command.add(mp4.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp4;
    }

    public File createMuteAudio(Integer duration) throws Exception {
        File mp3 = createFile("mp3");
        StringJoiner command = new StringJoiner(" ");
        command.add("ffmpeg");
        command.add("-threads 4");
        command.add("-f");
        command.add("lavfi");
        command.add("-i");
        command.add("anullsrc");
        command.add("-t");
        command.add(String.valueOf(duration));
        command.add("-y");
        command.add(mp3.getAbsolutePath());
        String sys = System.getProperty("os.name");
        if (sys.startsWith("Windows")) {
            FFmpegUtil.ffmpegExecute(command.toString());
        } else {
            FFmpegUtil.linuxffmpegExecute(command.toString());
        }
        return mp3;
    }

    private File createVideoConcatFile(File... video) throws Exception {
        File concat = createFile("txt");
        FileWriter writer = new FileWriter(concat);
        BufferedWriter bw = new BufferedWriter(writer);
        try {
            for (File file : video) {
//                file = changeVideoFrameRate(file, 25, null);
                bw.write("file '" + file.getAbsolutePath() + "'");
                bw.newLine();
            }
        } finally {
            bw.flush();
            IoUtil.close(writer);
            IoUtil.close(bw);
        }
        return concat;
    }

    /**
     * del文件列表
     *
     * @param list 列表
     */
    private void delFileList(List<File> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (File file : list) {
            FileUtil.del(file);
        }
    }

}
