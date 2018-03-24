package com.dhu.lottery.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhu.common.HttpUtil;
import com.dhu.common.util.SpringContextUtil;
import com.dhu.common.util.StringUtil;
import com.dhu.lottery.dao.LotteryRecordDao;
import com.dhu.lottery.model.LotteryMiss;
import com.dhu.lottery.model.LotteryRecord;
import com.dhu.lottery.model.LotteryRule;
import com.dhu.lottery.rule.ILotteryRule;

@Service
public class LotteryRecordService {
	@Autowired
	LotteryRecordDao lotteryRecordDao;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	HttpUtil httpUtil;

	public String getLotteryMiss() {
		List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecord();
		List<LotteryRule> rules = lotteryRecordDao.getAllRule();
		List<ILotteryRule> ruleList = new ArrayList<>();
		if (rules != null) {
			for (LotteryRule lr : rules) {
				ILotteryRule ruleBean = (ILotteryRule) SpringContextUtil.getBean(lr.getRuleCode());
				if (ruleBean != null) {
					ruleBean.setLotteryRule(lr);
					ruleList.add(ruleBean);
				}
			}
		}
		StringBuilder result = new StringBuilder();
		for (ILotteryRule ilr : ruleList) {
			if (ilr.isMatch(records)) {
				result.append(ilr.getRuleResult()).append(";");
			}
		}
		if (result.length() > 0) {
			return result.toString();
		}
		return StringUtil.EMPTY;
	}

	public List<LotteryRecord> getTodayLotteryRecord() {
		return lotteryRecordDao.getTodayLotteryRecord();
	}

	public String insertLotteryRecord() {
		try {

			String result = httpUtil
					.doGet("http://caipiao.163.com/award/getAwardNumberInfo.html?gameEn=ssc&periodNum=10");
			JSONObject jo = JSONObject.parseObject(result);
			JSONArray awards = jo.getJSONArray("awardNumberInfoList");
			String lotteryNo = StringUtil.EMPTY;
			for (int i = 1; i < awards.size(); i++) {
				JSONObject award = awards.getJSONObject(i);
				LotteryRecord lotteryRecord = new LotteryRecord();
				String lastestPhase = award.getString("period");
				if (lotteryRecordDao.exists(lastestPhase) < 1) {
					String lastestNumber = award.getString("winningNumber").replaceAll(" ", StringUtil.EMPTY);
					lotteryRecord.setCreateTime(new Date());
					lotteryRecord.setLotteryNo(lastestPhase);
					lotteryRecord.setSequenceOfToday(Integer.parseInt(lastestPhase.substring(6)));
					lotteryRecord.setNumber(lastestNumber);
					lotteryRecord.setFirstDigit(lastestNumber.charAt(0) - '0');
					lotteryRecord.setSecondDigit(lastestNumber.charAt(1) - '0');
					lotteryRecord.setThirdDigit(lastestNumber.charAt(2) - '0');
					lotteryRecord.setFourthDigit(lastestNumber.charAt(3) - '0');
					lotteryRecord.setFifthDigit(lastestNumber.charAt(4) - '0');
					lotteryRecordDao.insertLotteryRecord(lotteryRecord);
					lotteryNo = lotteryRecord.getLotteryNo();
				}
			}
			return lotteryNo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringUtil.EMPTY;
	}

	public List<LotteryMiss> getAllLotteryMiss() {
		return lotteryRecordDao.getAllLotteryMiss();
	}

	public void insertLotteryMiss(LotteryMiss lm) {
		lotteryRecordDao.insertLotteryMiss(lm);
	}

	public int updateLotteryMiss() {
		return lotteryRecordDao.updateLotteryMiss();
	}
}
