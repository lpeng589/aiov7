<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/style1/css/workflow.css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.cookie.js" ></script>
<script type="text/javascript">
function clearCommet(){
	var oCommet = document.getElementById("search_text");
	if(oCommet.value!="关键字搜索"){
		oCommet.value="关键字搜索";
	}
}

function insertkeyword(){
	var keywordVal = $("#search_text").val();
	if(keywordVal=="关键字搜索" || keywordVal.trim()==""){
		self.parent.frames["f_mainFrame"].asyncbox.alert("请输入关键字！","搜索提示") ;
		return false ;
	}
	
	var selectType="keword";
	goFarme("/AdviceAction.do?operation=4&keywordVal="+encodeURI(keywordVal)+"&selectType="+selectType);
}

function goAction(goType){
	clearCommet();
	goFarme("/AdviceAction.do?operation=4&selectType="+goType);
}
function showWindow(){
clearCommet();
	self.parent.frames["f_mainFrame"].showSearch();	
}
function goFarme(url)
{	
	window.parent.f_mainFrame.location=url;	
}

function comeAction(){
	var selectType="$!selectType";
	if(selectType!=null){
		goAction(selectType);
	}
}
		</script>
	</head>
	<body onload="comeAction();">
		<table cellpadding="0" cellspacing="0" border="0" class="framework">
			<tr><!--左边菜单开始-->
				<td class="leftMenu">
					<div class="TopTitle"><span><img src="/style1/images/workflow/ti_001.gif" /></span><span>通知消息</span></div>
					<div class="LeftBorder">
						<div class="New_search">
							<div><input id ="search_text"  style="width: 80%;" name="content" type="text"  class="search_text" value="关键字搜索" onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索';" 
									onfocus="if(this.value=='关键字搜索'){this.value='';}this.select();"/><input type="button" class="search_button" style="cursor: pointer;" onclick="insertkeyword();"/></div>
						</div>
						<ul class="LeftMenu_list">		
							<li onclick="goAction('all');" style="cursor: pointer;"><span class="off">所有消息</span></li>	
						
							<li onclick="goAction('noRead');" style="cursor: pointer;"><span class="off">未读消息</span></li>
							<li onclick="goAction('hasRead');" style="cursor: pointer;"><span class="off">已读消息</span></li>
							<li  style="cursor: pointer;"><span style="font-weight:bold; margin-left:10px; width:auto; padding-left:6px; float:left;">消息类型</span>
								<ul >
									<li onclick="goAction('daiShen');" style="cursor: pointer;margin-top: 10px;">待审消息</li>
									<li onclick="goAction('hasShen');" style="cursor: pointer;">已审消息</li>
									<li onclick="goAction('email');" style="cursor: pointer;">邮件消息</li>
									<li onclick="goAction('other');" style="cursor: pointer;">其他消息</li>
								</ul>
							</li>
							<li onclick="showWindow();" style="cursor: pointer;"><span class="off">消息查询</span></li>
							<li></li>
						
						</ul>
					</div>				
				</td><!--左边菜单结束-->
			</tr>
		</table>
	</body>
</html>
