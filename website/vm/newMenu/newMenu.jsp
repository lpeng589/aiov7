<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=8"/>
<title></title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" >
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/> 

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
$(function(){
	$(".menuReport").click(function(){
		$(this).children("ul").show().end().siblings().children("ul").hide();
	});
	
	$("#table1 tr td").bind('contextmenu', function(e) {
		if($(this).children("img").attr("src").indexOf("sale")==-1){
			return false;
		}
		$("#lableId").show();
		$("#editId").hide();
		
		var top = $(this).position().top + 36 ;
		var left = $(this).position().left + 30 + "px";
		if($(this).position().top+245>$(document).height()){
			top = top - 120;
		}
		top += "px";
		$("#labelShow").css("top",top);
		$("#labelShow").css("left",left);
		$("#labelShow").attr("label",$(this).attr("id"));
		$("input[name='opertionType'][value='"+$(this).children("img").attr("opType")+"']").attr("checked",true);
		$("#labelShow").show();		
		return false;
	});
	
	$(document).click(function(event){
		var targetLen = $(event.target).parents(".nui-menu").length;
		if(targetLen==0){
			$("#labelShow").hide();
		}
	});
	
	$(".editBtn").click(function(){
		var editName = $(".editName").val();
		$("#"+($("#labelShow").attr("label")) + " img").attr("title",editName);
		$("#"+($("#labelShow").attr("label")) + " img").attr("dis",editName);
		if($("#"+($("#labelShow").attr("label")) + " p").size()>0){
			$("#"+($("#labelShow").attr("label")) + " p").html(editName);
		}else{
			$("#"+($("#labelShow").attr("label"))).append("<p>"+editName+"</p>");
		}
		$("#labelShow").hide();
	});
	
	$("#lableId").click(function(){
		$("#lableId").hide();
		$("#editId").show();
		$(".editName").val("").focus();
		var dis = $("#"+($("#labelShow").attr("label")) + " img").attr("dis");
		if(typeof(dis)!="undefined"){
			$(".editName").val(dis);
		}
	});
});

function setOpType(opType){
	$("#"+($("#labelShow").attr("label")) + " img").attr("opType",opType);
	$("#labelShow").hide();
}

function openSelect2(){
	openSelect1($("#"+($("#labelShow").attr("label")) + " img")[0]);
	$("#labelShow").hide();
}

