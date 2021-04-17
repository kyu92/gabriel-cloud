package com.kyu.gabriel.core.model.po.user;

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
@RequiredArgsConstructor
@Accessors(chain = true)
@TableName("user_data")
public class User extends Model<User> implements Serializable {

    public User(String uuid, String username, String password, String email, String mobile, Date registerDate){
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
        this.registerDate = registerDate;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 4953388822907176946L;

    @TableId(value = "uuid")
    @NonNull
    private String uuid;
    @NonNull
    private String username;
    @NonNull
    private String password;
    private String email;
    private String mobile;
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerDate;
    private String headPic;
    private String description;
    private Integer permission;
    private boolean gender;
    private Boolean getCover;
    private Boolean hitokoto;
    private boolean emailBind;
    private String nick;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginDate;
}
