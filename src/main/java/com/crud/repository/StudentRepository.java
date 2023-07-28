package com.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crud.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Integer>
{
	public boolean existsByEmail(String email);
	
	public Student findByEmail(String email);
	
	@Query("select s from Student s where s.email=:email")
	public Student getStudentByStudentName(@Param("email") String email);
}
