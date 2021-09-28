package com.cst438.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.EnrollmentDTO;


public class GradebookServiceREST extends GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	String gradebook_url;
	
	public GradebookServiceREST() {
		System.out.println("REST grade book service");
	}
	//Class will send new student enrollment to Gradebook service
	//using HTTP POST and EnrollmentDTO object
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		
		EnrollmentDTO myEnrollDTO = new EnrollmentDTO(student_email, student_name, course_id);
		
		restTemplate.postForEntity("http://localhost:8081/enrollment", myEnrollDTO, EnrollmentDTO.class);
		
	}

}
