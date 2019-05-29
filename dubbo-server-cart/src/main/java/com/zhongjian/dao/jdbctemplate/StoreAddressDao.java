package com.zhongjian.dao.jdbctemplate;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StoreAddressDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Map<String, Object> getStoreAddress(Integer sid) {
		String sql = "SELECT longitude,latitude from hm_store_address WHERE sid = ?";
		Map<String, Object> resMap = null;
		try {
			resMap = jdbcTemplate.queryForMap(sql, sid);
		} catch (EmptyResultDataAccessException e) {
		}
		return resMap;
	}
}
