package com.study.studymarket.common.util.elasticSearch.annotations;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface GroupBy {

}
