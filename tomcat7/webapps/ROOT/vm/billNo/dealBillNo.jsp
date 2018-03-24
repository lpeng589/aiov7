<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>单据编号规则</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
	.td_right{
		text-align: right;
		padding-right: 5px;
		vertical-align: bottom;
	}
</style>
<script type="text/javascript">

	function paterns(){
		var patterns = "";
		jQuery(":input[name='patterns']").each(function(){
			var value = jQuery(this).val();
			if($(this).attr("id")!="patterns"){
				patterns = patterns.substring(0,patterns.length-1)+value+"}";
			}else{
				patterns += value;
			}
		});
		patterns = patterns.replace("undefined","");
		if(patterns == ""){
			//alert("单据编号规则不能为空！");
			return false;
		}
		//保存
		jQuery("#pattern").val(patterns);
	}
	
	function saves(type){
		paterns();
		if(type == "1"){
			$("#noback").val("true");
		}
		form.submit();
	}
	
	function billPatterns(){
			var options= "";
			options += "<option value='' ></option>";
			options += "<option value='{date yy}'>年年</option>";
			options += "<option value='{date MM}'>月月</option>";
			options += "<option value='{date dd}'>日日</option>";
			options += "<option value='{date HH:mm:ss}'>时时:分分:秒秒</option>";
			options += "<option value='{date HHmmss}'>时时分分秒秒</option>";
			options += "<option value='{date yyyy}'>年年年年</option>";
			options += "<option value='{date yyyyMMdd}'>年年年年月月日日</option>";
			options += "<option value='{date yyyy-MM-dd}'>年年年年-月月-日日</option>";
			options += "<option value='{date yyyy/MM/dd}'>年年年年/月月/日日</option>";
			options += "<option value='{login.getempFullName}'>用户全称</option>";
			options += "<option value='{login.getname}'>登录名</option>";
			options += "<option value='{login.getdepartCode}'>部门编号</option>";
			options += "<option value='{login.getdepartmentName}'>部门名称</option>";
			options += "<option value='{serial 000}'>流水号格式[000]</option>";
			options += "<option value='{serial 0000}'>流水号格式[0000]</option>";
			options += "<option value='{serial 00000}'>流水号格式[00000]</option>";
//			options += "<option value='{input.}'>输入名称</option>";
			return options;
	}
	
	function delRow(count){
		jQuery("#"+count).remove();
	}

	//option选中的值





	
	function checkboxs(obj){
		var value=$(obj).attr("lang");
		var objvalue = $(obj).val();
		if(objvalue.indexOf('input')!=-1){
			if(document.getElementById(value)==undefined){
				jQuery(obj).after("<input id='"+value+"' name='patterns' type='text' style='width:30px;height:13px;' onChange='checkcase()'/>");
			}
		}else{
			jQuery("input[id='"+value+"']").remove();
		}
	}
	
	function openTrade(){
		//var str  = window.showModalDialog("/BillNo.do?selectType=queryTable",winObj,"dialogWidth=450px;dialogHeight=420px");
		//if(typeof(str) != "undefined"){
		//	var strs=str.split(";");
		//	jQuery("#key").val(strs[2]+"_"+strs[3]);
		//	jQuery("#billName").val(strs[0]+"_"+strs[1]);
		//	validateBillNo();
		//}
		
		asyncbox.open({title : '模块标识选择',id : 'Popdiv',url : '/BillNo.do?selectType=queryTable&popupWin=Popdiv',width : 520,height : 420});
	}
	
	
	function evaluate(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var note = returnValue.split(";") ;
		jQuery("#key").val(note[2]+"_"+note[3]);
		jQuery("#billName").val(note[0]+"_"+note[1]);
		validateBillNo();
	}
	
	function addRow(){
		var count = jQuery("tr[id='noRole'] select").length;
		var htmls="<div id='"+count+"'>";
		var s =	billPatterns();
		for(var i=1;i<5;i++){
			var countnum = count+i;
			htmls+="<input name='patterns' id='patterns' type='text' style='width:30px;height:13px;' onchange='checkcase(this)'/>&nbsp;";
			htmls+="<select name='patterns' lang='lang_"+countnum+"' id='patterns' onchange='checkcase(this);checkboxs(this)' style='width:120px;'>";
	        htmls +=s;
			htmls += "</select>&nbsp;";
		}
		htmls += "<img title='删除' style='margin-top: 4px; margin-left: 10px; cursor: pointer;' onclick='delRow("+count+")' src='/style/images/plan/del_02.gif' complete='complete'/>";
		htmls+="<br/><div>";
		jQuery("td[id='copyTd']:last").append(htmls);
	}
	
	
	function checkcase(){
		var patterns = "";
		var start = jQuery("#start").val();
		if(!isInt(start) || start<0){
			alert("起始流水号必须为大于等于0的正整数!");
			jQuery("#start").focus();
			return false;
		}
		jQuery(":input[name='patterns']").each(function(){
			var value = jQuery(this).val();
			if($(this).attr("id")!="patterns"){
				patterns = patterns.substring(0,patterns.length-1)+value+"}";
			}else{
				//if(value.indexOf('serial')!=-1){
					//var numbers = value.substring(value.indexOf(' ')+1,value.length-1);
					//var number = numbers+start;
					//var numberstr = number.substring(number.length-numbers.length);
					//patterns += value.replace(numbers,numberstr);
				//}else{
					patterns += value;
				//}
			}
		});
		var url = "/BillNoAction.do?selectType=queryCase&patterns="+encodeURIComponent(patterns)+"&serial="+start;
		jQuery.ajax({type: "POST", url: url, success: function(result){
		    jQuery("#example").html(result);
		}});
	}
	
	/* 验证模块标识是否存在*/
	function validateBillNo(){
		var keys = jQuery("#key").val();
		var url = "/BillNoAction.do?selectType=validateBillNO&key="+keys+"&date="+new Date();
		jQuery.ajax({type: "POST", url: url, success: function(result){
			jQuery("#validateKey").val(result);
		}});
	}
	
	$(document).ready(function (){
		$("select[name='patterns']").each(function(){
			$(this).append(billPatterns());
		});
		#if("$!bean.id" != "")
			var patternvalue = "$!bean.pattern";
			patternvalue = patternvalue.replaceAll("{","|{").replaceAll("}","}|");
			if(patternvalue.lastIndexOf('|')==patternvalue.length-1){
				patternvalue = patternvalue.substring(0,patternvalue.length-1);
			}
			var value = patternvalue.split("|");
			var j=0;
			var lengths= value.length;
			if(lengths/8>1){
				addRow();
			}
			jQuery(":input[name='patterns']").each(function(){
				//存在
				if(value[j]!=undefined){
					var i=0;
					if(value[j].indexOf("input")!=-1){
						$(this).val("{input.}");
						checkboxs(this);
						var langid=$(this).attr("lang");
						$("#"+langid).val(value[j].substring(7,value[j].length-1));
					}else{
						$(this).val(value[j]);
					}
				}
				j++;
			});
			//$("#start").val(new Number($!bean.start)+1);
		#end
		checkcase();
		$("select[name='patterns']").change(function(){
			var value = $(this).val();
			if(value.indexOf('input')==-1){
				checkcase();
			}
		});
		$("input[name='patterns']").change(function(){
			checkcase();
		});
	});
