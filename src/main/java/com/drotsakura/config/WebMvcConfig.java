package com.drotsakura.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//mvc静态资源映射器
/*@Slf4j
@Configuration*/
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        /**具体映射器(当不放在static和templates下)
         * param1：路径通配符
         * param2：具体工程目录下
         * 可添加多个
         */
        registry.addResourceHandler("/package/**").addResourceLocations("classpath:/package/");
    }
}
