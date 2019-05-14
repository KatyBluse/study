package com.study.studymarket.common.annotation;

@Show(key="1", value = "2")
public class Test {

    public static void main(String[] args) {
        boolean check = Test.class.isAnnotationPresent(Show.class);
        if (check) {
            Show show = Test.class.getAnnotation(Show.class);
            System.out.println(show.key());
            System.out.println(show.value());
        }
    }
}
