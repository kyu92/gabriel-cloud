package com.kyu.gabriel.core.model.po.manager;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sys_log")
public class Log extends Model<Log> implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = -7004937154883598575L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NonNull
    private String title;
    @NonNull
    private String fullName;
    @NonNull
    private String method;
    private String params;
    @NonNull
    private String path;
    private String userAgent;
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date callDate;
    private String callerIp;
    private String callerLocation;
    private Double callerLon;
    private Double callerLat;
    private String callerUuid;
    @NonNull
    private boolean success;
    private String exception;
    private String response;
}
