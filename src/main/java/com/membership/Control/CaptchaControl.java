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
    public String signIn(@RequestParam("userId") String userId,
                         @RequestParam("password") String password,
                         @RequestParam("g-recaptcha-response") String recaptchaResponse,
                         Model model) {
        boolean isCaptchaValid = captchaService.verifyCaptcha(recaptchaResponse);
        if (!isCaptchaValid) {
            model.addAttribute("loginFailMsg", "reCAPTCHA 검증에 실패했습니다.");
            return "login";
        }

        // 로그인 로직 추가
        // ...

        return "redirect:/home";
    }
}
