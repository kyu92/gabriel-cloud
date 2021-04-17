package com.kyu.gabriel.user.util;

import com.kyu.gabriel.user.entity.MailBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

@Component
@RefreshScope
public class MailUtil {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Autowired
    public MailUtil(JavaMailSender mailSender, MailProperties mailProperties){
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    /**
     * 发送不带附件的邮件
     * @param bean 邮件bean
     */
    public void sendMail(MailBean bean){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername());
        message.setTo(bean.getToAccount());
        message.setSubject(bean.getSubject());
        message.setText(bean.getContent());
        mailSender.send(message);
    }

    public boolean sendMailAttachment(MailBean bean){
        MimeMessage mimeMailMessage = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMailMessage,true,"utf-8");
            messageHelper.setFrom(mailProperties.getUsername());
            messageHelper.setTo(bean.getToAccount());
            messageHelper.setSubject(bean.getSubject());
            messageHelper.setText(bean.getContent(), true);
            mailSender.send(mimeMailMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送带附件的邮件
     * @param mailBean 邮件bean
     * @return 是否成功
     */
    public boolean sendMailAttachmentFile(MailBean mailBean) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(mailBean.getToAccount());
            helper.setSubject(mailBean.getSubject());
            helper.setText(mailBean.getContent(), true);
            // 增加附件名称和附件
            helper.addAttachment(MimeUtility.encodeWord(mailBean.getAttachmentFileName(), "utf-8", "B"), mailBean.getAttachmentFile());
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
