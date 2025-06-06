package com.walking.tbooking.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class SerializationFilter extends HttpFilter {
    private ObjectMapper mapper;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        //Инициализируем маппер, если еще не
        if (mapper==null)
            mapper=(ObjectMapper) getFilterConfig().getServletContext().getAttribute("objectMapper");
        //Ждем отработки остальных фильтров и сервлетов
        chain.doFilter(req,res);
        //Получаем объект для сериализации, установленный в сервлете
        Object javaObject=req.getAttribute("responseJavaObject");

        if(javaObject==null)
            return;
        //Сериализуем
        byte[] jsonBody=mapper.writeValueAsBytes(List.of(javaObject));
        // Устанавливаем тип ответа
        res.setContentType("application/json");
        // Устанавливаем размер ответа. Больше для демонстрации, обычно это происходит автоматически по факту коммита ответа
        res.setContentLength(jsonBody.length);
        // Записываем JSON в тело ответа
        res.getOutputStream().write(jsonBody);
    }
}
