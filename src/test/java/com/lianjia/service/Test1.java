package com.lianjia.service;

import javax.mail.MessagingException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhu.framework.utils.MailUtil;
import com.dhu.lottery.service.LotteryRecordService;

import base.BaseTest;

public class Test1 extends BaseTest {
	@Autowired
	LotteryRecordService lotteryRecordService;

	@Autowired
	MailUtil mailUtil;

	@Test
	public void test() {
		try {
			mailUtil.sendTextMail(lotteryRecordService.getLotteryMiss());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getData(){
		lotteryRecordService.insertLotteryRecord();
	}
}
