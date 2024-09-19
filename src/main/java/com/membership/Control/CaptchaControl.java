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

    @PostMapping("/submit")
    public String submitForm(@RequestParam("g-recaptcha-response") String captchaResponse, Model model) {
        boolean isCaptchaValid = captchaService.validateCaptcha(captchaResponse);
        if (!isCaptchaValid) {
            model.addAttribute("error", "Invalid Captcha");
            return "form";
        }
        // 폼 처리 로직
        return "success";
    }
}
