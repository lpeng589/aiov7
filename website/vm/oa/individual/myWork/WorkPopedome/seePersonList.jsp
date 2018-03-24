<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/sharingStyle.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/listPage.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>

<script type="text/javascript">
$(function(){
	$(".tempNameCla").each(function(i){
		var value = $(this).text();
	 	$("#queryPop"+(i+1)).attr("title",value);
		if (getStringLength($(this).text()) > 45){
			$(this).text(substr($(this).text(),45)+"...");
		}
	 });
});
			
function selectEmployee(){
	var urlstr = '/UserFunctionAction.do?operation=22&tableName=tblEmployee&selectName=SelectEventEmployee' ;
	var str = window.showModalDialog(urlstr+"&MOID=$!MOID&MOOP=query","","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined")return ;
	var content = str.split(";") ;
	document.getElementById("employee").value = content[0] ;
}

function selectDept(){
	var urlstr = '/UserFunctionAction.do?operation=22&tableName=tblDepartment&selectName=SelectEventDepartmentName' ;
	var str = window.showModalDialog(urlstr+"&MOID=$!MOID&MOOP=query","","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined")return ;
	var content = str.split(";") ;
	document.getElementById("department").value = content[0] ;
	document.getElementById("departmentName").value = content[1] ;
}

function openInvite(addOfUp,seeId){
	var operation=0;
	var urls="";
	var titles="";
	var btns="";
	if(addOfUp=="1"){
		titles="添加查看权限设置";
		 btns= jQuery.btn.OKCANCEL;
		urls="OAWorkPopedomeActon.do?operation=6";
	}else if(addOfUp=="2"){
		titles="修改查看权限设置";
		 btns=  jQuery.btn.OKCANCEL;
		urls="OAWorkPopedomeActon.do?operation=7&seeId="+seeId;
	}else{
		titles="查看权限设置详情";
		btns=jQuery.btn.CANCEL;
		urls="OAWorkPopedomeActon.do?detail=detail&seeId="+seeId;
	}
	asyncbox.open({
		 id : 'dealdiv',
	 	 title : titles,
	　　　url : urls,
		 // cache:true; 隐藏
	　　　width : 680,
	　　　height : 350,
		 btnsbar : btns,
		 callback : function(action,opener){
　　　　　	//判断 action 值。

　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。

				var seePersonIds=opener.document.getElementById("seePersonIds");
				var desContent=opener.document.getElementById("desContent");
				//alert(desContent.value);
　　　　　　　			var userIds = opener.document.getElementById("popedomUserIds");
		   		var deptIds = opener.document.getElementById("popedomDeptIds");
		   		var empGroupIds = opener.document.getElementById("empGroupId");
		   		if((empGroupIds.value == "" || empGroupIds.value == null) && (userIds.value == "" || userIds.value == null) && (deptIds.value == "" || deptIds.value == null)){
		   			asyncbox.alert('请选择员工/下级!','$!text.get("clueTo")');
					return false;
		   		}
		   		if(seePersonIds.value == "" || seePersonIds.value == null){
		   			asyncbox.alert('请选择检视员/上级!','$!text.get("clueTo")');
					return false;
		   		}
		   		if(desContent.value.length>200){
		   			return false;
		   		}
		   		opener.checkeSet();
		   		return false;
　　　　　	}
　　　	}
　	});
}

//双击回填方法
function fillData(datas){
	//处理两个弹出框

	if(jQuery.exist('dealdiv')){
		//找到弹出框传值

		var val = jQuery.opener('dealdiv').evaluate(datas);
	}
	//关掉窗口
	jQuery.close('Popdiv');
}

function dealAsyn(){
	asyncbox.tips('操作成功','success');
	window.location.reload();
}

function manageGroup(id,flag){
	var mydatasIds="";
	if(flag=="1"){
		var items = document.getElementsByName(id);
		for(var i=0;i<items.length;i++){
			if(items[i].checked){
						var  value = items[i].value;
						mydatasIds+=value+",";
			}
		}
	}else{
		mydatasIds=id;
	}
	if(mydatasIds!="" && mydatasIds.length!=0){
		asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
			if(action == 'ok'){
				jQuery.ajax({url: "/OAWorkPopedomeActon.do?operation=3&seeId="+mydatasIds, success: function(response){
					if(response == "success"){
						asyncbox.tips('$text.get("common.msg.delSuccess")','success',600);
						refreshBeg();
					}else{
						asyncbox.tips('$text.get("common.msg.delError")','error',600);
						refreshBeg();
					}
				}});
				//form.submit();
			}
		});
	}else{
		asyncbox.alert('$text.get('common.msg.mustSelectOne')','$!text.get("clueTo")');
	}
}
var i;
function refreshBeg(){
	i = setTimeout("refreshEnd()",500);
}
function refreshEnd(){
	window.location.reload() ;
}
</script>
<style type="text/css">
a{
	color:blue;
}
</style>
</head>
<body bgcolor="#FFFFFF";> 
<form  method="post" scope="request" name="form" action="/OAWorkPopedomeActon.do?operation=3">
 <input type="hidden" name="operation" value="$globals.getOP("OP_ADD_PREPARE")"/>
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/> 

