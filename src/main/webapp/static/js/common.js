/*********************************
 底层方法
 **********************************/
//处理ie8下不小心写了console时报错的情况
window.console = window.console || (function(){
    var c = {}; c.log = c.warn = c.debug = c.info = c.error = c.time = c.dir = c.profile
        = c.clear = c.exception = c.trace = c.assert = function(){};
    return c;
})();

//判断浏览器
function checkBrowserIsLegal(){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器

    var legalBrowser = true;

    if (isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);

        legalBrowser = fIEVersion > 8.0;//如果小于版本ie8
    }

    return legalBrowser;
}

/***公共的需要调用的对于localstorage的处理***/
var localStorageFunObj = {
    getLocalStorage:function(name){//获取localstorage的某个值
        try{
            var localstorageInfo = window.localStorage[name];
            if(!localstorageInfo){
                return null;
            }
            return JSON.parse(window.localStorage[name]);
        }catch(e){
            return null;
        }
    },
    setLocalStorage:function(name,obj){//设置localstorage的某个值
        try{
            window.localStorage[name] = JSON.stringify(obj);
        }catch(e){
            return ;
        }
    },
    removeLocalStorage:function(name){
        try{
            window.localStorage.removeItem(name);
        }catch(e){
            return ;
        }
    }
};

//阻止冒泡
function stopBubble(e){
    if (e && e.stopPropagation ){//not IE
        e.stopPropagation();
    }
    else{//IE
        window.event.cancelBubble = true;
    }

    if(window.ubtApi && window.ubtApi.loggerEvent){
        window.ubtApi.loggerEvent(e);
    }
}


/************************************
 业务、界面相关方法
 *************************************/

/*app下载相关的 二维码处理
 * 参数dom的属性上 图片宽、高、以及位置标志source
 * */
function buildDownAppQrcode(self){
    if(self.css('position') != 'absolute'){//本身没有定位
        self.css('position','relative');
    }

    var str = self.attr('source'),
        ewmImgW = Number(self.attr('ewmimgw')) ? Number(self.attr('ewmimgw')) : 119,
        ewmImgH = Number(self.attr('ewmimgh')) ? Number(self.attr('ewmimgh')) : 119;

    var hostObj = {
        productionUrl: 'http://m.sh.lianjia.com/client/',
        testUrl:       'http://192.168.3.85:8010/client/',
        developmentUrl:'http://local.lianjia.net:8010/client/',
        integrationUrl:'http://192.168.3.85:8010/client/',
    };

    var env = window.headerParameters && window.headerParameters.env ? window.headerParameters.env : '';
    var url = hostObj[env+'Url'] + "?source=" + str; //s=wem 来源：二维码
    var qrcodeOptions = {
        render: "canvas",
        width: ewmImgW,
        height:ewmImgH,
        text: toUtf8(url)
    };
    if(!checkBrowserIsLegal()){
        qrcodeOptions.render = "table";
    }
    self.qrcode(qrcodeOptions);

    //动态插入的logo图
    var logoImgW = parseInt(ewmImgW / 3),logoImgH = parseInt(ewmImgH / 3);
    if(self.find('.js_qrCodeIcon').length <= 0){
        self.append('<img class="js_qrCodeIcon" src="' + headerParameters.publichost + '/public/img/qrCodeIcon.png"  />');
        var bW = ewmImgW <= 70 ? 1 : ( ewmImgW <= 100 ? 2 : 3);
        self.find('.js_qrCodeIcon').css({
            borderWidth:bW+'px',
            borderRadius: parseInt(logoImgW / 5 ) + 'px',
            borderStyle: 'solid',
            borderColor: '#fff',
            background: '#fff',
            width:logoImgW + 'px',
            height:logoImgH + 'px',
            position:'absolute',
            top:'50%',
            left:'50%',
            marginTop:'-' + (logoImgW/2 + bW) + 'px',
            marginLeft:'-' + (logoImgH/2 + bW) + 'px'
        });
    }

}

var feedbackDoing = false;//反馈弹层－防重复提交中间变量
var callback_getUserInfo = null;
$(function(){

    //图片延迟加载（lazyload）
    if($('.lj-lazy').length > 0){
        $('.lj-lazy').lazyload();
    }


    //fix-right - hover - tips
    $(".fix-right-v2 ul").on("mouseenter", "li", function(){
        var index = $(".fix-right-v2 ul li").index($(this));

        //显示
        $(".fix-right-v2 ul li span.popup").eq(index).css({
            opacity : 0,
            display : 'block',
            right : '48px'
        }).animate({
            opacity : 1,
            right : '38px'
        }, 300);
    }).on("mouseleave", "li", function(){
        $(".fix-right-v2 ul li span.popup").css({
            opacity : 0,
            display : 'none',
            right : '48px'
        });
    });

    //fix-right - go top
    function resetGotop(){
        var h = $(window).height(),
            t = $(document).scrollTop();
        if(t>h){
            $("#gotop").show();
            $(".fix-right .tips,.fix-right .has-ask").show();
        }
        else{
            $("#gotop").hide();
            $(".fix-right .tips,.fix-right .has-ask").hide();
        }
    }
    resetGotop();
    $("#gotop").click(function(){
        $("body").css({
            scrollTop : 0
        });
    });

    $(window).scroll(function(e){
        resetGotop();
    });
    
    
    //界面上已经存在的dom 直接初始化
    $('.js_downAppEwmStatic_img').each(function(){
        var self = $(this);
        buildDownAppQrcode(self);
    });
    
    //点击详情链接时发送UBT日志
	// $("a[name=selectDetail]").click(function(){
	// 	try {
	// 		var searchType = ubtData.searchType,
	// 			searchKeyword = ubtData.searchKeyword,
	// 			resultNum = ubtData.resultNum,
	// 			selectResult = $(this).attr("key"),
	// 			selectIndex = $(this).closest("li").index() + 1;
	// 		ubtApi.trackSearch(searchType, searchKeyword, resultNum, selectResult, selectIndex);
	// 	}catch(e){
	// 		console.error(e);
	// 	}
	// });
	
	// $("#suggestion").on("click",".actSelect",function(){
	// 	var searchType = "headerSearch",
	// 		searchKeyword = $("#keyword-box").val(),
	// 		resultNum = 0,
	// 		selectResult = $(this).attr("key"),
	// 		selectIndex = $(this).closest("li").index() + 1;
	// 	ubtApi.trackSearch(searchType, searchKeyword, resultNum, selectResult, selectIndex);
 //    });

    $('#oldclose').click(function(){
        $("#old").hide();
    });
});