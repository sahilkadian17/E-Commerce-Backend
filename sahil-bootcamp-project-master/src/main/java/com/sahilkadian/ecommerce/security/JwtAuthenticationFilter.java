package com.sahilkadian.ecommerce.security;

import com.sahilkadian.ecommerce.entities.User;
import com.sahilkadian.ecommerce.repositories.TokenRepository;
import com.sahilkadian.ecommerce.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Value("${admin.email}")
    private String adminEmail;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJWTFromRequest(request);
        if(StringUtils.hasText(token) && jwtTokenGenerator.validateToken(token)){
            if(tokenRepository.existsByToken(token)){
                String username = jwtTokenGenerator.getUsernameFromToken(token);
                User user = userDetailsService.loadUserByUsername(username);
                if(!user.isActive()){
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("User Account Deactivated");
                    return;
                }
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(),null,user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            else{
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write("Token is Expired");
                return;
            }
        }
        String path = request.getRequestURI();
        System.out.println(path);
        if("/user/login".equals(path)){
            User user = userDetailsService.loadUserByUsername(request.getParameter("email"));
            if(user.isActive() && !user.isLocked()){
                String password = user.getPassword();
                if(!passwordEncoder.matches(request.getParameter("password"),password)){
                    if(!user.getEmail().equals(adminEmail)){
                        user.setInvalidAttemptCount(user.getInvalidAttemptCount()+1);
                        if(user.getInvalidAttemptCount()==3){
                            user.setLocked(true);
                            user.setActive(false);
                            userRepository.save(user);
                            response.setStatus(HttpStatus.LOCKED.value());
                            response.getWriter().write("Account Locked");
                            return;
                        }
                        userRepository.save(user);
                    }
                    else {
                        response.setStatus(HttpStatus.BAD_REQUEST.value());
                        response.getWriter().write("Password is Incorrect");
                        return;
                    }
                }
                else{
                    user.setInvalidAttemptCount(0L);
                    userRepository.save(user);
                }
            }
            else{
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write("Account Not Activated or Locked");
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    private String getJWTFromRequest(HttpServletRequest httpServletRequest){
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
