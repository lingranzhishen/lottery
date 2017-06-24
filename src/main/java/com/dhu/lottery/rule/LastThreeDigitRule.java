package com.dhu.lottery.rule;

import com.dhu.common.Constants;
import com.dhu.lottery.model.LotteryRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 后三遗漏
 * 
 * @author lizehua
 *
 */
@Component
public class LastThreeDigitRule extends AbstractLotteryRule {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String matchResult;

	@Override
	public boolean isMatch(List<LotteryRecord> record) {
		matchResult =getMatchResult(record);
		if (matchResult.length() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public String getRuleResult() {
		return matchResult;
	}

	private String getMatchResult(List<LotteryRecord> lotteryRecords){
		StringBuilder stringBuilder=new StringBuilder();
		for(int a=0; a<10; a++){
			for(int b=0; b<10; b++){
				if(a==b)continue;
				int missNum=isMiss(lotteryRecords,a,b);
				if(missNum>= Constants.MAX_LAST_THREE_MISS){
					stringBuilder.append(a+","+b+"后三遗漏"+missNum+"次");
				}else{
					logger.info(a+","+b+"后三遗漏"+missNum+"次");
				}
			}
		}

		return stringBuilder.toString();
	}
	private int isMiss(List<LotteryRecord>lotteryRecords,int a,int b){
		int missNum=0;
		for(LotteryRecord lotteryRecord:lotteryRecords){
			if(isMiss(lotteryRecord,a,b)){
				missNum++;
			}else{
				return missNum;
			}
		}
		return missNum;
	}

	private boolean isMiss(LotteryRecord lotteryRecord,int a,int b){
		String lastThreeNum=lotteryRecord.getNumber().substring(1);
		if(lastThreeNum.contains(String.valueOf(a))||lastThreeNum.contains(String.valueOf(b))){
			return true;
		}
		if(lotteryRecord.getFifthDigit().intValue()==lotteryRecord.getFourthDigit().intValue()||lotteryRecord.getFourthDigit().intValue()==lotteryRecord.getThirdDigit().intValue()
				||lotteryRecord.getThirdDigit().intValue()==lotteryRecord.getFifthDigit().intValue()){
			return true;
		}
		return false;
	}
}
