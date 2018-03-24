<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.consign.setting")</title>
<link rel="stylesheet" href=" /$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script>
function openInputTime(obj){
	WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd'});
}

function fillData(datas){
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	parent.jQuery.close('Popdiv');
}

/*清空数据*/
function removeData(fieldName,fieldNameIds){
	$("#"+fieldName,document).val("");
	$("#"+fieldNameIds,document).val("");
}

function selectFlowType(){
	var urlstr = '/WorkConsignAction.do?operation=4&type=selectFlowType&flowIds='+$("#flowName",document).val();
	asyncbox.open({id:'FlowTypePop',title:'添加工作流类型',url:urlstr,width:680,height:430,btnsbar:jQuery.btn.OKCANCEL,
		callback:function(action,opener){
　　　　	if(action == 'ok'){
　　　　		var returnValue = opener.returnValue();
				if(typeof(returnValue)!="undefined"){
					if(returnValue == "|"){
						$("#flowNameDis",document).val("全部流程");
					}else{
						$("#flowName",document).val(returnValue.split("|")[0]);
						$("#flowNameDis",document).val(returnValue.split("|")[1]);
					}
					return true;
				}
				return false;
　　　　　	}
　　　	}
	});
}

function beforeSubmit(){
	var consignUser = $("#consignUserID",document);
	if(consignUser.val().length == 0){
		asyncbox.tips('代理人不能为空');
		return ;
	}
	if(consignUser.val() == "$!LoginBean.id"){
		asyncbox.tips('代理人不能设置为自己');
		return ;
	}
	var beginTime = $("#beginTime",document);
	if(beginTime.val().length == 0){
		asyncbox.tips('委托开始时间不能为空');
		beginTime.focus();
		return ;
	}
	var endTime = $("#endTime",document);
	if(endTime.val().length == 0){
		asyncbox.tips('委托结束时间不能为空');
		endTime.focus();
		return ;
	}
	var beginDate = DateFormat.parse(beginTime.val(),'yyyy-MM-dd');
	var endDate = DateFormat.parse(endTime.val(),'yyyy-MM-dd');
	var curDate = DateFormat.parse(DateFormat.format(new Date(),'yyyy-MM-dd'),'yyyy-MM-dd');
	if(curDate.getTime()>beginDate.getTime()){
		asyncbox.tips('委托开始时间不能小于当前时间','alert','2000');
		beginTime.focus();
		return ;
	}
	
	if(endDate.getTime()<beginDate.getTime()){
		asyncbox.tips('委托开始时间不能大于结束时间','alert','2000');
		endTime.focus();
		return ;
	}
	form.submit();
}
</script>
<style>
.listRange_woke input{border:1px solid #bbb;height:18px;width:130px;};
</style>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/WorkConsignAction.do" name="form" method="post" target="formFrame">
<input type="hidden" name="keyId" value="$!consign.id"/>
#if("$!consign" == "")
<input type="hidden" name="operation" value="1"/>
#else
<input type="hidden" name="operation" value="2"/>
#end
<div id="listRange_id" align="center">
	<ul class="listRange_woke" style="padding:5px 0 0 0;">
		<li>
			<span>$text.get("com.work.confignor")：</span>
			#if("$!consign.userid"=="")
			<input type="text" value="$!LoginBean.EmpFullName" readonly="readonly"/>
			#else
			<input type="text" value="$!globals.getEmpNameById($consign.userid)" readonly="readonly"/>
			#end
		</li>
		<li>
			<span>委托流程：</span>
			<input type="hidden" id="flowName" name="flowName" value="$!consign.flowName"/>
			<input type="text" id="flowNameDis" name="flowNameDis" value="#if("$!consign.flowNameDis"=="")全部流程#else $!consign.flowNameDis #end" title="$!consign.flowNameDis" style="width:275px;" ondblclick="selectFlowType();" readonly="readonly"/>
			<img src="/$globals.getStylePath()/images/St.gif" onClick="selectFlowType();" class="search"/>(默认全部)
			 
		</li>
		
		<li>
			<span>代理人：</span>
			<input type="hidden" id="consignUserID" name="consignUserID" value="$!consign.congignuserid"/>
			<input type="text" id="consignName" name="consignName" value="#if("$!consign.congignuserid"!="")$!globals.getEmpNameById($consign.congignuserid)#end" ondblclick="deptPopForAccount('userGroup','consignName','consignUserID');" readonly="readonly"/>
			<img src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccount('userGroup','consignName','consignUserID');" class="search"/>
		</li>
		<li>
			<span>委托时间：</span>
			<input type="text" id="beginTime" name="beginTime" onClick="openInputTime(this);" value="$!consign.beginTime"/> - 
			<input type="text" id="endTime" name="endTime" onClick="openInputTime(this);"  value="$!consign.endTime"/>
		</li>
		<div style="margin:20px 10px 15px 15px;display:inline-block;color:#003E7B;"><font color="red">温馨提示：</font>您可以设置相关人员代理您审批、回复相关工作流程。成功设置后，系统会自动将您的待办流程流转给代理人处理</div>
	</ul>
</div>
</form>
</body>
</html>
