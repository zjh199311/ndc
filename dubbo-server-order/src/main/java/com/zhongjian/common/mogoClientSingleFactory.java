package com.zhongjian.common;

import org.apache.commons.lang3.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.zhongjian.commoncomponent.PropUtil;

/*
 * mongodb数据库链接池
 */
public class mogoClientSingleFactory {
	public static MongoClient mongoClient;

	static {
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.connectionsPerHost(50);// 与目标数据库可以建立的最大链接数
		builder.minConnectionsPerHost(5);
		builder.connectTimeout(1000 * 60 * 20);// 与数据库建立链接的超时时间
		builder.maxWaitTime(5000);// 一个线程成功获取到一个可用数据库之前的最大等待时间
		builder.threadsAllowedToBlockForConnectionMultiplier(100);
		builder.maxConnectionIdleTime(0);
		builder.maxConnectionLifeTime(0);
		builder.threadsAllowedToBlockForConnectionMultiplier(5);
		builder.socketTimeout(0);
		MongoClientOptions myOptions = builder.build();
		PropUtil propUtil = (PropUtil) SpringContextHolder.getBean(PropUtil.class);
		if (StringUtils.isBlank(propUtil.getMongoPassword()) || StringUtils.isBlank(propUtil.getMongoUserName())) {
			mongoClient = new MongoClient(new ServerAddress(propUtil.getMongoIp(), propUtil.getMongoPort()), myOptions);
		} else {
			MongoCredential credential = MongoCredential.createCredential(propUtil.getMongoUserName(),
					propUtil.getMongodbName(), propUtil.getMongoPassword().toCharArray());
			mongoClient = new MongoClient(new ServerAddress(propUtil.getMongoIp(), propUtil.getMongoPort()), credential,
					myOptions);
		}
	}
}