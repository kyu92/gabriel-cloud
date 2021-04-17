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

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sys_answer")
public class Answer extends Model<Answer> implements Serializable {

    public Answer(@NonNull String title, @NonNull String answer, String creator) {
        this.title = title;
        this.answer = answer;
        this.creator = creator;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 3786990209048918591L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NonNull
    private String title;
    @NonNull
    private String answer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    private String creator;

}
