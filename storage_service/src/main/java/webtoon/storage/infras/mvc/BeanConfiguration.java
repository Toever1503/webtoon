package webtoon.storage.infras.mvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import webtoon.storage.domain.utils.FileUploadProvider;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//@EnableWebMvc
//@Configuration
public class BeanConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:///".concat(FileUploadProvider.ROOT_CONTENT_SYS));
    }

    //WebMvcConfigurer bean
    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:3000",
                                "https://localhost:8080",
                                "http://localhost:8081",
                                "http://localhost:8085",
                                "http://localhost:19006",
                                "http://localhost:19000")
                        .allowedOriginPatterns("*.*.*.*:*")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS");
            }
        };
    }

    @Bean
    public Executor taskExecutor() {
        return new ThreadPoolExecutor(500, 5000, 300l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    //Mail sender bean configure
//    @Bean
//    public JavaMailSender getMailSender(@Value("${mail.smtp.host}")
//                                                String host,
//                                        @Value("${mail.smtp.port}")
//                                                int port,
//                                        @Value("${mail.smtp.username}")
//                                                String username,
//                                        @Value("${mail.smtp.password}")
//                                                String password,
//                                        @Value("${mail.smtp.auth}")
//                                                String auth) {
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost(host);
//        javaMailSender.setPort(port);
//        javaMailSender.setUsername(username);
//        javaMailSender.setPassword(password);
//
//        Properties javaMailProperties = new Properties();
//        javaMailProperties.put("mail.transport.protocol", "smtp");
//        javaMailProperties.put("mail.smtp.auth", auth);
//        javaMailProperties.put("mail.smtp.starttls.enable", "true");
//        javaMailProperties.put("mail.debug", "false");
//        javaMailProperties.put("mail.smtp.ssl.trust", "*");
//
//        javaMailSender.setJavaMailProperties(javaMailProperties);
//        return javaMailSender;
//    }
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
    }
}
