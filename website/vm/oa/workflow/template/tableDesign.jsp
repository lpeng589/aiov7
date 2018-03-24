<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
		<title> $!tableCHName表单设计</title>
		<link rel="stylesheet" type="text/css" href="/style/themes/table-design.css" />
		<script language="javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/fckeditor/fckeditor.js"></script>
		<script language="javascript" src="/js/oa/ajaxfileupload.js"></script>
		<script>

function exec_cmd(commandName) {
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	oEditor.Commands.GetCommand(commandName).Execute() ;
}	

function beforeSumbmit(){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	if (oEditor.EditMode == FCK_EDITMODE_SOURCE){
		oEditor.Commands.GetCommand('Source').Execute() ;
	}

	//判断html内容是否存在重复的标签（复制操作）  
	var FORM_HTML = oEditor.EditingArea.Window.document.body.innerHTML;  	
	if(!copyHtml(FORM_HTML)){
		alert("存在重复控件名称，请重新输入!");
		return false;
	}
	
	//判断删除的标签是否被其他标签引用
	if(!checkDelLabel()){
		return false;
	}
	var fieldStr=getData(); //编辑器中现有的内容

	if(fieldStr==false){
		return false;
	}

	fieldStr=replaceCountVal(fieldStr); /*替换计算控件的表达式*/

	var oldHtml=jQuery.trim($("#fieldStr").val()); //判断是否存在老的表单html
	
	var curNum=$("#curNum").val();
	if(curNum == 0){
		alert("表单内容必须包含表单控件，请先选择控件!");
		return false;
	}
	if(!isNullHtml()){  //判断html内容中是否包含标签控件
		return false;
	}
	if(oldHtml.length>0){
		$("#newfieldStr").val(fieldStr);  //新的表单内容
	}else{
		$("#fieldStr").val(fieldStr);
	}
	
	var FORM_HTML2 = oEditor.EditingArea.Window.document.body.innerHTML;  
	if(FORM_HTML2.indexOf("<style>p{margin:0px}</style>")==-1){
		FORM_HTML2 = '<style>p{margin:0px}</style>' + FORM_HTML2;
	}
	$("#layOutHtml").val(FORM_HTML2); //表单的内容   	
	var maxNum=parseInt(#if("$!maxValue"=="")"0"#else "$!maxValue"#end); 
	if(maxNum>0 || oldHtml.length>0){ //根据字段值判断是增加还是修改操作
		$("#operation").val("$globals.getOP("OP_UPDATE")");
	}else{
		$("#operation").val("$globals.getOP("OP_ADD")");
	}
	
	$("#exportForm").removeAttr("disabled");
	$("#viewForm").removeAttr("disabled");
	myform.submit();
}

function FCKeditor_OnComplete(){
	jQuery.get("/OAWorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&designId=$!designId&tableName=$!tableName",function(data){
   		var oEditor2 = FCKeditorAPI.GetInstance('FCKeditor1') ;
   		if(data!="failure"){
   			oEditor2.SetData(data);
   		}
  	 	if(data.length==0){
  	 		$("#exportForm").attr("disabled","disabled");
  	 		$("#viewForm").attr("disabled","disabled");
  	 	}else{
  	 		$("#oldlayOutHtml").val(data);
  	 	}
  	 	var fieldStr=getData();
		$("#fieldStr").val(fieldStr);
		pageTurn();
   	}); 
}

//判断html中是否包含控件标签


function isNullHtml(){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	var imgCount=$("img",oEditor.EditingArea.Window.document.body).size();
	var inputCount=$("input",oEditor.EditingArea.Window.document.body).size();
	var textCount=$("textarea",oEditor.EditingArea.Window.document.body).size();
	var selectCount=$("select",oEditor.EditingArea.Window.document.body).size();
	if(imgCount==0 && inputCount==0 && textCount==0 && selectCount==0){
		alert("表单内容必须包含控件，请先设计表单!");
		return false;
	}
	return true;
}

/*替换计算控件的表达式*/
function replaceCountVal(fieldStr){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	$("img",oEditor.EditingArea.Window.document.body).each(function(){
		if($(this).attr("imgtype")!="undefined" && $(this).attr("imgtype")=="count" ){
			var docText=$(this).attr("value");
			var newDocText=docText;
			var countIndex=newDocText.indexOf("count"); //计算公式
			var re=/{.[^{}]*}/g;  //正则公式获取标签title值


	
			var arr=newDocText.match(re);
			if(arr!=null){
				for(var i=0;i<arr.length;i++){
					var labelName=arr[i].replace("{","").replace("}","");
					var temp="";
					if(countIndex == 0){ //计算公式
			    		temp=jQuery("input[title='"+labelName+"']",oEditor.EditingArea.Window.document.body).attr("name");
			    	}else{
			    		temp=jQuery("img[title='"+labelName+"']",oEditor.EditingArea.Window.document.body).attr("name");
			    	}
					newDocText=newDocText.replace(labelName,temp);
				}
				fieldStr=fieldStr.replace(docText,newDocText);
			}
		}
	});
	return fieldStr;
}

//获取表单中的数据
function getData(){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	var fieldStr=""; //保存html内容

	var labelNum=0;  //标签Id
	var maxNum=parseInt("$!maxValue"); //修改页面的话
	if(maxNum>labelNum){
		labelNum=maxNum;
	}	
	var oldlabelNum=labelNum;
	$("img",oEditor.EditingArea.Window.document.body).each(function(){
		if($(this).attr("imgtype")!="undefined"){
		    if(typeof($(this).attr("title"))=="undefined"){
				alert("有控件名称为空");
				$(this).addClass("redInput");
				return false;
			}
		     //单个控件新增                                         -----------------//复制控件或导入html文件 ------- 
			if(typeof($(this).attr("name"))=="undefined" || (typeof($(this).attr("id"))!="undefined" && $(this).attr("id").indexOf("field_")!=0 ) || oldlabelNum==0 || fieldStr.indexOf($(this).attr("name")+"|") > -1 ) { //新增的标签或者从外部复制的html内容，此时需要生成新的id和name
				labelNum=labelNum+1;
				var fieldId="field_"+labelNum;
				$(this).attr("id",fieldId);
				$(this).attr("name",fieldId);
			}
			
			var imgType=$(this).attr("imgtype");
			
			if(imgType=="affixAndPic"){
				imgType=$(this).attr("systype");
			}else if(imgType=="popup"){
				imgType=imgType+"&&"+$(this).attr("value");
			}else if(imgType=="count"){
				var docText=$(this).attr("value");
				imgType=imgType+"&&"+docText;
			}
			var isnull="";
			if($(this).attr("isnull")=="true"){
				isnull="true";
			}else{
				isnull="false";
			}
			var temp=$(this).attr("name")+"|"+$(this).attr("title")+"|"+imgType+"|"+isnull;
			fieldStr=fieldStr+temp+"#";
		}
	});
	
	$("input",oEditor.EditingArea.Window.document.body).each(function(){
		if(typeof($(this).attr("title"))=="undefined"){
			alert("有控件名称为空");
			//alert($(this).attr("class"));
			$(this).addClass("redInput");
			return false;
		}
		if(typeof($(this).attr("name"))=="undefined" ||(typeof($(this).attr("id"))!="undefined" && $(this).attr("id").indexOf("field_")!=0 ) || oldlabelNum==0 || fieldStr.indexOf($(this).attr("name")+"|") > -1 ){ //新增的标签或者从外部复制的html内容，此时需要生成新的id和name
			labelNum=labelNum+1;
			var fieldId="field_"+labelNum;
			$(this).attr("id",fieldId);
			$(this).attr("name",fieldId);
		}
		var isnull="";
		
		var inputType=$(this).attr("type");
		if($(this).attr("inputtype")=="BillNo"){
			inputType="BillNo&&"+$(this).attr("billvalue")+"="+$(this).attr("start")+"="+$(this).attr("step")+"="+$(this).attr("resettime");
			if($(this).attr("isback")=="true"){ //单据编号时，判断是否补号
				isnull="true";
			}else{
				isnull="false";
			}
		}else{
			if($(this).attr("isnull")=="true"){ 
				isnull="true";
			}else{
				isnull="false";
			}
		}
		var temp=$(this).attr("name")+"|"+$(this).attr("title")+"|"+inputType+"|"+isnull;
		fieldStr=fieldStr+temp+"#";
	});
	
	$("textarea",oEditor.EditingArea.Window.document.body).each(function(){
		if(typeof($(this).attr("title"))=="undefined"){
			alert("有控件名称为空");
			$(this).addClass("redInput");
			return false;
		}
		if(typeof($(this).attr("name"))=="undefined" || (typeof($(this).attr("id"))!="undefined" && $(this).attr("id").indexOf("field_")!=0 )|| oldlabelNum==0 || fieldStr.indexOf($(this).attr("name")+"|") > -1 ){ //新增的标签或者从外部复制的html内容，此时需要生成新的id和name
			labelNum=labelNum+1;
			var fieldId="field_"+labelNum;
			$(this).attr("id",fieldId);
			$(this).attr("name",fieldId);
		}
			
		var isnull="";
		if($(this).attr("isnull")=="true"){
			isnull="true";
		}else{
			isnull="false";
		}
		
		var str="";
		if($(this).attr("ishtml")=="true"){
			str="html";
		}else{
			str="textarea";
		}
		
		var temp=$(this).attr("name")+"|"+$(this).attr("title")+"|"+str+"|"+isnull;
		fieldStr=fieldStr+temp+"#";
	});
	
	$("select",oEditor.EditingArea.Window.document.body).each(function(){
		if(typeof($(this).attr("title"))=="undefined"){
			alert("有控件名称为空");
			$(this).addClass("redInput");
			return false;
		}
		
		if(typeof($(this).attr("name"))=="undefined" ||(typeof($(this).attr("id"))!="undefined" && $(this).attr("id").indexOf("field_")!=0 ) || oldlabelNum==0 || fieldStr.indexOf($(this).attr("name")+"|") > -1 ){ //新增的标签或者从外部复制的html内容，此时需要生成新的id和name
			labelNum=labelNum+1;
			var fieldId="field_"+labelNum;
			$(this).attr("id",fieldId);
			$(this).attr("name",fieldId);
		}
		
		var isnull="";
		if($(this).attr("isnull")=="true"){
			isnull="true";
		}else{
			isnull="false";
		}
		var temp=$(this).attr("name")+"|"+$(this).attr("title")+"|"+"select"+"|"+isnull;
		fieldStr=fieldStr+temp+"#";
	});
	$("#curNum").val(labelNum);
	return fieldStr;
}

//判断修改页面复制控件操作是否重复标签
function copyHtml(str){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	var re=/title=.[^title=]*\s/g;
	var rr=str.match(re);
	if(rr!=null){
		var strList=rr.toString();
		for(var i=0;i<rr.length;i++){
			var Id=rr[i].split("=")[1];
			if(strList.indexOf(rr[i])!=strList.lastIndexOf(rr[i])){ //包含Id值重复的标签
				$("input[title="+Id+"]",oEditor.EditingArea.Window.document.body).last().addClass("redInput");
				$("textarea[title="+Id+"]",oEditor.EditingArea.Window.document.body).last().addClass("redInput");
				$("img[title="+Id+"]",oEditor.EditingArea.Window.document.body).last().addClass("redInput");
				$("select[title="+Id+"]",oEditor.EditingArea.Window.document.body).last().addClass("redInput");
				return false;    
			}
		}
	}
	return true;
}

//判断删除和修改的标签是否别其他标签引用  
function checkDelLabel(){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
	//判断职员弹出框关联的部门弹出框是否还存在
	$("img[imgtype='employee']",oEditor.EditingArea.Window.document.body).each(function(){
		var len=jQuery.trim($(this).attr("relatedept")).length;
		var deptLen= $("img[imgtype='dept'][title="+$(this).attr("relatedept")+"]",oEditor.EditingArea.Window.document.body).size();
		if(len>0 && deptLen==0){
			alert("职员弹出框{"+$(this).attr("title")+"}所关联的弹出框{"+$(this).attr("relatedept")+"}不存在，请您重新输入!");
			return false;
		}
	});
	
	//判断计算控件关联的标签是否还存在
	var flag2="true";
	$("img[imgtype='count']",oEditor.EditingArea.Window.document.body).each(function(){
		var docText=$(this).attr("value");
		var re=/{.[^{}]*}/g;  //正则公式获取标签title值 
		var arr=docText.match(re);
		for(var i=0;i<arr.length;i++){
	    	var labelName=arr[i].replace("{","").replace("}","");
	    	var timeLen= $("img[title="+labelName+"]",oEditor.EditingArea.Window.document.body).size();
	    	var countLen=$("input[title="+labelName+"]",oEditor.EditingArea.Window.document.body).size();
	    	var flag="false";
	    	if(docText.indexOf("count")>-1){ //数字计算
	    		if(countLen==0){
	    			flag="true";
	    		}
	    	}else{
	    		if(timeLen==0){
	    			flag="true";
	    		}
	    	}
	    	if(flag=="true"){
	    		alert("计算控件{"+$(this).attr("title")+"}所关联的控件{"+labelName+"}不存在，请您重新输入!");
	    		flag2="false";
	    		break;
	    	}
	    }
	});
	if(flag2=="false"){
		return false;
	}
	return true;
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

function checkHtml(theHtml){
	if(theHtml!=""){
	    var strLen=theHtml.length; 
	    var strImg=theHtml.substring(theHtml.lastIndexOf(".")+1,strLen); 
	    if(strImg!='html' && strImg!= 'htm'){ 
			return false;
	    } 
	 }
	 return true;
}

function checkFile(){
	var fileName = $("#formFile").val();
	if(!checkHtml(fileName)){
		alert( "请上传html或htm格式文件!!"); 
		return false;
	}
	jQuery.ajaxFileUpload({
		url:'/OAWorkFlowTableAction.do?operation=$globals.getOP("OP_IMPORT")',
		secureuri:false,
		fileElementId:'formFile',
		dataType: 'text',
		success: function (data, status){
			var oEditor2 = FCKeditorAPI.GetInstance('FCKeditor1') ;
  			oEditor2.SetData(data);
		},error: function (data, status, e){
			alert(data);  
		}
	});
}
</script>
	</head>
	<iframe name="formFrame" style="display:none"></iframe>
	<body>
		<input type="file" name="formFile" id="formFile" value="" style="display:none;" onchange="checkFile();" > 
		<form name="myform" id="myform" action="/OAWorkFlowTableAction.do" method="post" target="formFrame">
			<input type="hidden" name="operation" id="operation" value="" />
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
			<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
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
							保存表单
						</button>
						<button onClick="javascript:$('#formFile').click();" type="button" >
							<img src="/js/img/import.gif" />
							导入表单
						</button>
						<button onClick="viewFlow();" id="viewForm" type="button" />
							<img src="/js/img/view.gif" />
							预览表单
						</button>
						<button onClick="closeWin();" type="button" />
							<img src="/js/img/close.gif" />
							关闭设计器 
						</button>
					</div>
					<span>表 单 控 件</span>
					<div class="button_list" style="height:380px;overflow:hidden;overflow-y:auto;">
						
						<button onClick="exec_cmd('TextField');" type="button">
							<img src="/js/img/textfield.gif" />
							单行输入框 
						</button>
						<button onClick="exec_cmd('Textarea');" type="button">
							<img src="/js/img/textarea.gif" />
							多行输入框 
						</button>
						<button onClick="exec_cmd('BillNoField');" type="button">
							<img src="/js/img/textfield.gif" />
							单据编号
						</button>
						<button onClick="exec_cmd('Select');" type="button">
							<img src="/js/img/listmenu.gif" />
							下拉列表
						</button>
						<button onClick="exec_cmd('Radio');" type="button">
							<img src="/js/img/radio.gif" />
							单 选 框

						</button>
						<button onClick="exec_cmd('Checkbox');" type="button">
							<img src="/js/img/check.gif" />
							复 选 框

						</button>
						<button onClick="exec_cmd('DateField');" type="button">
							<img src="/js/img/date_add.gif" />
							日期控件
						</button>
						<button onClick="exec_cmd('AffixField');" type="button">
							<img src="/js/img/textfield.gif" />
							图片/附件
						</button>
						<button onClick="exec_cmd('CountField');" type="button">
							<img src="/js/img/calculator.gif" />
							计算控件
						</button>
						<button onClick="exec_cmd('HongField');" type="button">
							<img src="/js/img/signature.gif" />
							宏控件

						</button>
						<button onClick="exec_cmd('EmployeeField');" type="button">
							<img src="/js/img/user_add.gif" />
							人员选择框

						</button>
						<button onClick="exec_cmd('DeptField');" type="button">
							<img src="/js/img/group_add.gif" />
							部门选择框

						</button>
						<button onClick="exec_cmd('ClientField');" type="button">
							<img src="/js/img/user_suit.gif" />
							CRM客户
						</button>
						<button onClick="exec_cmd('PopupField');" type="button">
							<img src="/js/img/tips.png" />
							定制弹出框

						</button>
						<button onClick="exec_cmd('RepeatField');" type="button">
							<img src="/js/img/tips.png"/>去重复控件

						</button>
						<ul id="links"></ul>
					</div>
				</div>

				<div class="table_right">
        <script type="text/javascript">
			var fckHeight = document.documentElement.clientHeight-81;
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
