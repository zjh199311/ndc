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
		}
		jdbcTemplate.update(updateSql, newIntegral,uid);
	}
	
}
