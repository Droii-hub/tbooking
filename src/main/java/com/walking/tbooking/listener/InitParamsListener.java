package com.walking.tbooking.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class InitParamsListener implements ServletContextListener {


    public void contextInitialized(ServletContextEvent event){
        ServletContext context=event.getServletContext();

    }
}
