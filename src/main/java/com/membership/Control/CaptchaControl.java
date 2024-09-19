package com.membership.Control;

import com.membership.Service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CaptchaControl {
    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/member/signIn")
    public String submitForm(@RequestParam("g-recaptcha-response") String captchaResponse,
                             @RequestParam("userId") String userId,
                             @RequestParam("password") String password,
                             Model model) {
        boolean isCaptchaValid = captchaService.validateCaptcha(captchaResponse);
        if (!isCaptchaValid) {
            model.addAttribute("loginFailMsg", "Invalid Captcha");
            return "login";
        }
        // 로그인 처리 로직
        return "redirect:/home";
    }
}
