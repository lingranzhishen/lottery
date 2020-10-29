package com.dhu.lottery.enums;

/**
 * 标签枚举
 * 
 * @author Fant
 */
public enum LotteryType {

	CQ_SSC(1,"https://www.cjcp.com.cn/kaijiang/cqssc/","重庆时时彩"),
    XJ_SSC(2,"https://www.cjcp.com.cn/kaijiang/xjssc/","新疆时时彩"),
    TJ_SSC(3,"https://www.cjcp.com.cn/kaijiang/tjssc/","天津时时彩"),
//    HLJ_SSC(4,"https://shishicai.cjcp.com.cn/heilongjiang/kaijiang/","黑龙江时时彩"),
    YN_SSC(5,"https://www.cjcp.com.cn/kaijiang/ynssc/","云南时时彩"),

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
