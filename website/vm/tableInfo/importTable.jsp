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

function getZip()
{
 var path=document.getElementById("fileName").value;
   if(path.length>0)
   {
   
        var pos=path.lastIndexOf(".");
		var lastName=path.substring(pos,path.length);
		if(lastName.toLowerCase()!=".zip")
		{
			alert('文件格式错误，请选择zip模块压缩包');
			return false;
		}else
		{
			var starIndex = path.lastIndexOf("\\");
			var endIndex = path.lastIndexOf(".");
			var fileName = path.substr(starIndex+1,(endIndex-starIndex-1));
			
			document.form.submit();
			top.cancelImport= false;	
			
		}
	}else
   {
     alert('请选择zip模块压缩包');
	 return false;
   }
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
<form  method="post" scope="request" id="form" name="form" action="/CustomImportAction.do" enctype="multipart/form-data" >
<input type="hidden" name="type" value="importTable"/>
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/>
		<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span>模块导入</span>
						
						
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
										<ul>
											<span style="line-height: 40px;"><span class="num_1"></span><font>上传zip压缩格式文件</font></span>
											<li>文件为模块导出格式</li>
											<li style="list-style: none;">
												<input type="file" name="fileName"  id="fileName" style="width:250px;height:24px; vertical-align:bottom" class="text" />
												<button type="button" onclick="return getZip();" class="bu_02">$text.get("excel.lb.upload")</button>&nbsp;
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
