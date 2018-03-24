<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("common.lb.printActiveX")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"  />
<style type="text/css">
.HeadingButton2{padding:5px 0 0 0;;overflow:hidden;border-top:1px solid #c1c1c1;}
.HeadingButton2 li{float:right;display:inline;text-align:center ;vertical-align:text-top;margin:3px 3px 0 0;}
.hBtns-small{padding:0 8px;cursor:pointer;display:inline-block;height:21px;line-height:21px;color:#666;border:1px #bbb solid;border-radius:3px;}
.hBtns-small:hover{background:#f2f2f2;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
try{
	var koron = new ActiveXObject("KoronCom.TrustedSites") ;
	koron.add("$!IP") ;
}catch (e){
}

function printView(format,reportNumber){
	try{
		var varSQL = "" ;
		#if("$!printType"!="labelPrint")
			var sqlObj = parent.$("#SQLSave"); 
			if(typeof(sqlObj.attr("name"))!="undefined" && sqlObj!=null){
				varSQL = sqlObj.val() ;
			}
		#else
		varSQL = "$!SQLSave";
		#end
	    var mimetype = navigator.mimeTypes["application/np-print"];
		if(mimetype){
			if(mimetype.enabledPlugin){
				 var cb = document.getElementById("plugin");
				 cb.printView('$!IP|$!port',format,'$!BillID',reportNumber,'$!BillTable','$!JSESSIONID','$LoginBean.id','zh_CN',varSQL);
			}
		}else{
			var obj = new ActiveXObject("KoronReportPrint.BatchPrint") ;
		    obj.URL="$!IP|$!port" ;
		    obj.SQL= varSQL ;
		    obj.StyleList = format ;
		    obj.BillID= "$!BillID" ;
		    obj.BillTable="$!BillTable" ;
		   	obj.Cookie ="$!JSESSIONID" ;
		   	obj.UserId ='$LoginBean.id' ;
		    obj.ReportNumber=reportNumber ;
		    obj.printView();
		}
	}catch (e){
		asyncbox.alert("$text.get('com.sure.print.control')<br><br><a class='aio-print' href='/common/AIOPrint.exe' target='_blank'>下载控件</a>","信息提示");
	}	
}

function doPrint(format,reportNumber){
	try{
		var varSQL = "" ;
		#if("$!printType"!="labelPrint")
			var sqlObj = parent.$("#SQLSave");
			if(typeof(sqlObj.attr("name"))!="undefined" && sqlObj!=null){
				varSQL = sqlObj.val() ;
			}
		#else
		varSQL = "$!SQLSave" ;
		#end
		var mimetype = navigator.mimeTypes["application/np-print"];
		if(mimetype){
			if(mimetype.enabledPlugin){
				 var cb = document.getElementById("plugin");
				 cb.print('$!IP|$!port',format,'$!BillID',reportNumber,'$!BillTable','$!JSESSIONID','$LoginBean.id','zh_CN',varSQL);
			}
		}else{
			var obj = new ActiveXObject("KoronReportPrint.BatchPrint") ;
		    obj.URL="$!IP|$!port" ;
		    obj.SQL= varSQL ;
		    obj.StyleList = format ;
		    obj.BillID= "$!BillID" ;
		    obj.BillTable="$!BillTable" ;
		   	obj.Cookie ="$!JSESSIONID" ;
		   	obj.UserId ='$LoginBean.id' ;
		    obj.ReportNumber=reportNumber ;
			obj.doPrint();
		}
	}catch (e){
		asyncbox.alert("$text.get('com.sure.print.control')<br><br><a href='/common/AIOPrint.exe' target='_blank'>下载控件</a>","信息提示");
	}
}

//打印样式弹出框    
function styleFormat(){	var urls = "/ReportSetAction.do?operation=84&reportId=$!reportId&isbillType=$!isbillType&tableName=$!tableName&moduleType=$!moduleType";
	asyncbox.open({id:'PopPrintSFdiv',title:'打印样式',url:urls,width:750,height:470});
}
#if("$!printType"=="savePrint")
function closeAsyn(){
	parent.location.href='/common/message.jsp';
}
#end

</script>
</head>
<body scroll=no>
 	<div style="height:205px;overflow: auto;margin-top:5px;padding:0 5px;">
		<a id="reload" href="" style="display: none;"></a>
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function"  id="tblSort" >
				<thead>
					<tr>
						<!-- <td width="30">&nbsp;</td>  -->
						<td width="143" align="center">$text.get("common.lb.print.FormatName")</td>
						<td width="85" align="center">$text.get("common.lb.print.Operation")</td>				
					</tr>			
				</thead>
				<tbody>
				#foreach($style in $formatList)
					<tr height="30">
						<!-- <td align="center"><input type="radio" name="keyId" value="$!globals.get($style,2)" #if($velocityCount==1)checked#end/></td> -->
						<td align="left">$!globals.get($style,0)</td>
						<td align="center">
							<span class="btn btn-mini" onclick="printView('@col$!globals.get($style,1)@row','$!globals.get($style,3)')">$text.get("common.lb.print.Preview")</span>
							<span class="btn btn-danger btn-mini" onclick="doPrint('@col$!globals.get($style,1)@row','$!globals.get($style,3)')">$text.get("common.lb.print")</span>
						</td>				
					</tr>
					
				#end
				</tbody>
		</table>
	</div>
	<div class="HeadingButton2">
		<ul style="overflow:hidden;border-bottom:0 #AED3F1 solid;">
			<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>
			<!-- 
			<li><button onclick="javascript:styleFormat();" style="width: 45px;">添加</button></li>
			<li><button onclick="javascript:styleFormat();" style="width: 45px;">删除</button></li>
			 -->
			<li><span class="hBtns" onclick="javascript:closeAsyn();">$text.get("common.lb.close")</span></li>
			#if($LoginBean.operationMap.get("/ReportSetQueryAction.do?styleType=print").query())
			<li><span class="hBtns" onclick="javascript:styleFormat();">$text.get("common.lb.print.FormatDeisgn")</span></li>
			#end
		</ul>
	</div>
</body>
</html>
