package com.example.magoyaapp.middleware;

import com.example.magoyaapp.enums.TransactionType;
import com.example.magoyaapp.model.Transaction;
import com.example.magoyaapp.request.CachedBodyHttpServletRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionLoggerFilter extends OncePerRequestFilter {

    private static final BigDecimal DEPOSIT_THRESHOLD = BigDecimal.valueOf(10000);
    private static final Logger logger = LoggerFactory.getLogger(TransactionLoggerFilter.class);

    private final ObjectMapper objectMapper;

    public TransactionLoggerFilter() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        /*
         * Esta condición verifica si la solicitud es una solicitud POST a la ruta "/api/transactions".
         */
        if ("/api/transactions".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            /*
             * CachedBodyHttpServletRequest es una subclase de HttpServletRequestWrapper que almacena en caché
             * el cuerpo de la solicitud para poder leerlo varias veces.
             */
            CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
            String requestBody = IOUtils.toString(cachedRequest.getInputStream(), StandardCharsets.UTF_8);

            try {
                /*
                 * Se deserializa el cuerpo de la solicitud JSON a un objeto Transaction.
                 */
                Transaction transaction = objectMapper.readValue(requestBody, Transaction.class);

                transaction.setTimestamp(LocalDateTime.now());

                /*
                 * Si el tipo de transacción es un depósito y el monto es superior al umbral de $10000,
                 * se registran detalles de la transacción en el registro.
                 */
                if (transaction.getType() == TransactionType.DEPOSIT && transaction.getAmount().compareTo(DEPOSIT_THRESHOLD) > 0) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedTimestamp = transaction.getTimestamp().format(formatter);
                    logger.info("Depósito superior a $10000 - Fecha y hora: {}", formattedTimestamp);
                    logger.info("Account ID: {}", transaction.getAccountId());
                    logger.info("Monto de la transaccion: {}", transaction.getAmount());
                }
            } catch (JsonProcessingException e) {

                logger.error("Error processing JSON", e);
            }

            /*
             * Se pasa la solicitud con el cuerpo en caché al siguiente filtro en la cadena.
             */
            filterChain.doFilter(cachedRequest, response);
        } else {
            /*
             * Si la solicitud no es una solicitud POST a "/api/transactions",
             * simplemente se pasa al siguiente filtro en la cadena.
             */
            filterChain.doFilter(request, response);
        }
    }
}