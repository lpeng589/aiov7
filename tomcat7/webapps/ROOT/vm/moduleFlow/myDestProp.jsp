<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>123</title>
<link rel="stylesheet" href="/style/css/about.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script src="/js/jquery.js" type="text/javascript"></script>
<style>
#bodyUl button {height:20px; line-height:20px; overflow:hidden; margin:0px; padding:0px;}
</style>
<script language="javascript">
	function goCSPrograme(varPath){
   		var obj = new ActiveXObject("KoronCom.MACAddress") ;
    	var isExist = obj.findapp(varPath) ;
    	if(isExist==1){
    		obj.runapp(varPath) ;
    	}else{
    		alert("$text.get('com.programe.not.exist')") ;
    	}
    }
    
    function addMyDest(title,url){
		if(url.indexOf("src=menu") >0){
			url = url.substring(0,url.indexOf("src=menu") -1);
		}
		jQuery.get("/moduleFlow.do?operation=addMyDest&title="+encodeURIComponent(title)+"&url="+encodeURIComponent(url), function(data){ 		
			alert(data); 
			for(i=0;i<document.frames.length;i++){
				if(document.frames[i].frameElement.src.indexOf("/moduleFlow.do?operation=my_dest")>-1){
					document.frames[i].location.reload();				
				}
			}		
		 }); 
	}
</script>
</head> 

<body >
<form  method="post" scope="request" id="form" name="form" action="/AIOBOL88Action.do">
<input type="hidden" name="operation" value="1">
<input type="hidden" name="aioClose" value="0">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">
		$text.get("oa.mydesk.program")
	</div>
	<ul class="HeadingButton">
		<li></li>
	</ul>
</div>
<div id="listRange_id"  align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div style="margin-top:15px">
<div id="contentleft">
		    <div id="contentright">
		    <div id="contentMain"> 
<div id="Content">
    <div id=pageTitle><div>
	</div></div>
	<div id="bodyUl">
	
		<ul >		
			<li >
			
#foreach($programe in $programeList) 
	#if("$globals.get($programe,3)"=="cs")
		<a href='javascript:goCSPrograme("$globals.addStringAfter($globals.get($programe,1))")' class="regbut" style="text-align:center">$globals.get($programe,0)</a>
		<a href='javascript:addMyDest("$globals.get($programe,0)","CS:$globals.addStringAfter($globals.get($programe,1))")' style="font-size:11px;color:#000">$text.get("common.lb.addto")$text.get("aio.lb.mynav")</a><br/><br/>
	#else
		<a href='#if($globals.get($programe,1).indexOf("http://")==-1)http://#end$globals.get($programe,1)' target="_blank" class="regbut" style="text-align:center">$globals.get($programe,0)</a>
		<a href='javascript:addMyDest("$globals.get($programe,0)","BS:$globals.addStringAfter($globals.get($programe,1))")'  style="font-size:11px;color:#000">$text.get("common.lb.addto")$text.get("aio.lb.mynav")</a><br/><br/>
	#end
#end
					
			</li>
		</ul>
	</div>
		    </div>
            </div>
	    </div>
				    </div>
            </div>
	    </div>
</form>
</body>
</html>

