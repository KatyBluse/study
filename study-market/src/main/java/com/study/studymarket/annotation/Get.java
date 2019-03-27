package com.study.studymarket.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
@RequestMapping(
        method = RequestMethod.GET
)
//属性传递的时候，必须带上目标注解在当前注解上，比如这个@RequestMapping。并且annotation要写上目标注解的.class名称
public @interface Get {
    String name() default "";

    @AliasFor(value = "path", annotation = RequestMapping.class)
    String[] sh() default {};

    @AliasFor(value = "value", annotation = RequestMapping.class)
    String[] val() default {};

    @AliasFor(attribute = "consumes", annotation = RequestMapping.class)
    String[] val2() default {};

    String[] params() default {};

    String[] headers() default {};

    @AliasFor(attribute = "consumes", annotation = RequestMapping.class)
    String[] cs() default {};

    String[] produces() default {};
}
