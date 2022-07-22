package testspringboot.testspringboot.mongo;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import testspringboot.testspringboot.domain.Student;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 肖龙威
 * @date 2022/07/21 13:42
 */
@Component
public class StudentMdbService {

    /**
     * 使用MongoTemplate的方式操作mongodb
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 基于 JPA 方式操作mongodb
     */
    @Autowired
    private StudentOperate studentOperate;

    public void save(Student student) {
        mongoTemplate.insert(student);
    }

    public void batchSave(List<Student>list) {
        mongoTemplate.insertAll(list);
    }

    public void replace(Student student) {
        //没有则创建,有则替换
        mongoTemplate.save(student);
    }

    public long update(Student student) throws IllegalAccessException {
        //查询对象
        Query query = Query.query(Criteria.where("id").is(student.getId()));
        //修改对象
        Update update = new Update();
        if (student.getAge() != null) {
            update.set("age", student.getAge());
        }
        if (student.getName() != null) {
            update.set("name", student.getName());
        }
        if (student.getRoom() != null) {
            update.set("room", student.getRoom());
        }
        if (student.getExerciseId() != null) {
            update.set("exercise_id", student.getExerciseId());
        }
        UpdateResult result = mongoTemplate.updateFirst(query, update, Student.class);
        return result.getModifiedCount();
    }

    public long deleteById(Integer id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, Student.class);
        return result.getDeletedCount();
    }

    public long deleteByIds(List<Integer> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(ids));
        DeleteResult result = mongoTemplate.remove(query, Student.class);
        return result.getDeletedCount();
    }

    public Student queryStudent(Integer id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), Student.class);
    }

    public List<Student> queryAll(){
        List<Student> all = mongoTemplate.findAll(Student.class);
        return all;
    }

    public List<Student> queryByPage(Integer currentPage, Integer pageSize) {
        Query query = new Query();
        query.skip((pageSize - 1) * currentPage);
        query.limit(pageSize);
        query.with(Sort.by(Sort.Direction.DESC, "id"));
        List<Student> list = mongoTemplate.find(query, Student.class);
        return list;
    }

    public List<Student> queryByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(name));
        List<Student> list = mongoTemplate.find(query, Student.class);
        return list;
    }

    public long queryTotal(){
        long count = mongoTemplate.count(new Query(), Student.class);
        return count;
    }

    public List<Map> queryMax(){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("age").max("room.price").as("max"));
        AggregationResults<Map> aggregate = mongoTemplate.aggregate(aggregation, Student.class, Map.class);
        return aggregate.getMappedResults();
    }

    public List<Map> queryExerciseIdSum(){
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.project( "name").and("exerciseId").project("sum").as("exsum")
                        .andExpression("age * [0] - [1]", 3, 2).as("newAge")
        );
        AggregationResults<Map> aggregate = mongoTemplate.aggregate(aggregation, Student.class, Map.class);
        return aggregate.getMappedResults();
    }

    public List<Student> queryDistinctList() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("age").first("age").as("age").first("name").as("name").first("birthDate").as("birthDate"),
                Aggregation.sort(Sort.Direction.DESC, "age")
        );
        AggregationResults<Student> aggregate = mongoTemplate.aggregate(aggregation, Student.class, Student.class);
        return aggregate.getMappedResults();
    }

    public List<Student> queryByDate(Date start, Date end) {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(Criteria.where("birthDate").gte(start).lte(end)));
        List<Student> list = mongoTemplate.find(query, Student.class);
        return list;
    }
}
