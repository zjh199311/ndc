package com.zhongjian.dao.jdbctemplate;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.zhongjian.dao.entity.order.address.OrderAddressBean;
import com.zhongjian.dao.entity.order.address.OrderAddressOrderBean;

@Repository
public class AddressDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public OrderAddressBean getAddressById(Integer id) {
		String sql = "select contacts,gender,phone,address,house_number,longitude,latitude,uid from hm_address where id = ?";
		BeanPropertyRowMapper<OrderAddressBean> rowMapper = new BeanPropertyRowMapper<>(OrderAddressBean.class);
		OrderAddressBean orderAddress = jdbcTemplate.queryForObject(sql, rowMapper, id);
		return orderAddress;
	}
	
	public void addOrderAddress(OrderAddressOrderBean obj) {
		String sql = "INSERT INTO `hm_order_address` (contacts,gender,phone,address,house_number,longitude,"
				+ "latitude,uid,ctime,rider_sn) values (?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, obj.getContacts(),obj.getGender(),obj.getPhone(),obj.getAddress(),obj.getHouseNumber(),
				obj.getLongitude(),obj.getLatitude(),obj.getUid(),obj.getCtime(),obj.getRiderSn());
	}
	
	public void updateDefaultAdress(Integer addressId,Integer uid)  {
		String sql = "UPDATE hm_address s,hm_address a" + 
				" SET s.STATUS = 1,a.`status`=0 WHERE s.id = ?" + 
				" AND s.uid = ?" + 
				" AND s.is_delete=0" + 
				" AND a.id != ?" + 
				" AND a.uid = ?";
		jdbcTemplate.update(sql,addressId,uid,addressId,uid);
	}
}
