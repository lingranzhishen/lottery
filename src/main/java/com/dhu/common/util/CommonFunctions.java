package com.dhu.common.util;

import java.util.List;

import com.dhu.common.Constants;
import com.dhu.lottery.model.LotteryRecord;

public class CommonFunctions {

	public static String isMatch(List<LotteryRecord> records, int type) {
		int missNum = 0;
		StringBuilder stringBuilder = new StringBuilder();
		for (int x = 0; x < 5; x++) {
			LotteryRecord lr = null;
			missNum = 0;
			for (int i = 0; i < records.size(); i++) {
				lr = records.get(i);
				if (type <= Constants.ODD_TYPE) {
					if (type != getEvenOrOddType(lr.getNumber(), x)) {
						missNum++;
					} else {
						if (type == Constants.EVEN_TYPE) {
							if (missNum >= Constants.MAX_ODD_MISS) {
								stringBuilder.append("第" + Constants.DIGIT_NAME[x % 5] + "位已经有" + missNum + "次奇数遗漏;");
							}
						} else {
							if (missNum >= Constants.MAX_EVEN_MISS) {
								stringBuilder.append("第" + Constants.DIGIT_NAME[x % 5] + "位已经有" + missNum + "次偶数遗漏;");
							}
						}
						break;
					}
					if (i == records.size() - 1) {
						if (type == Constants.EVEN_TYPE) {
							if (missNum >= Constants.MAX_ODD_MISS) {
								stringBuilder.append("第" + Constants.DIGIT_NAME[x % 5] + "位已经有" + missNum + "次奇数遗漏;");
							}
						} else {
							if (missNum >= Constants.MAX_EVEN_MISS) {
								stringBuilder.append("第" + Constants.DIGIT_NAME[x % 5] + "位已经有" + missNum + "次偶数遗漏;");
							}
						}
						break;
					}
				} else {
					if (type != getBigOrSmallType(lr.getNumber(), x)) {
						missNum++;
					} else {
						if (type == Constants.BIG_TYPE) {
							if (missNum >= Constants.MAX_BIG_MISS) {
								stringBuilder.append("第" + Constants.DIGIT_NAME[x % 5] + "位已经有" + missNum + "次小数遗漏;");
							}
						} else {
							if (missNum >= Constants.MAX_SMALL_MISS) {
								stringBuilder.append("第" + Constants.DIGIT_NAME[x % 5] + "位已经有" + missNum + "次大数遗漏;");
							}
						}
						break;
					}
					if (i == records.size() - 1) {
						if (type == Constants.BIG_TYPE) {
							if (missNum >= Constants.MAX_BIG_MISS) {
								stringBuilder.append("第" + Constants.DIGIT_NAME[x % 5] + "位已经有" + missNum + "次小数遗漏;");
							}
						} else {
							if (missNum >= Constants.MAX_SMALL_MISS) {
								stringBuilder.append("第" + Constants.DIGIT_NAME[x % 5] + "位已经有" + missNum + "次大数遗漏;");
							}
						}
						break;
					}
				}
			}
		}
		return stringBuilder.toString();
	}

	public static int getEvenOrOddType(String number, int pos) {
		int num = number.charAt(pos) - '0';
		if (num % 2 == 0) {
			return Constants.EVEN_TYPE;
		}
		return Constants.ODD_TYPE;
	}

	public static int getBigOrSmallType(String number, int pos) {
		int num = number.charAt(pos) - '0';
		if (num > 4) {
			return Constants.BIG_TYPE;
		}
		return Constants.SMALL_TYPE;
	}
}
