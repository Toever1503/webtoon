package webtoon.account;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;

@ServletComponentScan
@SpringBootApplication
@Import({ WebtoonAccountApplicationInitializer.class })

public class WebtoonAccountBootApplication {
	public static void main(String[] args) {
		System.out.println("##### Webtoon account service Start #####");

		SpringApplication springApplication = new SpringApplication(WebtoonAccountBootApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		// springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		System.out.println("##### Webtoon account service End #####");
	}

}
