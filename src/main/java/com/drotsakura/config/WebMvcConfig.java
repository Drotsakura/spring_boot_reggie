package com.drotsakura.config;

import com.drotsakura.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

//mvc静态资源映射器
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("路径映射器被调用");
        /**具体映射器(当不放在static和templates下)
         * param1：路径通配符
         * param2：具体工程目录下
         * 可添加多个
         */
         registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
         registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    //扩展mvc框架的消息转换器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("消息转换器被调用");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用jackson将java对象转换成json对象
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器对象追加到mvc框架转换器集合中
        converters.set(0,messageConverter);
    }


}
