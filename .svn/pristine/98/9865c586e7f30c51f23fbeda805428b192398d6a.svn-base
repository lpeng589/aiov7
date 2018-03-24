<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>预警项目设置</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
	
	var showvalues = "";
	var hidevalues = "";
	function popupSelectReport(name,showvalue,hidevalue){
		var displayName=encodeURI(name) ;
		var urlstr = "/UserFunctionAction.do?operation=22&popupWin=modulediv&tableName=&selectName=tblReports&MOID=$LoginBean.getOperationMap().get("/AlertSetAction.do").moduleId&MOOP=add&LinkType=@URL:&displayName="+displayName;
		asyncbox.open({id:'modulediv',title:name,url:urlstr,width:750,height:470})
		showvalues = showvalue; 
		hidevalues = hidevalue;
	}
	
	/* 返回数据 */
	function exemodulediv(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var note = returnValue.split("#;#") ;
		jQuery("#"+showvalues).val(note[1]);
		jQuery("#"+hidevalues).val(note[0]);
		parent.jQuery.close('modulediv');
	}
	
	function saves(){
		form.submit();
	}
	
	jQuery(document).ready(function() {
		#if("$!detail"=="true")
			//详情
			jQuery("#listRange_frame").find("input[type='text']").attr("readonly","readonly");
			jQuery("#listRange_frame").find("input[type='checkbox']").attr("disabled","disabled");
			jQuery("#listRange_frame").find("input[type='radio']").attr("disabled","disabled");
			jQuery("#listRange_frame").find("select").attr("disabled","disabled");
			jQuery("#listRange_frame").find("textarea").attr("disabled","disabled");
		#end
		showcondition();
	});
	
	var fieldNames;
	var fieldNIds;
	
	/* 选择提示用户 */
	function deptPopUser(fieldName,fieldNameIds){
		var urls = "/Accredit.do?popname=userGroup&inputType=checkbox&chooseData="+form.popedomUserIds.value ;
		fieldNames=fieldName;
		fieldNIds=fieldNameIds;
		asyncbox.open({id:'Popdiv',title:'请选择用户',url:urls,width:750,height:430,
			 btnsbar : jQuery.btn.OKCANCEL,
			 callback : function(action,opener){
	　　　　　	if(action == 'ok'){
					var employees = opener.strData;
					jQuery("#"+fieldName+" option").remove();
					getValueById(fieldNameIds).value="";
					newOpenSelect(employees,fieldName,fieldNameIds);
	　　　　　	}
	　　　	}
	　	});
	}
	
	function newOpenSelect(employees,fieldName,fieldNameIds){
		var employees = employees.split("|") ;
		for(var j=0;j<employees.length;j++){
			var field = employees[j].split(";") ;
			if(field!=""){
				var existOption = getValueById(fieldName).options;
				var length = existOption.length;
				var talg = false ;
				for(var i=0;i<length;i++){
					if(existOption[i].value==field[0]){
						talg = true;
					}
				}
				if(!talg){
					getValueById(fieldName).options.add(new Option(field[1],field[0]));
					getValueById(fieldNameIds).value+=field[0]+",";
				}
			}
		}
	}
	
	function getValueById(value){
		return document.getElementById(value) ;
	}
	
	//弹出框双击回填数据


	function fillData(datas){
		newOpenSelect(datas,fieldNames,fieldNIds);
		parent.jQuery.close('Popdiv');
	}
	
	/*下拉列表的批量删除操作*/
	function deleteOpation(fileName,popedomId){
		var index = jQuery("#"+fileName+" option:selected").size();
		if(index==0){
			alert("请选择要移除的项!");
			return false;
		}
		jQuery("#"+fileName+" option:selected").remove();
		/*获取指定下拉列表的值，并为指定的参数赋值*/
		var showContent="";
		jQuery("#"+fileName+" option").each(function(){
		   showContent+= this.value+",";
		});
		jQuery("#"+popedomId).attr("value",showContent);
	}
	
	//帮助
	function showHelp(){
		asyncbox.alert('预警名称：预警设置的名称<br /><br />来源模板：该预警对应的模块，可根据预警连接到模块<br /><br />执行时间：开始执行此预警的时间<br /><br />执行频率：一天中执行的次数<br /><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;状态：此预警状态设置<br /><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;条件：预警提醒所设置的条件，条件相当于模块的条件查询<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;规则：属性名称=值，对个用&连接<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;例如：hide_StockCode=00003;0;测试仓;<font style="color:red;">&</font>tblStock.StockFullName=测试仓,<font style="color:red;">&</font>StockCode=00003<br /><br />提醒方式/用户：选择提醒的方式和用户','输入帮助');
	}
	
	/* 多语言选择 */
	var moreLanguagefield;
	function moreLanguage(len,moreLanguagefield){
		window.moreLanguagefield = moreLanguagefield;
	    moreLanguagefield.testi = "testi";	
		var id = 0;	
	   	var mi = document.getElementsByName(moreLanguagefield.name); 
	   	for(i=0;i<mi.length;i++){
			if(mi[i].testi == "testi"){
				id = i;
				moreLanguagefield.testi = "";	
			}	
		}
		var flan=moreLanguagefield.name.substr(0,moreLanguagefield.name.lastIndexOf("_"));
		var cf=document.getElementsByName(flan);
		var url = "/common/moreLanguage.jsp?popupWin=Language&len="+len+"&str="+encodeURI(cf[id].value);
		asyncbox.open({id:'Language',title:'多语言',url:url,width:530,height:300});
	}
	
	/* 返回数据 */
	function fillLanguage(str){
		if(str == ""){
			//清空数据
			jQuery("#"+moreLanguagefield.name).val("");
			jQuery("#"+moreLanguagefield.name.substr(0,moreLanguagefield.name.lastIndexOf("_"))).val("");
		}
		var strs=str.split(";");
	    for(var i=0;i<strs.length;i++){
		    var lanstr=strs[i].split(":");
			if(lanstr[0]=='$globals.getLocale()'){
			    moreLanguagefield.value=lanstr[1];break;
			}
		}
		moreLanguagefield.testi = "testi";	
		var id = 0;	
	   	var mi = document.getElementsByName(moreLanguagefield.name); 
	   	for(i=0;i<mi.length;i++){
			if(mi[i].testi == "testi"){
				id = i;
				moreLanguagefield.testi = "";	
			}	
		}
		var flan=moreLanguagefield.name.substr(0,moreLanguagefield.name.lastIndexOf("_"));
		var cf=document.getElementsByName(flan);
		cf[id].value = str;
	}
	
	function showcondition(){
		var conditionStatus = jQuery("#conditionStatus").val();
		if(conditionStatus == "0"){
			jQuery("#con_tr").show();
		}else{
			jQuery("#con_tr").hide();
		}
	}
