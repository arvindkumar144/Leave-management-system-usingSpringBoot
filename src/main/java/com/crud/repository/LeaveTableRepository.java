package com.crud.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crud.entities.LeaveTable;

public interface LeaveTableRepository extends JpaRepository<LeaveTable, Integer>{

	public boolean existsByStdEmail(String email);
	
	public LeaveTable findByStdId(int id);
	
	public LeaveTable findByStdIdAndApplicationStatus(int id,String applicationStatus);
	
	public ArrayList<LeaveTable> findByStdEmail(String email);
	
	public ArrayList<LeaveTable> findByStdEmailAndApplicationStatus(String email,String applicationStatus);
	
	@Query("select s from LeaveTable s where s.applicationStatus=:status")
	public ArrayList<LeaveTable> getLeaveTableByApplicationStatus(@Param("status") String status);
}
