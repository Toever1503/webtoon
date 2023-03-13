package webtoon.domain;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;


@ServletComponentScan
@SpringBootApplication
@Import({ WebtoonDomainApplicationInitializer.class })
public class WebtoonDomainBootApplication {
	public static void main(String[] args) {
		System.out.println("##### Webtoon manga service Start #####");

		SpringApplication springApplication = new SpringApplication(WebtoonDomainBootApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		// springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		System.out.println("##### Webtoon manga service End #####");
	}

}
