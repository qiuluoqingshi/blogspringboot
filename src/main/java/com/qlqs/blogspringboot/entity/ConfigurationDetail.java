package com.qlqs.blogspringboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationDetail implements Serializable {
    private String webByAuthor;
    private String webIcp;
    private String authorContact;
    private String webName;
    private String webDescribe;
    private String webIcon;
    private String authorEmail;
}
