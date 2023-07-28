package com.crud.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LeaveTable 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int stdId;
	private String stdName;
	private String stdEmail;
	private String stdPhone;
	private String stdCourse;
	private LocalDate localdate;
	private LocalTime localTime;
	private LocalTime ExitTime;
	private int otp;
	private String subject;
	@Column(length = 2000)
	private String content;
	private String applicationStatus;
	private String applicationImage;
	public LeaveTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public LeaveTable(int id, int stdId, String stdName, String stdEmail, String stdPhone, String stdCourse,
			LocalDate localdate, LocalTime localTime, LocalTime exitTime, int otp, String subject, String content,
			String applicationStatus, String applicationImage) {
		super();
		this.id = id;
		this.stdId = stdId;
		this.stdName = stdName;
		this.stdEmail = stdEmail;
		this.stdPhone = stdPhone;
		this.stdCourse = stdCourse;
		this.localdate = localdate;
		this.localTime = localTime;
		ExitTime = exitTime;
		this.otp = otp;
		this.subject = subject;
		this.content = content;
		this.applicationStatus = applicationStatus;
		this.applicationImage = applicationImage;
	}
	
	public int getOtp() {
		return otp;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalTime getExitTime() {
		return ExitTime;
	}
	public void setExitTime(LocalTime exitTime) {
		ExitTime = exitTime;
	}
	public LocalTime getLocalTime() {
		return localTime;
	}
	public void setLocalTime(LocalTime localTime) {
		this.localTime = localTime;
	}
	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStdId() {
		return stdId;
	}
	public String getStdName() {
		return stdName;
	}
	public String getStdEmail() {
		return stdEmail;
	}
	public String getStdPhone() {
		return stdPhone;
	}
	public String getStdCourse() {
		return stdCourse;
	}
	
	public String getApplicationStatus() {
		return applicationStatus;
	}
	public String getApplicationImage() {
		return applicationImage;
	}
	public void setStdId(int stdId) {
		this.stdId = stdId;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public void setStdEmail(String stdEmail) {
		this.stdEmail = stdEmail;
	}
	public void setStdPhone(String stdPhone) {
		this.stdPhone = stdPhone;
	}
	public void setStdCourse(String stdCourse) {
		this.stdCourse = stdCourse;
	}
	
	public LocalDate getLocaldate() {
		return localdate;
	}

	public void setLocaldate(LocalDate localdate) {
		this.localdate = localdate;
	}
	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public void setApplicationImage(String applicationImage) {
		this.applicationImage = applicationImage;
	}



	@Override
	public String toString() {
		return "LeaveTable [id=" + id + ", stdId=" + stdId + ", stdName=" + stdName + ", stdEmail=" + stdEmail
				+ ", stdPhone=" + stdPhone + ", stdCourse=" + stdCourse + ", localdate=" + localdate + ", localTime="
				+ localTime + ", ExitTime=" + ExitTime + ", otp=" + otp + ", subject=" + subject + ", content="
				+ content + ", applicationStatus=" + applicationStatus + ", applicationImage=" + applicationImage + "]";
	}


}
