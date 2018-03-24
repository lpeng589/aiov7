<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.mail.mailDirectory")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBox.css" type="text/css"/>
<link rel="stylesheet" href="/style/css/mail.css" type="text/css"/>
<link type="text/css" rel="stylesheet" href="/style/css/email.css" />
<script type="text/javascript" src="/js/function.js"></script>
<link rel="stylesheet" href="/js/tree/jquery.treeview.css" /> 
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/js/jquery.cookie.js" type="text/javascript"></script>
<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
<script language="javascript" src="/js/aioselect.js"></script>
<style type="text/css"> 
#progress {background: white; height: 20px; padding: 2px; border: 1px solid green; margin: 2px;} 
#progress span {background: green; height: 16px; text-align: center; padding: 1px; margin: 1px; display: block; color: yellow; font-weight: bold; font-size: 14px; width: 0%;} 
.CRselectBox a.CRselectValue {margin:1px 1px 2px 13px;}
ul,li{margin:0;padding:0;list-style:none;ss}
.bg-png{background-image:url(/style/images/bg.jpg);background-repeat:no-repeat;}
.write-mail-ul{height:46px;position:relative;}
.write-mail-ul>li{width:88px;height:32px;position:relative;float:left;display:inline-block;text-align:center;cursor:pointer;}
.write-mail-ul>li.r-li{width:89px;margin-left:-1px;background-position:-87px 0;}
.write-mail-ul>li>span{color:#937959;display:inline-block;text-shadow:#f8f2ea 0 1px 0;font-size:15px;font-weight:bold;line-height:33px;padding-left:20px;position:relative;}
.write-mail-ul>li>span>b{width:16px;height:32px;display:inline-block;position:absolute;left:0;top:8px;}
.write-mail-ul>li .triangle{right:3px;top:15px;}
.write-mail-ul>li:hover .triangle{-webkit-transform:rotate(180deg);transform:rotate(180deg);}
.b-s{background-position:0 -192px;}
.b-w{background-position:-64px -192px;}
.a_sty_visited {color: black;}
.a_sty_link {color:red;}
</style>

<script>
var shu=1;
var curaccount="inner";
#if("$accountSelect"!="")
curaccount = '$!accountSelect';
#end

var curIsReceier=false;
function goFarme(url,account){
	if(account){
		curaccount = account;
	}
	if(!curIsReceier){
		//接收界面不能跳转
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: "<div style='width:135px;'><img src='/style/images/load.gif'/></div>",css:{ background: 'transparent'}}); 
		}
		window.parent.mainFrame.location=url;
	}
}

function receiver(){
	re = document.getElementById("receiverMail");
	if(re.value == "-1"){
		return;
	}
	if(re.value == "inner"){
		showMailFrame('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=inner&type=main&groupId=1');
	} else {		
		showMailFrame('EMailAction.do?operation=$!globals.getOP("OP_QUERY")&type=receive&emailType='+re.value+"&oldEmailType="+curaccount);
		curIsReceier=true;
	}
	re.value="-1";
	document.getElementById("receiverMail_CRDis").innerHTML = '$text.get("oa.mail.inbox")';	
}

function writer(){
	window.parent.mainFrame.location = 'EMailAction.do?operation=$!globals.getOP("OP_ADD_PREPARE")&type=main&emailType='+curaccount+'&groupId='+shu;
}

function showMailFrame(url){
	window.parent.mainFrame.location = url;
}
	
function refresh(){ 
#if("$!accountSelect" == "" || "$!accountSelect"=="inner")	
	window.parent.frames['mainFrame'].document.location.href="/EMailQueryAction.do?operation=4&emailType=inner&type=main&groupId=1";
#else
	#foreach($row in $outteremail)
		#if("$!accountSelect"=="$row.id")
			window.parent.frames['mainFrame'].document.location.href="/EMailQueryAction.do?operation=4&emailType=$row.id&type=main&groupId=1";
		#end
	#end
#end		
//window.parent.frames['mainFrame'].document.location.reload();
}	

	
function clickType(){
	refresh();
	var type=document.getElementById("emailType").value;
    if(window.parent.frames['mainFrame'].document.getElementById("marker")!=null)
	{
	window.parent.frames['mainFrame'].document.getElementById("emailType").value = type ;
	window.parent.frames['mainFrame'].document.forms['form'].submit();
	}
}

