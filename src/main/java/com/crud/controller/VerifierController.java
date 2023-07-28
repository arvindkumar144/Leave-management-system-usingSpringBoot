package com.crud.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalTime;
import java.util.ArrayList;

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

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/verifier")
public class VerifierController 
{
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private LeaveTableRepository leaveTableRepository;
	
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
		m.addAttribute("title", "Verifier Home Page - Rajshree Gate Pass");
		return "verifier/home";
	}
	
	@GetMapping("/viewProfile")
	public String viewProfile(Model m)
	{
		m.addAttribute("title", "Verifier View Profile - Rajshree Gate Pass");
		return "verifier/viewProfile";
	}
	
	@GetMapping("/editProfile")
	public String editProfile(Model m,Principal principal)
	{
		m.addAttribute("title", "Verifier Edit Profile - Rajshree Gate Pass");
		return "verifier/editProfile";
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
		
		return "redirect:/verifier/viewProfile";
	}
	
	@GetMapping("/changePassword")
	public String changePassword(Model m) {
		m.addAttribute("title", "Change Login Password");
		return "verifier/passwordChange";
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
			return "redirect:/verifier/";
			
		}else {
			session.setAttribute("message",
					new com.crud.helper.Message("Your OldPassword doesn't Match. Try Again", "alert-danger"));
			return "redirect:/verifier/changePassword";
		}

	}
	
	
	@GetMapping("/pendingApplication")
	public String pendingRequests(Model m)
	{
		ArrayList<LeaveTable> table = this.leaveTableRepository.getLeaveTableByApplicationStatus("Confirm");
		m.addAttribute("leaveTable", table);
		m.addAttribute("title", "Confirm Requests for OTP Verification - Verifier Page");
		return "verifier/pendingAppRequests";
	}
	
	
	@GetMapping("/viewAllApplication")
	public String AlldoneApplications(Model m)
	{
		ArrayList<LeaveTable> table = this.leaveTableRepository.getLeaveTableByApplicationStatus("Out from College");
		m.addAttribute("leaveTable", table);
		m.addAttribute("title", "All Done Applications - Verifier Page");
		return "verifier/viewAllAppApplication";
	}
	
	
	@GetMapping("/VerifyOTP/{id}/{stdId}")
	public String VerifyOTP(Model m,Principal principal,@PathVariable("id") int id,@PathVariable("stdId") int stdId)
	{
		m.addAttribute("stdId",stdId);
		m.addAttribute("id", id);
		m.addAttribute("title", "OTP Verification Page - Verifier");
		return "verifier/verifyWithOTP";
	}
	
	
	@PostMapping("/SubmitOTP")
	public String submitOTP(Model m,@RequestParam("otp") int otp,@RequestParam("stdId") int stdId,@RequestParam("id") int id,HttpSession session)
	{
		LeaveTable leaveTable = this.leaveTableRepository.findById(id).get();
		
		int otp2 = leaveTable.getOtp();
		
		if(otp==otp2)
		{
			leaveTable.setApplicationStatus("Out from College");
			leaveTable.setExitTime(LocalTime.now());
			leaveTable.setOtp(0);
			this.leaveTableRepository.save(leaveTable);
			session.setAttribute("message", new Message("Status Updated Successfully.", "alert-success"));
			return "redirect:/verifier/viewAllApplication";
		}
		else
		{
			session.setAttribute("message", new Message("Your OTP was wrong. Try Again", "alert-danger"));
			return "redirect:/verifier/pendingApplication";
		}
		
		
	}
	
}
