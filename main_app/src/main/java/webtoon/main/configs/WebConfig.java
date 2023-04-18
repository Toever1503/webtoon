package webtoon.main.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
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
    public List<AntPathRequestMatcher> publicUrls() {
//        SecurityUtils.isAuthenticated();
//        SecurityUtils.getCurrentUser().getUser();
        // session.getAttribute("loggedUser");
        // PUBLIC PATH
        return List.of(
                new AntPathRequestMatcher("/index"),
                new AntPathRequestMatcher("/signin"),
                new AntPathRequestMatcher("/static/**")
                , new AntPathRequestMatcher("/"),
                new AntPathRequestMatcher("/rating/**")
                ,new AntPathRequestMatcher("/rating/getRating/**")
                , new AntPathRequestMatcher("/manga/**")
                , new AntPathRequestMatcher("/post/**")
                , new AntPathRequestMatcher("/signin/**")
                , new AntPathRequestMatcher("/signup/**")
        );
    }

}