$(document).ready(function(){
	var curLi ;	
	// first example
	$("#navigation").treeview({
	#if("$!accountSelect" == "" && $outteremail.size() > 0)
		persist: "cookie",
	#end	
		cookieId: "aioemail" ,
		//animated: true,
		collapsed: false,
		unique: false
	});
	
	$("#navigation").find('span').click(function (){
		if(curLi){
			curLi.removeClass("selNode");
		}
		$(this).addClass("selNode");
		curLi = $(this);
	});
	
	$(".d-menu-li").hover(
		function(){$(this).find(".in-nav-ul").show();},
		function(){$(this).find(".in-nav-ul").hide();}
	);
	
});



function refreshStat(){
	#set($stat = "")
	#if("$!mailStyle" == "Outlook")	
		#set($stat = "all")
	#else
		#foreach($rw in $outteremail)
			#set($stat = "$stat$rw.id:")
		#end	
	#end
	
	AjaxRequest('/EMailAction.do?operation=$globals.getOP("OP_QUERY")&type=stat&statId=$stat');
    var value = response;
	if(value!="no response text"&&value!="close"){	
		//删除
#if("$!accountSelect" == "" || "$!accountSelect"=="inner")		
		obj = document.getElementById("_1_top");	 
		obj.innerHTML = "";	
		obj = document.getElementById("_1");	 
		obj.innerHTML = "";	
		obj = document.getElementById("_2");	 
		obj.innerHTML = "";	
		obj = document.getElementById("_3");	 
		obj.innerHTML = "";	
		obj = document.getElementById("_4");	 
		obj.innerHTML = "";	
		obj = document.getElementById("_5");	 
		obj.innerHTML = "";	
		#foreach($group in $emailgroup)
			#if("$!globals.get($group,2)" == "")
			obj = document.getElementById("_$!globals.get($group,0)");	 
			obj.innerHTML = "";	
			#end
		#end	
#end		

#if("$!mailStyle" == "Outlook")	
		obj = document.getElementById("all_1_top");	 
		obj.innerHTML = "";	
		obj = document.getElementById("all_1");	 
		obj.innerHTML = "";	
		obj = document.getElementById("all_2");	 
		obj.innerHTML = "";	
		obj = document.getElementById("all_3");	 
		obj.innerHTML = "";	
		obj = document.getElementById("all_4");	 
		obj.innerHTML = "";	
		obj = document.getElementById("all_5");	 
		obj.innerHTML = "";	
		
		#foreach($group in $emailgroup)									
			obj = document.getElementById("all_$!globals.get($group,0)");	 
			obj.innerHTML = "";	
		#end
#else				
#foreach($row in $outteremail)
	#if("$!accountSelect" == "" || "$!accountSelect"=="$row.id")	
		obj = document.getElementById("${row.id}_1_top");	 
		obj.innerHTML = "";	
		obj = document.getElementById("${row.id}_1");	 
		obj.innerHTML = "";	
		obj = document.getElementById("${row.id}_2");	 
		obj.innerHTML = "";	
		obj = document.getElementById("${row.id}_3");	 
		obj.innerHTML = "";	
		obj = document.getElementById("${row.id}_4");	 
		obj.innerHTML = "";	
		obj = document.getElementById("${row.id}_5");	 
		obj.innerHTML = "";	
				#foreach($group in $emailgroup)
					#if("$!globals.get($group,2)" == "$row.id")
									
		obj = document.getElementById("${row.id}_$!globals.get($group,0)");	 
		obj.innerHTML = "";	
				
					#end
				#end	
	#end	
#end	
#end			
		
		//alert(value);
		if(value.length>0){ 
			gs = value.split(";");
			for(i=0;i<gs.length;i++){		
				vs = gs[i].split(":");
				if(vs[0]=="") continue;
				obj = document.getElementById(vs[0]);
				if(obj != null && typeof(obj) != "undefined"){
					obj.innerHTML = "("+vs[1]+")";
					if(vs[0].substring(vs[0].length-2)=="_1"){
						obj = document.getElementById(vs[0]+"_top");
						obj.innerHTML = "("+vs[1]+")";
					}
				}
			}
		}
	}
}
function showreModule(title,href)
{		
	top.showreModule(title,href);	
}
    
window.parent.frames['mainFrame'].document.referrer;

