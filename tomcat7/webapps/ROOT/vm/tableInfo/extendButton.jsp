<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/workflow.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
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
	str =parent.$("#extendButton").val();
	
	row = 1;
	while(str.indexOf(">") > -1){
		temp = str.substring(0,str.indexOf(">")+1);
		str = str.substring(str.indexOf(">")+1);
		//取operation="add"
		var regx = /([\w]*)[\s]*=[\s]*"([^"]*)"/gi;  
     
	    var result = {};  
	    while(regx.test(temp)){  
	    	result[RegExp.$1] =RegExp.$2;
	    } 
	    
	    hasParse = false;
	    rowHtml = '<td >'+row+'</td>'+
	    			  '<td >'+getType(result.type)+'</td>'+
	    			  '<td >'+getOperation(result.operation)+'</td>'+
	    			  '<td ><input name="name" value="'+(result.name==undefined?'':result.name)+'"/></td>';
	    
	    if(result.type=="quote" || result.type=="tool" || result.type=="query" || result.type=="export"){	    	
	    	rowHtml +='<td ><input name="param1" value="'+(result.value==undefined?'':result.value)+'"/></td>'+
	    			  '<td ><input name="param2" value="'+''+'"/></td>';
	    			  hasParse = true;   			  
	    }else if(result.type=="copy"){	    	
	    	rowHtml +='<td ><input name="param1" value="'+''+'"/></td>'+
	    			  '<td ><input name="param2" value="'+''+'"/></td>';	
	    			  hasParse = true;   			  
	    }else if(result.type=="dataMove"){	    	
	    	rowHtml +='<td ><input name="param1" value="'+(result.id==undefined?'':result.id)+'"/></td>'+
	    			  '<td ><input name="param2" value="'+(result.selectName==undefined?'':result.selectName)+'"/></td>';	
	    			  hasParse = true;   			  
	    }else if(result.type=="midCalculate"){	    	
	    	rowHtml +='<td ><input name="param1" value="'+''+'"/></td>'+
	    			  '<td ><input name="param2" value="'+''+'"/></td>';	
	    			  hasParse = true;   			  
	    }else if(result.type=="define"){	    	
	    	rowHtml +='<td ><input name="param1" value="'+(result.right==undefined?'':result.right)+'"/></td>'+
	    			  '<td ><input name="param2" value="'+(result.value==undefined?'':result.value)+'"/></td>';
	    			  hasParse = true;   			  
	    }else if(result.type=="myButton"){	    	
	    	rowHtml +='<td ><input name="param1" value="'+(result.oper==undefined?'':result.oper)+'"/></td>'+
	    			  '<td ><input name="param2" value="'+(result.jsFunction==undefined?'':result.jsFunction)+'"/></td>';	
	    			  hasParse = true;   			  
	    }else if(result.type=="popDefine"){	    	
	    	rowHtml +='<td ><input name="param1" value="'+(result.id==undefined?'':result.id)+'"/></td>'+
	    			  '<td ><input name="param2" value="'+(result.selectName==undefined?'':result.selectName)+'"/></td>';	
	    			  hasParse = true;   			  
	    }else if(result.type=="defineNew"){
	    	rowHtml +='<td ><input name="param1" value="'+(result.right==undefined?'':result.right)+'"/></td>'+
			  '<td ><input name="param2" value="'+(result.value==undefined?'':result.value)+'"/></td>';
			  hasParse = true;   			  
		}
	    
	    if(hasParse){
	    	rowHtml +='<td ><a href="javascript:delRow(this)">删除</a></td>';	  
	    	rowHtml = '<tr  class="c1" onclick="focusRow(this)" >'+rowHtml+'</tr>';		
	    	$("#FieldInfoId tbody",document).append(rowHtml);
	    	curRow = $("#FieldInfoId tbody",document).find("tr:eq("+(row -1)+")").get(0);
	    	row ++;
	    	showTitle();	    	
	    }else {
	    	noParse += temp; //非add,delete ,update操作 暂时原样处理，不做分析 
	    }      
	}
}
function addRow(){
	row = $("#FieldInfoId tbody",document).find("tr").size()+1;
	rowHtml = '<td >'+row+'</td>'+
   			  '<td >'+getType('')+'</td>'+
   			  '<td >'+getOperation('')+'</td>'+
   			  '<td ><input name="name" value="'+''+'"/></td>';
	    	    	
   	rowHtml +='<td ><input name="param1" value="'+''+'"/></td>'+
   			  '<td ><input name="param2" value="'+''+'"/></td>';
	      
   	rowHtml +='<td ><a href="javascript:delRow(this)">删除</a></td>';	  
   	rowHtml = '<tr  class="c1" onclick="focusRow(this)" >'+rowHtml+'</tr>';		
   	$("#FieldInfoId tbody",document).append(rowHtml);  
}

