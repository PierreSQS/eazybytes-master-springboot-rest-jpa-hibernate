package com.eazybytes.eazyschool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().ignoringAntMatchers("/saveMsg").ignoringAntMatchers("/public/**")
                .and()
                    .authorizeRequests()
                    .mvcMatchers("/dashboard").authenticated()
                    .mvcMatchers("/displayMessages").hasRole("ADMIN")
                    .mvcMatchers("/home").permitAll()
                    .mvcMatchers("/holidays/**").permitAll()
                    .mvcMatchers("/public/**").permitAll()
                    .mvcMatchers("/contact").permitAll()
                    .mvcMatchers("/saveMsg").permitAll()
                    .mvcMatchers("/courses").permitAll()
                    .mvcMatchers("/about").permitAll()
                    .mvcMatchers("/login").permitAll()
                .and()
                    .formLogin().loginPage("/login").defaultSuccessUrl("/dashboard")
                    .failureUrl("/login?error=true").permitAll()
                .and()
                    .logout().logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).permitAll()
                .and()
                    .httpBasic();


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user").password("{bcrypt}$2a$10$dCBD.jGcNRzgKrMCsHEKM.D/Izbt5dyI.m4EiC8VGEnygd8unm/pO")
                .roles("USER")
            .and()
            .withUser("admin").password("{bcrypt}$2a$10$HToPCyb1oOC0i4uy9ubQ.OlN3IO2H7No8gMySG1CbGmvP4aA0.LUi")
                .roles("ADMIN");
    }

}
