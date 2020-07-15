package com.yc.projects.yc74ibike.service;

import java.util.List;

import com.yc.projects.yc74ibike.bean.User;

public interface UserService {
	/**
	 * 生成一个uuid,以它为键，  sessionkey和openid为值，存到 redis中，且设置超时时间为 30天。
	 * @param openId
	 * @param sessionKey
	 * @return
	 */
	public String redisSessionKey(String openId,String sessionKey);
	
	/**
	 * 添加用户到mongo的users集合
	 * @param u
	 */
	public void addMember(User u);
	/**
	 * 根据openid到 mongo中的 users集合中查是否有这个人.
	 * @param openid
	 * @return
	 */
	public List<User> selectMember(String openid) ;
	
	/*
	 * 自动生成验证码，并发送到 指定手机 phoneNum
	 */
	public void genVerifyCode(String nationCode, String phoneNum) throws Exception;

	public boolean verify(User user);

	/**
	 * 押金充值
	 * 
	 * @param user
	 * @return
	 */
	public boolean deposit(User user);

	/** 完成身份验证 */
	public boolean identity(User user);

	/**
	 * 充值
	 * 
	 * @param balance:
	 *            金额
	 * @param phoneNum:
	 *            电话
	 * @return
	 */
	public boolean recharge(double balance, String phoneNum);

}
