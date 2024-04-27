package com.example.magoyaapp.middleware;

import com.example.magoyaapp.enums.TransactionType;
import com.example.magoyaapp.model.Transaction;
import com.example.magoyaapp.request.CachedBodyHttpServletRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Component
public class TransactionLoggerFilter extends OncePerRequestFilter {

    private static final BigDecimal DEPOSIT_THRESHOLD = BigDecimal.valueOf(10000);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if ("/api/transactions".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);

            String requestBody = IOUtils.toString(cachedRequest.getInputStream(), StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

            Transaction transaction = objectMapper.readValue(requestBody, Transaction.class);

            if (transaction.getType() == TransactionType.DEPOSIT && transaction.getAmount().compareTo(DEPOSIT_THRESHOLD) > 0) {
                System.out.println("Depósito superior a $10000");
                System.out.println("ID de cuenta: " + transaction.getAccountId());
                System.out.println("Monto: " + transaction.getAmount());
                System.out.println("Fecha y hora: " + transaction.getTimestamp());
            }

            filterChain.doFilter(cachedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}