package com.dhu.lottery.enums;

/**
 * 高德标签枚举
 * 
 * @author Fant
 */
public enum GDLotteryType {
    /**
     *                          <option class="LotteryGameID" value="1">重庆时时彩</option>
     *                                 <option class="LotteryGameID" value="25">云南时时彩</option>
     *                                 <option class="LotteryGameID" value="128">腾讯十分彩</option>
     *                                 <option class="LotteryGameID" value="145">河内时时彩</option>
     */
    ssc_128(128,"https://s.gdbet999.com/DrawHistory/Trend/128?issue=120&day=0#history","腾讯十分彩"),
    ssc_145(145,"https://s.gdbet999.com/DrawHistory/Trend/145?issue=120&day=0#history","河内时时彩"),
    ssc_129(129,"https://s.gdbet999.com/DrawHistory/Trend/129?issue=120&day=0#history","河内五分彩"),
    ssc_127(127,"https://s.gdbet999.com/DrawHistory/Trend/127?issue=120&day=0#history","腾讯五分彩"),

    DEFAULT(-1,"","非法类型"),

    ;

    GDLotteryType(int type, String url, String desc) {
        this.type = type;
        this.url = url;
        this.desc = desc;
    }

    int type;
	String url;
	String desc;

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getDesc() {
        return desc;
    }

    public static GDLotteryType  getGdTypeByValue(int type){
        for(GDLotteryType gdLotteryType:values()){
            if(gdLotteryType.type==type){
                return gdLotteryType;
            }
        }
        return DEFAULT;
    }
}
