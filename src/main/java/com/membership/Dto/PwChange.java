package com.membership.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
public class PwChange {

    private String userId;
    @NotBlank(message = "(필수)현재 비밀번호를 입력해주세요.")
    private String currentPassword;
    @Size(min=4 , max=12, message="비밀번호는 4~12자리 입니다.")
    private String newPassword;
    private String confirmNewPassword;



}
