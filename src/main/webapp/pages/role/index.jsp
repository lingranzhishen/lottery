<%@page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp"></jsp:include>
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
		#fm{
			margin:0;
			padding:10px 30px;
		}
		.ftitle{
			font-size:14px;
			font-weight:bold;
			color:#666;
			padding:5px 0;
			margin-bottom:10px;
			border-bottom:1px solid #ccc;
		}
		.fitem{
			margin-bottom:5px;
		}
		.fitem label{
			display:inline-block;
			width:80px;
		}
</style>
<div style="align:center">
前台用户管理
</div>
<h2>Basic CRUD Application</h2>
	<div class="demo-info" style="margin-bottom:10px">
		<div class="demo-tip icon-tip">&nbsp;</div>
		<div>Click the buttons on datagrid toolbar to do crud actions.</div>
	</div>
	
	<table id="dg" title="My Users" class="easyui-datagrid" style="width:700px;height:250px"
			url="/api/role/getRoleList.json"
			toolbar="#toolbar" pagination="true"
			rownumbers="true" fitColumns="true" singleSelect="true">
		<thead>
			<tr>
				<th field="roleCode" width="50">角色编码</th>
				<th field="type" width="50">类型</th>
				<th field="roleName" width="50">角色名</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">New Role</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">Edit Role</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeUser()">Remove Role</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="configAuth()">配置权限</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
			closed="true" buttons="#dlg-buttons">
		<div class="ftitle">Admin Information</div>
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<label>角色编码:</label>
				<input name="roleCode" id ="roleCodeInput" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>类型:</label>
				<input name="type" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>角色名:</label>
				<input name="roleName" class="easyui-validatebox" required="true">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">Save</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>
<jsp:include page="../common/footer.jsp"></jsp:include>

<script type="text/javascript">
var url;
function newUser(){
	$('#roleCodeInput').attr("readonly",false);
	$('#dlg').dialog('open').dialog('setTitle','New Role');
	$('#fm').form('clear');
	url = '/api/role/add.json';
}
function editUser(){
	$('#roleCodeInput').attr('readonly',true);
	var row = $('#dg').datagrid('getSelected');
	if (row){
		$('#dlg').dialog('open').dialog('setTitle','Edit Role');
		$('#fm').form('load',row);
		url = '/api/role/update.json';
	}
}
function saveUser(){
    var jsonuserinfo = $('#fm').serializeObject();
    if(!$('#fm').form('validate')){
    	return false;
    }
    $.ajax({
        type: "POST",
        url: url,
        data:JSON.stringify(jsonuserinfo),
        dataType: "json",
        contentType:"application/json",
        success: function(data){
			if (data.status=='ok'){
				$('#dlg').dialog('close');		// close the dialog
				$('#dg').datagrid('reload');	// reload the user data
			} else {
				 $.messager.show({
					title: 'Error',
					msg: data.error
				}); 
			}
         }
    });
}
function removeUser(){
	var row = $('#dg').datagrid('getSelected');
	if (row){
		$.messager.confirm('Confirm','Are you sure you want to remove this user?',function(r){
			if (r){
				$.post('/api/role/deleteRole.json',{roleCode:row.roleCode},function(result){
					if (result.status=='ok'){
						$('#dg').datagrid('reload');	// reload the user data
					} else {
						$.messager.show({	// show error message
							title: 'Error',
							msg: result.msg
						});
					}
				},'json');
			}
		});
	}
}
function configAuth(){
	var row = $('#dg').datagrid('getSelected');
	if (row){
		location.href = "/role/auth/"+row.roleCode;
	}else{
		$.messager.show({	// show error message
			title: 'INFO',
			msg: '请选择一个角色'
		});
	}
}
	</script>
