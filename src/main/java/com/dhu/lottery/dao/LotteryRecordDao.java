package com.dhu.lottery.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dhu.lottery.model.LotteryMiss;
import com.dhu.lottery.model.LotteryRecord;
import com.dhu.lottery.model.LotteryRule;

@Repository
public interface LotteryRecordDao {
	List<LotteryRecord> getTodayLotteryRecord();

	void insertLotteryRecord(LotteryRecord record);

	List<LotteryRule> getAllRule();

	int exists(String lastestPhase);

	int updateLotteryMiss();

	void insertLotteryMiss(LotteryMiss lm);

	void insertLotteryRecordV2(LotteryMiss lm);


	List<LotteryRecord> getTodayLotteryRecordV2(Integer type);

	List<LotteryMiss> getAllLotteryMiss();
	
	List<String> getAllOptionValues(String type);
}