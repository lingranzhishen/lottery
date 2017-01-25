<%@ include file="../common/includes.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/pages/common/header.jsp">
	<jsp:param name="title" value="权限获取失败" />
</jsp:include>

	<!--begin: 正文-->
    <div class="OA_container">
        <div class="OA_module mt_20 pt_20 pd_20 ">
            <div class="mt_20 pt_20" style="height:380px;width:600px;margin:0 auto;background: url(/static/public/images/errorPageIcon.png) no-repeat center bottom;">
                <h1 style="font-size:72px;">Ooops</h1>
            </div>
			<p class="fs_16 grey333 mt_20 mb_20 pb_20 green ta_c">不好意思，您没有权限查看该页面
				<a class="OA_btn_normal OA_btn_green_fill ml_10 mr_10" href="/">回首页</a></p>
		</div>
	</div>
	<!--end: 正文-->

<!-- footer begin -->
<%@include file="/pages/common/footer.jsp"%>
