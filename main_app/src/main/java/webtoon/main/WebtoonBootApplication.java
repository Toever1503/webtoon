package webtoon.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.util.FileSystemUtils;
import webtoon.main.utils.FileUtils;

import java.io.File;
import java.io.IOException;

@ServletComponentScan
@SpringBootApplication
public class WebtoonBootApplication {
	public static void main(String[] args) throws IOException {
		if(new File("").getAbsolutePath().contains("webtoon")){ // is opening all project
			FileUtils.deleteFolderContent("main_app/src/main/resources/static");
			FileUtils.deleteFolderContent("main_app/src/main/resources/templates");

			FileSystemUtils.copyRecursively(new File("src/main/resources/static"), new File("main_app/src/main/resources/static"));
			FileSystemUtils.copyRecursively(new File("src/main/resources/templates"), new File("main_app/src/main/resources/templates"));
		}
		else { // is opening only payment_service

		}

		System.out.println("##### Webtoon Start #####");

		SpringApplication springApplication = new SpringApplication(WebtoonBootApplication.class);
		// springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		System.out.println("##### Webtoon End #####");
	}

}
