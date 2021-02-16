package com.qbi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;

import com.qbi.authentication.CrossOriginsFilter;
import com.qbi.authentication.TokenRequestFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	TokenRequestFilter tokenRequestFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService);
	}

	@Override 
	protected void configure(HttpSecurity http) throws Exception{
		http.addFilterBefore(new CrossOriginsFilter(), ChannelProcessingFilter.class);
		
		http.authorizeRequests()
			.antMatchers("/Authenticate").permitAll()
			.antMatchers("/hello").permitAll()
			.antMatchers("/api-doc").permitAll()
			// For testing purpose TODO:delete
			.antMatchers("/test/role/admin").hasAuthority("ADMIN")
			.antMatchers("/test/role/sales").hasAuthority("SALES")
			.antMatchers("/test/role/user").hasAuthority("USER")
			.antMatchers("/users/companies/users").hasAnyAuthority("SALES", "ADMIN")
			//
			.anyRequest().authenticated()
		   	.and().httpBasic();
		
		http.headers().frameOptions().disable();
		http.cors().and().csrf().disable();
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(tokenRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder getPasswordEncoder() {return NoOpPasswordEncoder.getInstance(); }
}
