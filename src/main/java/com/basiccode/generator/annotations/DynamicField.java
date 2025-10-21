package com.basiccode.generator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicField {
    String condition() default "";
    String[] dependsOn() default {};
    boolean generated() default true;
    String version() default "1.0";
}