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

    @Bean
    public FilterRegistrationBean<TransactionLoggerFilter> transactionLoggerFilterRegistration() {
        FilterRegistrationBean<TransactionLoggerFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(transactionLoggerFilter);
        registration.addUrlPatterns("/api/transactions");
        return registration;
    }
}