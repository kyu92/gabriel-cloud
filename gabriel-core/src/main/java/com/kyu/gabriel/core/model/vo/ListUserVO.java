package com.kyu.gabriel.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserVO {

    private int page;
    private int limit;
    private String username;
    private String email;
    private String mobile;
    private String nick;
    private String regDateStart;
    private String regDateEnd;
    private String loginDateStart;
    private String loginDateEnd;
    private Boolean banned;
}
