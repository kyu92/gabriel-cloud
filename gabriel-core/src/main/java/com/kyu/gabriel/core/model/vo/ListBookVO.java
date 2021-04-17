package com.kyu.gabriel.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListBookVO {

    private int page;
    private int limit;
    private String name;
    private String type;
    private String createDateStart;
    private String createDateEnd;
}
