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

	
	<table id="dg" title="${role.roleName}的权限列表" class="easyui-datagrid" style="width:700px;height:250px"
			url="/api/role/getRoleAuthList.json?roleCode=${role.roleCode }"
			toolbar="#toolbar" pagination="true"
			rownumbers="true" fitColumns="true" singleSelect="true">
		<thead>
			<tr>
				<th field="authId" width="50">权限编号</th>
				<th field="authName" width="50">权限名</th>
				<th field="createTime" width="50">创建时间</th>
				<th field="updateTime" width="50">更新时间</th>
				<th field="creator" width="50">创建者</th>
				<th field="updator" width="50">更新者</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">New Role</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeUser()">Remove Role</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
			closed="true" buttons="#dlg-buttons">
		<div class="ftitle"> Information</div>
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<label>角色编码:</label>
				<input name="roleCode" id="userNameInput" class="easyui-validatebox"  >
			</div>
			<div class="fitem">
				<label>权限:</label>
				<input class="easyui-combobox" name=authId id="roleCodeCombobox" style="width:30%"
			data-options="
				url:'/api/role/getUnAuthRoleList.json?roleCode=${role.roleCode}',
				method:'get',
				valueField:'id',
				textField:'text',
				panelHeight:'auto'
			">
			</div>
			<div class="fitem">
			<label>操作者:</label>
				<input name="updator" class="easyui-validatebox" required="true">
			</div>				
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">Save</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>
<jsp:include page="../common/footer.jsp"></jsp:include>

<script type="text/javascript">
var roleCode='${role.roleCode}';//当前被管理的角色编码
var url;
function newUser(){
	
	$('#dlg').dialog('open').dialog('setTitle','New Administrator');
	$('#fm').form('clear');
	$('#userNameInput').val(roleCode);
	$('#userNameInput').attr("readOnly",true);
	url = '/api/role/addRoleAuth.json';
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
				$('#roleCodeCombobox').combobox('reload');
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
				$.post('/api/role/deleteRoleAuth.json',{authId:row.authId,roleCode:roleCode},function(result){
					if (result.status=='ok'){
						$('#dg').datagrid('reload');	// reload the user data
						$('#roleCodeCombobox').combobox('reload');
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

	</script>
