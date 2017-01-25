package com.dhu.lottery.rule;

import java.util.List;

import com.dhu.lottery.model.LotteryRecord;
import com.dhu.lottery.model.LotteryRule;

public interface ILotteryRule {
	void setLotteryRule(LotteryRule lotteryRule);

	String getRuleName();

	String getRuleDesc();

	Integer getNumber();
	
	String getRuleResult();

	boolean isMatch(List<LotteryRecord> record);
}
