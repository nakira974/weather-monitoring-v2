package coffee.lkh.weathermonitoringv2.configurations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@Import(OAuth2Config.class)
public class SecurityConfig  {


    @Autowired
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;

    @Autowired
    private LogoutHandler keycloakLogoutHandler;


    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        // Create some dummy users for testing purposes
        var user = org.springframework.security.core.userdetails.User
                .withUsername("user")
                .password("password")
                .roles("USER")
                .build();
        var admin = org.springframework.security.core.userdetails.User
                .withUsername("admin")
                .password("password")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(List.of(user, admin));
    }
    @Bean
    public DefaultOAuth2UserService defaultOAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        http.authorizeRequests()
                .requestMatchers("/login", "/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(oidcUserService)
                .and().defaultSuccessUrl("/weather?city=Abbeville&country=US&state=LA").permitAll()
                .and().logout().addLogoutHandler(keycloakLogoutHandler)
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }
}