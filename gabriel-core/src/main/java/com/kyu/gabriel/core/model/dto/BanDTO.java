package com.kyu.gabriel.core.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kyu.gabriel.core.model.po.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BanDTO {
    private boolean status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
    private String reason;
    private String uuid;
    private User operator;
}
