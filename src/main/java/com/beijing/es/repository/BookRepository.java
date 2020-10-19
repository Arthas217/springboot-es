package com.beijing.es.repository;

import com.beijing.es.bean.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @Author zc217
 * @Date 2020/10/19
 */
public interface BookRepository extends ElasticsearchRepository<Book,Integer> {

    List<Book> findByBookNameLike(String bookName);

}
