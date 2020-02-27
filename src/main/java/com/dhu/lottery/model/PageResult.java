/**
  * Copyright 2020 bejson.com 
  */
package com.dhu.lottery.model;
import java.util.List;

/**
 * Auto-generated: 2020-02-27 22:31:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PageResult{


    private Integer totalCount;
    private Integer page;
    private Integer num;
    private List<StockInfo> data;
    public void setTotalCount(Integer totalCount) {
         this.totalCount = totalCount;
     }
     public Integer getTotalCount() {
         return totalCount;
     }

    public void setPage(Integer page) {
         this.page = page;
     }
     public Integer getPage() {
         return page;
     }

    public void setNum(Integer num) {
         this.num = num;
     }
     public Integer getNum() {
         return num;
     }

    public void setData(List<StockInfo> data) {
         this.data = data;
     }
     public List<StockInfo> getData() {
         return data;
     }

}