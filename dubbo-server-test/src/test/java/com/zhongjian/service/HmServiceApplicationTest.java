package com.zhongjian.service;

import com.zhongjian.NDCServiceApplication;
import com.zhongjian.common.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Project Name: kc-risk
 * Package Name: cn.kingcar.risk.provider
 * Function: 风控系统
 *
 * @author San
 *         Date:2018/2/5
 */
@SpringBootApplication(scanBasePackages = "com.zhongjian")
public class HmServiceApplicationTest {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(NDCServiceApplication.class, args);
        if (context.isActive()) {
            LogUtil.info("ndc-test", "启动完成");
        }
    }

}
