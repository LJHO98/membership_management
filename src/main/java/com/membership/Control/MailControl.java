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

//    @ResponseBody
//    @PostMapping("/mail")
//    public String MailSend(String mail){
//        int number = mailService.sendMail(mail);
//        String num = "" + number;
//        return num;
//    }


    @PostMapping("/findId")
    public @ResponseBody ResponseEntity findId(String email){
        findIdService.sendMail(email);
        return new ResponseEntity<Integer>(0, HttpStatus.OK);
    }


    // 비밀번호 찾기시, 임시 비밀번호 담긴 이메일 보내기
    @Transactional
    @PostMapping("/findPw")
    public @ResponseBody ResponseEntity sendEmail(String email) {
        MailDto dto = findPwService.createMailAndChangePassword(email);
        findPwService.mailSend(dto);

        return new ResponseEntity<Integer>(0, HttpStatus.OK);
    }


    // 수정
    @PostMapping("/mail")
    public @ResponseBody ResponseEntity<String> sendEmailPath(String mail) throws MessagingException {
        emailService.sendEmail(mail);
        return ResponseEntity.ok("이메일을 확인하세요");
    }

//    @PostMapping("/{email_addr}/authcode")
//    public ResponseEntity<String> sendEmailAndCode(@PathVariable String email_addr, @RequestBody EmailRequestDto dto) throws NoSuchAlgorithmException {
//        if (emailService.verifyEmailCode(email_addr, dto.getCode())) {
//            return ResponseEntity.ok(emailService.makeMemberId(email_addr));
//        }
//        return ResponseEntity.notFound().build();
//    }
}