function changeAccount(){
	value = document.getElementById("accountSelect").value;
	window.location.href="/EMailAction.do?operation=4&type=left&accountSelect="+value;
}
function styleClick(){
	value = "";
	#if("$!mailStyle" == "Outlook") value="Foxmail";	 #else value="Outlook"; #end
	window.location.href="/EMailAction.do?operation=4&type=left&mailStyle="+value;
}

function toChangeColor(obj,preClass){
	 
	shu=(obj.href.match(/&groupId=\d/)+"").match(/\d/);
	if(shu == null){
		shu = 1;
	}
	$(".navigation_bottom a").each(function(i){
		  	$(this).css("color","#484A4C");
	});
	$("#navigation a").each(function(i){
	  	$(this).parent().find("a").css("color","#484A4C");
 	});
	if (preClass){
	 	$(obj).css("color","red");
	} else {
	 	$(obj).parent().find("a").css("color","red");
	}
}
function selectChange(methodName,fieldName,fieldValue){
	$("#"+fieldName).val(fieldValue);
	setTimeout(methodName,100);
}

</script>

</head>
<body>
<div id="" class="left_list" style="margin-left: 5px;margin-top: 7px;" >
	<ul class="write-mail-ul">
		<li class="d-menu-li" title="收 信">
			<input type="hidden" value="-1" name="receiverMail" id="receiverMail" />
			<span><b class="bg-png b-s"></b>收 信</span>
			<b class="triangle"></b>
			<ul class="in-nav-ul frame-left">
				<li onclick="selectChange('receiver()','receiverMail','all')" title='$text.get("email.lb.allaccount")'>$text.get("email.lb.allaccount")</li>
				<li onclick="selectChange('receiver()','receiverMail','inner')" title='$text.get("oa.common.innerMail")'>$text.get("oa.common.innerMail")</li>
				#foreach($row in $outteremail)
					<li onclick="selectChange('receiver()','receiverMail','${row.id}')"  title='$row.account&lt;$row.mailaddress&gt;'>$row.account&lt;$row.mailaddress&gt;</li>
				#end
			</ul>
		</li>
		<li class="r-li" title="写 信" onclick="writer();">
			<span><b class="bg-png b-w"></b>写 信</span>
		</li>
	</ul>
	
	<div class="left_list_menu" id="nn" style="float:left;width:180px;height:380px; overflow:hidden; overflow-y:auto;">
		<div style="margin:15px 0px 15px 15px;">$text.get("email.lb.mailCap")：$!globals.get($mailSize,1) /$!globals.get($mailSize,0) M</div>
		 <script type="text/javascript">
		var oDiv=document.getElementById("nn");
		var sHeight=document.documentElement.clientHeight-55;
		oDiv.style.height=sHeight+"px";
	</script>
		#if("$!mailStyle" != "Outlook")			
		<select id="accountSelect"  style="width:120px;margin:0px 0px 5px 15px;" onChange="changeAccount()">
		<option value="">$text.get("email.lb.viewallacc")</option>
		<option value="inner" #if("$!accountSelect" =="inner") selected #end>$text.get("email.lb.innermail")</option>
		#foreach($row in $outteremail)
		<option value="$row.id" #if("$!accountSelect" =="$row.id") selected #end>$row.account</option>
		#end
		</select>	
		#end
		<div  id="innerEmail"  style="margin:0px 0px 0px 13px;">
			<ul id="navigation">
			<!-- 内部邮件箱 -->
			#if("$!accountSelect" == "" || "$!accountSelect"=="inner")
				<li class="navigation">
					<span ><a href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=inner&type=main&groupId=1&clearSerchForm=yes','inner')" ><img src="/style/images/mail//inmail.gif" class="vimg"/> $text.get("oa.common.innerMail")<font style="color:#ff0000" id="_1_top"></font></a></span>
						<ul>
							<li><span>
							 <a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=inner&type=main&groupId=1&clearSerchForm=yes','inner')" ><img src="/style/images/mail//mail_inbox.gif" class="vimg"/> $text.get("oa.mail.receive.box")<font style="color:#ff0000" id="_1"></font></a></span>
							</li>
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=inner&type=main&groupId=2&clearSerchForm=yes','inner')" ><img src="/style/images/mail//mail_draft.gif" class="vimg"/> $text.get("oa.mail.draft")$text.get("oa.mail.box")<font style="color:#ff0000" id="_2"></font></a></span>
							</li>		
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=inner&type=main&groupId=3&clearSerchForm=yes','inner')" > <img src="/style/images/mail//mail_send.gif" class="vimg"/> $text.get("oa.mail.outBox")$text.get("oa.mail.box")<font style="color:#ff0000" id="_3"></font></a></span>
							</li>	
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=inner&type=main&groupId=4&clearSerchForm=yes','inner')" > <img src="/style/images/mail//mail_notread.gif" class="vimg"/> $text.get("oa.mail.dust")<font style="color:#ff0000" id="_4"></font></a></span>
							</li>
							<li><span>
							<a onclick="toChangeColor(this)"  href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=inner&type=main&groupId=5&clearSerchForm=yes','inner')" ><img src="/style/images/mail//mail_delete.gif" class="vimg"/> $text.get("oa.mail.deleted")<font style="color:#ff0000" id="_5"></font></a></span>
							</li>
							#foreach($group in $emailgroup)
								#if("$!globals.get($group,2)" == "")
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=inner&type=main&groupId=$!globals.get($group,0)','inner')" ><img src="/style/images/mail//mail_userdefined.gif" class="vimg"/> $!globals.get($group,1)<font style="color:#ff0000" id="_$!globals.get($group,0)"></font></a></span>
							</li>	
								#end
							#end
							</ul>
					</li>					
			#end	
			#if("$!mailStyle" == "Outlook")	
					<li class="navigation">
						<span><a  onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=all&type=main&groupId=1&clearSerchForm=yes','all')" ><img src="/style/images/mail//webmail.gif" class="vimg"/> $text.get("email.lb.outmail")</a><font style="color:#CC0033" id="all_1_top"></font></span>
						<ul>
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=all&type=main&groupId=1&clearSerchForm=yes','all')" ><img src="/style/images/mail//mail_inbox.gif" class="vimg"/> $text.get("oa.mail.receive.box")<font style="color:#CC0033" id="all_1"></font></a></span>
							</li>
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=all&type=main&groupId=2&clearSerchForm=yes','all')" ><img src="/style/images/mail//mail_draft.gif" class="vimg"/> $text.get("oa.mail.draft")$text.get("oa.mail.box")<font style="color:#CC0033" id="all_2"></font></a></span>
							</li>		
							<li><span>
							<a  onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=all&type=main&groupId=3&clearSerchForm=yes','all')" ><img src="/style/images/mail//mail_send.gif" class="vimg"/> $text.get("oa.mail.outBox")$text.get("oa.mail.box")<font style="color:#CC0033" id="all_3"></font></a></span>
							</li>	
							<li><span>
							<a  onclick="toChangeColor(this)"  href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=all&type=main&groupId=4&clearSerchForm=yes','all')" ><img src="/style/images/mail//mail_notread.gif" class="vimg"/> $text.get("oa.mail.dust")<font style="color:#CC0033" id="all_4"></font></a></span>
							</li>	
							<li><span>
							<a onclick="toChangeColor(this)"  href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=all&type=main&groupId=5&clearSerchForm=yes','all')" ><img src="/style/images/mail//mail_delete.gif" class="vimg"/> $text.get("oa.mail.deleted")<font style="color:#CC0033" id="all_5"></font></a></span>
							</li>	
						</ul>
					</li>
					#if($emailgroup.size() > 0)
					<li class="navigation"><span><a href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=all&type=main&groupId=1&clearSerchForm=yes','all')" ><img src="/style/images/mail//webmail.gif" class="vimg"/> $text.get("email.lb.archer")</a><font style="color:#CC0033" id="all_1_top"></font></span>
						<ul>				
							#foreach($group in $emailgroup)
							<li><span>
							<a onclick="toChangeColor(this)"  href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$!globals.get($group,2)&type=main&groupId=$!globals.get($group,0)','$!globals.get($group,2)')" ><img src="/style/images/mail//mail_userdefined.gif"/> $!globals.get($group,1)<font style="color:#CC0033" id="all_$!globals.get($group,0)"></font></a></span>
							</li>	
							#end				
						</ul>
					</li>
					#end
			#else
				<!-- 外部邮件箱 -->
				#foreach($row in $outteremail)  
					#if("$!accountSelect" == "" || "$!accountSelect"=="$row.id")
					<li class="navigation"><span><a href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$row.id&type=main&groupId=1&clearSerchForm=yes','$row.id')" ><img src="/style/images/mail/inmail.gif" class="vimg"/> $row.account</a><font style="color:#CC0033" id="${row.id}_1_top"></font></span>
						<ul>
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$row.id&type=main&groupId=1&clearSerchForm=yes','$row.id')" ><img src="/style/images/mail/mail_inbox.gif" class="vimg"/> $text.get("oa.mail.receive.box")<font style="color:#CC0033" id="${row.id}_1"></font></a></span>
							</li>
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$row.id&type=main&groupId=2&clearSerchForm=yes','$row.id')" ><img src="/style/images/mail/mail_draft.gif" class="vimg"/> $text.get("oa.mail.draft")$text.get("oa.mail.box")<font style="color:#CC0033" id="${row.id}_2"></font></a></span>
							</li>		
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$row.id&type=main&groupId=3&clearSerchForm=yes','$row.id')" ><img src="/style/images/mail//mail_send.gif" class="vimg"/> $text.get("oa.mail.outBox")$text.get("oa.mail.box")<font style="color:#CC0033" id="${row.id}_3"></font></a></span>
							</li>	
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$row.id&type=main&groupId=4&clearSerchForm=yes','$row.id')" ><img src="/style/images/mail/mail_notread.gif" class="vimg"/> $text.get("oa.mail.dust")<font style="color:#CC0033" id="${row.id}_4"></font></a></span>
							</li>	
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$row.id&type=main&groupId=5&clearSerchForm=yes','$row.id')" ><img src="/style/images/mail/mail_delete.gif" class="vimg"/> $text.get("oa.mail.deleted")<font style="color:#CC0033" id="${row.id}_5"></font></a></span>
							</li>
							#foreach($group in $emailgroup)
								#if("$!globals.get($group,2)" == "$row.id")
							<li><span>
							<a onclick="toChangeColor(this)" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$row.id&type=main&groupId=$!globals.get($group,0)','$row.id')" ><img src="/style/images/mail/mail_userdefined.gif"/> $!globals.get($group,1)<font style="color:#CC0033" id="${row.id}_$!globals.get($group,0)"></font></a></span>
							</li>	
								#end
							#end				
						</ul>
					</li>	
					#end	
				#end
			#end
					<li class="navigation"><span ><a onclick="toChangeColor(this,'navigation_bottom')" href="javascript:goFarme('/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=collection&type=main&groupId=1&clearSerchForm=yes','alert','alert')" ><img src="/style/images/plan/tuopiao_yes.gif" class="vimg"/> $text.get("oa.mail.mailCollection")<font style="color:#ff0000" id="assign_1_top"></font></a></span>
					</li>	
				</ul><!--说明：请将这里的树形菜单，改成使终只展开一级，这样就不需要出现滚动条了。-->
		</div>	
		
		<div class="navigation_bottom" style="margin-left: 20px;">
			<li>
				<a onclick="toChangeColor(this,'navigation_bottom')" href="javascript:refresh()" >
					$text.get("oa.mail.Directoryrefur")
				</a>
			</li>
			<li>		
				<a onclick="toChangeColor(this,'navigation_bottom')" href="javascript:showMailFrame('/EMailAction.do?operation=$!globals.getOP("OP_QUERY")&type=groupmanager')" >
					$text.get("oa.mail.mailDirectory")
				</a>
			</li>
			#if($LoginBean.operationMap.get("/EMailAccountQueryAction.do").query() || $LoginBean.id=="1")
			<li>
				<a onclick="toChangeColor(this,'navigation_bottom')" href="javascript:showMailFrame('/EMailAccountQueryAction.do')" >		
					$text.get("oa.mail.mailAccountSetting")
				</a>
			</li>
			#end
			#if($LoginBean.operationMap.get("/CRMClientAction.do").query() || $LoginBean.id=="1")
				<li>  
					<a onclick="toChangeColor(this,'navigation_bottom')" href="javascript:showreModule('$text.get("crm.client.list")','/CRMClientAction.do?src=menu')" >		
					  $text.get("crm.client.list")
					</a>
				</li>
			#end
		</div>
	</div>
</div>
</body>
</html>
