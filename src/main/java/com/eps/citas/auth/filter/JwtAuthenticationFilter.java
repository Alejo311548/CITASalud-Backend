package com.eps.citas.auth.filter;

import com.eps.citas.auth.util.JwtTokenUtil;
import com.eps.citas.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Rutas que se deben excluir del filtro
        if (path.startsWith("/api/auth/")
                || path.startsWith("/api/especialidades/")
                || path.startsWith("/api/sedes/")
                || path.startsWith("/api/profesionales/")) {

            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtTokenUtil.extractUsername(token);
        }

        System.out.println("Token recibido: " + token);

        if (username != null) {
            System.out.println("Username extraído del token: " + username);

            var userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("UserDetails: " + userDetails);

            if (jwtTokenUtil.validateToken(token)) {
                System.out.println("Token válido");

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("Token inválido");
            }
        } else {
            System.out.println("No se extrajo username o ya está autenticado");
        }

        filterChain.doFilter(request, response);
    }
}
