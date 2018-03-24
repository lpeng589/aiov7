<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>group</title>
<link rel="stylesheet" type="text/css" href="/$globals.getStylePath()/css/ListRange.css"  />
<link rel="stylesheet" type="text/css" href="/style1/css/oa_news.css" />
<link rel="stylesheet" type="text/css" href="/style1/css/sharingStyle.css" />
<link rel="stylesheet" type="text/css" href="/style/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css" />

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
	/* 双击tr回填数据 */
	function chooseData(obj){
		var value = $(obj).find("input[name=keyId]").val();
		var popDiv = $(".asyncbox_normal",parent.parent.document) ;
		if(popDiv.size()>1){
			var popId = popDiv.last().prev().attr("id");
			parent.parent.jQuery.opener(popId).$!{PopupSearchForm.returnName}(value);
		}else if(typeof(parent.parent.$!{PopupSearchForm.returnName})!="undefined"){
			parent.parent.$!{PopupSearchForm.returnName}(value);
		}
		parent.parent.jQuery.close('Popdiv');
	}
	
	function checkinput(valueinput,countnum){
		#if("$!PopupSearchForm.chooseType" == "chooseChild")
		if(countnum>0){
			return;
		}
		#end
		if($("#"+valueinput).attr("checked")){
			$("#"+valueinput).removeAttr("checked");
		}else{
			$("#"+valueinput).attr("checked","true");
		}
		#if("$!PopupSearchForm.inputType" == "radio")
			parent.values = $("#"+valueinput).val();
		#elseif("$!PopupSearchForm.inputType" == "checkbox")
			if($("#"+valueinput).attr("checked")=="checked"){
				parent.values += $("#"+valueinput).val();
			}else{
				delstrData($("#"+valueinput).val());
			}
		#end
		delitera();
	}
	
	function delitera(){
		var strDatas = parent.values;
		if(strDatas != ""){
			var str = "";
			var datasStr = "";
			var strings = strDatas.split("#|#");
			for(var i = 0; i<strings.length; i++){
				var talg = false ;
				if(strings[i]!=""){
					for(var j = i+1;j<strings.length;j++){
						if(strings[i]==strings[j]){
							talg = true;
						}
					}
					if(!talg){
						datasStr = datasStr+strings[i]+"#|#";
					}
				}
			}
			parent.values = datasStr;
		}
	}
	
	var isAllSelectSelected = false;
	function checkAll(name){
		var items = document.getElementsByName(name);
		for(var i=0;i<items.length;i++){
		    if(!items[i].disabled){
		       if(!isAllSelectSelected){
		       		parent.values += items[i].value;
		       }else{
		       		delstrData(items[i].value);
		       }
		       items[i].checked = !isAllSelectSelected;
		    }
		}
		delitera();
	  	isAllSelectSelected = !isAllSelectSelected;
	}
	
	//移除数据
	function delstrData(value){
		var strDatas = parent.values;
		if(strDatas != ""){
			var str = "";
			var strings = strDatas.split("|");
			for(var i = 0; i<strings.length-1; i++){
				if(value != strings[i]+"|"){
					str += strings[i];
					if(i<strings.length-1){
						str += "|";
					}
				}
			}
			parent.values = str;
		}
	}
	//加载此页面默认选中复选框
	function selectData(){
		var sData = parent.values;	
		if(sData != ""){
			var str = sData.split("#|#");
			for(var i = 0; i<str.length; i++){
				var strs = str[i].split("#;#")[0];
				var object = jQuery("#"+strs);
				if(typeof(object)=="undefined" || object == null){
			 		continue;
			 	}
			 	jQuery("#"+strs).attr("checked","true");
			}
		}
	}
	
	/* 点击文本框或者点击单选框时 */
	function checks(valueinput){
		#if("$!PopupSearchForm.inputType" == "radio")
			parent.values = $("#"+valueinput).val();
		#elseif("$!PopupSearchForm.inputType" == "checkbox")
			if($("#"+valueinput).attr("checked")=="checked"){
				parent.values += $("#"+valueinput).val();
			}else{
				delstrData($("#"+valueinput).val());
			}
		#end
		delitera();
	}
