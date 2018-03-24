<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/workflow.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"  /> 


<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">


var curRow;
var curRowNo;
function focusRow(obj){
	curRow = obj;
	curRowNo = jQuery.inArray(obj,$("#FieldInfoId tbody",document).find("tr"));
	$(".focusRow",document).removeClass("focusRow");
	$(curRow).addClass("focusRow");	
}

function dblclick(obj){
	parent.callBackPopup($(curRow).find("td:eq(1)").find("a").text().trim(),$(curRow).find("td:eq(2)").text().trim());
}

function delRow(mydobj){
	if(confirm("确定删除本行吗?")){
		$(curRow).remove();
		curRow = null;
		row = 1;
		$("#FieldInfoId tbody",document).find("tr").each(function (){
			$(this).find("td:eq(0)").text(row);
			row ++;
		});
	}
}
var noParse = ''; //未能正确解释的define
function initdefine(){	
	str =parent.$("#fieldCalculate").val();
	
	row = 1;
	while(str.indexOf(">") > -1){
		temp = str.substring(0,str.indexOf(">")+1);
		str = str.substring(str.indexOf(">")+1);
		//取operation="add"
		var regx = /([\w]*)[\s]*=[\s]*"([\w]*)"/gi;  
     
	    var result = {};  
	    result.other = '';
	    while(regx.test(temp)){  
	    	if("operation" == RegExp.$1 || "post" == RegExp.$1  || "id" == RegExp.$1){
	    		result[RegExp.$1] =RegExp.$2;
	    	}else{
	    		result.other += ' '+RegExp.$1+'="'+RegExp.$2+'"';
	    	}
	    }     
	    if(result.operation!="add" && result.operation!="delete" && result.operation!="update" && result.operation!="addView" && result.operation!="updateView"){
	    	noParse += temp; //非add,delete ,update操作 暂时原样处理，不做分析 
	    }else{
	    	rowHtml = '<td >'+row+'</td>'+
	    			  '<td ><select name="operation"><option '+(result.operation=="add"?'selected':'')+' value="add">添加</option>'+
	    			  '<option '+(result.operation=="update"?'selected':'')+' value="update">修改</option>'+
	    			  '<option '+(result.operation=="delete"?'selected':'')+' value="delete">删除</option>'+
	    			  '<option '+(result.operation=="addView"?'selected':'')+' value="addView">添加界面</option>'+
	    			  '<option '+(result.operation=="updateView"?'selected':'')+' value="updateView">修改界面</option>'+
	    			  '</td>'+
	    			  '<td ><select name="post"><option '+(result.post!="before"?'selected':'')+' value="">执行之后</option><option '+(result.post=="before"?'selected':'')+' value="before">执行之前</option></td>'+
	    			  '<td ><input name="id" value="'+result.id+'"/><input name="other" type="hidden" value=\''+result.other+'\'/></td>'+
	    			  '<td ><a href="javascript:delRow(this)">删除</a>&nbsp;&nbsp;<a href="javascript:defineUpdate()">修改文件</a></td>';	  
	    	rowHtml = '<tr  class="c1" onclick="focusRow(this)" >'+rowHtml+'</tr>';		
	    	$("#FieldInfoId tbody",document).append(rowHtml);    
	    	row ++;
	    }        
	}
}
function addRow(){
	row = $("#FieldInfoId tbody",document).find("tr").size()+1;
	rowHtml = '<td >'+row+'</td>'+
   			  '<td ><select name="operation"><option  value="add">添加</option><option  value="update">修改</option><option  value="delete">删除</option> <option  value="addView">添加界面</option><option  value="updateView">修改界面</option></td>'+
   			  '<td ><select name="post"><option value="">执行之后</option><option value="before">执行之前</option></td>'+
   			  '<td ><input name="id" value=""/><input name="other" type="hidden" value=\'\'/></td>'+
   			  '<td ><a href="javascript:delRow(this)">删除</a>&nbsp;&nbsp;<a href="javascript:defineUpdate()">修改文件</a></td>';	  
   	rowHtml = '<tr  class="c1" onclick="focusRow(this)" >'+rowHtml+'</tr>';		
   	$("#FieldInfoId tbody",document).append(rowHtml);    
}

