package com.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.crud.entities.Student;
import com.crud.helper.Message;
import com.crud.service.StudentServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private StudentServiceImpl studentService;

	// for index page
	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home Page - Rajshree Gate Pass");
		return "home";
	}

//	for registration page
	@GetMapping("/signup")
	public String reigsterPage(Model m) {
		m.addAttribute("title", "Registration Page - Rajshree Gate Pass");
		m.addAttribute("student", new Student());
		return "register";
	}

//	for login page
	@GetMapping("/signin")
	public String loginPage(Model m) {
		m.addAttribute("title", "Login Page - Rajshree Gate Pass");
		return "login";
	}

//	for register student
	@PostMapping("/registerRecord")
	public String registerStudent(@ModelAttribute Student student, Model m, HttpSession session,HttpServletRequest request) {

//		String url=request.getRequestURI().toString();
		
//		url=url.replace(request.getServletPath(), "");
		
		
		
		boolean f = this.studentService.checkEmail(student.getEmail());

		if (f) {
			session.setAttribute("message",
					new Message("This Email is already Registered. Try With Another One.", "alert-danger"));
			System.out.println("This Email Already exist");
		} else {
			Student student2 = this.studentService.SaveStudent(student);

			if (student2 != null) {
				session.setAttribute("message", new Message("Registration Successfully.", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Something Went Wrong", "alert-danger"));
				m.addAttribute("student", student2);
			}

		}
		return "redirect:/signup";

	}

	
}
