package fch_shell.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import fch_shell.security.DatabaseAuthProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
    private DatabaseAuthProvider authProvider;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
          auth.authenticationProvider(authProvider);
    }
	
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.authorizeRequests()
			.antMatchers("/zkau/**").permitAll()
			.antMatchers("/css/**").permitAll()
			.antMatchers("/services/**").permitAll()
			.antMatchers("/images/**").permitAll()
			.antMatchers("/**").authenticated()
			.and()
			.formLogin()
			.loginProcessingUrl("/access_request")
			.loginPage("/login.zul")
			.permitAll()
			.defaultSuccessUrl("/index.zul", true)
			.failureUrl("/login.zul")
			.and()
	        .logout().logoutSuccessUrl("/login.zul")
	        .logoutUrl("/j_spring_security_logout")
	        .and().csrf().disable();
		
		http.headers()
	        .frameOptions().sameOrigin()
	        .httpStrictTransportSecurity().disable();
	}

}
