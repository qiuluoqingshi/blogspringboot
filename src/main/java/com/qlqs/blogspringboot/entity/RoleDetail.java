package com.qlqs.blogspringboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RoleDetail implements Serializable {
    private Long roleId;
    private String roleName;
    private String roleType;
    private String roleRequest;

}
