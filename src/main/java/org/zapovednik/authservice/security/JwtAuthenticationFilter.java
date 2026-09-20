package org.zapovednik.authservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zapovednik.authservice.configuration.constant.HttpConstant;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailServiceImpl;

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpConstant.AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(HttpConstant.BEARER_VALUE)) {
            filterChain.doFilter(request, response);
            return;
        }

        final int tokenBeginIndex = HttpConstant.BEARER_VALUE.length();
        final String token = authHeader.substring(tokenBeginIndex);
        final String login = jwtService.extractLogin(token);

        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(login);

            if (jwtService.isTokenValid(token, userDetails)) {
                final Long userId = jwtService.extractUserId(token);
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(userId);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}