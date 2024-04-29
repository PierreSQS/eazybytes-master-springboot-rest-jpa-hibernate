package com.eazybytes.eazyschool.config;

import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@Import(H2ConsoleProperties.class)
public class ProjectSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(csrfConfigurer -> csrfConfigurer
                     .ignoringRequestMatchers("/saveMsg")
                     .ignoringRequestMatchers(toH2Console())
                     .ignoringRequestMatchers("/public/**")
                     .ignoringRequestMatchers("/api/**")
                     .ignoringRequestMatchers("/data-rest/**"))
             .headers(headers -> headers
                     .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
             .authorizeHttpRequests(authorize -> authorize
                     .requestMatchers("/dashboard").authenticated()
                     .requestMatchers("/displayProfile").authenticated()
                     .requestMatchers("/updateProfile").authenticated()
                     .requestMatchers("/api/**").authenticated()
                     .requestMatchers("/data-rest/**").authenticated()
                     .requestMatchers("/closeMsg/**").hasRole("ADMIN")
                     .requestMatchers("/displayMessages/**").hasRole("ADMIN")
                     .requestMatchers("/admin/**").hasRole("ADMIN")
                     .requestMatchers("/student/**").hasRole("STUDENT")
                     .requestMatchers(toH2Console()).permitAll()
                     .requestMatchers("/home").permitAll()
                     .requestMatchers("/holidays/**").permitAll()
                     .requestMatchers("/contact").permitAll()
                     .requestMatchers("/saveMsg").permitAll()
                     .requestMatchers("/courses").permitAll()
                     .requestMatchers("/about").permitAll()
                     .requestMatchers("/logout").permitAll()
                     .requestMatchers("/login").permitAll()
                     .requestMatchers("/public/**").permitAll())
             .formLogin(loginConfigurer -> loginConfigurer
                     .loginPage("/login")
                     .defaultSuccessUrl("/dashboard")
                     .failureUrl("/login?error=true").permitAll())
             .logout(logoutConfigurer -> logoutConfigurer
                     .logoutSuccessUrl("/login?logout=true")
                     .invalidateHttpSession(true).permitAll())
             .httpBasic(Customizer.withDefaults());

            return http.build();
    }

    @Bean
    PasswordEncoder getPassWordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
