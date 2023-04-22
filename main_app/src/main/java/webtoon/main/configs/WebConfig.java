package webtoon.main.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.expression.Numbers;
import webtoon.account.configs.security.SecurityUtils;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/")
                .setViewName("forward:/comment");
    }

    @Bean
    public OrRequestMatcher publicUrls() {
//        SecurityUtils.isAuthenticated();
//        SecurityUtils.getCurrentUser().getUser();
        // session.getAttribute("loggedUser");
        // PUBLIC PATH  
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/index"),
                new AntPathRequestMatcher("/signin"),
                new AntPathRequestMatcher("/static/**")
                , new AntPathRequestMatcher("/")
                , new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.name())
                , new AntPathRequestMatcher("/manga/**")
                , new AntPathRequestMatcher("/post/**")
                , new AntPathRequestMatcher("/signin/**")
                , new AntPathRequestMatcher("/signup/**")
                , new AntPathRequestMatcher("/subscription_pack/**")
                , new AntPathRequestMatcher("/mangas/**")
                , new AntPathRequestMatcher("/payment/**")
                , new AntPathRequestMatcher("/order/**")
                // for account module
                , new AntPathRequestMatcher("/api/users/forgot-password")
                , new AntPathRequestMatcher("/api/users/signin")
        );
    }

}