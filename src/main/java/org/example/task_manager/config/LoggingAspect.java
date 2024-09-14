package org.example.task_manager.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(* org.example.task_manager.controller..*(..))")
    public void pointcutLog() {}

    // Нужно указывать полный адрес. Иначе работать не будет
    @Pointcut("@annotation(org.example.task_manager.annotations.Log)")
    public void callAspectByAnnotation() {}

    @Before("pointcutLog()")
    public void writeLog(JoinPoint joinPoint) {
        log.warn("Calling method: {}", joinPoint.getSignature().getName());
    }

    @After("callAspectByAnnotation()")
    public void logAfter(JoinPoint joinPoint) {
        log.warn("After calling method: {}", joinPoint.getSignature().getName());
    }
}
