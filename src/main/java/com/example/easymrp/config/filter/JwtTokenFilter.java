package com.example.easymrp.config.filter;

import com.example.easymrp.model.dto.ErrorResponse;
import com.example.easymrp.service.auth.CustomDetailsService;
import com.example.easymrp.utils.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (IllegalArgumentException e) {
            setErrorResponse(response, new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid JWT token data"));
            return;
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, new ErrorResponse(HttpStatus.UNAUTHORIZED, "JWT token expired"));
            return;
        }
        catch (SignatureException e) {
            setErrorResponse(response, new ErrorResponse(HttpStatus.UNAUTHORIZED, "JWT token signature not matched"));
            return;
        } catch (Exception e) {
            setErrorResponse(response, new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error!!"));
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private void setErrorResponse(HttpServletResponse response, ErrorResponse errorResponse) {
        try {
            response.getWriter().print(new ObjectMapper().writeValueAsString(errorResponse));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            System.out.println(e);
        }
    }

}
