package com.zhongjian.service.hm.impl;

import com.zhongjian.common.config.properties.MessagePushProperties;
import com.zhongjian.common.util.LogUtil;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hmRider.HmRiderPositionDTO;
import com.zhongjian.service.HmBaseTest;
import com.zhongjian.service.hmRider.HmRiderPositionService;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @Author: ldd
 */
public class HmRiderPositionServiceImplTest extends HmBaseTest {

    @Resource
    private HmRiderPositionService riderPositionService;

    @Resource
    private MessagePushProperties messagePushProperties;

    @Test
    public void test() {
        HmRiderPositionDTO hmRiderPositionBean = new HmRiderPositionDTO();
        hmRiderPositionBean.setLatitude("ss");
        hmRiderPositionBean.setRid(1);
        ResultDTO<Boolean> insert = riderPositionService.insert(hmRiderPositionBean);
        if (true == insert.getFlag()) {
            LogUtil.info("增加成功", "");
        }
    }

    @Test
    public void test1() {
        System.out.println(messagePushProperties.getAppKey());
    }
}