package com.membership.Control;

import com.membership.Service.MailService;
import com.membership.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MainControl {
    private final MailService mailService;

    @GetMapping("/")
    public String home(){

        return "/index";
    }


}
