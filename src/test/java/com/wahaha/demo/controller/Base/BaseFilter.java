package com.wahaha.demo.controller.Base;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Mr.Fan
 */
public abstract class BaseFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
    }

}
