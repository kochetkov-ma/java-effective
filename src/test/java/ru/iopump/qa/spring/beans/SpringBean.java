package ru.iopump.qa.spring.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpringBean {

    public final String userName;

    public SpringBean(@Value("${spring.user.variable:unknown}") String userName) {
        this.userName = userName;
    }

    public String call() {
        return "Name from bean : " + userName;
    }
}
