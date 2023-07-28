package com.crud.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.crud.entities.LeaveTable;
import com.crud.entities.Student;
import com.crud.helper.Message;
import com.crud.repository.LeaveTableRepository;
import com.crud.repository.StudentRepository;
import com.crud.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController 
{
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
		
	@Autowired
	private LeaveTableRepository leaveTableRepository;
	
	@Autowired
	private EmailService emailService;
	
	@ModelAttribute
	public void getDetails(Model m,Principal principal)
	{
		String username = principal.getName();
		Student student = this.studentRepository.findByEmail(username);
		m.addAttribute("student", student);
	}
	
	@GetMapping("/")
	public String home(Model m)
	{
		m.addAttribute("title", "Admin Home Page - Rajshree Gate Pass");
		return "admin/home";
	}
	
	@GetMapping("/viewProfile")
	public String viewProfile(Model m)
	{
		m.addAttribute("title", "Admin View Profile - Rajshree Gate Pass");
		return "admin/viewProfile";
	}
	
	@GetMapping("/editProfile")
	public String editProfile(Model m,Principal principal)
	{
		m.addAttribute("title", "Admin Edit Profile - Rajshree Gate Pass");
		return "admin/editProfile";
	}
	
	
	@PostMapping("/editStudentProfile")
	public String updateStudent(@ModelAttribute Student student,@RequestParam("profile") MultipartFile file) throws IOException
	{
		if(file.isEmpty())
		{
			student.setProfilePic("default.png");
		}else {
			student.setProfilePic(file.getOriginalFilename());
			
			File file2 = new ClassPathResource("static/img").getFile();
			
			Path path = Paths.get(file2.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}
		
		
		this.studentRepository.save(student);
		
		return "redirect:/admin/viewProfile";
	}
	
	@GetMapping("/changePassword")
	public String changePassword(Model m) {
		m.addAttribute("title", "Change Login Password");
		return "admin/passwordChange";
	}

	@PostMapping("/changeLoginPass")
	public String changePass(@RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass,
			Principal principal, Model m, HttpSession session) {
		Student student = this.studentRepository.findByEmail(principal.getName());
		if (this.bCryptPasswordEncoder.matches(oldPass, student.getPassword())) 
		{
			student.setPassword(this.bCryptPasswordEncoder.encode(newPass));
			this.studentRepository.save(student);
			session.setAttribute("message",
					new com.crud.helper.Message("Password Changed Successfully", "alert-success"));
			return "redirect:/admin/";
			
		}else {
			session.setAttribute("message",
					new com.crud.helper.Message("Your OldPassword doesn't Match. Try Again", "alert-danger"));
			return "redirect:/admin/changePassword";
		}

	}
	
	
	@GetMapping("/pending")
	public String pending(Model m,HttpSession session)
	{
		ArrayList<LeaveTable> table = this.leaveTableRepository.getLeaveTableByApplicationStatus("Pending");
		m.addAttribute("leaveTable", table);
		m.addAttribute("title", "Pending Requests - Admin Page");
		return "admin/pendingRequests";
	}
	@GetMapping("/confirm")
	public String confirm(Model m)
	{
		ArrayList<LeaveTable> table = this.leaveTableRepository.getLeaveTableByApplicationStatus("Confirm");
		m.addAttribute("leaveTable", table);
		m.addAttribute("title", "Confirm Requests - Admin Page");
		return "admin/confirmRequests";
	}
	@GetMapping("/reject")
	public String reject(Model m)
	{
		ArrayList<LeaveTable> table = this.leaveTableRepository.getLeaveTableByApplicationStatus("Rejected");
		m.addAttribute("leaveTable", table);
		m.addAttribute("title", "Rejected Requests - Admin Page");
		return "admin/rejectedRequests";
	}
	
	@GetMapping("/history")
	public String history(Model m)
	{
		ArrayList<LeaveTable> table = this.leaveTableRepository.getLeaveTableByApplicationStatus("Out from College");
		m.addAttribute("leaveTable", table);
		m.addAttribute("title", "Review All Done Application - Admin Page");
		return "admin/historyPage";
	}
	
	
	@GetMapping("/confirmApplication/{id}/{applicationStatus}")
	public String confirmApplication(Model m,HttpSession session,@PathVariable("id") int id,@PathVariable("applicationStatus") String applicationStatus,Principal principal)
	{
		LeaveTable leaveTable = this.leaveTableRepository.findByStdIdAndApplicationStatus(id, applicationStatus);
		int otp = new Random().nextInt(10000);
		String message="Verification OTP : "+otp;
		String subject="OTP for Gate Verification";
		String to=leaveTable.getStdEmail();
		//send the email for verification in gate
		
		this.emailService.sendMail(message, subject, to);
		leaveTable.setApplicationStatus("Confirm");
		leaveTable.setOtp(otp);
		LeaveTable save = this.leaveTableRepository.save(leaveTable);
		
		m.addAttribute("leaveTable", save);
		session.setAttribute("message", new Message("Application Confirm.", "alert-success"));
		return "redirect:/admin/pending";
	}
	
	
	@GetMapping("/rejectApplication/{id}/{applicationStatus}")
	public String rejectApplication(Model m,HttpSession session,@PathVariable("id") int id,@PathVariable("applicationStatus") String applicationStatus)
	{
		LeaveTable leaveTable = this.leaveTableRepository.findByStdIdAndApplicationStatus(id, applicationStatus);
		leaveTable.setApplicationStatus("Rejected");
		LeaveTable save = this.leaveTableRepository.save(leaveTable);
		m.addAttribute("leaveTable", save);
		session.setAttribute("message", new Message("Application Rejected.", "alert-danger"));
		return "redirect:/admin/pending";
	}
	
	
	
	@GetMapping("/viewApplication/{id}/{applicationStatus}")
	public String viewApplication(Model m,HttpSession session,@PathVariable("id") int id,@PathVariable("applicationStatus") String applicationStatus)
	{
		LeaveTable leaveTable = this.leaveTableRepository.findByStdIdAndApplicationStatus(id, applicationStatus);
		m.addAttribute("leaveTable", leaveTable);
		m.addAttribute("title", "View Full Application");
		return "admin/viewApplication";
	}
}
