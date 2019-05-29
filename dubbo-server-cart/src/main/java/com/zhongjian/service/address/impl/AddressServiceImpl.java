package com.zhongjian.service.address.impl;

import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.cart.address.CartAddressBean;
import com.zhongjian.dao.entity.cart.market.CartMarketBean;
import com.zhongjian.dao.entity.cart.user.UserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dao.jdbctemplate.StoreAddressDao;
import com.zhongjian.dto.cart.address.query.CartAddressQueryDTO;
import com.zhongjian.dto.cart.address.result.CartAddressResultDTO;
import com.zhongjian.service.address.AddressService;
import com.zhongjian.util.DistanceUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import javax.annotation.Resource;

/**
 * @Author: ldd
 */
@Service("addressService")
public class AddressServiceImpl extends HmBaseService<CartAddressBean, Integer> implements AddressService {

    private HmDAO<UserBean, Integer> hmDAO;

    private HmDAO<CartMarketBean, Integer> marketBean;


    @Autowired
    private StoreAddressDao storeAddressDao;
    @Resource
    private void setHmDAO(HmDAO<UserBean, Integer> hmDAO) {
        this.hmDAO = hmDAO;
        this.hmDAO.setPerfix(UserBean.class.getName());

    }

    @Resource
    private void setMarketBean(HmDAO<CartMarketBean, Integer> marketBean) {
        this.marketBean = marketBean;
        this.marketBean.setPerfix(CartMarketBean.class.getName());

    }


    @Override
    public CartAddressResultDTO previewOrderAddress(CartAddressQueryDTO cartAddressQueryDTO) {
        /**
         * 要是传来的id为0.则根据uid去查询数据库并根据status为1的默认地址返回.limit=1 要是传来的id不为0,则根据id去查询在返回
         */
        CartAddressResultDTO cartAddressResultDTO = null;
        if (FinalDatas.ZERO == cartAddressQueryDTO.getId()) {
            cartAddressResultDTO = this.dao.executeSelectOneMethod(
                    cartAddressQueryDTO.getUid(), "findAddressByUid", CartAddressResultDTO.class);
        } else {
            cartAddressResultDTO = this.dao.executeSelectOneMethod(
                    cartAddressQueryDTO,"findAddressByid", CartAddressResultDTO.class);
        }
        if (cartAddressResultDTO != null) {
            //地址的经度纬度
            double latitude = Double.parseDouble(cartAddressResultDTO.getLatitude());
            double longitude = Double.parseDouble(cartAddressResultDTO.getLongitude());

            //获取菜场经纬度
            CartMarketBean findMarketById = this.marketBean.executeSelectOneMethod(cartAddressQueryDTO.getMarketId(), "findMarketById", CartMarketBean.class);
            //菜场经纬度
            double latitude1 = Double.parseDouble(findMarketById.getLatitude());
            double longitude1 = Double.parseDouble(findMarketById.getLongitude());
            double distance = DistanceUtils.getDistance(longitude, latitude, longitude1, latitude1);
            if (distance <= 3.6) {
                return cartAddressResultDTO;
            }else{
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void updateDefaultAddress(CartAddressQueryDTO cartAddressQueryDTO) {

        this.dao.executeUpdateMethod(cartAddressQueryDTO, "updateStatusByUid");
    }

    @Override
    public void updateUserMarketIdById(CartAddressQueryDTO cartAddressQueryDTO) {

        this.hmDAO.executeUpdateMethod(cartAddressQueryDTO, "updateMarketIdById");

    }

	@Override
	public CartAddressResultDTO previewCVOrderAddress(CartAddressQueryDTO cartAddressQueryDTO,Integer sid) {
		  /**
         * 要是传来的id为0.则根据uid去查询数据库并根据status为1的默认地址返回.limit=1 要是传来的id不为0,则根据id去查询在返回
         */
        CartAddressResultDTO cartAddressResultDTO = null;
        if (FinalDatas.ZERO == cartAddressQueryDTO.getId()) {
            cartAddressResultDTO = this.dao.executeSelectOneMethod(
                    cartAddressQueryDTO.getUid(), "findAddressByUid", CartAddressResultDTO.class);
        } else {
            cartAddressResultDTO = this.dao.executeSelectOneMethod(
                    cartAddressQueryDTO,"findAddressByid", CartAddressResultDTO.class);
        }
        if (cartAddressResultDTO != null) {
            //地址的经度纬度
            double latitude = Double.parseDouble(cartAddressResultDTO.getLatitude());
            double longitude = Double.parseDouble(cartAddressResultDTO.getLongitude());
            //商户经纬度
            Map<String, Object> storeAddress = storeAddressDao.getStoreAddress(sid);
            double longitude1 = Double.parseDouble((String) storeAddress.get("longitude"));
            double latitude1 = Double.parseDouble((String) storeAddress.get("latitude"));
            double distance = DistanceUtils.getDistance(longitude, latitude, longitude1, latitude1);
            if (distance <= 0.5) {
                return cartAddressResultDTO;
            }else{
                return null;
            }
        } else {
            return null;
        }
	}
}
