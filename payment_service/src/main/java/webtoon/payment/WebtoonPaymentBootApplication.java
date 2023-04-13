package webtoon.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.util.FileSystemUtils;
import webtoon.utils.FileUtils;

import java.io.File;
import java.io.IOException;

@ServletComponentScan
@SpringBootApplication(
		exclude = {
				org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
		})
@Import({ WebtoonPaymentApplicationInitializer.class })

public class WebtoonPaymentBootApplication {
	public static void main(String[] args) throws IOException {

		if(new File("").getAbsolutePath().contains("webtoon")){ // is opening all project
			FileUtils.deleteFolderContent("payment_service/src/main/resources/static");
			FileUtils.deleteFolderContent("payment_service/src/main/resources/templates");

			FileSystemUtils.copyRecursively(new File("src/main/resources/static"), new File("payment_service/src/main/resources/static"));
			FileSystemUtils.copyRecursively(new File("src/main/resources/templates"), new File("payment_service/src/main/resources/templates"));
		}
		else { // is opening only payment_service

		}
		System.out.println("##### Webtoon payment service Start #####");

		SpringApplication springApplication = new SpringApplication(WebtoonPaymentBootApplication.class);
		// springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		System.out.println("##### Webtoon payment service End #####");
	}

}
