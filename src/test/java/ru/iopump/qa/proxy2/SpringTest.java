package ru.iopump.qa.proxy2;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SpringTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testJpa() {
        // given
        Employee alex = new Employee();
        alex.setName("Max");
        alex = entityManager.persist(alex);
        entityManager.flush();

        assertThat(Proxy.isProxyClass(employeeRepository.getClass())).isTrue();

        // when
        Employee found = employeeRepository.findByName(alex.getName());

        // then
        assertThat(found.getName())
            .isEqualTo(alex.getName());
    }

    @SpringBootApplication
    public static class TestApplication {}

    @Entity
    @Table(name = "person")
    @NoArgsConstructor
    @Data
    public static class Employee {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
    }
}
