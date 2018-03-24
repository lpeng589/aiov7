<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$globals.getCompanyName('')</title>
	
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="$globals.js("/js/mainBody.vjs","",$text)"></script>

<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
<link rel="stylesheet" href="/js/tree/jquery.treeview.css" /> 

<link rel="stylesheet" href="style/css/homeMobile.css" type="text/css" />
<script language="JavaScript"> 
	
#if("$!globals.getSystemState().noRegUseDate" != "")
    if(confirm("$text.get("common.lb.plsReg")")){
    	window.location.href = '/RegisterAction.do?regFlag=2&step=two&encryptionType=$!globals.getSystemState().encryptionType';
	}
#end	

function mdiwin(href,title)
{	
	showreModule(title,href);
}
function showreModule(title,href)
{	
	pindex.contentWindow.mdiwin(href,title);
}

var showorganizaion = false;
function organization(){
	jQuery.get("/UtilServlet?operation=showEmp&departmentCode="+encodeURI("$text.get("emList.Title")")+"&"+new Date().getTime(), function(data){ 	
		$("#PopUp_organizationDIV").html(data);
		$("#PopUp_organizationDIV").css("display","block");
		showorganizaion  = true;
		$("#navigation").treeview({	
			animated: false,
			collapsed: false,
			unique: false
		});	
		 
		$("#PopUp_organizationDIV").unbind();		
		$("#PopUp_organizationDIV").bind("mouseout", function(){  
			if(	event.srcElement == document.getElementById("PopUp_organizationDIV") || 
				event.srcElement == document.getElementById("organizationOut")||
				event.srcElement == document.getElementById("organizationBody")
				){
				 $("#PopUp_organizationDIV").css("display","none");				 
			} 
		}); 
		
		var s = document.getElementById("PopUp_organizationDIV").getElementsByTagName('*');				
		for(i=0;i<s.length;i++){		
			regEvent(s[i],'mouseover',function(){
				$("#PopUp_organizationDIV").css("display","block"); 
				window.event.cancelBubble = true;
			});
		}		
			
	 }); 
}
function departChange(){	
	jQuery.get("/UtilServlet?operation=showEmp&departmentCode="+encodeURI($("#departmentCode").val())+"&"+new Date().getTime(), function(data){ 		
		$("#PopUp_organizationDIV").html(data);
		$("#PopUp_organizationDIV").css("display","block");
		$("#navigation").treeview({	
			animated: false,
			collapsed: $("#departmentCode").val()=="" || $("#departmentCode").val()=="$text.get("oa.oamessage.usergroup")",
			unique: false
		});	
		$("#PopUp_organizationDIV").unbind();		
		$("#PopUp_organizationDIV").bind("mouseout", function(){  
			if(	event.srcElement == document.getElementById("PopUp_organizationDIV") || 
				event.srcElement == document.getElementById("organizationOut")||
				event.srcElement == document.getElementById("organizationBody")
				){
				 $("#PopUp_organizationDIV").css("display","none");				 
			} 
		}); 
		
		var s = document.getElementById("PopUp_organizationDIV").getElementsByTagName('*');				
		for(i=0;i<s.length;i++){		
			regEvent(s[i],'mouseover',function(){
				$("#PopUp_organizationDIV").css("display","block"); 
				window.event.cancelBubble = true;
			});
		}	
	 }); 
}
function regEvent(target, type, func){
	if (target.addEventListener){
       	target.addEventListener(type, func, false);
	}else if (target.attachEvent){
        target.attachEvent("on" + type, func);
    }else{ target["on" + type] = func;}
}
function sendmsg(id,type){ 
	mdiwin('/MessageAction.do?operation=70&empId='+id,'$text.get('message.lb.pageTitle')');
}
</script>
</head>
<body onLoad="setTimeout('flushMsg()',30000);#if("true" == "$!globals.getSysSetting('aioshop')")flushAIOShop();#end isExistNewChat();">
	<div class="top">
	<table width="100%" >
	<tr>
	<td width="150px" >
		<div class="top_UserName"><a title="#if($!NowYear==-1)$text.get("login.lb.noMakeBill")#else$!NowYear.$!NowPeriod #end">欢迎，$LoginBean.empFullName</a></div>
	</td>
	<td>
		<div class="AdShow">
			<iframe src="bottomMsg.jsp" scrolling="no" allowtransparency="true" style="width:100%;" height="18" frameborder="0" id="bottomMsg.jsp"/></iframe>
		</div>	
	</td>
	<td  width="250px">		
		<div class="infoDispose">
	<a href="javascript:void(0)" 
	onClick="mdiwin('/AlertAction.do?src=menu','$text.get("alertCenter.lb.msg")')" title=$text.get("alertCenter.lb.msg.number") style="vertical-align:top">
	<font color="red"><div style="margin:5px 0px 0px 0px ;"><img id='alertIcon' src="/$globals.getStylePath()/images/home/alert_1.gif" /></div><span id="alertNumber"></span></font></a>  
	<a href="javascript:void(0)"
	 onClick="mdiwin('/AdviceAction.do?src=menu','$text.get("bottom.lb.advice")')" title=$text.get("bottom.lb.advice")>
	<font color="red"><div style="margin:5px 0px 0px 0px ;"><img id='noteIcon' src="/$globals.getStylePath()/images/home/note_1.gif" /></div><span id="adviceNumber"></span></font></a>
	<a href="javascript:void(0)" onClick="mdiwin('/MessageQueryAction.do?src=menu','$text.get("oa.mydesk.news")')" title=$text.get("oa.mydesk.news.number")>
	<font color="red"><div style="margin:5px 0px 0px 0px ;"><img id='msgIcon' src="/$globals.getStylePath()/images/home/msg_1.gif" /></div><span id="msgNumber"></span></font></a>
	 </div>
	</td>	
	</tr>	
	</table>	
	</div>
		
<iframe id="pindex" name="pindex" src="/welcomeMobile.jsp"  frameborder=false scrolling="no" class="list" width="100%" height="100%" frameborder=no ></iframe>
<div id="PopUp_organizationDIV" class="PopUp_organization" style="display:none">
</div>	
</body>
</html>