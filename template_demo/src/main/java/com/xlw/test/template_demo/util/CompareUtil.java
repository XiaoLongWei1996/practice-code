package com.xlw.test.template_demo.util;


import cn.hutool.core.lang.Assert;
import com.xlw.test.template_demo.entity.Student;
import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @description: 比较工具类
 * @Title: CompareUtil
 * @Author xlw
 * @Package com.invoice.tcc.util
 * @Date 2025/2/21 17:44
 */
public class CompareUtil {

    private static final String DEFAULT_ERROR_MSG = "ERROR";

    /**
     * 字段比较器构建
     *
     * @param bean1 豆 1
     * @param bean2 豆 2
     * @return {@link FieldEqualsHandler }<{@link T1 }, {@link T2 }>
     */
    public static <T1, T2> FieldEqualsHandler<T1, T2> fieldEqualsGenerator(@NonNull T1 bean1, @NonNull T2 bean2) {
        return new FieldEqualsHandler<T1, T2>(bean1, bean2);
    }

    /**
     * 等于
     *
     * @param o1 o1
     * @param o2 O2
     * @return boolean
     */
    public static <T1, T2> boolean isEquals(@NonNull T1 o1, @NonNull T2 o2) {
        return o1 == null || o2 == null ? false : !o1.getClass().equals(o2.getClass()) ? false : Objects.equals(o1, o2);
    }

    /**
     * 不等于执行
     *
     * @param o1
     * @param o2               O2
     * @param notEqualsExecute 不等于执行
     * @return {@link Optional }<{@link R }>
     */
    public static <T1, T2, R> Optional<R> notEqualsExecute(@NonNull T1 o1, @NonNull T2 o2, @NonNull Supplier<R> notEqualsExecute) {
        Assert.notNull(o1, "对象为空");
        Assert.notNull(o2, "对象为空");
        return Objects.equals(o1, o2) ? Optional.empty() : Optional.ofNullable(notEqualsExecute.get());
    }

    /**
     * 不等于执行
     *
     * @param o1
     * @param o2               O2
     * @param notEqualsExecute 不等于执行
     */
    public static <T1, T2> void notEqualsExecute(@NonNull T1 o1, @NonNull T2 o2, @NonNull Runnable notEqualsExecute) {
        Assert.notNull(o1, "对象为空");
        Assert.notNull(o2, "对象为空");
        if (!Objects.equals(o1, o2)) {
            notEqualsExecute.run();
        }
    }

    /**
     * 等于执行
     *
     * @param o1            o1
     * @param o2            o2
     * @param equalsExecute 等于执行
     * @return {@link Optional }<{@link R }>
     */
    public static <T1, T2, R> Optional<R> equalsExecute(@NonNull T1 o1, @NonNull T2 o2, @NonNull Supplier<R> equalsExecute) {
        Assert.notNull(o1, "对象为空");
        Assert.notNull(o2, "对象为空");
        return Objects.equals(o1, o2) ? Optional.ofNullable(equalsExecute.get()) : Optional.empty();
    }

    /**
     * 等于执行
     *
     * @param o1            o1
     * @param o2            o2
     * @param equalsExecute 等于执行
     */
    public static <T1, T2> void equalsExecute(@NonNull T1 o1, @NonNull T2 o2, @NonNull Runnable equalsExecute) {
        Assert.notNull(o1, "对象为空");
        Assert.notNull(o2, "对象为空");
        if (Objects.equals(o1, o2)) {
            equalsExecute.run();
        }
    }

    /**
     * 字段等于处理程序
     *
     * @author xlw
     * @date 2025/02/22
     */
    public static class FieldEqualsHandler<T1, T2> {

        private T1 t1;

        private T2 t2;

        private List<EqualsObject> eqs = new ArrayList<>();

        private FieldEqualsHandler(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        public final FieldEqualsHandler<T1, T2> fieldGroup(Function<T1, ?> filedFn1, Function<T2, ?> filedFn2, String errorMsg) {
            eqs.add(new EqualsObject(filedFn1.apply(t1), filedFn2.apply(t2), errorMsg));
            return this;
        }

        public final FieldEqualsHandler<T1, T2> fieldGroup(Function<T1, ?> filedFn1, Function<T2, ?> filedFn2) {
            return fieldGroup(filedFn1, filedFn2,DEFAULT_ERROR_MSG);
        }

        public boolean isEquals() {
            Assert.notEmpty(eqs, "未找到可比对的字段信息");
            for (EqualsObject eq : eqs) {
                if (!eq.equals()) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 等于返回错误信息
         *
         * @return {@link String } 若为null则为true否则为false
         */
        public Set<String> isEqualsReturnMsg() {
            Assert.notEmpty(eqs, "未找到可比对的字段信息");
            Set<String> msgSet = new HashSet<>();
            for (EqualsObject eq : eqs) {
                if (!eq.equals()) {
                    msgSet.add(eq.getErrorMsg());
                }
            }
            return msgSet;
        }

        /**
         * 等于执行
         *
         * @param equalsExecute 等于执行
         */
        public void equalsExecute(@NonNull Runnable equalsExecute) {
            if (isEquals()) {
                equalsExecute.run();
            }
        }

        /**
         * 等于执行
         *
         * @param equalsExecute 等于执行
         * @return {@link Optional }<{@link R }>
         */
        public <R> Optional<R> equalsExecute(@NonNull Supplier<R> equalsExecute) {
            return isEquals() ? Optional.ofNullable(equalsExecute.get()) : Optional.empty();
        }

        /**
         * 不等于执行
         *
         * @param notEqualsExecute 不等于执行
         */
        public void notEqualsExecute(@NonNull Runnable notEqualsExecute) {
            if (!isEquals()) {
                notEqualsExecute.run();
            }
        }

        /**
         * 不等于执行
         *
         * @param notEqualsExecute 不等于执行
         * @return {@link Optional }<{@link R }>
         */
        public <R> Optional<R> notEqualsExecute(@NonNull Supplier<R> notEqualsExecute) {
            return !isEquals() ? Optional.ofNullable(notEqualsExecute.get()) : Optional.empty();
        }

    }

    /**
     * equals 对象
     *
     * @author xlw
     * @date 2025/02/22
     */
    public static class EqualsObject {

        private Object o1;

        private Object o2;

        private String errorMsg;

        private EqualsObject(Object o1, Object o2, String errorMsg) {
            this.o1 = o1;
            this.o2 = o2;
            this.errorMsg = errorMsg;
        }

        public boolean equals() {
            if (o1 == null && o2 == null) {
                return true;
            }
            return o1 == null || o2 == null ? false : !o1.getClass().equals(o2.getClass()) ? false : Objects.equals(o1, o2);
        }

        public String getErrorMsg() {
            return errorMsg;
        }
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setName("张三");
        Student student1 = new Student();
        student1.setName("张三1");
        Optional<Integer> i = CompareUtil.fieldEqualsGenerator(student, student1)
                .fieldGroup(Student::getName, Student::getName)
                .notEqualsExecute(() -> 123);
        System.out.println(i.get());
    }
}
