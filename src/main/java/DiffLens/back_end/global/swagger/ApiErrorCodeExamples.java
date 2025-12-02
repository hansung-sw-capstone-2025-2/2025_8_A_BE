package DiffLens.back_end.global.swagger;

import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCodeExamples {

    ErrorStatus[] value();

}
