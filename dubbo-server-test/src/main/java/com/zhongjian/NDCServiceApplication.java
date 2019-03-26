package com.zhongjian;

import com.zhongjian.common.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication(scanBasePackages = "com.zhongjian")
public class NDCServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(NDCServiceApplication.class, args);
        if (run.isActive()) {
            LogUtil.info("ndc-application", "启动完成");
        }
    }

}
