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
	doUpdate();
}

function doUpdate(file,defineName){
	#if("$!from" == "adv")
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
	#else
	parent.jQuery.opener("defineOp").updateFile($(curRow).find("td:eq(1)").text());
	parent.jQuery.close("defineQuery");
	#end
}
function doAdd(){
	if($("#keyword").val()==""){
		alert("请输入自定义名称");
		return;
	}
	asyncbox.prompt('输入文件名','允许不带文件路径:','','text',function(action,val){
　　　if(action == 'ok'){
　　　　　if(val != ""){
		if(val.indexOf(".xml") == -1){
			val +=".xml";
		}
		pmsg = jQuery.ajax({ url: "/CustomAction.do?type=newdefineFile&fn="+val+"&cid="+$("#from").val(), 
	 		async: false   
		}).responseText; 
		if(pmsg.indexOf("OK:") == -1){
			alert(pmsg);
		}else{
			doUpdate(pmsg.substring(3),$("#from").val())
		}
	  }
　　　}
	});
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
				#if("$!from" == "adv")
				<form method="post" scope="request" name="form" action="/CustomAction.do?type=defineQuery" class="mytable" >
					<input type="hidden" name="from" id="from" value="$!from"/>
					<span style="margin-top: 4px;margin-bottom: 12px;">
					自定义名称:<input name=keyword id= keyword value="$!keyword" onkeydown="if(event.keyCode==13) document.form.submit();"/> 
					</span>
					<span style="float: right;font-weight: normal;margin-right: 12px;margin-bottom: 10px;">
						<input type="submit" class="bu_02"  value="查询" />	
						
						<input type="button" onclick="doAdd()" class="bu_02"  value="添加" />	
						
					</span>
				</form>	
				#end
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
								<td width="*">文件</td>	
								<td  width="60px" >操作</td>	
							</tr>
						</thead>
						<tbody>
							#foreach($row in $files)
							<tr onclick="focusRow(this)" ondblclick="dblclick();">
								<td>$velocityCount</td>
								<td >$row</td>	
								<td  ><a href="javascript:doUpdate('$row.replaceAll("\\","\\\\")','')">修改</a></td>	
							</tr>
							#end
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
