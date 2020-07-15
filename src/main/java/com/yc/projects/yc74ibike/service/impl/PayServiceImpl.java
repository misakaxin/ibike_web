package com.yc.projects.yc74ibike.service.impl;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yc.projects.yc74ibike.bean.Bike;
import com.yc.projects.yc74ibike.bean.PayModel;
import com.yc.projects.yc74ibike.bean.User;
import com.yc.projects.yc74ibike.dao.BikeDao;
import com.yc.projects.yc74ibike.service.BikeService;
import com.yc.projects.yc74ibike.service.PayService;
import com.yc.projects.yc74ibike.service.UserService;


@Service
@Transactional
public class PayServiceImpl implements PayService {
	@Autowired
	private BikeService bikeService;
	@Autowired
	private UserService userService;
	@Autowired
	private MongoTemplate mongoTemplate;

	private Logger logger = LogManager.getLogger();
	
	public static final int MONEYPERHOUR=4;

	@Override
	public void pay(PayModel payModel) {
		//1. 计算金额 	
		Long startTime=payModel.getStartTime();
		Long endTime=payModel.getEndTime();
		Long spendTime=endTime-startTime;
		double hours=(Double.parseDouble( spendTime+"" ) )/(60*60);
		Integer h=(int) Math.ceil(   hours );   //小时数
		int payMoney= h*MONEYPERHOUR;    //花费
		payModel.setPayMoney(payMoney);
		payModel.setLogTime(    new Date().toLocaleString() );


		//2. 将数据保存到mongo的 payLog ( uuid,phoneNum,openId, 结账时间(年月日小时) 起(经纬),时间, 止(经纬) ,时间, 花费) 
		this.mongoTemplate.insert(    payModel,   "payLog"    );


		//3. 修改单车的经纬度, 状态为1 
		Query q=new Query(   Criteria.where("id").is(   payModel.getBid() )  );
		Update u=new Update().set("longitude", payModel.getLongitude()).set("latitude", payModel.getLatitude());
		Double[] loc=new Double[] {payModel.getLongitude(),payModel.getLatitude()};
		u.set("loc", loc).set("status", 1);
		mongoTemplate.updateFirst(q, u, Bike.class, "bike");


		//4. 修改用户态: status , balance-花费.
		Query qu=new Query(   Criteria.where("phoneNum").is( payModel.getPhoneNum()) );
		User user=mongoTemplate.findOne(qu, User.class, "users");
		Update uu=new Update().set("status",3 );   //用户的状态:  0 没有注册   1. 注册电话成功  2 押金缴纳成功   3. 实名认证,可以开锁   4. 骑行态. 
		uu.set("balance", user.getBalance()-payMoney    );
		mongoTemplate.updateFirst( qu, uu, User.class,"users");
		logger.info( "结账成功" );
	}

}
