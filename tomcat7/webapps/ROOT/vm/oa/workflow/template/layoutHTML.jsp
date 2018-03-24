<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
		<title> $!tableCHName表单设计</title>
		<link rel="stylesheet" type="text/css" href="/style/themes/table-design.css" />
		<script language="javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/vm/oa/workflow/template/fckeditor/fckeditor.js"></script>
		<script language="javascript" src="/js/oa/ajaxfileupload.js"></script>
		<script>
var curFName = "";
var curTitle = "";
function exec_cmd(inputType,inputTypeOld,fieldType,fName,title) {
	
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	curTitle = title;
	curFName = fName;
	
	
	if(fieldType=='3' || fieldType=='16'){//备注,html
		commandName = 'AIOTextarea';
		if($("textarea[fName="+fName+"]",oEditor.EditingArea.Window.document).size() > 0){
			alert($("#bt_"+fName).text().trim()+"已经存在,不能再添加");
			return;
		}
	}else if(inputType=='5' || inputTypeOld=='5'){//复选框
		commandName = 'AIOCheckbox';
		if($("input[fName="+fName+"]",oEditor.EditingArea.Window.document).size() > 0){
			alert($("#bt_"+fName).text().trim()+"已经存在,不能再添加");
			return;
		}
	}else if(inputType=='10' || inputTypeOld=='10'){//单选框
		commandName = 'AIORadio';
		if($("input[fName="+fName+"]",oEditor.EditingArea.Window.document).size() > 0){
			alert($("#bt_"+fName).text().trim()+"已经存在,不能再添加");
			return;
		}
	}else if(inputType=='1' || inputType=='16' || inputTypeOld=='1' || inputTypeOld=='16' ) {//枚举,动态选项
		commandName = 'AIOSelect';
		if($("select[fName="+fName+"]",oEditor.EditingArea.Window.document).size() > 0){
			alert($("#bt_"+fName).text().trim()+"已经存在,不能再添加");
			return;
		}
	}else{
		commandName = 'AIOTextField';
		if($("input[fName="+fName+"]",oEditor.EditingArea.Window.document).size() > 0){
			alert($("#bt_"+fName).text().trim()+"已经存在,不能再添加");
			return;
		}
	}	
		
	oEditor.Commands.GetCommand(commandName).Execute() ;
}	
function exec_detail(){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	oEditor.Commands.GetCommand("AIODetail").Execute() ;
}
function hideBt(bt){ 
	//$("#bt_"+bt).hide();
}
function beforeSumbmit(){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	if (oEditor.EditMode == FCK_EDITMODE_SOURCE){
		oEditor.Commands.GetCommand('Source').Execute() ;
	}
	
	var FORM_HTML2 = oEditor.EditingArea.Window.document.body.innerHTML;  
	if(FORM_HTML2.indexOf("<style type=\"text/css\">p{margin:0px}</style>")==-1){
		FORM_HTML2 = '<style type="text/css">p{margin:0px}</style>' + FORM_HTML2;
	}
	$("#layOutHtml").val(FORM_HTML2); //表单的内容   	
		
	myform.submit();
}

function FCKeditor_OnComplete(){
	jQuery.get("/OAWorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&turnType=layoutHTMLData&tableName=$!tableName",function(data){
   		
   		var oEditor2 = FCKeditorAPI.GetInstance('FCKeditor1') ;
   		if(data!="failure"){
   			oEditor2.SetData(data);
   		}  	 	
  	 	
  	 	$("input[inputtype]",oEditor2.EditingArea.Window.document).each(function(){
  	 		$(this).val($(this).attr("title"));
  	 		//$("#bt_"+$(this).attr("fName"),document).hide();
  	 	});
  	 	$("select[inputtype]",oEditor2.EditingArea.Window.document).each(function(){
  	 		$(this).val($(this).attr("title"));
  	 		//$("#bt_"+$(this).attr("fName"),document).hide();
  	 	});
  	 	$("textarea[inputtype]",oEditor2.EditingArea.Window.document).each(function(){
  	 		$(this).val($(this).attr("title"));
  	 		//$("#bt_"+$(this).attr("fName"),document).hide();
  	 	});
   	}); 
}



//提交表单
function submitForm(){
	$("#saveType").val("save");
	if(!beforeSumbmit()){
		return false;
	}	
}

//预览表单
function viewFlow(){
	$("#saveType").val("view");
	if(!beforeSumbmit()){
		return false;
	}
}


//页面跳转
function pageTurn(){
	var width=screen.width ;
	var height=screen.height;
	var saveType=$("#saveType").val();
	if(saveType=="view"){ //预览
		var url="/OAWorkFlowAction.do?tableName=$!tableName&designId=$!designId&src=menu&operation=$globals.getOP("OP_ADD_PREPARE")&type=viewFlow";
		$("#saveType").val("");
		window.open(url,"","width="+width+",height="+height);
	}
}


var flag="false";
function closeWin(){
	if(!confirm("您确定退出表单设计器?")){
		return false;
	}
	window.close();
}



