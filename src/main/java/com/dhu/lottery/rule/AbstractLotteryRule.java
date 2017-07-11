package com.dhu.lottery.rule;

import com.dhu.common.Constants;
import com.dhu.lottery.model.LotteryRecord;
import com.dhu.lottery.model.LotteryRule;

import java.util.List;

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
		if(lotteryRule.getNumber()==null){
			return Constants.MAX_BIG_MISS;
		}
		return lotteryRule.getNumber();
	}
	public Integer getLoggingMissNumber() {
		if(lotteryRule.getNumber()==null){
			return Constants.MAX_BIG_MISS-5;
		}
		return lotteryRule.getNumber()-5;
	}
	@Override
	public abstract boolean isMatch(List<LotteryRecord> record);

	@Override
	public void setLotteryRule(LotteryRule lotteryRule) {
		this.lotteryRule=lotteryRule;
	}

}
