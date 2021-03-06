package com.example.finalProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.finalProject.filter.JwtFilter;
import com.example.finalProject.service.CustomUserDetailService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Properties;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtFilter jwtFilter;
	@Autowired
	private CustomUserDetailService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("childappadmain@gmail.com");
		mailSender.setPassword("aabxuafmcdcshfrq");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

  @Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers("/postTemp/public/{id}","/authenticate","/login", "/signup","/parentverification","/","/chat/{to}","/topic/messages/","/chat","/registration/{userName}","/fetchAllUsers","/games","/games/{id}","/games/category/{category}","/send", "/videos/childrenStories", "/videos/{query}", "/videos/prophetsStories","/loginAsParent","/post/public/{id}","/userposts/{id}","/events","/event/{id}","/eventbytitle/{title}","/group/{title}","/attendgroups/{id}","/topic/drow","/user/{userName}").permitAll().antMatchers(HttpMethod.OPTIONS, "/**")
				.permitAll().anyRequest().authenticated().and().exceptionHandling().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	  	http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
