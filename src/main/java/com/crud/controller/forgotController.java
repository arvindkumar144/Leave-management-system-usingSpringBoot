package com.crud.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crud.entities.Student;
import com.crud.helper.Message;
import com.crud.repository.StudentRepository;
import com.crud.service.EmailService;
import com.crud.service.StudentServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class forgotController 
{

	@Autowired
	private StudentServiceImpl studentService;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@GetMapping("/forgotPassword")
	public String forgotPassword(Model m) {
		m.addAttribute("title", "Reset Password - Rajshree Gate Pass");
		return "forgot_Password_Page";
	}

	@PostMapping("/forgotPass")
	public String forgotPassCheckEmail(@RequestParam("email") String email, HttpSession session, Model m) {
		boolean b = this.studentService.checkEmail(email);
		if (b) {
			int otp = new Random().nextInt(100000);
			String message = "Verification OTP : " + otp;
			String subject = "OTP for Forgot Password";
			String to = email;

			boolean sendMail = this.emailService.sendMail(message, subject, to);

			if (sendMail) {
				session.setAttribute("otp", otp);
				session.setAttribute("email", email);
				session.setAttribute("message", new Message("OTP send on your Registered Email", "alert-success"));

				return "verify_otp";
			} else {
				session.setAttribute("message",
						new Message("Something Went Wrong on Server. Try again...", "alert-danger"));
				return "redirect:/forgotPassword";
			}

		} else {
			session.setAttribute("message",
					new Message("This Email Id is not Registered. Try with Another", "alert-danger"));
			return "redirect:/forgotPassword";
		}

	}

	@PostMapping("/verifyOTP")
	public String verifyOTP(@RequestParam("otp") int otp, @RequestParam("email") String email, HttpSession session,
			Model m) {
		int verifyOTP = (int) session.getAttribute("otp");
		if (otp == verifyOTP) {
			session.setAttribute("email", email);
			m.addAttribute("email", email);
			return "generateNewPass";
		} else {
			session.setAttribute("message", new Message("You Entered wrong OTP..Try Again", "alert-danger"));
			return "redirect:/forgotPassword";
		}
	}

	@PostMapping("/generatePassword")
	public String generatePassword(@RequestParam("email") String email, @RequestParam("newPassword") String newpassword,
			HttpSession session) {
		Student student = this.studentRepository.getStudentByStudentName(email);
		
		System.out.println(student);
		
		student.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		this.studentRepository.save(student);
		session.setAttribute("message", new Message("New Password Generated Successfully", "alert-success"));
		return "redirect:/signin";
	}

}
