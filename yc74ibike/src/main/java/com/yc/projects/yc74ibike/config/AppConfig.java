package com.yc.projects.yc74ibike.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@Configuration
@ComponentScan(basePackages = "com.yc")
@EnableTransactionManagement // 查找spring 托管Bean 上是否有
// @Transactional注解,如果有，则启用事务管理器
// @Transactional(value="tx")
public class AppConfig {

	private Logger log = Logger.getLogger(AppConfig.class);


	@Bean   // 键[字符串]: 值[对象]
	public RedisTemplate redsiTemplate(  JedisConnectionFactory conn   ) {
		RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
		template.setConnectionFactory(conn);
		template.afterPropertiesSet();
		return template;
	}

	@Bean     // 键[字符串]: 值[字符串]
	public StringRedisTemplate stringRedisTemplate(     JedisConnectionFactory conn      ) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(conn);
		template.afterPropertiesSet();
		return template;
	}


	@Bean    //  MongoTemplate由spring 托管
	@Primary
	public MongoTemplate template() {
		return new MongoTemplate(factory());
	}

	/**
	 * 功能描述: 创建数据库名称对应的工厂，数据库名称可以通过配置文件导入
	 * @param
	 * @return:org.springframework.data.mongodb.MongoDbFactory
	 * @since: v1.0
	 */
	@Bean("mongoDbFactory")
	public MongoDbFactory factory() {
		return new SimpleMongoDbFactory(client(), "mybike");
	}

	/**
	 * 功能描述: 配置client，client中传入的ip和端口可以通过配置文件读入
	 *
	 * @param
	 * @return:com.mongodb.MongoClient
	 */
	@Bean("mongoClient")
	public MongoClient client() {
		List<ServerAddress> list=new ArrayList<ServerAddress>();
		// mongos   路由服务器.
		ServerAddress sa1=new ServerAddress("192.168.192.200",23000);
		ServerAddress sa2=new ServerAddress("192.168.192.201",23000);
		ServerAddress sa3=new ServerAddress("192.168.192.202",23000);
		list.add( sa1 );
		list.add( sa2 );
		list.add( sa3 );
		return new MongoClient(   list );
	}

	@Bean // TODO: 修改成数据库联接池, 获取数据源
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/ibike?serverTimezone=GMT%2B8");
		dataSource.setUsername("root");
		dataSource.setPassword("aaaa");
		log.info("创建数据源" + dataSource);
		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager tx(DriverManagerDataSource ds) {
		log.info("创建事务管理器," + ds);
		DataSourceTransactionManager dtm = new DataSourceTransactionManager();
		dtm.setDataSource(ds);
		return dtm;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
		c.setIgnoreUnresolvablePlaceholders(true);
		return c;
	}

}
