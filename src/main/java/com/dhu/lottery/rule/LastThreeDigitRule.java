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
		for(int f=0; f<5; f++)
			for(int s=f+1;s<5;s++)
				for(int t=s+1;t<5; t++) {
					for (int a = 0; a < 10; a++) {
						for (int b = a; b < 10; b++) {
							if (a == b) continue;
							int missNum = isMiss(lotteryRecords, a, b,f,s,t);
							if (missNum >= Constants.MAX_LAST_THREE_MISS) {
								stringBuilder.append(a + "_" + b+","+Constants.DIGIT_NAME[f]+"_"+Constants.DIGIT_NAME[s]+"_"+Constants.DIGIT_NAME[t] +"遗漏" + missNum + "次");
							} else {
								if(missNum>=15){
									logger.info(a + "_" + b+","+Constants.DIGIT_NAME[f]+"_"+Constants.DIGIT_NAME[s]+"_"+Constants.DIGIT_NAME[t] +"遗漏" + missNum + "次");
								}
							}
						}
					}
				}

		return stringBuilder.toString();
	}
	private int isMiss(List<LotteryRecord>lotteryRecords,int a,int b,int f,int s,int t){
		int missNum=0;
		for(LotteryRecord lotteryRecord:lotteryRecords){
			if(isMiss(lotteryRecord,a,b,f,s,t)){
				missNum++;
			}else{
				return missNum;
			}
		}
		return missNum;
	}

	private boolean isMiss(LotteryRecord lotteryRecord,int a,int b,int f,int s,int t){
		String lastThreeNum=""+lotteryRecord.getNumber().charAt(f)+lotteryRecord.getNumber().charAt(s)+lotteryRecord.getNumber().charAt(t);
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