</script>
<style type="text/css">
	.inpsearch_btn{
		background: url(/style/images/client/bg.gif) no-repeat 1000px 1000px;
		width:20px;
		background-position: right -413px;
		height: 20px;
		color:#fff;
		display: block;
		text-indent: -999em;
		overflow: hidden;
		font-size: 1px;
	}
</style>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" id="form" name="form" action="/BillNo.do" target="formFrame">
#if("$!bean.id" == "")
<input type="hidden" id="operation" name="operation" value="1"/>
#else
<input type="hidden" name="id" id="id" value="$!bean.id"/>
<input type="hidden" id="operation" name="operation" value="2"/>
<input type="hidden" id="oldkey" name="oldkey" value="$!bean.key"/>
#end
<input type="hidden" name="validateKey" id="validateKey" value=""/>
<input type="hidden" name="noback" id="noback" value=""/>
<input type="hidden" name="pattern" id="pattern" value="$!bean.pattern"/>
<!-- <div class="Heading">
	<div class="HeadingIcon"><img src="/style1/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">单据编号规则设置 </div>
	<ul class="HeadingButton">
		<li><button type="button" name="saveAdd" title="Ctrl+D" onClick="saves(0)" class="b4">保存</button></li>
		#if("$!bean.id" == "")
		<li><button type="button" name="copySave" title="Ctrl+S" onClick="saves(1)" class="b2">保存返回</button></li>
		#end
		<li><button type="button"  name="backList" title="Ctrl+Z" onClick="window.location.href='/BillNoAction.do?operation=4'" class="b2">返回</button></li> 
	</ul>
