package com.kyu.gabriel.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.File;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MailBean {

    private String subject; // 标题
    private String content; // 正文
    private String toAccount; // 邮件接收方
    private File attachmentFile; // 附件
    private String attachmentFileName; // 附件名
}
