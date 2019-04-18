package com.zhongjian.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.AppTest;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.order.address.query.OrderAddressQueryDTO;
import com.zhongjian.dto.order.address.result.OrderAddressResultDTO;
import com.zhongjian.service.order.OrderService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @Author: ldd
 */
public class OrderServiceImplTest extends AppTest {

    @Resource
    private OrderService orderService;


    @Test
    public void previewOrderTime() throws Exception {
    }

}