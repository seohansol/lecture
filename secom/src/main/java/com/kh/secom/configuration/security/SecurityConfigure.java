package com.kh.secom.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigure {
	
	@Bean // Bean 애노테이션을 이용해서 빈으로 등록하는 경우 동일한 이름의 메소드가 존재해서는 안됨
	public SecurityFilterChain securityFiltarChain(HttpSecurity httpSecurity) throws Exception {
		/*
		return httpSecurity
				.formLogin().disable()
				.build();
		// HttpSecurity 메서드안에 formlogin.disable() <로그인기능안해!의미>
			7.0 버전에서 없어질 문법
		 */
		/*
		return httpSecurity
				.formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
					@Override
					public void customize(FormLoginConfigurer<HttpSecurity> formLogin) {
						formLogin.disable();
					}
				}).httpBasic(null).csrf(null).cors(null).build();
		 근본 작성방법
	*/
	return httpSecurity
			.formLogin(AbstractHttpConfigurer::disable) // form 로그인 방식은 사용하지 않겠다
			.httpBasic(AbstractHttpConfigurer::disable) // httpBasic 사용하지 않겠다
			.csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
			/*
			 * csrf 란?
			 * 
			 * XsS 방지를 위한것 하지만 앞단에서 미리 검증을 하기에 필요없음!
			 * 
			 * <form>
			 * 	<input type="hidden" value="1">
			 * 
			 * </form>
			 * 
			 */
			.cors(AbstractHttpConfigurer::disable) // 얘는 일단 꺼놓고 나중에 nginx붙이기
			.build();
	}
	
	@Bean // 암호문 시큐리티로 생성하는 방법
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean //인증할때 필요한 담당 메서드
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
		
	}
}
