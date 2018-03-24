<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8"/>
<title>表单智能设计器</title>
<link rel="stylesheet" type="text/css" href="/style/themes/table-design.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/fckeditor/fckeditor.js"></script>
<script>

var lastField = 1 ;
function exec_cmd(commandName) {
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	oEditor.Commands.GetCommand(commandName).Execute() ;
}	

function beforeSumbmit(){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	var FORM_HTML = oEditor.EditingArea.Window.document.body.innerHTML;  
	
	var newfieldStr=getData();
	$("#newfieldStr").val(newfieldStr);
	
	$("#layOutHtml").val(FORM_HTML);
	if ( oEditor.EditMode == FCK_EDITMODE_SOURCE){
		oEditor.Commands.GetCommand('Source').Execute() ;
	}
	if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG ){
		oEditor.InsertHtml("<span id='lastField' value='"+lastField+"' style='display:none;'>&nbsp;</span>");
	}
	if($("#lastField",oEditor.EditingArea.Window.document.body).last().val()=="1"){
		alert("表单内容不能为空，请先设计表单!");
		return false;
	}
	myform.submit();
}

function FCKeditor_OnComplete(){
	jQuery.get("/OAWorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&tableName=$!tableName",function(data){
   		var oEditor2 = FCKeditorAPI.GetInstance('FCKeditor1') ;
   		oEditor2.SetData(data);
  	 	var fieldStr=getData();
		$("#fieldStr").val(fieldStr);
   	});
   	
}

//获取表单修改后的数据
function getData(){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	var fieldStr="";
	$("img",oEditor.EditingArea.Window.document.body).each(function(){
		if($(this).attr("imgtype")!="undefined"){
			var imgType=$(this).attr("imgtype");
			if(imgType=="affixAndPic"){
				imgType=$(this).attr("systype")
			}
			var temp=$(this).attr("name")+"|"+$(this).attr("title")+"|"+imgType;
			fieldStr=fieldStr+temp+"#";
		}
	});
	
	$("input",oEditor.EditingArea.Window.document.body).each(function(){
		var temp=$(this).attr("name")+"|"+$(this).attr("title")+"|"+$(this).attr("type");
		fieldStr=fieldStr+temp+"#";
	});
	
	$("textarea",oEditor.EditingArea.Window.document.body).each(function(){
		var temp=$(this).attr("name")+"|"+$(this).attr("title")+"|"+"textarea";
		fieldStr=fieldStr+temp+"#";
	});
	
	var selectStr="";
	$("select",oEditor.EditingArea.Window.document.body).each(function(){
		var temp=$(this).attr("name")+"|"+$(this).attr("title")+"|"+"select";
		fieldStr=fieldStr+temp+"#";
		
	});
	return fieldStr;
}

</script>
</head>

<body >
<form name="myform" action="/OAWorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE")" method="post">
  
<input type="hidden" name="tableName"   id="tableName" value="$!tableName"/>
<input type="hidden" name="tableCHName" id="tableCHName" value="$!tableCHName"/>
<input type="hidden" name="fieldStr"     id="fieldStr" value=""/>
<input type="hidden" name="layOutHtml" id="layOutHtml" value=""/>
<input type="hidden" name="newfieldStr"     id="newfieldStr" value=""/>

<div class="table-layout">
	<div class="table_top"><span>表单智能设计器：首先，将网页设计工具或Word编辑好的表格框架粘贴到表单设计区。然后，创建表单控件。</span></div>
	<div class="table_left">
        <span>保存或退出</span>
        <div class="button_list">
            <button onClick="beforeSumbmit();" type="button"><img src="/js/tabImg/save.png"/>保存表单</button>
            <button type="button"><img src="/js/tabImg/import.gif" />导入表单</button>
            <button type="button"><img src="/js/tabImg/export.gif" />导出表单</button>
            <button onClick="exec_cmd('Preview');" type="button" /><img src="/js/tabImg/view.gif"/>预览表单</button>
            <button onClick="window.close();" type="button"/><img src="/js/tabImg/close.gif"/>关闭设计器</button>
        </div>
        <span>表 单 控 件</span>
        <div class="button_list">
            <button onClick="exec_cmd('TextField');" type="button"><img src="/js/tabImg/textfield.gif"/>单行输入框</button>
            <button onClick="exec_cmd('Textarea');" type="button"><img src="/js/tabImg/textarea.gif"/>多行输入框</button>
            <button onClick="exec_cmd('Select');" type="button"><img src="/js/tabImg/listmenu.gif"/>下拉列表</button>
            <button onClick="exec_cmd('Radio');" type="button"><img src="/js/tabImg/radio.gif"/>单 选 框</button>
            <button onClick="exec_cmd('Checkbox');" type="button"><img src="/js/tabImg/check.gif"/>复 选 框</button>
            <button onClick="exec_cmd('DateField');" type="button"><img src="/js/tabImg/date_add.gif"/>日期控件</button>
            <button onClick="exec_cmd('AffixField');" type="button"><img src="/js/tabImg/textfield.gif"/>图片/附件</button>
            <button onClick="exec_cmd('CountField');" type="button"><img src="/js/tabImg/calculator.gif"/>计算控件</button>
            <button onClick="exec_cmd('HongField');" type="button"><img src="/js/tabImg/signature.gif"/>宏控件</button>
            <button onClick="exec_cmd('EmployeeField');" type="button"><img src="/js/tabImg/user_add.gif"/>人员选择框</button>
            <button onClick="exec_cmd('DeptField');" type="button"><img src="/js/tabImg/group_add.gif"/>部门选择框</button>
            <button onClick="exec_cmd('ClientField');" type="button"><img src="/js/tabImg/user_suit.gif"/>客户选择框</button>
            <button onClick="exec_cmd('PopupField');" type="button"><img src="/js/tabImg/tips.png"/>定制弹出框</button>
            <button onClick="exec_cmd('RepeatField');" type="button"><img src="/js/tabImg/tips.png"/>去重复控件</button>
        </div>
    </div>
    <div class="table_right">
    	<script type="text/javascript">
    		var fckHeight = document.documentElement.clientHeight-91;
			var sBasePath = document.location.href.substring(0,document.location.href.lastIndexOf('vm'))+"js/fckeditor/" ;
			var oFCKeditor = new FCKeditor('FCKeditor1') ;
			oFCKeditor.BasePath	= sBasePath ;
			oFCKeditor.Height= fckHeight ;	
    		oFCKeditor.Create() ;
		</script>
		
    </div>
</div>
</form>
</body>
</html>
