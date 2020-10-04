package com.rajeevn.languageperformance.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthFilter extends BasicAuthenticationFilter
{

    public JWTAuthFilter(AuthenticationManager authenticationManager)
    {
        super(authenticationManager);
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256("youraccesstokensecret");
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(request.getHeader("Authorization").split(" ")[1]);

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("", null, null));
        }
        catch (Exception e)
        {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }
}