</div>-->
<div class="boxbg2 subbox_w660" style="width:100%">
<div class="subbox cf">
<div class="operate operate2" id="handle">
  <ul>
  	<li class="sel">单据编号规则设置</li>
  </ul>
  <div class="closel"></div>
  </div>

<div class="listRange_frame" id="listRange_frame" style="padding-top: 10px;padding-left: 10px;width:98%;">
<div id="conter" style="border: #81b2e3 1px solid;">
	<table border="0" width="100%" style="margin: 5px 5px 5px 5px;">
	<tr><td class="td_right">模块标识：</td><td>
		<input name="key" id="key" value="$!bean.key" style="width: 200px;" ondblclick="openTrade()" readonly="readonly"></input><img style="padding-left: 10px;" onclick="openTrade()" src="/style1/images/search.gif"/>
		</td></tr>
	<tr><td class="td_right">模块名称：</td><td><input name="billName" id="billName" value="$!bean.billName" style="width: 200px;"/></td></tr>
	<tr id='noRole'><td class="td_right" nowrap="" style="vertical-align: top;">单据编号规则：</td><td id="copyTd">
	#foreach ( $foo in [1..4] )
		<input name="patterns" id="patterns" type="text" style="width:30px;height:13px;" />&nbsp;<select name="patterns" lang="lang_$foo" id="patterns" onchange="checkboxs(this)" style="width:120px;"></select>
	#end
	<img title="增加" style="margin-top: 4px; margin-left: 5px; cursor: pointer;" onclick="addRow()" src="/style/images/plan/Ic_005.gif" complete="complete"/>
	<br/></td></tr>
	<tr><td></td><td>
	<label>例如：<label id="example" style="margin-top: 10px;color:blue;"></label></label>
	</tr></tr>
	<tr><td class="td_right">起始流水号：</td><td><input name="start" id="start" #if("$!bean.start"=="")value="1"#else value="$!bean.start" #end onchange="checkcase()"/></td></tr>
	<tr><td class="td_right">流水号增量：</td><td><input name="step" id="step" #if("$!bean.step"=="")value="1"#else value="$!bean.step" #end/></td></tr>
	<tr><td class="td_right">重置周期：</td><td>
		<select name="reset" id="reset">
			<option value="1" #if("$!bean.reset"=="1")selected#end>&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;</option>
			<option value="2" #if("$!bean.reset"=="")selected#end #if("$!bean.reset"=="2")selected#end>&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;</option>
			<option value="5" #if("$!bean.reset"=="5")selected#end>&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;</option>
		</select>
	</td></tr>
	<tr><td class="td_right">经手人默认为登录者：</td>
		<td>
			<select name="isDefaultLoginPerson" id="isDefaultLoginPerson">
				<option value="0" #if("$!bean.isDefaultLoginPerson"=="0") selected #end>&nbsp;&nbsp;&nbsp;是&nbsp;&nbsp;</option>
				<option value="-1" #if("$!bean.isDefaultLoginPerson"=="-1") selected #end>&nbsp;&nbsp;&nbsp;否&nbsp;&nbsp;</option>
			</select>
		</td>
	</tr>
	<tr><td class="td_right" style="vertical-align: middle">补号规则：</td><td>
		<input type="radio" name="supplementNo" id="supplementNo1" value="0" checked class="td_right"/><label for="supplementNo1">新增时生成编号&nbsp;&nbsp;&nbsp;&nbsp;(新增时生成编号并占用，在新增界面可以看到编号，但即使不保存此号也不可再次使用)</label><br />
		<input type="radio" name="supplementNo" id="supplementNo2" value="1" #if("$!bean.isfillback"=="true" && "$!bean.isAddbeform"=="false") checked #end class="td_right"/><label for="supplementNo2">保存时生成编号&nbsp;&nbsp;&nbsp;&nbsp;(保存时才生成编号，在新增界面看不到编号，只在保存时才占用编号)</label><br />
		<input type="radio" name="supplementNo" id="supplementNo3" value="2" #if("$!bean.isfillback"=="true" && "$!bean.isAddbeform"=="true") checked #end class="td_right"/><label for="supplementNo3">补号<span style="padding-left: 70px;"></span> (保存时生成编号，如果前面的编号单据被删除，则会使用以前的编号)</label>
	</td></tr>
	</table>
</div>
</div>
</form>
</body>
</html>

