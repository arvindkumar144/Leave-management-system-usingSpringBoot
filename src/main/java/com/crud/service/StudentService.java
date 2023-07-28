package com.crud.service;

import com.crud.entities.Student;

public interface StudentService
{
	public Student SaveStudent(Student student);
	
	public boolean checkEmail(String email);
	
}
