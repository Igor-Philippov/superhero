package com.superhero.security;

import static com.superhero.rest.constant.Paths.MISSIONS_DELETED;
import static com.superhero.rest.constant.Paths.MISSION_BY_ID;
import static com.superhero.rest.constant.Paths.SUPERHERO_BY_ID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.superhero.config.ApplicationConfig;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ApplicationConfig config;

	@Autowired
	public void configAuthenticationProvider(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder encoder = passwordEncoder();

		auth.inMemoryAuthentication()
			.withUser(config.getSpringSecurityUserName())
			.password(encoder.encode(config.getSpringSecurityUserPwd()))
			.roles(config.getSpringSecurityRole());
		
//		// Securizing actuator endpoints
//		auth.inMemoryAuthentication()
//		    .withUser("act_user")
//		    .password(encoder.encode("act_password"))
//		    .roles("ACTRADMIN");
	}
    
//    @Autowired
//    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
//        BCryptPasswordEncoder encoder = passwordEncoder();
//        auth.inMemoryAuthentication().withUser(username).password(encoder.encode(password)).roles(role);
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.csrf().disable()
             .authorizeRequests()
             .antMatchers(HttpMethod.DELETE, MISSION_BY_ID).authenticated()
             .antMatchers(HttpMethod.GET, MISSIONS_DELETED).authenticated()
             .antMatchers(HttpMethod.DELETE, SUPERHERO_BY_ID).authenticated()
             .anyRequest().permitAll()
             .and().httpBasic();
//         .and()
//         .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/hello");
    }
}