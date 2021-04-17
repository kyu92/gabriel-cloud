package com.kyu.gabriel.core.model.po.book;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@TableName("book_list")
public class Book extends Model<Book> {

    private static final long serialVersionUID = 8697132794035806450L;
    @TableId(value = "uuid", type = IdType.ASSIGN_UUID)
    @NonNull
    private String uuid;
    @NonNull
    private String userUid;
    private String name;
    private String cover;
    @NonNull
    private String suffix;
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastReadDate;
    private String progress;
}
