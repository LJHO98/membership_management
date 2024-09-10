package com.membership.Dto;

import com.membership.Constant.Role;
import com.membership.Entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberForm {
    private Long id;
    @NotBlank(message = "아이디는 필수 입니다.")
    private String userId;

    @NotBlank(message = "이메일을 작성해주세요")
    private String email;

    @Size(min=4 , max=12, message="비밀번호는 4~12자리 입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotBlank(message = "주소를 입력해주세요.")
    private String addr1; // 주소

    @NotBlank(message = "상세주소를 입력해주세요.")
    private String addr2; // 상세주소


    private int zipCode;  // 우편번호

    // DTO -> Entity (회원가입시 동작 메서드)
    public Member createEntity(PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(this.name);
        member.setEmail(this.email);
        member.setUserId(this.userId);
        member.setZipCode(this.zipCode);
        member.setAddr1(this.addr1);
        member.setAddr2(this.addr2);
        member.setRole(Role.USER);
        String pw = passwordEncoder.encode(this.password);
        member.setPassword(pw);
        return member;
    }


    // Entity -> DTO
    public static MemberForm of(Member member){
        MemberForm memberForm = new MemberForm();
        memberForm.setName(member.getName());
        memberForm.setEmail(member.getEmail());
        memberForm.setAddr1(member.getAddr1());
        memberForm.setAddr2(member.getAddr2());
        memberForm.setZipCode(member.getZipCode());
        memberForm.setUserId(member.getUserId());
        return memberForm;
    }
}
