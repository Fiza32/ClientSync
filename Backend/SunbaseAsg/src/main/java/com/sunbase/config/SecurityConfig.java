package com.sunbase.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.sunbase.jwt.JwtAuthenticationEntryPoint;
import com.sunbase.jwt.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final JwtAuthenticationEntryPoint point;
	private final JwtAuthenticationFilter filter;
	
	public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtAuthenticationEntryPoint point, JwtAuthenticationFilter filter) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.point = point;
		this.filter = filter;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.sessionManagement(se -> se.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).cors(cors -> {
			cors.configurationSource(new org.springframework.web.cors.CorsConfigurationSource() {

				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration configuration = new CorsConfiguration();
					configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
					configuration.setAllowedMethods(Collections.singletonList("*"));
					configuration.setAllowCredentials(true);
					configuration.setAllowedHeaders(Collections.singletonList("*"));
					configuration.setExposedHeaders(Arrays.asList("Authorization"));
					return configuration;
				}
			});
		})
                .authorizeHttpRequests(requests -> requests
                                .requestMatchers(
                                        "/api/sunbase/auth/register",
                                        "/api/sunbase/auth/login"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}
}
