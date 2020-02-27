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
public class ResultDTO {

    private int error_code;
    private String reason;
    private PageResult result;
    public void setError_code(int error_code) {
         this.error_code = error_code;
     }
     public int getError_code() {
         return error_code;
     }

    public void setReason(String reason) {
         this.reason = reason;
     }
     public String getReason() {
         return reason;
     }

    public void setResult(PageResult result) {
         this.result = result;
     }
     public PageResult getResult() {
         return result;
     }

}