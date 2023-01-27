package nl.jemaja.weekmenu.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private DataSource dataSource;
	
	@Value("${jemaja.weekmenu.username}")
	private String username;
	
	@Value("${jemaja.weekmenu.password}")
	private String password;

	/*@Bean
	public BCryptPasswordEncoder encode() {
		log.debug("creating BCryptPasswordEncoder");
		return new BCryptPasswordEncoder();
	}
	*/

	@Bean
	public UserDetailsService userDetailsService() {
		
		log.debug("got user: "+username);
		
		UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username(username)
				.password(password)
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests((requests) -> requests
			.antMatchers("/js/*", "/home","/api/v1/init","/h2-console/*").permitAll()
			.anyRequest().authenticated()
		)
		.formLogin((form) -> form
			.loginPage("/login")
			.permitAll()
		)
		.logout((logout) -> logout.permitAll());
		
		/*
		 * Note i added this to make add recipe work again. However, this potentially is a security risk.
		 * See:https://www.baeldung.com/spring-security-csrf
		 * Added to backlog
		 */
		
		http.csrf().disable();
		return http.build();

}
	}
