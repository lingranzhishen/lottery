package com.dhu.lottery.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dhu.lottery.model.LotteryMiss;
import com.dhu.lottery.model.LotteryRecord;
import com.dhu.lottery.model.LotteryRule;

@Repository
public interface LotteryRecordDao {
	List<LotteryRecord> getTodayLotteryRecord();

	void insertLotteryRecord(LotteryRecord record);

	List<LotteryRule> getAllRule();

	int exists(String lotteryNo);

	int updateLotteryMiss();

	void insertLotteryMiss(LotteryMiss lm);

	void insertLotteryRecordV2(LotteryRecord lm);


	List<LotteryRecord> getTodayLotteryRecordByType(Integer type);

	List<LotteryMiss> getAllLotteryMiss();
	
	List<String> getAllOptionValues(String type);

	int existsV2(Map param);
}