package com.eazybytes.eazyschool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.eazybytes.eazyschool.constants.EazySchoolConstants.ADMIN_ROLE;
import static com.eazybytes.eazyschool.constants.EazySchoolConstants.STUDENT_ROLE;

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
                                "/displayMessages/**",
                                "/admin/**",
                                "/closeMsg/**").hasRole(ADMIN_ROLE)
                        .requestMatchers(
                                "/student/**").hasRole(STUDENT_ROLE).
                        requestMatchers(
                                "/home",
                                "/holidays/**",
                                "/contact",
                                "/saveMsg",
                                "/courses",
                                "/about",
                                "/login",
                                "/logout",
                                "/public/**",
                                "/assets/**").permitAll())
                .formLogin(loginConfigurer -> loginConfigurer
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard")
                        .failureUrl("/login?error=true").permitAll())
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true).permitAll())
                .httpBasic(Customizer.withDefaults());

//        http.csrf(csrf -> csrf
//                        .ignoringRequestMatchers("/saveMsg")
//                        .ignoringRequestMatchers("/public/**"))
//                .authorizeHttpRequests(requests -> requests
//                        .requestMatchers("/dashboard").authenticated()
//                        .requestMatchers("/displayProfile").authenticated()
//                        .requestMatchers("/updateProfile").authenticated()
//                        .requestMatchers("/displayMessages/**").hasRole(ADMIN_ROLE)
//                        .requestMatchers("/closeMsg/**").hasRole(ADMIN_ROLE)
//                        .requestMatchers("/admin/**").hasRole(ADMIN_ROLE)
//                        .requestMatchers("/student/**").hasRole(STUDENT_ROLE)
//                        .requestMatchers("/", "/home").permitAll()
//                        .requestMatchers("/holidays/**").permitAll()
//                        .requestMatchers("/contact").permitAll()
//                        .requestMatchers("/saveMsg").permitAll()
//                        .requestMatchers("/courses").permitAll()
//                        .requestMatchers("/about").permitAll()
//                        .requestMatchers("/assets/**").permitAll()
//                        .requestMatchers("/login").permitAll()
//                        .requestMatchers("/logout").permitAll()
//                        .requestMatchers("/public/**").permitAll())
//                .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login")
//                        .defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll())
//                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/login?logout=true")
//                        .invalidateHttpSession(true).permitAll())
//                .httpBasic(Customizer.withDefaults());

            return http.build();
    }

    @Bean
    PasswordEncoder getPassWordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
