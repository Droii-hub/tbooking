package com.walking.tbooking.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthorizationFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        if ("/login".equals(request.getServletPath())) {
            // Если запрос на логин - пускаем дальше по цепочке без дополнительных проверок
            chain.doFilter(request, response);
        }
        // Получаем объект сессии. Если сессии не существует - отправляем ошибку.
        HttpSession session = request.getSession(false);

        if ("/registration".equals(request.getServletPath())) {
            if (session==null)
            {
                request.setAttribute("roleId", 2);
            } else {
                request.setAttribute("roleId", session.getAttribute("roleId"));
            }
            chain.doFilter(request, response);
        }
        if (session == null) {
            response.sendError(401);
            return;
        }
        //Условия авторизации
        if (request.getServletPath().equals("/user")&request.getMethod().equals("GET")&!session.getAttribute("roleId").equals("1")) {
            response.sendError(401);
            return;
        }
        if (request.getServletPath().equals("/user/ban")&!session.getAttribute("roleId").equals("1")) {
            response.sendError(401);
            return;
        }
        if(request.getServletPath().equals("/passenger/search")&!session.getAttribute("roleId").equals("1")){
            response.sendError(401);
            return;
        }
        if(request.getServletPath().equals("/airport/admin")&!session.getAttribute("roleId").equals("1")){
            response.sendError(401);
            return;
        }

        chain.doFilter(request,response);
    }
}
