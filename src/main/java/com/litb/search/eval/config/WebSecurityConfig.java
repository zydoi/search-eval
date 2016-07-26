package com.litb.search.eval.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/index").hasRole("ADMIN")
				.antMatchers("/itemlist").hasAnyRole("USER", "ADMIN")
				.and()
				.formLogin();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("admin").password("32167").roles("ADMIN").and()
				.withUser("user").password("123456").roles("USER").and()
				.withUser("zhangyiding").password("32167").roles("ADMIN").and()
				.withUser("xiachunyan").password("123456").roles("USER");
	}
}
