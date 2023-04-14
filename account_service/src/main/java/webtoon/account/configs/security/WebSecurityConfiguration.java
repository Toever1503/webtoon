package webtoon.account.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import webtoon.account.configs.security.jwt.JwtFilter;
import webtoon.account.services.IUserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity

public class WebSecurityConfiguration {
    private final RequestMatcher PUBLIC_URLS;
    private RequestMatcher PRIVATE_URLS;


    public WebSecurityConfiguration(List<AntPathRequestMatcher>[] publicRequestPaths) {
        List<AntPathRequestMatcher> publics = Stream.of(publicRequestPaths)
                .flatMap(Collection::stream).collect(Collectors.toList());
        this.PUBLIC_URLS = new OrRequestMatcher(publics.toArray(new AntPathRequestMatcher[0]));
        this.PRIVATE_URLS = new NegatedRequestMatcher(PUBLIC_URLS);
        System.out.println("SecurityConfiguration");
    }


    @Value("${server.port}")
    private String serverPort;

    @Autowired
    @Lazy
    private IUserService userService;
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
        http.cors().and().csrf().disable();

        http.authorizeRequests()
                .requestMatchers(PRIVATE_URLS).authenticated();
        http.formLogin().disable()
                .logout().disable();
        //OAuth2 - Đăng nhập từ mạng xã hội
        http
                .oauth2Login()
                .clientRegistrationRepository(clientRegistrationRepository)
                .authorizedClientService(oAuth2AuthorizedClientService)
                .defaultSuccessUrl("/oauth2-success", true)
                .failureUrl("/oauth2-failed")
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization");

        http.addFilterBefore(new JwtFilter(this.userService), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);


        // for redirect to login page
        PortMapperImpl portMapper = new PortMapperImpl();
        portMapper.setPortMappings(Collections.singletonMap(serverPort, serverPort));
        PortResolverImpl portResolver = new PortResolverImpl();
        portResolver.setPortMapper(portMapper);
        LoginUrlAuthenticationEntryPoint entryPoint = new LoginUrlAuthenticationEntryPoint(
                "/login");
        entryPoint.setPortMapper(portMapper);
        entryPoint.setPortResolver(portResolver);
        http
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint);


        return http.build();
    }

}
