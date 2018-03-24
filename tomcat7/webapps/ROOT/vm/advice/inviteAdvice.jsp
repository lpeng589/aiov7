<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>group</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<script language="javascript" src="/js/function.js"></script>
<script src="/js/jquery.js" type="text/javascript"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<link rel="stylesheet" href="/style/css/common.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="/style/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="/style/themes/icon.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">

function checkeSet(){
	var userIds =document.getElementById("popedomUserIds").value;
 	var deptIds =document.getElementById("popedomDeptIds").value;
 	var empGroupIds =document.getElementById("empGroupId").value;
 	if((empGroupIds== "" || empGroupIds== null) && (userIds == "" || userIds== null) && (deptIds== "" || deptIds == null)){
 		asyncbox.alert('请选择邀请对象!','$!text.get("clueTo")');
		return false;
 	}
 	return true;
}

function submitForm(){
	var popedomDeptIds = jQuery("#popedomDeptIds").val();   		//部门
	var popedomUserIds = jQuery("#popedomUserIds").val();			//职员
	var empGroupId = jQuery("#empGroupId").val();					//分组
	var favoriteURL = jQuery("#favoriteURL").val();				//连接地址
	var id = jQuery("#id").val();				
	var module=jQuery("#module").val();					//文件的id.
	var description = jQuery("#description").val();				//内容
	var favoriteName = jQuery("#favoriteName").val();
	jQuery.ajaxSetup({   
  	  async : false  
	});
  
  	jQuery.post("/AdviceAction.do",{module:module,popedomDeptIds:popedomDeptIds,popedomUserIds:popedomUserIds,empGroupId:empGroupId,favoriteURL:favoriteURL,id:id,description:description,description:description},
  	function (data){
  		 if(data=="success"){
			asyncbox.tips('$text.get("oa.common.invitesucceed")','success');
		 }else{
			asyncbox.tips('$text.get("oa.common.inviteunsucceed")','error');
			return false;
		 }
  	});
}
var fieldNames;
var fieldNIds;
function selectPop(popname,fieldName,fieldNameIds){
	var popedomUserId = form.popedomUserIds;
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	var titles= "";
	if(popname == "deptGroup"){
		titles = "请选择部门";
	}else if(popname == "userGroup"){
		titles = "请选择个人";
		urls += "&chooseData=" + popedomUserId.value;
	}else if(popname == "empGroup"){
		titles = "请选择职员分组";
	}
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({id:'Popdiv',title:titles,url:urls,width:750,height:430,btnsbar:jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var employees = opener.strData;
				if(popname == "userGroup"){
					jQuery("#"+fieldName+" option").remove();
					getValueById(fieldNameIds).value="";
				}
				newOpenSelect(employees,fieldName,fieldNameIds)
　　　　　	}
　　　	}
　	});
}

function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds);
	parent.jQuery.close('Popdiv');
}
</script>
<style type="text/css">
.borders{
	border: 1px solid #C8CECE;
}
</style>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" name="form" action="AdviceAction.do" target="formFrame">
<input type="hidden" name="module" id="module" value="inviteAdvice"/>
<input type="hidden" name="userinvite" id="userinvite" value="$!userinvite"/>
<input type="hidden" name="favoriteURL" id="favoriteURL" value="$!favoriteURL"/>
<input type="hidden" name="head"  value="$!head"/>
<input type="hidden" name="id" id="id" value="$!id"/>
<input type="hidden" name="favoriteName" id="favoriteName" value="$!favoriteName"/>
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value=""/>
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value=""/>
<input type="hidden" name="empGroupId" id="empGroupId" value=""/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/style1/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">邀请阅读</div>
	<ul class="HeadingButton">
	</ul>
</div>
<div id="listRange_id">
	<div id="alertDivId" style="width: 630px;margin-top:5px;margin-left:10px;padding:1px;text-align: left">
		<table border="0" width="100%" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
			<tr>
			  <td valign="middle" bgcolor="#F5F5F5">邀请对象：</td>
			  <td valign="middle">
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="selectPop('deptGroup','formatDeptName','popedomDeptIds');"  alt="$text.get("oa.common.adviceDept")" class="search"/>&nbsp;<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="selectPop('deptGroup','formatDeptName','popedomDeptIds');">$text.get("oa.select.dept")</a></div>
				<select name="formatDeptName" id="formatDeptName" multiple="multiple">
				
				</select></div>
				<div class="oa_signDocument2">
				<img onClick="deleteOpation2('formatDeptName','popedomDeptIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search"/>
				</div>
				
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="selectPop('userGroup','formatFileName','popedomUserIds');" alt="$text.get("oa.common.advicePerson")" class="search"/>&nbsp;<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="selectPop('userGroup','formatFileName','popedomUserIds');">$text.get("oa.select.personal")</a></div>
				<select name="formatFileName" id="formatFileName" multiple="multiple">
				
				</select></div>
				<div class="oa_signDocument2">
				<img onClick="deleteOpation2('formatFileName','popedomUserIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search"/>
				</div>
				
			</td>
			</tr>
			<tr>
			  <td valign="middle" bgcolor="#F5F5F5">邀请内容：</td>
			  <td valign="top"><textarea rows="5" cols="60" id="description" name="description" class="borders">针对‘$!title’,$!userinvite与你有如下交流：</textarea>
			  	<div style="color: #C0C0C0;">注:邀请内容为空将发送默认信息！</div>
			  </td>
			</tr>
	  </table>	
	</div>
</div>
</form>
</body>
</html>