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
@EntityScan(
        basePackages = {
                "webtoon.domains",
                "webtoon.payment",
                "webtoon.comment",
                "webtoon.account",
        }
)
@EnableJpaRepositories(
        basePackages = {
                "webtoon.domains",
                "webtoon.comment",
                "webtoon.payment",
                "webtoon.account"
        }
)
@ComponentScan(
        basePackages = {
                "webtoon.domains",

                "webtoon.account.configs",
                "webtoon.account.controller",
                "webtoon.account.services",

                "webtoon.comment.services",
                "webtoon.comment.controllers",

                "webtoon.payment.services",
                "webtoon.payment.controllers",
        }
)
public class ComponentLoaderConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
