package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

@Configuration    //메모리에 뜨게 하기위해
@EnableWebSecurity    //활성화하기위해        스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled =  true)		//secured 어노테이션 활성화   preAuthorize, postAuthoriz어노테이션활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauthUserService;
	
	//해당 메서드의 리턴되는 오브젝트를 Ioc로 등록해준다.
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()   // 인증만 되면 들어갈 수 있는 주소
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN')or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
		.and()                          //권한이 없는 페이지로 요청이 될 때 로그인페이지로 이동되게 하기
			.formLogin()
			.loginPage("/loginForm")
			.usernameParameter("username")
			.loginProcessingUrl("/login") //  /login이라는 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 처리를 해준다.
			.defaultSuccessUrl("/")    // loginForm에서 로그인을하면 "/"로 가고 어떤 특정 페이지요청해서 로그인을 하게 되면 그 페이지로 돌아간다.			
		.and()
			.oauth2Login()
			.loginPage("/loginForm")	 //구글 로그인이 완료된 뒤의 후처리가 필요하다.
			.userInfoEndpoint()
			.userService(principalOauthUserService);
	}
}
