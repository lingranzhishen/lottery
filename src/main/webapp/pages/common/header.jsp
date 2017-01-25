<%@ include file="includes.jsp" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<meta http-equiv="Cache-Control" content="no-transform" />
		<meta http-equiv="Cache-Control" content="no-siteapp" />
		<meta name="format-detection" content="telephone=no" />
		<link href="${config.statichost}/static/img/favicon.ico" type="image/x-icon" rel=icon>
        <link href="${config.statichost}/static/img/favicon.ico" type="image/x-icon" rel="shortcut icon">
		<title>${param.title}</title>
		<meta name="keywords" content="${param.keywords}" />
		<meta name="description" content="${param.description}" />
		<!--[if lt IE 9]>
		<script type="text/javascript" src="${config.statichost}/public/js/html5.js"></script>
		<![endif]-->
		<script>
			var headerParameters = {
				env : '${config.env}',
				statichost : '${config.statichost}',
				cityCode : '${config.cityCode}',
				cityName : '${config.cityName}',
			}
		</script>

		<script src="${config.statichost}/static/js/jquery/jquery.min.js"></script>
		<script src="${config.statichost}/static/js/jquery/jquery.easyui.min.js"></script>
		<script src="${config.statichost}/static/js/jquery/easyloader.js"></script>
		<script src="${config.statichost}/static/js/jquery/jquery.serialize-object.min.js"></script>
		
		<script src="${config.statichost}/static/js/common.js?v=${config.version}"></script>
		
	<link rel="stylesheet" type="text/css" href="${config.statichost}/static/js/jquery/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${config.statichost}/static/js/jquery/themes/icon.css">
	</head>

	<body>
	