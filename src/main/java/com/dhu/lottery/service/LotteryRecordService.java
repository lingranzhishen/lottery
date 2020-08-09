package com.dhu.lottery.service;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.dhu.lottery.enums.GDLotteryType;
import com.dhu.lottery.enums.LotteryType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhu.common.HttpUtil;
import com.dhu.common.util.SpringContextUtil;
import com.dhu.common.util.StringUtil;
import com.dhu.lottery.dao.LotteryRecordDao;
import com.dhu.lottery.model.LotteryMiss;
import com.dhu.lottery.model.LotteryRecord;
import com.dhu.lottery.model.LotteryRule;
import com.dhu.lottery.rule.ILotteryRule;

@Service
public class LotteryRecordService {
    @Autowired
    LotteryRecordDao lotteryRecordDao;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HttpUtil httpUtil;

    private final static Logger logger = LoggerFactory.getLogger(LotteryRecordService.class);


    public String getLotteryMiss() {
        List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecord();
        List<LotteryRule> rules = lotteryRecordDao.getAllRule();
        List<ILotteryRule> ruleList = new ArrayList<>();
        if (rules != null) {
            for (LotteryRule lr : rules) {
                ILotteryRule ruleBean = (ILotteryRule) SpringContextUtil.getBean(lr.getRuleCode());
                if (ruleBean != null) {
                    ruleBean.setLotteryRule(lr);
                    ruleList.add(ruleBean);
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (ILotteryRule ilr : ruleList) {
            if (ilr.isMatch(records)) {
                result.append(ilr.getRuleResult()).append(";");
            }
        }
        if (result.length() > 0) {
            return result.toString();
        }
        return StringUtil.EMPTY;
    }

    public String getLotteryMissByType(LotteryType type) {
        List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecordByType(type.getType());
        List<LotteryRule> rules = lotteryRecordDao.getAllRule();
        List<ILotteryRule> ruleList = new ArrayList<>();
        if (rules != null) {
            for (LotteryRule lr : rules) {
                ILotteryRule ruleBean = (ILotteryRule) SpringContextUtil.getBean(lr.getRuleCode());
                if (ruleBean != null) {
                    ruleBean.setLotteryRule(lr);
                    ruleList.add(ruleBean);
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (ILotteryRule ilr : ruleList) {
            if (ilr.isMatch(records)) {
                result.append(ilr.getRuleResult()).append(";");
            }
        }
        if (result.length() > 0) {
            return type.getDesc()+":"+result.toString();
        }
        return StringUtil.EMPTY;
    }

    public String getNewestLotteryRecord(LotteryType type) {
        List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecordByType(type.getType());
        if(CollectionUtils.isEmpty(records)){
            return StringUtil.EMPTY;
        }
        return records.get(0).getLotteryNo();
    }


    public String getLotteryMissByType(GDLotteryType type) {
        List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecordByType(type.getType());
        List<LotteryRule> rules = lotteryRecordDao.getAllRule();
        List<ILotteryRule> ruleList = new ArrayList<>();
        if (rules != null) {
            for (LotteryRule lr : rules) {
                ILotteryRule ruleBean = (ILotteryRule) SpringContextUtil.getBean(lr.getRuleCode());
                if (ruleBean != null) {
                    ruleBean.setLotteryRule(lr);
                    ruleList.add(ruleBean);
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (ILotteryRule ilr : ruleList) {
            if (ilr.isMatch(records)) {
                result.append(ilr.getRuleResult()).append(";");
            }
        }
        if (result.length() > 0) {
            return type.getDesc()+":"+result.toString();
        }
        return StringUtil.EMPTY;
    }

    public String getNewestLotteryRecord(GDLotteryType type) {
        List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecordByType(type.getType());
        if(CollectionUtils.isEmpty(records)){
            return StringUtil.EMPTY;
        }
        return records.get(0).getLotteryNo();
    }

    public List<LotteryRecord> getTodayLotteryRecord() {
        return lotteryRecordDao.getTodayLotteryRecord();
    }

    @Deprecated
    public String insertLotteryRecord() {
        try {

            String result = httpUtil
                    .doGet("http://caipiao.163.com/award/getAwardNumberInfo.html?gameEn=ssc&periodNum=10");
            JSONObject jo = JSONObject.parseObject(result);
            JSONArray awards = jo.getJSONArray("awardNumberInfoList");
            String lotteryNo = StringUtil.EMPTY;
            for (int i = 1; i < awards.size(); i++) {
                JSONObject award = awards.getJSONObject(i);
                LotteryRecord lotteryRecord = new LotteryRecord();
                String lastestPhase = award.getString("period");
                if (lotteryRecordDao.exists(lastestPhase) < 1) {
                    String lastestNumber = award.getString("winningNumber").replaceAll(" ", StringUtil.EMPTY);
                    lotteryRecord.setCreateTime(new Date());
                    lotteryRecord.setLotteryNo(lastestPhase);
                    lotteryRecord.setSequenceOfToday(Integer.parseInt(lastestPhase.substring(6)));
                    lotteryRecord.setNumber(lastestNumber);
                    lotteryRecord.setFirstDigit(lastestNumber.charAt(0) - '0');
                    lotteryRecord.setSecondDigit(lastestNumber.charAt(1) - '0');
                    lotteryRecord.setThirdDigit(lastestNumber.charAt(2) - '0');
                    lotteryRecord.setFourthDigit(lastestNumber.charAt(3) - '0');
                    lotteryRecord.setFifthDigit(lastestNumber.charAt(4) - '0');
                    lotteryRecordDao.insertLotteryRecord(lotteryRecord);
                    lotteryNo = lotteryRecord.getLotteryNo();
                }
            }
            return lotteryNo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtil.EMPTY;
    }


    public String insertLotteryRecordV3() {
        try {
            String result = httpUtil
                    .doGet("https://shishicai.cjcp.com.cn/chongqing/kaijiang");
            Document doc = Jsoup.parse(result);
            String lotteryNo = StringUtil.EMPTY;
            Elements kjjg_table = doc.getElementsByClass("kjjg_table");
            Element table = kjjg_table.first();
            Elements trs = table.select("tr");

            for (int i = 0; i < trs.size(); i++) {
                String lastestPhase = "";
                String lastestNumber = "";
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                if (tds.get(0).hasClass("hui")) {
                    continue;
                }
                lastestPhase = tds.get(0).text().substring(2, 11);
                Elements numbers = tds.get(2).getElementsByClass("hm_bg");
                for (int j = 0; j < numbers.size(); ++j) {
                    Element td = numbers.get(j);
                    lastestNumber += td.text();
                }

                LotteryRecord lotteryRecord = new LotteryRecord();
                if (lotteryRecordDao.exists(lastestPhase) < 1) {
                    lotteryRecord.setCreateTime(new Date());
                    lotteryRecord.setLotteryNo(lastestPhase);
                    lotteryRecord.setSequenceOfToday(Integer.parseInt(lastestPhase.substring(6)));
                    lotteryRecord.setNumber(lastestNumber);
                    lotteryRecord.setFirstDigit(lastestNumber.charAt(0) - '0');
                    lotteryRecord.setSecondDigit(lastestNumber.charAt(1) - '0');
                    lotteryRecord.setThirdDigit(lastestNumber.charAt(2) - '0');
                    lotteryRecord.setFourthDigit(lastestNumber.charAt(3) - '0');
                    lotteryRecord.setFifthDigit(lastestNumber.charAt(4) - '0');
                    lotteryRecordDao.insertLotteryRecord(lotteryRecord);
                    lotteryNo = lotteryRecord.getLotteryNo();
                }

            }
            return lotteryNo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtil.EMPTY;
    }

    public String insertLotteryRecordByType(LotteryType lotteryType) {
        if(lotteryType.equals(LotteryType.HLJ_SSC)){
            return insertHeilongjiangLotteryRecordByType(lotteryType);
        }
        try {
            String result = httpUtil
                    .doGet(lotteryType.getUrl());
            Document doc = Jsoup.parse(result);
            String lotteryNo = StringUtil.EMPTY;
            Elements kjjg_table = doc.getElementsByClass("kjjg_table");
            Element table = kjjg_table.first();
            Elements trs = table.select("tr");

            for (int i = 0; i < trs.size(); i++) {
                String lastestPhase = "";
                String lastestNumber = "";
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                if (tds.get(0).hasClass("hui")) {
                    continue;
                }
                lastestPhase = tds.get(0).text().substring(2, 11);
                Elements numbers = tds.get(2).getElementsByClass("hm_bg");
                for (int j = 0; j < numbers.size(); ++j) {
                    Element td = numbers.get(j);
                    lastestNumber += td.text();
                }

                LotteryRecord lotteryRecord = new LotteryRecord();
                Map param=new HashMap();
                param.put("lotteryNo",lastestPhase);
                param.put("type",lotteryType.getType());
                if (lotteryRecordDao.existsV2(param) < 1) {
                    lotteryRecord.setCreateTime(new Date());
                    lotteryRecord.setLotteryNo(lastestPhase);

                    lotteryRecord.setSequenceOfToday(Integer.parseInt(lastestPhase.substring(6)));
                    lotteryRecord.setNumber(lastestNumber);
                    lotteryRecord.setFirstDigit(lastestNumber.charAt(0) - '0');
                    lotteryRecord.setSecondDigit(lastestNumber.charAt(1) - '0');
                    lotteryRecord.setThirdDigit(lastestNumber.charAt(2) - '0');
                    lotteryRecord.setFourthDigit(lastestNumber.charAt(3) - '0');
                    lotteryRecord.setFifthDigit(lastestNumber.charAt(4) - '0');
                    lotteryRecord.setType(lotteryType.getType());
                    lotteryRecordDao.insertLotteryRecordV2(lotteryRecord);
                    lotteryNo = lotteryRecord.getLotteryNo();
                }

            }
            return lotteryNo;
        } catch (Exception e) {
            logger.error("获取号码失败"+e.getMessage(),e);
        }
        return StringUtil.EMPTY;
    }

    public String insertHeilongjiangLotteryRecordByType(LotteryType lotteryType) {
        try {
            String result = httpUtil
                    .doGet(lotteryType.getUrl());
            Document doc = Jsoup.parse(result);
            String lotteryNo = StringUtil.EMPTY;
            Elements kjjg_table = doc.getElementsByClass("kjjg_table");
            Element table = kjjg_table.first();
            Elements trs = table.select("tr");

            for (int i = 0; i < trs.size(); i++) {
                String lastestPhase = "";
                String lastestNumber = "";
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                if (tds.get(0).hasClass("hui")) {
                    continue;
                }
                lastestPhase = tds.get(0).text().substring(0, 6);
                Elements numbers = tds.get(2).getElementsByClass("hm_bg");
                for (int j = 0; j < numbers.size(); ++j) {
                    Element td = numbers.get(j);
                    lastestNumber += td.text();
                }

                LotteryRecord lotteryRecord = new LotteryRecord();
                Map param=new HashMap();
                param.put("lotteryNo",lastestPhase);
                param.put("type",lotteryType.getType());
                if (lotteryRecordDao.existsV2(param) < 1) {
                    lotteryRecord.setCreateTime(new Date());
                    lotteryRecord.setLotteryNo(lastestPhase);

                    lotteryRecord.setSequenceOfToday(Integer.parseInt(lastestPhase));
                    lotteryRecord.setNumber(lastestNumber);
                    lotteryRecord.setFirstDigit(lastestNumber.charAt(0) - '0');
                    lotteryRecord.setSecondDigit(lastestNumber.charAt(1) - '0');
                    lotteryRecord.setThirdDigit(lastestNumber.charAt(2) - '0');
                    lotteryRecord.setFourthDigit(lastestNumber.charAt(3) - '0');
                    lotteryRecord.setFifthDigit(lastestNumber.charAt(4) - '0');
                    lotteryRecord.setType(lotteryType.getType());
                    lotteryRecordDao.insertLotteryRecordV2(lotteryRecord);
                    lotteryNo = lotteryRecord.getLotteryNo();
                }

            }
            return lotteryNo;
        } catch (Exception e) {
            logger.error("获取号码失败"+e.getMessage(),e);
        }
        return StringUtil.EMPTY;
    }



    public List<LotteryMiss> getAllLotteryMiss() {
        return lotteryRecordDao.getAllLotteryMiss();
    }

    public void insertLotteryMiss(LotteryMiss lm) {
        lotteryRecordDao.insertLotteryMiss(lm);
    }

    public void insertGDLotteryRecord(String lotteryNo, String digits,int lotteryType) {

        GDLotteryType gdLotteryType=  GDLotteryType.getGdTypeByValue(lotteryType);
        if(GDLotteryType.DEFAULT.equals(gdLotteryType)){
            return;
        }
        LotteryRecord lotteryRecord = new LotteryRecord();
        Map param=new HashMap();
        param.put("lotteryNo",lotteryNo);
        param.put("type",lotteryType);
        if (lotteryRecordDao.existsV2(param) < 1) {
            lotteryRecord.setCreateTime(new Date());
            lotteryRecord.setLotteryNo(lotteryNo);

            lotteryRecord.setSequenceOfToday((int)(new BigDecimal(lotteryNo).longValue()%1000));
            lotteryRecord.setNumber(digits);
            lotteryRecord.setFirstDigit(digits.charAt(0) - '0');
            lotteryRecord.setSecondDigit(digits.charAt(1) - '0');
            lotteryRecord.setThirdDigit(digits.charAt(2) - '0');
            lotteryRecord.setFourthDigit(digits.charAt(3) - '0');
            lotteryRecord.setFifthDigit(digits.charAt(4) - '0');
            lotteryRecord.setType(lotteryType);
            lotteryRecordDao.insertLotteryRecordV2(lotteryRecord);
            logger.info(JSON.toJSONString(lotteryRecord));

            String result = getLotteryMissByType(gdLotteryType);
            String lastestLottery=getNewestLotteryRecord(gdLotteryType);
            if (StringUtil.isNotEmpty(result)) {
                LotteryMiss lm = new LotteryMiss();
                lm.setLotteryNo(lastestLottery);
                lm.setStatus(1);
                lm.setMsg(result);
                lm.setType(gdLotteryType.getType());
                insertLotteryMiss(lm);
                logger.info("发送邮件！！！");
                System.out.println("发送邮件！！！");
            }
        }
    }


    public int updateLotteryMiss() {
        return lotteryRecordDao.updateLotteryMiss();
    }
}
