package webtoon.domains.main.loader.infras;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import webtoon.account.entities.UserEntity;
import webtoon.account.repositories.IUserRepository;
import webtoon.comment.entities.CommentEntity;
import webtoon.comment.repositories.ICommentRepository;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.repositories.ISubscriptionPackRepository;

@Configuration
@EntityScan(
        basePackages = {
                "webtoon.domains",
                "webtoon.payment.entities",
                "webtoon.comment.entities",
                "webtoon.account.entities",
        }
)
@EnableJpaRepositories(
        basePackages = {
                "webtoon.domains",
                "webtoon.comment.repositories",
                "webtoon.payment.repositories",
                "webtoon.account.repositories"
        }
)
@ComponentScan(
        basePackages = {
                "webtoon.account.services",
                "webtoon.account.controller",
                "webtoon.comment.services",
                "webtoon.comment.controllers",
        }
)
public class JpaLoader {
        @Bean
        public PasswordEncoder passwordEncoder(){
                return new BCryptPasswordEncoder();
        }
}
