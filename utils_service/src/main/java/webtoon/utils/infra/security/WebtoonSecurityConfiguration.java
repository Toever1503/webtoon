package webtoon.utils.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import webtoon.utils.infra.security.jwt.IJwtService;
import webtoon.utils.infra.security.jwt.JwtAuthenticationProvider;
import webtoon.utils.infra.security.jwt.JwtFilter;

public class WebtoonSecurityConfiguration {

    // List of public urls
    /*
     * Example: new OrRequestMatcher(new
     * AntPathRequestMatcher("/api/v1/categories/**"))
     */
    private final RequestMatcher PUBLIC_URLS;

    private final RequestMatcher PRIVATE_URLS;

    public WebtoonSecurityConfiguration(RequestMatcher pUBLIC_URLS) {
        super();
        this.PUBLIC_URLS = pUBLIC_URLS;
        this.PRIVATE_URLS = new NegatedRequestMatcher(PUBLIC_URLS);
    }

    @Autowired
    @Lazy
    private IJwtService userService;

    // Gain access for public urls
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    // Authentication manager bean config
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .parentAuthenticationManager(authentication -> {
                    throw new ProviderNotFoundException("No AuthenticationProvider found for " + authentication);
                }).build();
        httpSecurity.authenticationProvider(new JwtAuthenticationProvider())
                .authenticationManager(authenticationManager);
        return authenticationManager;
    }

    // Filter chain bean config
    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().disable();
        http.cors().and().csrf().disable().formLogin().disable().logout().disable();

        http.authorizeHttpRequests().requestMatchers(PRIVATE_URLS).authenticated();
        http.addFilterBefore(new JwtFilter(this.userService),
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
