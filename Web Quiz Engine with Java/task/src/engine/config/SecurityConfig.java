package engine.config;

import engine.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final UserService service;
    @Autowired
    public SecurityConfig(UserService service) {
        this.service = service;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/h2/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/actuator/shutdown/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/register", "POST")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/quizzes")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/quizzes/**")).authenticated()
                )
                .userDetailsService(service)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}