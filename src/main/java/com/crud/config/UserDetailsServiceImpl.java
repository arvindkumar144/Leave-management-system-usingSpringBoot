package com.crud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crud.entities.Student;
import com.crud.repository.StudentRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

	@Autowired
	private StudentRepository studentRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Student student = this.studentRepository.findByEmail(username);
		if(student!=null)
		{
			return new CustomUserDetails(student);
		}else
		{
			throw new UsernameNotFoundException(username+" not available.");			
		}
	}

}
