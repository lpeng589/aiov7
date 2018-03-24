<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/css/client.css"/>
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/function.js"></script>  
<style type="text/css">


</style>
<script language="javascript">
	
function downLoad(url){
	window.location.href = url;
}

if(typeof(top.junblockUI)!="undefined"){	
	top.junblockUI();
}



function Refresh() {
	var ready = true ;
    path="/UtilServlet?operation=importInfo&importName=$!importName"+"&time="+(new Date()).getTime();;
	if(top.cancelImport){
		path="/UtilServlet?operation=cancelImport&importName=$!importName"+"&time="+(new Date()).getTime();;
		
	}
	var xmlHttp;
	if (window.ActiveXObject) {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	} 
	else if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	}
	xmlHttp.onreadystatechange = function(){
		if(xmlHttp.readyState == 4) {
			if(xmlHttp.status == 200) {
				response = xmlHttp.responseText;
				if(response == "nullObject"){
					ready = false ;
				}else if(response != "" && response != "ok"){
					var is = response.split("|");
					document.getElementById("imTotal").innerHTML=is[0];
					document.getElementById("imSuccess").innerHTML=is[1];
					document.getElementById("imError").innerHTML=is[2];					
					pspan = document.getElementById("progressSpan");
					pross = (Number(is[1])+Number(is[2])) /Number(is[0]);
					pross = pross*100;
					pross = Math.round(pross);
					
					//alert("to:"+is[0]+"su:"+is[1]+"er:"+is[2]+"pr"+pross);
					
					pspan.style.width=pross+"%";
					if(pross>0){
						pspan.innerHTML=pross+"%";						
					}
					
				}else if(response == "ok"){					
					document.getElementById("imCancel").innerHTML="$text.get('suspend.import.process')";
					top.cancelImport = true;
				}
			}else{
				response = "no response text" ;
			}
		}
	};
	xmlHttp.open("get", path, false);
	xmlHttp.send();
	var hit ;
	if(ready){
		hit =document.getElementById('ask_load').outerHTML;
	}else{
		hit = document.getElementById('ask_load2').outerHTML;
	}
	
	top.jblockUI({ message: hit, css: { left:'35%', top:'30%', width: '460px',height:'auto' } });
}


function getExcel()
{
 var path=document.getElementById("fileName").value;
   if(path.length>0)
   {
   
        var pos=path.lastIndexOf(".");
		var lastName=path.substring(pos,path.length);
		if(lastName.toLowerCase()!=".xls")
		{
			alert('$text.get("excel.format.error")');
			return false;
		}else
		{
			var starIndex = path.lastIndexOf("\\");
			var endIndex = path.lastIndexOf(".");
			var fileName = path.substr(starIndex+1,(endIndex-starIndex-1));
			
			document.form.submit();
			top.cancelImport= false;
			setInterval("Refresh()",1000);		
			
		}
	}else
   {
     alert('$text.get("excel.up.select")');
	 return false;
   }
}

function downloadExample(fileName){
	window.location.href="/ReadFile?tempFile=example&fileName="+fileName;
}
function download(keyId){
 	window.location.href="/UtilServlet?operation=importTemplete&tableName="+keyId+"&winCurIndex=3";
}

function downView(urls){
	//var url = urls;
	jQuery.ajax({
	   type: "POST",
	   url: urls,
	   success: function(msg){
	     window.location.href=msg;
	   }
	});
	
}

/*下载模板*/
function templateDown(){
	jQuery.ajax({
		type: "POST",
		url: "/CRMBrotherAction.do",
		data: "operation=99&ButtonType=billExport&isTemplate=true&tableName="+$("#importName").val(),
		success: function(msg){
			if(msg=="error"){
				asyncbox.tips('下载失败!','error');
			}else{
				window.location.href=msg;
			}
		}
	});	
}



function keyDown(e) { 
	var iekey=event.keyCode; 
	if(iekey==13){
		alert("请选择要导入的文件!");
		return false;
	}
}
document.onkeydown = keyDown;

</script>
</head>
<body onload="aa();">
#set($tableName=$request.getParameter("tableName"))
<form  method="post" scope="request" id="form" name="form" action="/importDataAction.do" enctype="multipart/form-data" >
<input type="hidden" name="operation" value="$globals.getOP("OP_IMPORT")"/>
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/>
<input type="hidden" name="winCurIndex" value="$winCurIndex"/>
<input type="hidden" id="importName" name="importName" value="$tableName"/>
<input type="hidden" name="moduleType" value=""/>
<input type="hidden" name="moduleParam" value=""/>
<input type="hidden" name="NoBack" value=""/>
<input type="hidden" name="fromPage" value="$!fromPage"/>
		<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span>$text.get("excel.list.updata") &nbsp;--&nbsp; [&nbsp;$!globals.getTableDisplayName($tableName)&nbsp;]</span>
						
						
					</div>
					<div id="cc" class="data" style="width:100%; overflow:hidden;overflow-y:auto;width:100%;">
						<script type="text/javascript">
							var oDiv=document.getElementById("cc");
							var sHeight=document.documentElement.clientHeight-55;
							oDiv.style.height=sHeight+"px";
						</script>
						<div class="boxbg2 subbox_w700">
							<div class="subbox cf">
								<div class="inputbox">
									<div>					
									#if("$!NoBack" !="NoBack")
										#if($request.getParameter("NoBack") != "NoBack")
										<input class="bu_02" style="margin-right:10px;margin-bottom:2px;float: right;" type="button" name="back" value="返回"  onclick = "location.href='/importDataQueryAction.do'"; />
										#end
									#end
									</div> 
									<div>
										<ul>
											<span style="line-height: 40px;"><span class="num_1"></span><font>获取execl导入模板，并填写内容</font></span>
											<div style="width: 100%;">
												<div style="float: left;margin-left: 40px;margin-right:5px; padding-top:2px; vertical-align: middle;">导入模板: </div>
												
							        		 		<div class="lp"  id="$view.id"  style="float: left;"  onclick="templateDown();"><span class="a"></span>点击下载<span class="c"></span></div>
											</div>
											<li style="list-style-type: none;"><font color="red">*</font>注：请勿改动模板中的名称，否则可能导入错误</li>
										</ul>
									</div>
									<div>
										<ul>
											<span style="line-height: 40px;"><span class="num_2"></span><font>另存为xls格式文件</font></span>
											<li>数据录入完成后，在excel中，选中菜单"文件" - "另存为..."</li>
											<li>在另存为的窗口，底部"保存类型"中选择 "*.xls"</li>
											<li>忽略兼容性提示，点击是，保存</li>
										</ul>
									</div>
									<div>
										<ul>
											<span style="line-height: 40px;"><span class="num_3"></span><font>上传xls格式文件</font></span>
											<li>将上一步生成的xls文件上传到服务器</li>
											<li style="list-style: none;">
												<input type="file" name="fileName"  id="fileName" style="width:250px;height:24px; vertical-align:bottom" class="text" />
												<button type="button" onclick="return getExcel();" class="bu_02">$text.get("excel.lb.upload")</button>&nbsp;
											</li>
											
										</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
