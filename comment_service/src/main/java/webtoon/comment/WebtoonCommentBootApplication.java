package webtoon.comment;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.util.FileSystemUtils;
import webtoon.utils.FileUtils;

import java.io.File;
import java.io.IOException;

@ServletComponentScan
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//@Import({ WebtoonCommentApplicationInitializer.class })

public class WebtoonCommentBootApplication {
	public static void main(String[] args) throws IOException {
		if(new File("").getAbsolutePath().contains("webtoon")){ // is opening all project
			FileUtils.deleteFolderContent("comment_service/src/main/resources/static");
			FileUtils.deleteFolderContent("comment_service/src/main/resources/templates");

			FileSystemUtils.copyRecursively(new File("src/main/resources/static"), new File("comment_service/src/main/resources/static"));
			FileSystemUtils.copyRecursively(new File("src/main/resources/templates"), new File("comment_service/src/main/resources/templates"));
		}
		else { // is opening only payment_service

		}

		System.out.println("##### Webtoon comment service Start #####");

		SpringApplication springApplication = new SpringApplication(WebtoonCommentBootApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		// springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		System.out.println("##### Webtoon comment service End #####");
	}

}
