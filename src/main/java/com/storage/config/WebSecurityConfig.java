package com.storage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{


	//	@Autowired
	//	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
	//		auth.inMemoryAuthentication().withUser("bill").password("abc123").roles("USER");
	//		auth.inMemoryAuthentication().withUser("admin").password("root123").roles("ADMIN");
	//		auth.inMemoryAuthentication().withUser("dba").password("root123").roles("ADMIN","DBA");
	//	}

	@Autowired
	ManagerDetailService userdetailService;
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		.ignoring()
		.antMatchers("/static/**","/bower_components/**","/dist/**","/plugins/**");
	}

	@Autowired
	MyAuthenticationProvider authenticationprovider;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userdetailService);
		auth.authenticationProvider(this.authenticationprovider);

	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {

/*		http.authorizeRequests().antMatchers("/manager/login").permitAll().antMatchers("/product/**","/customer/**","/vat/**","/manager/**","/","/index").
		permitAll()hasRole("ADMIN").anyRequest().authenticated().and().formLogin().loginPage("/login").loginProcessingUrl("/manager/login").successHandler(new SimpleLoginSuccessHandler())
		.permitAll().and().logout()logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.deleteCookies("JSESSIONID")
		.invalidateHttpSession(true).and().rememberMe().tokenValiditySeconds(3600*24);
		*/
		/*http.authorizeRequests().antMatchers("/manager/login").permitAll().antMatchers("/product/**","/customer/**","/vat/**","/manager/**","/","/index")
		.hasAnyRole("ADMIN").anyRequest().authenticated().and().formLogin().loginPage("/login").loginProcessingUrl("manager/login").successHandler(new SimpleLoginSuccessHandler())
		.permitAll().and()*/
		http.authorizeRequests().antMatchers("/manager/login").permitAll().antMatchers("/product/**","/customer/**","/vat/**","/manager/**","/","/index").
		hasRole("ADMIN").anyRequest().authenticated().and().formLogin().loginPage("/login").loginProcessingUrl("/manager/login").successHandler(new SimpleLoginSuccessHandler())
		.permitAll().and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.deleteCookies("JSESSIONID")
		.invalidateHttpSession(true).and().rememberMe().tokenValiditySeconds(3600*24);
		
		

		//		http.bas
	}




}
