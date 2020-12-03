package com.perscholas.recipeApp.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.perscholas.recipeApp.dao.UserRepoIF;
import com.perscholas.recipeApp.models.User;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepoIF userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findByUsername(username);
		System.out.println(user.get().getUsername() + "in MyUserDetailsService");
		System.out.println(user.get().getPassword() + "in MyUserDetailsService");
		user.orElseThrow(() -> new UsernameNotFoundException("Not Found: " + username));
		return user.map(MyUserDetails::new).get();
	}

}
