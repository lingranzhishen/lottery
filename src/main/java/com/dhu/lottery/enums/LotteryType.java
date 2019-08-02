package com.dhu.lottery.enums;

/**
 * 标签枚举
 * 
 * @author Fant
 */
public enum LotteryType {

	CQ_SSC(1,"https://shishicai.cjcp.com.cn/chongqing/kaijiang/","重庆时时彩"),
    XJ_SSC(2,"https://shishicai.cjcp.com.cn/xinjiang/kaijiang/","新疆时时彩"),
    TJ_SSC(3,"https://shishicai.cjcp.com.cn/tianjin/kaijiang/","天津时时彩"),
    JX_SSC(4,"https://shishicai.cjcp.com.cn/jiangxi/kaijiang/","江西时时彩"),
    ;

    LotteryType(int type, String url, String desc) {
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
}
