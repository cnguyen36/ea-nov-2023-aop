package sean.ea.labs.lab5.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sean.ea.labs.lab5.entity.ActivityLog;
import sean.ea.labs.lab5.exception.AopIsAwesomeHeaderException;
import sean.ea.labs.lab5.repository.ActivityLogRepo;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class Lab5Aspect {

    private final ActivityLogRepo activityLogRepo;


    @Before("within(sean.ea.labs.lab5.controller..*) && @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void checkAopIsAwesomeHeader() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String aopHeader = request.getHeader("AOP-IS-AWESOME");
        if (!"true".equalsIgnoreCase(aopHeader)) {
            throw new AopIsAwesomeHeaderException("AOP-IS-AWESOME header is missing");
        }
    }


    @Around("@annotation(ExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info(joinPoint.getSignature() + " executed in " + (endTime - startTime) + "ms");
        ActivityLog activityLog = new ActivityLog();
        activityLog.setDate(LocalDateTime.now());
        activityLog.setOperation(String.valueOf(joinPoint.getSignature()));
        activityLog.setDuration(endTime - startTime);
        activityLogRepo.save(activityLog);
        return proceed;
    }
}
