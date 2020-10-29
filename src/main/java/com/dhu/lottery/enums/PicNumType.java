package com.dhu.lottery.enums;

/**
 * 数字图片标签枚举
 * 
 * @author Fant
 */
public enum PicNumType {

	NUM_0(0,"https://www.cjcp.com.cn/js/kj_js_css/img/201808200329021430.png","重庆时时彩"),
    NUM_1(1,"https://www.cjcp.com.cn/js/kj_js_css/img/201808200329022908.png","重庆时时彩"),
    NUM_2(2,"https://www.cjcp.com.cn/js/kj_js_css/img/201808200329023852.png","重庆时时彩"),
    NUM_3(3,"https://www.cjcp.com.cn/js/kj_js_css/img/201808200329024582.png","重庆时时彩"),
    NUM_4(4,"https://www.cjcp.com.cn/js/kj_js_css/img/201808200329025833.png","重庆时时彩"),
    NUM_5(5,"https://www.cjcp.com.cn/js/kj_js_css/img/201808200329026188.png","重庆时时彩"),
    NUM_6(6,"https://www.cjcp.com.cn/js/kj_js_css/img/201808200329027351.png","重庆时时彩"),
    NUM_7(7,"https://www.cjcp.com.cn/js/kj_js_css/img/201808200329028495.png","重庆时时彩"),
    NUM_8(8,"https://www.cjcp.com.cn/js/kj_js_css/img/201808200329029838.png","重庆时时彩"),
    NUM_9(9,"https://www.cjcp.com.cn/js/kj_js_css/img/2018082003290210496.png","重庆时时彩"),


    ;

    PicNumType(int type, String url, String desc) {
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


    public static int getNumByPicUrl(String picUrl){
        for(PicNumType picNumType:values()){
            if(picNumType.getUrl().contains(picUrl)){
                return picNumType.getType();
            }
        }
        return 0;
    }
}
