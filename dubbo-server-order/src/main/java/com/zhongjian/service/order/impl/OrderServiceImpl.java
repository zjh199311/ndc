package com.zhongjian.service.order.impl;

import com.alibaba.dubbo.config.spring.context.annotation.DubboConfigConfiguration.Single;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.hm.address.HmAddressBean;
import com.zhongjian.dao.entity.hm.shopown.HmShopownBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.hm.storeActivity.result.HmStoreActivityResultDTO;
import com.zhongjian.dto.order.address.query.OrderAddressQueryDTO;
import com.zhongjian.dto.order.address.result.OrderAddressResultDTO;
import com.zhongjian.dto.order.address.result.OrderPreviewAddressResultDTO;
import com.zhongjian.dto.order.order.query.OrderStatusQueryDTO;
import com.zhongjian.dto.order.order.result.OrderPreviewResultDTO;
import com.zhongjian.dto.order.order.result.OrderPreviewShopownResultDTO;
import com.zhongjian.service.order.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.zhongjian.util.DateUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("orderService")
public class OrderServiceImpl extends HmBaseService<HmShopownBean, Integer> implements OrderService {

	private HmDAO<HmAddressBean, Integer> hmAddressDAO;

	@Autowired
	private OrderDao orderDao;

	@Resource
	public void setHmAddressDAO(HmDAO<HmAddressBean, Integer> hmAddressDAO) {
		this.hmAddressDAO = hmAddressDAO;
		this.hmAddressDAO.setPerfix(HmAddressBean.class.getName());
	}

