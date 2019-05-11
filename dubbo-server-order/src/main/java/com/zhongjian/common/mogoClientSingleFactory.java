package com.zhongjian.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
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
		if (StringUtils.isBlank(propUtil.getMongoPassword())||StringUtils.isBlank(propUtil.getMongoUserName())) {
			mongoClient = new MongoClient(new ServerAddress(propUtil.getMongoIp(), propUtil.getMongoPort()), myOptions);
		} else {
			MongoCredential credential = MongoCredential.createCredential(propUtil.getMongoUserName(), propUtil.getMongodbName(), propUtil.getMongoPassword().toCharArray());
			mongoClient = new MongoClient(new ServerAddress(propUtil.getMongoIp(), propUtil.getMongoPort()), credential,
					myOptions);
		}
		MongoDatabase db = mongoClient.getDatabase("nidcai");
		MongoCollection<Document> collection = db.getCollection("vipSetting");
		Document filter = new Document();
		filter.append("effectTime", "year").append("enabled", true);
		List<Document> results = new ArrayList<Document>();
		FindIterable<Document> iterables = collection.find(filter);
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			results.add(cursor.next());
		}
		List<HashMap<String, String>> listMap = new ArrayList<HashMap<String, String>>();
		for (Iterator<Document> iterator = results.iterator(); iterator.hasNext();) {
			Document document = iterator.next();
			HashMap<String, String> map = new HashMap<>();
			map.put("time", (String) document.get("limitOne"));
			map.put("event", (String) document.get("limitDay"));
		}
		System.out.println(listMap);
	}
}