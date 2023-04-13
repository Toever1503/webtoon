package webtoon.account.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    private final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/signin"),
            new AntPathRequestMatcher("/oauth2-failed"),
            new AntPathRequestMatcher("/static/**"),
            new AntPathRequestMatcher("/**")
    );
    private RequestMatcher PRIVATE_URLS = new NegatedRequestMatcher(PUBLIC_URLS);


    public WebSecurityConfiguration() {
        System.out.println("SecurityConfiguration");
    }


    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

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
                .clientRegistrationRepository(clientRegistrationRepository)
                .authorizedClientService(oAuth2AuthorizedClientService)
                .defaultSuccessUrl("/oauth2-success", true)
                .failureUrl("/oauth2-failed")
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization");

        return http.build();
    }

}
