package webtoon;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;

@ServletComponentScan
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Import({ WebtoonApplicationInitializer.class })

public class WebtoonBootApplication {
	public static void main(String[] args) {
		System.out.println("##### Webtoon service Start #####");

		SpringApplication springApplication = new SpringApplication(WebtoonBootApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		// springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		System.out.println("##### Webtoon service End #####");
	}

}
