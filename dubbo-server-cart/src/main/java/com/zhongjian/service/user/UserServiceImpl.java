package com.zhongjian.service.user;

import com.zhongjian.dao.entity.cart.user.UserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dto.user.query.UserQueryDTO;
import com.zhongjian.dto.user.result.UserCopResultDTO;
import com.zhongjian.dto.user.result.UserResultDTO;
import com.zhongjian.util.StringUtil;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: ldd
 */
@Service("userService")
public class UserServiceImpl extends HmBaseService<UserBean, Integer> implements UserService {
    @Override
    public Integer getUidByLoginToken(String loginToken) {

        Integer findUserByLoginToken = this.dao.executeSelectOneMethod(loginToken, "findUserByLoginToken", Integer.class);

        if (null == findUserByLoginToken) {
            return 0;
        }
        return findUserByLoginToken;
    }

    @Override
    public UserResultDTO getUserBeanById(Integer id) {

        UserResultDTO findUserById = this.dao.executeSelectOneMethod(id, "findUserById", UserResultDTO.class);

        return findUserById;
    }

    @Override
    public List<UserCopResultDTO> getCouponByUid(UserQueryDTO userQueryDTO) {

        List<UserCopResultDTO> findCouponByUid = this.dao.executeListMethod(userQueryDTO.getUid(), "findCouponByUid", UserCopResultDTO.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = true;
        Iterator<UserCopResultDTO> iterator = findCouponByUid.iterator();
        while (iterator.hasNext()) {
            UserCopResultDTO userCopResultDTO = iterator.next();
            userCopResultDTO.setState(1);
            if (flag) {
                if (new BigDecimal(userQueryDTO.getPrice()).compareTo(userCopResultDTO.getPayFull()) >= 0) {
                	userCopResultDTO.setState(0);
                } else {
                	flag = false;
				}
            }
            userCopResultDTO.setContent("满"+userCopResultDTO.getPayFull()+"元可用(每天限用一张)");
            String startTime = sdf.format(new Date((long) userCopResultDTO.getStarttime() * 1000));
            String endTime = sdf.format(new Date((long) userCopResultDTO.getEndtime() * 1000));
            userCopResultDTO.setEndtime(null);
            userCopResultDTO.setStarttime(null);
            userCopResultDTO.setPayFull(null);
            userCopResultDTO.setStart_Time(startTime);
            userCopResultDTO.setEnd_Time(endTime);
            if (userQueryDTO.getMarketId() != null && !StringUtil.isBlank(userCopResultDTO.getMarketId())) {
                String[] split = userCopResultDTO.getMarketId().split("\\|");
                Boolean flagtwo = true;
                for (String s : split) {
                    if (userQueryDTO.getMarketId().toString().equals(s)) {
                        flagtwo = false;
                        break;
                    }
                }
                if (flagtwo) {
                    iterator.remove();
                }
            } else {
                continue;
            }
        }
        return findCouponByUid;
    }
}
