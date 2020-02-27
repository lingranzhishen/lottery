/**
  * Copyright 2020 bejson.com 
  */
package com.dhu.lottery.model;

/**
 * Auto-generated: 2020-02-27 22:31:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class StockInfo {

    /**
     * 股票代码
     */
    private String symbol;
    /**
     * 股票名称
     */
    private String name;
    private String trade;
    private String pricechange;
    private String changepercent;
    private String buy;
    private String sell;
    private String settlement;
    private String open;
    private String high;
    private String low;
    private long volume;
    /**
     * 市值 元
     */
    private long amount;
    private String code;
    private String ticktime;
    public void setSymbol(String symbol) {
         this.symbol = symbol;
     }
     public String getSymbol() {
         return symbol;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setTrade(String trade) {
         this.trade = trade;
     }
     public String getTrade() {
         return trade;
     }

    public void setPricechange(String pricechange) {
         this.pricechange = pricechange;
     }
     public String getPricechange() {
         return pricechange;
     }

    public void setChangepercent(String changepercent) {
         this.changepercent = changepercent;
     }
     public String getChangepercent() {
         return changepercent;
     }

    public void setBuy(String buy) {
         this.buy = buy;
     }
     public String getBuy() {
         return buy;
     }

    public void setSell(String sell) {
         this.sell = sell;
     }
     public String getSell() {
         return sell;
     }

    public void setSettlement(String settlement) {
         this.settlement = settlement;
     }
     public String getSettlement() {
         return settlement;
     }

    public void setOpen(String open) {
         this.open = open;
     }
     public String getOpen() {
         return open;
     }

    public void setHigh(String high) {
         this.high = high;
     }
     public String getHigh() {
         return high;
     }

    public void setLow(String low) {
         this.low = low;
     }
     public String getLow() {
         return low;
     }

    public void setVolume(long volume) {
         this.volume = volume;
     }
     public long getVolume() {
         return volume;
     }

    public void setAmount(long amount) {
         this.amount = amount;
     }
     public long getAmount() {
         return amount;
     }

    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    public void setTicktime(String ticktime) {
         this.ticktime = ticktime;
     }
     public String getTicktime() {
         return ticktime;
     }

}