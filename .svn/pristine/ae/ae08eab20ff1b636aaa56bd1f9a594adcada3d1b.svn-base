<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link rel="stylesheet" href="/style/css/mail.css" type="text/css">
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style >
p{margin:0;padding:0;}
.tips_div{
width:320px;
height:147px;
border:1px solid #57a7ff;
background-color:#e3f0ff;
}

.tips_title{
height:28px;
line-height:28px;
vertical-align:middle;
border-top:1px solid #fff;
border-left:1px solid #fff;
border-right:1px solid #fff;
border-bottom:1px solid #57a7ff;
background-color:#acd3fe;
}

.left_title{
float:left;
padding-left:10px;
}

.tips_close{
padding-top:2px;
padding-right:2px;
float:right;
}

.tips_content{
margin-top:-20px;
margin-left:10px;
float:left;
}
.tips_operate{
width:100%;
float:left;
text-align:center;
margin-top:-20px;
margin-bottom:10px;
}
</style>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">
#if("$dir" == "next") 

putValidateItem("smtpserver","$text.get("oa.email.send")","any","",false,0,100);
putValidateItem("pop3server","$text.get("oa.email.Receiving")","any","",false,0,100);
putValidateItem("pop3username","$text.get("oa.email.POP")","any","",false,0,50 );
putValidateItem("pop3userpassword","$text.get("oa.email.mailbox")","any","",false,0,100 );
putValidateItem("smtpPort","$text.get("oa.email.smtport")","int","",false,0,50000000 );
putValidateItem("pop3Port","$text.get("oa.email.popport")","int","",false,0,50000000 );

#else
putValidateItem("mailaddress","$text.get("oa.email.address")","mail","",false,0,50);
putValidateItem("account","$text.get("oa.email.Accountname")","any","",false,0,50 );
putValidateItem("displayName","$text.get("oa.email.adopted")","any","",false,0,100 );
#end 

function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	//window.parent.frames['leftFrame'].location.reload() ;
	return true;
}	

if(typeof(top.junblockUI)!="undefined"){
	top.junblockUI();
}

function mailaddresschange(){
	//新增时，自动修改帐户名称，和邮件中采用的名称
	#if("$!result.id" == "")
		address = document.getElementById("mailaddress").value;
		if(address.indexOf("@") == -1){
			document.getElementById("account").value = address;
			document.getElementById("displayName").value = address;
		}else{
			document.getElementById("account").value = address.substr(0,address.indexOf("@"));
			document.getElementById("displayName").value = address.substr(0,address.indexOf("@"));
		}
	#end
}

function popsslclick(){
	if(document.form.popssl.checked){
		document.form.pop3Port.value="995";
	}else{
		document.form.pop3Port.value="110";
	}
}
function portDefault(){
	document.form.smtpPort.value="25";
	if(document.form.popssl.checked){
		document.form.pop3Port.value="995";
	}else{
		document.form.pop3Port.value="110";
	}
}
function closeAdvanceSet(){
	document.getElementById('advanceSet').style.display='none';
}
function closeSmtpSet(){
	document.getElementById('smtpSet').style.display='none';
}
function smtpsamepopclick(value){
	if(value == "1"){
		document.form.smtpusername.disabled = true;
		document.form.smtpuserpassword.disabled = true;
	}else{
		document.form.smtpusername.disabled = false;
		document.form.smtpuserpassword.disabled = false;
	}
}
function submitform()
{
	document.form.submit();
}

function preStep(){
	document.form.dir.value='one';
	jQuery("#form").removeAttr("target");
	//if(beforSubmit(document.form)){
		document.form.submit();
	//}
}

function retentDayChange(){
	document.getElementById("rd3").checked=true;
	document.getElementById("rd3").value=document.form.retentDay2.value;
	if(document.form.retentDay2.value == ""){
		document.getElementById("rd3").value = "-1";
	}	
}
function rcchange(type){
	if(type=="1"){
		document.getElementById("rd1").checked=true;
	}else if(type=="2"){
		document.getElementById("rd2").checked=true;
	}else{
		document.getElementById("rd3").checked=true;
	}
}

function closeDiv(){
	parent.jQuery.close('dealSetting');
}

