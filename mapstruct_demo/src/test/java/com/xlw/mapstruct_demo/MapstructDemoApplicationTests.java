package com.xlw.mapstruct_demo;

import com.xlw.mapstruct_demo.entity.Student;
import com.xlw.mapstruct_demo.mapper.StudentMapper;
import com.xlw.mapstruct_demo.vo.StudentVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class MapstructDemoApplicationTests {

    @Test
    void contextLoads() {
        Student student = new Student();
        student.setName("小米");
        student.setAge(12);
        student.setBirthday(LocalDateTime.now());
        student.setDesc("描述");
        StudentVO vo = StudentMapper.INSTANCT.toVO(student);
        System.out.println(vo);

    }

}
