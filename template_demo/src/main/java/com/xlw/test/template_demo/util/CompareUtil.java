package com.xlw.test.template_demo.util;


import cn.hutool.core.lang.Assert;
import com.xlw.test.template_demo.entity.Student;
import com.xlw.test.template_demo.entity.User;

import java.util.*;
import java.util.function.Function;

/**
 * @description: 比较工具类
 * @Title: CompareUtil
 * @Author xlw
 * @Package com.invoice.tcc.util
 * @Date 2025/2/21 17:44
 */
public class CompareUtil {

    private static final String DEFAULT_ERROR_MSG = "ERROR";

    public static <T1, T2> FieldEqualsBuilder<T1, T2> fieldComparatorBuild(T1 t1, T2 t2) {
        return new FieldEqualsBuilder<>(t1, t2);
    }

    private static class FieldEqualsBuilder<T1, T2> {

        private T1 t1;

        private T2 t2;

        private List<EqualsObject> eqs = new ArrayList<>();

        private FieldEqualsBuilder(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        public final FieldEqualsBuilder<T1, T2> append(Function<T1, ?> filedFn1, Function<T2, ?> filedFn2, String errorMsg) {
            eqs.add(new EqualsObject(filedFn1.apply(t1), filedFn2.apply(t2), errorMsg));
            return this;
        }

        public final FieldEqualsBuilder<T1, T2> append(Function<T1, ?> filedFn1, Function<T2, ?> filedFn2) {
            return append(filedFn1, filedFn2,DEFAULT_ERROR_MSG);
        }

        public boolean isEquals() {
            Assert.notEmpty(eqs, "未找到可比对的字段信息");
            boolean flag = true;
            for (EqualsObject eq : eqs) {
                if (!eq.equals()) {
                    flag = false;
                }
            }
            return flag;
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

    }

    private static class EqualsObject {

        private Object o1;

        private Object o2;

        private String errorMsg;

        private EqualsObject next;

        private EqualsObject(Object o1, Object o2, String errorMsg) {
            this.o1 = o1;
            this.o2 = o2;
            this.errorMsg = errorMsg;
        }

        public boolean equals() {
            return o1 == null || o2 == null ? false : !o1.getClass().equals(o2.getClass()) ? false : Objects.equals(o1, o2);
        }

        public String getErrorMsg() {
            return errorMsg;
        }
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setName("张伟");
        User user = new User();
        user.setName("张1伟");
        Set<String> msg = CompareUtil.fieldComparatorBuild(student, user)
                .append(Student::getName, User::getName)
                .append(Student::getIdcard, User::getName, "姓名不一致")
                .isEqualsReturnMsg();
        System.out.println(msg);
    }
}
