<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<link type="text/css" href="/style/css/communicationNote.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>

<script type="text/javascript">
	var zTree1;
	var setting;
	var zNodes = $!deptData;
	setting = {
		expandSpeed : false,
		checkType : {"Y":"s", "N":"ps"}, 
		showLine: true,
		callback: {
				click: zTreeOnClick,
		}
	};
	
	$(document).ready(function(){
		reloadTree();
		$(".div_all").mouseover(function(){
  			$(".div_all").css("text-decoration","underline");
		});
		$(".div_all").mouseout(function(){
  			$(".div_all").css("text-decoration","none");
		});
	});
	
	function reloadTree(node) {
		var setting1 = clone(setting);
		setting1.treeNodeKey = "id";
		setting1.treeNodeParentKey = "pId";
		setting1.isSimpleData = true;
		zNodes1 = clone(zNodes);
		setting1.showLine = true;
		setting1.showIcon = false;
		zTree1 = $("#treeDemo").zTree(setting1, zNodes1);
		var nodes = zTree1.getNodes();
		if(nodes.length > 0){
			#if("$!CommunicationNoteSearchForm.searchType"=="group")
				var zTreeLen = zTree1.transformToArray(nodes);
				for(var i=0; i<zTreeLen.length; i++) {
					if (zTreeLen[i].id == "$!CommunicationNoteSearchForm.searchValue") {
						zTree1.selectNode(zTreeLen[i]);
						zTree1.expandNode(zTreeLen[i], true,null);
					}
				}
			#elseif("$!CommunicationNoteSearchForm.searchType"=="keyWord")
				zTree1.expandNode(nodes[0], true,null);
			#elseif("$!CommunicationNoteSearchForm.searchType"=="letter")
				$("#$!{CommunicationNoteSearchForm.searchValue}"+"_edh").addClass("letter_a");
				zTree1.expandNode(nodes[0], true,null);
			#else
				zTree1.selectNode(nodes[0]);
				zTree1.expandNode(nodes[0], true,null);
			#end
		}
	}
	
	/* 点击树事件 */
	function zTreeOnClick(event, treeId, treeNode) {
		checkeds(event,treeNode);
		empData('group',treeNode.id);
	}
	
	function checkeds(event,treeNode) {
		var setting1 = clone(setting);
		var srcNode = zTree1.getSelectedNode();
		if (treeNode.open) {
			if (srcNode) {
				zTree1.expandNode(srcNode, false,null);
			}
		} else {
			if (srcNode) {
				zTree1.expandNode(srcNode, true,null);
			}
		}
	}
	
	/* 右边列表连接 */	
	function empData(type,value){
		searchField('',type,value);
		form.submit();
	}
	
	function searchField(empStatus,searchType,searchValue){
		$("#empStatus").val(empStatus);
		$("#searchType").val(searchType);
		$("#searchValue").val(searchValue);
	}
	
	function noCheck(){
		zTree1.cancelSelectedNode(false);
	}
	
	/* 在职，离职 */
	function showEmp(type){
		if(type == 'leave'){
			$("#leave_li").addClass("sel");
			$("#in_li").removeClass("sel");
			$("#empStatus").val('leave');
		}else{
			$("#in_li").addClass("sel");
			$("#leave_li").removeClass("sel");
			$("#empStatus").val('');
		}
		form.submit();
	}
	
	/* 关键字搜索 */
	function keywords(){
		searchField('','keyWord',$("#keyword").val().trim());
		form.submit();
	}
	
	/* 字母查询 */
	function edh(value){
		searchField('$!CommunicationNoteSearchForm.empStatus','letter',value);
		form.submit();
	}
	
	function submitQuery(){
		form.submit();
	}
	
	function pageSubmit(pageNo){
		$("#pageNo").val(pageNo);
		form.submit();
	}
	
	/* 发送邮件 */
	function sendEmail(id){
		jQuery.ajax({
		   type: "POST",
		   url: "/CommunicationNoteAction.do?operation=4&opType=queryContactInfo&fieldName=Email&id="+id,
		   success: function(msg){
		   		if(msg == "no"){
		   			alert("请填写职员邮箱地址后，再进行此操作");
		   		}else{
		   			sendMsg("email",id) ;
		   		}
		   }
		});
	}
	
	/* 发送短信 */
	function sendMS(id){
		jQuery.ajax({
		   type: "POST",
		   url: "/CommunicationNoteAction.do?operation=4&opType=queryContactInfo&fieldName=Mobile&id="+id,
		   success: function(msg){
		   		if(msg == "no"){
		   			alert("请填写联系人手机信息后，再进行此操作");
		   		}else{
		   			sendMsg("sms",id,top) ;
		   		}
		   }
		});
	}
	
	/* 发生私信（KK） */
	function sendMessage(empId,empName){
		if(typeof(top.msgCommunicate) != "undefined"){
			top.msgCommunicate(empId,empName);
		}else if(typeof(window.opener.top.msgCommunicate) != "undefined"){ 
			window.opener.top.msgCommunicate(empId,empName);
		}else if(typeof(window.opener.window.opener.top.msgCommunicate) != "undefined"){ 
			window.opener.window.opener.top.msgCommunicate(empId,empName);
		}else if(typeof(window.opener.window.opener.window.opener.top.msgCommunicate) != "undefined"){ 
			window.opener.window.opener.window.opener.top.msgCommunicate(empId,empName);
		}	 
	} 
	
	/* 发送 */
	function sendMsg(msgType,keyIds,top){
		if(keyIds!="" && keyIds.length!=0){
			if(msgType.indexOf("email")>-1){
				var left = 200;  
				var top = screen.height/2 - 200;	
				var str  = window.open("/EMailAction.do?operation=6&txl=txl&msgType="+msgType+"&type=main&sendPerson="+keyIds+"&noback=true",null,'menubar=no,toolbar=no,resizable=no,scrollbars=no,loaction=no,status=yes,width=900,height=500,left='+left+',top='+top);
			}else{
				var urls = '/CommunicationNoteAction.do?operation=4&opType=sendPrepare&msgType='+msgType+'&keyId='+keyIds ;
				asyncbox.open({
					id:'sendMsg',title :'短信发送',url:urls,cache:false,modal:true,width:710,height:355, btnsbar:jQuery.btn.OKCANCEL,
					callback : function(action,opener){
			　　　　　	if(action == 'ok'){
							var returnValue = opener.returnValue();
							if(returnValue=="false"){
								return false ;
							}else{
								jQuery.ajax({
								   type: "POST",
								   url: "/CommunicationNoteAction.do?operation=4&opType=sendMessage",
								   data: "newCreateBy="+returnValue.split("@koron@")[0]+"&handContent="+returnValue.split("@koron@")[1]+"&handSmsType="+returnValue.split("@koron@")[2],
								   success: function(msg){
								   		if(msg == "yes"){
								   			alert("发送成功！");
								   			jQuery.close("sendMsg");
								   		}else if(msg == "no"){
								   			alert("发送失败！");
								   		}else{
								   			alert(msg);
								   		}
								   }
								});
						   		return false;
							}
						}
					}
				});
			}
		}else{
			alert(selectOne);		
		}
	}
	
