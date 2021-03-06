package com.lianjia.service;

import javax.mail.MessagingException;

import com.dhu.common.HttpUtil;
import com.dhu.common.util.StringUtil;
import com.dhu.lottery.enums.LotteryType;
import com.dhu.lottery.enums.PicNumType;
import com.dhu.lottery.model.LotteryRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhu.framework.utils.MailUtil;
import com.dhu.lottery.service.LotteryRecordService;

import base.BaseTest;
import org.springframework.util.CollectionUtils;

import java.io.IOException;

public class Test1 extends BaseTest {
	@Autowired
	LotteryRecordService lotteryRecordService;

	@Autowired
	MailUtil mailUtil;

	@Autowired
	HttpUtil httpUtil;

	@Test
	public void test() {
		try {
			mailUtil.sendTextMail(lotteryRecordService.getLotteryMiss());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHtml() throws IOException {
		String result = httpUtil
				.doGet("https://shishicai.cjcp.com.cn/chongqing/kaijiang");
		Document doc = Jsoup.parse(result);
		String lotteryNo = StringUtil.EMPTY;
		Elements kjjg_table = doc.getElementsByClass("kjjg_table");
		Element table = kjjg_table.first();
		Elements trs = table.select("tr");

		for (int i = 0; i < trs.size(); i++) {
			String lastestPhase = "";
			String lastestNumber = "";
			Element tr = trs.get(i);
			Elements tds = tr.select("td");
			if (tds.get(0).hasClass("hui")) {
				continue;
			}
			lastestPhase = tds.get(0).text().substring(2, 11);
			Elements numbers = tds.get(2).getElementsByClass("hm_bg");
			for (int j = 0; j < numbers.size(); ++j) {
				Element td = numbers.get(j);
				lastestNumber += td.text();
			}


		}
	}
	
	@Test
	public void getData(){
		try {
			String result = httpUtil
					.doGet(LotteryType.CQ_SSC.getUrl());
			Document doc = Jsoup.parse(result);
			String lotteryNo = StringUtil.EMPTY;

			Elements kjjg_table = doc.getElementsByClass("result_infor fr");
			Element table = kjjg_table.first();
			Elements trs = table.select("tr");

			for (int i = 0; i < trs.size(); i++) {
				String lastestPhase = "";
				String lastestNumber = "";
				Element tr = trs.get(i);
				Elements tds = tr.select("td");
				if (CollectionUtils.isEmpty(tds)) {
					continue;
				}
				lastestPhase = tds.get(0).text().substring(2, 11);

				Elements numbers = tds.get(2).select("img");
				;
				for (int j = 0; j < numbers.size(); ++j) {
					Element td = numbers.get(j);
					lastestNumber += PicNumType.getNumByPicUrl(td.attributes().get("src"));
				}

				LotteryRecord lotteryRecord = new LotteryRecord();


			}
		}catch (Exception e){

		}
	}
}
