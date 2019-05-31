package com.zhongjian.dao.jdbctemplate;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
	
	// 更改优惠券状态为已使用
	public boolean todayCouponSetUse(Integer uid) {
		String sql = "update hm_cvuserorder_record set coupon_use = 1 where uid = ? and coupon_use = 0";
		return jdbcTemplate.update(sql, uid) > 0 ? true : false;
	}

	// 更改优惠券状态为未使用
	public boolean todayCouponSetNoUse(Integer uid) {
		String sql = "update hm_cvuserorder_record set coupon_use = 0 where uid = ? and coupon_use = 1";
		return jdbcTemplate.update(sql, uid) > 0 ? true : false;
	}
	
	// 查看优惠券数量
	public Integer getCouponsNumCanUse(Integer uid) {
		String sql = "SELECT COUNT(1) from hm_user_coupon where uid = ? and model = 2 and state = 0";
		Integer num = jdbcTemplate.queryForObject(sql, new Object[] { uid }, Integer.class);
		return num;
	}
	
	// hm_cvuser_order 新增记录
	public Integer addCVUserOrder(Map<String, Object> map) {
		final String sql = "INSERT INTO hm_cvuser_order (order_sn,out_trade_no,pay_status,uid,integralPrice,store_activity_price,"
				+ "vip_relief,coupon_price,coupon_id,is_show,totalPrice,originalPrice,ctime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, (String) map.get("order_sn"));
				ps.setString(2, (String) map.get("out_trade_no"));
				ps.setInt(3, (Integer) map.get("pay_status"));
				ps.setInt(4, (Integer) map.get("uid"));
				ps.setBigDecimal(5, (BigDecimal) map.get("integralPrice"));
				ps.setBigDecimal(6, (BigDecimal) map.get("store_activity_price"));
				ps.setBigDecimal(7, (BigDecimal) map.get("vip_relief"));
				ps.setBigDecimal(8, (BigDecimal) map.get("coupon_price"));
				ps.setInt(9, (Integer) map.get("couponid"));
				ps.setInt(10, 1);
				ps.setBigDecimal(11, (BigDecimal) map.get("totalPrice"));
				ps.setBigDecimal(12, (BigDecimal) map.get("originalPrice"));
				ps.setInt(13, (Integer) map.get("ctime"));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	// hm_cvorder 新增记录
	public Integer addCVOrder(Map<String, Object> map) {
		final String sql = "INSERT INTO hm_cvorder (order_sn,sid,total,payment,ctime,ordertaking_time,"
				+ "orderend_time,addressid,order_status,pay_status,deliver_fee,remark,uoid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, (String) map.get("order_sn"));
				ps.setInt(2, (Integer) map.get("sid"));
				ps.setBigDecimal(3, (BigDecimal) map.get("total"));
				ps.setBigDecimal(4, (BigDecimal) map.get("payment"));
				ps.setInt(5, (Integer) map.get("ctime"));
				ps.setInt(6, (Integer) map.get("ordertaking_time"));
				ps.setInt(7, (Integer) map.get("orderend_time"));
				ps.setInt(8, (Integer) map.get("addressid"));
				ps.setInt(9, (Integer) map.get("order_status"));
				ps.setInt(10, (Integer) map.get("pay_status"));
				ps.setBigDecimal(11, (BigDecimal) map.get("deliver_fee"));
				ps.setInt(12, (Integer) map.get("remark"));
				ps.setInt(13, (Integer) map.get("uoid"));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	

}
