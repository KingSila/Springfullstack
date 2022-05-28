package com.example.demo.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
   private StudentRepository underTest;


    @Test
    void itShouldCheckIfStudentExistsEmail() {

        //given
        String email = "silasmokone@gmail.com";
        Student student = new Student(
                "Silas",
                email,
                Gender.MALE
        );
        underTest.save(student);

        //when
            boolean exists = underTest.selectExistsEmail(email);

        ///then
        assertThat(exists).isTrue();
    }
}