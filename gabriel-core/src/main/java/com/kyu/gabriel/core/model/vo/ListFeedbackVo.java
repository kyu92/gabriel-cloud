package com.kyu.gabriel.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListFeedbackVo {

    private String keyword;
    private int page;
    private int limit;
}
