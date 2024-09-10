package com.membership.Dto;

import com.membership.Entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private Long id;
    private String userId;
    private String email;
    private String password;
    private String name;
    private String addr1; // 주소
    private String addr2; // 상세주소
    private int zipCode;  // 우편번호

    public static UserInfo of(Member member){
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
