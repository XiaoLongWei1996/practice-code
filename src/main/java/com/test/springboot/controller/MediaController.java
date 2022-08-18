package com.test.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhlabs.image.PointFilter;
import com.test.springboot.exception.RequestException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.test.springboot.domain.RestResponse;
import com.test.springboot.util.ImageFilterFactory;
import com.test.springboot.util.MediaService;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 肖龙威
 * @date 2022/03/14 16:49
 */
@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    /**
     * 添加文字水印
     *
     * @param img
     * @param text
     * @param font
     * @param color
     * @param x
     * @param y
     * @param response
     */
    @PostMapping("/fontMark")
    public void fontMark(MultipartFile img, String text, String font, String color, int x, int y, HttpServletResponse response) {
        if (ObjectUtils.isEmpty(img)) {
            throw new RequestException("文件不能为空");
        }
        Font f;
        if (StringUtils.isBlank(font)) {
            f = Font.getFont("微软雅黑");
        } else {
            JSONObject jsonObject = JSON.parseObject(font);
            f = new Font((String) jsonObject.get("name"), (Integer) jsonObject.get("style"), (Integer) jsonObject.get("size"));
        }
        Color c;
        if (StringUtils.isBlank(color)) {
            c = Color.BLACK;
        } else {
            JSONObject jsonObject = JSON.parseObject(color);
            c = new Color((Integer) jsonObject.get("r"), (Integer) jsonObject.get("g"), (Integer) jsonObject.get("b"));
        }
        response.setContentType("image/jpeg");
        try {
            mediaService.mark(img.getInputStream(), response.getOutputStream(), text, f, c, x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/videoFontMark")
    public void videoFontMark(MultipartFile video, String text, String font, String color, int x, int y, HttpServletResponse response) {
        Font f;
        if (StringUtils.isBlank(font)) {
            f = Font.getFont("微软雅黑");
        } else {
            JSONObject jsonObject = JSON.parseObject(font);
            f = new Font((String) jsonObject.get("name"), (Integer) jsonObject.get("style"), (Integer) jsonObject.get("size"));
        }
        Color c;
        if (StringUtils.isBlank(color)) {
            c = Color.BLACK;
        } else {
            JSONObject jsonObject = JSON.parseObject(color);
            c = new Color((Integer) jsonObject.get("r"), (Integer) jsonObject.get("g"), (Integer) jsonObject.get("b"));
        }
        try {
            mediaService.addVideoFontMark(video.getInputStream(), response.getOutputStream(), text, f, c, x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加图片水印
     *
     * @param img
     * @param markImg
     * @param x
     * @param y
     * @param response
     */
    @PostMapping("/imageMark")
    public void imageMark(@RequestPart("img") MultipartFile img, @RequestPart("markImg") MultipartFile markImg, int x, int y, HttpServletResponse response) {
        if (ObjectUtils.isEmpty(img) || ObjectUtils.isEmpty(markImg)) {
            throw new RequestException("文件不能为空");
        }
        response.setContentType("image/jpeg");
        try {
            mediaService.mark(img.getInputStream(), markImg.getInputStream(), response.getOutputStream(), x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/addVideoImageMark")
    public void addVideoImageMark(@RequestPart("video") MultipartFile video, @RequestPart("markImg") MultipartFile markImg, int x, int y, HttpServletResponse response) {
        if (ObjectUtils.isEmpty(video) || ObjectUtils.isEmpty(markImg)) {
            throw new RequestException("文件不能为空");
        }
        response.setContentType("image/jpeg");
        try {
            mediaService.addVideoImgMark(video.getInputStream(), markImg.getInputStream(), response.getOutputStream(), x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片添加滤镜
     *
     * @param img
     * @param filterName
     */
    @PostMapping("/addImgFilter")
    public void addImgFilter(@RequestPart("img") MultipartFile img,
                             String filterName, String properties, HttpServletResponse response) {
        if (ObjectUtils.isEmpty(img)) {
            throw new RequestException("参数不能为空");
        }
        Map<String, String> map = JSONObject.parseObject(properties, Map.class);
        PointFilter filter = ImageFilterFactory.createFilter(map, filterName);
        if (ObjectUtils.isEmpty(filter)) {
            throw new RequestException("请选择正确滤镜");
        }
        try {
            response.setContentType("image/jpeg");
            mediaService.addImageFilter(img.getInputStream(), response.getOutputStream(), filter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/addVideoFilter")
    public void addVideoFilter(MultipartFile video, String filterName, HttpServletResponse response) {
        PointFilter filter = ImageFilterFactory.createDitherFilter();
        if (ObjectUtils.isEmpty(filter)) {
            throw new RequestException("请选择正确滤镜");
        }
        try {
            mediaService.addVideoFilter(video.getInputStream(), response.getOutputStream(), filter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/imageZoom")
    public void imageZoom(@RequestPart("img") MultipartFile img, Integer height, Integer width, HttpServletResponse response) {
        try {
            String filename = img.getOriginalFilename();
            int index = filename.lastIndexOf(".") + 1;
            mediaService.imageZoom(img.getInputStream(), response.getOutputStream(), height, width, filename.substring(index));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/videoZoom")
    public void videoZoom(@RequestPart("video") MultipartFile video, Integer height, Integer width, HttpServletResponse response) {
        try {
            mediaService.videoZoom(video.getInputStream(), response.getOutputStream(), height, width);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 照片合成视频
     *
     * @param imgs
     * @param width
     * @param height
     * @param response
     */
    @PostMapping("/imgsToVedio")
    public void imgsToVedio(MultipartFile[] imgs, Integer width, Integer height, String[] trasitions, HttpServletResponse response) throws Exception {
        if (ArrayUtils.isEmpty(imgs)) {
            throw new RequestException("上传文件为空");
        }
        List<File> list = new ArrayList<>();
        for (MultipartFile multipartFile : imgs) {
            File file = mediaService.storeLocalTempDir(multipartFile);
            File f = mediaService.changeImg("jpg", width, height, file);
            list.add(f);
            //删除临时数据
            file.delete();
        }
        response.setContentType("video/mpeg4");
        //response.setHeader("Content-Disposition", "attachment; filename=" + UUID.randomUUID() + ".mp4");
        mediaService.imgCompositeVideo(list, response.getOutputStream(), trasitions, width, height);
    }

    @PostMapping("/clipVideo")
    private void clipVideo(MultipartFile video, Integer start, Integer end, HttpServletResponse response) {
        try {
            mediaService.videoClip(video, response.getOutputStream(), start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/clipAudio")
    private void clipAudio(MultipartFile audio, Integer start, Integer end, HttpServletResponse response) {
        try {
            mediaService.audioClip(audio, response.getOutputStream(), start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/mergeVideos")
    public void mergeVideos(MultipartFile[] videos, Integer width, Integer height, String[] trasitions, HttpServletResponse response) throws Exception {
        List<File> list = new ArrayList<>();
        for (MultipartFile video : videos) {
            File file = mediaService.storeLocalTempDir(video);
            list.add(file);
        }
        mediaService.mergeVideos(list, response.getOutputStream(), trasitions, width, height);
    }

    @PostMapping("/mergeAudioAndVideo")
    public void mergeAudioAndVideo(MultipartFile video, MultipartFile audio, HttpServletResponse response) {
        try {
            mediaService.mergeAudioAndVideo(video.getInputStream(), audio.getInputStream(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 视频画中画
     *
     * @param source   源文件
     * @param nested   嵌套文件
     * @param x        嵌套文件坐标x
     * @param y        嵌套文件坐标y
     * @param width    嵌套文件宽
     * @param height   嵌套文件高
     * @param response
     */
    @PostMapping("/nestedVideo")
    public void nestedVideo(MultipartFile source, MultipartFile nested, Integer x, Integer y, Integer width, Integer height, HttpServletResponse response) {
        try {
            mediaService.nestedVideo(source.getInputStream(), nested.getInputStream(), x, y, width, height, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/imgClarityAnalyse")
    public RestResponse<Double> imgClarityAnalyse(MultipartFile img) throws IOException {
        File file = mediaService.storeLocalTempDir(img);
        double v = mediaService.imgClarityAnalyse(file);
        return new RestResponse<>(v);
    }

    @PostMapping("/generate")
    public RestResponse<String> test(MultipartFile[] files, String effect, int width, int height) throws Exception {

        List<File> list = new ArrayList<>();
        for (MultipartFile file : files) {
            File f = mediaService.storeLocalTempDir(file);
            list.add(f);
        }

        File file = mediaService.generateFile(list, effect, width, height);
        System.out.println(file.getName());
        list.forEach(f -> {
            if (f.exists()) {
                f.delete();
            }
        });
        return new RestResponse<>("ok");
    }

    @PostMapping("/videoTime")
    public RestResponse<Object> getVideoTime(MultipartFile video) throws Exception {
        File file = mediaService.storeLocalTempDir(video);
        List<File> files = mediaService.parseThumbnails(file);
        return new RestResponse<>("ok");
    }

}
