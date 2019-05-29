package com.zhongjian.dao.jdbctemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.zhongjian.dao.MongoDBDaoBase;

@Repository
public class IntegralVipDao extends MongoDBDaoBase{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// 查看积分
	public Map<String, Object> getIntegralAndVipInfo(Integer uid) {
		String sql = "SELECT integral,vip_status,vip_expire from hm_user where id = ?";
		Map<String, Object> resMap = null;
		try {
			resMap = jdbcTemplate.queryForMap(sql, uid);
		} catch (EmptyResultDataAccessException e) {
		}

		return resMap;
	}
	
	// 加减积分
	public void updateUserIntegral(Integer uid,String option,Integer integral) {
		final String sql = "SELECT integral from hm_user where id = ? for update";
		final String updateSql = "update hm_user set integral = ? where id = ?";
		Integer currentIntegral = jdbcTemplate.queryForObject(sql, new Object[] { uid }, Integer.class);
		Integer newIntegral = 0;
		if ("+".equals(option)) {
			newIntegral = currentIntegral + integral;
		}else {
			newIntegral = currentIntegral - integral;
			if (newIntegral < 0) {
				throw new RuntimeException("积分不可以减为负");
			}
			if (newIntegral > currentIntegral) {
				throw new RuntimeException("用户Id:" + uid + "存在异常行为");
			}
		}
		jdbcTemplate.update(updateSql, newIntegral,uid);
	}
	
	public void addIntegralLog(Integer uid, Integer integral , Integer type,Integer ctime) {
		String sql = "INSERT INTO `hm_integral_log` (integral,uid,type,ctime) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql,integral,uid,type,ctime);
	}
	
	public Map<String, Object> getDefualtVipConfig() {
		MongoCollection<Document> collection = getCollection("nidcai", "vipSetting");
		Document filter = new Document();
		filter.append("effectTime","year");
		filter.append("enabled",true);
		List<Document> results = new ArrayList<Document>();
		FindIterable<Document> iterables = collection.find(filter);
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			results.add(cursor.next());
		}
		HashMap<String, Object> resMap = new HashMap<String, Object>();
		for (Iterator<Document> iterator = results.iterator(); iterator.hasNext();) {
			Document document = iterator.next();
			resMap.put("discount",document.get("discount"));
			resMap.put("riderDiscount",document.get("riderDiscount"));
			Object limitDayReliefObj = document.get("limitDayRelief");
			Object limitOneObj = document.get("limitOne");
			if (limitOneObj!=null && limitOneObj instanceof String) {
				resMap.put("limitOne",Double.valueOf((String) limitOneObj));
			}else if (limitOneObj!=null && limitOneObj instanceof Double){
				resMap.put("limitOne",Double.valueOf((Double) limitOneObj));
			}
			if (limitDayReliefObj!=null && limitDayReliefObj instanceof String) {
				resMap.put("limitDayRelief",Double.valueOf((String) limitDayReliefObj));
			}else if (limitDayReliefObj!=null && limitDayReliefObj instanceof Double){
				resMap.put("limitDayRelief",Double.valueOf((Double) limitDayReliefObj));
			}
			
			//便利店参数
			resMap.put("cvdiscount",document.get("cvdiscount"));
			Object cvlimitDayReliefObj = document.get("cvlimitDayRelief");
			Object cvlimitOneObj = document.get("cvlimitOne");
			if (cvlimitOneObj!=null && cvlimitOneObj instanceof String) {
				resMap.put("cvlimitOne",Double.valueOf((String) cvlimitOneObj));
			}else if (cvlimitOneObj!=null && cvlimitOneObj instanceof Double){
				resMap.put("cvlimitOne",Double.valueOf((Double) cvlimitOneObj));
			}
			if (cvlimitDayReliefObj!=null && cvlimitDayReliefObj instanceof String) {
				resMap.put("cvlimitDayRelief",Double.valueOf((String) cvlimitDayReliefObj));
			}else if (cvlimitDayReliefObj!=null && cvlimitDayReliefObj instanceof Double){
				resMap.put("cvlimitDayRelief",Double.valueOf((Double) cvlimitDayReliefObj));
			}
			break;
		}
		return resMap;
	}
	
	public Map<String, Object> getVipConfigByUid(Integer uid) {
		final String sql = "SELECT vip from hm_vip_order  WHERE uid = ? AND pay_status = 1 ORDER BY ctime DESC LIMIT 1 ";
		String vipType = null;
		try {
			vipType = jdbcTemplate.queryForObject(sql, new Object[] { uid }, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		Map<String, Object> resMap = null;
		if (vipType == null) {
			//default
			resMap = getDefualtVipConfig();
		}else {
			resMap = new HashMap<String, Object>();
			MongoCollection<Document> collection = getCollection("nidcai", "vipSetting");
			Document filter = new Document();
			filter.append("_id",new ObjectId(vipType));
			List<Document> results = new ArrayList<Document>();
			FindIterable<Document> iterables = collection.find(filter);
			MongoCursor<Document> cursor = iterables.iterator();
			while (cursor.hasNext()) {
				results.add(cursor.next());
			}
			for (Iterator<Document> iterator = results.iterator(); iterator.hasNext();) {
				Document document = iterator.next();
				resMap.put("discount",document.get("discount"));
				resMap.put("riderDiscount",document.get("riderDiscount"));
				Object limitDayReliefObj = document.get("limitDayRelief");
				Object limitOneObj = document.get("limitOne");
				if (limitOneObj!=null && limitOneObj instanceof String) {
					resMap.put("limitOne",Double.valueOf((String) limitOneObj));
				}else if (limitOneObj!=null && limitOneObj instanceof Double){
					resMap.put("limitOne",Double.valueOf((Double) limitOneObj));
				}
				if (limitDayReliefObj!=null && limitDayReliefObj instanceof String) {
					resMap.put("limitDayRelief",Double.valueOf((String) limitDayReliefObj));
				}else if (limitDayReliefObj!=null && limitDayReliefObj instanceof Double){
					resMap.put("limitDayRelief",Double.valueOf((Double) limitDayReliefObj));
				}
				
				//便利店参数
				resMap.put("cvdiscount",document.get("cvdiscount"));
				Object cvlimitDayReliefObj = document.get("cvlimitDayRelief");
				Object cvlimitOneObj = document.get("cvlimitOne");
				if (cvlimitOneObj!=null && cvlimitOneObj instanceof String) {
					resMap.put("cvlimitOne",Double.valueOf((String) cvlimitOneObj));
				}else if (cvlimitOneObj!=null && cvlimitOneObj instanceof Double){
					resMap.put("cvlimitOne",Double.valueOf((Double) cvlimitOneObj));
				}
				if (cvlimitDayReliefObj!=null && cvlimitDayReliefObj instanceof String) {
					resMap.put("cvlimitDayRelief",Double.valueOf((String) cvlimitDayReliefObj));
				}else if (cvlimitDayReliefObj!=null && cvlimitDayReliefObj instanceof Double){
					resMap.put("cvlimitDayRelief",Double.valueOf((Double) cvlimitDayReliefObj));
				}
				break;
			}
		}
	
		return resMap;
	}
}
