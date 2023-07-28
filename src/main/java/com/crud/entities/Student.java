package com.crud.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Student
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	@Column(unique = true)
	private String email;
	private String password;
	private String phone;
	private String profilePic;
	private String course;
	@Column(length = 2000)
	private String address;
	private String role;
	private boolean enable;
	private boolean accountNotLocked;
	private String verificationCode;
	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Student(int id, String name, String email, String password, String phone, String profilePic, String course,
			String address, String role, boolean enable, boolean accountNotLocked,
			String verificationCode) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.profilePic = profilePic;
		this.course = course;
		this.address = address;
		this.role = role;
		this.enable = enable;
		this.accountNotLocked = accountNotLocked;
		this.verificationCode = verificationCode;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getPhone() {
		return phone;
	}
	public String getProfilePic() {
		return profilePic;
	}
	public String getCourse() {
		return course;
	}
	public String getAddress() {
		return address;
	}
	public boolean isEnable() {
		return enable;
	}
	public boolean isAccountNotLocked() {
		return accountNotLocked;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public void setAccountNotLocked(boolean accountNotLocked) {
		this.accountNotLocked = accountNotLocked;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", phone="
				+ phone + ", profilePic=" + profilePic + ", course=" + course + ", address=" + address 
				 + ", role=" + role +  ", enable=" + enable
				+ ", accountNotLocked=" + accountNotLocked + ", verificationCode=" + verificationCode + "]";
	}

	
	
}
