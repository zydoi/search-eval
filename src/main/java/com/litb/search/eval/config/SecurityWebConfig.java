package com.litb.search.eval.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityWebConfig  extends WebSecurityConfigurerAdapter  {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("admin").password("32167").roles("ADMIN").and()
				.withUser("zhangyiding").password("32167").roles("ADMIN").and()
				.withUser("user").password("123").roles("USER").and()
				.withUser("xiachunyan").password("123").roles("USER");
	}
	
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/index").hasRole("ADMIN")
				.antMatchers("/select").hasRole("USER")
				.and()
			.formLogin()
				.loginPage("/login") 
				.permitAll();
	}
}
