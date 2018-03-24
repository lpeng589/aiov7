<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预警项目设置</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/workflow2.css" />
<link rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/CRselectBoxList.css"/>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/gen/listPage.vjs_zh_CN.js?1357894434000"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script type="text/javascript"  src="/js/aioselect.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
 
<script type="text/javascript">

	jQuery(document).ready(function(){
		if("$!searType"=="search"){
			jQuery("#keyWord").val("$!searValue");
		}
		
	});
	
	function dealAlertSet(type,id){
		var title = "";
		var urls = "/AlertSetAction.do?operation=";
		if(type==1){
			title="添加预警项目";
			urls += "6";
		}else if(type=2) {
			title="修改预警项目";
			urls += "7&keyId="+id;
		}
		asyncbox.open({
		 title : title,
		 	 id : 'alertDiv',
		　　　url : urls,
		　　　width : 700,
		　　　height : 450,
			 btnsbar : jQuery.btn.OKCANCEL,
			 callback : function(action,opener){
	　　　　　	if(action == 'ok'){
					var alertName = opener.document.getElementById("alertName");
					var modelId = opener.document.getElementById("modelId");
					var actionTime = opener.document.getElementById("actionTime");
					var actionFrequency = opener.document.getElementById("actionFrequency");
					var alertType = opener.document.getElementsByName("alertType");
					var condition = opener.document.getElementById("condition");
					if(alertName.value == ""){
						alert("预警名称不能为空！");
						alertName.focus();
						return false;
					}
					if(modelId.value == ""){
						alert("来源模块不能为空！");
						modelId.focus();
						return false;
					}
					if(actionTime.value == ""){
						alert("执行时间不能为空！");
						actionTime.focus();
						return false;
					}
					if(!isInt(actionTime.value) || actionTime.value<0){
						alert("执行时间必须为大于等于0的正整数!");
						actionTime.focus();
						return false;
					}
					if(actionFrequency.value == ""){
						alert("执行频率不能为空！");
						actionFrequency.focus();
						return false;
					}
					if(!isInt(actionFrequency.value) || actionFrequency.value<=0){
						alert("执行频率必须为大于0的正整数!");
						actionFrequency.focus();
						return false;
					}
					var checkFlag  = false;
					for(var i=0;i<alertType.length;i++){
						if(alertType[i].checked){
							checkFlag = true;
							break;
						}
					}
					//if(!checkFlag){
					//	alert("请选择提醒方式！");
					//	return false;
					//}
					/*if(condition.value == ""){
						alert("条件不能为空！");
						condition.focus();
						return false;
					}*/
					opener.saves();
					return false;
	　　　　　	}
	　　　	}
	　	});
	}
	
	function detailAlertSet(id){
		var urls = "/AlertSetAction.do?operation=5&keyId="+id;
		var title="详情预警项目";
		asyncbox.open({
		 	title : title,
		 	 id : 'alertDiv',
		　　　url : urls,
		　　　width : 700,
		　　　height : 450,
			 btnsbar : jQuery.btn.CANCEL
	　	});
	}
	
	function checks(){
		window.location.href='/AlertSetAction.do?operation=4';
	}
	
	function dealAsyn(){
		checks();
	}
	
	/* 删除 */
	function deletes(id){
		asyncbox.confirm('你确定删除吗？','提示',function(action){
		　　if(action == 'ok'){
				window.location.href="/AlertSetAction.do?operation=3&keyId="+id;	
			}
		});
	}
	
	//启用停用
	function dealStatus(type){
		var msg = "是否确定";
		if(type == "open"){
			msg += "显示选中的预警";
		}else if(type == "stop"){
			msg += "隐藏选中的预警";
		}
		if(!isChecked('keyId')){
			asyncbox.alert('$text.get("common.msg.mustSelectOne")','提示');
			return false;
		}else{
			asyncbox.confirm(msg,'提示',function(action){
			　　if(action == 'ok'){
					jQuery("#operation").val(2);
					jQuery("#statusType").val(type);
					jQuery("#optype").val('openOrStop');
					form.submit();
				}
			});
		}
	}
	
	function fillLanguage(str){
		jQuery.opener('alertDiv').fillLanguage(str);
	}
	
	function dealAsyn(){
		window.location.href="/AlertSetAction.do?operation=4&optype=query";
	}
	
	/* 所属组 */
	function selectOver(type){
		var value= "all"
		var searchType = "";
		if(type == 1){
			value=jQuery("#_view").val();
			jQuery("#_status").val('all');
			searchType = "group";
		}else{
			value=jQuery("#_status").val();
			jQuery("#_view").val('all');
			searchType = "status";
		}
		jQuery("#keyWord").val("");
		jQuery("#searType").val(searchType);
		jQuery("#searValue").val(value);
		refure();
	}
	
	/* 关键字查询 */
	function keywordSearch(){
		var value = jQuery("#keyWord").val();
		jQuery("#_view").val('all');
		jQuery("#_status").val('all');
		if(value == "" || value == "关键字搜索"){
			jQuery("#searType").val('');
			jQuery("#searValue").val('');
		}else{
			jQuery("#searType").val('search');
			jQuery("#searValue").val(value);
		}
		refure();
	}
	function refure(){
		form.submit();
	}
