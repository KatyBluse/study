package com.study.studymarket.common.util.elasticSearch.annotations;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Document {

    String index();

    String type();
}
