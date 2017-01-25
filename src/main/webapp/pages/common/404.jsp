<%@ include file="../common/includes.jsp" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/pages/common/header.jsp">
	<jsp:param name="title" value="页面没有找到" />
	<jsp:param name="keywords" value="${config.cityName}二手房,${config.cityName}二手房出售,${config.cityName}二手房买卖,${config.cityName}二手房交易,${config.cityName}二手房价格,${config.cityName}二手房信息。"/>
	<jsp:param name="description" value="链家${config.cityName}二手房网在${config.cityName}二手房市场用户口碑极佳，为${config.cityName}二手房用户使用率和满意度极好的网络二手房交易及信息平台，同时也是${config.cityName}二手房用户重点推荐使用的网站。链家${config.cityName}二手房网提供${config.cityName}二手房最新房源，房源真实可靠无虚假无重复，让每一个用户安心满意的进行${config.cityName}二手房交易，营造最值得用户信赖的${config.cityName}二手房交易平台。"/>
	<jsp:param name="pageType" value="ershoufang"/>
	<jsp:param name="pageName" value="二手房"/>
	<jsp:param name="pageTips" value="请输入区域、板块或小区名开始找房"/>
</jsp:include>

<!--begin: 正文-->
<div class="errorPage">
	<div class="errorWrap">
		<h1>404</h1>
	</div>
	<p class="errorMessageInfo">
		您所要查找的页面找不到了，稍安勿躁
		<a href="/" class="btn_goHome ml_20">回首页</a>
	</p>
</div>
<!--end: 正文-->
<script>
	var pagePrefix = "/ershoufang";
</script>
<!-- footer begin -->
<%@include file="/pages/common/footer.jsp"%>
