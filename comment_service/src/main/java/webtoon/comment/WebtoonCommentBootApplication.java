package webtoon.comment;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;

@ServletComponentScan
@SpringBootApplication
@Import({ WebtoonCommentApplicationInitializer.class })

public class WebtoonCommentBootApplication {
	public static void main(String[] args) {
		System.out.println("##### Webtoon comment service Start #####");

		SpringApplication springApplication = new SpringApplication(WebtoonCommentBootApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		// springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		System.out.println("##### Webtoon comment service End #####");
	}

}
