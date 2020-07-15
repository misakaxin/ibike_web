package yc74ibike;

import com.yc.projects.yc74ibike.bean.Bike;
import com.yc.projects.yc74ibike.config.AppConfig;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class AppMongoTest extends TestCase {

	
	@Autowired
	private MongoTemplate mongoTemplate;

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
    public void test5() {
        Point point = new Point(28.2080941, 112.959854);
        List<Bike> list = mongoTemplate.find(new Query(Criteria.where("loc").near(point).maxDistance(20)).limit(4), Bike.class);
        System.out.println(list);
    }
    
    @Test  // 最近点查询
    public void test7() {
    	Point point = new Point(28.2043941, 112.959854);
        NearQuery query = NearQuery.near(point).maxDistance(new Distance(10, Metrics.KILOMETERS));
        GeoResults<Bike> result = mongoTemplate.geoNear(query, Bike.class);
        System.out.println(result);
    }
    
  
    
    
	
	
	
	
}
