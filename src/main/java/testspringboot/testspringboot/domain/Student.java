package testspringboot.testspringboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author 肖龙威
 * @date 2022/03/07 13:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("students")
public class Student{

    @Id
    private Integer id;

    private String name;

    private Integer age;

    private Room room;

    private List<Integer> exercise_id;
}
