package com.dhu.lottery.rule;

import java.util.List;

import com.dhu.lottery.model.LotteryRecord;
import com.dhu.lottery.model.LotteryRule;

public abstract class AbstractLotteryRule implements ILotteryRule {

	private LotteryRule lotteryRule;

	@Override
	public String getRuleName() {
		return lotteryRule.getRuleName();
	}

	@Override
	public String getRuleDesc() {
		return lotteryRule.getRuleDesc();
	}

	@Override
	public Integer getNumber() {
		return lotteryRule.getNumber();
	}

	@Override
	public abstract boolean isMatch(List<LotteryRecord> record);

	@Override
	public void setLotteryRule(LotteryRule lotteryRule) {
		this.lotteryRule=lotteryRule;
	}

}
