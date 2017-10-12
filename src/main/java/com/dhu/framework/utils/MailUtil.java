package com.dhu.framework.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.dhu.lottery.dao.LotteryRecordDao;
import com.dhu.lottery.service.LotteryRecordService;

/**
 * spring发送mail工具
 * 
 * @author: haleywang,@created on: 2012-9-20
 */
@Component
public class MailUtil {
	private final static Logger logger = LoggerFactory.getLogger(MailUtil.class);
	@Autowired
	JavaMailSenderImpl mailSender;
	@Autowired
	LotteryRecordDao lotteryRecordDao;

	public void sendTextMail(String msg) throws MessagingException {
		MimeMessage mailMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "utf-8");
		// 设置收件人,群发邮件
		String[] defaultArray={"280146985@qq.com","libingchen159159@163.com"};
		String[] array=null;
		try{
		 array =lotteryRecordDao.getAllOptionValues("email").toArray(new String[0]);
		}catch(Exception e){
		}
		if(array==null|| array.length==0){
			array=defaultArray;
		}
		logger.info("发送邮件！！！"+JSON.toJSONString(array));
		helper.setTo(array);
		helper.setFrom("280146985@qq.com");
		helper.setSubject("彩票提醒！！！");
		helper.setText(msg, true);
		// 发送邮件
		mailSender.send(mailMessage);
	}
}
