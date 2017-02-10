package com.dhu.lottery.job;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dhu.common.util.StringUtil;
import com.dhu.framework.utils.MailUtil;
import com.dhu.lottery.service.LotteryRecordService;

@Component
public class LotteryMonitor {

	@Autowired
	LotteryRecordService lotteryRecordService;
	private final static Logger logger = LoggerFactory.getLogger(LotteryRecordService.class);

	@Autowired
	MailUtil mailUtil;

	@Scheduled(cron = "0 0/1 * * * ?")
	public void monitorLottery() {
		logger.info("彩票监控开始！！！");
		String lastestLottery = lotteryRecordService.insertLotteryRecord();
		if (StringUtil.isNotEmpty(lastestLottery)) {
			String result = lotteryRecordService.getLotteryMiss();
			if (StringUtil.isNotEmpty(result)) {
				try {
					mailUtil.sendTextMail(result);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				logger.info("发送邮件！！！");
				System.out.println("发送邮件！！！");
			}
		}
		logger.info("彩票监控结束！！！");
	}
}
