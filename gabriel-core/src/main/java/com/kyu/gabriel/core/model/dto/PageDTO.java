package com.kyu.gabriel.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageDTO {
    private int page;
    private int limit;
}
