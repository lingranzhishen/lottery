package com.dhu.lottery.rule;

import com.dhu.common.Constants;
import com.dhu.common.util.CommonFunctions;
import com.dhu.lottery.model.LotteryRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 组六单式
 *
 * @author lizehua
 */
@Component
public class CombinationSixSingleDigitRule extends AbstractLotteryRule {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String matchResult;

    @Override
    public boolean isMatch(List<LotteryRecord> record) {
        logger.info("组六单式监控次数" + getNumber());
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
        for (int f = 0; f < 5; f++)
            for (int s = f + 1; s < 5; s++)
                for (int t = s + 1; t < 5; t++) {

                    int missNum = isMiss(lotteryRecords, f, s, t);
                    if (missNum >= getNumber()) {
                        stringBuilder.append(Constants.DIGIT_NAME[f] + "_" + Constants.DIGIT_NAME[s] + "_" + Constants.DIGIT_NAME[t] + "组六单式遗漏" + missNum + "次");
                    } else {
                        if (missNum >= getLoggingMissNumber()) {
                            logger.info(Constants.DIGIT_NAME[f] + "_" + Constants.DIGIT_NAME[s] + "_" + Constants.DIGIT_NAME[t] + "组六单式遗漏" + missNum + "次");
                        }
                    }
                }

        return stringBuilder.toString();
    }

    private int isMiss(List<LotteryRecord> lotteryRecords, int f, int s, int t) {
        int missNum = 0;
        for (LotteryRecord lotteryRecord : lotteryRecords) {
            if (isMiss(lotteryRecord, f, s, t)) {
                missNum++;
            } else {
                return missNum;
            }
        }
        return missNum;
    }

    private boolean isMiss(LotteryRecord lotteryRecord, int f, int s, int t) {
        int first = lotteryRecord.getNumber().charAt(f) - '0';
        int second = lotteryRecord.getNumber().charAt(s) - '0';
        int third = lotteryRecord.getNumber().charAt(t) - '0';

        //组三全单全双全大全小
        if (CommonFunctions.isCombinationThree(first, second, third) || CommonFunctions.isAllBigOrSmall(first, second, third) || CommonFunctions.isAllEvenOrOdd(first, second, third)) {
            return true;
        }

        return false;
    }
}
