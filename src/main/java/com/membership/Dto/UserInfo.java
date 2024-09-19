package com.membership.Dto;

import com.membership.Entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserInfo {
    private String userId;

    @NotBlank(message = "이메일을 작성해주세요")
    private String email;

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotBlank(message = "주소를 입력해주세요.")
    private String addr1; // 주소

    @NotBlank(message = "상세주소를 입력해주세요.")
    private String addr2; // 상세주소

    private int zipCode;  // 우편번호

    public static UserInfo of(Member member) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(member.getName());
        userInfo.setEmail(member.getEmail());
        userInfo.setAddr1(member.getAddr1());
        userInfo.setAddr2(member.getAddr2());
        userInfo.setZipCode(member.getZipCode());
        userInfo.setUserId(member.getUserId());
        return userInfo;
    }


}
