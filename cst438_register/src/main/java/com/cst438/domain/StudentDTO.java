package com.cst438.domain;

public class StudentDTO{
	public int id, student_id, status_code;
	public String email, name, status;
	
	@Override
	public String toString() {
		return "CourseDTO [id=" + id 
				+ ", student_id=" + student_id
				+ ", name=" + name
				+ ", email=" + email 
				+ ", status=" + status + "]";
	}
}