</script>
<style type="text/css">
	.inpsearch_btn{
		background: url(/style/images/client/bg.gif) no-repeat 1000px 1000px;
		width:40px;
		background-position: right -413px;
		height: 20px;
		color:#fff;
		display: block;
		text-indent: -999em;
		overflow: hidden;
		font-size: 1px;
	}
	.td_right{
		text-align: right;
		padding-right: 5px;
		vertical-align: bottom;
	}
	* {
		margin: inherit;
	}
	#_context select{
		width:120px;
	}
	.deloption{
		padding-left: 10px;
		padding-top: 20px;
		cursor: pointer;
	}
	.closel:hover{text-decoration:underline; cursor:pointer;}
	.se_td{width:70px;}
</style>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" id="form" name="form" action="/AlertSetAction.do" target="formFrame">
#if("$!bean.id" == "")
<input type="hidden" id="operation" name="operation" value="1"/>
#else
<input type="hidden" name="id" id="id" value="$!bean.id"/>
<input type="hidden" id="operation" name="operation" value="2"/>
<input type="hidden" id="statusId" name="statusId" value="$!bean.statusId"/>
<input type="hidden" id="createby" name="createby" value="$!bean.createBy"/>
<input type="hidden" id="createtime" name="createtime" value="$!bean.createtime"/>
<input type="hidden" id="old_alertName" name="old_alertName" value="$!bean.AlertName"/>
<input type="hidden" id="old_bewrite" name="old_bewrite" value="$!bean.bewrite"/>
#end
#set($userids = "")
#foreach($!det in $!bean.detList)
	#set($userids = $userids+$!det.alertUser+",")
#end
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="$!userids"/>
<div class="boxbg2 subbox_w660" style="width:100%">
<div class="subbox cf">
<div class="operate operate2" id="handle">
  <ul>
  	<li class="sel">预警项目设置</li>
  </ul>
  <!-- <div class="closel" style="width: inherit;color: blue;"><label style="text-decoration: none;font-size: 14px;" onclick="showHelp()">输入帮助</label></div> -->
  </div>

