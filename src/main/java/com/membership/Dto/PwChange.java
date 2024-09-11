package com.membership.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PwChange {

    private String userId;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;



}
