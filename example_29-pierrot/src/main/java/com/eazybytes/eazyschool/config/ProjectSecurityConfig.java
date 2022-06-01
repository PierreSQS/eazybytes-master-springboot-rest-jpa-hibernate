package com.eazybytes.eazyschool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

            http.csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/home").authenticated()
                .mvcMatchers("/holidays/**").permitAll()
                .mvcMatchers("/contact").permitAll()
                .mvcMatchers("/saveMsg").permitAll()
                .mvcMatchers("/courses").permitAll()
                .mvcMatchers("/about").permitAll()
                .and().formLogin().and().httpBasic();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                    .password("{bcrypt}$2a$10$F1GQjxZSwZjCbb.75hHsn.7cNLwNQsR48EnWqWSpZN2Ctu.MjLiW.")
                    .roles("USER")
                .and()
                .withUser("admin").
                    password("{bcrypt}$2a$10$YK339vcP40I6BJp0jQDCSewyFu7FGqNgkWwA5WD2y2.VpxCg1fry.")
                    .roles("USER","ADMIN");
    }
}