</script>
</head>
 
<body>
<div id="scroll-wrap">
<script type="text/javascript"> 
	var oDiv=document.getElementById("scroll-wrap");
	var sHeight=document.documentElement.clientHeight;
	oDiv.style.height=sHeight+"px";
</script>
<form action="/CommunicationNoteAction.do" method="post" name="form" id="form">
<input type="hidden" name="empStatus" id="empStatus" value="$!CommunicationNoteSearchForm.empStatus"/>
<input type="hidden" name="searchType" id="searchType" value="$!CommunicationNoteSearchForm.searchType"/>
<input type="hidden" name="searchValue" id="searchValue" value="$!CommunicationNoteSearchForm.searchValue"/>
<div class="main">
	<div class="mainLeft">
    	<div class="topL">
        	<p class="p-title">通讯录</p>
        </div>
        <div class="treeMenu">
        	<ul id="treeDemo" class="tree" style="overflow-x: auto; overflow-y: auto; height: 410px;margin:0 0 0 10px;"></ul>
        </div>
    </div>
    <div class="mainRight">
    	<div class="topR">
            <div class="btnItems">
                <span class="cTxt">
                	<input type="text" placeholder="搜索同事" #if("$!CommunicationNoteSearchForm.searchType"=="keyWord") value="$!CommunicationNoteSearchForm.searchValue" #end id="keyword" name="keyword" onkeydown="if(event.keyCode==13){keywords();}"/>
                    <b class="icons" onclick="keywords();"></b>
                </span>
            </div>
            <ul class="uTag clear">
            	<li #if("$!CommunicationNoteSearchForm.empStatus"=="") class="sel" #end onclick="showEmp('')" id="in_li">在职员工</li>
                <li #if("$!CommunicationNoteSearchForm.empStatus"=="leave") class="sel" #end onclick="showEmp('leave')" id="leave_li">离职员工</li>
            </ul>
            <div class="pagination-flickr">
            	#set( $list = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"])
            	<a href="javascript:edh('0')" id="0_edh">全</a>
            	#foreach ($element in $list)
	            	<a href="javascript:edh('$!element')" id="$!{element}_edh">$!element</a>
				#end
            </div>
        </div>
        <div class="infoList">
            <div class="address-wp" id="content">
            	<ul class="address-ul">
            		#foreach($emp in $empList)
                	<li class="address-li">
                    	<span class="icons send-mail" title="发送邮件" onclick="sendEmail('$!emp.id')"></span>
                        <span class="icons send-info" title="发送私信" onclick="sendMessage('$!emp.id','$!emp.empFullName')"></span>
                        <span class="icons send-ms" title="发送短信" onclick="sendMS('$!emp.id')"></span>
                    	<a href="#" class="default-pt">
                    		#if("$!emp.photo"=="")
	                    		<img src="/style/images/item/defaultPhoto.jpg" alt=""/>
	                    	#else
	                    		<img src="/ReadFile.jpg?fileName=$!{emp.id}_140.jpg&amp;tempFile=false&amp;type=PIC&amp;tableName=tblEmployee" alt=""/>
                    		#end
                    	</a>
                        <div class="r-d">
                        	<span class="name-department">
                            	<a class="name-a">$!emp.empFullName</a>
                                <a class="department-a">$!emp.deptFullName</a>
                            </span>
                            <ul class="contack-show">
                            	<li>     	
                                    <i>邮箱</i>
                                    <em>$!emp.email</em>
                                </li>	
                                <li>
                                    <i>座机</i>
                                    <em>$!emp.tel</em>
                                </li>
                                <li>
                                    <i>手机</i>
                                    <em>$!emp.mobile</em>
                                </li>
                            </ul>
                        </div>
                    </li>
                    #end
                </ul>
            </div>
            #if($!empList.size()>0)
           	<div class="listRange_pagebar">
				$!pageBar
			</div>	
			#end
        </div>
    </div>
</div>
</form>
</div>
</body>
</html>

