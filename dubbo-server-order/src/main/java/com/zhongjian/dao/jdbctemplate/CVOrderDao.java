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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
	//查询用户记录表
		public Map<String, Object> getCVUserOrderRecord(Integer uid) {
			String sql = "SELECT vip_relief,order_num,today_order_num from hm_cvuserorder_record where uid = ? for update";
			Map<String, Object> resMap = null;
			try {
				resMap = jdbcTemplate.queryForMap(sql, uid);
			} catch (EmptyResultDataAccessException e) {
			}

			return resMap;
		}
	
		
	//查询hm_waitdeliver_order列表
		public List<Integer> queryWaitdeliverOrderList(String serverCenter) {
			String sql = "SELECT orderid from hm_waitdeliver_order where servercenter = ?";
			List<Integer> orderIds  = jdbcTemplate.queryForList(sql,new Object[] { serverCenter },Integer.class);
			return orderIds;
		}
	
	// 更新用户记录表
	public boolean recordUpdate(Map<String, Object> record) {
		String sql = "update hm_cvuserorder_record set vip_relief = ?,order_num = ?, today_order_num = ? where uid = ?";
		record.get("order_num");
		return jdbcTemplate.update(sql, record.get("vip_relief"), record.get("order_num"), record.get("today_order_num"), record.get("uid")) > 0 ? true : false;
	}

	//优惠券设置今日已使用
	public boolean recordCouponUse(Integer uid) {
		String sql = "update hm_cvuserorder_record set coupon_use = 1 where uid = ? and coupon_use = 0";
		return jdbcTemplate.update(sql, uid) > 0 ? true : false;
	}
	
	// 取消订单更新用户记录表
	public boolean recordCouponCancel(Integer uid) {
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
				+ "vip_relief,coupon_price,coupon_id,is_show,totalPrice,originalPrice,ctime,deliver_fee) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
				ps.setObject(9, (Integer) map.get("coupon_id"),java.sql.Types.INTEGER);
				ps.setInt(10, 1);
				ps.setBigDecimal(11, (BigDecimal) map.get("totalPrice"));
				ps.setBigDecimal(12, (BigDecimal) map.get("originalPrice"));
				ps.setInt(13, (Integer) map.get("ctime"));
				ps.setBigDecimal(14, (BigDecimal) map.get("deliver_fee"));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	// hm_cvorder 新增记录
	public Integer addCVOrder(Map<String, Object> map) {
		final String sql = "INSERT INTO hm_cvorder (order_sn,sid,total,payment,ctime,ordertaking_time,"
				+ "orderend_time,addressid,order_status,pay_status,deliver_fee,remark,uoid,rid,deliver_model,service_fee) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, (String) map.get("order_sn"));
				ps.setInt(2, (Integer) map.get("sid"));
				ps.setBigDecimal(3, (BigDecimal) map.get("total"));
				ps.setBigDecimal(4, (BigDecimal) map.get("payment"));
				ps.setInt(5, (Integer) map.get("ctime"));
				ps.setObject(6, (Integer) map.get("ordertaking_time"),java.sql.Types.INTEGER);
				ps.setObject(7, (Integer) map.get("orderend_time"),java.sql.Types.INTEGER);
				ps.setInt(8, (Integer) map.get("addressid"));
				ps.setInt(9, (Integer) map.get("order_status"));
				ps.setInt(10, (Integer) map.get("pay_status"));
				ps.setBigDecimal(11, (BigDecimal) map.get("deliver_fee"));
				ps.setString(12, (String) map.get("remark"));
				ps.setInt(13, (Integer) map.get("uoid"));
				ps.setInt(14, (Integer) map.get("rid"));
				ps.setInt(15, (Integer) map.get("deliver_model"));
				ps.setBigDecimal(16, (BigDecimal) map.get("service_fee"));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	public void addCVOrderDetail(List<Map<String , Object>> list,Integer oid)   
    {
       final List<Map<String, Object>> cvOrderDetails = list;   
       String sql="insert into hm_cvorder_detail(gid,gname,uid,unit,price,amount,oid,sid,ctime,remark)" +  
            " values(?,?,?,?,?,?,?,?,?,?)";   
      jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			ps.setObject(1, cvOrderDetails.get(i).get("gid"),java.sql.Types.INTEGER);
			ps.setObject(2, cvOrderDetails.get(i).get("gname"),java.sql.Types.VARCHAR);
			ps.setObject(3, cvOrderDetails.get(i).get("uid"),java.sql.Types.INTEGER);
			ps.setObject(4, cvOrderDetails.get(i).get("unit"),java.sql.Types.VARCHAR);
			ps.setObject(5, cvOrderDetails.get(i).get("price"),java.sql.Types.BIGINT);
			ps.setObject(6, cvOrderDetails.get(i).get("amount"),java.sql.Types.BIGINT);
			ps.setObject(7, oid);
			ps.setObject(8, cvOrderDetails.get(i).get("sid"),java.sql.Types.INTEGER);
			ps.setObject(9, cvOrderDetails.get(i).get("ctime"),java.sql.Types.INTEGER);
			ps.setObject(10, cvOrderDetails.get(i).get("remark"),java.sql.Types.VARCHAR);
		}
		
		@Override
		public int getBatchSize() {
			 return cvOrderDetails.size(); 
		}
	});
    }
	
	// hm_cvuserorder_record添加
	public void addCVUserOrderRecord(Map<String, Object> map) {
		String sql = "INSERT INTO `hm_cvuserorder_record` (uid,vip_relief,order_num,today_order_num,coupon_use) VALUES (?,?,?,?,?)";
		jdbcTemplate.update(sql, map.get("uid"), map.get("vip_relief"), map.get("order_num"), map.get("today_order_num"), map.get("coupon_use"));
	}
	//查询用户订单的uoid
	public Integer getUidByOutTradeNo(String outTradeNo)  {
		String sql = "select id from hm_cvuser_order where out_trade_no = ?";
		Integer id = null;
		try {
			id  = jdbcTemplate.queryForObject(sql, new Object[] { outTradeNo }, Integer.class);	
		} catch (EmptyResultDataAccessException e) {
		}
		return id;
	}
	
	//查询用户订单的uoid
	public Integer getUidByCVOrderId(Integer id)  {
		String sql = "select uoid from hm_cvorder where id = ?";
		Integer uoid = 0;
		try {
			uoid  = jdbcTemplate.queryForObject(sql, new Object[] { id }, Integer.class);	
		} catch (EmptyResultDataAccessException e) {
		}
		return uoid;
	}
	
	//查询cvorder的rid
	public Integer getRidFromCVOrder(Integer uoid)  {
		String sql = "select rid from hm_cvorder where uoid = ?";
		Integer rid = 0;
		try {
			rid  = jdbcTemplate.queryForObject(sql, new Object[] { uoid }, Integer.class);	
		} catch (EmptyResultDataAccessException e) {
		}
		return rid;
	}
	//查询用户订单的vip减免，优惠券id，积分价值
	public Map<String, Object> getOrderDetailById(Integer orderId)  {
		String sql = "select uid,vip_relief,integralPrice,coupon_id from hm_cvuser_order where id = ?";
		Map<String, Object> resMap = null;
		try {
			resMap = jdbcTemplate.queryForMap(sql, orderId);
		} catch (EmptyResultDataAccessException e) {
		}
		return resMap;
	}
	
	public boolean updateUCVOrderToS(String outTradeNo, Integer unixTime){
		String sql = "update hm_cvuser_order set pay_status = 1,pay_time = ? where out_trade_no = ? and pay_status = 0";
		return jdbcTemplate.update(sql, unixTime, outTradeNo) > 0 ? true : false;
		
	}
	
	public boolean updateUCVStatusToTimeout(Integer UCVOrderId) {
		String sql = "update hm_cvuser_order set pay_status = 2 where id = ? and pay_status = 0";
		return jdbcTemplate.update(sql, UCVOrderId) > 0 ? true : false;
	}
	
	public boolean updateCVOrderToS(Integer UCVOrderId){
		String sql = "update hm_cvorder set pay_status = 1 where uoid = ? and pay_status = 0";
		return jdbcTemplate.update(sql, UCVOrderId) > 0 ? true : false;
	}
	public boolean updateCVOrderToTimeout(Integer UCVOrderId){
		String sql = "update hm_cvorder set pay_status = 2 where uoid = ? and pay_status = 0";
		return jdbcTemplate.update(sql, UCVOrderId) > 0 ? true : false;
	}
	
	public void addWaitDeliverOrder(Map<String, Object> map) {
		String sql = "INSERT INTO `hm_waitdeliver_order` (id,servercenter,orderId) VALUES (?,?,?)";
		jdbcTemplate.update(sql, map.get("id"), map.get("servercenter"), map.get("orderId"));
	}
	
	public boolean changeModelToTwo(Integer uoid) {
		String sql = "update `hm_cvorder` set deliver_model = 2 where deliver_model = 0 and uoid = ?";
		return jdbcTemplate.update(sql, uoid) > 0 ? true : false;
	}
	
	public boolean updateRidOfHmCVOrder(Integer rid,Integer uoid) {
		String sql = "update `hm_cvorder` set rid = ? where  uoid = ?";
		return jdbcTemplate.update(sql, rid,uoid) > 0 ? true : false;
	}
	
	public Integer getDeliverModelByUoid(Integer uoid) {
		String sql = "select deliver_model from hm_cvorder where uoid = ?";
		Integer deliverModel = 0;
		try {
			deliverModel  = jdbcTemplate.queryForObject(sql, new Object[] { uoid }, Integer.class);	
		} catch (EmptyResultDataAccessException e) {
		}
		return deliverModel;
	}
	
	public Integer getOrderStatusByUoid(Integer uoid) {
		String sql = "select order_status from hm_cvorder where uoid = ?";
		Integer orderStatus = 0;
		try {
			orderStatus  = jdbcTemplate.queryForObject(sql, new Object[] { uoid }, Integer.class);	
		} catch (EmptyResultDataAccessException e) {
		}
		return orderStatus;
	}
	
	public boolean deleteWaitdeliverOrder(Integer uoid) {
		String sql = "DELETE FROM `hm_waitdeliver_order` where orderid = ?";
		return jdbcTemplate.update(sql ,uoid) > 0 ? true : false;
	}
	
	public Integer getMarketIdByCVOrder(Integer uoid) {
		String sql = "SELECT hs.marketid from hm_shopown hs,hm_cvorder hc WHERE hs.pid = hc.sid and uoid = ?";
		Integer marketId = 0;
		try {
			marketId  = jdbcTemplate.queryForObject(sql, new Object[] { uoid }, Integer.class);	
		} catch (EmptyResultDataAccessException e) {
		}
		return marketId;
	}
	
	public Map<String, Object> getDetailByOrderId(Integer uid, Integer orderId) {
		String sql = "select out_trade_no,totalPrice from hm_cvuser_order where id = ? and uid = ? and pay_status = 0";
		Map<String, Object> resMap = null;
		try {
			resMap = jdbcTemplate.queryForMap(sql, orderId, uid);
		} catch (EmptyResultDataAccessException e) {
		}
		return resMap;
	}
	
}
