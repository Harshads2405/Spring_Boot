package com.example.bookcrud.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterThrowing(pointcut = "execution(* com.example.bookcrud.service.*.*(..))", throwing = "ex")
    public void logAfterThrowing(Exception ex) {
        logger.error("Method failed with exception: {}", ex.getMessage());
    }
}