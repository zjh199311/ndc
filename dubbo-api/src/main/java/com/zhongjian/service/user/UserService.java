package com.zhongjian.service.user;

import com.zhongjian.dto.user.result.UserResultDTO;

public interface UserService {
	/**
	 * 根据token获取uid
	 * @param loginToken
	 * @return
	 */
	Integer getUidByLoginToken(String loginToken);

	/**
	 * 根据uid获取user信息
	 * @param id
	 * @return
	 */
	UserResultDTO getUserBeanById(Integer id);

}
