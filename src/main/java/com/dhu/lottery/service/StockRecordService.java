package com.dhu.lottery.service;

import com.alibaba.fastjson.JSONObject;
import com.dhu.common.HttpUtil;
import com.dhu.common.util.DateUtil;
import com.dhu.common.util.StringUtil;
import com.dhu.lottery.dao.StockInfoDao;
import com.dhu.lottery.model.ResultDTO;
import com.dhu.lottery.model.StockInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
                        StockInfo stockInfo= lotteryRecordDao.queryRecord(maps);
                        if(stockInfo==null){
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


    public ResultDTO getByCityAndPage(String city,int page) throws IOException, URISyntaxException {
        String url=String.format("http://web.juhe.cn:8080/finance/stock/%sall",city);
        Map<String,String>param=new HashMap<>();
        param.put("key","8cd0382e563cc83b8950187c8f04d8db");
        param.put("type","4");
        param.put("page",String.valueOf(page));
        String result = httpUtil.doGet(url,param);
        return JSONObject.parseObject(result,ResultDTO.class);
    }

}
