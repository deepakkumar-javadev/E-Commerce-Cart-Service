package com.deepak.cartService.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final InternalServiceTokenFilter internalServiceTokenFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(auth -> auth

						.requestMatchers("/cart/createcart", "/cart/getcart/**", "/cart/removecart/cart/*/items/*",
								"/cart/updatefield/**")
						
						
						.hasRole("CUSTOMER")
						//public urls
						.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
						.permitAll().requestMatchers("/cart/removecart/cart/*").hasRole("ADMIN")

						.requestMatchers("/cart/clear/**").hasRole("INTERNAL_SERVICE")

						.anyRequest().authenticated())

				.addFilterBefore(internalServiceTokenFilter, UsernamePasswordAuthenticationFilter.class)

				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}