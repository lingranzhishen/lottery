package com.dhu.lottery.rule;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dhu.common.Constants;
import com.dhu.common.util.CommonFunctions;
import com.dhu.lottery.model.LotteryRecord;

/**
 * 五位数字，大小单双奇偶有遗漏10次的，然后进行定位胆
 * 
 * @author lizehua
 *
 */
@Component
public class SingleDigitRule extends AbstractLotteryRule {

	private String matchResult;

	@Override
	public boolean isMatch(List<LotteryRecord> record) {
		StringBuilder matchResultBuilder = new StringBuilder();
		for (int i : Constants.MISS_TYPE) {
			matchResultBuilder.append(CommonFunctions.isMatch(record, i));
		}
		if (matchResultBuilder.length() > 0) {
			matchResult = matchResultBuilder.toString();
			return true;
		}
		return false;
	}

	@Override
	public String getRuleResult() {
		return matchResult;
	}

}
