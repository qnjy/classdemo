package com.example.esdemo;

import com.example.esdemo.pojo.User;
import com.example.esdemo.repository.UserRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.MoreLikeThisQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@SpringBootTest
public class ESTemplateTests {

    @Autowired
    private ElasticsearchRestTemplate template;

    //添加索引
    @Test
    void createIndex() {
        //spring-data 4.x
        IndexOperations indexOps = template.indexOps(IndexCoordinates.of("zy_index"));
        indexOps.create();
        // spring-data 3.x
//        boolean b = template.createIndex("zy_index");
//        System.out.println(b);
    }

    //判断索引是否存在
    @Test
    void indexExists() {
        IndexOperations indexOps = template.indexOps(User.class);
        boolean b = indexOps.exists();
//        boolean b = template.indexExists("zy_index");
        System.out.println(b);
    }

    //删除索引
    @Test
    void deleteIndex() {
        IndexOperations indexOps = template.indexOps(IndexCoordinates.of("zy_index"));
        indexOps.delete();
//        template.deleteIndex("zy_index");
    }

    @Test
    void testQuery() {
        //QueryBuilder   查询构建器
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "哥");
        //NativeSearchQuery
        NativeSearchQuery query = new NativeSearchQuery(queryBuilder);
        //search  查询   Query  返回结果是SearchHits  是Spring-data 4.0中推荐使用的
        SearchHits<User> searchHits = template.search(query, User.class);
        //获取查询命中结果
        List<SearchHit<User>> list = searchHits.getSearchHits();
        for (SearchHit<User> userSearchHit : list) {
            System.out.println(userSearchHit.getContent());
        }
    }

    //nosql   jpa方式   spring-data-es 中提供的有repository  CRUD
    @Autowired
    private UserRepository userRepository;

    @Test
    void addDoc() {
        User user = new User();
        user.setId(8);
        user.setName("八哥");
        user.setAge(18);
        user.setCreateTime(LocalDateTime.now());
        userRepository.save(user);
//        User user1 = new User(2, "大哥", 28, new Date());
//        User user2 = new User(3, "二哥", 27, new Date());
//        User user3 = new User(4, "四哥", 26, new Date());
//        User user4 = new User(5, "五哥", 25, new Date());
//
//        ArrayList<User> users = new ArrayList<>();
//        users.add(user1);
//        users.add(user2);
//        users.add(user3);
//        users.add(user4);
//
//        userRepository.saveAll(users);
    }

    @Test
    void find() {
//        User user = userRepository.findById(1L).get();
//        System.out.println(user);
//        Iterable<User> iterable = userRepository.findAll(Sort.by("age").descending());
//        iterable.forEach(System.out::println);

        PageRequest request = PageRequest.of(1, 3);
        Page<User> page = userRepository.findAll(request);
        page.forEach(System.out::println);

    }

    //自定义查询方法
    @Test
    void testPage() {
        List<User> list = userRepository.findUserByNameLike("哥", PageRequest.of(0, 3));
        list.forEach(System.out::println);
    }

    //使用queryBuilds封装查询
    @Test
    void testBuilds() {
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "哥");
//        Iterable<User> iterable = userRepository.search(queryBuilder);
//        iterable.forEach(System.out::println);
//        QueryBuilders.fuzzyQuery("","").fuzziness(Fuzziness.ONE);
        BoolQueryBuilder builder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("name", "大"))
//                .must(QueryBuilders.wildcardQuery("name", "大*"))
                ;
        /*Iterable<User> iterable1 = userRepository.search(builder);
        for (User user : iterable1) {
            System.out.println(user);
        }*/
        NativeSearchQuery query = new NativeSearchQuery(queryBuilder);
        SearchHits<User> searchHits = template.search(query, User.class);
        List<SearchHit<User>> list = searchHits.getSearchHits();
        for (SearchHit<User> userSearchHit : list) {
            System.out.println(userSearchHit.getContent());
        }
    }

    //NativeSearchQueryBuilder 构建复杂查询
    @Test
    void test() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.termQuery("name","哥"));
        //分页
        queryBuilder.withPageable(PageRequest.of(0,3));
        //排序
        queryBuilder.withSort(SortBuilders.fieldSort("age").order(SortOrder.DESC));
        NativeSearchQuery nativeSearchQuery = queryBuilder.build();
        SearchHits<User> searchHits = template.search(nativeSearchQuery, User.class);
        searchHits.getSearchHits()
                .forEach(userSearchHit -> System.out.println(userSearchHit.getContent()));
    }


    //聚合操作
    @Test
    void testAggregation() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //按字段分组
        queryBuilder.addAggregation(
                AggregationBuilders.terms("age").field("age")
//                .subAggregation(AggregationBuilders.max("age").field("age"))
        );
        SearchHits<User> searchHits = template.search(queryBuilder.build(), User.class);
        searchHits.getSearchHits()
                .forEach(userSearchHit -> System.out.println(userSearchHit.getContent()));

        TopHitsAggregationBuilder topHits = AggregationBuilders.topHits("age");
        Map<String, Object> map = topHits.getMetaData();
        map.forEach((s, o) -> System.out.println(map.get(s)));
    }

    @Test
    void test2(){
        MoreLikeThisQuery query = new MoreLikeThisQuery();
        template.search(query,User.class);

    }




}
