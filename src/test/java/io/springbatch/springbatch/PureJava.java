package io.springbatch.springbatch;

import lombok.Builder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PureJava {

    @Test
    void test1(){
        Member.builder().name("eogh").age(33).build();

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
}