function getdefineOp(){
	ret = noParse;
	rowNum = 0;
	$("input[name=id]",document).each(function(){
		if($(this).val() != ""){
			operation = $("select[name=operation]:eq("+rowNum+")",document).val();
			post = $("select[name=post]:eq("+rowNum+")",document).val();
			post = post==''?'':' post="'+post+'"';
			other = $("input[name=other]:eq("+rowNum+")",document).val();
			ret +='<sql operation="'+operation+'"'+post+' id="'+$(this).val()+'"'+other+'>';
		}
		rowNum ++;
	});
	return ret;
}
function defineUpdate(){
	cid = $(curRow).find("input[name=id]").val();
	if(cid==""){
		alert("请选录入自定义代号");
		return;
	}	
	path  = jQuery.ajax({ url: "/CustomAction.do?type=defineFile&id="+cid,
 		async: false   
	}).responseText; 
	if(path==""){
		//没有对应的文件，则要么新建一个文件，要么选择一个文件来修改
		asyncbox.open({
	　　		url: "/CustomAction.do?type=defineQuery&id="+cid,
			id  : 'defineQuery',
	　　 		width : 800,
	　　 		height : 500,
			title  :'请选择需修改的自定义文件',
	　　　		btnsbar : [{text: '创建新文件',action  : 'new_File'}].concat(jQuery.btn.OKCANCEL), 
	　　　		callback : function(action,opener){
		　　　 		if(action == 'ok'){
		　　　　　　　		opener.doUpdate();				
					return false;
		　　　		}else if(action == 'new_File'){
		　　　　　　　		　asyncbox.prompt('输入文件名','允许不带文件路径:','','text',function(action,val){
					　　　if(action == 'ok'){
					　　　　　if(val != ""){
							if(val.indexOf(".xml") == -1){
								val +=".xml";
							}
							pmsg = jQuery.ajax({ url: "/CustomAction.do?type=newdefineFile&fn="+val+"&cid="+cid, 
						 		async: false   
							}).responseText; 
							if(pmsg.indexOf("OK:") == -1){
								alert(pmsg);
							}else{
								updateFile(pmsg.substring(3),cid)
							}
						  }
					　　　}
					 });
		　　　		}
	　　　	   }
	    });		
	}else{
		updateFile(path,cid)
	}
	return;
	
	
}

function updateFile(file,defineName){	
	try{				
		var mimetype = navigator.mimeTypes["application/np-print"];		
		if(mimetype){
			if(mimetype.enabledPlugin){
				 var cb = document.getElementById("plugin");
				 cb.OpenDefine(file,defineName,'$!IP|$!port','$!JSESSIONID','$LoginBean.id');
			}
		}else{
			var obj = new ActiveXObject("KoronReportPrint.DefineEdit") ;
		    obj.URL="$!IP|$!port" ;
		   	obj.JsessionID ="$!JSESSIONID" ;
		   	obj.UserId ='$LoginBean.id' ;
		    obj.FileName=file ;
		    obj.DefineName=defineName ;
			obj.OpenDefine();	
		}
	}catch (e){
		asyncbox.alert("$text.get('com.sure.print.control')<br><br><a class='aio-print' href='/common/AIOPrint.exe' target='_blank'>下载控件</a>","信息提示");
	}

}


</script>
<style type="text/css">

#FieldInfoId input{background:none;border:0px;width: 100%;height: 100%;}
#FieldInfoId select{background:none;border:0px;width: 100%;height: 100%;}
#FieldInfoId thead td{border-right:1px solid #999;border-bottom:1px solid #999;line-height: 33px;}
#FieldInfoId tbody td{border-right:1px solid #999;border-bottom:1px solid #999;position: relative;}

.data_list .dataListUL{margin:0px 5px 0px 5px;line-height:33px;border: 1px solid #999;min-height:67px}
.data_list .dataListUL li{ border-bottom: 1px solid #999;  }
.data_list .dataListUL .titleLi{ background:url(/style1/images/workflow/data_list_head_bg.gif); height:33px;padding-left:10px }
.data_list .dataListUL input,.data_list .dataListUL select{width: 117px;margin-left: 5px;}
.data_list .dataListUL label{width: 48px;margin-left: 5px;display: inline-block;}
.data_list table{width:700px;}
.data_list table td{overflow:hidden;text-align:left;}
.tstBtn{background-image: url(/style/images/client/icon16.png);background-position: -32px 0;width: 16px;height: 16px;position: absolute;cursor: pointer;right: 1px;top: 8px;}



.focusRow{background-color:#9AF850;}
</style>
</head>
<body onload="initdefine()">

	
	<table cellpadding="0" cellspacing="0" border="0" class="framework" >
		<tr>
			<td>
				<div class="TopTitle">
					<span style="margin-top: 4px;margin-bottom: 12px;">
					<input type="button" class="bu_02" onclick="addRow()" value="增加一行" />	
					</span>
				</div>
				<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;" >	
					<div id="data_list_idTabXa" style="overflow-y:auto;padding: 0px;margin: 0px;height: 100%;width: 100%;background: none;">
					<script type="text/javascript">
					var oDiv=document.getElementById("data_list_id");
					var sHeight=document.documentElement.clientHeight-60;
					oDiv.style.height=sHeight+"px";					
					</script>
					<table cellpadding="0" cellspacing="0" style="border-left: 0px;table-layout:fixed" id="FieldInfoId">	
						<thead>
							<tr >
								<td width="30">序号</td>
								<td width="80">操作</td>
								<td width="80px" >时间</td>
								<td width="*">自定义代号</td>	
								<td  width="100px" >操作</td>	
							</tr>
						</thead>
						<tbody>
							
						</tbody>
					</table>
					</div>
				</div>
			</td>				
		</tr>
	</table>
<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>	
</body>
</html>
