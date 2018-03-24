<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("oa.mail.sendMail")$text.get("oa.mail.email")</title>
<link type="text/css" rel="stylesheet" href="/js/tree/jquery.treeview.css" />
<link type="text/css" rel="stylesheet" href="/style/css/email.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/tree/jquery.treeview.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<style type="text/css">
.textboxlist-bits{border: 0px;}
.tree-u li a{cursor:pointer;}
.li_more{text-align: center; color: blue;border-top:1px #D5CFCF solid; }
</style>
<script type="text/javascript">
	var tBox_to;
	var tBox_cc;
	var tBox_bcc;
	var input_type = "mail";
	var input_attr = "";
	var inputType = "";
	$(document).ready(function (){
		tBox_to = new jQuery.TextboxList('#to', {unique: true, plugins: {autocomplete: {}}});
		tBox_cc = new jQuery.TextboxList('#cc', {unique: true, plugins: {autocomplete: {}}});
		tBox_bcc = new jQuery.TextboxList('#bcc', {unique: true, plugins: {autocomplete: {}}});
		
		chooseSend();
		/* 选择发件人 */
		$(".d-mail").live('click', function(e) {
			$("#ul_send").show();
			$("#ul_send").bind("click",function(event){
				e = e||event; stopFunc(event);
			});
			e = e||event; stopFunc(e);
		});
		
		$(".textboxlist").live('click', function(){
			inputType = $(this).parent().find("input[class='in_data']").attr("name");
		})
		
		$(".email-body").height($(window).height()-60);
		
		$(document).on("click", function(){
			$("#ul_send").hide();
		});
		
		$("#ul_send li").bind("click",function(){
			$("#showSend").html($(this).text());
			var sendType = $("#sendEmailType").val();
			$("#ul_send").hide();
			var li_value = $(this).attr("li_value");
			$("#sendEmailType").val(li_value);
			if(sendType != li_value){
				if(li_value==""){
					setInputType('ob_type','specify');
					$(".p2").html("组织架构");
				}else{
					setInputType('ob_type','mail');
					$(".p2").html("联系人");
				}
				chooseSend();
			}
		});
		
		#if("$!result.mailTo"!="")
			#foreach($mailto in $!result.mailTo.split(';'))
				#set($mailto = $!globals.encodeHTML2($mailto))
				tBox_to.add('$mailto');
			#end
		#end
		
		//当前存在收件人时（客户列表连接过来）
		#if("$!sendPeriod"!="")
			#foreach($detInfo in $sendPeriodList)
				#set($det = "$globals.get($detInfo,0)&lt;$globals.get($detInfo,2)&gt;")
				tBox_to.add('$det','$globals.get($detInfo,4)');
			#end
		#end
		
		#if("$!result.mailCc"!="")
			#foreach($mailcc in $!result.mailCc.split(';'))
				#set($mailcc = $!globals.encodeHTML2($mailcc))
				tBox_cc.add('$mailcc');
			#end
			deal_cc();
		#end
		#if("$!result.mailBCc"!="")
			#foreach($mailbcc in $!result.mailBCc.split(';'))
				#set($mailbcc = $!globals.encodeHTML2($mailbcc))
				tBox_bcc.add('$mailbcc');
			#end
			deal_bcc();
		#end
	});
	
	//进行切换发件箱时，对应数据重新取
	function chooseSend(){
		var emailType = $("#sendEmailType").val();
		/* 把数据添加到控件textBoxList中 */
		jQuery.ajax({
			type: "POST",
			url: '/EMailAction.do?operation=$!globals.getOP("OP_QUERY")&type=queryTo&emailType='+emailType,
			dataType: "json",
			async: false,
			cache: false,
			success: function(msg){
				tBox_to.plugins['autocomplete'].setValues(msg);
				tBox_to.getContainer().removeClass('textboxlist-loading');
				tBox_cc.plugins['autocomplete'].setValues(msg);
				tBox_cc.getContainer().removeClass('textboxlist-loading');
				tBox_bcc.plugins['autocomplete'].setValues(msg);
				tBox_bcc.getContainer().removeClass('textboxlist-loading');
			}
		});
		
		//隐藏抄送，密送
		$(".div_cc").show();
		if(emailType == ""){
			$(".div_bcc").hide();
			$("#sendto").val("inner");
			$("#moreMail").val("inner");
			$("#sign_label").hide();
		}else{
			$(".div_bcc").show();
			deal_bcc();
			var html = "<li class='br-bottom'><b class='icons'></b><em>通讯录</em><ul class='ul_address'></ul></li><li class='client_li'><b class='icons'></b><em>客户</em><ul class='ul_client'></ul></li>";
			jQuery("#navigation").html(html);
			jQuery("#navigation").removeClass("treeview");
			$("#sendto").val("outter");
			$("#moreMail").val(emailType);
			$("#sign_label").show();
		}
		deal_cc();
		//加载发送历史记录
		querySendHistory();
		
		//加载组织架构
		queryDeptOrEmp();
		changeAccount(emailType);
		inputType = "to";
		inputFocus();
		$("#emailType").val(emailType);
		
	}
	
	function setInputType(type,value){
		$("#to").attr(type,value);
		$("#cc").attr(type,value);
		$("#bcc").attr(type,value);
	}
	
	var curEmailType = "$!result.sendeMailType";
	/* 选择发件箱对内容进行处理 */
	function changeAccount(str){
		if(str == curEmailType){
			return;	
		}
		curEmailType = str;
		jQuery.get('/EMailAction.do?operation=$!globals.getOP("OP_QUERY")&type=sign&emailType='+curEmailType, {},
		   function(data){
		   		var content = "";
		   		content = $("#mailContent").val();
		   		if(content.indexOf("<!-- SIGN_START -->")> -1){ 
		   			content = content.substring(0,content.indexOf("<!-- SIGN_START -->")+"<!-- SIGN_START -->".length)+"------------------------------------\r\n<br/>"+data+"<br/>\r\n------------------------------------"+content.substring(content.indexOf("<!-- SIGN_END -->"));
		   		}else{
		   			pos = content.indexOf("--\n");
		   			content = content.substring(0,pos+"--\n".length)+data+content.substring(content.indexOf("\n--",pos));
		   		}
		   		editor.html(content);
		   }
	  	);
	}
	
	function stopFunc(e){ 
		e.stopPropagation?e.stopPropagation():e.cancelBubble = true; 
	}
	
	//加载内容控件
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="mailContent"]',{
			items : [
		        'undo', 'redo', '|','source', 'template', 'code', 'cut', 'copy', 'paste',
		        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
		        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'clearhtml', 'fullscreen', '/',
		        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
		        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
		        'flash', 'media', 'table', 'hr', 'emoticons',
		        'anchor', 'link', 'unlink'
			],
			uploadJson:'/UtilServlet?operation=uploadFile'
		});
	});
	
	//判断是否存在特殊字符
	function isHaveSpecilizeChar(data){
		var i = data.indexOf("\"");
		var j = data.indexOf("\'");
		var m = data.indexOf("\\");
		if (i >= 0 || j >= 0 || m >= 0) {
			return false;
		}
		return true;
	}
	
	//判断是否为空
	function isNull(variable,message){
		if(variable=="" || variable==null){
			asyncbox.tips(message+" "+"$text.get('common.validate.error.null')",'alert',1500);
			return false;
		}else{
			return true;
		}		
	}
	
	/* 发送邮件时验证数据 */
	function checkForm(){
		var emailType = $("#sendEmailType").val();
		var to_value = "";
		$("#to").parent().find(".showSelect").each(function(){
			to_value += $(this).text()+";";
		});
		if($("#isPart").val() == ""){
			//密送收件人
			var bcc_value = "";
			$("#bcc").parent().find(".showSelect").each(function(){
				bcc_value += $(this).text()+";";
			});
			$("#bcc").val(bcc_value);
			if($(".div_bcc").is(":visible") == false){
				$("#bcc").val('');
			}
			if(to_value == "" && $("#bcc").val() == ""){
				//验证收件人是否存在
				asyncbox.tips("收件人 "+"$text.get('common.validate.error.null')",'alert',1500);
				inputType = "to";
				inputFocus();
				return false;
			}
		}else{
			if(!isNull(to_value,'分别发送人')){
				return false;
			}
		}
		$("#to").val(to_value);
		var mailTitle = $("#mailTitle").val();
		if(!isNull(mailTitle,'主题')){
			//验证主题是否为空
			$("#mailTitle").focus();
			return false;
		}
		if(getStringLength(mailTitle)>100){
			//验证主题字数是否超出100
			asyncbox.tips("$text.get('oa.mail.content.mailtitle')",'alert',1500);
			$("#mailTitle").focus();
			return false ;
		}
		var bool1 = isHaveSpecilizeChar(mailTitle);
		if(bool1==false){
			//验证主题是否存在特殊字符
			asyncbox.tips("$text.get('oa.mail.content.mailtitle')$text.get('contain.specilize')",'alert',1500);
			$("#mailTitle").focus();
			return false;
		}
		var mailContent= editor.html();
		if(!isNull(mailContent,'$text.get("oa.mail.contenet")')){
			$("#mailContent").focus();
			return false;
		}
		document.form.mailContent.value = mailContent ;
		
		//抄送收件人
		var cc_value = "";
		$("#cc").parent().find(".showSelect").each(function(){
			cc_value += $(this).text()+";";
		});
		$("#cc").val(cc_value);
		if($(".div_cc").is(":visible") == false){
			$("#cc").val('');
		}
		return true;
	}
	
	
	/* 发送邮件 */
	function send(type){
		if(checkForm()){
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
			$("#saveType").val(type);
			form.submit();
		}
	}
	
	/* 返回按钮 */
	function doBack(){
		var fromPage = '$!request.getParameter("fromPage")';
		if(fromPage == "addFriend"){
			location.href='/OAUserGroup.do?type=userManager&handetype=addFirend';
		}else if(fromPage == "myFriend"){
			location.href='/OAUserGroup.do?type=userManager';
		}else{
			location.href='/EMailQueryAction.do?operation=4&emailType=$moreMail&type=main&groupId=$!groupId';
		}
	}
	
	/* 抄送输入框显示和隐藏 */
	function deal_cc(){
		if($(".div_cc").is(":hidden")){
			$(".div_cc").show();
			$("#span_cc").html("删除抄送");
			inputType = "cc";
		}else{
			$(".div_cc").hide();
			$("#span_cc").html("添加抄送");
			inputType = "to";
		}
		inputFocus();
	}
	
	/* 密送输入框显示和隐藏 */
	function deal_bcc(){
		if($(".div_bcc").is(":hidden")){
			$(".div_bcc").show();
			$("#span_bcc").html("删除密送");
			inputType = "bcc";
		}else{
			$(".div_bcc").hide();
			$("#span_bcc").html("添加密送");
			inputType = "to";
		}
		$("#span_bcc").show();
		inputFocus();
	}
	
	/* 分别发送 */
	function partSend(){
		if($("#lable_part").is(":hidden")){
			$(".div_cc").hide();
			$(".div_bcc").hide();
			$("#span_bcc").hide();
			$("#span_cc").hide();
			$("#lable_part").show();
			$("#span_part").html("取消分别发送");
			$("#send_em").html("分别发送");
			$("#isPart").val('1');
		}else{
			$(".div_cc").show();
			$(".div_bcc").show();
			$("#span_bcc").show();
			$("#span_cc").show();
			$("#lable_part").hide();
			$("#span_part").html("分别发送");
			$("#send_em").html("收件人");
			deal_cc();
			deal_bcc();
			$("#isPart").val('');
		}
	}
	
	/* 添加附件 */
	function addFile(){
		openAttachUpload('$path');
	}
	
	/* 上传附件返回数据 */
	function insertNextFile(str){
		fileCount=fileCount+1;
		var filevalue = document.form.attachFiles.value;
		if(filevalue.indexOf(str) == -1){	  
			var fileHtml = '';
		   	fileHtml += '<li class="update-file-li" id ="'+str+'"><b class="icons"></b>';	  
		    fileHtml +='<i>'+str+'</i><input type=hidden name="attachFile" value="'+str+'"/>';	
		    fileHtml += '<em onclick="removeFile(\'' + str + '\');">删除</em></li>';	   
		    var fileElement = document.getElementById("files_preview");
		    fileElement.innerHTML = fileElement.innerHTML + fileHtml;  
		    document.form.attachFiles.value = filevalue+str+";";
	    }
	}
	
	/* 历史记录设置数据 */
	function addSend(str,id){
		if(inputType == "to"){
			tBox_to.add(str,id);
		}else if(inputType == "cc"){
			tBox_cc.add(str,id);
		}else if(inputType == "bcc"){
			tBox_bcc.add(str,id);
		}
	}
	
	
	/* 获取发送人历史记录 */
	var counts = "";
	function querySendHistory(type){
		var mailType = $("#sendEmailType").val();
		var keyWord = $("#keyWord").val();
		if(type == "true"){
			counts = "";
			$("#ul_his").html("");
		}else if(type == ""){
			counts = "0";
		}
		var url = "/UtilServlet?operation=mailSendHistory&loginId=$globals.getLoginBean().id&emailType="+mailType+"&keyWord="+encodeURI(keyWord);
		if(counts != ""){
			counts = (new Number(counts)+1);
			url += "&pageNo="+counts;
		}
		jQuery.ajax({
		   type: "POST",
		   url: url,
		   async: false,
		   cache: false,
		   success: function(msg){
		   		var m = msg.split(";|");
		   		var mVal = m[1];
		   		var count = m[0];
		   		var ds = mVal.split(";");
		   		var str = "";
		   		$(".li_more").remove();
		   		$(".del-all").remove();
				for(var i=0 ;i<ds.length;i++){
					var obj = jQuery.parseJSON(ds[i]);
					if(obj != null){
						var obj_name = obj.name;
						var obj_str = obj.name;
						if(mailType != ""){
							obj_str = "";
							if(typeof(obj_name) != undefined && obj_name != "" && obj_name != "null"){
								obj_str = obj.name+'<'+obj.email+'>';
							}else{
								obj_name = obj.id;
								obj_str = obj.id;
							}
						}
						str += '<li id="li_'+obj.id+'" onclick="addSend(\''+obj_str+'\',\''+obj.id+'\')">';
						str += '<a style="color:black;" title="'+obj_name+'" href="javascript:void(0)">'+obj_str+'</a>';
						str += '<b class="bg-icons" onclick="delHistory(\''+obj.id+'\')"></b></li>';
					}
				}
				if(new Number(count) > 20 * (new Number(counts)+1)){
					str += '<li class="li_more"><a href="javascript:void(0)" title="显示更多最近联系人" onclick="querySendHistory(\''+counts+'\')">更多</a></li>';
				}
				str += '<li class="del-all"><span style="color:#999;" onclick="delHistory(\'all\')">清空所有记录</span></li>';
				if(type == undefined){
					$("#ul_his").html(str);
				}else{
					$("#ul_his").append(str);
				}
		   }
		});
	}
	
	
	/* 删除邮件发送的历史记录 */
	function delHistory(id){
		event.stopPropagation();
		var url = "/EMailQueryAction.do?operation=3&type=delSendHistory&id="+id;
		var title = "此联系人";
		if(id == "all"){
			title = "所有最近通讯录";
		}
		asyncbox.confirm('是否清空'+title+'！','提示',function(action){
			if(action == 'ok'){
				jQuery.ajax({
				   type: "POST",
				   url: url,
				   async: false,
				   cache: false,
				   success: function(msg){
				   		if(msg == "OK"){
					   		if(id != "all"){
					   			jQuery("#li_"+id).remove();
					   		}else{
					   			querySendHistory();
					   		}
				   		}
				   }
				});
			}
		});
	}
	
	var countto = "";
	/* 加载组织架构 */
	function queryDeptOrEmp(type){
		var keyword = $("#frameKeyWord").val();
		var sendEmailType = $("#sendEmailType").val();
		if(type == "true" || type == undefined){
			countto = "";
   			$(".ul_address").html("");
   			$(".ul_client").html("");
   		}else if(type == ""){
			countto = "0";
		}
		var url = "/EMailQueryAction.do?operation=4&type=queryUserData&keyword="+encodeURI(keyword);
		var dataType = "text";
		if(sendEmailType != ""){
			url += "&opType=outside";
			if(countto != ""){
				countto = (new Number(countto)+1);
				url += "&pageNo="+countto;
			}
			dataType = "json";
		}
		
		jQuery.ajax({
		   type: "POST",
		   url: url,
		   async: false,
		   cache: false,
		   dataType: dataType,
		   success: function(msg){
		   	if(sendEmailType == ""){
				jQuery("#navigation").html(msg);
				jQuery("#navigation").treeview({
					collapsed: true,
					unique: false
				});
			}else{
		   		var mVal = msg.address;
		   		var count = msg.count;
		   		var ds = mVal;
		   		var str = "";
		   		$(".li_clientMore").remove();
		   		for(var i=0 ;i<ds.length;i++){
					var obj = ds[i];
					if(obj != null){
						str += '<li onclick="addSend(\''+obj.name+'&lt;'+obj.email+'&gt;'+'\',\''+obj.id+'\')"><a title="'+obj.name+'&lt;'+obj.email+'&gt;" >'+obj.name+'&lt;'+obj.email+'&gt;'+'</a></li>';
					}
		   		}
		   		if(type == undefined){
		   			$(".ul_address").html(str);
		   		}else{
		   			$(".ul_address").append(str);
		   		}
		   		ds = msg.client;
		   		str = "";
		   		for(var i=0 ;i<ds.length;i++){
					var obj = ds[i];
					if(obj != null){
						var email = obj.name+'&lt;'+obj.email+"&gt;";
						var clientName = obj.clientName;
						str += '<li><a title="'+email+'" onclick="addSend(\''+email+'\',\''+obj.id+'\')">'+clientName+'-'+email+'</a></li>';
					}
		   		}
		   		if(new Number(count) > 20 * (new Number(countto)+1)){
					str += '<li class="li_clientMore"><a href="javascript:void(0)" title="显示更多客户联系人" onclick="queryDeptOrEmp(\''+countto+'\')">更多</a></li>';
				}
				if(type == undefined){
					$(".ul_client").html(str);
				}else{
		   			$(".ul_client").append(str);
		   		}
			}
		   }
		});
	}
	
	//光标
	function inputFocus(){
		jQuery(".div_"+inputType).find(".textboxlist-bit-editable:visible").find("input[autocomplete=off]").focus();
	}
	
	
	function show_p(type){
		if(type == "1"){
			$(".p1").removeClass("p_bg");
			$(".p2").addClass("p_bg");
			$("#div_p1").show();
			$("#div_p2").hide();
		}else{
			$(".p2").removeClass("p_bg");
			$(".p1").addClass("p_bg");
			$("#div_p1").hide();
			$("#div_p2").show();
		}
	}
	
	/* 更改签名 */
	function updateSign(){
		var sendEmailType = $("#sendEmailType").val();
		asyncbox.open({
		　　 url : '/EMailAccountAction.do?operation=7&op=signPre&mainAccount=&accountId='+sendEmailType,
			title : '签名设置',
			id : 'dealSign',
		　　	width : 700,
		　　 height : 400
		});
	}
	
	function dealAsyn(type){
		if(type == "SignOK"){
			var curEmailTypes = curEmailType;
			curEmailType = "";
			changeAccount(jQuery("#sendEmailType").val());
			curEmailType = curEmailTypes;
			jQuery.close('dealSign');
		}
	}
	
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form name="form" method="post" action="/EMailAction.do" #if("$!result.sendeMailType" == "")target="formFrame"#end>
<input type="hidden" name="operation" id="operation" value="$globals.getOP("OP_ADD")"/>
<input type="hidden" name="noback" id="noback" value="$!noback"/>
<input type="hidden" name="saveType" id="saveType" value="save"/>
<input type="hidden" name="oldDraftid" id="oldDraftid" value="$!result.id"/>
<input type="hidden" name="fromPage" id="fromPage" value='$!request.getParameter("fromPage")'/>
<input type="hidden" name="replayId" id="replayId" value="$!replayId"/>
<input type="hidden" name="revolveId" id="revolveId" value="$!revolveId"/>

