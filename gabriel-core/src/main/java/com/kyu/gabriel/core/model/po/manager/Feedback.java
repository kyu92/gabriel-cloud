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
@TableName("sys_feedback")
public class Feedback extends Model<Feedback> implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 3178035710367926708L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NonNull
    private String title;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date putDate;
    @NonNull
    private String information;
    @NonNull
    private int contactType;
    private int status;
}
