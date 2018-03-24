<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.common.department")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript">
	function openWorkInfo(userId,strClass){
		var urlstr = "" ;
		if(strClass=="dept"){
			urlstr = "/FrameworkAction.do?type=dept&deptId="+userId ;
		}else{
			urlstr = "/FrameworkAction.do?type=work&userId="+userId;
		}
		var str  = window.open(urlstr+"&MOID=$MOID&MOOP=add"); 
	}
	function selectDept(obj,deptName){
		items = document.getElementsByTagName("INPUT");
		for(var i=0;i<items.length;i++){		
			if(items[i].type =="checkbox" && items[i].getAttribute("mcode") == deptName ){
				items[i].checked = obj.checked;
			}		
		}
	}
	function selectAll(obj){
		var deptNames = document.getElementsByName("keyId") ;
		for(var i=0;i<deptNames.length;i++){
			deptNames[i].checked=obj.checked ;
		}
	}
	function sendMsg(type){
		if("employee"==type){
			mdiwin("/UserFunctionQueryAction.do?tableName=tblEmployee&src=menu","$text.get('com.employee.management')");
			return;
		}
		var keyIds = document.getElementsByName("keyId") ;
		var number = 0 ;
		var userIds = "" ;
		for(var i=0;i<keyIds.length;i++){	
			if(keyIds[i].checked){
				number++ ;
				userIds += keyIds[i].value+","
			}
		}
		if(userIds.length>1){
			userIds = userIds.substring(0,userIds.length -1);
		}
		if(number==0){
			alert("$text.get('oa.msgSend.selectreceive')")
			return false ;
		} ;
		if(type=="msg"){
			#foreach($dept in $deparList)
			#foreach($user in $allEmp)
				#if($user.DepartmentCode==$dept.classCode)
					#if($user.openFlag!=1)
						userIds=userIds.replace("$user.id",'');
					#end
				#end
			#end			
  	 		#end
  	 		if(userIds.length==0){
  	 			alert("请选择系统用户，非系统用户不能发送邮件和消息") ;
  	 			return ;
  	 		}
			mdiwin("/MessageAction.do?noback=true&operation=70&empId="+userIds,"$text.get('oa.mydesk.news')") ;
		}else if("mail"==type){
			#foreach($dept in $deparList)
			#foreach($user in $allEmp)
				#if($user.DepartmentCode==$dept.classCode)
					#if($user.openFlag!=1)
						userIds=userIds.replace("$user.id",'');
					#end
				#end
			#end			
  	 		#end
  	 		if(userIds.length==0){
  	 			alert("请选择系统用户，非系统用户不能发送邮件和消息") ;
  	 			return ;
  	 		}
			mdiwin("/EMailAction.do?noback=true&operation=6&type=main&emailType=inner&empId="+userIds,"$text.get('oa.common.sendEmail')") ;
		}else if("tel"==type){
			if(number > 1) {
				alert("$text.get('common.msg.telCall')");
			}else{
				window.showModalDialog("/TelAction.do?operator=callTel&from=tblEmployee&keyId="+userIds,"","dialogWidth=600px;dialogHeight=350px");	
			}
		}else{
			mdiwin("/NoteAction.do?noback=true&operator=sendSMS&empId="+userIds,"$text.get('com.sms.send')") ;
		}
	}
</script>
</head>

<body>
	<div class="oamainhead">
	<div class="HeadingButton">
	<div style="float:left; color:#666; padding-top:5px; margin-left:4px;"><input type="checkbox" onClick="selectAll(this)"/>$text.get('common.lb.selectAll')</div>
<ul style="float:right">

#if($LoginBean.operationMap.get("/TelAction.do?operator=callTel").query())
		<li>
			<button onClick="sendMsg('tel');">$text.get("common.lb.telCall")</button>
		</li>
#end		
		<li>
			<button onClick="sendMsg('mail');">$text.get("oa.common.sendEmail")</button>
		</li>
		<!-- 
		<li>
			<button onClick="sendMsg('msg');">$text.get("oa.mydesk.news")</button>
		</li>
		 -->
		<li>
			<button onClick="sendMsg('note');">$text.get("com.sms.send")</button>
		</li>
		<li>
			<button onClick="sendMsg('employee');">$text.get("com.employee.management")</button>
		</li>
		</ul>
	</div>
</div>
<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-32;
oDiv.style.height=sHeight+"px";
</script>
	#set($num=1)
	#foreach($dept in $deparList)
	<div class="listRange_1_space_1" style="text-align:left;font-weight:normal">
		<input type="checkbox" style="vertical-align:middle" onClick="selectDept(this,'dept$num')"><a href="javascript:openWorkInfo('$!dept.id','dept')">$!dept.deptFullName</a>
		#foreach($deptNum in $deptNums)
			#if($!dept.id==$!deptNum.name)
				<span>($!deptNum.value $text.get("oa.inquisition.men"))</span>
			#end
		#end
	</div>
	<div class="UserAdmin_scroll" style="float:none;">
		<ul class="li_u" style="padding:0;float:none;overflow:hidden;">
		#foreach($user in $allEmp)
			#if($user.DepartmentCode==$dept.classCode)
			<li>
				
				<input name="keyId" mcode="dept$num" type="checkbox" value="$user.id"/>
				
				<a href="javascript:void(0)" onClick="openWorkInfo('$user.id','dept$num')">$!user.empFullName</a>
			</li>	
			#end
		#end
		</ul>			
    </div>
    
    #set($num=$num+1)
    #end
</div>
</body>
</html>
