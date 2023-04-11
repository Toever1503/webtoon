package webtoon.account.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .cors().disable();

        http
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers().permitAll()
//                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login/form")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);
        //OAuth2 - Đăng nhập từ mạng xã hội
        http
                .oauth2Login()
                .defaultSuccessUrl("/login/success", true)
                .failureUrl("/login/error")
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization");

        return http.build();
    }

}
