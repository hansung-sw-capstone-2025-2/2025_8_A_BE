package DiffLens.back_end.global.logger.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SubServerExecutionTime {
    String value() default "메서드 실행 시간";
}
