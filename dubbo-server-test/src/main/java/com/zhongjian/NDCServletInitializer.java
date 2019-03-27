package com.zhongjian;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * Function: servlet容器初始化
        * @author ldd
        * Date: 2019/3/26
        */
public class NDCServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(NDCServiceApplication.class);
    }

}
