package com.example.magoyaapp.config;

import com.example.magoyaapp.middleware.TransactionLoggerFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class AppConfig implements WebMvcConfigurer {

    private final TransactionLoggerFilter transactionLoggerFilter;


    public AppConfig(TransactionLoggerFilter transactionLoggerFilter) {
        this.transactionLoggerFilter = transactionLoggerFilter;
    }


    /*
     * Se método define un bean que registra el filtro TransactionLoggerFilter en la aplicación.
     * El método crea una instancia de FilterRegistrationBean y configura los siguientes aspectos:
     *
     * 1. Se establece el filtro a través de la inyección de la instancia de TransactionLoggerFilter.
     * 2. Se especifica la ruta URL a la que se aplicará el filtro ("/api/transactions").
     *
     * De esta manera, el filtro TransactionLoggerFilter se aplicará a todas las solicitudes que coincidan
     * con la ruta "/api/transactions".
     */
    @Bean
    public FilterRegistrationBean<TransactionLoggerFilter> transactionLoggerFilterRegistration() {
        FilterRegistrationBean<TransactionLoggerFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(transactionLoggerFilter);
        registration.addUrlPatterns("/api/transactions");
        return registration;
    }
}