package com.practice.lms.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.practice.lms..service.*.*(..))")
    public Object logServiceCall(final ProceedingJoinPoint joinPoint) throws Throwable {
        final var methodName = joinPoint.getSignature().toShortString();
        final var args = joinPoint.getArgs();

        log.debug(">> {} args={}", methodName, args);
        final var start = System.currentTimeMillis();

        try {
            final var result = joinPoint.proceed();
            log.debug("<< {} ({}ms)", methodName, System.currentTimeMillis() - start);
            return result;
        } catch (Exception ex) {
            log.warn("!! {} threw: {}", methodName, ex.getMessage());
            throw ex;
        }
    }
}
