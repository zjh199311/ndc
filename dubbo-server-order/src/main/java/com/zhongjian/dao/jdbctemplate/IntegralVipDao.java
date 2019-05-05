package com.zhongjian.dao.jdbctemplate;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class IntegralVipDao {

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
}
