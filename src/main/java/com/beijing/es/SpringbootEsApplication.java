package com.beijing.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot1.x默认有两种方式和ES交互
 * 1. jest（版本默认不生效，需要导入jest工具包）JestAutoConfiguration
 * 2. spring data elasticsearch 默认pom依赖
 *    客户端      ElasticsearchAutoConfiguration Client
 *    数据操作     ElasticsearchDataAutoConfiguration elasticsearchTemplate
 *    数据操作接口  ElasticsearchRepositoriesAutoConfiguration
 */
@SpringBootApplication
public class SpringbootEsApplication {
    //启动报错org.elasticsearch.transport.ConnectTransportException: [][192.168.0.106:9300] connect_timeout[30s]
    //可能是spring-data-elasticsearch-2.1.11.RELEASE.jar版本和运行的版本不一致（5.6.13）
    public static void main(String[] args) {
        SpringApplication.run(SpringbootEsApplication.class, args);
    }

}
