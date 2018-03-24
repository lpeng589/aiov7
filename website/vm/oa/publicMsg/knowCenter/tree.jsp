<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>tree</title>
		<link rel="stylesheet"
			href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
		<link type="text/css" rel="stylesheet"
			href="/$globals.getStylePath()/css/oa_news.css" />
		<link rel="stylesheet" href="/js/tree/jquery.treeview.css" />
		<script type="text/javascript" src="/js/function.js"></script>
		<script src="/js/jquery.js" type="text/javascript"></script>
		<script src="/js/jquery.cookie.js" type="text/javascript"></script>
		<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
		
		<script type="text/javascript">
$(document).ready(function(){
	var curLi ;	
	// first example
	$("#navigation").treeview({
		persist: "cookie",
		cookieId: "aioemail" ,
		//animated: true,
		collapsed: true,
		unique: false
	});
	
	$("#navigation").find('span').click(function (){
		if(curLi){
			curLi.removeClass("selNode");
		}
		$(this).addClass("selNode");
		curLi = $(this);
	});
	
});

function goFarme(url){
	
	window.parent.f_mainFrame.location=url;	
}

function showMenu(selectid){
	changeColor("", "");
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
function term(){
	changeColor("", "");
	if(parent.frames.f_mainFrame.divw != null || parent.frames.f_mainFrame.divw != undefined){
		parent.frames.f_mainFrame.divw.style.display="block";
		$(parent.frames.f_mainFrame.divw).append("<input type='hidden' name='dbData' id='dbData' value='' onpropertyChange='javascript:if(this.value!=null)evaluate()'/>");
	}else{
		goFarme('/OAKnowCenter.do?type=oaKnowList&status=1&queryType=&folderCode=')
	}
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
	var selectType="key";
	goFarme("/OAKnowCenter.do?type=oaKnowList&keyWord="+encodeURI(keywordVal)+"&queryType="+selectType);
}

function insertType(x){
	changeColor(x,"type_");
	document.getElementById("selectId").innerText=x;
	document.getElementById("selectType").innerText="type";
}
function refreshself(){
	window.location.reload();
}
function groups(){
	changeColor("", "");
	goFarme('/OAKnowFolder.do?operation=4&first=1');
}
</script>
	</head>
	<body>
		<input type="hidden" id="selectId" value="" />
		<input type="hidden" id="selectType" value="" />
		<input type="hidden" id="fCode" value="$!fCode"/>
		<table cellpadding="0" cellspacing="0" border="0" class="frame">
			<tr>
				<td class="bigTitle">
					$text.get("oa.common.knowledgeCenter")
				</td>
			</tr>
			<tr>
				<td class="F_left">
					<div class="leftModule">
						<ul style="margin:0;padding:0;">
							<li style="margin: 3px;">
								<div><input type="text" id="keyWord" name="keyWord" class="search_text" value="关键字搜索" 
									onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索';" 
									onfocus="if(this.value=='关键字搜索'){this.value='';}this.select();" /><input type="button" class="search_button" onclick="insertkeyword();"/></div>
							</li>
						</ul>
					</div>
					<div id="cc" class="leftModule"	style="overflow-x:auto;overflow-y: auto;" >
					<script type="text/javascript"> 
	var oDiv=document.getElementById("cc");
	var sHeight=document.documentElement.clientHeight-90;
	oDiv.style.height=sHeight+"px";
</script>
					
						<div class="leftMenu">
							<div class="leftMenu2" onclick="javascript:showMenu('pic1');javascript:goFarme('/OAKnowCenter.do?type=oaKnowList&queryType=&folderCode=');">
								<span class="ico_1" id="change"></span>$text.get("oa.know.lb.group")
							</div>
							<div style="display:block; padding-left:10px;padding-top: 30px;" id="pic1" >
								<ul id="navigation">								
									$!result
								</ul>
							</div>
						</div>
						<div style="display:block;">
							
							<div class="leftMenu2" onclick="javascript:showMenu('pic2')" style=" margin-top:5px';">
								<span class="ico_3"></span>$text.get("com.lb.timeSearch")
							</div>
							
							<ul style="display:none;" id="pic2">
								#set($num = 1)
								#foreach($row_time in $globals.getEnumerationItems("Times"))
								<li>
									<a href="javascript:goFarme('/OAKnowCenter.do?type=oaKnowList&queryType=time&term=$num&folderCode=')" onclick="insertTime($row_time.value);"  class="left_mail_li"><font id="time_$row_time.value" style="color: black;">$row_time.name</font></a>
								</li>
								#set($num = $num+1)
								#end
							</ul>							
						</div>
						
						<div>
							<div class="leftMenu2" onclick="term();" style="display:block;">
								<span class="ico_4"></span><span>$text.get("com.query.conditions")</span>
							</div>
							#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").query())
							<div class="leftMenu2" onclick="javascript:groups()">
								<img src="style1/images/show.gif" style="margin-left: 2px;"></img>目录管理$text.get("common.msg.set")
							</div>
							#end
						</div>
					</div>

				</td>
			</tr>
		</table>
	</body>
</html>
