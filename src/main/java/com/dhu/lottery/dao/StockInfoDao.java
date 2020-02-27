package com.dhu.lottery.dao;

import com.dhu.lottery.model.StockInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface StockInfoDao {

	void insertStockInfo(StockInfo record);

	StockInfo queryRecord(String symbol,Date pushDate);
}