package com.zhongjian.service.user;

import com.zhongjian.dao.entity.cart.user.UserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dto.user.result.UserResultDTO;
import org.springframework.stereotype.Service;

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
}
