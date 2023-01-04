package com.test.springboot;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.aggregations.RangeBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StatsAggregate;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.test.springboot.domain.FileDetail;
import com.test.springboot.domain.Hero;
import com.test.springboot.domain.Student;
import com.test.springboot.mapper.ClazzMapper;
import com.test.springboot.mapper.HeroMapper;
import com.test.springboot.mongo.StudentMdbService;
import com.test.springboot.mongo.StudentOperate;
import com.test.springboot.service.HeroService;
import com.test.springboot.util.MediaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestSpringbootApplicationTests {

    @Autowired
    private ElasticsearchClient esClient;

    @Autowired
    private StudentOperate studentOperate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StudentMdbService studentMdbService;

    @Autowired
    private HeroService heroService;

    @Autowired
    private HeroMapper heroMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    void contextLoads() throws IOException {
        CreateIndexResponse createIndexResponse = esClient.indices().create(i -> i.index("haha")  //指定索引名称
                .aliases("my_haha", new Alias.Builder().isWriteIndex(true).build()) //指定索引别名
                .settings(s -> s.index(set -> set.numberOfShards("2")    //指定分片数量
                        .numberOfReplicas("1") //指定备份分片数
                ))  //指定setting
                .mappings(m -> m.dynamic(DynamicMapping.Strict)  //指定动态映射的模式
                        .properties("name", p -> p.keyword(KeywordProperty.of(t -> t.ignoreAbove(10))))   //设置属性
                        .properties("describe", Property.of(t -> t.text(set -> set.analyzer("ik_max_word")))) //设置属性
                        .properties("count", p -> p.integer(set -> set)) //设置属性
                ) //指定映射
        );
        System.out.println(createIndexResponse.acknowledged()); //告知
    }

    @Test
    void test() throws IOException {
        DeleteIndexResponse deleteIndexResponse = esClient.indices().delete(i -> i.index("idx"));
        System.out.println(deleteIndexResponse.acknowledged());
    }

    @Test
    void test01() throws IOException {
        BooleanResponse booleanResponse = esClient.indices().exists(i -> i.index("hello"));
        System.out.println(booleanResponse.value());
    }

    @Test
    void test02() throws IOException {
        GetIndexResponse getIndexResponse = esClient.indices().get(i -> i.index("hello"));
        System.out.println(getIndexResponse.result());
    }

    @Test
    void test03() throws IOException {
        //修改setting
        PutIndicesSettingsResponse putIndicesSettingsResponse = esClient.indices().putSettings(i -> i.index("hello").settings(set -> set.numberOfReplicas("0")));
        //增加mapping-properties字段
        PutMappingResponse putMappingResponse = esClient.indices().putMapping(i -> i.index("hello").properties("age", set -> set.integer(s -> s)));
        //修改别名
        DeleteAliasResponse deleteAliasResponse = esClient.indices().deleteAlias(i -> i.index("hello").name("my_hello"));
        //添加别名
        PutAliasResponse putAliasResponse = esClient.indices().putAlias(i -> i.index("hello").name("my_hello").isWriteIndex(true));
    }

    @Test
    void test04() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "哆啦A梦");
        map.put("age", "16");
        IndexResponse indexResponse = esClient.index(i -> i.index("hello").type("_doc").id("2").document(map));
        System.out.println(indexResponse.result().jsonValue());
    }

    @Test
    void test05() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "哆啦A梦");
        map.put("age", "16");
        CreateResponse createResponse = esClient.create(i -> i.index("hello").type("_doc").id("2").document(map));
        System.out.println(createResponse.result().jsonValue());
    }

    @Test
    void test06() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "哆啦A梦");
        map.put("age", "5");
        UpdateResponse<Map> updateResponse = esClient.update(i -> i.index("hello").type("_doc").id("2").doc(map), Map.class);
        System.out.println(updateResponse.result());
    }

    @Test
    void test07() throws IOException {
        DeleteResponse deleteResponse = esClient.delete(i -> i.index("class").type("_doc").id("1"));
        System.out.println(deleteResponse.result());
    }

    @Test
    void test08() throws IOException {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "哆啦A梦");
        map1.put("age", "5");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "樱桃小丸子");
        map2.put("age", "5");
        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", "蜡笔小新");
        map3.put("age", "5");
        List<BulkOperation> list = new ArrayList<>();
        list.add(BulkOperation.of(o -> o.index(d -> d.index("hello").id("3").document(map1))));
        list.add(BulkOperation.of(o -> o.index(d -> d.index("hello").id("4").document(map2))));
        list.add(BulkOperation.of(o -> o.index(d -> d.index("hello").id("5").document(map3))));
        BulkResponse bulkResponse = esClient.bulk(i -> i.operations(list));
        System.out.println(bulkResponse.errors());
    }

    @Test
    void test09() throws IOException {
        List<BulkOperation> list = new ArrayList<>();
        list.add(BulkOperation.of(o -> o.delete(d -> d.index("hello").id("3"))));
        list.add(BulkOperation.of(o -> o.delete(d -> d.index("hello").id("4"))));
        list.add(BulkOperation.of(o -> o.delete(d -> d.index("hello").id("5"))));
        BulkResponse bulkResponse = esClient.bulk(i -> i.operations(list));
        System.out.println(bulkResponse.items());
    }

    @Test
    void test10() throws IOException {
        SearchResponse<Map> searchResponse = esClient.search(i -> i.index("class").query(s -> s.term(p -> p.field("name").value("火箭"))), Map.class);
        for (Hit<Map> hit : searchResponse.hits().hits()) {  //遍历击中的每条数据
            System.out.println(hit.source());
        }
    }

    @Test
    void test11() throws IOException {
        List<FieldValue> list = new ArrayList<>();
        list.add(FieldValue.of("蜡笔小新"));
        list.add(FieldValue.of("哆啦A梦"));
        SearchResponse<Map> searchResponse = esClient.search(i -> i.index("hello").query(q -> q.terms(t -> t.terms(v -> v.value(list)).field("name"))), Map.class);
        for (Hit<Map> hit : searchResponse.hits().hits()) {
            System.out.println(hit.source());
        }
    }

    @Test
    void test12() throws IOException {
        SearchResponse<Map> searchResponse = esClient.search(i -> i  //搜索请求
                        .index("class")                        //索引
                        .query(q -> q.matchAll(m -> m))
                        .sort(s -> s.field(f -> f.field("code").order(SortOrder.Desc)))                                   //页大小
                , Map.class);
        for (Hit<Map> hit : searchResponse.hits().hits()) {
            System.out.println(hit.source());
        }
    }

    @Test
    void test13() throws IOException {
        SearchResponse<Map> searchResponse = esClient.search(i -> i
                        .index("class")
                        .query(q -> q.match(m -> m.field("describe").query("葵花 希望")))
                        .highlight(h -> h.fields("describe",
                                l -> l.preTags("<span color='red'>").postTags("</span>")))
                , Map.class);
        for (Hit<Map> hit : searchResponse.hits().hits()) {
            System.out.println(hit.source());
            System.out.println(hit.highlight());
        }
    }

    @Test
    void test14() throws IOException {
        GetResponse<Map> getResponse = esClient.get(i -> i.index("class").type("_doc").id("5"), Map.class);
        Map source = getResponse.source();
        System.out.println(source);
    }

    @Test
    void test15() throws IOException {
        SearchResponse<Map> searchResponse = esClient.search(s -> s
                        .index("class")
                        .scroll(Time.of(t -> t.time("5m")))
                        .query(q -> q.matchAll(m -> m))
                        .size(2)
                , Map.class);
        for (Hit<Map> hit : searchResponse.hits().hits()) {
            System.out.println(hit.source());
        }
        System.out.println(searchResponse.scrollId());
    }

    @Test
    void test16() throws IOException {
        SearchResponse<Map> searchResponse = esClient.scroll(s -> s
                        .scroll(Time.of(t -> t.time("5m")))
                        .scrollId("FGluY2x1ZGVfY29udGV4dF91dWlkDXF1ZXJ5QW5kRmV0Y2gBFlhJN1pJbjNDUkVtNGI3RFppdlNqZFEAAAAAAAAChBY3eE9taUJ4b1JtNm5tcU84bkxCeW93")
                        .scroll(Time.of(t -> t.time("1m")))
                , Map.class);
        for (Hit<Map> hit : searchResponse.hits().hits()) {
            System.out.println(hit.source());
        }
    }

    @Test
    void test17() throws IOException {
        DeleteByQueryResponse deleteByQueryResponse = esClient.deleteByQuery(q -> q
                .index("class")
                .query(s -> s
                        .term(t -> t
                                .field("name")
                                .value("一班")
                        )
                )
        );
        System.out.println(deleteByQueryResponse.deleted());
    }

    @Test
    void test18() throws IOException {
        UpdateByQueryResponse updateByQueryResponse = esClient.updateByQuery(i -> i
                .index("hello")
                .query(q -> q
                        .term(t -> t
                                .field("name")
                                .value("哆啦A梦")
                        )
                )
                .script(s -> s
                        .inline(l -> l
                                .lang("painless")
                                .source("ctx._source.count = params.c")
                                .params("c", JsonData.of(1000))
                        )
                )
        );
        System.out.println(updateByQueryResponse.updated());
    }

    @Test
    void test19() throws IOException {
        Map<String, SortOrder> map = new HashMap<>();
        map.put("_count", SortOrder.Desc);
        SearchResponse<Map> searchResponse = esClient.search(i -> i
                        .index("hello")
                        .aggregations("agg", a -> a.range(r -> r
                                .field("age")
                                .ranges(s -> s.from("1").to("10"))))
                , Map.class);
        for (RangeBucket agg : searchResponse.aggregations().get("agg").range().buckets().array()) {
            System.out.println(agg.key() + "=" + agg.docCount());
        }
    }

    @Test
    void test20() throws IOException {
        Map<String, SortOrder> map = new HashMap<>();
        map.put("_count", SortOrder.Desc);
        SearchResponse<Map> searchResponse = esClient.search(i -> i
                        .index("hello")
                        .aggregations("agg", a -> a.stats(s -> s.field("age")))
                , Map.class);
        StatsAggregate agg = searchResponse.aggregations().get("agg").stats();
        System.out.println(agg.max());
        System.out.println(agg.min());
        System.out.println(agg.sum());
        System.out.println(agg.avg());
        System.out.println(agg.count());
    }

    @Test
    void test21() throws IOException {
        IndicesResponse indicesResponse = esClient.cat().indices();
        for (IndicesRecord indicesRecord : indicesResponse.valueBody()) {
            System.out.println(indicesRecord.index());
        }
    }

    @Test
    void test22() throws IOException {
        Map<String, Property> map = new HashMap<>();
        map.put("name", Property.of(p -> p.keyword(k -> k.ignoreAbove(5))));
        map.put("describe", Property.of(p -> p.text(t -> t.analyzer("ik_max_word"))));
        map.put("age", Property.of(p -> p.integer(i -> i)));
        map.put("count", Property.of(p -> p.long_(l -> l)));
        CreateIndexResponse createIndexResponse = esClient.indices().create(i -> i
                .index("hellos")
                .settings(s -> s
                        .numberOfShards("1")
                        .numberOfReplicas("1")
                )
                .mappings(m -> m
                        .dynamic(DynamicMapping.Strict)
                        .properties(map)
                )
                .aliases("my_hellos", Alias.of(a -> a.isWriteIndex(true)))
        );
        System.out.println(createIndexResponse.acknowledged());
    }

    @Test
    void test23() throws IOException {
        ReindexResponse reindexResponse = esClient.reindex(r -> r
                .source(s -> s.index("hello")
                )
                .dest(d -> d.index("hellos"))
        );
        System.out.println(reindexResponse.batches());
    }

    @Test
    void test24() throws IOException {
        AggregationResults<Map> students = mongoTemplate.aggregate(Aggregation.newAggregation(Aggregation.project()
                .and("exercise_id").size().as("count")
                .andInclude("name")
                .andExpression("(age*4)").as("new_age")
        ), "students", Map.class);
        System.out.println(students.getMappedResults());
    }


    @Test
    void test25() throws IOException {
        Student student = new Student();
        student.setName("大雄");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true) //改变默认大小写忽略方式：忽略大小写
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains()) //采用“包含匹配”的方式查询
                .withIgnorePaths("pageNum", "pageSize");
        Example example = Example.of(student, matcher);
        List all = studentOperate.findAll(example);
        System.out.println(all);
    }

    @Test
    void test26() throws IOException {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("name").is("大雄"), Criteria.where("name").is("蜡笔小新"));
        query.addCriteria(criteria);
        List<Student> list = mongoTemplate.find(query, Student.class);
        System.out.println(list);
    }

    @Test
        //@Transactional(rollbackFor = Exception.class)
    void test27() throws Exception {
        Hero hero = new Hero();
        hero.setName("宋江");
        hero.setPower(1001);
        hero.setClassId(1l);
        System.out.println(hero.getId());
        long id = heroMapper.insert(hero);
        System.out.println(hero.getId());
    }

    @Test
    void test28() throws Exception {
        long start = System.currentTimeMillis();
        List<FileDetail> list = new ArrayList<>();
        File f1 = new File("D:\\img\\j1.jpg");
        list.add(FileDetail.builder().id(1).file(f1).time(4.0).effect("slideleft").format("jpg").build());
        File f2 = new File("D:\\img\\j2.jpg");
        list.add(FileDetail.builder().id(2).file(f2).time(6.0).effect("rectcrop").format("jpg").build());
        File f3 = new File("D:\\img\\m1.mp4");
        list.add(FileDetail.builder().id(1).file(f3).time(3.0).effect("hrslice").format("mp4").build());
        File f4 = new File("D:\\img\\m2.mp4");
        list.add(FileDetail.builder().id(1).file(f4).time(3.0).effect("wipetr").mute(1).format("mp4").build());
        File f5 = new File("D:\\img\\j3.png");
        list.add(FileDetail.builder().id(1).file(f5).time(5.0).effect("slideleft").format("jpg").build());
        File f = mediaService.produceVideo(list, 1208, 720);
        long end = System.currentTimeMillis();

        List<FileDetail> list1 = new ArrayList<>();
        File f6 = new File("D:\\img\\j1.mp3");
        list1.add(FileDetail.builder().id(1).file(f6).format("mp3").build());
        File f7 = new File("D:\\img\\j2.mp3");
        list1.add(FileDetail.builder().id(1).file(f7).format("mp3").build());
        File file = mediaService.produceAudio(list1);

        File file1 = mediaService.mergeVideoAndAudio(f, file);
        System.out.println(file1);
        System.out.println((end - start) / 1000);
//        File file = mediaService.changeImg("png", 500, 500, f1);
//        System.out.println(file);
//        File file1 = mediaService.changeImg2("png", 500, 500, f1);
//        System.out.println(file1);
    }

}
