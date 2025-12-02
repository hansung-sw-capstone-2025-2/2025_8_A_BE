package DiffLens.back_end.global.logger.aop;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointCut {

    // API 호출 시 로그 찍도록 @RestController 어노테이션에 PointCut 설정
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerEndpoints() {}

    @Pointcut("@annotation(DiffLens.back_end.global.logger.annotations.SubServerExecutionTime)")
    public void methodRuntimeEndpoints() {}

}
