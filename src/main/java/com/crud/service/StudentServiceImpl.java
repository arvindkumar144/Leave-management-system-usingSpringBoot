package com.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.entities.Student;
import com.crud.repository.StudentRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class StudentServiceImpl implements StudentService
{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public Student SaveStudent(Student student)
	{
		student.setPassword(this.passwordEncoder.encode(student.getPassword()));
		student.setProfilePic("default.png");
		student.setRole("ROLE_STUDENT");
		Student student2 = this.studentRepository.save(student);
		return student2;
	}
	
	public boolean checkEmail(String email) {
		return this.studentRepository.existsByEmail(email);
	}

	public void sendVerificationMail(Student student, String url) {
		String from="chakravartisumit11@gmail.com";
		String to=student.getEmail();
		String subject="Account Verification for Gate Pass";
		String content="Dear [[name]], <br>"
				+"Please Click the link below to verify Your Registration : <br>"
				+"<h3><a href=\"[[URL]]\" target=\"_self\">Verify</a></h3>"
				+"Thank You for Registration.<br>"
				+"Rajshree Institute Of Management And Technology";
				
		try {
			
			MimeMessage message=javaMailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message);
			
			helper.setFrom(from, "Rajshree");
			helper.setTo(to);
			helper.setSubject(subject);
			
			content=content.replace("[[name]]", student.getName());
			String siteurl=url+"/verify?code"+student.getVerificationCode();

			content=content.replace("[[URL]]", siteurl);
			
			helper.setText(content, true);
			
			javaMailSender.send(message);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
