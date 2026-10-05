package com.deepak.cartService.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InternalServiceTokenFilter extends OncePerRequestFilter {

	@Value("${internal.service.token}")
	private String internalServiceToken;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = request.getHeader("X-Internal-Token");

		if (token != null && token.equals(internalServiceToken)) {

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					"INTERNAL_SERVICE", null, List.of(new SimpleGrantedAuthority("ROLE_INTERNAL_SERVICE")));

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

}
