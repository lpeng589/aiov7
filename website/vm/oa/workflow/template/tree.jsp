<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/style1/css/workflow.css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/js/tree/jquery.treeview.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.cookie.js" ></script>
<script type="text/javascript" src="/js/tree/jquery.treeview.js" ></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script type="text/javascript">
var moduleType="$!moduleType";
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

function clearCommet(){
	var oCommet = document.getElementById("search_text");
	if(oCommet.value!="关键字搜索"){
		oCommet.value="关键字搜索";
	}
}

function insertkeyword(){
	var keywordVal = $("#search_text").val();
	if(keywordVal=="关键字搜索")
		keywordVal="";
	goAction("keyword",keywordVal);
	
}

function goAction(type,value){
	if(type!="keyword")
		clearCommet();
	goFarme("/OAWorkFlowTempQueryAction.do?operation=4&winCurIndex=$!winCurIndex&selectType="+type+"&val="+encodeURI(value)+"&moduleType="+moduleType);
}

function showWindow(){
	self.parent.frames["f_mainFrame"].showSearch();	
}

function goFarme(url){	
	window.parent.f_mainFrame.location=url;	
}

function showMenu(selectid){
	var id="#"+selectid;
	var changeId="#change"+selectid;
	if($(id).css("display") == "block"){
		$(changeId).removeClass();
		$(changeId).addClass("off");
		$(id).hide();
	}	
	else{
		$(id).show();
		$(changeId).removeClass();
		$(changeId).addClass("on");
	}
}	

/*wyy*/
function openSetting(){
	var url = "/OAWorkFlowTempAction.do?queryType=flowSet&moduleType="+moduleType;
	window.parent.f_mainFrame.asyncbox.open({
		id:'ADDDIV',url:url,title:'审核流类型列表',width:300,height:340,cache:false,modal:true,
		btnsbar:[{
			 text    : '保存',              
			 action  : 'ok'            
			},{
			 text    : '关闭',              
			 action  : 'no'            
			  }],
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.addNext();			   
				return false;
			}
		    if(action == 'no'){
		    	window.parent.f_mainFrame.jQuery.close("ADDDIV");
				return false;
			}			
　　  　 }
　  });
}
	</script>
	</head>
	<body>
		<table cellpadding="10px;" cellspacing="0" border="0" class="frame" style="margin-top:4px;">
		<input type="hidden" id="win" name="winCurIndex" value="$!winCurIndex"/>
			<tr><!--左边菜单开始-->
				<td class="leftMenu">
					<div class="TopTitle"><span><img src="/style1/images/workflow/ti_001.gif" /></span><span>工作流设计</span></div>
					<div class="LeftBorder">
						<div class="New_search">
							<div><input id ="search_text"  style="width: 120px;" name="content" type="text"  class="search_text" value="关键字搜索" onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索';" 
									onfocus="if(this.value=='关键字搜索'){this.value='';}this.select();"/><input type="button" class="search_button" style="cursor: pointer;" onclick="insertkeyword();"/></div>

						</div>
						<div id="cc" style=" width:100%; overflow:hidden;overflow-y: auto;">
<script type="text/javascript">
	var oDiv=document.getElementById("cc");
	var sHeight=document.documentElement.clientHeight-120;
	oDiv.style.height=sHeight+"px";
</script>
						
							<div class="leftMenu">
								<div class="leftMenu2" onclick="showMenu('pic')" >
									<li style="cursor: pointer;" ><span id="changepic" class="on">流程类别</span>
								</div>
								<div style="display:block; padding-left:10px;padding-top: 30px;" id="pic" >
									<ul id="navigation">								
										$!result
									</ul>
								</div>
							</div>
							<ul class="LeftMenu_list">	
								<li onclick="goAction('all','all');" style="cursor: pointer;"><span class="off">所有工作流</span></li>							
								<li onclick="goAction('status','1');" style="cursor: pointer;"><span class="off">$text.get("wokeflow.lb.enable")工作流</span></li>
								<li onclick="goAction('status','0');" style="cursor: pointer;"><span class="off">停用工作流</span></li>
								<li onclick="openSetting();" style="cursor: pointer;"><span class="off">工作流类型设置</span></li> 
								<!--<li onclick="goFarme('/UserFunctionQueryAction.do?tableName=tblWorkFlowType');" style="cursor: pointer;"><span class="off">工作流类型设置</span></li>
								<!--<li onclick="showWindow();" style="cursor: pointer;"><span class="off">工作流查询</span></li>
								--><li></li>
							</ul>
						
						</div>
					</div>				
				</td><!--左边菜单结束-->
			</tr>
		</table>
	</body>
</html>
