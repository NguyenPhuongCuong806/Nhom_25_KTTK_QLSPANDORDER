package com.example.userservice.filter;
import com.example.userservice.authen.UserPrincipal;
import com.example.userservice.models.Token;
import com.example.userservice.services.TokenService;
import com.example.userservice.until.JwtUntil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUntil jwtUtil;

    @Autowired
    private TokenService verificationTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader
                = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authorizationHeader);

        UserPrincipal user = null;
        Token token = null;

        if (StringUtils.hasText(authorizationHeader) &&
                authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            user = jwtUtil.getUserFromToken(jwt);
            token = verificationTokenService.findByToken(jwt);
        }

        if (null != user && null != token && token.getTokenExpDate().after(new Date())) {
            Set<GrantedAuthority> authorities = new HashSet<>();

            user.getAuthorities().forEach(
                    p -> authorities.add(new SimpleGrantedAuthority((String) p)));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, authorities);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }


}
