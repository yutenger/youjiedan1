package com.stardai.manage.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/*", filterName = "headersCORSFilter")
public class HeadersCORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse,
            FilterChain chain) throws IOException, ServletException {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            //HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            //String url = httpServletRequest.getRequestURI();
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT,HEAD");
            response.setHeader("Access-Control-Max-Age", "1728000");
            response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER,Authentication,Origin,X-Requested-With,Content-Type,Accept");
            chain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}