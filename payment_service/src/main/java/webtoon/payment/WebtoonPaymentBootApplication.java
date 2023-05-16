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
@SpringBootApplication
@Import({ WebtoonPaymentApplicationInitializer.class })

public class WebtoonPaymentBootApplication {
	public static void main(String[] args) throws IOException {
		System.out.println("##### Webtoon payment service Start #####");

		SpringApplication springApplication = new SpringApplication(WebtoonPaymentBootApplication.class);
		// springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		System.out.println("##### Webtoon payment service End #####");
	}

}
