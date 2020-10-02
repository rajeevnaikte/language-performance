package com.rajeevn.languageperformance.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class JwtAuthFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        Algorithm algorithm = Algorithm.HMAC256("youraccesstokensecret");
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        verifier.verify(
                ((HttpServletRequest) request).getHeader("Authorization").split(" ")[1]
        );

        chain.doFilter(request, response);
    }
}
