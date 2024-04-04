package com.maidscc.libraryManagementSystem.aspect;

import com.maidscc.libraryManagementSystem.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.maidscc.libraryManagementSystem.services.implementations.UserServiceImpl.*(..)) || " +
            "execution(* com.maidscc.libraryManagementSystem.services.implementations.BookServiceImpl.*(..)) || " +
            "execution(* com.maidscc.libraryManagementSystem.services.implementations.BorrowingServiceImpl.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        String currentUser = UserUtil.getLoggedInUser();
        logger.info("Entering method: {}.{} by user: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                currentUser);
    }

    @AfterReturning(pointcut = "execution(* com.maidscc.libraryManagementSystem.services.implementations.UserServiceImpl.*(..))",
            returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: {}.{} with result: {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "execution(* com.maidscc.libraryManagementSystem.services.implementations.UserServiceImpl.*(..))",
            throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), ex.getMessage());
    }

    @Pointcut("execution(* com.maidscc.libraryManagementSystem.services.implementations.BookServiceImpl.*(..))")
    private void inBookServiceImpl() {}

    @Before("inBookServiceImpl()")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String logMessage = String.format("Method %s() in class %s is called by user: %s",
                methodName, className, UserUtil.getLoggedInUser());
        log.info(logMessage);
    }

    @AfterThrowing(pointcut = "inBookServiceImpl()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String logMessage = String.format("Exception thrown from method %s() in class %s: %s",
                methodName, className, exception.getMessage());
        log.error(logMessage, exception);
    }
}
