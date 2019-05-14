package com.study.studymarket.common.util.elasticSearch.annotations;

import com.study.studymarket.common.util.elasticSearch.enums.Container;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface TermQuery {

    Container container();

    String column();
}
