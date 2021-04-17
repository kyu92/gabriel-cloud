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
@TableName("sys_config")
public class Config extends Model<Config> implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 6323799345436768454L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NonNull
    private String configKey;
    private String configValue;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;
    @NonNull
    private boolean disable;
    @NonNull
    private boolean writeable;
}
