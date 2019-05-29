package com.zhongjian.service.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.dao.jdbctemplate.CVOrderDao;
import com.zhongjian.dao.jdbctemplate.IntegralVipDao;
import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.dto.cart.storeActivity.result.CartStoreActivityResultDTO;
import com.zhongjian.service.order.CVOrderService;
import com.zhongjian.util.DateUtil;

@Service("cvOrderService")
public class CVOrderServiceImpl implements CVOrderService {

	@Autowired
	private CVOrderDao cvOrderDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private IntegralVipDao integralVipDao;

	@Override
	public Map<String, Object> previewCVOrder(Integer uid, Integer sid, String type, Integer extra) {
		// 结果集定义
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> goodMaps = new ArrayList<Map<String, Object>>();
		// 商铺下商品总价
		BigDecimal storeAmountBigDecimal = BigDecimal.ZERO;
		// 商户活动后价格
		BigDecimal actualStoreAmountBigDecimal = BigDecimal.ZERO;
		// 商户名
		String sname = "";
		// 会员折扣
		Double memberDiscount = 0.95;
		// 会员单笔limit
		Double limitOne = 200d;
		Double limitDayRelief = 10d;
		// 积分描述
		String integralContent = "";
		Integer integralCanUse = 0;
		String couponContent = "";
		Integer couponCanUse = 0;
		// 是否是vip
		Integer isVIp = 0;
		// 该单应付总价
		BigDecimal needPay = BigDecimal.ZERO;
		// 优惠券判断
		String priceForCouponString = "";
		String integralPriceString = "";
		//总价
		String needPayString = "";
		String vipFavour = "";
		List<Map<String, Object>> storeGoodsList = cvOrderDao.getCVStoreByUidAndSid(sid, uid);
		//查出购物车详情
		for (Iterator<Map<String, Object>> iterator = storeGoodsList.iterator(); iterator.hasNext();) {
			Map<String, Object> cvStore = (Map<String, Object>) iterator.next();
			boolean flag = true;
			if (flag == true) {
				sname = (String) cvStore.get("sname");// 商户名
				flag = false;
			}
			BigDecimal amoBigDecimal = (BigDecimal) cvStore.get("amount");// 数量
			BigDecimal priceBigDecimal = cvStore.get("price") == null ? (BigDecimal) cvStore.get("basketprice")
					: (BigDecimal) cvStore.get("price");
			BigDecimal singleAmount = amoBigDecimal.multiply(priceBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
			Map<String, Object> good = new HashMap<String, Object>();
			String gname = (String) cvStore.get("gname");
			good.put("gname", gname);
			good.put("price", singleAmount.toString());
			goodMaps.add(good);
			storeAmountBigDecimal = storeAmountBigDecimal.add(singleAmount);
		}
		CartStoreActivityResultDTO storeActivtiy = orderDao.getStoreActivtiy(sid, storeAmountBigDecimal);
		if (storeActivtiy == null) {
			actualStoreAmountBigDecimal = storeAmountBigDecimal;
		} else {
			if (storeActivtiy.getType() == 0) {
				actualStoreAmountBigDecimal = storeAmountBigDecimal.subtract(new BigDecimal(storeActivtiy.getReduce()))
						.setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				actualStoreAmountBigDecimal = storeAmountBigDecimal
						.multiply(new BigDecimal(storeActivtiy.getDiscount())).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}
		needPay = actualStoreAmountBigDecimal;
		// 积分优惠券初始化显示----start
		Map<String, Object> uMap = integralVipDao.getIntegralAndVipInfo(uid);
		Integer integral = (Integer) (uMap.get("integral"));
		integralContent = "共" + integral + "积分";
		if (integral > 0) {
			integralCanUse = 1;
		}
		boolean todayCouponUse = cvOrderDao.todayCouponUse(uid);
		if (cvOrderDao.getCouponsNumCanUse(uid) > 0) {
			if (todayCouponUse) {
				couponContent = "有可用优惠券";
				couponCanUse = 1;
			} else {
				couponContent = "每日一张限用一张";
			}
		} else {
			couponContent = "";
		}
		Integer vipStatus = (Integer) (uMap.get("vip_status"));
		// 动态获取VIP参数 --start
		Map<String, Object> config = getConfigByUidAndStatus(uid, vipStatus);
		limitDayRelief = config.get("cvlimitDayRelief") == null ? limitDayRelief
				: (Double) config.get("cvlimitDayRelief");
		limitOne = config.get("cvlimitOne") == null ? limitOne : (Double) config.get("cvlimitOne");
		memberDiscount = config.get("cvdiscount") == null ? memberDiscount : (Double) config.get("cvdiscount");

		// 动态获取VIP参数 --end
		BigDecimal subtract = new BigDecimal(1).subtract(new BigDecimal(memberDiscount));
		Double canFavourOne = subtract.multiply(new BigDecimal(limitOne)).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		Double vipRelief = cvOrderDao.getVipRelief(uid);
		Double canFavourDay = limitDayRelief - vipRelief;
		if (canFavourDay < 0.00) {
			canFavourDay = 0.00;
		}
		BigDecimal vipFavourMoney = BigDecimal.ZERO;
		if (canFavourDay < canFavourOne) {
			vipFavourMoney = new BigDecimal(canFavourDay);
		} else {
			vipFavourMoney = new BigDecimal(canFavourOne);
		}
		BigDecimal vipFavourable = subtract.multiply(needPay).setScale(2, BigDecimal.ROUND_HALF_UP);
		if (vipFavourable.compareTo(vipFavourMoney) == 1) {
			vipFavourable = vipFavourMoney;
		}
		// 判断是否是会员
		if (vipStatus == 1) {
			isVIp = 1;
			needPay = needPay.subtract(vipFavourable);
		}
		BigDecimal priceForIntegral = needPay;
		BigDecimal priceForCoupon = needPay;
		priceForCouponString = priceForCoupon.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		if ("1".equals(type) && integral > 0) {
			BigDecimal hundredBigDecimal = new BigDecimal(100);
			// 使用积分
			BigDecimal integralBigDecimal = new BigDecimal(integral);
			BigDecimal integralPrice = integralBigDecimal.divide(hundredBigDecimal);

			if (priceForIntegral.compareTo(integralPrice) > 0) {
				integralContent = "共" + integral + "积分,可抵扣" + integral + "积分";
				integralPriceString = "-￥" + integralPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
				needPay = needPay.subtract(integralPrice);
			} else {
				// 全积分支付
				integralContent = "共" + integral + "积分，可抵扣"
						+ priceForIntegral.multiply(hundredBigDecimal).setScale(0, BigDecimal.ROUND_HALF_UP) + "积分";
				integralPriceString = "-￥" + priceForIntegral.setScale(2, BigDecimal.ROUND_HALF_UP);
				needPay = BigDecimal.ZERO;
			}
		}
		
		if ("2".equals(type) && todayCouponUse) {
			Map<String, Object> couponInfo = orderDao.getCouponInfo(uid, extra);
			if (couponInfo != null) {
				BigDecimal payFullBigDecimal = (BigDecimal) couponInfo.get("pay_full");
				Integer couponType = (Integer) couponInfo.get("type");
				if (couponType == 0) {
					if (priceForCoupon.compareTo(payFullBigDecimal) >= 0) {
						BigDecimal couponPrice = (BigDecimal) couponInfo.get("price");
						needPay = needPay.subtract(couponPrice);
						if (priceForCoupon.compareTo(couponPrice) < 0) {
							couponPrice = priceForCoupon;
							needPay = BigDecimal.ZERO;
						}
						couponContent = "-￥" + couponPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					}
				} 
			}
		}
		needPayString = needPay.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		// 数据封装
		resMap.put("sname", sname);
		resMap.put("isMember", isVIp);
		resMap.put("originalTotalPrice", "￥" + storeAmountBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
		resMap.put("totalPrice", "￥" + actualStoreAmountBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
		resMap.put("payTotal", needPayString);
		resMap.put("integralCanUse", integralCanUse);
		resMap.put("couponCanUse", couponCanUse);
		resMap.put("integralContent", integralContent);
		resMap.put("couponContent", couponContent);
		resMap.put("goodMap", goodMaps);
		resMap.put("integralPrice", integralPriceString);
		resMap.put("priceForCoupon", priceForCouponString);
		vipFavour = vipFavourable.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		if (isVIp == 0) {
			resMap.put("memberContent", "会员用户专享");
			resMap.put("delMemberPrice", "开通会员，预计最高可为您节省" + vipFavour + "元");
		} else {
			if (vipFavourable.compareTo(BigDecimal.ZERO) == 0) {
				resMap.put("memberContent", "当日vip优惠额度达到上限");
				resMap.put("delMemberPrice", "");
			} else {
				resMap.put("delMemberPrice", "-￥" + vipFavour);
				String disCountStr = new BigDecimal(memberDiscount).multiply(new BigDecimal(100))
						.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
				resMap.put("memberContent", "会员享" + getHan(disCountStr));
			}

		}
		return resMap;
	}

	@Override
	public String previewOrderTime() {
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, 20);
		String format = DateUtil.lastDayTime.format(nowTime.getTime());
		return format;
	}

	@Override
	public boolean cancelOrder(Integer orderId) {
		// TODO Auto-generated method stub
		return false;
	}

	private Map<String, Object> getConfigByUidAndStatus(Integer uid, Integer vipStatus) {
		if (vipStatus == 0) {
			return integralVipDao.getDefualtVipConfig();
		} else {
			// 如果是会员
			return integralVipDao.getVipConfigByUid(uid);
		}
	}
	
	public static String getHan(String numberString) {
		StringBuilder result = new StringBuilder();
		if (numberString.length() == 2 && numberString.contains("0")) {
			numberString = numberString.substring(0, 1);
		} else if (numberString.length() == 1) {
			result.append("零点");
		}
		for (int i = 0; i < numberString.length(); i++) {
			char current = numberString.charAt(i);
			if (current == '9') {
				result.append("九");
			} else if (current == '8') {
				result.append("八");
			} else if (current == '7') {
				result.append("七");
			} else if (current == '6') {
				result.append("六");
			} else if (current == '5') {
				result.append("五");
			} else if (current == '4') {
				result.append("四");
			} else if (current == '3') {
				result.append("三");
			} else if (current == '2') {
				result.append("二");
			} else if (current == '1') {
				result.append("一");
			}
		}
		result.append("折");
		return result.toString();
	}

	@Override
	public Map<String, Object> createOrder(Integer uid, Integer sid, String type, Integer extra, Integer addressId,
			Integer unixTime) {
		//
		return null;
	}

	@Override
	public boolean judgeHmShopownStatus(Integer sid) {
		// TODO Auto-generated method stub
		return false;
	}

}