</script>
	</head>
	<iframe name="formFrame" style="display:none"></iframe>
	<body>
		<form name="myform" id="myform" action="/OAWorkFlowTableAction.do" method="post" target="formFrame">
			<input type="hidden" name="operation" id="operation" value="2" />
			<input type="hidden" name="tableName" id="tableName" value="$!tableName" />
			<input type="hidden" name="tableCHName" id="tableCHName" value="$!tableCHName" />
			<input type="hidden" name="designId" id="designId" value="$!designId"/>
			<input type="hidden" name="fieldStr" id="fieldStr" value="" />
			<input type="hidden" name="newfieldStr" id="newfieldStr" value="" />
			<input type="hidden" name="layOutHtml" id="layOutHtml" value="" />
			<input type="hidden" name="maxValue" id="maxValue" value="$!maxValue" /><!-- 记录表单设计页面，字段后缀值最大值 -->
			<input type="hidden" name="saveType" id="saveType" value="$!saveType" />
			<input type="hidden" name="turnType" id="turnType" value="$!turnType" />
			<input type="hidden" name="curNum" id="curNum" value="" /><!-- 记录表单设计页面，字段后缀值 -->
			<span style="display: none;" id="test"></span>
			
			<div class="table-layout">
				<div class="table_top">
					<span>表单智能设计器：首先，从WORD文档中将表格框架粘贴到表单设计区。然后，创建表单控件。</span>
				</div>
				<div class="table_left">
					<span>操作控件</span>
					<div class="button_list">
						<button onClick="submitForm();" type="button">
							<img src="/js/img/save.png" />
							保存界面
						</button>
						<button onClick="closeWin();" type="button" />
							<img src="/js/img/close.gif" />
							关闭设计器 
						</button>
					</div>
					<span>表 单 字 段</span>
					<div class="button_list" style="height:380px;overflow:hidden;overflow-y:auto;">
						
						#foreach($row in $fields)
						#if($row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother" && "$row.fieldName"!="checkPersons" && 
							"$row.fieldName"!="classCode" && "$row.fieldName"!="workFlowNode" && "$row.fieldName"!="workFlowNodeName" && 
							"$row.fieldName"!="createBy" && "$row.fieldName"!="lastUpdateTime"	&& "$row.fieldName"!="SCompanyID" && 
							"$row.fieldName"!="statusId" && "$row.fieldName"!="lastUpdateBy" && "$row.fieldName"!="createTime" && 
							"$row.fieldName"!="isCatalog" && "$row.fieldName"!="finishTime" && "$row.inputType"!="100" && "$row.inputType"!="3" &&
							"$row.fieldType"!="13" && "$row.fieldType"!="14")
						
						#if("$row.inputType" != "2" && "$row.inputTypeOld" != "2") 
						<button id = "bt_$row.fieldName" isNull="$row.isNull" onClick="exec_cmd($row.inputType,$row.inputTypeOld,'$row.fieldType','$row.fieldName','$row.display.get("$globals.getLocale()")');" type="button">
							$row.display.get("$globals.getLocale()")
						</button>
						#else
						
			
	#if("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
		#set($dismh="$row.fieldName"+"_mh")          
		<button id = "bt_$dismh" isNull="$row.isNull" onClick="exec_cmd(2,2,'$row.fieldType','$dismh','$row.display.get("$globals.getLocale()")');" type="button">
				$row.display.get("$globals.getLocale()")
		</button>
	#elseif(!$row.getSelectBean().relationKey.hidden)
		<button id = "bt_$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" isNull="$row.isNull" onClick="exec_cmd(2,2,'$row.fieldType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)','$row.display.get("$globals.getLocale()")');" type="button">
				$row.display.get("$globals.getLocale()")
		</button>		
	#end
	#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
		#foreach($srow in $row.getSelectBean().viewFields)	
			#set($colName="")
   			#if($!srow.display!="")
				#if($!srow.display.indexOf("@TABLENAME")==0)
					#set($index=$srow.display.indexOf(".")+1)#set($tableField=$tableName+"."+$srow.display.substring($index))
				#else
					#set($tableField=$srow.display)
				#end
				#set($colName="$srow.display")
			#else
				#set($tableField="")
				#set($colName="$srow.fieldName")
			#end
			#if($!srow.display!="" && $!srow.display.indexOf(".")==-1)
				#set($dis = $!srow.display )
			#else
				#set($dis = $globals.getFieldDisplay($tableName,$row.getSelectBean().name,$tableField))
				#if($dis == "") #set($dis = $globals.getFieldDisplay($srow.fieldName)) #end
			#end
				
	 		<button id = "bt_$globals.getTableField($srow.asName)" isNull="0" onClick="exec_cmd(2,2,'$!globals.getFieldBean($srow.asName).fieldType','$globals.getTableField($srow.asName)','$dis');" type="button">
				$dis
			</button>				
		#end				
	#end		
						
						#end
						#end
						#end
						
						#set($hasDetail = false)
						#foreach($row in $fields)
						#if(("$row.fieldType"=="13" || "$row.fieldType"=="14")  && "$row.inputType"!="100" && "$row.inputType"!="3"  )
						#set($hasDetail = true) 
						#end
						#end
						
						#foreach($child in $childTables)
						#set($hasDetail = true)							
						#end
						
						#if($hasDetail)
						<button onClick="exec_detail();" id = "bt_AIOdetail" type="button">
							明细 图片 附件
						</button>
						#end
						<ul id="links"></ul>
					</div>
				</div>

				<div class="table_right">
        <script type="text/javascript">
			var fckHeight = document.documentElement.clientHeight-81;
			var sBasePath = document.location.href.substring(0,document.location.href.lastIndexOf('vm'))+"/vm/oa/workflow/template/fckeditor/" ;
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
