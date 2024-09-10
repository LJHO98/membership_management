package com.membership.Entity;

import com.membership.Constant.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="member_id")
    private Long id;

    @Column(unique = true)
    private String userId;
    private String password;
    private String name;

    @Column(unique = true)
    private String email;
    private String addr1;
    private String addr2;
    private int zipCode;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void updatePassword(String encodePw) {
        this.password = encodePw;
    }
}
