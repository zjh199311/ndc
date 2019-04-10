package com.zhongjian.dao.jdbctemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class OrderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	

}
