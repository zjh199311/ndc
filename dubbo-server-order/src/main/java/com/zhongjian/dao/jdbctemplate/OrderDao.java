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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.zhongjian.dto.cart.storeActivity.result.HmStoreActivityResultDTO;
import com.zhongjian.util.DateUtil;

@Repository
public class OrderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public HmStoreActivityResultDTO getStoreActivtiy(Integer sid, BigDecimal amount) {
		String sql = "SELECT full,reduce,discount,type from hm_store_activity where sid="
				+ " ? and full <= ? and `enable` = 1 and is_delete = 0 AND examine = 2 ORDER BY full DESC LIMIT 1";
		RowMapper<HmStoreActivityResultDTO> rowMapper = new BeanPropertyRowMapper<>(HmStoreActivityResultDTO.class);
		HmStoreActivityResultDTO hmStoreActivityResultDTO = null;
		try {
			hmStoreActivityResultDTO = jdbcTemplate.queryForObject(sql, rowMapper, sid, amount);
		} catch (EmptyResultDataAccessException e) {
			// hmStoreActivityResultDTO = null;
		}
		return hmStoreActivityResultDTO;
	}

	public Map<String, Object> getMarketActivtiy(Integer marketId) {
		String sql = "SELECT type,rule,up_limit from hm_market_activity where marketid = ? and `status` = 1";
		Map<String, Object> resMap = null;
		try {
			resMap = jdbcTemplate.queryForMap(sql, marketId);
		} catch (EmptyResultDataAccessException e) {
			// hmStoreActivityResultDTO = null;
		}
		return resMap;
	}

	public List<Map<String, Object>> getBasketByUidAndSid(Integer sid, Integer uid) {
		String sql = "SELECT hm_goods.price,hm_basket.amount,hm_basket.gid,hm_goods.gname,hm_goods.unit,hm_shopown.sname,"
				+ "hm_shopown.unFavorable,hm_shopown.marketid,hm_shopown.pid,"
				+ "hm_basket.price basketprice,hm_basket.remark from hm_basket LEFT JOIN hm_goods ON hm_basket.gid = "
				+ "hm_goods.id INNER JOIN hm_shopown on  hm_shopown.pid = hm_basket.sid where "
				+ "hm_basket.uid = ? and hm_basket.sid = ?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, new Object[] { uid, sid });
		return resultList;
	}

	public boolean checkFirstOrderByUid(Integer uid) {
		String sql = "SELECT COUNT(1) FROM hm_rider_order where uid = ? AND ctime  > ? AND (pay_status = 0 or pay_status = 1)";
		Integer num = jdbcTemplate.queryForObject(sql, new Object[] { uid, DateUtil.getTodayZeroTime() },
				Integer.class);
		return num > 0 ? false : true;
	}

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

	// 查看优惠券
	public Integer getCouponsNum(Integer uid) {
		String sql = "SELECT COUNT(1) from hm_user_coupon,hm_coupon where uid = ? and state "
				+ "= 0 and timeout = 0 and hm_user_coupon.couponid = hm_coupon.id";
		Integer num = jdbcTemplate.queryForObject(sql, new Object[] { uid }, Integer.class);
		return num;
	}

	// 查看优惠券
	public Map<String, Object> getCouponInfo(Integer uid, Integer couponId) {
		String sql = "SELECT  hm_coupon.type ,hm_coupon.pay_full,hm_coupon.coupon from hm_user_coupon,hm_coupon where hm_user_coupon.id = ? "
				+ "and hm_user_coupon.couponid = hm_coupon.id and hm_user_coupon.uid = ? "
				+ "and hm_user_coupon.state = 0 and hm_coupon.timeout = 0 ";
		Map<String, Object> resMap = null;
		try {
			resMap = jdbcTemplate.queryForMap(sql, couponId, uid);
		} catch (EmptyResultDataAccessException e) {
		}
		return resMap;
	}

	// hm_cart添加
	public void addHmCart(Map<String, Object> map) {
		String sql = "INSERT INTO `hm_cart` (pid,gid,gname,unit,uid,price,amount,oid,sid,status,ctime,remark) VALUES (null,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, map.get("gid"), map.get("gname"), map.get("unit"), map.get("uid"), map.get("price"),
				map.get("amount"), map.get("oid"), map.get("sid"), map.get("status"), map.get("ctime"),
				map.get("remark") == null ? "" : map.get("remark"));
	}

	// hm_order添加
	public Integer addHmOrder(Map<String, Object> map) {
		String sql = "INSERT INTO `hm_order` (order_sn,pid,uid,marketid,total,payment,integral,"
				+ "pay_status,order_status,ctime,is_appointment,roid,type,couponid,pay_time,cartids,remark,test) "
				+ "VALUES (?, ?, ?, ?, ?, ?, 0, ?, ?,  ?, ?, ?,null,null,null,null,null,null)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, (String)map.get("order_sn"));
				ps.setInt(2, (int)map.get("pid"));
				ps.setInt(3, (int)map.get("uid"));
				ps.setInt(4, (int)map.get("marketid"));
				ps.setBigDecimal(5, (BigDecimal)map.get("total"));
				ps.setBigDecimal(6, (BigDecimal)map.get("payment"));
				ps.setInt(7, (Integer)map.get("pay_status"));
				ps.setInt(8, (Integer)map.get("order_status"));
				ps.setInt(9, (int)map.get("ctime"));
				ps.setInt(10, (int)map.get("is_appointment"));
				ps.setInt(11, (int)map.get("roid"));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	// hm_rider_order添加
	public Integer addHmRiderOrder(Map<String, Object> map) {
		final String sql = "INSERT INTO hm_rider_order (rider_sn,order_sn,uid,marketid,rid,pay_status,address_id,rider_pay,"
				+ "couponid,type_pay,totalPrice,pay_time,service_time,order_time,finish_time,ctime,integral,is_appointment,end_time,"
				+ "original_price,out_trade_no,coupon_price,market_activity_price,store_activity_price,vip_relief,remark,test,rider_status) VALUES (?,?,?,"
				+ "?,?,?,?,?,?,null,?,?,?,null,null,?,?,?,null,?,?,?,?,?,?,null,null,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, (String)map.get("rider_sn"));
				ps.setString(2, (String)map.get("order_sn"));
				ps.setInt(3, (Integer)map.get("uid"));
				ps.setInt(4, (Integer)map.get("marketid"));
				ps.setObject(5, (Integer)map.get("rid"), java.sql.Types.INTEGER);
				ps.setInt(6, (Integer)map.get("pay_status"));
				ps.setInt(7, (Integer)map.get("address_id"));
				ps.setBigDecimal(8, (BigDecimal)map.get("rider_pay"));
				ps.setObject(9, (Integer)map.get("couponid"));
				ps.setBigDecimal(10, (BigDecimal)map.get("totalPrice"));
				ps.setObject(11, (Integer)map.get("pay_time"), java.sql.Types.INTEGER);
				ps.setInt(12, (Integer)map.get("service_time"));
				ps.setInt(13, (Integer)map.get("ctime"));
				ps.setObject(14, (BigDecimal)map.get("integral"), java.sql.Types.BIGINT);
				ps.setInt(15, (Integer)map.get("is_appointment"));
				ps.setBigDecimal(16, (BigDecimal)map.get("original_price"));
				ps.setString(17, (String)map.get("out_trade_no"));
				ps.setObject(18, (BigDecimal)map.get("coupon_price"), java.sql.Types.INTEGER);
				ps.setObject(19, (BigDecimal)map.get("market_activity_price"), java.sql.Types.INTEGER);
				ps.setBigDecimal(20, (BigDecimal)map.get("store_activity_price"));
				ps.setBigDecimal(21, (BigDecimal)map.get("vip_relief"));
				ps.setInt(22, (Integer)map.get("rider_status"));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public List<Integer> getRidBymarketId(Integer marketId) {
		String sql = "select rid from  hm_rider_user where marketid = ?";
		List<Integer> res = jdbcTemplate.queryForList(sql, Integer.class,new Object[] { marketId });
		return res;
	}
	
	public  Map<String, Object>getDetailByOrderId(Integer uid,Integer orderId) {
		String sql = "select out_trade_no,totalPrice from  hm_rider_order where id = ? and uid = ?";
		Map<String, Object> resMap = null;
		try {
			resMap = jdbcTemplate.queryForMap(sql,uid,orderId);
		} catch (EmptyResultDataAccessException e) {
			
		}
		return resMap;
	}
}
