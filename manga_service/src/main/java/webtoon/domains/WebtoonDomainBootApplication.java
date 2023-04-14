package webtoon.domains;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.util.FileSystemUtils;
import webtoon.utils.FileUtils;

import java.io.File;
import java.io.IOException;


@ServletComponentScan
@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                OAuth2ClientAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        })
@Import({WebtoonDomainApplicationInitializer.class})
public class WebtoonDomainBootApplication {
    public static void main(String[] args) throws IOException {
        if(new File("").getAbsolutePath().contains("webtoon")){ // is opening all project
            FileUtils.deleteFolderContent("manga_service/src/main/resources/static");
            FileUtils.deleteFolderContent("manga_service/src/main/resources/templates");

            FileSystemUtils.copyRecursively(new File("src/main/resources/static"), new File("manga_service/src/main/resources/static"));
            FileSystemUtils.copyRecursively(new File("src/main/resources/templates"), new File("manga_service/src/main/resources/templates"));
        }
        else { // is opening only payment_service

        }

        System.out.println("##### Webtoon manga service Start #####");

        SpringApplication springApplication = new SpringApplication(WebtoonDomainBootApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        // springApplication.setLogStartupInfo(false);
        springApplication.run(args);

        System.out.println("##### Webtoon manga service End #####");
    }

}
