<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>$text.get("excel.lb.addModel")</title>
		<link rel="stylesheet" href="/style/css/about.css" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="/style/themes/default/easyui.css"/>
		<link rel="stylesheet"	href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
		
		
<style type="text/css">
#positionDiv input{
	width: 50px;
}
</style>
		<script language="javascript">
function closeWin(){
	this.parent.win.removewin(this.parent.win.currentwin);
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
		if(lastName.toLowerCase()!=".txt")
		{
			alert('$text.get("excel.format.error")');
			return false;
		}else
		{
			if($('#numStart').val()=="" ){
				alert('$text.get("hr.upload.numberPosition")' + '$text.get("common.msg.fileFlow.NoContent")');
				$('#numStart').focus();
				return false;
			}else if($('#numEnd').val()==""){
				alert('$text.get("hr.upload.numberPosition")' + '$text.get("common.msg.fileFlow.NoContent")');
				$('#numEnd').focus();
				return false;
			}else if($('#dateStart').val()==""){
				alert('$text.get("hr.upload.datePosition")' + '$text.get("common.msg.fileFlow.NoContent")');
				$('#dateStart').focus();
				return false;
			}else if($('#dateEnd').val()==""){
				alert('$text.get("hr.upload.datePosition")' + '$text.get("common.msg.fileFlow.NoContent")');
				$('#dateEnd').focus();
				return false;
			}else if($('#timeStart').val()==""){
				alert('$text.get("hr.upload.timePosition")' + '$text.get("common.msg.fileFlow.NoContent")');
				$('#timeStart').focus();
				return false;
			}else if($('#timeEnd').val()==""){
				alert('$text.get("hr.upload.timePosition")' + '$text.get("common.msg.fileFlow.NoContent")');
				$('#timeEnd').focus();
				return false;
			}
			
			var flag = "false";
			$("#positionDiv input").each(function(){
				if($(this).val() == "0"){
					alert("输入的位置不能为0，请重新输入正确位置");
					$(this).select();
					flag = "true";
					return false;
				}
			})
			if(flag == "true"){
				return false;
			}
			
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

 	window.location.href="/UtilServlet?operation=importTemplete&id="+keyId+"&winCurIndex=3";
}

function checkNum(objId){
	var o = document.getElementById(objId);
	if(isNaN(o.value)){
		alert("非法输入,请输入数字!");
		o.value = "";
		o.focus();
	};
}
</script>
		<style>
#bodyUl button {height:20px; line-height:20px; overflow:hidden; margin:0px; padding:0px;}
</style>
	</head>

	<body>
		<form method="post" scope="request" id="form" name="form"
			action="/TextUploadAction.do" enctype="multipart/form-data">
			<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_ADD")">
			<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN"
				value="$!globals.getToken()">
			<input type="hidden" id="winCurIndex" name="winCurIndex" value="$winCurIndex">
			<input type="hidden" id="importName" name="importName" value="$importName" />
			<input type="hidden" id="NoBack" name="NoBack" value="$!NoBack">
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("excel.list.updata")
				</div>
			</div>
			<div id="listRange_id" align="center">
				<div class="upgrade_mtop">
					<div id="contentleft">
						<div id="contentright" style="float: none;border-left:1px solid #ccc;border-right: 1px solid #ccc;background-image: none;">
							<div id="contentMain">
								<div id="Content">
									<div id=pageTitle>
										<span
											style="font-weight: bold; font-size: 14px; float: left; color: #5e5e5e;letter-spacing:0px">$!targetName
											$text.get("excel.bill.import")</span>
										<div>
										</div>
									</div>
									<div id="bodyUl">
												
									</div>
									<div style="margin-left: -190px;">
										<div style="margin-top: 30px;" id="positionDiv">
														<div style="margin-top: 10px;margin-left: -50px;">$text.get("hr.upload.numberPosition") : &nbsp;&nbsp;<input type="text"  id="numStart" name="numStart" class="easyui-validatebox" required="true" onchange="checkNum(this.id)" value="$!cardRecordPosition.numStart"/> - <input type="text" id="numEnd"  name="numEnd" onchange="checkNum(this.id)" class="easyui-validatebox" required="true" value="$!cardRecordPosition.numEnd"/><br/></div>
														<div style="margin-top: 10px;margin-left: -50px;">$text.get("hr.upload.datePosition") : &nbsp;&nbsp;<input type="text"  id="dateStart" name="dateStart" class="easyui-validatebox" required="true" onchange="checkNum(this.id)" value="$!cardRecordPosition.dateStart"/> - <input type="text" id="dateEnd" name="dateEnd" onchange="checkNum(this.id)" class="easyui-validatebox" required="true" value="$!cardRecordPosition.dateEnd"/><br/></div>
														<div style="margin-top: 10px;margin-left: -50px;">$text.get("hr.upload.timePosition") : &nbsp;&nbsp;<input type="text"  id="timeStart" name="timeStart" class="easyui-validatebox" required="true" onchange="checkNum(this.id)" value="$!cardRecordPosition.timeStart"/> - <input type="text" id="timeEnd" name="timeEnd" onchange="checkNum(this.id)" class="easyui-validatebox" required="true" value="$!cardRecordPosition.timeEnd"/><br/></div>
										</div>
										<div class="itemName" style="margin-left: 70px;margin-top: 7px;">
														$text.get("excel.datafile")：



	
														<input type="file" name="fileName" id="fileName"
														style="width:180px; vertical-align:bottom;" class="text" />&nbsp; &nbsp; 
														<button type="button" onClick="return getExcel();"
														class="b2">
														$text.get("excel.lb.upload")
														</button>
														&nbsp; 
														<!-- <button type="button" onClick="download('$importName');"
															class="b4">
															$text.get("excel.export.format")
														</button> -->
														#if("$!exampleName"!="") &nbsp;
														<button type="button"
															onClick="downloadExample('$!exampleName');" class="b4">
															$text.get("com.import.example")
														</button>
														#end
										</div>
										<div style="margin-left: 340px;text-align: left;margin-top: 15px;" >
											<span style="font-size: 14px;">导入说明 :</span><br/>
											<span>如需导入的行数据格式为：0002117392&nbsp;&nbsp;2012-11-23_10:44:16（<span style="color: red;">注</span>:空格、下划线等字符算一位数）</span><br/>
											<span >卡号位置输入 : 1-10</span><br/>
											<span >日期位置输入 : 12-21</span><br/>
											<span >时间位置输入 : 23-30</span><br/>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div style="display:none;" align="center">
			<div id="ask_load">
				<link rel="stylesheet"
					href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
				<div class="listRange_div_jag_hint" style="margin:0px;">
					<div class="listRange_div_jag_div">
						<span>$text.get("import.plan.prompt")</span>
					</div>
					<ul class="listRange_woke">
						<li>
							<span>$text.get("aio.import.total")</span>
							<div id="imTotal">
								0
							</div>
						</li>
						<li>
							<span>$text.get("import.succees.number")</span>
							<div id="imSuccess">
								0
							</div>
						</li>
						<li>
							<span>$text.get("import.failure.number")</span>
							<div id="imError">
								0
							</div>
						</li>
						<li
							style=" text-align:left; width:380px;height:10px;line-height:10px;">
							<div id="progress"
								style="width:375px;text-align:left;background: white; height: 9px; padding: 2px; border: 1px solid green; margin: 2px;">
								<span id="progressSpan"
									style="background: green; height: 7px; text-align: center; padding: 0px; margin: 0px;display: block; color: yellow; font-weight: bold; font-size: 7px; width: 0%;">
								</span>
							</div>
						</li>
						<li style=" text-align:right; width:220px" id="imCancel">
							<input type="button"
								style="border:0;border:1px solid #1CA4FC;text-align:center;margin-right:5px;height:20px;padding-top:2px;cursor:pointer;color:#02428A;background:url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left top;"
								id="cancel" name="cancel" value="$text.get('import.importUpdata.lb.stop')"
								onClick="top.cancelImport = true;" />
							</span>
						</li>
					</ul>
				</div>
			</div>
			<div id="ask_load2">
				<link rel="stylesheet"
					href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
				<div class="listRange_div_jag_hint" style="margin:0px;">
					<div class="listRange_div_jag_div">
						<span>$text.get("import.plan.prompt")</span>
					</div>
					<ul class="listRange_woke">
						<li>
							$text.get("now.prepare.import")
						</li>
					</ul>
				</div>
			</div>
		</div>
	</body>
</html>
