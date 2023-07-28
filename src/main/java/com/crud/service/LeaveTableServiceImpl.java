package com.crud.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.entities.LeaveTable;
import com.crud.entities.Student;
import com.crud.repository.LeaveTableRepository;
@Service
public class LeaveTableServiceImpl implements LeaveTableService{

	@Autowired
	private LeaveTableRepository leaveTableRepository;
	
	@Override
	public LeaveTable SaveTable(LeaveTable leaveTable,Student student) {
		leaveTable.setStdId(student.getId());
		leaveTable.setStdName(student.getName());
		leaveTable.setStdEmail(student.getEmail());
		leaveTable.setStdPhone(student.getPhone());
		leaveTable.setStdCourse(student.getCourse());
		leaveTable.setLocaldate(LocalDate.now());
		leaveTable.setLocalTime(LocalTime.now());
		leaveTable.setApplicationStatus("Pending");
		LeaveTable leaveTable2 = this.leaveTableRepository.save(leaveTable);
		return leaveTable2;
	}

}
