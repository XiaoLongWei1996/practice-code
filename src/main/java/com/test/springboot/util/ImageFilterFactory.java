package com.test.springboot.util;

import com.jhlabs.image.*;

import java.util.Map;

/**
 * 图片滤镜工厂
 *
 * @author 肖龙威
 * @date 2022/03/18 15:36
 */
public class ImageFilterFactory {


    /**
     * 混合滤镜,把指定的rgb混合到图片中
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static ChannelMixFilter createMixFilter(int r, int g, int b) {
        ChannelMixFilter mixFilter = new ChannelMixFilter();
        mixFilter.setIntoR(r);
        mixFilter.setIntoG(g);
        mixFilter.setIntoB(b);
        return mixFilter;
    }

    /**
     * 对比滤镜,可以设置亮度和对比度
     *
     * @param contrast   对比度
     * @param brightness 亮度
     * @return
     */
    public static ContrastFilter createContrastFilter(float contrast, float brightness) {
        ContrastFilter contrastFilter = new ContrastFilter();
        contrastFilter.setContrast(contrast);
        contrastFilter.setBrightness(brightness);
        return contrastFilter;
    }

    /**
     * 抖动滤镜
     *
     * @return
     */
    public static DitherFilter createDitherFilter() {
        DitherFilter ditherFilter = new DitherFilter();
        return ditherFilter;
    }

    /**
     * 曝光滤镜,设置曝光度
     *
     * @param exposure
     * @return
     */
    public static ExposureFilter createExposureFilter(float exposure) {
        ExposureFilter exposureFilter = new ExposureFilter();
        exposureFilter.setExposure(exposure);
        return exposureFilter;
    }

    /**
     * 增益滤镜,更改偏差会使颜色偏向更亮或更暗。改变增益会改变对比度。
     *
     * @param gain 增益
     * @param bias 偏差
     * @return
     */
    public static GainFilter createGainFilter(float gain, float bias) {
        GainFilter gainFilter = new GainFilter();
        gainFilter.setGain(gain);
        gainFilter.setBias(bias);
        return gainFilter;
    }

    /**
     * 伽马滤镜,通过设置伽马值改变图片亮度,小于 1 的值会使图像更暗，大于 1 的值会使图像更亮
     *
     * @param gamma
     * @return
     */
    public static GammaFilter createGammaFilter(float gamma) {
        GammaFilter gammaFilter = new GammaFilter();
        gammaFilter.setGamma(gamma);
        return gammaFilter;
    }

    /**
     * 灰度滤镜
     *
     * @return
     */
    public static GrayscaleFilter createGrayscale() {
        GrayscaleFilter grayscaleFilter = new GrayscaleFilter();
        return grayscaleFilter;
    }

    /**
     * 饱和滤镜,设置色调,饱和度,亮度
     *
     * @param h 色调
     * @param b 亮度
     * @param s 饱和度
     * @return
     */
    public static HSBAdjustFilter createHsbAdjustFilter(float h, float b, float s) {
        HSBAdjustFilter hsbAdjustFilter = new HSBAdjustFilter();
        hsbAdjustFilter.setBFactor(b);
        hsbAdjustFilter.setHFactor(h);
        hsbAdjustFilter.setSFactor(s);
        return hsbAdjustFilter;
    }

    /**
     * 反转滤镜,颜色反转
     *
     * @return
     */
    public static InvertFilter createInvertFilter() {
        InvertFilter invertFilter = new InvertFilter();
        return invertFilter;
    }

    /**
     * 比率滤镜,将输入乘以给定的比例因子。它主要用于更换遮罩或增强照明效果。
     *
     * @param scale 比率
     * @return
     */
    public static RescaleFilter createRescaleFilter(float scale) {
        RescaleFilter rescaleFilter = new RescaleFilter();
        rescaleFilter.setScale(scale);
        return rescaleFilter;
    }

    /**
     * 卡通滤镜
     *
     * @return
     */
    public static PosterizeFilter createPosterizeFilter() {
        PosterizeFilter posterizeFilter = new PosterizeFilter();
        return posterizeFilter;
    }

    /**
     * rgb滤镜,该过滤器从图像的每个红色、绿色和蓝色通道中添加或减去给定的数量。
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static RGBAdjustFilter createRGBAdjustFilter(int r, int g, int b) {
        RGBAdjustFilter rgbAdjustFilter = new RGBAdjustFilter();
        rgbAdjustFilter.setBFactor(b);
        rgbAdjustFilter.setRFactor(r);
        rgbAdjustFilter.setGFactor(g);
        return rgbAdjustFilter;
    }

    /**
     * 日晒滤镜
     *
     * @return
     */
    public static SolarizeFilter createSolarizeFilter() {
        SolarizeFilter solarizeFilter = new SolarizeFilter();
        return solarizeFilter;
    }

    /**
     * 黑色滤镜
     *
     * @return
     */
    public static ThresholdFilter createThresholdFilter() {
        ThresholdFilter thresholdFilter = new ThresholdFilter();
        return thresholdFilter;
    }

    /**
     * 根据滤镜名称创建滤镜
     *
     * @param properties 属性
     * @param filterName 滤镜名称
     * @return
     */
    public static PointFilter createFilter(Map<String, String> properties, String filterName) {
        switch (filterName) {
            case "混合滤镜":
                return createMixFilter(Integer.parseInt(properties.get("r")), Integer.parseInt(properties.get("g")), Integer.parseInt(properties.get("b")));
            case "对比滤镜":
                return createContrastFilter(Float.parseFloat(properties.get("contrast")) , Float.parseFloat(properties.get("brightness")));
            case "抖动滤镜":
                return createDitherFilter();
            case "曝光滤镜":
                return createExposureFilter(Float.parseFloat(properties.get("exposure")));
            case "增益滤镜":
                return createGainFilter(Float.parseFloat(properties.get("gain")), Float.parseFloat(properties.get("bias")));
            case "伽马滤镜":
                return createGammaFilter(Float.parseFloat(properties.get("gamma")));
            case "灰度滤镜":
                return createGrayscale();
            case "饱和滤镜":
                return createHsbAdjustFilter(Float.parseFloat(properties.get("h")), Float.parseFloat(properties.get("b")), Float.parseFloat(properties.get("s")));
            case "反转滤镜":
                return createInvertFilter();
            case "比率滤镜":
                return createRescaleFilter(Float.parseFloat(properties.get("scale")));
            case "卡通滤镜":
                return createPosterizeFilter();
            case "rgb滤镜":
                return createRGBAdjustFilter(Integer.parseInt(properties.get("r")), Integer.parseInt(properties.get("g")), Integer.parseInt(properties.get("b")));
            case "日晒滤镜":
                return createSolarizeFilter();
            case "黑色滤镜":
                return createThresholdFilter();
            default:
                return null;
        }
    }

}
