<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" type="text/css"/>
<link rel="stylesheet" href="/js/tree/jquery.treeview.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.cookie.js" ></script>
<script type="text/javascript" src="/js/tree/jquery.treeview.js" ></script>

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

function goFarme(url)
{
	window.parent.f_mainFrame.location=url;	
}

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
		goFarme("/OAnewOrdain.do?operation=4&opentype=returnindex");
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
	$("#selectId").val(y);
	$("#selectType").val("time");
}

function insertType(x){
	changeColor(x,"type_");
	$("#selectId").val(x);
	$("#selectType").val("type");
}

function insertkeyword(){
	changeColor("", "");
	var keywordVal = $("#keyWord").val();
	if(keywordVal=="关键字搜索" || keywordVal.trim()==""){
		self.parent.frames["f_mainFrame"].asyncbox.alert("请输入关键字！","搜索提示") ;
		return false ;
	}
	var selectType="keyword";
	goFarme("/OAnewOrdain.do?operation=$globals.getOP("OP_QUERY")&keywordVal="+encodeURI(keywordVal)+"&selectType="+selectType);
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
	</head>

	<body>
		<input type="hidden" id="selectId" value="$!ordainSearchForm.selectId" />
		<input type="hidden" id="selectType" value="$!ordainSearchForm.selectType" />
		<input type="hidden" id="fCode" value="$!fCode"/>
		<table cellpadding="0" cellspacing="0" border="0" class="frame">
			<tr>
				<td class="bigTitle">
					$text.get("oa.common.bylaw")
				</td>
			</tr>
			<tr>
				<td class="F_left">
					<div class="leftModule">
						<ul style="padding:0;margin:0;">
							<li style="margin: 3px;"><!--
								<b>$text.get("oa.common.keyWord")：</b>
								<input type="text" id="keyWord" size="6" name="keyWord"
									value="$!ordainSearchForm.keyWord"  onKeyDown="if(event.keyCode==13) insertkeyword();" />
								<img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onclick="insertkeyword();" />
								--><div><input type="text" id="keyWord" name="keyWord" class="search_text" value="关键字搜索" 
									onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索';" 
									onfocus="if(this.value=='关键字搜索'){this.value='';}this.select();" /><input type="button" class="search_button" onclick="insertkeyword();"/></div>
							
							</li>
						</ul>
					</div>
					<div  id="cc" class="leftModule" style="overflow-x:auto;overflow-y: auto;">				
<script type="text/javascript">
	var oDiv=document.getElementById("cc");
	var sHeight=document.documentElement.clientHeight-90;
	oDiv.style.height=sHeight+"px";
</script>
						<div class="leftMenu">
							<div class="leftMenu2" onclick="javascript:showMenu('pic1');javascript:goFarme('/OAnewOrdain.do?operation=4&opentype=indexType');">
								<span class="ico_1" id="change"></span>$text.get("oa.ordain.lb.group")
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
								#foreach($row_time in $globals.getEnumerationItems("Times"))
								<li>
									<a
										href="javascript:clearContent();goFarme('/OAnewOrdain.do?operation=$globals.getOP("OP_QUERY")&selectId=$row_time.value&tree=time&selectType=time')" onclick="insertTime($row_time.value);"  class="left_mail_li"><font id="time_$row_time.value" style="color: black">$row_time.name</font></a>
								</li>
								#end
							</ul>							
						</div>
						
						<div>
							<div class="leftMenu2" style="display:block;" onclick="clearContent();showWindow();">
								<span class="ico_4"></span>$text.get("com.query.conditions")</a>
							</div>
							#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAOrdainGroup").query())
							<div class="leftMenu2" onclick="javascript:goFarme('/OAOrdainGroup.do?operation=$globals.getOP("OP_QUERY")&dealType=ordainGroup&first=1')">
									<img src="style1/images/show.gif" style="margin-left: 2px;"></img>目录管理$text.get("common.msg.set")</a>
							</div>
							#end
						</div>
						<br/>
						<br/><br/><br/>
					</div>

				</td>
			</tr>
		</table>
	</body>
</html>
