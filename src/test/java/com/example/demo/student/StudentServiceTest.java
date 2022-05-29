package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentService underTest;


    @BeforeEach
    void setUp() {

        underTest = new StudentService(studentRepository);
    }


    @Test
    void canGetAllStudents() {
     //when
        underTest.getAllStudents();

        //then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        //given
        Student student = new Student(
                "SIlA",
                "silasm@gmail.com",
                Gender.MALE
        );
        //when
        underTest.addStudent(student);


        //then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);


        verify(studentRepository)
                .save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);

    }

    @Test
    void willThrowWhenEmailIsTaken() {
        //given
        Student student = new Student(
                "SIlA",
                "silasm@gmail.com",
                Gender.MALE
        );

        given(studentRepository.selectExistsEmail(anyString()))
                .willReturn(true);
        //when
        //then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");

        verify(studentRepository,never()).save(any());


    }

    @Test
    void willThrowWhenStudentDontExist() {
        //given
        long studentId = 1L;
        Student student = new Student(
                studentId,
                "SIlA",
                "silasm@gmail.com",
                Gender.MALE
        );

        given(studentRepository.existsById(studentId))
                .willReturn(false);
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteStudent(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exists");

        verify(studentRepository,never()).deleteById(any());


    }

    @Test
    void canDeleteStudent() {
        //given
        long studentId = 1;
        Student student = new Student(
                studentId,
                "SIlA",
                "silasm@gmail.com",
                Gender.MALE
        );
        //when
        given(studentRepository.existsById(studentId))
                .willReturn(true);
        underTest.deleteStudent(studentId);


        //then
        verify(studentRepository).deleteById(studentId);


    }
}