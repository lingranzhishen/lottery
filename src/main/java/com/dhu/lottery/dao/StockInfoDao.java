package com.dhu.lottery.dao;

import com.dhu.lottery.model.StockInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Repository
public interface StockInfoDao {

	void insertStockInfo(StockInfo record);

	List<StockInfo> queryRecord(Map param);
}