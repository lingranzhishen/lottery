package com.dhu.lottery.rule;

import com.dhu.common.Constants;
import com.dhu.common.util.CommonFunctions;
import com.dhu.lottery.model.LotteryRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 120
 *
 * @author lizehua
 */
@Component
public class Digit120Rule extends AbstractLotteryRule {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private String matchResult;

    @Override
    public boolean isMatch(List<LotteryRecord> record) {
        matchResult = getMatchResult(record);
        if (matchResult.length() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public String getRuleResult() {
        return matchResult;
    }

    private String getMatchResult(List<LotteryRecord> lotteryRecords) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(isMiss(lotteryRecords));
        return stringBuilder.toString();
    }

    private String isMiss(List<LotteryRecord> lotteryRecords) {
        int missNum = 0;
        for (LotteryRecord lr : lotteryRecords) {
            char []nums=lr.getNumber().toCharArray();
            for(int i=0; i<nums.length; i++){
                for(int j=i+1; j<nums.length; j++){
                    if(nums[i]==nums[j]){
                        return "";
                    }
                }
            }
            missNum++;
        }
        if (missNum >= Constants.MAX_LAST_THREE_MISS) {
            return "120遗漏" + missNum + "次";
        }
        if (missNum >= 15) {
            logger.info("120遗漏" + missNum + "次");
        }
        return "";
    }

}
