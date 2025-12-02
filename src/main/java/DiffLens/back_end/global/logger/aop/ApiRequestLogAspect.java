package DiffLens.back_end.global.logger.aop;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.service.auth.CurrentUserService;
import DiffLens.back_end.global.fastapi.FastApiRequestType;
import DiffLens.back_end.global.logger.annotations.SubServerExecutionTime;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ApiRequestLogAspect {

    private final CurrentUserService authService;

    /**
     * 전/후 처리 모두 가능
     * CommonPointcut.restControllerEndpoints() Pointcut 지정하여
     * API 호출 시 호출 전후로 로그 출력하도록 하는 Aspect
     */
    @Around("DiffLens.back_end.global.logger.aop.CommonPointCut.restControllerEndpoints()")
    public Object logApiRequest(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();

        Member currentUser = null;
        try { currentUser = authService.getCurrentUser(); } catch (Exception ignored) {}

        String methodName = jp.getSignature().getName();
        String args = jp.getArgs() != null ? String.join(", ", java.util.Arrays.stream(jp.getArgs())
                .map(String::valueOf).toArray(String[]::new)) : "";

        String userInfo = currentUser != null ? "[User: " + currentUser.getId() + "]" : "[User: Anonymous]";
        String requestInfo = "[" + httpMethod + ": " + uri + " - " + methodName + "(" + args + ")]";

        log.info("⏳ [API 호출 시작] {} {}", userInfo, requestInfo);

        try {
            Object result = jp.proceed();
            long end = System.currentTimeMillis();
            log.info("✅ [API 호출 종료] {} {} - 실행시간: {}ms", userInfo, requestInfo, (end - start));
            return result;
        } catch (Throwable ex) {
            long end = System.currentTimeMillis();
            log.error("❌ [API 호출 예외] {} {} - 실행시간: {}ms - 예외: {}", userInfo, requestInfo, (end - start), ex.getMessage());
            throw ex; // 예외를 다시 던져서 컨트롤러에게 전달
        }
    }

    @Around("DiffLens.back_end.global.logger.aop.CommonPointCut.methodRuntimeEndpoints()")
    public Object subServerExecutionTime(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();

        SubServerExecutionTime annotation = method.getAnnotation(SubServerExecutionTime.class);
        String label = annotation.value();

        // === FastApiRequestType 찾아오기 ===
        FastApiRequestType apiType = null;
        for (Object arg : jp.getArgs()) {
            if (arg instanceof FastApiRequestType) {
                apiType = (FastApiRequestType) arg;
                break;
            }
        }

        // label 확장
        if (apiType != null)
            label = label + " - " + apiType.getName();     // ← 자동 확장

        try {
            Object result = jp.proceed();
            long end = System.currentTimeMillis();
            log.info("⏱️ [{}] {}: {}ms", label, signature.toShortString(), (end - start));
            return result;
        } catch (Throwable ex) {
            long end = System.currentTimeMillis();
            log.error("💥 [{}] {} ({}ms) - {}",
                    label,
                    signature.toShortString(),
                    (end - start),
                    ex.getMessage());
            throw ex;
        }
    }


}