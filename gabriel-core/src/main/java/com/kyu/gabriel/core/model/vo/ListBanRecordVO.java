package com.kyu.gabriel.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListBanRecordVO {

    private int page;
    private int limit;
    private String username;
    private String uuid;
    private String relieveDateStart;
    private String relieveDateEnd;
    private String reason;
    private String operator;
    private Boolean deleteFlag;
}
