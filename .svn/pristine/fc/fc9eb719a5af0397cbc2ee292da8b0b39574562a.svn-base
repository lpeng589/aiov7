<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("excel.lb.addModel")</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript" src="/js/function.js"></script>  
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">

if(typeof(top.junblockUI)!="undefined"){	
	top.junblockUI();
}
cancelImport = false;


function Refresh() {
	var ready = true ;
    path="/UtilServlet?operation=importInfo&importName=$!importName"+"&time="+(new Date()).getTime();;
	if(cancelImport){
		path="/UtilServlet?operation=cancelImport&importName=$!importName"+"&time="+(new Date()).getTime();;
		cancelImport = false;
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
					//ready = false ;
				}else if(response == "finish"){	
					window.location.href="/importDataAction.do?operation=91&tableName=$!importName&parentTableName=&moduleType=$!moduleType&NoBack=$!NoBack&moduleId=$!moduleId";
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
					
				} else if(response == "ok"){	
				}
			}else{
				response = "no response text" ;
			}
		}
	};
	xmlHttp.open("get", path, false);
	xmlHttp.send();
	var hit ;
	if(!ready){		
		document.getElementById('ask_load').style.display='none'
		document.getElementById('ask_load2').style.display='block'	
		document.getElementById("impHit").innerHTML="$text.get('suspend.import.process')";
	}else{
		document.getElementById('ask_load2').style.display='none'
		document.getElementById('ask_load').style.display='block'
	}
	
	
}




function downloadExample(fileName){
	window.location.href="/ReadFile?tempFile=example&fileName="+fileName;
}
function download(keyId){

 	window.location.href="/UtilServlet?operation=importTemplete&tableName="+keyId+"&winCurIndex=3";
}
</script>
<style>
#bodyUl button {height:20px; line-height:20px; overflow:hidden; margin:0px; padding:0px;}
</style>
</head>

<body onload="Refresh();setInterval('Refresh()',1000)">

<div id="ask_load" style="display:block;width:460px;height:250px;margin:0 auto;margin-top:2%;">
				<div  class="listRange_div_jag_hint" style="margin:0;height:250px">
					<div class="listRange_div_jag_div" style="margin:0 auto;padding:0px"><span style="margin:0px;padding:5px 0px 0px 0px;">$!mainTableDisplay  -->> $text.get("excel.list.updata") $text.get("import.plan.prompt")</span></div>					
					<ul class="listRange_woke" style="width:400px;padding:0;overflow:hidden;margin:0 auto;margin-top:30px;">
						<li><span>$text.get("aio.import.total")</span><div id="imTotal">0</div></li>
						<li><span>$text.get("import.succees.number")</span><div id="imSuccess">0</div></li>
						<li><span>$text.get("import.failure.number")</span><div id="imError">0</div></li>
						<li style=" text-align:left; width:380px;height:15px;line-height:15px;" >
						<div id="progress" style="width:375px;text-align:left;background: white; height: 9px; padding: 2px; border: 1px solid green; margin: 2px;">
							<span id="progressSpan" style="background: green; height: 7px; text-align: center; padding: 0px; margin: 0px; 
      display: block; color:#000; font-weight: bold; font-size: 7px; width: 0%;"> 
      						</span></div></li>	
      					<li style=" text-align:right; width:400px; height:30px;clear:both;padding:10px 0;" id="imCancel">
						<input type="button" class="btn btn-danger btn-small" name="cancel" value="$text.get('import.importUpdata.lb.stop')" onClick="cancelImport = true;"/>
						</li>															
					</ul>
				</div>
			</div>	
</div>
<div id="ask_load2" style="display:none; position:absolute;left:35%;top:30%; width:460px;height:250px">
			<div class="listRange_div_jag_hint" style="margin:0px;height:250px">
				<div class="listRange_div_jag_div" style="margin:0px;padding:0px"><span>$text.get("import.plan.prompt")</span></div>					
					<ul class="listRange_woke" style="width:400px;padding:0;overflow:hidden;margin:0 auto;margin-top:30px;">
						<li style="height:50px;"><div id="impHit">$text.get("now.prepare.import")</div></li>											
					</ul>
				</div>
				</div>
</div>
											

</body>
</html>

