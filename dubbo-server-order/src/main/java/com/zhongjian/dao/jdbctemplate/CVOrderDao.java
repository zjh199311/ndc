package com.zhongjian.dao.jdbctemplate;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CVOrderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getCVStoreByUidAndSid(Integer sid, Integer uid) {
		String sql = " SELECT hg.price,hc.amount,hc.gid,hg.gname,hg.unit,hs.sname,hs.pid,"
				+ " hc.price basketprice,hc.remark from hm_cvstore hc LEFT JOIN hm_goods hg ON hc.gid = "
				+ " hg.id INNER JOIN hm_shopown hs on hs.pid = hc.sid where" + " hc.uid = ? and hc.sid = ?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, new Object[] { uid, sid });
		return resultList;
	}

	// 查看vip额度使用
	public Double getVipRelief(Integer uid) {
		String sql = "SELECT vip_relief FROM hm_cvuserorder_record where uid = ?";
		Double vipRelief = 0.00;
		try {
			vipRelief = jdbcTemplate.queryForObject(sql, new Object[] { uid }, Double.class);
		} catch (EmptyResultDataAccessException e) {
		}

		return vipRelief;
	}

	// 查看优惠券今天有没有使用
	public boolean todayCouponUse(Integer uid) {
		String sql = "SELECT coupon_use FROM hm_cvuserorder_record where uid = ?";
		Integer couponUse = 0;
		try {
			couponUse = jdbcTemplate.queryForObject(sql, new Object[] { uid }, Integer.class);
		} catch (EmptyResultDataAccessException e) {
		}

		return couponUse == 0?true:false;
	}
	
	// 查看优惠券数量
	public Integer getCouponsNumCanUse(Integer uid) {
		String sql = "SELECT COUNT(1) from hm_user_coupon where uid = ? and model = 2 and state = 0";
		Integer num = jdbcTemplate.queryForObject(sql, new Object[] { uid }, Integer.class);
		return num;
	}

}
