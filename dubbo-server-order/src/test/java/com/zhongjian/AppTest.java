package com.zhongjian;


import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.dto.hm.storeActivity.result.HmStoreActivityResultDTO;
import com.zhongjian.service.order.OrderService;
import com.zhongjian.util.RandomUtil;



@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"/META-INF/spring/dubbo-server.xml"})
public class AppTest {  
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    OrderService orderService;
    @Test
    public void test() {
//    	Map<String, Object> marketActivtiy = orderDao.getMarketActivtiy(118);
//    	System.out.println(marketActivtiy);
//    	HmStoreActivityResultDTO storeActivtiy = orderDao.getStoreActivtiy(529,new BigDecimal(10));
//    	if (storeActivtiy == null) {
//    		System.out.println("23321321321");
//		}else {
//			System.out.println("5555555");
//		}
//    	boolean checkFirstOrderByUid = orderDao.checkFirstOrderByUid(32715);
//    	System.out.println(checkFirstOrderByUid);
//     List<Map<String, Object>> basketByUidAndSid = orderDao.getBasketByUidAndSid(530, 32716);
// 	for (Iterator<Map<String, Object>> iterator = basketByUidAndSid.iterator(); iterator.hasNext();) {
// 		Map<String, Object> map = (Map<String, Object>) iterator.next();
//		BigDecimal priceBigDecimal = map.get("price") == null? (BigDecimal)map.get("basketprice"):(BigDecimal)map.get("price");// 单位价格
//		System.out.println(priceBigDecimal);
//	}
//    	System.out.println((String)basketByUidAndSid.get(1).get("price"));
//    	Map<String, Object> integralAndVipInfo = orderDao.getIntegralAndVipInfo(32716);
//    	Map<String, Object> couponInfo = orderDao.getCouponInfo(31026, 565909);
//    BigDecimal  payFullBigDecimal = (BigDecimal)couponInfo.get("pay_full");
//    System.out.println(payFullBigDecimal);
//    	System.out.println(couponInfo);
    	Integer[] sids = {529,530};
    	try {
    		orderService.previewOrCreateOrder(32716, sids, "2", 565909, "0", true, 1, 1, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
    
//    	orderService.previewOrCreateOrder(32716, { 530,529 }, extra, isSelfMention, toCreateOrder, addresId, unixTime, isAppointment)
//    	storeOrderInfo.put("pid",1);
//		storeOrderInfo.put("order_sn", RandomUtil.getFlowNumber());
//		storeOrderInfo.put("uid", 1);
//		storeOrderInfo.put("marketid", 1);
//		storeOrderInfo.put("total", null);
//		storeOrderInfo.put("payment",  new BigDecimal("3.1"));
//		storeOrderInfo.put("ctime", System.currentTimeMillis() / 1000);
//		storeOrderInfo.put("is_appointment", 0);
//		storeOrderInfo.put("roid", 0);
//		storeOrderInfo.put("pay_status", 0);
//    	orderDao.addHmOrder(storeOrderInfo);
    }
//  
public static void main(String[] args) {
	Map<String, Object> storeOrderInfo = new HashMap<String, Object>();
	Map<String, Object> storeOrderInfo1 = new HashMap<String, Object>();
	 System.out.println((Integer)storeOrderInfo.get("a"));
	storeOrderInfo1.put("1", (Integer)storeOrderInfo.get("a"));
	System.out.println((Integer)storeOrderInfo1.get("a"));
	}
}
