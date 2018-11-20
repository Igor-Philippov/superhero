package com.superhero;

import static com.superhero.rest.constant.Paths.MISSION_BY_ID;
import static com.superhero.rest.constant.Paths.SUPERHERO_BY_ID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.username}")
    private String username;

    @Value("${spring.security.password}")
    private String password;

    @Value("${spring.security.role}")
    private String role;

    @Autowired
    public void configAuthenticationProvider(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(username).password(password).roles(role);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http
        .csrf().disable()
        .authorizeRequests()
          .antMatchers(HttpMethod.DELETE, MISSION_BY_ID).authenticated()
          .antMatchers(HttpMethod.DELETE, SUPERHERO_BY_ID).authenticated()
          .anyRequest().permitAll()
          .and()
          .httpBasic()
          .and()
          .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/hello");
    }
}