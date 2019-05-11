package com.zhongjian.dao.jdbctemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.zhongjian.dao.MongoDBDaoBase;

@Repository
public class CouponDao extends MongoDBDaoBase{

  
    public Map<String, Object> getCoupInfo(String mongoId) {
    	MongoCollection<Document> collection = getCollection("nidcai", "coupon");
    	Document filter = new Document();
		filter.append("_id",new ObjectId(mongoId));
		List<Document> results = new ArrayList<Document>();
		FindIterable<Document> iterables = collection.find(filter);
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			results.add(cursor.next());
		}
		HashMap<String, Object> resMap = new HashMap<String, Object>();
		for (Iterator<Document> iterator = results.iterator(); iterator.hasNext();) {
			Document document = iterator.next();
			resMap.put("pay_full",new BigDecimal((Double) document.get("payFull")));
			resMap.put("type",document.get("type"));
			resMap.put("marketid", document.get("mid"));
			break;
		}
		return resMap;
    	
    }
}
