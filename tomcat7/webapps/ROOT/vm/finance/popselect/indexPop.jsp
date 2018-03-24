<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<base target="_self"></base>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会计科目</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/vm/oa/album/ui/ztree/demoStyle/demo.css" type="text/css"/>
<link rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/oa_news.css" type="text/css"/>
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css" />

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>
<script language="javascript"> 
var zTree1;
var setting;
var zNodes;
#if("$!folderTree"=="")
	zNodes = [];
#else
	zNodes=$!folderTree;
#end
setting = {
	checkable : true,
	expandSpeed : false,
	#if("$!PopupSearchForm.inputType"=="radio")
	checkStyle: "radio",
	#end
	checkType : {"Y":"s", "N":"ps"}, 
	showLine: true,
	callback: {
		change:	zTreeOnChange,
		dblclick: zTreeOnDBlClick,
		click: zTreeOnClick
	}
};

var values = "";
function zTreeOnClick(event, treeId, treeNode){
	if(treeNode!=null){
		#if("$!PopupSearchForm.chooseType"=="all")
			#if("$!isParent"=="false")
				if(!treeNode.isParent){
					//values = treeNode.tname;
				}
			#else
				//values = treeNode.tname;
			#end
		#elseif("$!chooseType"=="chooseChild")
			//if(!treeNode.isParent){
			//	values = treeNode.tname;
			//}
		#end
		var srcNode = zTree1.getSelectedNode();
		if (srcNode) {
			if(treeNode.open){
				zTree1.expandNode(srcNode, false, false);
			}else{
				zTree1.expandNode(srcNode, true, false);
			}
		}
		var url = "/PopupAction.do?operation=popDetail&popupName=$!popupName&selectType=group&returnName=$!PopupSearchForm.returnName&isCheckItem=$!PopupSearchForm.isCheckItem";
		url += "&chooseType=$!PopupSearchForm.chooseType&inputType=$!PopupSearchForm.inputType&isCease=$!PopupSearchForm.isCease&selectValue="+treeNode.id;
		jQuery("#firameMain").attr("src",url);
	}
}

function zTreeOnDBlClick(event, treeId, treeNode) {
	if(treeNode!=null){
		#if("$!PopupSearchForm.chooseType"=="all")
				if(typeof(parent.$!{PopupSearchForm.returnName})!="undefined"){
					parent.$!{PopupSearchForm.returnName}(treeNode.tname);
				}
				parent.jQuery.close('Popdiv');
		#elseif("$!PopupSearchForm.chooseType"=="chooseChild")
			//if(!treeNode.isParent){
			//	if(typeof(parent.$!{PopupSearchForm.returnName})!="undefined"){
			//		parent.$!{PopupSearchForm.returnName}(treeNode.tname);
			//	}
			//	parent.jQuery.close('Popdiv');
			//}
		#end
	}
}

function zTreeOnChange(event, treeId, treeNode) {
	var tmp = zTree1.getChangeCheckedNodes();
	var tmparr = "";
	var strarr = "";
	for (var i=0; i<tmp.length; i++) {
		if(tmp[i].checked){
			tmparr += tmp[i].tname;
		}
	}
	values = tmparr;
}

$(document).ready(function(){
	$("#keywordValue").focus();
	reloadTree();
});

function reloadTree(node) {
	var setting1 = clone(setting);
	setting1.treeNodeKey = "id";
	setting1.isSimpleData = true;
	zNodes1 = clone(zNodes);
	setting1.showLine = true;
	
	zTree1 = $("#treeDemo").zTree(setting1, zNodes1);
	var nodes = zTree1.getNodes();
	var url = "/PopupAction.do?operation=popDetail&popupName=$!popupName&returnName=$!PopupSearchForm.returnName&isCease=$!PopupSearchForm.isCease&chooseType=$!PopupSearchForm.chooseType&inputType=$!PopupSearchForm.inputType";
	url += "&selectType=$!PopupSearchForm.selectType&selectValue=$!PopupSearchForm.selectValue&isCheckItem=$!PopupSearchForm.isCheckItem";
	$("#firameMain").attr("src",url);
}	

function clears(){
	window.returnValue = "";
	window.close();
}

function querys(){
	if($("#keywordSearch").val()=="" && $("#accCodeSearch").val()==""
		&& $("#accNameSearch").val()=="" && $("#accFullNameSearch").val()==""){
		$("#classCode").remove();
	}else{
		$("#classCode").val('');
	}
	$("#strValue").val('');
	form.submit();
}

