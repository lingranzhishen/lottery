<div class="page-box house-lst-page-box">
	<#assign fPage = 7 />
	<#if !paginate.firstPage>
	    <a href="/${pageType}/${nowUrl ? replace(pageCode, 'd' + paginate.prePage)}" gahref="results_prev_page">上一页</a>	    
	</#if>
	
	<#if paginate.prePage2 &gt; 1>
		<#assign fPage = fPage - 1 />
		<a href="/${pageType}/${nowUrl ? replace(pageCode, 'd1') }" gahref="results_d1">1</a>
		&nbsp;...&nbsp;
	</#if>
	
	<#list paginate.prePage2..paginate.nextPage2 as page>
	    <#assign fPage = fPage - 1 />
        <#if page = paginate.pageNo>
        	<a href="/${pageType}/${nowUrl}" class="on" gahref="results_d${page}">${page}</a>
        <#else>
        	<a href="/${pageType}/${nowUrl ? replace(pageCode, 'd' + page) }" gahref="results_d${page}">${page}</a>
        </#if>
	</#list>
	
	<#list 1..fPage as page>
	    <#if paginate.nextPage2 + page lte paginate.totalPage>
	        <a href="/${pageType}/${nowUrl ? replace(pageCode, 'd' + (paginate.nextPage2 + page)) }"  gahref="results_d${paginate.nextPage2 + page}">${paginate.nextPage2 + page}</a>
	    </#if>
	</#list>
	
	<#if paginate.nextPage2 + fPage lte paginate.totalPage - 1>
		&nbsp;...&nbsp;
		<a href="/${pageType}/${nowUrl ? replace(pageCode, 'd' + paginate.totalPage) }" gahref="results_totalpage">${paginate.totalPage}</a>
	</#if>
	
	<#if !paginate.lastPage>
	    <a href="/${pageType}/${nowUrl ? replace(pageCode, 'd' + paginate.nextPage)}" gahref="results_next_page" >下一页</a>
	</#if>
	
	<#--
	&emsp;到第<input size='3' id="pageNo" value="${paginate.pageNo } " class="input-goto"/>页
	<input type="button" value="确定" class="btn-goto" />
	-->
</div>