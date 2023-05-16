package webtoon.main;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
//@EntityScan(
//        basePackages = {
//                "webtoon.main.domains",
//                "webtoon.main.payment",
//                "webtoon.comment",
//                "webtoon.account",
//
//                "webtoon.storage.domain",
//        }
//)
//@EnableJpaRepositories(
//        basePackages = {
//                "webtoon.main.domains",
//                "webtoon.comment",
//                "webtoon.main.payment",
//                "webtoon.account",
//
//                "webtoon.storage.domain",
//        }
//)
//@ComponentScan(
//        basePackages = {
//                "webtoon.main.domains",
//
//                "webtoon.account.configs.security",
//                "webtoon.account.controller",
//                "webtoon.account.services",
//
//                "webtoon.comment.services",
//                "webtoon.comment.controllers",
//
//                "webtoon.main.payment.services",
//                "webtoon.main.payment.controllers",
//                "webtoon.main.payment.resources",
//
//                "webtoon.storage.api",
//                "webtoon.storage.domain",
//        }
//)
public class ComponentLoaderConfiguration {
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