function showTitle(){
	type = $(curRow).find("select[name=type]").val();
	if(type=="quote"){	    	
    	$(curRow).find("td:eq(4)").attr("title","对应引用弹出窗-value");	
    	$(curRow).find("td:eq(5)").attr("title","此参数不要填写");		  
    }else if(type=="tool"){	    	
    	$(curRow).find("td:eq(4)").attr("title","工具的URL链接-value");	
    	$(curRow).find("td:eq(5)").attr("title","此参数不要填写");		  
    }else if(type=="query"){	    	
    	$(curRow).find("td:eq(4)").attr("title","联查的URL链接-value");	
    	$(curRow).find("td:eq(5)").attr("title","此参数不要填写");		  			  
    }else if(type=="export"){	    	
    	$(curRow).find("td:eq(4)").attr("title","导入表对应名字-value");	
    	$(curRow).find("td:eq(5)").attr("title","此参数不要填写");		     			  
    }else if(type=="copy"){	    	
    	$(curRow).find("td:eq(4)").attr("title","此参数不要填写");	
    	$(curRow).find("td:eq(5)").attr("title","此参数不要填写");		    			  
    }else if(type=="dataMove"){	    	
    	$(curRow).find("td:eq(4)").attr("title","要执行的define-id");	
    	$(curRow).find("td:eq(5)").attr("title","弹出窗名字-selectName");		  		  
    }else if(type=="midCalculate"){	    	
    	$(curRow).find("td:eq(4)").attr("title","此参数不要填写");	
    	$(curRow).find("td:eq(5)").attr("title","此参数不要填写");		   			  
    }else if(type=="define"){	    	
    	$(curRow).find("td:eq(4)").attr("title","归属的权限-right");	
    	$(curRow).find("td:eq(5)").attr("title","要执行的ButtonDefine中名字-define-value");		  			  
    }else if(type=="myButton"){	    	
    	$(curRow).find("td:eq(4)").attr("title","链接地址-oper");	
    	$(curRow).find("td:eq(5)").attr("title","要执行的JS函数-jsFunction");		  		  
    }else if(type=="popDefine"){	    	
    	$(curRow).find("td:eq(4)").attr("title","要执行的define-id");	
    	$(curRow).find("td:eq(5)").attr("title","弹出窗名字-selectName");		  		  
    }else if(type=="defineNew"){	    	
    	$(curRow).find("td:eq(4)").attr("title","归属的权限-right");	
    	$(curRow).find("td:eq(5)").attr("title","要执行的define-value");		  			  
    }
}

function getType(type){
	ret =   '<select name="type" onchange="showTitle()">'+
			'<option '+(type=="quote"?'selected':'')+'  value="quote">引用</option>'+
			'<option '+(type=="tool"?'selected':'')+'  value="tool">工具</option>'+
			'<option '+(type=="query"?'selected':'')+'  value="query">联查</option>'+
			'<option '+(type=="copy"?'selected':'')+'  value="copy">复制</option>'+
			'<option '+(type=="export"?'selected':'')+'  value="export">导入</option>'+
			'<option '+(type=="dataMove"?'selected':'')+'  value="dataMove">数据搬移</option>'+
			'<option '+(type=="midCalculate"?'selected':'')+'  value="midCalculate">运算</option>'+
			'<option '+(type=="define"?'selected':'')+'  value="define">执行define</option>'+
			'<option '+(type=="defineNew"?'selected':'')+'  value="defineNew">执行define新</option>'+
			'<option '+(type=="myButton"?'selected':'')+'  value="myButton">执行JS</option>'+
			'<option '+(type=="popDefine"?'selected':'')+'  value="popDefine">先弹出窗再Define</option>'+
			'</select>';
	return ret;		
			/*
<button type="myButton" operation="list" oper="TelAction.do?operator=callTel" name="common.lb.telCall" jsFunction="telCall">
<button type="copy"     operation="list">
<button type="quote"    operation="add"    value="tblBuyInStock:tblSalesOutStock_quote_buyinStock" name="BuyOutStock">
<button type="tool"     operation="update" value="/ReportDataAction.do?repName" name="往来应收">
<button type="query"    operation="update" value="/ReportDataAclesOrder" name="销售订单明细">
<button type="define"   operation="list"   right="delete" value="tblBuyApplicationTotalStatusStop">
<button type="export"   operation="list"   value="tblGoods">
<button type="dataMove" operation="list"   id="Company_dataMove" selectName="SelectCompanyClassCode">
<button type="midCalculate" operation="update">			
			*/			
}
function getOperation(type){
	ret =   '<select name="operation">'+
			'<option '+(type=="list"?'selected':'')+'  value="list">列表</option>'+
			'<option '+(type=="add"?'selected':'')+'  value="add">添加</option>'+
			'<option '+(type=="update"?'selected':'')+'  value="update">修改</option>'+
			'</select>';
	return ret;				
}

