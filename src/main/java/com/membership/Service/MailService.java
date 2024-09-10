package com.membership.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "smy2363@gmail.com";
    private static int number;

    public int createNumber(){
        int number = (int)(Math.random() * (90000)) + 100000;
        return number;
    }

    public MimeMessage createMail(String mail) {

        number = createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();


        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body="";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e){
            e.printStackTrace();
        }
        return message;
    }
    public int sendMail(String mail){
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);
        return number;
    }


}
