package webtoon.account.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //OAuth2 - Đăng nhập từ mạng xã hội
        http
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .loginPage("/oauth2/login/form")
                .defaultSuccessUrl("/oauth2/login/success", true)
                .failureUrl("/oauth2/login/error")
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization");
        return http.build();
    }
}
