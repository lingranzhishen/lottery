<%@page language="java" pageEncoding="UTF-8"%>
<jsp:include page="common/header.jsp"></jsp:include>
<style>
body {
    font-family:verdana,helvetica,arial,sans-serif;
    padding:20px;
    font-size:12px;
    margin:0;
}
h2 {
    font-size:18px;
    font-weight:bold;
    margin:0;
    margin-bottom:15px;
}
.demo-info{
	padding:0 0 12px 0;
}
.demo-tip{
	display:none;
}

</style>
<div style="align:center">
<h2>管理员中心</h2>
	<p>欢迎，</p>
	<div style="margin:20px 0;"></div>
	<div class="easyui-layout" style="width:1200px;height:700px;">
		<div data-options="region:'north'" style="height:50px"></div>
		<div data-options="region:'south',split:true" style="height:50px;"></div>
		
		<div data-options="region:'west',split:true" title="管理菜单" style="width:200px;">
			<div style="margin:20px 0;"></div>

		<ul class="easyui-tree" id="menuTree">
		
		</ul>
	</div>
	
		<div data-options="region:'center',title:'Main Title',iconCls:'icon-ok'">
	<div id="contentTabs" class="easyui-tabs" style="width:1000px;height:700px;">
	</div> 
		</div>
	</div>
</div>
<jsp:include page="common/footer.jsp"></jsp:include>

<script>
$('#menuTree').tree({
	url: '${config.uchost}/api/auth/getAuthTree.json?userName=aaa',
	loadFilter: function(rows){
		return convert(rows.data);
	},
	onClick:function(node){
         if (node.parentId !== 0 && node.text !== undefined) {
             addTab(node.text,node.url);    
         }                                                                
     }
});

function addTab(title, url){
    if ($('#contentTabs').tabs('exists', title)){
        $('#contentTabs').tabs('select', title);
    } else {
        var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
        $('#contentTabs').tabs('add',{
            title:title,
            content:content,
            closable:true
        });
    }
}


function convert(rows){
	function exists(rows, parentId){
		for(var i=0; i<rows.length; i++){
			if (rows[i].id == parentId) return true;
		}
		return false;
	}
	
	var nodes = [];
	// get the top level nodes
	for(var i=0; i<rows.length; i++){
		var row = rows[i];
		if (!exists(rows, row.parentId)){
			nodes.push({
				id:row.id,
				text:row.authName,
				parentId:row.parentId,
				url:row.url
			});
		}
	}
	
	var toDo = [];
	for(var i=0; i<nodes.length; i++){
		toDo.push(nodes[i]);
	}
	while(toDo.length){
		var node = toDo.shift();	// the parent node
		// get the children nodes
		for(var i=0; i<rows.length; i++){
			var row = rows[i];
			if (row.parentId == node.id){
				var child = {id:row.id,text:row.authName,parentId:row.parentId,url:row.url};
				if (node.children){
					node.children.push(child);
				} else {
					node.children = [child];
				}
				toDo.push(child);
			}
		}
	}
	return nodes;
}

</script>
