package org.test.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 日期操作实效
 * 日期操作工具
 *
 * @author Xlw
 * @date 2023/05/01
 */
public class DateOperateUtil {

    /**
     * 日期豆
     */
    private final DateBean dateBean;

    /**
     * 日期操作实效
     *
     * @param year     年
     * @param date     日期
     * @param timeType 时间类型(0:年, 1:季度, 2:月度)
     */
    public DateOperateUtil(int year, int date, String timeType) {
        Assert.isTrue(year > 0 && date > 0, "无效参数");
        this.dateBean = DateBean.create(year, date, timeType);
    }

    public DateOperateUtil(int year) {
        this.dateBean = DateBean.create(year, 1, "0");
    }

    /**
     * 间隔时间
     *
     * @param interval 时间间隔
     * @return {@link DateBean}
     */
    public DateBean intervalPeriod(int interval) {
        if (Objects.equals(dateBean.getTimeType(), "0")) {
            //计算年的间隔时间对象
            return dateBean.operateYear(interval);
        }
        //计算季度/月份的间隔对象
        return dateBean.operateDate(interval);
    }

    /**
     * 间隔时间列表
     *
     * @param interval 时间间隔
     * @return {@link List}<{@link DateBean}>
     */
    public List<DateBean> intervalPeriodList(int interval) {
        DateBean pre;
        DateBean current;
        if (interval > 0) {
            pre = dateBean;
            current = intervalPeriod(interval);
        } else {
            pre = intervalPeriod(interval);
            current = dateBean;
        }
        List<DateBean> list = new ArrayList<>(Math.abs(interval));
        if (Objects.equals(dateBean.getTimeType(), "0")) {
            for (int i = pre.getYear(); i <= current.getYear(); i++) {
                list.add(DateBean.create(i, current.getDate(), current.getTimeType()));
            }
        } else if (Objects.equals(dateBean.getTimeType(), "1")) {
            addPeriod(list, 4, pre, current);
        } else {
            addPeriod(list, 12, pre, current);
        }
        return list;
    }

    /**
     * 添加时间
     *
     * @param list    列表
     * @param border  边境
     * @param pre     精准医疗
     * @param current 当前
     */
    private void addPeriod(List<DateBean> list, int border, DateBean pre, DateBean current) {
        int y = pre.getYear();
        int d = pre.getDate();
        while (y <= current.getYear()) {
            if (y == current.getYear() && d > current.getDate()) {
                break;
            }
            list.add(DateBean.create(y, d, current.getTimeType()));
            d++;
            if (d > border) {
                y++;
                d -= border;
            }
        }
    }

    /**
     * 月转换季度
     *
     * @return {@link DateBean}
     */
    public DateBean monthToQuarter() {
        return DateBean.create(dateBean.getYear(), dateBean.quarter(), "1");
    }


    /**
     * 日期豆
     *
     * @author Xlw
     * @date 2023/05/01
     */
    @Data
    @AllArgsConstructor
    public static class DateBean {

        /**
         * 一年
         */
        private int year;

        /**
         * 日期
         */
        private int date;

        /**
         * 时间类型
         */
        private String timeType;

        /**
         * 创建
         *
         * @param year     一年
         * @param date     日期
         * @param timeType 时间类型
         * @return {@link DateBean}
         */
        public static DateBean create(int year, int date, String timeType) {
            return new DateBean(year, date, timeType);
        }

        /**
         * 运行一年
         * 操作一年
         *
         * @param interval 时间间隔
         * @return int
         */
        public DateBean operateYear(int interval) {
            Assert.isTrue(Objects.equals(timeType, "0"), "季度/月度对象不能计算年");
            return DateBean.create(year + interval, date, timeType);
        }

        /**
         * 操作日期
         *
         * @param interval 时间间隔
         * @return {@link DateBean}
         */
        public DateBean operateDate(int interval) {
            Assert.isFalse(Objects.equals(timeType, "0"), "年度对象不能计算日期");
            //判断是增加还是减少时间
            int i = date + interval;
            if (Objects.equals(timeType, "1")) {
                //操作季度的间隔时间
                if (i > 0) {
                    if (i <= 4) {
                        return DateBean.create(year, i, timeType);
                    }
                    return DateBean.create(year + i / 4, i % 4 == 0 ? 1 : i % 4, timeType);
                }

                if (i < 0) {
                    int y = year;
                    while (i < 0) {
                        y -= 1;
                        i += 4;
                    }
                    if (i == 0) {
                        y -= 1;
                        i = 4;
                    }
                    return DateBean.create(y, i, timeType);
                }
                return DateBean.create(year - 1, 4, timeType);
            }

            //操作月度的间隔时间
            if (i > 0) {
                if (i <= 12) {
                    return DateBean.create(year, i, timeType);
                }
                return DateBean.create(year + i / 12, i % 12 == 0 ? 1 : i % 12, timeType);
            }

            if (i < 0) {
                int y = year;
                while (i < 0) {
                    y -= 1;
                    i += 12;
                }
                if (i == 0) {
                    y -= 1;
                    i = 12;
                }
                return DateBean.create(y, i, timeType);
            }
            return DateBean.create(year - 1, 12, timeType);
        }

        public int quarter() {
            Assert.isFalse(Objects.equals(timeType, "0"), "年度不能转成季度");
            if (Objects.equals(timeType, "1")) {
                return date;
            }
            return date % 3 == 0 ? date / 3 : date / 3 + 1;
        }

        /**
         * 字符串
         *
         * @return {@link String}
         */
        @Override
        public String toString() {
            if (Objects.equals(timeType, 0)) {
                return String.valueOf(year);
            }
            return StrUtil.join("-", year, date);
        }
    }

}
