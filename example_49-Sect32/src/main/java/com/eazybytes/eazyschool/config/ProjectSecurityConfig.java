package com.eazybytes.eazyschool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(csrfConfigurer -> csrfConfigurer
                     .ignoringRequestMatchers("/saveMsg")
                     .ignoringRequestMatchers("/h2-console/**")
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
                     .requestMatchers("/displayMessages/**").hasRole("ADMIN")
                     .requestMatchers("/admin/**").hasRole("ADMIN")
                     .requestMatchers("/student/**").hasRole("STUDENT")
                     .requestMatchers("/h2-console/**").permitAll()
                     .requestMatchers("/home").permitAll()
                     .requestMatchers("/holidays/**").permitAll()
                     .requestMatchers("/contact").permitAll()
                     .requestMatchers("/saveMsg").permitAll()
                     .requestMatchers("/courses").permitAll()
                     .requestMatchers("/about").permitAll()
                     .requestMatchers("/login").permitAll())
             .formLogin(loginConfigurer -> loginConfigurer
                     .loginPage("/login")
                     .defaultSuccessUrl("/dashboard")
                     .failureUrl("/").permitAll())
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
