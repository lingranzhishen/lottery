package com.dhu.lottery.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.dhu.lottery.service.StockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dhu.common.JsonResult;
import com.dhu.common.base.BaseController;
import com.dhu.framework.utils.MailUtil;
import com.dhu.lottery.service.LotteryRecordService;
import com.dhu.portal.service.LoginService;

@Controller
@RequestMapping(value = "/")
public class IndexController extends BaseController {

	@Autowired
	LoginService loginService;
	@Autowired
	MailUtil mailUtil;
	@Autowired
	LotteryRecordService lotteryRecordService;
	@Autowired
	StockRecordService stockRecordService;
	@RequestMapping(value = "")
	public String index(HttpServletResponse response) {
		return "index";
	}
	
	@RequestMapping(value = "preProduct")
	public String preProduct(HttpServletResponse response) {
		return "preProduct";
	}
	@RequestMapping(value = "product")
	public String product(HttpServletResponse response) {
		return "product";
	}

	@ResponseBody
	@RequestMapping(value = "/testMail.json")
	public JsonResult index(String msg,String email) {
		try {
			mailUtil.sendTextMail(msg,email);
		} catch (MessagingException e) {
			log.info("发送失败", e);
		}
		return ok();
	}
	@ResponseBody
	@RequestMapping(value = "/lastLottery.json")
	public JsonResult lastLottery() {
		JsonResult result=ok();
		result.put("record", lotteryRecordService.getTodayLotteryRecord());
		return result;
	}


	@ResponseBody
	@RequestMapping(value = "/insertLottery.json")
	public JsonResult insertLottery(String lotteryNo,String digits,int type) {
		JsonResult result=ok();
		lotteryRecordService.insertGDLotteryRecord(lotteryNo,digits,type);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/testStock.json")
	public JsonResult testStock() {
		JsonResult result=ok();
		stockRecordService.insertStockInfo();
		return result;
	}
}
