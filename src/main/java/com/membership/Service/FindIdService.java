package com.membership.Service;

import com.membership.Entity.Member;
import com.membership.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor

public class FindIdService {
    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private String senderEmail = "smy2363@gmail.com";


    public MimeMessage createMail(String email) {

        MimeMessage message = javaMailSender.createMimeMessage();
        Member member = memberRepository.findByEmail(email);
        String id = member.getUserId();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("아이디 찾기");
            String body="";
            body += "<h3>" + "해당 이메일로 조회된 아이디는" + "</h3>";
            body += "<h1>" + id + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e){
            e.printStackTrace();
        }
        return message;
    }

    public void sendMail(String email){
        MimeMessage message = createMail(email);
        javaMailSender.send(message);
    }

}
