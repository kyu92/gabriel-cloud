package com.kyu.gabriel.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListFeedbackVo {

    private String keyword;
    private int page;
    private int limit;
}
