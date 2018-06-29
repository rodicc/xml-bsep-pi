package ftn.xmlwebservisi.firme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import ftn.xmlwebservisi.firme.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().disable();
		http.csrf().csrfTokenRepository(csrfTokenRepository());
	
		// No session will be used or created by spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		// Entry points
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/register").permitAll()
			.antMatchers("/").permitAll()
			.antMatchers("/public").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(new LoginProcessingFilter(authenticationManager()))
			.addFilterBefore(jwtAuthenticationTokenFilter(), BasicAuthenticationFilter.class)
			
			//inserting CsrfHeaderFilter after the Spring security CsrfFilter 
			//so that the request attribute is available
			.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// Allowing access to static resources without authentication
		web.ignoring()
			.antMatchers("/assets/**")
			.antMatchers("/js/**")
			.antMatchers("/view/**")
			.antMatchers("/app.js")
			.antMatchers("/index.html")
			.antMatchers("/favicon.ico");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Using AuthenticationManagerBuilder to create an instance of AuthenticationManager
		// which is used for user authentication
		auth.userDetailsService(userService)
			.passwordEncoder(bCryptPasswordEncoder());
	}
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public TokenAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
		return new TokenAuthenticationFilter();
	}
	
	@Bean
	public CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
	
	
}