function closes(){
	window.close();
}

function datas(){
	return values;
}

function returns(classCode,value){
	$("#classCode").val(classCode);
	$("#strValue").val(value);
	form.submit();
}

//关键字搜索
function insertkeyword(){
	var keywordVal = jQuery("#keywordValue").val();
	if(keywordVal!="关键字搜索" && keywordVal.trim()!=""){
		jQuery("#selectType").val("keyword");
	}
	if(keywordVal=="关键字搜索"){
		keywordVal = "";
	}
	jQuery("#selectValue").val(keywordVal);
	form.submit();
}

/* 刷新树（重新加载框架） */
function showtree(){
	jQuery("#selectValue").val("");
	jQuery("#selectType").val("");
	form.submit();
}

/* 点击已选数据时 */
function chooseData(){
	var str = "";
	if(values!=""){
		//存在已选数据
		var strValue = values.split("|");
		for(var i = 0; i<strValue.length; i++){
			var strs = strValue[i].split(";")[0];
			if(strs != ""){
				str += "'"+strs+"',";
			}
		}
	}
	if(str.length>0){
		str = str.substring(0,str.length-1);
	}
	var url = "/PopupAction.do?operation=popDetail&popupName=$!popupName&returnName=$!PopupSearchForm.returnName&chooseType=$!PopupSearchForm.chooseType&inputType=$!PopupSearchForm.inputType&selectType=choose&isCease=$!PopupSearchForm.isCease&selectValue="+str;
	jQuery("#firameMain").attr("src",url);
}

</script>
<style type="text/css">
div.zTreeDemoBackground {
	width:auto;
}

ul#treeDemo {
	width:auto;
	overflow:auto;
}
.leftModule{
	width: 200px;
}
.leftMenu2{width:200px;}
.frame2{width:100%;}
</style>
</head>
<body style="text-align: left;">
<form method="post" scope="request" id="form" name="form" action="/PopupAction.do">
<input type="hidden" name="selectValue" id="selectValue" value="$!PopupSearchForm.selectValue"/>
<input type="hidden" name="popupName" id="popupName" value="$!popupName"/>
<input type="hidden" name="chooseType" id="chooseType" value="$!PopupSearchForm.chooseType"/>
<input type="hidden" name="selectType" id="selectType" value="$!PopupSearchForm.selectType"/>
<input type="hidden" name="inputType" id="inputType" value="$!PopupSearchForm.inputType"/>
<input type="hidden" name="returnName" id="returnName" value="$!PopupSearchForm.returnName"/>
<input type="hidden" name="isCease" id="isCease" value="$!PopupSearchForm.isCease"/>
<input type="hidden" name="isCheckItem" id="isCheckItem" value="$!PopupSearchForm.isCheckItem"/>
	<table cellpadding="0" cellspacing="0" class="frame2">
		<tr>
			<td width="30%">
				<div style="padding-top: 5px;padding-left: 30px;width: 90%"><input type="text" id="keywordValue" name="keywordValue" class="search_text" #if("$!PopupSearchForm.selectType"=="keyword") value="$!PopupSearchForm.selectValue" #else value="关键字搜索" #end  
				onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索';" 
				onfocus="if(this.value=='关键字搜索'){this.value='';}" /><input type="button" class="search_button" onclick="insertkeyword();"/>
				</div>
				<div class="leftModule" style="padding-left: 5px; border: 0px;">
				<div class="leftMenu">
					#if("$!PopupSearchForm.inputType" == "checkbox")
					<div class="leftMenu2" onclick="chooseData()">
						<span class="ico_1" id=""></span>已选数据
					</div>
					#end
					<div class="leftMenu2" onclick="showtree()">
						<span class="ico_2" id="change"></span>组目录
					</div>
					<div style="height:280px;overflow-y:auto;margin-top:5px;float:left;width:100%;">
						<div id="left_tree" style="display:block;float:left;margin-left: 10px;white-space: normal">
							<div class="zTreeDemoBackground">
								<ul id="treeDemo" class="tree"></ul>
							</div>		
						</div>
					</div>
				</div>
				</div>
			</td>
			<td valign="top" class="list" id="listTd">
				<iframe id="firameMain" frameborder=false src="" name="firameMain" style="margin-top: 10px;" height="360px;" scrolling="no" width="100%"></iframe>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
