package com.membership.Service;

import com.membership.Dto.MailDto;
import com.membership.Entity.Member;
import com.membership.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindPwService {

    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;

    // 임시 비밀번호 생성
    public static String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    // 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경
    public MailDto createMailAndChangePassword(String email) {
        String str = getTempPassword();
        MailDto dto = new MailDto();
        dto.setAddress(email);
        dto.setTitle("임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + str + " 입니다." + "로그인 후에 비밀번호를 변경해주세요!");
        updatePassword(str, email);
        return dto;
    }

    // MailDto를 바탕으로 실제 이메일 전송
    public void mailSend(MailDto mailDto) {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        message.setFrom("smy2363@gmail.com");
        message.setReplyTo("smy2363@gmail.com");
        System.out.println("message"+message);
        javaMailSender.send(message);
    }

    //임시 비밀번호로 업데이트
    @Transactional
    public boolean updatePassword(String str, String email){
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodePw = encoder.encode(str); // 패스워드 암호화
            Member member = memberRepository.findByEmail(email);
            member.updatePassword(encodePw);
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}