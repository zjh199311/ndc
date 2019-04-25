package com.zhongjian.dao.jdbctemplate;

import org.springframework.beans.factory.annotation.Autowired;
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

}
