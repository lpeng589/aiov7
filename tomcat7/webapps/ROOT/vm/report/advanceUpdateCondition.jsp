<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("common.lb.print.FormatDeisgn")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" /> 
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<script type="text/javascript">
var condStr = "<option value=''></option>";
function loadCondition(){
	if(parent.dateConditions){
		condStr +="<option value='dateConditions'>"+parent.dateConditions[0]+"</option>";
	}
	if(parent.workFlowNodeNameCond){
		condStr +="<option value='workFlowNodeNameCond'>"+parent.workFlowNodeNameCond[0]+"</option>";
	}
	if(parent.otherConditions){
		for(var i = 0;i<parent.otherConditions.length;i++){
			if(parent.otherConditions[i][3] != "3"){
				condStr +="<option value='"+parent.otherConditions[i][1]+"'>"+parent.otherConditions[i][0]+"</option>";
			}
		}		
	}
	$("select[name=cond]").each(function(){
		$(this).empty();
		$(this).html(condStr);
		$(this).val($(this).attr("val"));
		changeCond(this);
		if($(this).val()=="dateConditions"){
			$("#date_start",document).val($(this).attr(parent.dateConditions[1]));
			$("#date_end",document).val($(this).attr(parent.dateConditions[2]));
		}else if($(this).val()=="workFlowNodeNameCond"){
			$("#"+parent.workFlowNodeNameCond[1],document).val($(this).attr(parent.workFlowNodeNameCond[1]));
		}else if($(this).val() !="" ){
			$("#"+$(this).val(),document).val($(this).attr($(this).val()));
			if($(this).attr("hide_"+$(this).val())){
				$("#hide_"+$(this).val(),document).val($(this).attr("hide_"+$(this).val()));
			}
			for(var i=0;i<parent.otherConditions.length;i++){
				if(parent.otherConditions[i][1] == $(this).val()){
					var p11s = parent.otherConditions[i][11];
					if(p11s !=null && p11s != ""){
						var hidVals = $(this).attr("hide_"+$(this).val()).split("#;#");
						var p11 =p11s.split("#|#");
						var kcount = 1;
						for(var k=p11.length -1;k>=0;k--){
							var itm = p11[k].split("#;#");
							kcount ++;
							var hidv = hidVals[hidVals.length -kcount];
							var itmname = itm[1];
							itmname = itmname.replace(".","\\.");
							$("#"+itmname,document).val(hidv);						
						}
					}
					break;
				}
			}
		} 
	});
}
function openInputDate(obj)
{
	WdatePicker();
}

function addRowline(){
	var html = '<tr onclick="focustr(this)"><td ><select name="cond" onchange="changeCond(this)">'+condStr+'</select></td><td >&nbsp;</td></tr>';
	$("#tblSort tbody tr:last",document).after(html);
}
function delline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请先选择需要删除的字段");
		return;
	}
	if(curRow != null){
		$(curRow).remove();
		curRow = null;
	}
	curRowNo  = -1;
}
//上移单元格  
function upline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请选择需要上移的字段");
		return;
	}
	$(curRow).insertBefore($(curRow).prev());
}
//下移单元格  
function downline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请选择需要下移的字段");
		return;
	}
	$(curRow).insertAfter($(curRow).next());
}