function showreModule(title,href){		
	top.showreModule(title,href);	
}
</script>
<style type="text/css">
.listRange_frame table td{text-align:center; width:65px;height:65px; border:1px dashed #ccc;padding:0px;margin:0px;}
.listRange_frame table td img{float:left;margin:0px;padding:0px;}
.listRange_frame table td img.hbg{margin:0 0 0 8px;}
.listRange_frame table td p{float:left;display:inline-block;margin:-5px 0 0 0;width:64px;height:14px;text-align:center;}
img.hbg{background:url(/style/images/body2/show_bg.png) no-repeat 1px 2px;width:44px;}
</style>
</head>
<body >
<form  method="post" scope="request" name="form" action="/newMenu.do?operation=$globals.getOP('OP_ADD')">
 <!--  <input type="hidden" name="operation" value="4"> -->
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="a8358e12dec066bd31066cc9f10a282d"/>
 <input type="hidden" name="name" value="$!name"/>
 <input type="hidden" name="keyId" value="$!keyId"/>
 <input type="hidden" name="tdName" id="tdName" value=""/>
 <input type="hidden" name="flag" value="$flag"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$!name</div>
	<div style="float:left; height:25px; width:30px; margin-left:5px;"><img  src="style/images/newMenu/conservation.jpg" style="width:25px; height:25px; cursor: pointer;" onclick="getTdImgName();" title="$text.get('mrp.lb.save')" /></div>
	
</div>
<div class="listRange_frame" style="overflow-y: auto; overflow-x:auto;"  align="center" id="outconter">
<script type="text/javascript">
$("#outconter").height($(document).height()-45);
</script>
  <div  style="width:78%; border:0px solid red; float:left;" >
 <table id="table1"  border="0"  cellpadding="0" cellspacing="0" style="width:736px;">
 		#set($int=0)
 		#foreach($elment in [1..7])
 			#set($int=$int+1)
 			<tr>
 				#set($td=0)
 				#foreach($elment in [1..11])
 					#set($td=$td+1)
 					<td  id="tdx${int}y$td" name="tdx${int}y$td">
 					 	 #if($!tdImgsList.get("tdx${int}y$td"))
 					 	 	#if($!tdImgLinkList.get("tdx${int}y$td"))
	 					 	 	#if($!tdImgsList.get("tdx${int}y$td").indexOf("sale")>-1)
	 						 	<img class="hbg" title="$!moduHash.get($!tdImgLinkList.get("tdx${int}y$td")).get(1)" lang=1 id="th" name="th" style="cursor:pointer; width:44px;height:44px;" alt="$!tdImgLinkList.get("tdx${int}y$td")" dis="$!tdDis.get("tdx${int}y$td")" opType="$!tdOpType.get("tdx${int}y$td")"   ondblclick="openSelect1(this);" onmousedown="getImgName(this);"  src="/style/images/newMenu/$!tdImgsList.get("tdx${int}y$td")"/>
	 						 	#else
	 						 	<img class="hbg" title="$!moduHash.get($!tdImgLinkList.get("tdx${int}y$td")).get(1)" lang=1 id="th" name="th" style="cursor:pointer;width:65px;height:65px;" alt="$!tdImgLinkList.get("tdx${int}y$td")" dis="$!tdDis.get("tdx${int}y$td")" opType="$!tdOpType.get("tdx${int}y$td")"  onmousedown="getImgName(this);"  src="/style/images/newMenu/$!tdImgsList.get("tdx${int}y$td")"/>
	 						 	#end 
	 						 	#if($!tdDis.get("tdx${int}y$td").length()>0)
	 						 		#if($!tdDis.get("tdx${int}y$td").length()>5)
	 						 		<p>$!tdDis.get("tdx${int}y$td").substring(0,5)</p>
	 						 		#else
	 						 		<p>$!tdDis.get("tdx${int}y$td")</p>
	 						 		#end
	 						 	#else
		 						 	#if( $!moduHash.get($!tdImgLinkList.get("tdx${int}y$td")).get(1).length()>5) 
		 						 		<p>$!moduHash.get($!tdImgLinkList.get("tdx${int}y$td")).get(1).substring(0,5)</p>
		 						 	#else 
		 						 		<p>$!moduHash.get($!tdImgLinkList.get("tdx${int}y$td")).get(1)</p>
		 						 	#end
	 						 	#end
 						  	#else
 						  		 #if($!tdImgsList.get("tdx${int}y$td").indexOf("sale")>-1)
 						  		 <img class="hbg" id="th" name="th" style="cursor:pointer;width:44px;height:44px;" lang=1 alt="" ondblclick="openSelect1(this);"  onmousedown="getImgName(this);" dis="$!tdDis.get("tdx${int}y$td")" opType="$!tdOpType.get("tdx${int}y$td")"   src="/style/images/newMenu/$!tdImgsList.get("tdx${int}y$td")"/>
 						  		 	#if($!tdDis.get("tdx${int}y$td").length()>5)
	 						 		<p>$!tdDis.get("tdx${int}y$td").substring(0,5)</p>
	 						 		#else
	 						 		<p>$!tdDis.get("tdx${int}y$td")</p>
	 						 		#end
 						  		 #else
 						  		 <img id="th" name="th" style="cursor:pointer;width:65px;height:65px;" lang=1 alt="" onmousedown="getImgName(this);"   src="/style/images/newMenu/$!tdImgsList.get("tdx${int}y$td")" dis="" opType="4"/>
 						  		 #end
 						  	#end
 						 #else
 							 &nbsp;
 						 #end
 					 </td>
 				#end
 			</tr>
 		#end
 </table> 
 </div>
</div>
 <div id="deleteImg" name="deleteImg"  align="center" style="display:none; width:20%;height:85px;position:absolute;top:35px;right:100px;" >
 <img id="th" name="th"  src="/style/images/newMenu/shit2.png"   title="$!text.get('oa.common.del')"/>
 
 </div>
 <div style=" position: absolute;width:232px;height:200px;top:130px;right:10px;border-left:3px solid #CCCCCC; border-top:3px solid #CCCCCC; border-bottom:3px solid  #CCCCCC;  overflow-x:auto;overflow-y: auto;" id="outconter2">
 	<script language="javascript">
		var oDiv2=document.getElementById("outconter2");
		var img="";
		for(var i=1;i<=34;i++){
			img=img+"<img id='th' name='th'  style='margin-top:5px;margin-left:1px;width:48px; height:48px;' onmousedown='getImgName(this);' src='/style/images/newMenu/direction"+i+".png'/>";
			oDiv2.innerHTML=img;
		}
	</script>
 </div>
 
 <div style=" position: absolute;width:232px;height:200px;top:337px;right:10px;border-left:3px solid #CCCCCC; border-top:3px solid #CCCCCC; border-bottom:3px solid  #CCCCCC; overflow-x:auto;overflow-y: auto;" id="outconter3" >
 	<script language="javascript">
		var oDiv3=document.getElementById("outconter3");
		var img="";
		for(var i=1;i<=40;i++){
			img=img+"<img class='hbg' id='th' name='th'  onmousedown='getImgName(this);' src='/style/images/newMenu/sale"+i+".png' />";
			oDiv3.innerHTML=img;
		}
		for(var i=41;i<=86;i++){
			img=img+"<img class='hbg' id='th' name='th'  onmousedown='getImgName(this);' src='/style/images/newMenu/sale"+i+".png' />";
			oDiv3.innerHTML=img;
		}
	</script>
 </div>
 <div id="moveDiv" name="moveDiv" onmouseup="moveDivUp(this);"  style="display:none; width:44px; height:44px; border:1 dashed #CC66FF;position: absolute;top:460px;right:0px;">
 </div>
 <div class="nui-menu" style="display: none;position:relative;z-index: 99;width:120px;" id="labelShow" label="">
	<div id="labelContDiv">
        <div class="nui-menu-item">
        	<div class="nui-menu-item-link">
            	<span class="nui-menu-item-icon"><input name="opertionType" type="radio" checked="checked" value="4" onclick="setOpType('4')"/></span>
               	<span class="nui-menu-item-text"><b class="nui-ico nui-ico-tag" style="background:$label.labelColor;"></b>默认查询</span>
            </div>
        </div>
        <div class="nui-menu-item">
        	<div class="nui-menu-item-link">
            	<span class="nui-menu-item-icon"><input name="opertionType" type="radio" value="6" onclick="setOpType('6')"/></span>
               	<span class="nui-menu-item-text"><b class="nui-ico nui-ico-tag" style="background:$label.labelColor;"></b>默认添加</span>
            </div>
        </div>
     </div>
     <div class="nui-menu-split nui-split"></div>
     <div class="nui-menu-item">
     	<div class="nui-menu-item-link" onclick="openSelect2();">
        	<span class="nui-menu-item-text2">链接地址</span>
        </div>
     </div>
     <div class="nui-menu-split nui-split"></div>
     <div class="nui-menu-item">
     	<div id="lableId" class="nui-menu-item-link">
        	<span class="nui-menu-item-text2">编辑名称</span>
        </div>
        <div id="editId" style="display:none;">
        	<span><input type="text" name="editName" class="editName" onkeydown="if(event.keyCode==13&&this.value.length>0)$('.editBtn').click();"/></span>
        	<p><button type="button" class="editBtn">保存</button></p>
        </div>
     </div>
</div>
</form>
</body>
<script language="javascript"> 
 var imgClass=""
 var flag="";
 var imgName="";
 var parentName="";
 var funTime=new Date();
 funTime.setTime(0);
 
function moveDivUp(imgThis){
	 imgThis.style.display="none";
	 document.all("deleteImg").style.display="none";
	 var tdx=(event.x-$("#table1").position().left)/67;
	 var tdy=(event.y-33+document.getElementById("outconter").scrollTop)/67;
	  
	 if(tdx>=Math.floor(tdx)&& tdy>=Math.floor(tdy)){
	  	tdx=Math.floor(tdx)+1;
	  	tdy=Math.floor(tdy)+1;
	 }
	 if(parentName!="tdx"+tdy+"y"+tdx){ 
		 if(parentName==undefined ||  parentName=="") {
		 	var imgType=imgName.indexOf("sale");
		 	if(imgType>-1){
 				$("#tdx"+tdy+"y"+tdx).html("<img class='hbg'  src='"+ imgName + "' style='cursor:pointer;width:44px;height:44px;' lang=1 dis='' opType='4' alt='' ondblclick='openSelect1(this);' onmousedown='getImgName(this);' />");
 			}else{
 				$("#tdx"+tdy+"y"+tdx).html("<img  src='"+ imgName + "' style='cursor:pointer;width:65px;height:65px;' lang=1 alt='' dis='' opType='4'  onmousedown='getImgName(this);' />");
 			}
 		 }else{
 		 	$("#tdx"+tdy+"y"+tdx).html($("#"+parentName).html());
 		 	$("#"+parentName).html("&nbsp;");
 		 }	
 	 }else{
 	   	var time=new Date();
	 	var lang=new Number(imgClass.lang);
	 	if(time.getTime()<=funTime.getTime() || funTime.getTime()==0){
	 	 	 if(funTime.getTime()==0){
	 	 	 	lang=1;
	 	 	 }
	 	 	 if(lang==2){
	 	 	 	if(document.createEvent){
		    		var evt = document.createEvent("HTMLEvents");	 	    		
		    		evt.initEvent("dblclick",false,false);
		    		imgClass.dispatchEvent(evt);	
				 }else{
					imgClass.fireEvent('ondblclick');
				 }	 	 	 	
	 	 	 	imgClass.lang=1;
	 	 	 	funTime.setTime(0);
	 	 	 }else{
	 	 	 	imgClass.lang=1+lang;
	 	 	 	funTime.setTime(time.getTime()+555);
	 	 	 }
 	 	 }else{
 	 	   	imgClass.lang=2;
 	 	   	funTime.setTime(0);
 	 	}
 	 }
 	 parentName="";
 }
 

var varLink ;
function openSelect1(link){
	varLink = link;
	var displayName = "模块管理";
	var strURL  = '/UserFunctionAction.do?operation=22&selectName=SelectNewMenuLinkAddress&popupWin=PopupDiv&MOID=111&MOOP=add&LinkType=@URL:&displayName='+displayName; 
	asyncbox.open({id:'PopupDiv',title:displayName,url:strURL,width:750,height:470});
}

function exePopupDiv(str){
	if(str.length>0){
		var str=str.split("#;#");
		var linkAddress=str[0];
		var moduName=str[1];
		varLink.title=moduName;
		if(moduName.length>5){
			moduName=moduName.substring(0,5);
		}
		varLink.alt=linkAddress;
		var tdValue=$("#"+varLink.parentElement.id).html();
		if(tdValue.indexOf("<P>")>-1 || tdValue.indexOf("<p>")>-1){
			var index=tdValue.indexOf("<p>");
			tdValue=tdValue.substring(0,index);
		   	$("#"+varLink.parentElement.id).html(tdValue+"<p>"+moduName+"</p>");
		}else{
		   $("#"+varLink.parentElement.id).html(tdValue+"<p>"+moduName+"</p>");
		}
	}
}
	
function deleteImg(){
	$("#"+flag).html("&nbsp;");
	flag="";
	imgName="";
}
	
function getImgName(Name){
	document.all("moveDiv").style.display="block";
	document.all("deleteImg").style.display="block";

	document.all("moveDiv").style.left=(event.x-25)+"px";
	document.all("moveDiv").style.top=(event.y-25)+"px";
	
	imgClass=Name;
	imgName=Name.src;
	$("#"+"moveDiv").html("<img class='hbg' src='"+ imgName + "' style='width:44px; height:44px; cursor:move; opacity:0.7;filter:alpha(opacity=70)'  />");
	flag="";
	if(Name.parentElement.nodeName!="DIV"){
		parentName=Name.parentElement.id;
	}
	mousedown(function(){return false});
}
 
 
function getTdImgName(){
	$('#table1 td img').each(function(i){
		var tdName = $(this).parent().attr("name");
		var tdImage = $(this).attr("src");
		tdImage = tdImage.substring(tdImage.lastIndexOf("\/")+1,tdImage.length);
		$("#tdName").val($("#tdName").val()+tdName+","+tdImage+","+$(this).attr("alt")+","+$(this).attr("dis")+","+$(this).attr("opType")+";"); 
	});
	document.form.submit();
}
 
var moveTdName="";
function bgFun(bg){
	// moveTdName=bg.name;
	//alert("s");
	//bg.style.border.color="red";
}
 
function overFun(bg){
	//bg.style.border.color="";
}
	
function mouseMoveDiv(obj){
	Obj=Object.id;
	document.all(Obj).style.display="block";
	document.all(Obj).style.left=(event.x)+"px";
	document.all(Obj).style.top=(event.y)+"px";
}
	
//让移动层移动
$(document).mousemove(function(e){
 	 if(document.all("moveDiv").style.display=="block"){
 		document.all("moveDiv").style.left=(event.x-25)+"px";
	 	document.all("moveDiv").style.top=(event.y-25)+"px";
	 }
});
</script>

</html>