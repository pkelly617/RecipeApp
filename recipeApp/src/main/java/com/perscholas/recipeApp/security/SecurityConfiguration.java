package com.perscholas.recipeApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;
	
	public SecurityConfiguration(MyUserDetailsService userPrincipalDetailsService) {
        this.userDetailsService = userPrincipalDetailsService;
    }

	// method to config user details class
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// PasswordEncoder encoder =
		// PasswordEncoderFactories.createDelegatingPasswordEncoder();

		// auth.inMemoryAuthentication().withUser("admin").password(encoder.encode("admin")).roles("ADMIN");
		
		//auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(daoAuthenticationProvider());

	}

	@Bean
	public AuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(this.userDetailsService);
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// main config method to allow users to which page
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/view_users").hasAuthority("ADMIN").antMatchers("/login", "/register_user")
				.permitAll().antMatchers("/recipe_search", "/ingredient_search", "/new_recipe", "/new_filter", "/user_recipes", "/user_filters", "/view_recipe", "/edit_recipe", "/view_filter", "/edit_filter").hasAnyAuthority("USER", "ADMIN")

				.and()

				.httpBasic()

				.and()

				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")

				.and()

				.exceptionHandling().accessDeniedPage("/login")

				.and()

				.formLogin().loginPage("/login").loginProcessingUrl("/login/authenticate").permitAll()
				.failureUrl("/login").defaultSuccessUrl("/user_recipes").usernameParameter("username")
				.passwordParameter("password");

	}

	// pages not to follow the security
	@Override
	public void configure(final WebSecurity web) throws Exception {
		//web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

}