	@Override
	public ResultDTO<Object> previewOrder(Integer uid, Integer[] sids, String type, String extra,
			String isSelfMention) {
		//各商户总价和
		BigDecimal storesAmountBigDecimal =BigDecimal.ZERO;
		//各商户参与优惠总价和
		BigDecimal storesAmountBigDecimalForFavorable =BigDecimal.ZERO;
		//商户价格列表
		List<Map<String, Object>> storeList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < sids.length; i++) {
			Map<String, Object> store = new HashMap<String, Object>();
			//查出价格商户名(原价)
			BigDecimal storeAmountBigDecimal = BigDecimal.ZERO;
			//商户活动后价格
			BigDecimal actualStoreAmountBigDecimal = BigDecimal.ZERO;
			List<Map<String, Object>> singleStoreInfoList  = orderDao.getBasketByUidAndSid(sids[i], uid);
			int unFavorable = 0;//默认参与市场优惠
			String sname = "";
		    for (Iterator<Map<String, Object>> iterator = singleStoreInfoList.iterator(); iterator.hasNext();) {
		    	    boolean flag = true;
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				if (flag == true) {
				     sname =  (String)map.get("sname");//商户名
					unFavorable = (int)map.get("unFavorable");
					flag = false;
				}
				
				BigDecimal amoBigDecimal = (BigDecimal)map.get("amount");//数量
				BigDecimal priceBigDecimal = (BigDecimal)map.get("price");//单位价格
				BigDecimal singleAmount = amoBigDecimal.multiply(priceBigDecimal);
				storeAmountBigDecimal = storeAmountBigDecimal.add(singleAmount);
			}
		    if (unFavorable == 0) {
				//享受市场优惠
		    	storesAmountBigDecimalForFavorable = storesAmountBigDecimalForFavorable.add(storeAmountBigDecimal);
			}
			HmStoreActivityResultDTO storeActivtiy = orderDao.getStoreActivtiy(sids[i],storeAmountBigDecimal);
			if(storeActivtiy == null) {
				actualStoreAmountBigDecimal = storeAmountBigDecimal;
			}
			else {
				if (storeActivtiy.getType() == 0) {
					actualStoreAmountBigDecimal = storeAmountBigDecimal.subtract(new BigDecimal(storeActivtiy.getReduce()));
				}else {
					actualStoreAmountBigDecimal = storeAmountBigDecimal.multiply(new BigDecimal(storeActivtiy.getDiscount()));
				}
			}
			store.put("sname", sname);
			store.put("totalPrice", storeAmountBigDecimal.setScale(2).toString());
			store.put("realPrice", actualStoreAmountBigDecimal.setScale(2).toString());
			storeList.add(store);
			storesAmountBigDecimal = storesAmountBigDecimal.add(actualStoreAmountBigDecimal);
		}
		return null;
		

	}

	@Override
	// 付款时检测商户状态服务 ext增加检测是否同一市场
	public ResultDTO<Object> judgeHmShopownStatus(OrderStatusQueryDTO orderStatusQueryDTO) {
		ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();
		resultDTO.setFlag(false);
		if (null == orderStatusQueryDTO.getPids() || 0 == orderStatusQueryDTO.getPids().size()) {
			return ResultUtil.getFail(CommonMessageEnum.PID_IS_NULL);
		}
		if (null == orderStatusQueryDTO.getStatus()) {
			return ResultUtil.getFail(CommonMessageEnum.STATUS_IS_NULL);
		}
		List<HmShopownBean> hmShopownBeans = this.dao.executeListMethod(orderStatusQueryDTO,
				"selectHmShopownStatusByPids", HmShopownBean.class);
		// 默认返回状态匹配
		resultDTO.setFlag(true);
		resultDTO.setData(true);
		for (HmShopownBean hmShopownBean : hmShopownBeans) {
			// 如果商户状态与传入状态不匹配，返回false
			if (!orderStatusQueryDTO.getStatus().equals(hmShopownBean.getStatus())) {
				resultDTO.setData(false);
				break;
			}
			// 如果店铺状态为预约中，但是该店铺没有开启预约 返回false
			if (2 == hmShopownBean.getStatus() && 0 == hmShopownBean.getIsAppointment()) {
				resultDTO.setData(false);
				break;
			}
			// 如果店铺状态为打烊或开张并且店铺为开启预约。返回false
			if ((1 == hmShopownBean.getStatus() || 0 == hmShopownBean.getStatus())
					&& 1 == hmShopownBean.getIsAppointment()) {
				resultDTO.setData(false);
				break;
			}
		}
		return ResultUtil.getSuccess(CommonMessageEnum.SUCCESS);
	}




    @Override
    public OrderAddressResultDTO previewOrderAddress(OrderAddressQueryDTO orderAddressQueryDTO) {
        /**
         * 要是传来的id为0.则根据uid去查询数据库并根据status为1的默认地址返回.limit=1
         * 要是传来的id不为0,则根据id去查询在返回
         */
        if (FinalDatas.ZERO == orderAddressQueryDTO.getId()) {
            OrderAddressResultDTO orderAddressResultDTO = this.hmAddressDAO.executeSelectOneMethod(orderAddressQueryDTO.getUid(), "findAddressByUid", OrderAddressResultDTO.class);
            return orderAddressResultDTO;
        } else {
            OrderAddressResultDTO findAddressByid = this.hmAddressDAO.executeSelectOneMethod(orderAddressQueryDTO.getId(), "findAddressByid", OrderAddressResultDTO.class);
            return findAddressByid;
        }
    }

    @Override
    public String previewOrderTime(Integer isAppointment) {
        //传值如果传0则为没有预约就将当前时间加上48分返回. 如果传1则为预约就为当前时间加1天后的第二天的8点48精确到分.
        //如果当前时间为这个月的最后一天,则显示为下个月的1号8点48,如果为今年的最后一天.则显示为明年的1月1号的8点48分.
        if (FinalDatas.ONE != isAppointment) {
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE, 48);
            String format = DateUtil.HourMinute.format(nowTime.getTime());
            return format;
        } else {
            //首先先判断今天是否为这个月的最后一天.如果不是最后一天则为第二天的08:48分
            //获取今天的天数
            String today = DateUtil.day.format(new Date());
            //获取这个月的月份
            String month =DateUtil.month.format(new Date());
            //获取这个月最后一天的天数.
            Date lastDayOfMonth = DateUtil.getLastDayOfMonth(new Date());
            String lastDay = DateUtil.day.format(lastDayOfMonth);
            //如果等于0那么.今天则为最后一天.那么就是月数加1后的08点48分.
            try {
                if (today.compareTo(lastDay) == 0) {
                    //如果当前是12月.则直接将月份设置为1月.如果不是则月份加1;
                    Date date;
                    if(FinalDatas.twelve.compareTo(month)==0){
                        date = DateUtil.getFirstDayOfYear(new Date());
                    }else{
                        //将月数加1
                        date = DateUtil.addMonthFroDate(new Date(), 1);   //2019/05/30 20:00
                    }
                    //获取今天的最小时间
                    Date parse = DateUtil.lastDayTime.parse(DateUtil.formateDate(date) + " 00:00:00");
                    Calendar nowTime = Calendar.getInstance();
                    nowTime.setTime(parse);
                    //天数设置为第一天
                    nowTime.set(Calendar.DATE, 1);
                    nowTime.add(Calendar.HOUR_OF_DAY, 8);
                    nowTime.add(Calendar.MINUTE, 48);
                    String format = DateUtil.lastDayTime.format(nowTime.getTime());
                    return format;
                } else {
                    Date parse = DateUtil.lastDayTime.parse(DateUtil.formateDate(new Date()) + " 00:00:00");
                    Date date = DateUtil.addDayFroDate(parse, 1);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.HOUR_OF_DAY, 8);
                    calendar.add(Calendar.MINUTE, 48);
                    //获取第二天的08点48分.
                    String format = DateUtil.lastDayTime.format(calendar.getTime());
                    return format;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