</script>

<style type="text/css">
	.oabbs_tc1{
		width:8%;
	}
	.oabbs_tc2{
		width:12%;
		text-align: center;
	}
	.oabbs_tc3{
		width:15%;
	}
	.oabbs_tc{
		width:10%;
	}
	.td_width3{
		width:*;
	}
	.td_width4{
		width:22%;
	}
	.td_width2{
		width:15%;
	}
	.td_tbox{
		width:5%;
		text-align: center;
	}
	.CRselectBoxOptions{
		width:110px;
	}
</style>
</head>
 
<body >
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" id="form" name="form" action="/AlertSetAction.do">
<input type="hidden" id="operation" name="operation" value="4"/>
<input type="hidden" id="optype" name="optype" value="query"/>
<input type="hidden" id="statusType" name="statusType" value="" />
<input type="hidden" id="searType" name="searType" value="$!searType"/>
<input type="hidden" id="searValue" name="searValue" value="$!searValue"/>
		<table cellpadding="0" cellspacing="0" border="0" class="framework" >
			<tr>
				<td>
					<div class="TopTitle">
						<span style="font-weight:normal;">
						<img src="/style1/images/workflow/ti_001.gif"/>预警项目设置
						</span>
						<span style="padding-left: 50px;font-weight:normal;">
							所属组：<script type="text/javascript">
	  							aioselect('_view',[
	  								{value:'all',name:'全部分组'},
	  								#foreach($group in $groupList)
	  									{value:'$globals.get($group,0)',name:'$globals.get($group,1)'},
	  								#end
								],#if("$!searType"=="group") '$!searValue' #else '' #end,'100','selectOver(1)');
				 			</script>
						</span>
						<span style="padding-left: 10px;font-weight:normal;">
							预警状态：<script type="text/javascript">
	  							aioselect('_status',[
	  								{value:'all',name:'全部'},{value:'0',name:'显示'},{value:'1',name:'隐藏'},
	  								{value:'2',name:'启用'},{value:'3',name:'停用'},
								],#if("$!searType"=="status") '$!searValue' #else '' #end,'100','selectOver(2)');
				 			</script>
						</span>
				 		<span style="padding-left: 10px;font-weight:normal;"><input type="text" class="search_text" id="keyWord" name="keyWord"  value="关键字搜索"  onKeyDown="if(event.keyCode==13) keywordSearch();" onblur="if(this.value=='')this.value='关键字搜索';" onfocus="if(this.value=='关键字搜索'){this.value='';}this.select();"/><input type="button" class="search_button" style="cursor: pointer;" onclick="javascript:keywordSearch();"/>
				 		</span>
						<span style="float: right;font-weight: normal;margin-right: 12px;margin-bottom: 4px;">
							<input type="button" class="bu_02" onClick="refure()" value="刷新"/>
							#if($LoginBean.operationMap.get("/AlertSetAction.do").add() || $!LoginBean.id=="1")
							<input type="button" class="bu_02" onclick="dealAlertSet(1,'')" value="添加" />	
							#end
							#if($LoginBean.operationMap.get("/AlertSetAction.do").update() || $!LoginBean.id=="1")
							<input type="button" class="bu_02" onClick="dealStatus('open')" value="显示"/>
							<input type="button" class="bu_02" onClick="dealStatus('stop')" value="隐藏"/>
							#end
						</span>
					</div>
					
					<table class="data_list_head"  cellpadding="0" cellspacing="0" >
						<thead>
							<tr>
								<td width="30px;">序号</td>
								<td width="30px;"><input type="checkbox" id="selAll" onClick="checkAll('keyId')"/></td>
								<td class="oabbs_tc1">所属组</td>
								<td class="oabbs_tc3">预警名称</td>
								<td class="td_width2">来源报表</td>
								<td>条件</td>
								<td class="oabbs_tc1">提醒类型</td>
								<td class="td_tbox" align="center">执行时间</td>
								<td class="td_tbox" align="center">执行频率</td>
								<td class="td_tbox" align="center">显示</td>
								<td class="td_tbox" align="center">启用</td>
								<td class="oabbs_tc2" align="center">
									操作
								</td>
							</tr>
						</thead>
					</table>
					<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow-y:auto;width:99%;" >
					<script type="text/javascript"> 
						var oDiv=document.getElementById("data_list_id");
						var sHeight=document.documentElement.clientHeight-115;
						oDiv.style.height=sHeight+"px";
					</script>
						<table cellpadding="0" cellspacing="0">					
							<tbody>
								#set($count=1)
								#foreach($!alertSet in $!alertSetList)
									<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
										<td width="30px;">$!count</td>
										<td width="30px;"><input type="checkbox" id="keyId" name="keyId" value="$!alertSet.id"/></td>
										<td class="oabbs_tc1">$!alertSet.groupName</td>
										<td class="oabbs_tc3">$!alertSet.AlertName</td>
										<td class="td_width2">$!alertSet.modelName</td>
										<td><span title="$!alertSet.condition">$!globals.subTitle($!alertSet.condition,20)</span></td>
										<td class="oabbs_tc1">
											#foreach($t in $!globals.strSplit($!alertSet.AlertType,','))
								  				#if($t==1)
								  					短信&nbsp;
								  				#elseif($t==2)
								  					邮件&nbsp;
								  				#elseif($t==4)
								  					通知&nbsp;
								  				#end
									  		#end
										</td>
										<td class="td_tbox">$!alertSet.ActionTime</td>
										<td class="td_tbox">$!alertSet.ActionFrequency</td>
										<td class="td_tbox">#if("$!alertSet.isHidden"=="0")是#elseif("$!alertSet.isHidden"=="1")否#end</td>
										<td class="td_tbox">#if("$!alertSet.Status"=="0")是#elseif("$!alertSet.Status"=="1")否#end</td>
										<td align="center" class="oabbs_tc2">
											#if($LoginBean.operationMap.get("/AlertSetAction.do").update() || $!LoginBean.id=="1")
											<a href="javascript:void(0)" onclick="dealAlertSet(2,'$!alertSet.id')" style="color:blue;">修改</a>&nbsp;
											#end
											#if($LoginBean.operationMap.get("/AlertSetAction.do").delete() || $!LoginBean.id=="1")
											<a href="javascript:void(0)" onclick="deletes('$!alertSet.id')" style="color:blue;">删除</a>&nbsp;
											#end
											<a href="javascript:void(0)" onclick="detailAlertSet('$!alertSet.id')" style="color:blue;">详情</a>&nbsp;
										</td>
									</tr>
									#set($count=$count+1)
								#end
							</tbody>		
						</table>
						#if($alertSetList.size()==0)
						<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">
							暂无预警项目设置信息
						</div>
						#end
					</div>
			</td>
			</tr>
			</table>
		</form>
	</body>
	</html>