package webtoon.storage.infras;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@EnableWebMvc
@Configuration
public class WebMvcConfig  implements WebMvcConfigurer{
	
	public static String DOMAIN;
    public static String ROOT_CONTENT_SYS;
    
	public WebMvcConfig(@Value("${app.root-content-path}") String ROOT_CONTENT_SYS,
			@Value("${app.domain}") String HOST,
			Environment environment ) {
		super();
		// TODO Auto-generated constructor stub
		WebMvcConfig.ROOT_CONTENT_SYS = ROOT_CONTENT_SYS;
		WebMvcConfig.DOMAIN = HOST;
	}
    
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
        .addResourceHandler("/uploads/**")
        .addResourceLocations("file:///".concat(ROOT_CONTENT_SYS));
    }
    
}