<input type="hidden" name="clientStr" id="clientStr" value=""/>

<!-- 分别发送处理 -->
<input type="hidden" name="isPart" id="isPart" value="" />

<input type="hidden" name="emailType" id="emailType" value="$!emailType"/>
<input type="hidden" name="moreMail" id="moreMail" value="$!moreMail"/>

<!--用户选择的收件人 -->
<input type="hidden" name="sendPeriod" id="sendPeriod" value="#if("$!sendPeriod" != "")true#end"/>

<input type="hidden" name="sendEmailType" id="sendEmailType" value="$!result.sendeMailType" />
<input type="hidden" name="attachFiles" id="attachFiles" value="$!result.mailAttaches"/>
<input type="hidden" name="delFiles" id="delFiles" value=""/>
	<div class="top-btns">
    	<span class="btn-b" onclick="send('save')">发 送</span>
        <!-- <span class="btn-w">预 览</span> -->
        <span class="btn-w" onclick="send('draft')">存草稿</span>
        #if($noback)
        	<span class="btn-w" onClick="closeWin();">关 闭</span>
		#else
			<span class="btn-w" onClick="doBack()">返 回</span>
		#end
    </div>
    <div class="email-body">
    	<div class="email-body-lf">
        	<!-- 发送头部 body-lf-head  start -->
        	<div class="body-lf-head">
        		<div class="fa div_to" style="height:27px;">
                	<em>发件人</em>
                   	<div class="d-mail">
                   		<i id="showSend"></i>
                   		<ul id="ul_send" class="ul-mail">
                   			#if("$!type" != "transmitOut")
                   			<li li_value="">$globals.getLoginBean().empFullName</li>
                   			#end
                   			#if("$!type" != "transmitInt")
	                   			#foreach($row in $MailinfoSetting)
		                   			<li li_value="$!globals.get($row,0)">$!globals.get($row,1)</li>
		                   			#if("$!result.sendeMailType" == "$!globals.get($row,0)" || ("$!type" == "transmitOut" && "$!globals.get($row,2)"=="1"))
		                   				<script type="text/javascript">
		                   					$("#showSend").html("$!globals.get($row,1)");
		                   					changeAccount('$!globals.get($row,0)');
		                   				</script>
		                   			#end
	                   			#end
	                   		#end
                   			#if("$!result.sendeMailType" == "")
                   				<script type="text/javascript">
	                   				$("#showSend").html("$globals.getLoginBean().empFullName");
	                   			</script>
	                   		#end
                   		</ul>
                   		<b class="triangle"></b>
                   	</div>
					<span id="span_part" onclick="partSend();" style="float: right;">分别发送</span>
                   	<lable id="lable_part" style="display:none;color:#ccc;float: right;margin:5px;">每个收件人将收到单独发给他/她的邮件。</lable>
                    <span id="span_bcc" onclick="deal_bcc();" style="float: right;">添加密送</span>
                	<span id="span_cc" onclick="deal_cc();" style="float: right;">添加抄送</span>  
                </div>
                <input name="sendto" id="sendto" type="hidden" value="" />
            	<div class="fa div_to">
                	<em class="f-c" id="send_em">收件人</em>
                    <div class="d-txt"><input name="to" id="to" type="text" class="in_data" value="" #if("$!result.sendeMailType" != "") ob_type="mail" #else ob_type="specify" #end/></div>
                </div>
                <div class="fa div_cc">
                	<em class="f-c">抄&nbsp;&nbsp;&nbsp;送</em>
                	<div class="d-txt"><input name="cc" id="cc" type="text" class="in_data" value="" #if("$!result.sendeMailType" != "") ob_type="mail" #else ob_type="specify" #end/></div>
                </div>
                <div class="fa div_bcc">
                	<em class="f-c">密&nbsp;&nbsp;&nbsp;送</em>
                    <div class="d-txt"><input name="bcc" id="bcc" type="text" class="in_data" value="" #if("$!result.sendeMailType" != "") ob_type="mail" #else ob_type="specify" #end/></div>
                </div>
                <div class="fa">
                	<em>主&nbsp;&nbsp;&nbsp;题</em>
                    <div class="d-txt">
                    	<input class="easyui-validatebox title-ipt" type="text" name="mailTitle" id="mailTitle" value="$!result.mailTitle" required="true"/>
                    </div>
                </div>
                <div class="fa" style="margin:0;">
                	<span class="lf add-file-span" onclick="addFile()"><b class="bg-icons"></b>添加附件</span>
                	<ul style="clear:both;">
                		<div id="files_preview">
                			#foreach($att in $!result.mailAttaches.split(",|;"))
								#if($att.length()>0)
								<li class="update-file-li" id="$att"><b class="icons"></b>
									#if($att.indexOf("&fileName=") >0)
										#set($pp =$att.lastIndexOf("=") +1 )
									  $att.substring($pp)
									#else
										<i style="cursor: pointer;" title="$att" ><a style="color: #666;" href="/ReadFile?tempFile=email&emlfile=$!globals.encode($!result.emlfile)&charset=$!result.mailcharset&attPath=$attPath&fileName=$!globals.encode($att)" target="_blank">$att</a></i>
									#end
									<em onclick="removeFile('$att');">删除</em>
								</li>
								#end
							#end
                		</div>
                	</ul>
                </div>
            </div>
            <!-- 发送头部 body-lf-head  End -->
            <div class="d-txt-area">
            	<textarea name="mailContent" id="mailContent" style="height:300px;width:100%;line-height:normal;">$!result.mailContent</textarea>
                <div class="txt-area-btns">
                	<span class="btn-b" onclick="send('save')">发 送</span>
                    #if($noback)
			        	<span class="btn-w" onClick="closeWin();">关 闭</span>
					#else
						<span class="btn-w" onClick="doBack()">返 回</span>
					#end
					#if($LoginBean.operationMap.get("/BillNoAction.do").update() || $LoginBean.id == "1")
					<label id="sign_label" style="float: right;display: none;"><em style="margin-left: 10px;cursor: pointer;" onclick="updateSign()">更改签名</em></label>
					#end
					<label class="lf" style="overflow:hidden;margin:1px 0 0 5px;float: right;">
						<input class="lf" style="margin:2px 2px 0 0;" type="checkbox" name="begReplay" value="1" #if("$!result.begReplay" == "1") checked #end/>
						<em class="lf" >$text.get("email.lb.begreplay")</em>
					</label>
                </div>
            </div>
        </div>
        
        <div class="right-kb">
        	<p class="p1" onclick="show_p('1')">最近联系人</p>
        	<p class="p2 p_bg" onclick="show_p('2')">
        		#if("$!result.sendeMailType" == "")组织架构#else 联系人#end
        	</p>
            <div id="div_p1">
	            <div class="d-search">
	            	<div class="s-ipt">
	                	<input class="ipt" type="text" name="keyWord" id="keyWord" value="" placeholder="搜索最近联系人" onkeyup="querySendHistory('true')"/>
	                    <b class="b-s icons" onclick="querySendHistory('1')"></b>
	                </div>
	                <!-- <b class="b-a icons" title="添加最近联系人"></b> -->
	            </div>
	            <ul class="tree-u" id="ul_his">
	            </ul>
	        </div>
	        <div id="div_p2" style="display: none;">
	        	<div class="d-search">
	            	<div class="s-ipt">
	                	<input class="ipt" type="text" name="frameKeyWord" id="frameKeyWord" value="" placeholder="搜索" onkeyup="queryDeptOrEmp('true')"/>
	                    <b class="b-s icons" onclick="queryDeptOrEmp('true')"></b>
	                </div>
	            </div>
	            <ul id="navigation">
	            </ul>
            	<script type="text/javascript">
					$("#navigation").css("height",$(".right-kb").height()-140);
					$("#ul_his").css("height",$(".right-kb").height()-150);
				</script>
	        </div>
        </div>
    </div>
</form>
</body>
</html>
