package com.dhu.lottery.service;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.dhu.lottery.enums.GDLotteryType;
import com.dhu.lottery.enums.LotteryType;
import com.dhu.lottery.enums.PicNumType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhu.common.HttpUtil;
import com.dhu.common.util.SpringContextUtil;
import com.dhu.common.util.StringUtil;
import com.dhu.lottery.dao.LotteryRecordDao;
import com.dhu.lottery.model.LotteryMiss;
import com.dhu.lottery.model.LotteryRecord;
import com.dhu.lottery.model.LotteryRule;
import com.dhu.lottery.rule.ILotteryRule;

@Service
public class LotteryRecordService {
    @Autowired
    LotteryRecordDao lotteryRecordDao;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HttpUtil httpUtil;

    private final static Logger logger = LoggerFactory.getLogger(LotteryRecordService.class);


    public String getLotteryMiss() {
        List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecord();
        List<LotteryRule> rules = lotteryRecordDao.getAllRule();
        List<ILotteryRule> ruleList = new ArrayList<>();
        if (rules != null) {
            for (LotteryRule lr : rules) {
                ILotteryRule ruleBean = (ILotteryRule) SpringContextUtil.getBean(lr.getRuleCode());
                if (ruleBean != null) {
                    ruleBean.setLotteryRule(lr);
                    ruleList.add(ruleBean);
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (ILotteryRule ilr : ruleList) {
            if (ilr.isMatch(records)) {
                result.append(ilr.getRuleResult()).append(";");
            }
        }
        if (result.length() > 0) {
            return result.toString();
        }
        return StringUtil.EMPTY;
    }

    public String getLotteryMissByType(LotteryType type) {
        List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecordByType(type.getType());
        List<LotteryRule> rules = lotteryRecordDao.getAllRule();
        List<ILotteryRule> ruleList = new ArrayList<>();
        if (rules != null) {
            for (LotteryRule lr : rules) {
                ILotteryRule ruleBean = (ILotteryRule) SpringContextUtil.getBean(lr.getRuleCode());
                if (ruleBean != null) {
                    ruleBean.setLotteryRule(lr);
                    ruleList.add(ruleBean);
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (ILotteryRule ilr : ruleList) {
            if (ilr.isMatch(records)) {
                result.append(ilr.getRuleResult()).append(";");
            }
        }
        if (result.length() > 0) {
            return type.getDesc()+":"+result.toString();
        }
        return StringUtil.EMPTY;
    }

    public String getNewestLotteryRecord(LotteryType type) {
        List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecordByType(type.getType());
        if(CollectionUtils.isEmpty(records)){
            return StringUtil.EMPTY;
        }
        return records.get(0).getLotteryNo();
    }


    public String getLotteryMissByType(GDLotteryType type) {
        List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecordByType(type.getType());
        List<LotteryRule> rules = lotteryRecordDao.getAllRule();
        List<ILotteryRule> ruleList = new ArrayList<>();
        if (rules != null) {
            for (LotteryRule lr : rules) {
                ILotteryRule ruleBean = (ILotteryRule) SpringContextUtil.getBean(lr.getRuleCode());
                if (ruleBean != null) {
                    ruleBean.setLotteryRule(lr);
                    ruleList.add(ruleBean);
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (ILotteryRule ilr : ruleList) {
            if (ilr.isMatch(records)) {
                result.append(ilr.getRuleResult()).append(";");
            }
        }
        if (result.length() > 0) {
            return type.getDesc()+":"+result.toString();
        }
        return StringUtil.EMPTY;
    }

    public String getNewestLotteryRecord(GDLotteryType type) {
        List<LotteryRecord> records = lotteryRecordDao.getTodayLotteryRecordByType(type.getType());
        if(CollectionUtils.isEmpty(records)){
            return StringUtil.EMPTY;
        }
        return records.get(0).getLotteryNo();
    }

    public List<LotteryRecord> getTodayLotteryRecord() {
        return lotteryRecordDao.getTodayLotteryRecord();
    }

    @Deprecated
    public String insertLotteryRecord() {
        try {

            String result = httpUtil
                    .doGet("http://caipiao.163.com/award/getAwardNumberInfo.html?gameEn=ssc&periodNum=10");
            JSONObject jo = JSONObject.parseObject(result);
            JSONArray awards = jo.getJSONArray("awardNumberInfoList");
            String lotteryNo = StringUtil.EMPTY;
            for (int i = 1; i < awards.size(); i++) {
                JSONObject award = awards.getJSONObject(i);
                LotteryRecord lotteryRecord = new LotteryRecord();
                String lastestPhase = award.getString("period");
                if (lotteryRecordDao.exists(lastestPhase) < 1) {
                    String lastestNumber = award.getString("winningNumber").replaceAll(" ", StringUtil.EMPTY);
                    lotteryRecord.setCreateTime(new Date());
                    lotteryRecord.setLotteryNo(lastestPhase);
                    lotteryRecord.setSequenceOfToday(Integer.parseInt(lastestPhase.substring(6)));
                    lotteryRecord.setNumber(lastestNumber);
                    lotteryRecord.setFirstDigit(lastestNumber.charAt(0) - '0');
                    lotteryRecord.setSecondDigit(lastestNumber.charAt(1) - '0');
                    lotteryRecord.setThirdDigit(lastestNumber.charAt(2) - '0');
                    lotteryRecord.setFourthDigit(lastestNumber.charAt(3) - '0');
                    lotteryRecord.setFifthDigit(lastestNumber.charAt(4) - '0');
                    lotteryRecordDao.insertLotteryRecord(lotteryRecord);
                    lotteryNo = lotteryRecord.getLotteryNo();
                }
            }
            return lotteryNo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtil.EMPTY;
    }


    public static void main(String[] args) {
        String result="\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "      <head>\n" +
                "        <meta charset=\"GB2312\">\n" +
                "        <title>「新疆时时彩开奖结果」新疆时时彩开奖号码-彩经网</title>                     \n" +
                "        <meta name=\"keywords\" content=\"新疆时时彩开奖结果,新疆时时彩开奖信息,新疆时时彩开奖号码\" />\n" +
                "        <meta name=\"Description\" content=\"彩经网新疆时时彩开奖频道提供专业的新疆时时彩数据参考服务，提供新疆时时彩开奖结果，新疆时时彩开奖信息，新疆时时彩开奖号码等服务。\" />\n" +
                "        <meta http-equiv=\"mobile-agent\" content=\"format=xhtml; url=https://m.cjcp.com.cn/kaijiang/xjssc/\" />\n" +
                "        <meta http-equiv=\"mobile-agent\" content=\"format=html5; url=https://m.cjcp.com.cn/kaijiang/xjssc/\" />\n" +
                "        <meta name=\"applicable-device\" content=\"pc\">\n" +
                "        <meta http-equiv=\"Cache-Control\" content=\"no-transform \" />\n" +
                "        <meta http-equiv=\"Cache-Control\" content=\"no-siteapp\" />\n" +
                "        <meta content=\"always\" name=\"referrer\"/>\n" +
                "        <link rel=\"alternate\" type=\"application/vnd.wap.xhtml+xml\" media=\"handheld\" href=\"target\" />\n" +
                "        <link rel=\"alternate\" media=\"only screen and (max-width: 640px)\" href=\"https://m.cjcp.com.cn/kaijiang/xjssc/\" >\n" +
                "        <base href=\"https://www.cjcp.com.cn/\">\n" +
                "        <link href=\"js/kj_js_css/css/new_g.css\" rel=\"stylesheet\" />\n" +
                "        <link href=\"js/kj_js_css/css/new_common.css\" rel=\"stylesheet\" />\n" +
                "        <link href=\"js/kj_js_css/css/new_kj.css\" rel=\"stylesheet\" />\n" +
                "        <link href=\"js/kj_js_css/css/index.css\" rel=\"stylesheet\" />\n" +
                "        <script>\n" +
                "        var _hmt = _hmt || [];\n" +
                "        (function() {\n" +
                "          var hm = document.createElement(\"script\");\n" +
                "          hm.src = \"https://hm.baidu.com/hm.js?78803024be030ae6c48f7d9d0f3b6f03\";\n" +
                "          var s = document.getElementsByTagName(\"script\")[0]; \n" +
                "          s.parentNode.insertBefore(hm, s);\n" +
                "        })();\n" +
                "        </script>\n" +
                "        <script>\n" +
                "        (function(){\n" +
                "            var bp = document.createElement('script');\n" +
                "            var curProtocol = window.location.protocol.split(':')[0];\n" +
                "            if (curProtocol === 'https') {\n" +
                "                bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';\n" +
                "            }\n" +
                "            else {\n" +
                "                bp.src = 'http://push.zhanzhang.baidu.com/push.js';\n" +
                "            }\n" +
                "            var s = document.getElementsByTagName(\"script\")[0];\n" +
                "            s.parentNode.insertBefore(bp, s);\n" +
                "        })();\n" +
                "        </script>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <style>\n" +
                "#topBanners {\n" +
                "width: 980px;\n" +
                "position: relative;\n" +
                "z-index: 2;\n" +
                "}\n" +
                ".ad_con {\n" +
                "height: 65px;\n" +
                "overflow: hidden;\n" +
                "width: 980px;\n" +
                "margin: 0px auto;\n" +
                "}\n" +
                "#topBanners span {\n" +
                "background: url(/images/close.gif);\n" +
                "position: absolute;\n" +
                "right: 4px;\n" +
                "top: 4px;\n" +
                "width: 15px;\n" +
                "height: 13px;\n" +
                "overflow: hidden;\n" +
                "font-size: 1px;\n" +
                "cursor: pointer;\n" +
                "}\n" +
                "</style>\n" +
                "<script src=\"https://www.cjcp.com.cn/script/common.js?v=3\"></script>\n" +
                "<div class=\"content\" id=\"topAd\"><div id=\"topBanner\"><script type=\"text/javascript\">topads_new()</script><span onclick=\"close_banner()\"></span></div></div>\n" +
                "   \n" +
                "<div class=\"top_header vip_top_header\">\n" +
                "    <div class=\"header vip_header\">\n" +
                "        <link href=\"https://www.cjcp.com.cn/css/header.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
                "        <script type=\"text/javascript\" src=\"https://www.cjcp.com.cn/member/loginjs.php\"></script>              \n" +
                "        <div class=\"head_r\">\n" +
                "            <ul> \n" +
                "                <li><a href=\"https://www.cjcp.com.cn/\">首页</a></li>\n" +
                "                <li><a href=\"https://www.cjcp.com.cn/help/\" target=\"_brank\">帮助中心</a></li>\n" +
                "                <li><a href=\"javascript:void(0)\" style=\"cursor:hand\" onclick=\"setcjcpHome('https://www.cjcp.com.cn/')\">设为首页</a></li>                        \n" +
                "                <li style=\"background:none!important;\"><a href=\"https://www.cjcp.com.cn/about/sitemap/\" target=\"_brank\">网站地图</a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<div class=\"content\">                                      \n" +
                "    <div class=\"clear\"></div>\n" +
                "        <div class=\"content jieri\">\n" +
                "            <div class=\"logo\">\n" +
                "                <a title=\"彩经网：双色球，大乐透，福彩3D开奖查询、走势图预测网站\" href=\"http://www.cjcp.com.cn/\"><img width=\"125\" height=\"74\" alt=\"彩经网\" src=\"js/kj_js_css/css/head/images/logo.gif\" /></a>\n" +
                "            </div>\n" +
                "            <div class=\"dh\">\n" +
                "                <div class=\"kh_kf\" onmouseover=\"javascript:$('#headshowmore').attr('style','display:none');\" class=\"hover\">                                    \n" +
                "                    <div class=\"kouhao\"><img width=\"153\" height=\"75\" alt=\"想要中大奖，天天彩经网。\" src=\"js/kj_js_css/css/head/images/gg_img1.jpg\" /></div>\n" +
                "                    <div class=\"search\">\n" +
                "                      <div class=\"sear_bd\">\n" +
                "                            <form  name=\"site_search1\" action=\"//www.cjcp.com.cn/search/\" target=\"_blank\"  onsubmit= \"return searchaa(document.getElementById('q1'))\">\n" +
                "                                <input type=\"text\" class=\"search_txt\" name=\"q1\" id=\"q1\" value=\"请输入专家名称或者文章标题等关键词\" style=\"color:#999;\" onclick=\"if(this.value.replace(/[ ]/g,'')=='请输入专家名称或者文章标题等关键词') {this.value='';this.style['color']='#333'}\"/>\n" +
                "                                <input type=\"submit\" class=\"search_btn\" name=\"btnsea1\" id=\"btnsea1\" value=\"\" />\n" +
                "                            </form>                \n" +
                "                      </div>\n" +
                "                <div class=\"sear_hot\">热门：\n" +
                "                      \n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"telphone\" style=\"background:url(../images/tel_new.jpg) no-repeat 13px 27px!important;background: none!important;margin-top: 25px;padding-bottom: 0!important;\">    \n" +
                "                <p style=\"line-height: 22px;font-size: 18px;font-weight: bold;font-family:宋体;\">\n" +
                "                    Q Q：<a href=\"http://www.cjcp.com.cn/about/contactus/\" style=\"color:#c90200;font-size:14px;text-decoration:underline;\">联系客服</a>\n" +
                "                </p>\n" +
                "                <p style=\"line-height: 22px;font-size: 14px;font-weight: bold;font-family:宋体;\">\n" +
                "                    电话：<a href=\"javascript:void(0)\" style=\"color:#c90200;font-size:14px;\">400-816-8876</a>\n" +
                "                </p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <!--主导航部分start-->\n" +
                "        <div class=\"mainnav\">\n" +
                "             <ul>\n" +
                "                <li onmouseover=\"javascript:$('#headshowmore').attr('style','display:none');\" ><a href=\"http://www.cjcp.com.cn/\">首页</a></li>\n" +
                "                <li class=\"more\" onmouseover=\"javascript:$('#headshowmore').attr('style','display:block');\" ><a class=\"new_tj\" target=\"_blank\" href=\"http://yuce.cjcp.com.cn/\">彩票推荐</a></li>   \n" +
                "                <li onmouseover=\"javascript:$('#headshowmore').attr('style','display:none');\" class=\"hover\"><a  href=\"https://www.cjcp.com.cn/kaijiang/\">开奖大厅</a></li> \n" +
                "                <li><a target=\"_blank\" href=\"https://zst.cjcp.com.cn\">走势图</a></li> \n" +
                "                <li><a target=\"_blank\" href=\"http://tools.cjcp.com.cn/\">彩票工具</a></li> \n" +
                "                <li><a target=\"_blank\" href=\"https://www.cjcp.com.cn/caipao/\">彩炮儿</a></li> \n" +
                "                <li><a target=\"_blank\" href=\"http://www.cjcp.com.cn/special/yh_wapcjcp.html\">手机彩经网</a></li>                 \n" +
                "                <div class=\"clear\"></div>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "        </div>\n" +
                "        <!--主导航部分end-->\n" +
                "       <div class=\"clear\"></div>\n" +
                "        <div class=\"subnav new_content\" style=\"z-index:999;\">  \n" +
                "        <!--首页更多------开始-->\n" +
                "            <div class=\"newIndex_pop\" id='headshowmore'>        \n" +
                "                <ul onclick=\"javascript:$('#headshowmore').attr('style','display:none');\">\n" +
                "                    <li class=\"on\"><a target=\"_blank\" href=\"https://www.cjcp.com.cn/sortchannel/\">专家排行</a></li>\n" +
                "                    <li><a target=\"_blank\" href=\"https://www.cjcp.com.cn/contrast.php?article=sf\">成绩对比</a></li>\n" +
                "                    <li><a target=\"_blank\" href=\"https://www.cjcp.com.cn/expert/2002000000007-11-1-0-0-0-0-1.html\">热门专家</a></li>\n" +
                "                    <li><a target=\"_blank\" href=\"https://www.cjcp.com.cn/sms/2002000000007-13-1-0-0-0-0-1.html\">定制专家</a></li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "         <!--首页更多------结束--> \n" +
                "                <style>\n" +
                "                    .subnav .newSub_nav a{padding:0 5.5px;}\n" +
                "                    .phb_pop{position:absolute;background:#fff;padding:0 10px;width:958px;border:1px solid #d01d00;border-top:none;}\n" +
                "                    .phb_pop a{font-size:12px!important;color:#000!important;padding-right:20px!important;}\n" +
                "                    .phb_pop a.cur{color:#d01d00!important;}\n" +
                "                </style>   \n" +
                "                <script>\n" +
                "                    function changedec(n,sel){\n" +
                "                        for(i=1;i<=n;i++){  \n" +
                "                            var obj=document.getElementById('showdec_'+i); \n" +
                "                            if(i == sel){                      \n" +
                "                                obj.style.display='';\n" +
                "                            }else{ \n" +
                "                                obj.style.display='none';  \n" +
                "                            }  \n" +
                "                        }\n" +
                "                    } \n" +
                "                       \n" +
                "                    var t;  \n" +
                "\n" +
                "                    function doSomethingDiv(){  \n" +
                "                       t = setTimeout('changedec(7,0)',200);  \n" +
                "                    } \n" +
                "                    function clearTime(){  \n" +
                "                       clearTimeout(t);  \n" +
                "                    }\n" +
                "                </script>\n" +
                "                 <div class=\"newSub_nav\" style=\"display:block;\" onmouseover=\"javascript:$('#headshowmore').attr('style','display:none');\">\n" +
                "                <a target=\"_blank\" href=\"https://3d.cjcp.com.cn/\" onmouseover=\"changedec(7,0)\">福彩3D</a>\n" +
                "                <a target=\"_blank\" href=\"https://3d.cjcp.com.cn/shijihao/\" onmouseover=\"changedec(7,0)\">3D试机号</a>\n" +
                "                <a target=\"_blank\" href=\"https://shuangseqiu.cjcp.com.cn/\" onmouseover=\"changedec(7,0)\">双色球</a>\n" +
                "                <a target=\"_blank\" href=\"https://daletou.cjcp.com.cn/\" onmouseover=\"changedec(7,0)\">大乐透</a> \n" +
                "                <a target=\"_blank\" href=\"https://p3.cjcp.com.cn/\" onmouseover=\"changedec(7,0)\">排列三</a>                \n" +
                "                 <a target=\"_blank\" href=\"https://ssc.cjcp.com.cn/p5/\" style=\"position: relative;display: inline-block;\" onmouseover=\"changedec(7,3)\">\n" +
                "                    排列五\n" +
                "                    <span style=\"display: block;position: absolute;top: -16px;right: -15px;z-index: 99999;\">\n" +
                "                    <img src=\"https://ssc.cjcp.com.cn/resource/images/nwanf.png\"></span>\n" +
                "                </a>\n" +
                "                <a target=\"_blank\" href=\"https://qilecai.cjcp.com.cn/\" onmouseover=\"changedec(7,0)\">七乐彩</a>\n" +
                "                <a target=\"_blank\" href=\"https://qixingcai.cjcp.com.cn/\" onmouseover=\"changedec(7,0)\">七星彩</a>  \n" +
                "                <a target=\"_blank\" href=\"https://ssc.cjcp.com.cn/qxc/\" style=\"position: relative;display: inline-block;\" onmouseover=\"changedec(7,4)\">\n" +
                "                    海南4+1<span style=\"display: block;position: absolute;top: -16px;right: -15px;z-index: 99999;\">\n" +
                "                    <img src=\"https://ssc.cjcp.com.cn/resource/images/nwanf.png\"></span>\n" +
                "                </a>\n" +
                "                <a target=\"_blank\" href=\"https://ssc.cjcp.com.cn/cqssc/\" style=\"position: relative;display: inline-block;\" onmouseover=\"changedec(7,1)\">\n" +
                "                    重庆欢乐生肖<span style=\"display: block;position: absolute;top: -16px;right: -15px;z-index: 99999;\">\n" +
                "                    <img src=\"https://ssc.cjcp.com.cn/resource/images/nwanf.png\"></span>\n" +
                "                </a>                                                                                                      \n" +
                "                <a target=\"_blank\" href=\"https://ssc.cjcp.com.cn/xjssc/\" style=\"position: relative;display: inline-block;\" onmouseover=\"changedec(7,2)\">\n" +
                "                    新疆时时彩<span style=\"display: block;position: absolute;top: -16px;right: -15px;z-index: 99999;\">\n" +
                "                    <img src=\"https://ssc.cjcp.com.cn/resource/images/nwanf.png\"></span>\n" +
                "                </a> \n" +
                "                <a target=\"_blank\" href=\"https://shishicai.cjcp.com.cn/\" onmouseover=\"changedec(7,0)\">时时彩</a> \n" +
                "                <a target=\"_blank\" href=\"https://ssc.cjcp.com.cn/gd11x5/\"  style=\"position: relative;display: inline-block;\" onmouseover=\"changedec(7,5)\">\n" +
                "                    11选5<span style=\"display: block;position: absolute;top: -16px;right: -13px;z-index: 99999;\">\n" +
                "                    <img src=\"https://ssc.cjcp.com.cn/resource/images/nwanf.png\"></span>\n" +
                "                </a>\n" +
                "                <a target=\"_blank\" href=\"https://ssc.cjcp.com.cn/hubk3/\" style=\"position: relative;display: inline-block;\" onmouseover=\"changedec(7,7)\">\n" +
                "                快3<span style=\"display: block;position: absolute;top: -16px;right: -22px;z-index: 99999;\">\n" +
                "                    <img src=\"https://ssc.cjcp.com.cn/resource/images/nwanf.png\"></span></a>                 \n" +
                "                <a target=\"_blank\" href=\"https://www.cjcp.com.cn/kl8/\" onmouseover=\"changedec(7,0)\">快乐8</a>\n" +
                "                <a target=\"_blank\" href=\"https://ssc.cjcp.com.cn/beijing/\" style=\"position: relative;display: inline-block;\" onmouseover=\"changedec(7,6)\">\n" +
                "                PK10<span style=\"display: block;position: absolute;top: -16px;right: -15px;z-index: 99999;\">\n" +
                "                    <img src=\"https://ssc.cjcp.com.cn/resource/images/nwanf.png\"></span>\n" +
                "                </a>             \n" +
                "                <a target=\"_blank\" href=\"https://www.cjcp.com.cn/caizhong/\" class=\"new_icons\" onmouseover=\"changedec(7,0)\">更多彩种（41个）</a>\n" +
                "                \n" +
                "                <div class=\"phb_pop\" id=\"showdec_1\" style=\"display:none;\" onmouseout=\"doSomethingDiv();\" onmouseover=\"clearTime();\">\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/cqssc/\" class=\"cur\" target=\"_blank\">重庆时时彩</a>\n" +
                "                    <a href=\"https://zst.cjcp.com.cn/cjwssc/\" target=\"_blank\">走势图</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/contrast/1.html\" target=\"_blank\">成绩对比</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/1_2.html\" target=\"_blank\">计划排行榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/1.html\" target=\"_blank\">成功率榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/1_2_1_5_0_100.html\" target=\"_blank\">连中榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/1_3_1_5_0_100.html\" target=\"_blank\">连错榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/1_4_1_5_0_100.html\" target=\"_blank\">稳定榜</a>                    \n" +
                "                    <a href=\"https://tools.cjcp.com.cn/ssc/?area=cq\" target=\"_blank\">过滤工具</a>\n" +
                "                    <a href=\"https://zst.cjcp.com.cn/yllt/ylltTool/yl-ssc.html\" target=\"_blank\">遗漏分析</a>\n" +
                "                    <a href=\"https://shishicai.cjcp.com.cn/chongqing/jiqiao/\" target=\"_blank\">文章技巧</a>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"phb_pop\" id=\"showdec_2\" style=\"display:none;\" onmouseout=\"doSomethingDiv();\" onmouseover=\"clearTime();\">\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/xjssc/\" class=\"cur\" target=\"_blank\">新疆时时彩</a>\n" +
                "                    <a href=\"https://zst.cjcp.com.cn/cjwssc/index-xjssc.html\" target=\"_blank\">走势图</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/contrast/2.html\" target=\"_blank\">成绩对比</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/2_2.html\" target=\"_blank\">计划排行榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/2.html\" target=\"_blank\">成功率榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/2_2_1_5_0_100.html\" target=\"_blank\">连中榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/2_3_1_5_0_100.html\" target=\"_blank\">连错榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/2_4_1_5_0_100.html\" target=\"_blank\">稳定榜</a>                    \n" +
                "                    <a href=\"https://tools.cjcp.com.cn/ssc/?area=xj\" target=\"_blank\">过滤工具</a>                   \n" +
                "                    <a href=\"https://shishicai.cjcp.com.cn/xinjiang/jiqiao/\" target=\"_blank\">文章技巧</a>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"phb_pop\" id=\"showdec_3\" style=\"display:none;\" onmouseout=\"doSomethingDiv();\" onmouseover=\"clearTime();\">\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/p5/\" class=\"cur\" target=\"_blank\">排列五</a>\n" +
                "                    <a href=\"https://zst.cjcp.com.cn/cjwpl5/\" target=\"_blank\">走势图</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/contrast/7.html\" target=\"_blank\">成绩对比</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/7_2.html\" target=\"_blank\">计划排行榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/7.html\" target=\"_blank\">成功率榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/7_2_1_5_0_100.html\" target=\"_blank\">连中榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/7_3_1_5_0_100.html\" target=\"_blank\">连错榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/7_4_1_5_0_100.html\" target=\"_blank\">稳定榜</a>                    \n" +
                "                    <a href=\"https://tools.cjcp.com.cn/gl/pl5/\" target=\"_blank\">过滤工具</a>\n" +
                "                    <a href=\"https://zst.cjcp.com.cn/yllt/ylltToolPl5/yl.html\" target=\"_blank\">遗漏分析</a> \n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"phb_pop\" id=\"showdec_4\" style=\"display:none;\" onmouseout=\"doSomethingDiv();\" onmouseover=\"clearTime();\">\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/qxc/\" class=\"cur\" target=\"_blank\">海南4+1</a>\n" +
                "                    <a href=\"https://zst.cjcp.com.cn/cjwpl5/\" target=\"_blank\">走势图</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/contrast/6.html\" target=\"_blank\">成绩对比</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/6_2.html\" target=\"_blank\">计划排行榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/6.html\" target=\"_blank\">成功率榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/6_2_1_5_0_100.html\" target=\"_blank\">连中榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/6_3_1_5_0_100.html\" target=\"_blank\">连错榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/6_4_1_5_0_100.html\" target=\"_blank\">稳定榜</a>                    \n" +
                "                    <a href=\"https://zst.cjcp.com.cn/tools/filter/?cId=qxc\" target=\"_blank\">过滤工具</a>\n" +
                "                    <a href=\"https://zst.cjcp.com.cn/yllt/ylltToolQxc/yl.html\" target=\"_blank\">遗漏分析</a> \n" +
                "                    <a href=\"https://qixingcai.cjcp.com.cn/jiqiao/\" target=\"_blank\">文章技巧</a> \n" +
                "                </div>\n" +
                "                \n" +
                "                 <div class=\"phb_pop\" id=\"showdec_5\" style=\"display:none;\" onmouseout=\"doSomethingDiv();\" onmouseover=\"clearTime();\">\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/sd11x5/\" class=\"cur\" target=\"_blank\">山东11选5</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/gd11x5/\" target=\"_blank\">广东11选5</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/jx11x5/\" target=\"_blank\">江西11选5</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/sh11x5/\" target=\"_blank\">上海11选5</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/8.html\" target=\"_blank\">成功率榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/8_2_1_5_0_100_0_1.html\" target=\"_blank\">连中榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/8_3_1_5_0_100_0_1.html\" target=\"_blank\">连错榜</a>\n" +
                "                    <a href=\"https://zst.cjcp.com.cn/gaopin/\" target=\"_blank\">走势图</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/contrast/8.html\" target=\"_blank\">成绩对比</a>\n" +
                "                    <a href=\"https://tools.cjcp.com.cn/11x5/?area=sd\" target=\"_blank\">过滤工具</a>\n" +
                "                    <a href=\"https://11xuan5.cjcp.com.cn/\" target=\"_blank\">更多省份</a>                    \n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"phb_pop\" id=\"showdec_6\" style=\"display:none;\" onmouseout=\"doSomethingDiv();\" onmouseover=\"clearTime();\">\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/beijing/\" class=\"cur\" target=\"_blank\">北京PK10</a>                 \n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/12.html\" target=\"_blank\">成功率榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/12_2_1_9_0_100_0_1.html\" target=\"_blank\">连中榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/12_3_1_9_0_100_0_1.html\" target=\"_blank\">连错榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/12_4_1_9_0_100_0_1.html\" target=\"_blank\">稳定榜</a> \n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/contrast/12.html\" target=\"_blank\">成绩对比</a> \n" +
                "                    <a href=\"https://zst.cjcp.com.cn/cjwpk10/\" target=\"_blank\">走势图</a>                                                                                                                        \n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"phb_pop\" id=\"showdec_7\" style=\"display:none;\" onmouseout=\"doSomethingDiv();\" onmouseover=\"clearTime();\">                                    \n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/hubk3/\" class=\"cur\" target=\"_blank\">湖北快3</a>                 \n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/ahk3/\" target=\"_blank\">安徽快3</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/shk3/\" target=\"_blank\">上海快3</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/jsk3/\" target=\"_blank\">江苏快3</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/hebk3/\" target=\"_blank\">河北快3</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/jlk3/\"  target=\"_blank\">吉林快3</a> \n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/13.html\" target=\"_blank\">成功率榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/13_2_3_321_0_100_0_1.html\" target=\"_blank\">连中榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/13_3_3_321_0_100_0_1.html\" target=\"_blank\">连错榜</a>\n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/13_4_3_321_0_100_0_1.html\" target=\"_blank\">稳定榜</a> \n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/sortselect/13_2.html\" target=\"_blank\">计划榜</a> \n" +
                "                    <a href=\"https://ssc.cjcp.com.cn/expert/contrast/13.html\" target=\"_blank\">成绩对比</a> \n" +
                "                    <a href=\"https://zst.cjcp.com.cn/cjwk3/index-hub.html\" target=\"_blank\">走势图</a>\n" +
                "                </div>\n" +
                "         </div>\n" +
                "               \n" +
                "            <div class=\"clear\"></div>\n" +
                "        </div>\n" +
                "</div>  \n" +
                "\n" +
                "<div class=\"content\">\n" +
                "<script type=\"text/javascript\" src=\"https://m.cjcp.com.cn/apijson/gg.js?value=86787_pc1\"></script>\n" +
                "</div>\n" +
                "    \n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"kj_crumbs\">您的位置：<a target='_blank' href=\"https://www.cjcp.com.cn/\" title=\"彩经网\">彩经网</a> > <a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/\" title=\"彩票开奖\">彩票开奖</a>> <a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/xjssc/\" title=\"新疆时时彩开奖结果\">新疆时时彩开奖结果</a></div> \n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"kj_result\">\n" +
                "                <div class=\"result_l fl\">\n" +
                "                    <div class=\"kj_tit\"><span></span>新疆时时彩开奖结果</div>\n" +
                "                    <div class=\"result_cz\">\n" +
                "                        <dl>\n" +
                "                            <dt class=\"ssc fl\"></dt>\n" +
                "                            <dd>彩经网新疆时时彩开奖结果来源于福彩官方处，提供国内更快更新的福彩新疆时时彩开奖结果查询，更有新疆时时彩历史开奖结果供您分析，让您准时获得新疆时时彩开奖结果。</dd>\n" +
                "                        </dl>\n" +
                "                        <div class=\"clear\"></div>\n" +
                "                    </div>\n" +
                "                    <div class=\"result_con\">\n" +
                "                        <div class=\"result_infol fl\">\n" +
                "                            <div class=\"info_list\">\n" +
                "                                <dl>\n" +
                "                                    <dt>新疆时时彩开奖结果今天</dt>\n" +
                "                                    <dd>\n" +
                "                                        <p>新疆时时彩第20201029038期开奖结果</p>\n" +
                "                                        <div class=\"kj_num\">\n" +
                "                                            <img src='js/kj_js_css/img/201808200329027351.png' />\n" +
                "                              <img src='js/kj_js_css/img/201808200329023852.png' />\n" +
                "                              <img src='js/kj_js_css/img/201808200329024582.png' />\n" +
                "                              <img src='js/kj_js_css/img/201808200329023852.png' />\n" +
                "                              <img src='js/kj_js_css/img/201808200329027351.png' />                                        </div>\n" +
                "                                        <p>开奖时间：2020-10-29 22:39:00</p>\n" +
                "                                        <p><a target='_blank' href=\"https://zst.cjcp.com.cn/cjwssc/index-xjssc.html\">新疆时时彩走势图</a></p>\n" +
                "                                                                                <p><a target='_blank' href=\"http://www.caipiaow.com/index.php?m=Zst&a=index&caizhong=ssc&lot=xjssc\">新疆时时彩任选走势图</a></p> \n" +
                "                                                                            </dd>\n" +
                "                                </dl>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                                                <div class=\"result_infor fr\" style=\"width:390px\">\n" +
                "                            <table>\n" +
                "                                <thead>\n" +
                "                                    <tr>\n" +
                "                                        <th>期号</th>\n" +
                "                                        <th>开奖时间</th>\n" +
                "                                        <th>开奖结果</th>\n" +
                "                                    </tr>\n" +
                "                                </thead>\n" +
                "                                <tbody>\n" +
                "                                    <tr><td>20201029038</td><td>22:39:00</td><td><img src='js/kj_js_css/img/201808200329027351.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329023852.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329024582.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329023852.png' />\n" +
                "                              <img src='js/kj_js_css/img/201808200329027351.png' /></td><tr><tr><td>20201029037</td><td>22:19:00</td><td><img src='js/kj_js_css/img/201808200329029838.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329025833.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329022908.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329021430.png' />\n" +
                "                              <img src='js/kj_js_css/img/201808200329029838.png' /></td><tr><tr><td>20201029036</td><td>21:59:00</td><td><img src='js/kj_js_css/img/201808200329023852.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329028495.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329027351.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329026188.png' />\n" +
                "                              <img src='js/kj_js_css/img/201808200329021430.png' /></td><tr><tr><td>20201029035</td><td>21:39:00</td><td><img src='js/kj_js_css/img/201808200329028495.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329025833.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329029838.png' />\n" +
                "                                  <img src='js/kj_js_css/img/2018082003290210496.png' />\n" +
                "                              <img src='js/kj_js_css/img/201808200329023852.png' /></td><tr><tr><td>20201029034</td><td>21:19:00</td><td><img src='js/kj_js_css/img/201808200329028495.png' />\n" +
                "                                  <img src='js/kj_js_css/img/2018082003290210496.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329028495.png' />\n" +
                "                                  <img src='js/kj_js_css/img/201808200329024582.png' />\n" +
                "                              <img src='js/kj_js_css/img/201808200329029838.png' /></td><tr> \n" +
                "                                </tbody>\n" +
                "                            </table>\n" +
                "                        </div>\n" +
                "                                                <div class=\"clear\"></div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <div class=\"result_r fr\">\n" +
                "                    <div class=\"result_before\">\n" +
                "                        <div class=\"kj_tit\"><span></span>新疆时时彩往期开奖<p style=\"float: right;\"><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/xjssc_1/\">更多&gt;</a></p></div>\n" +
                "                        <div class=\"ul_list3\">\n" +
                "                            <ul>\n" +
                "                                <li><a target='_blank' style='font-size: 12px;' href='https://www.cjcp.com.cn/kaijiang/xjssc20201029038.html'>新疆时时彩20201029038期开奖结果</a><li>  <li><a target='_blank' style='font-size: 12px;' href='https://www.cjcp.com.cn/kaijiang/xjssc20201029037.html'>新疆时时彩20201029037期开奖结果</a><li>  <li><a target='_blank' style='font-size: 12px;' href='https://www.cjcp.com.cn/kaijiang/xjssc20201029036.html'>新疆时时彩20201029036期开奖结果</a><li>  <li><a target='_blank' style='font-size: 12px;' href='https://www.cjcp.com.cn/kaijiang/xjssc20201029035.html'>新疆时时彩20201029035期开奖结果</a><li>  <li><a target='_blank' style='font-size: 12px;' href='https://www.cjcp.com.cn/kaijiang/xjssc20201029034.html'>新疆时时彩20201029034期开奖结果</a><li>  <li><a target='_blank' style='font-size: 12px;' href='https://www.cjcp.com.cn/kaijiang/xjssc20201029033.html'>新疆时时彩20201029033期开奖结果</a><li>  <li><a target='_blank' style='font-size: 12px;' href='https://www.cjcp.com.cn/kaijiang/xjssc20201029032.html'>新疆时时彩20201029032期开奖结果</a><li>  <li><a target='_blank' style='font-size: 12px;' href='https://www.cjcp.com.cn/kaijiang/xjssc20201029031.html'>新疆时时彩20201029031期开奖结果</a><li>  <li><a target='_blank' style='font-size: 12px;' href='https://www.cjcp.com.cn/kaijiang/xjssc20201029030.html'>新疆时时彩20201029030期开奖结果</a><li>  <li><a target='_blank' style='font-size: 12px;' href='https://www.cjcp.com.cn/kaijiang/xjssc20201029029.html'>新疆时时彩20201029029期开奖结果</a><li>                              </ul>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <div class=\"clear\"></div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <script type=\"text/javascript\" src=\"//1.cjcp.cn/source/static/b70f.js?ojb=ikiggf\"></script>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"result_all\">\n" +
                "                                     <iframe src=\"https://ssc.cjcp.com.cn/cqssc/kaijiang.php?lotteryid=2\" scrolling=\"no\" frameborder=\"0\" height=\"530\" width=\"100%\"></iframe>   \n" +
                "                             </div>\n" +
                "            <div class=\"clear\"></div>\n" +
                "            <div class=\"result_all\" style=\"margin-top:10px;\">\n" +
                "                <div class=\"result_common fl\">\n" +
                "                    <div class=\"kj_tit\"><span></span>新疆时时彩彩种技巧</div>\n" +
                "                    <div class=\"ul_list3\">\n" +
                "                        <ul>\n" +
                "                             <li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436949.html' title='排列三2020096期壹灯大师推荐组选复式+组选号码6注'>.排列三2020096期壹灯大师推荐组</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436948.html' title='排列三2020096期千虫今日推荐不定位五码组选'>.排列三2020096期千虫今日推荐不</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436947.html' title='排列三2020096期幸运彩仙本期推荐组选20组'>.排列三2020096期幸运彩仙本期推</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436946.html' title='排列三2020096期于海滨免费推荐杀两码56'>.排列三2020096期于海滨免费推荐</a></li><li><a target='_blank' href='https://3d.cjcp.com.cn/2020/0525/5436945.html' title='3d2020097期紫霞仙子四胆码'>.3d2020097期紫霞仙子四胆码</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436944.html' title='排列三2020096期金生水专家推荐双胆19'>.排列三2020096期金生水专家推荐</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436943.html' title='排列三2020096期公孙策专家胆码推荐'>.排列三2020096期公孙策专家胆码</a></li><li><a target='_blank' href='https://3d.cjcp.com.cn/2020/0525/5436942.html' title='3d2020097期神仙姐姐胆码'>.3d2020097期神仙姐姐胆码</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436941.html' title='排列三2020096期E彩通本期精选16组'>.排列三2020096期E彩通本期精选</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436940.html' title='排列三2020096期诸葛推荐组选20组'>.排列三2020096期诸葛推荐组选2</a></li>                        </ul>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <div class=\"result_common fl\" style=\"margin-left:10px;\">\n" +
                "                    <div class=\"kj_tit\"><span></span>新疆时时彩彩种资讯</div>\n" +
                "                    <div class=\"ul_list3\">\n" +
                "                        <ul>\n" +
                "                            <li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436949.html' title='排列三2020096期壹灯大师推荐组选复式+组选号码6注'>.排列三2020096期壹灯大师推荐组</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436948.html' title='排列三2020096期千虫今日推荐不定位五码组选'>.排列三2020096期千虫今日推荐不</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436947.html' title='排列三2020096期幸运彩仙本期推荐组选20组'>.排列三2020096期幸运彩仙本期推</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436946.html' title='排列三2020096期于海滨免费推荐杀两码56'>.排列三2020096期于海滨免费推荐</a></li><li><a target='_blank' href='https://3d.cjcp.com.cn/2020/0525/5436945.html' title='3d2020097期紫霞仙子四胆码'>.3d2020097期紫霞仙子四胆码</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436944.html' title='排列三2020096期金生水专家推荐双胆19'>.排列三2020096期金生水专家推荐</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436943.html' title='排列三2020096期公孙策专家胆码推荐'>.排列三2020096期公孙策专家胆码</a></li><li><a target='_blank' href='https://3d.cjcp.com.cn/2020/0525/5436942.html' title='3d2020097期神仙姐姐胆码'>.3d2020097期神仙姐姐胆码</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436941.html' title='排列三2020096期E彩通本期精选16组'>.排列三2020096期E彩通本期精选</a></li><li><a target='_blank' href='https://p3.cjcp.com.cn/2020/0525/5436940.html' title='排列三2020096期诸葛推荐组选20组'>.排列三2020096期诸葛推荐组选2</a></li>                        </ul>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <div class=\"result_common fr\">\n" +
                "                    <div class=\"kj_tit\"><span></span>新疆时时彩问答</div>\n" +
                "                    <div class=\"ul_list3\">\n" +
                "                        <ul>\n" +
                "                            <li><a target='_blank' href='https://www.cjcp.com.cn/wenda/8_4544.html' title='中国福利彩票“快乐8”游戏规则'>.中国福利彩票“快乐8”游戏规则</a></li><li><a target='_blank' href='https://www.cjcp.com.cn/wenda/8_11318.html' title='中国福利彩票“快乐8”投注指南'>.中国福利彩票“快乐8”投注指南</a></li><li><a target='_blank' href='https://www.cjcp.com.cn/wenda/8_11319.html' title='中国福利彩票“快乐8”游戏介绍'>.中国福利彩票“快乐8”游戏介绍</a></li><li><a target='_blank' href='https://www.cjcp.com.cn/wenda/8_11314.html' title='福彩新快乐8上市'>.福彩新快乐8上市</a></li><li><a target='_blank' href='https://www.cjcp.com.cn/wenda/8_11323.html' title='福彩快乐8游戏10月28日13地同步上市'>.福彩快乐8游戏10月28日13地同步</a></li><li><a target='_blank' href='https://www.cjcp.com.cn/wenda/8_11317.html' title='新福彩快乐8上市时间'>.新福彩快乐8上市时间</a></li><li><a target='_blank' href='https://www.cjcp.com.cn/wenda/8_11320.html' title='新快乐8游戏上市'>.新快乐8游戏上市</a></li><li><a target='_blank' href='https://www.cjcp.com.cn/wenda/8_11321.html' title='福彩快乐8游戏什么时候上市'>.福彩快乐8游戏什么时候上市</a></li><li><a target='_blank' href='https://www.cjcp.com.cn/wenda/8_11322.html' title='体育总局彩票中心关于2020年体育彩票市场国庆节休市的公告'>.体育总局彩票中心关于2020年体育彩</a></li><li><a target='_blank' href='https://www.cjcp.com.cn/wenda/8_11332.html' title='关于2020年国庆节彩票市场休市放假安排的公告！'>.关于2020年国庆节彩票市场休市放假</a></li>                        </ul>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"clear\"></div>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"result_zst\">\n" +
                "                <ul>\n" +
                "                    <li>\n" +
                "                        <dl>\n" +
                "                            <dt class=\"fl\">双色球走势图</dt>\n" +
                "                            <dd>\n" +
                "                                <p><a target='_blank' href=\"https://zst.cjcp.com.cn/cjwssq/view/ssqlanqiuzonghe.html\">双色球蓝球综合走势图</a><a target='_blank' href=\"https://zst.cjcp.com.cn/cjwssq/view/ssqzonghe.html\">双色球红蓝综合走势图</a><a target='_blank' href=\"https://zst.cjcp.com.cn/cjwssq/view/ssqhongyiwei.html\">双色球定位走势图</a><a target='_blank' href=\"https://zst.cjcp.com.cn/cjwssq/view/ssqhezhi.html\">双色球和值走势图</a></p>\n" +
                "                                <p class=\"mt15\"><a target='_blank' href=\"https://zst.cjcp.com.cn/cjwssq/view/ssqsanfq.html\">双色球分区走势图</a><a target='_blank' href=\"https://zst.cjcp.com.cn/cjwssq/view/ssqlanqiudinglan.html\">双色球蓝球两数和走势图</a><a target='_blank' href=\"https://zst.cjcp.com.cn/cjwssq/view/ssqdaxiao.html\">双色球形态走势图</a><a target='_blank' href=\"https://zst.cjcp.com.cn/cjwssq/view/ssqhongerzhi1.html\">双色球一二位和值走势图</a></p>\n" +
                "                            </dd>\n" +
                "                        </dl>\n" +
                "                    </li>\n" +
                "                    <li>\n" +
                "                        <dl>\n" +
                "                            <dt class=\"fl\">全国彩种开奖</dt>\n" +
                "                            <dd>\n" +
                "                                <p><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/3d/\">福彩3D开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/pl3/\">排列三开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/pl5/\">排列五开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/ssq/\">双色球开奖结果</a></p>\n" +
                "                                <p class=\"mt15\"><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/dlt/\">大乐透开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/qlc/\">七乐彩开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/qxc/\">七星彩开奖结果</a></p>\n" +
                "                            </dd>\n" +
                "                        </dl>\n" +
                "                    </li>\n" +
                "                    <li>\n" +
                "                        <dl>\n" +
                "                            <dt class=\"fl\">地方彩种开奖</dt>\n" +
                "                            <dd>\n" +
                "                                <p><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/hd15x5/\">华东15x5开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/df6j1/\">东方6+1开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/hn41/\">海南4+1开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/yzfcpl5/\">河北排列五开奖结果</a></p>\n" +
                "                                <p class=\"mt15\"><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/yzfcpl7/\">河北排列七开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/xj35x7/\">新疆35选7开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/xj18x7/\">新疆18选7开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/xj25x7/\">新疆25选7开奖结果</a></p>\n" +
                "                            </dd>\n" +
                "                        </dl>\n" +
                "                    </li>\n" +
                "                    <li>\n" +
                "                        <dl>\n" +
                "                            <dt class=\"fl\">高频彩种开奖</dt>\n" +
                "                            <dd>\n" +
                "                                <p><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/cqssc/\">重庆时时彩开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/xjssc/\">新疆时时彩开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/tianjinssc/\">天津时时彩开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/jx11x5/\">江西11x5开奖结果</a></p>\n" +
                "                                <p class=\"mt15\"><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/11ydj/\">山东11运夺金开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/gd11x5/\">广东11x5开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/bjk3/\">北京快三开奖结果</a><a target='_blank' href=\"https://www.cjcp.com.cn/kaijiang/cqklsf/\">重庆快乐十分开奖结果</a></p>\n" +
                "                            </dd>\n" +
                "                        </dl>\n" +
                "                    </li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "<div class=\"footer\">\n" +
                "    <div class=\"footbg\">\n" +
                "    <a target=\"_blank\" href=\"http://www.cjcp.com.cn/about/aboutus/\">关于我们</a> | <a target=\"_blank\" href=\"http://www.cjcp.com.cn/about/contactus/\">联系我们</a> | <a target=\"_blank\" href=\"http://www.cjcp.com.cn/about/pay/\">汇款方式</a> | <a target=\"_blank\" href=\"http://www.cjcp.com.cn/about/joinus/\">诚聘英才</a> | <a target=\"_blank\" href=\"http://www.cjcp.com.cn/about/copyright/\">免责条款</a> | <a target=\"_blank\" href=\"guestbook/\">签写留言</a> | <a target=\"_blank\" href=\"about/business/\">商务合作</a> | <a target=\"_blank\" href=\"http://www.cjcp.com.cn/about/link/\">友情链接</a> | <a target=\"_blank\" href=\"https://www.cjcp.com.cn/wenda/\">彩票问答</a></div>\n" +
                "    <div class=\"blank10\"></div>\n" +
                "    <div class=\"foot_img\">\n" +
                "        <img src=\"js/kj_js_css/images/foot1.gif\" alt=\"网络110报警平台\">\n" +
                "        <img src=\"js/kj_js_css/images/foot2.gif\" alt=\"信息安全服务资质\">\n" +
                "        <img src=\"js/kj_js_css/images/foot3.gif\" alt=\"公安部认证\">\n" +
                "        <img src=\"js/kj_js_css/images/foot4.gif\" alt=\"北京互联网违法和不良信息举报\">\n" +
                "        <img src=\"js/kj_js_css/images/foot5.gif\" alt=\"IAPP成员隐私权专家国际协会\">\n" +
                "    </div>\n" +
                "    <div class=\"foot_txt\">Copyright &copy; 2010 cjcp.com.cn All Rights Reserved. 北京彩经网科技发展有限公司 版权所有<br>\n" +
                "    《中华人民共和国电信与信息服务业务经营许可证》编号：<a href=\"http://www.miibeian.gov.cn\">京ICP备13009488号-2；ICP证：京B2-20170534；</a>增值电信业务经营许可证：京B2-20170534<br />\n" +
                "     电话：4008-168-876/010-64913261,传真:010-64821971  \n" +
                "    &nbsp;</div>\n" +
                "</div>\n" +
                "<script language=\"JavaScript\" type=\"text/javascript\" src=\"https://www.cjcp.com.cn/kaijiang/js/jquery-1.8.3.min.js \"></script>\n" +
                "<script language=\"JavaScript\" type=\"text/javascript\" src=\"https://www.cjcp.com.cn/kaijiang/js/header.js \"></script>\n" +
                "\n" +
                "  \n" +
                "        <script language=\"javascript\" type=\"text/javascript\"  src=\"js/kj_js_css/css/head/js/tui.js\"></script> \n" +
                "    </body>\n" +
                "</html>\n";
        Document doc = Jsoup.parse(result);
        String lotteryNo = StringUtil.EMPTY;

        Elements kjjg_table = doc.getElementsByClass("result_infor fr");
        Element table = kjjg_table.first();
        Elements trs = table.select("tr");

        for (int i = 0; i < trs.size(); i++) {
            String lastestPhase = "";
            String lastestNumber = "";
            Element tr = trs.get(i);
            Elements tds = tr.select("td");
            if(CollectionUtils.isEmpty(tds)){
                continue;
            }
            lastestPhase = tds.get(0).text().substring(2, 11);

            Elements numbers =  tds.get(2).select("img");;
            for (int j = 0; j < numbers.size(); ++j) {
                Element td = numbers.get(j);
                lastestNumber += PicNumType.getNumByPicUrl( td.attributes().get("src"));
            }

            LotteryRecord lotteryRecord = new LotteryRecord();


        }
    }

    public String insertLotteryRecordV3() {
        try {
            String result = httpUtil
                    .doGet("https://shishicai.cjcp.com.cn/chongqing/kaijiang");
            Document doc = Jsoup.parse(result);
            String lotteryNo = StringUtil.EMPTY;
            Elements kjjg_table = doc.getElementsByClass("kjjg_table");
            Element table = kjjg_table.first();
            Elements trs = table.select("tr");

            for (int i = 0; i < trs.size(); i++) {
                String lastestPhase = "";
                String lastestNumber = "";
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                if (tds.get(0).hasClass("hui")) {
                    continue;
                }
                lastestPhase = tds.get(0).text().substring(2, 11);
                Elements numbers = tds.get(2).getElementsByClass("hm_bg");
                for (int j = 0; j < numbers.size(); ++j) {
                    Element td = numbers.get(j);
                    lastestNumber += td.text();
                }

                LotteryRecord lotteryRecord = new LotteryRecord();
                if (lotteryRecordDao.exists(lastestPhase) < 1) {
                    lotteryRecord.setCreateTime(new Date());
                    lotteryRecord.setLotteryNo(lastestPhase);
                    lotteryRecord.setSequenceOfToday(Integer.parseInt(lastestPhase.substring(6)));
                    lotteryRecord.setNumber(lastestNumber);
                    lotteryRecord.setFirstDigit(lastestNumber.charAt(0) - '0');
                    lotteryRecord.setSecondDigit(lastestNumber.charAt(1) - '0');
                    lotteryRecord.setThirdDigit(lastestNumber.charAt(2) - '0');
                    lotteryRecord.setFourthDigit(lastestNumber.charAt(3) - '0');
                    lotteryRecord.setFifthDigit(lastestNumber.charAt(4) - '0');
                    lotteryRecordDao.insertLotteryRecord(lotteryRecord);
                    lotteryNo = lotteryRecord.getLotteryNo();
                }

            }
            return lotteryNo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtil.EMPTY;
    }

    public String insertLotteryRecordByType(LotteryType lotteryType) {
        
        try {
            String result = httpUtil
                    .doGet(lotteryType.getUrl());
            Document doc = Jsoup.parse(result);
            String lotteryNo = StringUtil.EMPTY;

            Elements kjjg_table = doc.getElementsByClass("result_infor fr");
            Element table = kjjg_table.first();
            Elements trs = table.select("tr");

            for (int i = 0; i < trs.size(); i++) {
                String lastestPhase = "";
                String lastestNumber = "";
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                if(CollectionUtils.isEmpty(tds)){
                    continue;
                }
                lastestPhase = tds.get(0).text().substring(2, 11);

                Elements numbers = tds.get(2).select("img");
                for (int j = 0; j < numbers.size(); ++j) {
                    Element td = numbers.get(j);
                    lastestNumber += PicNumType.getNumByPicUrl( td.attributes().get("src"));
                }

                LotteryRecord lotteryRecord = new LotteryRecord();
                Map param=new HashMap();
                param.put("lotteryNo",lastestPhase);
                param.put("type",lotteryType.getType());
                if (lotteryRecordDao.existsV2(param) < 1) {
                    lotteryRecord.setCreateTime(new Date());
                    lotteryRecord.setLotteryNo(lastestPhase);

                    lotteryRecord.setSequenceOfToday(Integer.parseInt(lastestPhase.substring(6)));
                    lotteryRecord.setNumber(lastestNumber);
                    lotteryRecord.setFirstDigit(lastestNumber.charAt(0) - '0');
                    lotteryRecord.setSecondDigit(lastestNumber.charAt(1) - '0');
                    lotteryRecord.setThirdDigit(lastestNumber.charAt(2) - '0');
                    lotteryRecord.setFourthDigit(lastestNumber.charAt(3) - '0');
                    lotteryRecord.setFifthDigit(lastestNumber.charAt(4) - '0');
                    lotteryRecord.setType(lotteryType.getType());
                    lotteryRecordDao.insertLotteryRecordV2(lotteryRecord);
                    lotteryNo = lotteryRecord.getLotteryNo();
                }

            }
            return lotteryNo;
        } catch (Exception e) {
            logger.error("获取号码失败"+e.getMessage(),e);
        }
        return StringUtil.EMPTY;
    }

    public String insertHeilongjiangLotteryRecordByType(LotteryType lotteryType) {
        try {
            String result = httpUtil
                    .doGet(lotteryType.getUrl());
            Document doc = Jsoup.parse(result);
            String lotteryNo = StringUtil.EMPTY;
            Elements kjjg_table = doc.getElementsByClass("kjjg_table");
            Element table = kjjg_table.first();
            Elements trs = table.select("tr");

            for (int i = 0; i < trs.size(); i++) {
                String lastestPhase = "";
                String lastestNumber = "";
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                if (tds.get(0).hasClass("hui")) {
                    continue;
                }
                lastestPhase = tds.get(0).text().substring(0, 6);
                Elements numbers = tds.get(2).getElementsByClass("hm_bg");
                for (int j = 0; j < numbers.size(); ++j) {
                    Element td = numbers.get(j);
                    lastestNumber += td.text();
                }

                LotteryRecord lotteryRecord = new LotteryRecord();
                Map param=new HashMap();
                param.put("lotteryNo",lastestPhase);
                param.put("type",lotteryType.getType());
                if (lotteryRecordDao.existsV2(param) < 1) {
                    lotteryRecord.setCreateTime(new Date());
                    lotteryRecord.setLotteryNo(lastestPhase);

                    lotteryRecord.setSequenceOfToday(Integer.parseInt(lastestPhase));
                    lotteryRecord.setNumber(lastestNumber);
                    lotteryRecord.setFirstDigit(lastestNumber.charAt(0) - '0');
                    lotteryRecord.setSecondDigit(lastestNumber.charAt(1) - '0');
                    lotteryRecord.setThirdDigit(lastestNumber.charAt(2) - '0');
                    lotteryRecord.setFourthDigit(lastestNumber.charAt(3) - '0');
                    lotteryRecord.setFifthDigit(lastestNumber.charAt(4) - '0');
                    lotteryRecord.setType(lotteryType.getType());
                    lotteryRecordDao.insertLotteryRecordV2(lotteryRecord);
                    lotteryNo = lotteryRecord.getLotteryNo();
                }

            }
            return lotteryNo;
        } catch (Exception e) {
            logger.error("获取号码失败"+e.getMessage(),e);
        }
        return StringUtil.EMPTY;
    }



    public List<LotteryMiss> getAllLotteryMiss() {
        return lotteryRecordDao.getAllLotteryMiss();
    }

    public void insertLotteryMiss(LotteryMiss lm) {
        lotteryRecordDao.insertLotteryMiss(lm);
    }

    public void insertGDLotteryRecord(String lotteryNo, String digits,int lotteryType) {

        GDLotteryType gdLotteryType=  GDLotteryType.getGdTypeByValue(lotteryType);
        if(GDLotteryType.DEFAULT.equals(gdLotteryType)){
            return;
        }
        LotteryRecord lotteryRecord = new LotteryRecord();
        Map param=new HashMap();
        param.put("lotteryNo",lotteryNo);
        param.put("type",lotteryType);
        if (lotteryRecordDao.existsV2(param) < 1) {
            lotteryRecord.setCreateTime(new Date());
            lotteryRecord.setLotteryNo(lotteryNo);

            lotteryRecord.setSequenceOfToday((int)(new BigDecimal(lotteryNo).longValue()%1000));
            lotteryRecord.setNumber(digits);
            lotteryRecord.setFirstDigit(digits.charAt(0) - '0');
            lotteryRecord.setSecondDigit(digits.charAt(1) - '0');
            lotteryRecord.setThirdDigit(digits.charAt(2) - '0');
            lotteryRecord.setFourthDigit(digits.charAt(3) - '0');
            lotteryRecord.setFifthDigit(digits.charAt(4) - '0');
            lotteryRecord.setType(lotteryType);
            lotteryRecordDao.insertLotteryRecordV2(lotteryRecord);
            logger.info(JSON.toJSONString(lotteryRecord));

            String result = getLotteryMissByType(gdLotteryType);
            String lastestLottery=getNewestLotteryRecord(gdLotteryType);
            if (StringUtil.isNotEmpty(result)) {
                LotteryMiss lm = new LotteryMiss();
                lm.setLotteryNo(lastestLottery);
                lm.setStatus(1);
                lm.setMsg(result);
                lm.setType(gdLotteryType.getType());
                insertLotteryMiss(lm);
                logger.info("发送邮件！！！");
                System.out.println("发送邮件！！！");
            }
        }
    }


    public int updateLotteryMiss() {
        return lotteryRecordDao.updateLotteryMiss();
    }
}
