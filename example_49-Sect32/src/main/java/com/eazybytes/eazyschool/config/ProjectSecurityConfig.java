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

import static com.eazybytes.eazyschool.constants.EazySchoolConstants.ADMIN_ROLE;
import static com.eazybytes.eazyschool.constants.EazySchoolConstants.STUDENT_ROLE;
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
             .authorizeHttpRequests(authorize -> authorize
                     .requestMatchers("/dashboard").authenticated()
                     .requestMatchers("/displayProfile").authenticated()
                     .requestMatchers("/updateProfile").authenticated()
                     .requestMatchers("/api/**").authenticated()
                     .requestMatchers("/data-rest/**").authenticated()
                     .requestMatchers("/closeMsg/**").hasRole(ADMIN_ROLE)
                     .requestMatchers("/displayMessages/**").hasRole(ADMIN_ROLE)
                     .requestMatchers("/admin/**").hasRole(ADMIN_ROLE)
                     .requestMatchers("/student/**").hasRole(STUDENT_ROLE)
                     .requestMatchers("/","/home").permitAll() // empty pattern "" is no more allowed
                     .requestMatchers("/holidays/**").permitAll()
                     .requestMatchers("/contact").permitAll()
                     .requestMatchers("/saveMsg").permitAll()
                     .requestMatchers("/courses").permitAll()
                     .requestMatchers("/about").permitAll()
                     .requestMatchers("/logout").permitAll()
                     .requestMatchers("/login").permitAll()
                     .requestMatchers("/assets/**").permitAll()
                     .requestMatchers("/public/**").permitAll()
                     .requestMatchers(toH2Console()).permitAll())
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
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
