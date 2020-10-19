package com.beijing.es;

import com.beijing.es.bean.Article;
import com.beijing.es.bean.Book;
import com.beijing.es.repository.BookRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.util.CloseableIterator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SpringbootEsApplicationTest {

    @Autowired
    JestClient jestClient;

    /**
     * 第一种方式Jest
     * ES中索引保存一个文档
     * 测试地址 http://192.168.0.106:9200/atguigu/news/1
     */
    @Test
    public void testJest() throws IOException {
        Article article = new Article();
        article.setId(1);
        article.setAuthor("曹雪斤");
        article.setTitle("红楼梦");
        article.setContent("第一章开始.......");

        Index.Builder builder = new Index.Builder(article);
        Index.Builder atguigu = builder.index("atguigu");// 索引
        Index.Builder type = atguigu.type("news");// 类型
        Index index = type.build();// 构建一个索引功能
        jestClient.execute(index);
    }

    /**
     * 测试搜索功能
     */
    @Test
    public void testSearch() throws IOException {
        String searchJson = "{\n" +
                "    \"query\" : {\n" +
                "        \"match\" : {\n" +
                "            \"content\" : \"第一\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        Search.Builder builder = new Search.Builder(searchJson);
        builder.addIndex("atguigu").addType("news");
        Search build = builder.build();
        SearchResult result = jestClient.execute(build);
        System.out.println(result.getJsonString());
    }


    @Autowired
    BookRepository bookRepository;

    /**
     * 通过继承ElasticsearchRepository接口方式
     * http://192.168.0.106:9201/atguigu/book/_search
     */
    @Test
    public void testBook(){
        // 索引 数据文档
        Book book =new Book();
        book.setAuthor("左程云");
        book.setBookName("程序员");
        book.setId(1);
        bookRepository.index(book);
        // 获取 数据文档
        List<Book> list = bookRepository.findByBookNameLike("程序");
        for (Book book1:list) {
            System.out.println(book1.toString());
        }
    }


    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    /**
     *  ElasticsearchTemplate方式
     * http://192.168.0.106:9201/atguigu/book/_search
     */
    @Test
    public void testBook2(){
        // 索引 数据文档
        Book book = new Book();
        book.setId(1);
        book.setAuthor("zlk");
        book.setBookName("war 2020");
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(book.getId().toString())
                .withObject(book).build();
        elasticsearchTemplate.index(indexQuery);

        //获取 数据文档
        //https://docs.spring.io/spring-data/elasticsearch/docs/3.2.10.RELEASE/reference/html/#elasticsearch.operations.template
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withIndices("atguigu")
                .withTypes("book")
                .build();
        CloseableIterator<Book> stream = elasticsearchTemplate.stream(searchQuery, Book.class);
        List<Book> sampleEntities = new ArrayList<>();
        while (stream.hasNext()) {
            sampleEntities.add(stream.next());
        }
        System.out.println(sampleEntities);

    }


}