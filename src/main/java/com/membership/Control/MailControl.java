package com.membership.Control;

import com.membership.Dto.MailDto;
import com.membership.Service.EmailService;
import com.membership.Service.FindIdService;
import com.membership.Service.FindPwService;
import com.membership.Service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
public class MailControl {
    private final MailService mailService;
    private final EmailService emailService;
    private final FindIdService findIdService;
    private final FindPwService findPwService;

    @PostMapping("/findId")
    public @ResponseBody ResponseEntity findId(String email) {
        findIdService.sendMail(email);
        return new ResponseEntity<String>("이메일을 확인하세요", HttpStatus.OK);
    }


    // 비밀번호 찾기시, 임시 비밀번호 담긴 이메일 보내기
    @Transactional
    @PostMapping("/findPw")
    public @ResponseBody ResponseEntity sendEmail(String email) {
        MailDto dto = findPwService.createMailAndChangePassword(email);
        findPwService.mailSend(dto);
        return new ResponseEntity<String>("이메일을 확인하세요", HttpStatus.OK);
    }


    // 인증코드 전송
    @PostMapping("/mail")
    public @ResponseBody ResponseEntity<String> sendEmailPath(String email) throws MessagingException {
        emailService.sendEmail(email);
        return new ResponseEntity<String>("이메일을 확인하세요", HttpStatus.OK);
    }

    // 인증코드 인증
    @PostMapping("/verifyCode")
    public @ResponseBody ResponseEntity<String> verifyCode(@RequestParam("emailCode") String emailCode,
                                                           @RequestParam("email") String email) {
        if (emailService.verifyEmailCode(email, emailCode)) {
            return ResponseEntity.ok("인증 성공");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패: 잘못된 인증 코드입니다.");
    }
}
