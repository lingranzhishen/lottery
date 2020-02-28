package com.dhu.lottery.service;

import com.alibaba.fastjson.JSONObject;
import com.dhu.common.HttpUtil;
import com.dhu.common.util.DateUtil;
import com.dhu.common.util.StringUtil;
import com.dhu.lottery.dao.StockInfoDao;
import com.dhu.lottery.model.LotteryRecord;
import com.dhu.lottery.model.ResultDTO;
import com.dhu.lottery.model.StockInfo;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class StockRecordService {
    @Autowired
    StockInfoDao lotteryRecordDao;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HttpUtil httpUtil;

    private final static Logger logger = LoggerFactory.getLogger(StockRecordService.class);

    public String insertStockInfo() {
        try {
            String[]cityList={"sh","sz"};
            for(int i=0; i<cityList.length; i++){
                int totalPage=0;
                ResultDTO jo = getByCityAndPage(cityList[i],1);
               if(jo.getError_code()==0){
                 totalPage=  jo.getResult().getTotalCount()/80+1;
               }
                for(int j=1; j<totalPage;j++){
                    ResultDTO tempResult = getByCityAndPage(cityList[i],j);
                    for(StockInfo s: tempResult.getResult().getData()){
                        Map<String,Object>maps=new HashMap<>();
                        maps.put("symbol",s.getSymbol());
                        maps.put("pushDate",DateUtil.getTodayDate());
                        List<StockInfo> stockInfo= lotteryRecordDao.queryRecord(maps);
                        if(CollectionUtils.isEmpty(stockInfo)){
                            s.setAmount(getValueAmount(s.getSymbol()));
                            lotteryRecordDao.insertStockInfo(s);
                        }
                    }
                }

            }
        } catch (Exception e) {
            logger.error("fail",e);
        }
        return StringUtil.EMPTY;
    }


    public Long getValueAmount(String symbol) {
        try {
            String url=String.format("http://quote.cfi.cn/quote_%s.html",symbol.substring(2));
            String result = httpUtil
                    .doGet(url);
            Document doc = Jsoup.parse(result);
            Elements amount = doc.getElementsByClass("Rtable");
            Element table = amount.first();
            Elements trs = table.select("tr");

            for (int i = 0; i < trs.size(); i++) {
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                if (tds.get(0).text().contains("总")) {
                    String REGEX = "[^(0-9).]";
                    String  lastestPhase = tds.get(1).text();
                   String num= Pattern.compile(REGEX).matcher(lastestPhase).replaceAll("").trim();
                    return new BigDecimal(num).multiply(new BigDecimal(100000000)).longValue();
                }
            }
        } catch (Exception e) {
            logger.info("fail",e);
        }
        return 0L;
    }



    public ResultDTO getByCityAndPage(String city,int page) throws IOException, URISyntaxException {
        String url=String.format("http://web.juhe.cn:8080/finance/stock/%sall",city);
        Map<String,String>param=new HashMap<>();
        param.put("key","8cd0382e563cc83b8950187c8f04d8db");
        param.put("type","4");
        param.put("page",String.valueOf(page));
        String result = httpUtil.doGet(url,param);
        logger.info(result);
        return JSONObject.parseObject(result,ResultDTO.class);
    }

}
