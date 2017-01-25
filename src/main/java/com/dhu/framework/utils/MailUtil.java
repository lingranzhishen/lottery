package com.dhu.framework.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * spring发送mail工具
 * 
 * @author: haleywang,@created on: 2012-9-20
 */
@Component
public class MailUtil {
	@Autowired
	JavaMailSenderImpl mailSender;

	public void sendTextMail(String msg) throws MessagingException {
		MimeMessage mailMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "utf-8");
		// 设置收件人,群发邮件
		String[] array = new String[] { "280146985@qq.com" };
		helper.setTo(array);
		helper.setFrom("280146985@qq.com");
		helper.setSubject("彩票提醒！！！");
		helper.setText(msg, true);

		// 发送邮件
		mailSender.send(mailMessage);
		System.out.println("邮件发送成功.. ");
	}
}
