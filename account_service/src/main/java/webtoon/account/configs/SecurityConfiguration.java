package webtoon.account.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/signin"),
            new AntPathRequestMatcher("/oauth2-failed"),
            new AntPathRequestMatcher("/static/**"),
            new AntPathRequestMatcher("/**")
    );
    private RequestMatcher PRIVATE_URLS = new NegatedRequestMatcher(PUBLIC_URLS);
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.httpBasic().disable();
        http.cors().and().csrf().disable()
                .formLogin().disable()
                .logout().disable();

        http.authorizeRequests()
                .requestMatchers(PRIVATE_URLS).authenticated();

        //OAuth2 - Đăng nhập từ mạng xã hội
        http
                .oauth2Login()
                .defaultSuccessUrl("/oauth2-success", true)
                .failureUrl("/oauth2-failed")
                .authorizationEndpoint();
//                .baseUri("/oauth2/authorization");

        return http.build();
    }

}
