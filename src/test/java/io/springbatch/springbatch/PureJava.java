package io.springbatch.springbatch;

import lombok.Builder;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PureJava {

    @Test
    void test1(){
        Member.builder().name("eogh").age(33).build();

    }

    @Test
    void test2(){
        String scope = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/calendar openid";

        String[] s1 = scope.split(" ");

        System.out.println("s1 = " + s1);
    }



    @Builder
    static class Member{
        private String name;

        private int age;

        public Member(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    void dateConvert(){
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime zonedDateTime = now.withZoneSameInstant(ZoneOffset.of("+09:00"));

    }



}
