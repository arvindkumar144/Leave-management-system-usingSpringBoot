package com.crud.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.crud.entities.LeaveTable;
import com.crud.entities.Student;
import com.crud.repository.LeaveTableRepository;
import com.crud.repository.StudentRepository;
import com.crud.service.LeaveTableServiceImpl;
import com.crud.service.StudentService;

import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private LeaveTableServiceImpl leaveTableServiceImpl;
	
	@Autowired
	private LeaveTableRepository leaveTableRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@ModelAttribute
	public void getDetails(Model m, Principal principal) {
		String username = principal.getName();
		Student student = this.studentRepository.findByEmail(username);
		m.addAttribute("student", student);
	}

	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Student Home Page - Rajshree Gate Pass");
		return "student/home";
	}

	@GetMapping("/viewProfile")
	public String viewProfile(Model m) {
		m.addAttribute("title", "Student View Profile - Rajshree Gate Pass");
		return "student/viewProfile";
	}

	@GetMapping("/editProfile")
	public String editProfile(Model m, Principal principal) {
		m.addAttribute("title", "Student Edit Profile - Rajshree Gate Pass");
		return "student/editProfile";
	}

	@PostMapping("/editStudentProfile")
	public String updateStudent(@ModelAttribute Student student, @RequestParam("profile") MultipartFile file)
			throws IOException {
		if (file.isEmpty()) {
			student.setProfilePic("default.png");
		} else {
			student.setProfilePic(file.getOriginalFilename());

			File file2 = new ClassPathResource("static/img").getFile();

			Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}

		this.studentRepository.save(student);

		return "redirect:/student/viewProfile";
	}

	@GetMapping("/changePassword")
	public String changePassword(Model m) {
		m.addAttribute("title", "Change Login Password");
		return "student/passwordChange";
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
			return "redirect:/student/";
			
		}else {
			session.setAttribute("message",
					new com.crud.helper.Message("Your OldPassword doesn't Match. Try Again", "alert-danger"));
			return "redirect:/student/changePassword";
		}

	}
	
	@GetMapping("/application")
	public String applicationPage(Model m)
	{
		m.addAttribute("title", "Application Page - Rajshree Gate Pass");
		return "student/applicationLeave";
	}
	
	@PostMapping("/submitApplication")
	public String submitApplication(@ModelAttribute LeaveTable leaveTable,@RequestParam("application") MultipartFile file,
			Principal principal,Model m,HttpSession session) throws IOException
	{
		
		ArrayList<LeaveTable> table = this.leaveTableRepository.findByStdEmailAndApplicationStatus(principal.getName(), "Pending");
		
		System.out.println(table);
		
		if(table.size()!=0)
		{
			session.setAttribute("message", new com.crud.helper.Message("Your Applied Application is in Pending. Wait Util for Rejected/Confirm ", "alert-danger"));
			return "redirect:/student/viewPendingRequests";
		}
		else
		{
			
			ArrayList<LeaveTable> status = this.leaveTableRepository.findByStdEmailAndApplicationStatus(principal.getName(), "Confirm");
			
			if(status.size()!=0)
			{
				session.setAttribute("message", new com.crud.helper.Message("Your Applied Application is Confirm. You Don't able to Apply Again.", "alert-danger"));
				return "redirect:/student/viewConfirmStatus";
			}
			else
			{
				Student student = this.studentRepository.findByEmail(principal.getName());
				leaveTable.setApplicationImage(file.getOriginalFilename());
				
				
				File file2 = new ClassPathResource("static/img").getFile();
				
				Path path = Paths.get(file2.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				this.leaveTableServiceImpl.SaveTable(leaveTable, student);
				m.addAttribute("leaveTable", leaveTable);
				session.setAttribute("message", new com.crud.helper.Message("Your Application Has been Submitted Successfully.", "alert-success"));
			}
			
		}
		
		m.addAttribute("title", "View Application Status - Rajshree Gate Pass");
		return "redirect:/student/viewPendingRequests";
	}
	
	
	@GetMapping("/viewPendingRequests")
	public String viewPendingPage(Model m,Principal principal)
	{
		ArrayList<LeaveTable> leaveTable = this.leaveTableRepository.findByStdEmailAndApplicationStatus(principal.getName(), "Pending");
		
		m.addAttribute("leaveTable", leaveTable);
		m.addAttribute("title", "View Pending Status - Rajshree Gate Pass");
		return "student/viewStatus";
	}
	
	@GetMapping("/viewConfirmStatus")
	public String viewConfirmPage(Model m,Principal principal)
	{
		ArrayList<LeaveTable> leaveTable = this.leaveTableRepository.findByStdEmailAndApplicationStatus(principal.getName(), "Confirm");
		
		m.addAttribute("leaveTable", leaveTable);
		m.addAttribute("title", "View Confirm Status - Rajshree Gate Pass");
		return "student/viewConfirm";
	}
	
	
	@GetMapping("/viewRejectedStatus")
	public String viewRejectedPage(Model m,Principal principal)
	{
		ArrayList<LeaveTable> leaveTable = this.leaveTableRepository.findByStdEmailAndApplicationStatus(principal.getName(), "Rejected");
		
		m.addAttribute("leaveTable", leaveTable);
		m.addAttribute("title", "View Rejected Status - Rajshree Gate Pass");
		return "student/viewRejected";
	}
	
	
	@GetMapping("/viewHistory")
	public String history(Model m,Principal principal)
	{
		ArrayList<LeaveTable> leaveTable = this.leaveTableRepository.findByStdEmailAndApplicationStatus(principal.getName(), "Out from College");
		m.addAttribute("leaveTable", leaveTable);
		m.addAttribute("title", "History Application Status - Rajshree Gate Pass");
		return "student/applicationHistory";
	}

}
