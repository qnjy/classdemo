package com.example.esdemo;

import com.alibaba.fastjson.JSON;
import com.example.esdemo.pojo.SysUser;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class EsDemoApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate template;

    //基于rest的高级客户端
    @Autowired
    private RestHighLevelClient client;

    //测试索引   Request
    @Test
    void testIndex() throws IOException {
        //创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("xy_index");
        //利用client创建IndicesClient用于执行API
        IndicesClient indicesClient = client.indices();
        //执行请求
        CreateIndexResponse response = indicesClient.create(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    //判断索引是否存在
    @Test
    void getIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("xy_index");
        boolean b = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(b);
    }

    //删除索引
    @Test
    void deleteIndex() throws IOException {
        DeleteIndexRequest indexRequest = new DeleteIndexRequest("xy_index");
        AcknowledgedResponse response = client.indices().delete(indexRequest, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    //文档添加  client.index
    @Test
    void addDoc() throws IOException {
        SysUser user = new SysUser("xy", 20);
        //创建请求
        IndexRequest request = new IndexRequest("xy_index");
        //添加id.....
        request.id("1");
        request.timeout("1s");
        //将数据放入source  json格式
        request.source(JSON.toJSONString(user), XContentType.JSON);
        //客户端发送请求执行
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
        System.out.println(response.status());
    }

    //判断文档是否存在 client.exists
    @Test
    void existsDoc() throws IOException {
        GetRequest request = new GetRequest("xy_index", "1");
        boolean b = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(b);
    }

    //获取文档信息 client.get
    @Test
    void getDoc() throws IOException {
        GetRequest request = new GetRequest("xy_index", "1");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
    }

    //更新数据 client.update
    @Test
    void updateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("xy_index", "1");
        SysUser user = new SysUser("xueyin", 22);
        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);
        //client.update 更新
        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(update.status());
    }

    //删除
    @Test
    void deleteDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("xy_index", "1");
        //client.delete
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    //批量插入数据
    @Test
    void batchAddDoc() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();

        ArrayList<SysUser> list = new ArrayList<>();
        list.add(new SysUser("tom", 20));
        list.add(new SysUser("jack", 22));
        list.add(new SysUser("rose", 21));

        for (int i = 0; i < list.size(); i++) {
            //将数据以及id添加到bulkRequest中,不设置则随机
            bulkRequest.add(new IndexRequest("xy_index")
                    .id("" + (i + 1))
                    .source(JSON.toJSONString(list.get(i)), XContentType.JSON));

        }

        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        //返回false代表成功
        System.out.println(response.hasFailures());
    }

    //SearchSourceBuilder动态构建查询与SearchRequest的使用
    /**
     *  SearchRequest : 查询请求对象
     *  SearchSourceBuilder  用于构造条件
     *  TermQueryBuilder ： 精准匹配查询
     *  FuzzyQueryBuilder: 模糊匹配查询
     *  ..... 与http查询操作基本对应
     */
    @Test
    void testScrollSearch() throws IOException {
        //创建一个查询请求对象
        SearchRequest searchRequest = Requests.searchRequest("xy_index");
        //搜索源构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //term  精准文档查询
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "tom");
        //matchAll  查询全部
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
//        QueryBuilders 针对不同的查询操作封装的有不同的query方法
        //将封装的查询builder对象封装到SearchSourceBuilder中以便查询
        sourceBuilder.query(matchAllQueryBuilder);
        //查询条数、结果排序、过期时间的配置
        sourceBuilder.from(0);
        sourceBuilder.size(2);
        sourceBuilder.sort("age", SortOrder.DESC);
//        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //封装SearchSourceBuilder到SearchRequest中，以便执行操作
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response.toString());
        System.out.println(JSON.toJSONString(response.getHits()));
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

}
