package com.qlqs.blogspringboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {
    private String userId;
    private String userPassword;
    private int locked;
    private String salt;

}
