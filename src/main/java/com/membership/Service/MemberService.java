package com.membership.Service;

import com.membership.Dto.MemberForm;
import com.membership.Dto.PwChange;
import com.membership.Dto.UserInfo;
import com.membership.Entity.Member;
import com.membership.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;


    //회원 가입폼의 내용을 데이터 베이스에 저장
    public void saveMember(MemberForm memberForm, PasswordEncoder passwordEncoder) {
        Member member = memberForm.createEntity(passwordEncoder);
        // 아이디와 이메일 중복여부
        validUserIdEmail(member);
        memberRepository.save(member);
    }

    private void validUserIdEmail(Member member) {
        Member find = memberRepository.findByUserId(member.getUserId());
        if (find != null) {
            throw new IllegalStateException("이미 가입된 아이디 입니다.");
        }
        find = memberRepository.findByEmail(member.getEmail());
        if (find != null) {
            throw new IllegalArgumentException("이미 가입된 이메일 입니다.");
        }
    }

    public UserInfo getUserInfo(String userName) {
        Member member = memberRepository.findByUserId(userName);
        return UserInfo.of(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인시 입력한 아이디로 계정 조회 하여 해당 계정 비밀번호 비교를 위해 반환
        Member member = memberRepository.findByUserId(username);
        return User.builder()
                .username(member.getUserId())
                .password(member.getPassword())
                .roles(member.getRole().toString()).build();
    }


    public void changePassword(PwChange pwChange) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 사용자의 userId를 통해 Member 엔티티를 조회
        Member member = memberRepository.findByUserId(pwChange.getUserId());
        if (member == null) {
            throw new IllegalArgumentException("회원 정보가 없습니다.");
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(pwChange.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 새로운 비밀번호와 확인 비밀번호 일치 확인
        if (!pwChange.getNewPassword().equals(pwChange.getConfirmNewPassword())) {
            throw new IllegalArgumentException("새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 비밀번호 업데이트
        member.updatePassword(passwordEncoder.encode(pwChange.getNewPassword()));
        memberRepository.save(member);
    }
}