</script>
<style type="text/css">
ul{
	border:0; margin:0; padding:0;
}
#pagination-flickr li{
	font-size:12px;
	list-style:none;
}
#pagination-flickr a{
	color: #3397CE;
	border:solid 1px #C0C0C0;
	border-right:0px;
	float:left;
	padding:3px 6px;
	text-decoration:none;
}
#pagination-flickr a:link,#pagination-flickr a:visited {
	display:block;
	float:left;
	padding:3px 6px;
	text-decoration:none;
}
#pagination-flickr a:hover{
	border:solid 1px #666666;
}
</style>
</head>

<body #if("$!PopupSearchForm.inputType" == "checkbox")onload="selectData();"#end>
<iframe name="formFrame" style="display:none" ></iframe>
<form  method="post" name="form" action="/PopupAction.do?operation=popDetail&popupName=$!popupName&selectType=group&returnName=$!PopupSearchForm.returnName&chooseType=$!PopupSearchForm.chooseType&inputType=$!PopupSearchForm.inputType&isCease=$!PopupSearchForm.isCease&selectValue=$!PopupSearchForm.selectValue">
<input name="opertion" id="operation" value="" type="hidden"/>

		<div style="padding-left: 10px;"><strong>当前位置 - 
		#if("$!PopupSearchForm.selectType"=="choose")
			已选数据

		#else
			组目录

		#end
		</strong>
		</div>
		<div style="padding-top:10px;padding-left: 10px;overflow: hidden;overflow-y: auto;" id="data_list_id">
		<script type="text/javascript"> 
		var oDiv=document.getElementById("data_list_id");
		var sHeight=document.documentElement.clientHeight-50;
		oDiv.style.height=sHeight+"px";
		</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr>
			<td valign="top" class="list">
			<table cellpadding="0" width="100%" cellspacing="0" align="center">
				<thead>
				<tr>
					<td width="20px;">
					#if("$!PopupSearchForm.inputType" == "checkbox")
						<input type="checkbox" value="checkbox" id="selAll" name="selAll" onClick="checkAll('keyId')" #if("$!PopupSearchForm.selectType"=="choose")checked#end/>
					#end
					</td>
					<td>科目编号</td>
					<td>科目名称</td>
					<td>科目全称</td>
					<td>余额方向</td>
				</tr>
				</thead>
				#foreach($detail in $!detailList)
				<tr onMouseMove="setBackground(this,true);"	onMouseOut="setBackground(this,false);" #if("$!PopupSearchForm.chooseType" == "chooseChild" && "$!detail.countnumber"!="0")#else ondblclick="chooseData(this);" #end>
					<td>#if("$!PopupSearchForm.inputType" == "checkbox")<input type="checkbox" name="keyId" id="$!detail.AccNumber" value="$!detail.AccNumber#;#$!globals.replaceSpecLitter($!detail.AccFullName)#;# #|#" onclick="checks('$!detail.AccNumber')" #if("$!PopupSearchForm.chooseType" == "chooseChild"&&"$!detail.countnumber"!="0")disabled="disabled"#end/>#end
					#if("$!PopupSearchForm.inputType" == "radio")<input type="radio" name="keyId" id="$!detail.AccNumber" value="$!detail.AccNumber#;#$!globals.replaceSpecLitter($!detail.AccFullName)#;# #|#" onclick="checks('$!detail.AccNumber')" #if("$!PopupSearchForm.chooseType" == "chooseChild"&&"$!detail.countnumber"!="0")disabled="disabled"#end/>#end
					</td>
					<td title="$!detail.AccNumber" onclick="checkinput('$!detail.AccNumber','$!detail.countnumber')" style="text-align: left;padding-left: 10px;">$!globals.subTitle("$!detail.AccNumber",20)</td>
					<td title="$!detail.AccName" onclick="checkinput('$!detail.AccNumber','$!detail.countnumber')" style="text-align: left;padding-left: 10px;">$!globals.subTitle("$!detail.AccName",22)</td>
					<td title="$!detail.AccFullName" onclick="checkinput('$!detail.AccNumber','$!detail.countnumber')" style="text-align: left;padding-left: 10px;">$!globals.subTitle("$!detail.AccFullName",22)</td>
					<td title="$!detail.AccNumber" onclick="checkinput('$!detail.AccNumber','$!detail.countnumber')">$!detail.isjdFlag</td>
				</tr>
				#end
			</table>
			</td>
			</tr>
		</table>
		#if($detailList.size()==0)
		<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">
			#if("$!PopupSearchForm.selectType"=="choose")
				暂无已选数据

			#else
				暂无数据
			#end
		</div>
		#end
		</div>
		<div class="listRange_pagebar">
			$!pageBar
		</div>
</form>
</body>
</html>