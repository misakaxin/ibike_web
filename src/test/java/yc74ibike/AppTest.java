package yc74ibike;

import com.yc.projects.yc74ibike.bean.Bike;
import com.yc.projects.yc74ibike.config.AppConfig;
import com.yc.projects.yc74ibike.dao.BikeDao;
import com.yc.projects.yc74ibike.service.BikeService;
import com.yc.projects.yc74ibike.service.UserService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class AppTest extends TestCase {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private BikeDao bikeDao;
	@Autowired
	private BikeService bikeService;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private UserService userService;

	@Test  // 准备测试数据
	public void test1() {
		double x=28.2043941;
		double y=112.959854;
		for (int i = 0; i < 100; i++) {
			x+=0.0000010;
			for (int j = 0; j < 100; j++) {
				y+=0.0000003;
				Double loc[] = new Double[] { Double.valueOf(x), Double.valueOf(y) };
				Bike b=new Bike();
				b.setStatus(1);
				b.setLoc(  loc);
				b.setQrcode("");
				mongoTemplate.insert(b);
			}
		}
	}

	@Test
	public void testRedisTemplate() {
		stringRedisTemplate.opsForValue().set("hello", "world");
		stringRedisTemplate.opsForValue().set("hello2", "world");
	}

	@Test
	public void testUserService() throws Exception {
		userService.genVerifyCode("86", "15773541630");
	}

	@Test
	public void testNearBikes() {
		Bike b = new Bike();
		b.setLatitude(28.189122);
		b.setLongitude(112.943867);
		b.setStatus(1);
		List<Bike> list = bikeService.findNearAll(b);
		System.out.println(list);
	}

	@Test
	public void testMongoTemplate() {
		System.out.println(mongoTemplate.getDb().getName());
		System.out.println(mongoTemplate.getCollectionNames());
	}

	@Test
	public void testDataSource() throws SQLException {
		assertNotNull(dataSource);
		assertNotNull(dataSource.getConnection());
	}

	@Test
	public void testAddNewBike() {
		Bike b = new Bike();
		Bike result = bikeDao.addBike(b);
		assertNotNull(result.getBid());
		System.out.println(result.getBid());
	}

	@Test
	public void testUpdateBike() {
		Bike b = bikeDao.findBike(1L + "");
		b.setLatitude(20.9);
		b.setLongitude(22.2);
		b.setStatus(2);
		bikeDao.updateBike(b);
	}

	@Test
	public void testFindBike() {
		Bike b = bikeDao.findBike(1L + "");
		assertNotNull(b);
	}

	@Test
	public void testServiceOpen() {
		Bike b = bikeService.findByBid(1L + "");
		bikeService.open(b);
	}

	@Test
	public void testServiceAddNewBike() {
		Bike b = new Bike();
		Bike result = bikeService.addNewBike(b);
		System.out.println(result.getQrcode());
	}

}
