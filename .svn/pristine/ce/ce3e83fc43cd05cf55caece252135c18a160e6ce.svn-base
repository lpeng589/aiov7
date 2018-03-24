<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.cookie.js" ></script>	
<script type="text/javascript">			
function showMenu(selectid){
var id="#"+selectid;
	if($(id).css("display") == "block"){
		$("#change").removeClass();
		$("#change").addClass("ico_1");
		$(id).hide();
	}	
	else{
		$(id).show();
		$("#change").removeClass();
		$("#change").addClass("ico_2");
	}	
}	

function showWindow(){
	changeColor("","");
	var m=self.parent.frames["f_mainFrame"].n;
	if(m=="queryindex"){
		self.parent.frames["f_mainFrame"].showSearch();
	}else{
		goFarme("/OAnewAdvice.do?operation=4&opentype=returnindex");
	}
}

function goFarme(url)
{
	window.parent.f_mainFrame.location=url;	
}

function changeColor(newId,newType){
	var oldId=$("#selectId").val();
	var oldtype=$("#selectType").val()+"_";
	var newid="#"+newType+newId;
	var oldid="#"+oldtype+oldId;
			
	if($(oldid).css("color") == "red"){
		$(oldid).css("color","black");
	}
	if($(newid).css("color") == "black"){
		$(newid).css("color","red");
	}
}

function insertType(x){
	changeColor(x,"type_");
	document.getElementById("selectId").innerText=x;
	document.getElementById("selectType").innerText="type";
}

function insertTime(y){
	changeColor(y,"time_");
	document.getElementById("selectId").innerText=y;
	document.getElementById("selectType").innerText="time";
}

function insertkeyword(){
	changeColor("", "");
	var keywordVal = $("#keyWord").val();
	if(keywordVal=="关键字搜索" || keywordVal.trim()==""){
		self.parent.frames["f_mainFrame"].asyncbox.alert("请输入关键字！","搜索提示") ;
		return false ;
	}
	var selectType="keyword";
	goFarme("/OAnewAdvice.do?operation=$globals.getOP("OP_QUERY")&keywordVal="+encodeURI(keywordVal)+"&selectType="+selectType);
}

function clearContent(){
	var oCommet = document.getElementById("keyWord");
	if(oCommet.value!="关键字搜索"){
		oCommet.value="关键字搜索";
	}
}

function refreshself(){
	window.location.reload();
}
</script>
<style type="text/css">
.left_mail ul li .w_font{width:140px;display:inline-block;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;}
</style>
	</head>
	<body>
		<input type="hidden" id="selectId" value="$!adviceSearchForm.selectId" />
		<input type="hidden" id="selectType" value="$!adviceSearchForm.selectType" />
		<table cellpadding="0" cellspacing="0" border="0" class="frame">
			<tr>
				<td colspan="2" class="bigTitle">$text.get("oa.mydesk.notice")</td>
			</tr>
			<tr>
				<td class="F_left">
					<div class="left_mail" >
						 <ul style="padding:0;margin:0;">
							<li style="margin: 3px;"><!--			
									<b>$text.get("oa.common.keyWord")：</b>
									<input type="text" id="keyWord" size="6" name="keyWord" value="$!adviceSearchForm.keyWord" onKeyDown="if(event.keyCode==13) insertkeyword();"/>
										<img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onclick="insertkeyword();" />
									--><div><input type="text" id="keyWord" name="keyWord" class="search_text" value="关键字搜索" 
										onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索';" 
										onfocus="if(this.value=='关键字搜索'){this.value='';}this.select();" /><input type="button" class="search_button" onclick="insertkeyword();"/></div>
								
								</li>
						 </ul>
					</div>
						<div id="cc" class="left_mail"  style="width: 98%;overflow: hidden;overflow-y: auto;">
<script type="text/javascript">
	var oDiv=document.getElementById("cc");
	var sHeight=document.documentElement.clientHeight-95;
	oDiv.style.height=sHeight+"px";
</script>	
						<ul style="padding:0;margin:0;">
							<li>
								<a href="#" onClick="showMenu('pic1');javascript:goFarme('/OAnewAdvice.do?operation=4&opentype=indexType');"><span id="change" class="ico_1"></span>$text.get("oa.common.adviceType")</a>
								<ul id="pic1" style="display:block">
									#foreach($row_newsType in $globals.getEnumerationItems("AdviceType"))
										<li>
											<a href="javascript:clearContent();goFarme('/OAnewAdvice.do?operation=$globals.getOP("OP_QUERY")&selectId=$row_newsType.value&tree=type&selectType=type')" onclick="insertType($row_newsType.value);">
												<font class="w_font" id="type_$row_newsType.value" style="color: black" title='$row_newsType.name'>$row_newsType.name</font>
											</a>
										</li>
									#end
								</ul>
							</li>
						</ul>
						<ul style="padding:0;margin:0;">
							<li>
								<a href="#" onClick="showMenu('pic2');"><span class="ico_3"></span>$text.get("com.lb.timeSearch")</a>
								<ul id="pic2" style="display:block">
									#foreach($row_time in $globals.getEnumerationItems("Times"))
										<li>
											<a href="javascript:clearContent();goFarme('/OAnewAdvice.do?operation=$globals.getOP("OP_QUERY")&selectId=$row_time.value&tree=time&selectType=time')" onclick="insertTime($row_time.value);"><font id="time_$row_time.value" style="color: black">$row_time.name</font></a>
										</li>
									#end
								</ul>
							</li>
						</ul>
						<ul style="padding:0;margin:0;">
							<li>
							
							<a href="#" onclick="clearContent();showWindow();"><span class="ico_4"></span>$text.get("com.query.conditions")</a>
							
							
							</li>
						</ul>
						<br/>
						<br/>
						<!--<ul>
							<li>
								<a href="#" onclick="javascript:goFarme('/EnumerationAction.do?operation=7&type=newupdate&keyId=f4e71763_0904070858117960001&winCurIndex=4')"><span class="ico_4"></span>$text.get("oa.common.adviceType")$text.get("common.msg.set")</a>
							</li>
						</ul>
					--></div>
				</td>
			<tr>
		</table>
	</body>
</html>
