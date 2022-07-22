package testspringboot.testspringboot.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author 肖龙威
 * @date 2022/03/07 13:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//指定mongdb的集合
@Document("students")
public class Student{

    //指定为主键_id
    @Id
    private Integer id;

    private String name;

    private Integer age;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date birthDate;

    private Room room;

    private List<Integer> exerciseId;
}
