package testspringboot.testspringboot.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import testspringboot.testspringboot.domain.Student;

import java.util.List;

/**
 * @author 肖龙威
 * @date 2022/05/18 15:58
 */
public interface StudentOperate extends MongoRepository<Student,Integer> {

    List<Student> findByName(String nane);

    @Query("{'age':{$gte:?0,$lte:?1}}")
    Page<Student> findByRange(int start, int end, Pageable pageable);
}