<div class="listRange_frame" id="listRange_frame" style="padding-top: 10px;padding-left: 10px;width:98%;">
<div id="conter" style="border: #81b2e3 1px solid;">
	<table border="0" width="100%" style="margin: 5px 5px 5px 5px;">
	<tr>
		<td class="td_right">所属分组：</td>
		<td>
			<select name="moduleType" id="moduleType">
				#foreach($group in $groupList)
					<option value="$!globals.get($group,0)" #if("$!bean.ModuleType"=="$!globals.get($group,0)") selected #end>$globals.get($group,1)</option>
				#end
			</select>
		</td>
	</tr>
	<tr>
		<td class="td_right">预警名称：</td>
		<td>
		<input type="hidden" id="alertName" name="alertName" value="$!bean.alert_lan" />
		<input name="alertName_lan" id="alertName_lan" value="$!globals.getLocaleDisplay($!bean.alert_lan,$!globals.getLocale())" type="text" style="width: 200px;" size="1" onkeydown="if(event.keyCode==13) moreLanguage(500,this);" #if("$!detail"!="true") ondblclick="moreLanguage(500,this);" #end readonly="readonly" /><font color="red">*</font></td>
	</tr>
	<tr><td class="td_right">来源模板：</td><td>
		<input type="hidden" name="modelId" id="modelId" value="$!bean.modelId"/>
		<input name="modelName" type="text" id="modelName" value="$!bean.modelName" style="width: 200px;" #if("$!detail"!="true") ondblclick="popupSelectReport('选择来源报表','modelName','modelId')" #end readonly="readonly"></input>#if("$!detail"!="true")<img style="padding-left: 10px;" onclick="popupSelectReport('选择来源报表','modelName','modelId')" src="/style1/images/search.gif"/>#end
		<font color="red">*</font></td></tr>
	<tr><td class="td_right">显示：</td><td>
		<select name="isHidden" id="isHidden" class="se_td">
			<option value="0">是</option>
			<option value="1" #if("$!bean.isHidden"=="1") selected #end>否</option>
		</select></td></tr>
	<tr><td class="td_right">启用：</td><td>
		<select name="status" id="status" class="se_td">
			<option value="0">是</option>
			<option value="1" #if("$!bean.Status"=="1") selected #end>否</option>
		</select>
	</td></tr>
	<tr><td class="td_right">执行时间：</td><td>
		<select name="actionTime" id="actionTime" class="se_td">
		#foreach($foo in [0..23])
			#if($foo<10)
				<option value="0$foo" #if("$!bean.ActionTime"=="0$foo") selected #end>0$foo</option>
			#else
				<option value="$foo" #if("$!bean.ActionTime"=="$foo") selected #end>$foo</option>
			#end
		#end
		</select>点</td></tr>
	<tr><td class="td_right">执行频率：</td><td>
		<select name="actionFrequency" id="actionFrequency">
		 	#foreach($foo in [1..5])
		 		<option value="$foo" #if("$!bean.ActionFrequency"=="$foo") selected #end>每天$foo次</option>
		 	#end
		 </select>
		</td></tr>
	<tr><td class="td_right">条件状态：</td><td>
		<select name="conditionStatus" id="conditionStatus" class="se_td" onchange="showcondition()">
			<option value="0">显示</option>
			<option value="1" #if("$!bean.conditionStatus"=="1") selected #end>隐藏</option>
		</select>
		</td></tr>
	<tr id="con_tr"><td class="td_right">条件概述：</td><td>
		<input type="hidden" id="bewrite" name="bewrite" value="$!bean.bewrite_lan" />
		<input name="bewrite_lan" id="bewrite_lan" value="$!globals.getLocaleDisplay($!bean.bewrite_lan,$!globals.getLocale())" type="text" style="width: 200px;" size="1" onkeydown="if(event.keyCode==13) moreLanguage(500,this);" #if("$!detail"!="true") ondblclick="moreLanguage(500,this);" #end readonly="readonly" />
		如：提前input天预警


		</td></tr>
	<tr><td class="td_right" style="vertical-align: top;">条件：</td><td>
		<input type="text" name="condition" id="condition" value="$!bean.condition" style="width: 200px;"/>
		如：HideBillDate=2<span style="color:red;">*</span>
	</td></tr>
	<tr><td class="td_right" style="vertical-align: top;">描述：</td><td>
		<textarea rows="4" cols="80" name="remark" id="remark">$!bean.Remark</textarea>
	</td></tr>
	<tr><td class="td_right">提醒方式：</td><td>
		#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
 			<input type="checkbox" id="alertType" name="alertType" #foreach($t in $!bean.alertTypes) #if($row_wakeUpType.value==$t) checked="checked"  #end #end value="$row_wakeUpType.value"/>$row_wakeUpType.name
  		#end
	</td></tr>
	<tr><td class="td_right" style="vertical-align: top;">提示用户：</td><td>
		<div id="_context" style="float: left;">
			#if("$!detail"!="true")
			<div>
				<img src="/$globals.getStylePath()/images/St.gif" onclick="deptPopUser('formatUserName','popedomUserIds');" alt="$text.get("oa.common.advicePerson")" class="search" style="cursor: pointer;"/>&nbsp;
				<a href="javascript:void(0)" title="选择用户" onclick="deptPopUser('formatUserName','popedomUserIds');">选择用户</a>
			</div>
			#end
			<select name="formatUserName" id="formatUserName" multiple="multiple">
			#foreach($!det in $!bean.detList)
				<option value="$!det.alertUser">$!det.empFullName</option>
			#end
			</select>
		</div>
		#if("$!detail"!="true")
		<div>
			<img onclick="deleteOpation('formatUserName','popedomUserIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search deloption" />
		</div>
		#end
		</td>
	</tr>
	</table>
</div>
</div>
</form>
</body>
</html>

