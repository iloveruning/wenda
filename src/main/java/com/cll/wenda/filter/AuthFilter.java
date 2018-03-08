package com.cll.wenda.filter;

import com.cll.wenda.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * @author chenliangliang
 * @date: 2017/11/19
 */
@WebFilter(urlPatterns = {"/index/*","/question","/question/*","/answer/*","/answer","/comment/*","/comment","/reply","/reply/*","/report","/report/*"})
public class AuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req=(HttpServletRequest)servletRequest;

        String token=req.getHeader("token");
        if (token==null|| StringUtils.isEmpty(token)){
            servletResponse.setContentType("text/json;charset=utf8");
            servletResponse.getWriter().write("{\"status\":false,\"msg\":\"token is not found\"}");
            return;
        }
        Claims claims=JwtUtil.getClaimsFromToken(token);
        if (claims==null){

            servletResponse.setContentType("text/json;charset=utf8");
            servletResponse.getWriter().write("{\"status\":false,\"msg\":\"token is invalid\"}");
            return;
        }
        Date expiration=claims.getExpiration();
        if (expiration.before(new Date())){
            servletResponse.setContentType("text/json;charset=utf8");
            servletResponse.getWriter().write("{\"status\":false,\"msg\":\"token has extended upon expiration\"}");
            return;
        }

        servletRequest.setAttribute("uid",claims.get("uid"));
        servletRequest.setAttribute("name",claims.get("name"));
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
