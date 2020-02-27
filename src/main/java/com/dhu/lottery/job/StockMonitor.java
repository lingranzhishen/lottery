package com.dhu.lottery.job;

import com.dhu.framework.utils.MailUtil;
import com.dhu.lottery.service.StockRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockMonitor {

	@Autowired
	StockRecordService stockRecordService;
	private final static Logger logger = LoggerFactory.getLogger(StockMonitor.class);

	@Autowired
	MailUtil mailUtil;

	@Scheduled(cron = "0 0 1 * * ?")
	public void monitorStock() {
		logger.info("股票监控开始！！！");
		stockRecordService.insertStockInfo();
	}


}
