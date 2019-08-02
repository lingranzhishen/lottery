package com.dhu.lottery.job;

import java.util.List;

import javax.mail.MessagingException;

import com.dhu.lottery.enums.LotteryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dhu.common.util.StringUtil;
import com.dhu.framework.utils.MailUtil;
import com.dhu.lottery.model.LotteryMiss;
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
		String lastestLottery = lotteryRecordService.insertLotteryRecordV3();
		if (StringUtil.isNotEmpty(lastestLottery)) {
			String result = lotteryRecordService.getLotteryMiss();
			if (StringUtil.isNotEmpty(result)) {
					LotteryMiss lm = new LotteryMiss();
					lm.setLotteryNo(lastestLottery);
					lm.setStatus(1);
					lm.setMsg(result);
					lm.setType(1);
					lotteryRecordService.insertLotteryMiss(lm);
				logger.info("发送邮件！！！");
				System.out.println("发送邮件！！！");
			}
		}
		logger.info("彩票监控结束！！！");
	}


    @Scheduled(cron = "0 0/1 * * * ?")
    public void monitorAllTypeLottery() {
        logger.info("monitorAllTypeLottery彩票监控开始！！！");
        for(LotteryType lotteryType:LotteryType.values()) {
            String lastestLottery = lotteryRecordService.insertLotteryRecordByType(lotteryType);
            if (StringUtil.isNotEmpty(lastestLottery)) {
                String result = lotteryRecordService.getLotteryMissByType(lotteryType);
                if (StringUtil.isNotEmpty(result)) {
                    LotteryMiss lm = new LotteryMiss();
                    lm.setLotteryNo(lastestLottery);
                    lm.setStatus(1);
                    lm.setMsg(result);
                    lm.setType(lotteryType.getType());
                    lotteryRecordService.insertLotteryMiss(lm);
                    logger.info("发送邮件！！！");
                    System.out.println("发送邮件！！！");
                }
            }
        }
        logger.info("monitorAllTypeLottery彩票监控结束！！！");
    }
	
	@Scheduled(cron = "0 2/5 * * * ?")
	public void sendNotify() {
		logger.info("彩票信息监控发送开始！！！");
		List<LotteryMiss> lmList=lotteryRecordService.getAllLotteryMiss();
		if (!CollectionUtils.isEmpty(lmList)) {
			for(LotteryMiss lm:lmList){
				try {
					mailUtil.sendTextMail(lm.toString());
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			lotteryRecordService.updateLotteryMiss();
		}
		logger.info("彩票信息监控发送结束！！！");
	}
}