function getdefineOp(){
	ret = noParse;
	rowNum = 0;
	error = false;
	$("select[name=type]",document).each(function(){
		if($(this).val() != "" &&　!error){
			type = $(this).val();
			operation = $("select[name=operation]:eq("+rowNum+")",document).val();
			name = $("input[name=name]:eq("+rowNum+")",document).val();
			param1 = $("input[name=param1]:eq("+rowNum+")",document).val();
			param2 = $("input[name=param2]:eq("+rowNum+")",document).val();
			
			if(type=="quote"){	    
				if(name==""){
					alert("请在第"+(rowNum+1)+"行填写名字，可以是多语言key");		
					error = true;			
					return;
				}	
				if(param1==""){
					alert("请在第"+(rowNum+1)+"行参数1填写引用弹出窗,对应value");			
					error = true;					
					return;
				}	
				ret +=  '<button type="quote"    operation="'+operation+'"    value="'+param1+'" name="'+name+'">';
		    }else if(type=="tool"){	    
		    	if(name==""){
					alert("请在第"+(rowNum+1)+"行填写名字，可以是多语言key");		
					error = true;						
					return;
				}	
				if(param1==""){
					alert("请在第"+(rowNum+1)+"行参数1填写工具的URL链接,对应value");		
					error = true;						
					return;
				}	
				ret += '<button type="tool"     operation="'+operation+'" value="'+param1+'" name="'+name+'">';
		    }else if(type=="query"){	
		    	if(name==""){
					alert("请在第"+(rowNum+1)+"行填写名字，可以是多语言key");		
					error = true;						
					return;
				}	
				if(param1==""){
					alert("请在第"+(rowNum+1)+"行参数1填写联查的URL链接,对应value");		
					error = true;						
					return;
				}    
				ret += '<button type="query"    operation="'+operation+'" value="'+param1+'" name="'+name+'">';	 			  
		    }else if(type=="export"){	
				if(param1==""){
					alert("请在第"+(rowNum+1)+"行参数1填写导入表对应名字,对应value");		
					error = true;						
					return;
				}    
				ret +='<button type="export"   operation="'+operation+'"   value="'+param1+'">';	  
		    }else if(type=="copy"){	  
		    	ret +='<button type="copy"     operation="'+operation+'">'; 	    			  
		    }else if(type=="dataMove"){	 
		    	if(param1==""){
					alert("请在第"+(rowNum+1)+"行参数1填写要执行的define,对应id");		
					error = true;						
					return;
				} 
				if(param2==""){
					alert("请在第"+(rowNum+1)+"行参数2填写弹出窗名字,对应selectName");		
					error = true;						
					return;
				}   
				ret +='<button type="dataMove" operation="'+operation+'"   id="'+param1+'" selectName="'+param2+'">';
		    }else if(type=="midCalculate"){	   
		    	ret +='<button type="midCalculate" operation="'+operation+'">	';  			  
		    }else if(type=="define"){	    	
		    	if(param1==""){
					alert("请在第"+(rowNum+1)+"行参数1填写归属的权限对应right");		
					error = true;						
					return;
				} 
				if(param2==""){
					alert("请在第"+(rowNum+1)+"行参数2填写要执行的define,对应value");		
					error = true;						
					return;
				}   	
				ret +='<button type="define"   operation="'+operation+'"   right="'+param1+'" value="'+param2+'">';	  
		    }else if(type=="myButton"){	    	
		    	if(param1=="" && param2==""){
					alert("请在第"+(rowNum+1)+"行参数1填写链接地址,对应oper 或者在参数2填写要执行的JS函数对应jsFunction");		
					error = true;						
					return;
				} 
				ret +='<button type="myButton" operation="'+operation+'" oper="'+param1+'" name="'+name+'" jsFunction="'+param2+'">';
		    }else if(type=="popDefine"){	 
		    	if(name==""){
					alert("请在第"+(rowNum+1)+"行填写名字，可以是多语言key");		
					error = true;			
					return;
				}	
		    	if(param1==""){
					alert("请在第"+(rowNum+1)+"行参数1填写要执行的define,对应id");		
					error = true;						
					return;
				} 
				if(param2==""){
					alert("请在第"+(rowNum+1)+"行参数2填写弹出窗名字,对应selectName");		
					error = true;						
					return;
				}   
				ret +='<button type="popDefine" operation="'+operation+'"  name="'+name+'"  id="'+param1+'" selectName="'+param2+'">';
		    } else if(type=="defineNew"){	    	
		    	if(name==""){
					alert("请在第"+(rowNum+1)+"行填写名字，可以是多语言key");		
					error = true;			
					return;
				}
		    	if(param1==""){
					alert("请在第"+(rowNum+1)+"行参数1填写归属的权限对应right");		
					error = true;						
					return;
				} 
				if(param2==""){
					alert("请在第"+(rowNum+1)+"行参数2填写要执行的define,对应value");		
					error = true;						
					return;
				}   	
				ret +='<button type="defineNew"   operation="'+operation+'"   name="'+name+'"   right="'+param1+'" value="'+param2+'">';	  
		    }		
		}
		rowNum ++;
	});
	if(error){
		return "error";
	}		
	return ret;
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
					<table cellpadding="0" cellspacing="0" style="border-left: 0px;table-layout:fixed;width:99%" id="FieldInfoId">	
						<thead>
							<tr >
								<td width="30">序号</td>
								<td width="100">类型</td>
								<td width="100px" >界面</td>
								<td width="150px" >名字</td>
								<td width="400px" >参数1</td>
								<td width="*">参数2</td>	
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
