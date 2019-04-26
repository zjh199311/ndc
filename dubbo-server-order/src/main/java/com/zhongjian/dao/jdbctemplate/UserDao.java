package com.zhongjian.dao.jdbctemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void updateLateMarket(Integer marketId,Integer uid)  {
		String sql = "update hm_user set late_marketid = ? where id = ?";
		jdbcTemplate.update(sql,marketId,uid);
	}
	public String getAccidById(Integer id)  {
		String sql = "select accid from hm_user where id = ?";
		String accid = "";
		try {
			accid  = jdbcTemplate.queryForObject(sql, new Object[] { id }, String.class);	
		} catch (EmptyResultDataAccessException e) {
		}
		return accid;
	}

	public String getRiderAccidById(Integer id)  {
		String sql = "select accid from hm_rider_user where rid = ?";
		String accid = "";
		try {
			accid  = jdbcTemplate.queryForObject(sql, new Object[] { id }, String.class);	
		} catch (EmptyResultDataAccessException e) {
		}
		return accid;
	}
	
	public String getShopAccidById(Integer id)  {
		String sql = "select accid from hm_shopown where pid = ?";
		String accid = "";
		try {
			accid  = jdbcTemplate.queryForObject(sql, new Object[] { id }, String.class);	
		} catch (EmptyResultDataAccessException e) {
		}
		return accid;
	}
}
