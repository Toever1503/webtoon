package webtoon.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.util.FileSystemUtils;
import webtoon.utils.FileUtils;

import java.io.File;
import java.io.IOException;

@ServletComponentScan
@SpringBootApplication
public class WebtoonBootApplication {
	public static void main(String[] args) throws IOException {
		System.out.println("##### Webtoon Start #####");

		SpringApplication springApplication = new SpringApplication(WebtoonBootApplication.class);
		// springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		System.out.println("##### Webtoon End #####");
	}

}