var curRow = null;
function focustr(obj){
	curRow = obj;	
	if($("#tblSort tbody tr:last",document)[0] == obj){
		var html = '<tr onclick="focustr(this)"><td ><select name="cond" onchange="changeCond(this)">'+condStr+'</select></td><td >&nbsp;</td></tr>';
		$("#tblSort tbody tr:last",document).after(html);
	}
}
function getEnumeration(enumName,isNull){
	var ret="";
	var html = jQuery.ajax({ url: "/CommonServlet?operation=getEnumeration&enumName="+enumName, async: false   }).responseText; 
	if(html.length > 0){
		if(isNull!=1){
			ret +='<option value="" ></option>';
		}		
		html = html.substring(html.indexOf("<enum>")+6,html.indexOf("</enum>"));
		var items = html.split("</enumItem>");
		for(var i=0;i<items.length;i++){
			var item = items[i];
			if(item != ""){
				var pos = item.indexOf("value=\"")+7;
				var value = item.substring(pos,item.indexOf("\"",pos));
				pos = item.indexOf("zh_CN");
				pos = item.indexOf("display=\"",pos)+9;
				var name = item.substring(pos,item.indexOf("\"",pos));
				ret += '<option value="'+value+'">'+name+'</option>';
			}
		}
	}
	return ret;
}
function getCheckBox(itemName,enumName,isNull){
	var ret="";
	var html = jQuery.ajax({ url: "/CommonServlet?operation=getEnumeration&enumName="+enumName, async: false   }).responseText; 
	if(html.length > 0){				
		html = html.substring(html.indexOf("<enum>")+6,html.indexOf("</enum>"));
		var items = html.split("</enumItem>");
		for(var i=0;i<items.length;i++){
			var item = items[i];
			if(item != ""){
				var pos = item.indexOf("value=\"")+7;
				var value = item.substring(pos,item.indexOf("\"",pos));
				pos = item.indexOf("zh_CN");
				pos = item.indexOf("display=\"",pos)+9;
				var name = item.substring(pos,item.indexOf("\"",pos));
				ret += '<input type="checkBox" style="color: #717171;width:20px;border:0px;" class="ls3_in cbox" id="'+itemName+
					'_'+value+'" name="'+itemName+'" value="'+value+'"><label for="'+itemName+'_'+value+'" class="cbox_w">'+name+'</label>';
			}
		}
	}
	return ret;
}
function initDownSelect(sql,isNull){       
	    var retstr="";
	    var data = jQuery.ajax({ url: '/UtilServlet?operation=downSelect&sql='+encodeURIComponent(sql), async: false   }).responseText; 
	    
	    if(data.indexOf('ERROR:')>-1){alert(data.substring(6));return '';}
        if(isNull!=1){
			retstr +='<option value="" ></option>';
		}
		var ds = data.split('#;#');
	    for(i=0;i<ds.length;i++){
          	if(ds[i] !=''){
          		var itemDS = ds[i].split('#:#');
          		retstr += '<option value="'+itemDS[0]+'">'+itemDS[1]+'</option>';
          	}
	    }
 		return retstr;
} 

function exePopadvancediv(returnValue){ //弹出窗会先执行到这里回填
	parent.exePopadvancediv(returnValue);
}

