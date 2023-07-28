package com.crud.service;

import com.crud.entities.LeaveTable;
import com.crud.entities.Student;

public interface LeaveTableService 
{
	public LeaveTable SaveTable(LeaveTable leaveTable,Student student);
}
