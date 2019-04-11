package com.zhongjian.dao.jdbctemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.zhongjian.dto.hm.storeActivity.result.HmStoreActivityResultDTO;

@Repository
public class OrderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public HmStoreActivityResultDTO getStoreActivtiy(Integer sid,BigDecimal amount) {
		String sql = "SELECT full,reduce,discount,type from hm_store_activity where sid="
				+ " ? and full <= ? and `enable` = 1 and is_delete = 0 AND examine = 2 ORDER BY full DESC LIMIT 1";
		RowMapper<HmStoreActivityResultDTO> rowMapper = new BeanPropertyRowMapper<>(HmStoreActivityResultDTO.class);
		HmStoreActivityResultDTO hmStoreActivityResultDTO = null;
		try {
			hmStoreActivityResultDTO =  jdbcTemplate.queryForObject(sql, rowMapper, sid,amount);
		} catch (EmptyResultDataAccessException e) {
			//hmStoreActivityResultDTO = null;
		}
		return hmStoreActivityResultDTO;
	}
	
	
	public  List<Map<String, Object>> getBasketByUidAndSid(Integer sid,Integer uid) {
		String sql = "SELECT hm_goods.price,hm_basket.amount,hm_shopown.sname,hm_shopown.unFavorable from hm_basket,hm_goods,hm_shopown where hm_basket.uid = ?"
				+ " and hm_basket.sid = ? and hm_basket.gid = hm_goods.id and hm_shopown.pid = ?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, new Object[]{uid,sid,sid});
		return resultList;
	}
	
	public  List<Map<String, Object>> checkFirstOrderByUid(Integer uid) {
		return null;
	}
	

}
