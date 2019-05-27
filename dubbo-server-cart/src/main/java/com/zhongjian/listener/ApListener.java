package com.zhongjian.listener;

import com.zhongjian.listener.service.ApService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: ldd
 */
@Component
public class ApListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private ApService apService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null && event == null) {
            return;
        }
        apService.start();
    }
}