function onSubmit(){
	if(beforSubmit(document.form)){
		form.submit();
	}
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form id="form" name="form" method="post" action="/EMailAccountAction.do" #if("$dir" == "next") target="formFrame" #end>
<input type="hidden" name="type" value="mailSet"/>
#if("$dir" == "next") 
<input type="hidden" name="dir" value="" id="stat"/>
 #else 
<input type="hidden" name="dir" value="next" id="stat"/>
 #end 
<input type="hidden" name="id" value="$!result.id" />
<input type="hidden" name="defaultUser" value="$!result.defaultUser" />
<input type="hidden" name="operation" value="$!operation"/>
<input type="hidden" name="mainAccount" value="$!mainAccount">

<div class="MailAccount" id="MailAccount">
	<div class="list_left_topleft"></div><div class="list_left_topright"></div>
	
	<!-- 高级设置 -->
	<div id="advanceSet" class="MailAccount_smtp">
		<div class="MailAccount_smtp_C_W"><a href="javascript:closeAdvanceSet();" alt="$text.get("common.lb.close")"></a></div>
		<div class="MailAccount_smtp_div" style="padding-bottom:10px;height:300px;">
			<div class="MailAccount_smtp_title">$text.get("oa.email.Senior")</div>
			<ul style="margin:5px 0 0 15px;overflow:hidden;padding:0;">
				<li>$text.get("oa.email.smtport")：<input name="smtpPort" id="smtpPort" type="text" value="$!result.smtpPort" class="text"  style="width:50px; vertical-align:middle" >
				</li>
				<li><input type="checkbox" style="vertical-align:middle;" name="smtpssl" id="smtpssl" #if("$!result.smtpssl" == "1") checked #end value="1">$text.get("oa.email.safety")</li>
				<li>$text.get("oa.email.popport")：<input name="pop3Port" id="pop3Port" type="text" value="$!result.pop3Port" class="text"  style="width:50px;" >
				</li>
				<li><input type="checkbox" style=" vertical-align:middle" name="popssl" onClick="popsslclick()" id="popssl" #if("$!result.popssl" == "1") checked #end value="1">$text.get("oa.email.safety")</li>
				<ol style="margin:0;padding:0;text-align:left;height:auto;">
					<p>
						<input type="radio" style=" vertical-align:middle; border:0px; width:auto; background:none" name=retentDay id="rd1" value="-1" #if("$!result.retentDay"=="-1") checked #end ><a onClick="rcchange('1')">$text.get("oa.mail.copiesover")</a>
					</p>
					<p>
						<input type="radio" style=" vertical-align:middle; border:0px; width:auto; background:none" name=retentDay id="rd2"  value="0" #if("$!result.retentDay"=="0") checked #end ><a onClick="rcchange('2')">$text.get("oa.mail.copiesno")</a> <br/>
					</p>
					<p>
						<input type="radio" style=" vertical-align:middle; border:0px; width:auto; background:none" name="retentDay" id="rd3"  value="$!result.retentDay" #if("$!result.retentDay"!="0" && "$!result.retentDay"!="-1") checked #end ><a onClick="rcchange('3')">$text.get("oa.copies.copies")</a> 
					</p>
					<p>
						<input name="retentDay2" style=" vertical-align:middle" id="retentDay2" type="text" #if($!result.retentDay > 0) value="$!result.retentDay" #end class="text"  onchange="retentDayChange()"  style="width:30px;">$text.get("oa.later.delete")
					</p>
				</ol>
			</ul>
			<div class="MailAccount_smtp_button"><input type="button" onClick="closeAdvanceSet()" class="toolbu_02"  value="$text.get("mrp.lb.saveAdd")" style="border:none;cursor:pointer;color: #02428a;" /><button style="border:0px; padding:0px; margin-left:5px;" type="button" onClick="portDefault()" class="toolbu_02">$text.get("com.define.defaultField")</button></div>
		</div>
	</div>
	<!-- 设置 -->
	<div id="smtpSet" class="MailAccount_smtp">
		<div class="MailAccount_smtp_C_W"><a href="javascript:closeSmtpSet();" alt="$text.get("common.lb.close")"></a></div>
		<div class="MailAccount_smtp_div">
			<div class="MailAccount_smtp_title">$text.get("oa.email.validation")</div>
			<ul style="margin:0 0 0 15px;overflow:hidden;padding:0;">
				<li>$text.get("oa.validation.y")</li>
				<li><input type="radio"  style="vertical-align:middle" name="smtpsamepop" onClick="smtpsamepopclick('1')" #if("$!result.smtpsamepop" == "1") checked #end id="smtpsamepop" value="1">$text.get("oa.validation.sameinformation")</li>
				<li><input type="radio" style="vertical-align:middle" name="smtpsamepop" onClick="smtpsamepopclick('2')" #if("$!result.smtpsamepop" != "1") checked #end id="smtpsamepop" value="2">$text.get("oa.email.information")</li>
				<ol><span>$text.get("oa.email.emailaccount")</span>
					<input name="smtpusername" id="smtpusername" type="text" value="$!result.smtpusername" #if("$!result.smtpsamepop" == "1") disabled #end class="text"></ol>
				<ol><span>$text.get("userManager.lb.password")：</span>
					<input name="smtpuserpassword" id="smtpuserpassword" type="password" value="$!result.smtpuserpassword" #if("$!result.smtpsamepop" == "1") disabled #end class="text" ></ol>
			</ul>
			<div class="MailAccount_smtp_button">
				<input type="button" onClick="closeSmtpSet()" class="toolbu_02" value="$text.get("mrp.lb.saveAdd")" style="border:none;color: #02428a;" />
			</div>
		</div>
	</div>
	
	<div class="MailAccount_content">
		<ul class="MailAccount_form" style="display:#if("$dir" == "next") block #else none #end ;" >
			<!--下一页  -->
			<li>
				<span>$text.get("oa.email.SendSMT")</span>
				<input name="smtpserver" id="smtpserver" type="text" value="$!result.smtpserver">
			</li>
			<li style="margin-bottom:5px"><span></span>
				<div style="float:left; margin-top:6px"><input type="checkbox" style="width:auto; border:none; background:none;" name="smtpAuth" id="smtpAuth" #if("$!result.smtpAuth" == "1") checked #end  value="1">$text.get("oa.email.authentication")&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
				<button type="button" class="toolbu_01" onClick=" document.getElementById('smtpSet').style.display='block';" name="setsmtp">$text.get("common.msg.set")</button>
			</li>
			<li>
				<span>$text.get("oa.email.Receiving")：</span>
				<input name="pop3server" id="pop3server" type="text" value="$!result.pop3server">
			</li>
			<li>
				<span>$text.get("oa.email.POP")：</span>
				<input name="pop3username" id="pop3username" type="text" value="$!result.pop3username" >
			</li>
			<li>
				<span>$text.get("userManager.lb.password")：</span>
				<input name="pop3userpassword" id="pop3userpassword" type="password" value="$!result.pop3userpassword" >
			</li>
			<li><span></span>
				<button type="button" class="toolbu_02" onClick=" document.getElementById('advanceSet').style.display='block';" name="setsmtp">$text.get("oa.email.Senior")</button>
			</li>
			<li style="margin-top:25px"><span></span>
			<button type="button" class="toolbu_01" name="Submit" onclick="onSubmit();">$text.get("common.lb.save")</button>
			<button type="button" class="toolbu_02" onClick="preStep();" name="button">$text.get("mrp.lb.preStep")</button>
			<button type="button" class="toolbu_01" onClick="closeDiv()">关闭</button>
			</li>
		</ul>	
		<ul class="MailAccount_form" style="display:#if("$dir" == "next") none #else block #end ;" >
			<!--  第一页-->
			<li>
				<span>$text.get("oa.email.address")：</span>
				<input name="mailaddress" id="mailaddress" type="text" value="$!result.mailaddress" onkeydown="mailaddresschange()" onchange="mailaddresschange()">
			</li>
			<li>
				<em style="padding-left:140px;color:#999;">$text.get("oa.email.systemshows")</em>
			</li>
			<li>
				<span>$text.get("oa.email.Accountname")：</span>
				<input name="account" id="account" type="text" value="$!result.account">
			</li>
			<li>
				<em style="padding-left:140px;color:#999;">$text.get("oa.email.Emailadopted")</em>
			</li>
			<li>
				<span>$text.get("oa.email.adopted")：</span>
				<input name="displayName" id="displayName" type="text" value="$!result.displayName">
			</li>
			<li>
				<span>$text.get("oa.email.every")：</span>
				<input name="autoReceive" id="autoReceive" type="text" value="$!result.autoReceive">
			</li>	
			<li>
				<em style="padding-left:140px;color:#999;">$text.get("oa.email.Minutes")</em>
			</li>
			<li>
				<span></span>
				<input name="autoAssign" id="autoAssign" style="border:none; width:auto; background:none;" type="checkbox" value="1" #if("$!result.autoAssign" == "1") checked #end >
				$text.get("mail.lb.autoAssign")
			</li>	
			<li>
				<p style="display:inline-block;margin-left:130px;">
					<button type="button" class="toolbu_02" name="Submit" onclick="onSubmit();">$text.get("mrp.lb.nextStep")</button>
					<button type="button" class="toolbu_01" onClick="closeDiv()">关闭</button>
				</p>
			</li>
			<div class="clear"></div>
		</ul>
	</div>
	<div class="list_left_bottomleft"></div><div class="list_left_bottomright"></div>
</div>
</form>

</body>
</html>
