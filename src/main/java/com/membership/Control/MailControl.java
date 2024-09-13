package com.membership.Control;

import com.membership.Dto.MailDto;
import com.membership.Dto.UserInfo;
import com.membership.Service.*;
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
    private final EmailService emailService;
    private final FindIdService findIdService;
    private final FindPwService findPwService;
    private final MemberService memberService;

    //아이디 찾기
    @PostMapping("/findId")
    public @ResponseBody ResponseEntity findId(String email) {

        try{
            memberService.isExistEmail(email);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        findIdService.sendMail(email);

        return new ResponseEntity<String>("이메일을 확인하세요", HttpStatus.OK);
    }


    // 비밀번호 찾기시, 임시 비밀번호 담긴 이메일 보내기
    @Transactional
    @PostMapping("/findPw")
    public @ResponseBody ResponseEntity sendEmail(String email) {
        MailDto dto = findPwService.createMailAndChangePassword(email);
        findPwService.mailSend(dto);
        return new ResponseEntity<String>("임시비밀번호 발급, 이메일을 확인하세요.", HttpStatus.OK);
    }


    // 인증코드 전송(회원가입 이메일)
    @PostMapping("/mail")
    public @ResponseBody ResponseEntity<String> sendEmailPath(String email) throws MessagingException {
        try{
            memberService.validUserEmail(email);
        }catch(IllegalStateException e1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다. 다른 이메일을 입력해주세요.");
        }
        emailService.sendEmail(email);
        return new ResponseEntity<String>("인증코드 전송, 이메일을 확인하세요", HttpStatus.OK);
    }
    // 인증코드 전송(비밀번호 찾기 이메일)
    @PostMapping("/check/mail")
    public @ResponseBody ResponseEntity<String> checkAndSendEmail(String email) throws MessagingException {
        try{
            memberService.isExistEmail(email);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        emailService.sendEmail(email);
        return new ResponseEntity<String>("인증코드 전송, 이메일을 확인하세요", HttpStatus.OK);
    }
    // 인증코드 전송(유저정보 수정 이메일)
    @PostMapping("/updateCheck/mail")
    public @ResponseBody ResponseEntity updateCheckAndSendEmail(String email, String checkEmail) throws MessagingException{
           return memberService.validEmailOrSendEmail(email, checkEmail);
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
