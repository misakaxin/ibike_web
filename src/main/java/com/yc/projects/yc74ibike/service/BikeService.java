package com.yc.projects.yc74ibike.service;

import java.util.List;

import com.yc.projects.yc74ibike.bean.Bike;

public interface BikeService {

	/**
	 * 报修处理
	 * @param bike
	 */
	public void reportMantinant(Bike bike);

	/**
	 * 开锁: 1) bid必须 2) 根据bid查车 3) 车的状态
	 */
	public void open(Bike bike);

	/**
	 * 根据bid查车
	 *
	 * @param id
	 * @return
	 */
	public Bike findByBid(String id);

	/**
	 * 新车上架:必须生成bid, 且根据bid生成二维码.
	 */
	public Bike addNewBike(  Bike bike );


	public List<Bike> findNearAll(    Bike bike);

}
