package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.cst438.controller.StudentController;
import com.cst438.domain.Enrollment;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;

/* 
 * Junit and Mockito to use mock object to test student API. This
 * was modeled after provided example, JunitTestSchedule.
 */

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {

	static final String URL = "http://localhost:8080";
	public static final String TEST_STUDENT_EMAIL = "tanji@csumb.edu";
	public static final String TEST_STUDENT_NAME = "Tanjiro";

	@MockBean
	StudentRepository studentRepository;

	@Autowired
	private MockMvc mvc;
	
	//Test add new student
	@Test
	public void addStudent() throws Exception {
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		
		//post request using email and name parameters to create student
		response = mvc.perform(MockMvcRequestBuilders
				.post("/student?name=" + TEST_STUDENT_NAME + "&email=" + TEST_STUDENT_EMAIL)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		//verify return 
		assertEquals(200, response.getStatus());
		
		boolean found = false;
		
		Student confirmStudent = fromJsonString(response.getContentAsString(), Student.class);
		//check returned data for added course
		if((confirmStudent.getEmail().equals(TEST_STUDENT_EMAIL)) &&
				(confirmStudent.getName().equals(TEST_STUDENT_NAME))) {
			found = true;
		}
		
		assertTrue("Student added.", found);
		
		//Check save  method was called
		verify(studentRepository).save(any(Student.class));
		verify(studentRepository, times(1)).findByEmail(TEST_STUDENT_EMAIL);
	}
	//Test placing hold
	@Test
	public void placeHold()  throws Exception {
		
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		
		//post request using email and name parameters to create student
		response = mvc.perform(MockMvcRequestBuilders
				.post("/student?name=" + TEST_STUDENT_NAME + "&email=" + TEST_STUDENT_EMAIL)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		//verify return 
		assertEquals(200, response.getStatus());
		
		boolean found = false;
		
		Student confirmStudent = fromJsonString(response.getContentAsString(), Student.class);
		//check returned data for added course
		if((confirmStudent.getEmail().equals(TEST_STUDENT_EMAIL)) &&
				(confirmStudent.getName().equals(TEST_STUDENT_NAME))) {
			found = true;
		}
		
		assertTrue("Student added.", found);
		
		verify(studentRepository).save(any(Student.class));
		
		//post request with email parameter to place hold
		response = mvc.perform(MockMvcRequestBuilders
				.post("/student/placehold?email=" + TEST_STUDENT_EMAIL)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200,response.getStatus());
		
		Boolean addHold = false;
		
		confirmStudent = fromJsonString(response.getContentAsString(),Student.class);
		//check returned data contains added course
		if(confirmStudent.getStatusCode() == 1) {
			addHold = true;
		}
		
		assertTrue("Student Registration Held.", addHold);
		
		verify(studentRepository, times(3)).findByEmail(TEST_STUDENT_EMAIL);
	}
	//Test removing hold
	@Test
	public void removeHold()  throws Exception {
		
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		
		//post request using email and name parameters to create student
		response = mvc.perform(MockMvcRequestBuilders
				.post("/student?name=" + TEST_STUDENT_NAME + "&email=" + TEST_STUDENT_EMAIL)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		//verify return 
		assertEquals(200, response.getStatus());
		
		boolean found = false;
		
		Student confirmStudent = fromJsonString(response.getContentAsString(), Student.class);
		//check returned data for added course
		if((confirmStudent.getEmail().equals(TEST_STUDENT_EMAIL)) &&
				(confirmStudent.getName().equals(TEST_STUDENT_NAME))) {
			found = true;
		}
		
		assertTrue("Student added.", found);
		
		verify(studentRepository).save(any(Student.class));
		
		//post request with email parameter to place hold
		response = mvc.perform(MockMvcRequestBuilders
				.post("/student/removehold?email=" + TEST_STUDENT_EMAIL)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200,response.getStatus());
		
		Boolean holdRemove = false;
		
		confirmStudent = fromJsonString(response.getContentAsString(),Student.class);
		//check returned data contains added course
		if(confirmStudent.getStatusCode() == 1) {
			holdRemove = true;
		}
		
		assertTrue("Student Registration Hold Removed.", holdRemove);
		
		verify(studentRepository, times(3)).findByEmail(TEST_STUDENT_EMAIL);
	}
	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
