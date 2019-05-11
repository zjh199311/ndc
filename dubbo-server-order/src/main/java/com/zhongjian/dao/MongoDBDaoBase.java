package com.zhongjian.dao;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zhongjian.common.MogoClientSingleFactory;

/*
 * mongodb数据库链接池
 */
public class MongoDBDaoBase
{

    protected MongoDatabase getDb(String dbName)
    {
        return MogoClientSingleFactory.getMongoClient().getDatabase(dbName);
    }

    protected MongoCollection<Document> getCollection(String dbName, String collectionName)
    {
    	MongoDatabase db =  MogoClientSingleFactory.getMongoClient().getDatabase(dbName);
    	MongoCollection<Document> collection = db.getCollection(collectionName);
    	return collection;
    }

 
}