function changeCond(obj){
	cond = $(obj).val();
	if(cond == "dateConditions"){
		var html = '<input type="hidden" name="cond_'+cond+'" value="'+parent.dateConditions[1]+':'+parent.dateConditions[2]+'"/><input id="date_start" class="ls3_in" style="border:1px solid #978FA0;width:135px;" name="'+parent.dateConditions[1]+'" date="true" value=""  onClick="openInputDate(this);" >-'+
				'<input id="date_end" class="ls3_in" style="border:1px solid #978FA0;width:135px;" name="'+parent.dateConditions[2]+'" date="true" value=""  onClick="openInputDate(this);" >';
		$(obj).parent().next().html(html);
	}else if(cond == "workFlowNodeNameCond"){ 
		var html = '<input type="hidden" name="cond_'+cond+'" value="'+parent.workFlowNodeNameCond[1]+'"/><select class="ls3_in" style="border:1px solid #978FA0;width:135px;"  id="'+parent.workFlowNodeNameCond[1]+'" name="'+parent.workFlowNodeNameCond[1]+'" >'
			+ getEnumeration(parent.workFlowNodeNameCond[4],parent.workFlowNodeNameCond[7]) +
			'</select>';
		$(obj).parent().next().html(html);
	}else {
		var html = "";
		if(parent.otherConditions){
			for(var i = 0;i<parent.otherConditions.length;i++){
				if(parent.otherConditions[i][1]==cond){
					if(parent.otherConditions[i][3]=="0"){
						html = 
							'<input class="ls3_in"  style="border:1px solid #978FA0;width:135px;" id="'+parent.otherConditions[i][1]+'" name="'+parent.otherConditions[i][1]+'" value="'+parent.otherConditions[i][2]+'" >';
					}else if(parent.otherConditions[i][3]=="1"){
						html = 
							'<select class="ls3_in" style="border:1px solid #978FA0;width:135px;" id="'+parent.otherConditions[i][1]+'" name="'+parent.otherConditions[i][1]+'" >'
						+ getEnumeration(parent.otherConditions[i][4],parent.otherConditions[i][7]) +
						'</select>';
					}else if(parent.otherConditions[i][3]=="16"){
						html = 
							'<select class="ls3_in" style="border:1px solid #978FA0;width:135px;"  id="'+parent.otherConditions[i][1]+'" name="'+parent.otherConditions[i][1]+'" >'
						+ initDownSelect(parent.otherConditions[i][12],parent.otherConditions[i][7]) +
						'</select>';
					}else if(parent.otherConditions[i][3]=="5"){
						html = 
							getCheckBox(parent.otherConditions[i][1],parent.otherConditions[i][4],parent.otherConditions[i][7]);
					}else if(parent.otherConditions[i][3]=="2"){
						html = 
							'<input id="'+parent.otherConditions[i][1]+'" class="ls3_in" style="border:1px solid #978FA0;width:135px;" name="'+parent.otherConditions[i][1]+'" date="true" value="'+parent.otherConditions[i][2]+'" onClick="openInputDate(this);" >';
					}else if(parent.otherConditions[i][3]=="4"){
						if(parent.otherConditions[i][11]==null || parent.otherConditions[i][11]==""){ //当11不为空时，是版本一的数据
							html = 
								'<input id="'+parent.otherConditions[i][1]+'" class="ls3_in h-icons" style="border:1px solid #978FA0;width:135px;" name="'+parent.otherConditions[i][1]+
									'" value="'+parent.otherConditions[i][2]+'" ondblclick="jQuery(this).next().click()"  relationkey="true">'
									+'<b class="stBtn icon16" onClick="parent.openadvanceSelect(\''+parent.otherConditions[i][6]+'\',\''+parent.otherConditions[i][1]+'\',\''+parent.otherConditions[i][0]+'\');"></b>';
							if(parent.otherConditions[i][10] != ""){
								html +='<input name="hide_'+parent.otherConditions[i][1]+'" id="hide_'+parent.otherConditions[i][1]+'"   value="" type="hidden"/>';
							}		
						}else{
							html = '';
							if(parent.otherConditions[i][11].indexOf(";"+parent.otherConditions[i][1]+";")==-1){
								html += '<input name="'+parent.otherConditions[i][1]+'" id="'+parent.otherConditions[i][1]+'" value="" type="hidden" />';
							}	
							if(parent.otherConditions[i][10] != ""){
								html +='<input name="hide_'+parent.otherConditions[i][1]+'" id="hide_'+parent.otherConditions[i][1]+'"   value="" type="hidden"/>';
							}
							var val11s = parent.otherConditions[i][11].split("#|#")
							for(var k=0;k<val11s.length;k++){
								var items = val11s[k].split("#;#");
								html +='<li><div style="float:left;width: 70px;"><span style="float: right;padding-right: 5px;">'+items[0]+'</span></div>'+
									'<input name="'+items[1]+'" id="'+items[1]+'" class="ls3_in"  style="border:1px solid #978FA0;width:135px;" value=""  ondblclick="jQuery(this).next().click()"   relationkey="true"/>'+
									'<b class="stBtn icon16" onClick="parent.openadvanceSelect(\''+parent.otherConditions[i][6]+'\',\''+items[1]+'\',\''+parent.otherConditions[i][0]+'\');" ></b></li>'
							}
						}
					}
				}
			}		
		}
		$(obj).parent().next().html(html);
	}
}
function beforSubmit(){
	if($("#name",document).val() == ""){
		alert("请填写方案名称");
		return;
	}
	hasOne = false;
	allCond = ",";
	dbCond = "";
	//是否至少有一个条件
	$("select[name=cond]",document).each(function(){
		if($(this).val() != ""){
			hasOne = true;
			if(allCond.indexOf($(this).val()+",") > -1){
				dbCond = $(this).find("option:selected").text();
			}else{
				allCond +=$(this).val()+",";
			}
		}
	});
	if(!hasOne){
		alert("请至少选择一个条件");
		return;
	}
	if(dbCond !=""){
		alert("条件"+dbCond+"重复多次");
		return;
	}
	//必选条件较验
	if(parent.workFlowNodeNameCond){
		if(parent.workFlowNodeNameCond[7]=="1"){
			hasCond = false;
			hasValue = false;
			//是否至少有一个条件
			$("select[name=cond]",document).each(function(){
				if($(this).val() == parent.workFlowNodeNameCond[1]){
					hasCond = true;
				}
			});
			if(!hasCond){
				alert(parent.workFlowNodeNameCond[0]+"为必选条件，请选择");
				return;
			}
		}		
	}
	if(parent.otherConditions){
		for(var i = 0;i<parent.otherConditions.length;i++){
			if(parent.otherConditions[i][7]=="1"){
				hasCond = false;
				hasValue = false;
				//是否至少有一个条件
				$("select[name=cond]",document).each(function(){
					if($(this).val() == parent.otherConditions[i][1]){
						hasCond = true;
					}
				});
				if(!hasCond){
					alert(parent.otherConditions[i][0]+"为必选条件，请选择");
					return;
				}
			}			
		}		
	}
	document.form.submit();
}
</script>
<style>
.tips_div{width:250px;height:147px;display: none;cursor:default;position:absolute;left:200px;top:50px;border:5px solid #B9E6FF; background-color:#fff;}
.tips_title{height:25px;line-height:25px;vertical-align:middle;background-color:#B9E6FF;padding:3px 0 0 0;}
.left_title{float:left;padding-left:10px;font-family:微软雅黑;font-size:14px;font-weight:bold;color:#000;}
.tips_close{padding:2px 2px 0 0;float:right;}
.tips_content{margin:20px 0 0 10px;text-align:center;float:left;}
.tips_operate{width:100%;margin:20px 0 10px 0;float:left;text-align:center;}
.HeadingButton{margin:0;}
.HeadingButton li{margin:0 5px 0 0;}
.stBtn {background-position: -32px 0;background-color: #fff;cursor: pointer;}
</style>
</head>
<body style="overflow: hidden;" onload="loadCondition()">
<div class="Heading">
	<div class="HeadingTitle"></div>
	<ul class="HeadingButton">
		<li><span onClick="javascript:beforSubmit();" class="btn btn-small">保存</span></li>
		<li><span onClick="javascript:parent.closeAdvanceUpdate();" class="btn btn-small">$text.get("common.lb.close")</span></li>		
	</ul>
	<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>
</div>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post"  scope="request" name="form"  action="/ReportDataAction.do?operation=advance&opType=save" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name = "id" value="$!id"/>
<input type="hidden" name = "reportNumber" value="$!reportNumber"/>
<div id="listRange_id">
			<li style="padding:5px 20px">
				<div class="swa_c1 d_6" style="float:left">方案名称：</div>
				<div class="swa_c2"  style="float:left;padding-right:50px">
					<input class="ls3_in" id="name" name="name" value="$!name" disableautocomplete="true" autocomplete="off">
				</div>
				<ul class="detbtBar">
					<li id="b_addline" class="f-icon16" onclick="addRowline()" title="添加字段"></li>
					<li id="b_upline" class="f-icon16" onclick="upline()" title="上移"></li>
					<li id="b_downline" class="f-icon16" onclick="downline()" title="下移"></li>	
					<li id="b_delline" class="f-icon16" onclick="delline()" title="删除字段"></li>						
				</ul>
			</li>
		<div class="scroll_function_small_a" id="conter" style="height: 440px;">
			<div id="formatList">
			<table border="0" width="550" cellpadding="0" cellspacing="0" class="listRange_list_function"  id="tblSort">
			<thead>
				<tr>
					<td width="100" align="center">条件</td>	
					<td width="*" align="center">条件值</td>		
				</tr>			
			</thead>
			<tbody>
				#foreach($row in $condList)
				<tr onclick="focustr(this)">
					<td ><select name="cond" onchange="changeCond(this)" val="$row" 
						#foreach($rowOne in $condMap.get($row)) $!globals.get($rowOne,1)="$!globals.get($rowOne,2)" hide_$!globals.get($rowOne,1)="$!globals.get($rowOne,3)"  #end ><option value=""></option></select></td>	
					<td >
					&nbsp;
					</td>		
				</tr>
				#end
				<tr onclick="focustr(this)">
					<td ><select name="cond" onchange="changeCond(this)"><option value=""></option></select></td>	
					<td >&nbsp;</td>		
				</tr>		
			</tbody>
			<tbody>
			
			</tbody>
			</table>
			</div>
		</div>
</div>
</form>
</body>
</html>