package com.dhu.lottery.rule;

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
        logger.info("120监控次数"+getNumber());
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
          if(isMatch120(lr.getNumber())) {
             break;
          }else{
              missNum++;
          }
        }
        if (missNum >= getNumber()) {
            return "120遗漏" + missNum + "次";
        }
        if (missNum >= getLoggingMissNumber()) {
            logger.info("120遗漏" + missNum + "次");
        }
        return "";
    }

    private boolean isMatch120(String number) {
        char[] nums = number.toCharArray();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] == nums[j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
