package com.eazybytes.eazyschool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

            http.csrf(csrfConfigurer -> csrfConfigurer
                            .ignoringRequestMatchers("/saveMsg", "/public/**"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/dashboard",
                                "/displayProfile",
                                "/updateProfile").authenticated()
                        .requestMatchers(
                                "/displayMessages",
                                "/admin/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/student/**").hasRole("STUDENT").
                        requestMatchers(
                                "/home",
                                "/holidays/**",
                                "/contact",
                                "/saveMsg",
                                "/courses",
                                "/about",
                                "/login",
                                "/public/**").permitAll())
                    .formLogin(loginConfigurer -> loginConfigurer
                            .loginPage("/login")
                            .defaultSuccessUrl("/dashboard")
                            .failureUrl("/login?error=true").permitAll())
                    .logout(logoutConfigurer -> logoutConfigurer
                            .logoutSuccessUrl("/login?logout=true")
                            .invalidateHttpSession(true).permitAll())
                    .httpBasic(Customizer.withDefaults());


//                .mvcMatchers("/dashboard").authenticated()
//                .mvcMatchers("/displayProfile").authenticated()
//                .mvcMatchers("/updateProfile").authenticated()
//                .mvcMatchers("/displayMessages").hasRole("ADMIN")
//                .mvcMatchers("/admin/**").hasRole("ADMIN")
//                .mvcMatchers("/student/**").hasRole("STUDENT")
//                .mvcMatchers("/home").permitAll()
//                .mvcMatchers("/holidays/**").permitAll()
//                .mvcMatchers("/contact").permitAll()
//                .mvcMatchers("/saveMsg").permitAll()
//                .mvcMatchers("/courses").permitAll()
//                .mvcMatchers("/about").permitAll()
//                .mvcMatchers("/login").permitAll()
//                .mvcMatchers("/public/**").permitAll()
//                .and().formLogin().loginPage("/login")
//                .defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll()
//                .and().logout().logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).permitAll()
//                .and().httpBasic();

            return http.build();
    }

    @Bean
    PasswordEncoder getPassWordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
