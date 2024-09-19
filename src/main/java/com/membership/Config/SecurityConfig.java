package com.membership.Config;

import com.membership.Service.CaptchaService;
import com.membership.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Autowired
    CaptchaService captchaService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/member/signIn")
                .defaultSuccessUrl("/")
                .usernameParameter("userId")
                .passwordParameter("password")
                .failureUrl("/member/signIn/error")
                .successHandler((request, response, authentication) -> {
                    String recaptchaResponse = request.getParameter("g-recaptcha-response");
                    boolean isCaptchaValid = captchaService.verifyCaptcha(recaptchaResponse);
                    if (!isCaptchaValid) {
                        response.sendRedirect("/member/signIn?error=captcha");
                    } else {
                        response.sendRedirect("/");
                    }
                })
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/");

        // 인가, 인증, 누구든 접근 허용 주소 설정
        http.authorizeRequests()
                .mvcMatchers("/member/userInfo/**").authenticated()
                .mvcMatchers("/member/pwChange").authenticated()
                .mvcMatchers("/", "/member/**", "/mail", "/findId", "/findPw", "/verifyCode", "/check/mail").permitAll()
                .mvcMatchers("/css/**", "/js/**", "/image/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        return http.build();
    }
}

