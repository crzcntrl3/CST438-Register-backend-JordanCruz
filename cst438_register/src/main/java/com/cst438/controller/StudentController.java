package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
	@Autowired
	StudentRepository studentRepository;
	
	// Add student
	@PostMapping("/student")
	@Transactional
	public Student addStudent(@RequestParam("name")String name,@RequestParam("email")String email) {
		//Check if student previously exists
		Student student = null;
		
		if(student == null) {
			student = new Student();
			student.setEmail(email);
			student.setName(name);
			
			studentRepository.save(student);
			
			student = studentRepository.findByEmail(email);
			
			if(student != null) {	
				return student;
			}
			else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unable to add student.");
			}
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Student exists.");
		}
	}
	//Place hold
	@PostMapping("/student/placeHold")
	@Transactional
	public Student placeHold(@RequestParam("email")String email) {
		//Check for existing students
		Student student = studentRepository.findByEmail(email);
		
		if(student != null) {
			//Place hold
			student.setStatusCode(1);
			student.setStatus("On Hold");
			
			student = studentRepository.findByEmail(email);
			if(student.getStatusCode() == 1) {
				return student;
			}
			else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unable to add hold.");
			}
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Student does not exist.");
		}
	}
	//Remove hold
	@PostMapping("/student/removeHold")
	@Transactional
	public Student removeHold(@RequestParam("email")String email) {
		//Check for existing students
		Student student = studentRepository.findByEmail(email);
		
		if(student != null) {
			//Remove hold placed
			student.setStatusCode(0);
			student.setStatus(null);
			
			student = studentRepository.findByEmail(email);
			
			if(student.getStatusCode() == 0) {
				return student;
			}
			else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unable to remove hold.");
			}
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Student does not exist.");
		}
	}
}