<ul class="TopTitle">
		<ol><span><img src="/style1/images/oaimages/TopTitleIcon.jpg" /></span>权限查看设置</ol>
			#if($!del)<li><input type="button" class="TopSharing_button" value="$text.get("mrp.lb.delete")" onClick="manageGroup('keyId','1');" title="$text.get("mrp.lb.delete")"/></li>#end
			#if($!add)<li><input type="button" class="TopSharing_button" value="$text.get("common.lb.add")" onClick="openInvite('1','');" title="$text.get("common.lb.add")"/></li>#end
	</ul>
<div style="height:1px;background-color:#F3F3F3" ></div>
<div class="dd" id="data_list_id" align="center" style="border:0px solid red;">
<script type="text/javascript">
	var oDiv=document.getElementById("data_list_id");
	var sHeight=document.documentElement.clientHeight-90;
	oDiv.style.height=sHeight+"px";
</script>
			
	<table width="95%" border="0" cellpadding="0" cellspacing="1" >
          <tr>
            <td valign="top" class="list" align="center">
            <table cellpadding="0" cellspacing="0" >
            <thead>
				<tr style="border:1px solid #D5D5D5;">
					<td style="background:#F9F9F9;">&nbsp;<input type="checkbox"  onClick="checkAll('keyId')"/>&nbsp;</td>
					<td >&nbsp;序号&nbsp;</td>
					<td >检视员/上级</td>
					<td>查看类型</td>
					<td >描述</td>
					<td>操作</td>
				</tr>
			</thead>
	        <tbody id="nn">
	        	#set($num = 1)
	          #foreach($!d in $!workpopedomes)
	          <tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
	          	<td><input type="checkbox" name="keyId" id="keyId" value="$!d.id"/></td>
	          	<td >$num</td>
	          	<td  style ="text-align: left;">			
	          		<a onmouseover="moveover(this);" onmouseout="moveout(this);"  href="javascript:openInvite('3','$!d.id');"><span class="tempNameCla A1" id="queryPop$num" style="cursor: pointer;margin-left:10px;" title="">#if($!d.seePersonNames.size()>0)#set($uNum = 0)#set($uLen = $!d.seePersonNames.size())#foreach($user in $!d.seePersonNames)$!user.EmpFullName#if($uNum<$uLen)#if($!d.seePersonNames.size()>1)、#end#end#set($uNum=$uNum+1)#end#end</span></a>
				</td>
				<td>	
					 #if("$!d.seeType"=="1") 工作计划 #end
					 #if("$!d.seeType"=="2")事件计划#end
					 #if("$!d.seeType"=="0") 全部 #end
				</td>
	          	<td style="text-align: left;"><div title="$!d.desContent" style="margin-left:10px;">$!globals.subTitle("$!d.desContent",50)</div></td>
	          	<td >
	          	#if("$!group.groupName" != "默认")
	          	   <a href="javascript:openInvite('3','$!d.id')" title="详情">详情</a>
	          	   #if($!upd)
			  	    <a href="javascript:openInvite('2','$!d.id');" title="修改"> 修改</a>
			  	   #end
			  	   #if($!del)
	          	  	<a href="javascript:manageGroup('$!d.id','2')" title="删除"> 删除</a>
	          	   #end
	          	#end
			     <!-- <a href="javascript:manageGroup('$group.id','deleteAddressGroup',2)" title="清空数据"> 清空数据</a> -->
				</td>
	          </tr>
	          #set($num = $num+1)
	          #end
	        </tbody>
    	</table>
			<script type="text/javascript">
				var o=document.getElementById("nn");
				if(o){
					var gs=o.childNodes;
					var flag=false;
					for(i=0;i<gs.length;i++){
						if(i%2==0){
							flag=!flag;
						}
						gs[i].className=flag?"c1":"c2";
					}
				}
				function moveover(my){
 				 my.style.color="#000000";
				}
				function moveout(my){
 				  my.style.color="blue";
				}
			</script>
		</td></tr></table>

</div>
	
</form>
</body>
</html>
