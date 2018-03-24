<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("MainlReceive.title.mainlReceive")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script type="text/javascript" src="/js/function.js"></script>
<script src="/js/jquery.js" type="text/javascript"></script> 
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<style type="text/css"> 
   #progress {background: white; height: 11px; padding: 2px; border: 1px solid green; margin: 2px;} 
   #progress span {background: green; height: 7px; text-align: center; padding: 0px; margin: 0px; 
       display: block; color: yellow; font-weight: bold; font-size: 10px; width: 0%;} 
	.tips_div{
	width:450px;
	height:117px;
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
	margin-top:20px;
	margin-left:10px;
	margin-right:10px;
	width:95%;
	float:left;
	}
	
	.tips_operate{
	width:99%;
	float:left;
	text-align:center;
	margin-top:15px;
	margin-left:8px;
	}   
</style> 

<script>

var totalMail = 0;
var pos = 0;
var mailRate = 10;//10K 
var messageSize = 100;
var stime=500;
var iscancel = false;
var ajaxGet;
var curEmailType;
var accountPos = 0;

function receiveStop(){
	iscancel=true; 
	ajaxGet.abort();
	mailStop(true);
}
function background(){
	//后台执行
   	jQuery.get('EMailAction.do?operation=$!globals.getOP("OP_QUERY")&type=receive&emailType='+curEmailType+'&mailOp=backgroud', { },
	   function(data){
	   		iscancel=true; 
	   		cancelProgress();
	   		jQuery.unblockUI();	
			window.parent.leftFrame.curIsReceier = false;
			window.location='/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$emailType&type=main&groupId=1';
	   } 
	);  
}
function mailStop(cancel){

	iscancel=cancel; 
		
	//关闭邮件
   	jQuery.get('EMailAction.do?operation=$!globals.getOP("OP_QUERY")&type=receive&emailType='+curEmailType+'&mailOp=closeMailSystem', { },
	   function(data){
	   		
	   } 
	);  
	cancelProgress();
	accountPos ++;
	if(cancel || accountPos == mails.length){
		jQuery.unblockUI();	
		window.parent.topFrame.curIsReceier = false;
		window.location='/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$oldEmailType&type=main&groupId=1';
	}else{
		curEmailType = mails[accountPos][0];
	  	receiveMail();
	}
}

var progress_node_id = "progress"; 
function SetProgress(progress) { 
    if (progress) { 
        $("#" + progress_node_id + " > span").css("width", String(progress) + "%"); 
        $("#" + progress_node_id + " > span").html(String(progress) + "%"); 
    } 
} 

var prog = 0; 
var timmer;
function doProgress() {  
	if(stime < 100 && prog < 99){
		prog = 99;
		SetProgress(prog); 
        prog++; 
        return;
	}   
    if (prog <= 99) {     	
        timmer = setTimeout("doProgress()", stime+100); 
        SetProgress(prog); 
        prog++; 
    } 
} 
function cancelProgress(){
	prog = 0;
	clearTimeout(timmer);
}


function handleMail(){
		if(totalMail==0){ 
			mailStop(false);
			return;
		}
		if(iscancel){
			return;
		}
		if(pos>=totalMail){
			mailStop(false);
			return;
		}
   		releveMail = totalMail-pos;   		
   		try{ //取邮件头
	   		pos++; 
	   		ajaxGet = jQuery.get('EMailAction.do?operation=$!globals.getOP("OP_QUERY")&type=receive&emailType='+curEmailType+'&mailOp=header', 
	   			function(data){
	   				try{
	   					//alert(pos+":"+data);         
	   					receiveBody = false;
						if(data.indexOf("ERROR:")==-1 && data.indexOf("isNew:true")==0){
				   			receiveBody = true;
				   			data = data.substring("isNew:true;".length);
				   			messageSize = data.substring(0,data.indexOf(";"));
				   			subject = data.substring(data.indexOf(";")+1);
							
							$("#statDiv").html("$text.get("common.lb.mailReceive.total")："+totalMail+" $text.get("common.lb.mailReceive.Remain")："+releveMail);
					   		$("#subjectDiv").html(subject);
					   	}else if(data.indexOf("ERROR:")==-1){
					   		data = data.substring("isNew:false;".length);
				   			messageSize = data.substring(0,data.indexOf(";"));
				   			subject = data.substring(data.indexOf(";")+1);
							
							$("#statDiv").html("$text.get("common.lb.mailReceive.total")："+totalMail+" $text.get("common.lb.mailReceive.Remain")："+releveMail);
					   		$("#subjectDiv").html(subject);
					   	}else{
					   		$("#subjectDiv").html(data);
					   	}	
					   	if(!receiveBody){
				   			handleMail();
				   			return;
				   		}
				   		stime = messageSize / (mailRate);		
				   		stime = stime /100;
				   		stime = Math.round(stime);		    		
					   		
				   		//$("#aa").val($("#aa").val()+ " messageSize="+messageSize+":mailRate="+mailRate+":stime="+stime+":"); 
				   		doProgress();		    		
				   		curTime  = new Date().getTime();
				   		try{
					   		ajaxGet = jQuery.get('EMailAction.do?operation=$!globals.getOP("OP_QUERY")&type=receive&emailType='+curEmailType+'&mailOp=body', 
					   			function(data){
					   				try{
										nTime = new Date().getTime();
										nTime = nTime - curTime;
										if(data.indexOf("rate:")==0){
								   			//mailRate = data.substring(5);
								   			if(nTime == 0) nTime = 1;
								   			mailRate = messageSize / nTime;
								   			if(mailRate == 0) mailRate = 0.1;
								   		}
								   		//每封邮件一个进度条
								   		cancelProgress();
								   		//$("#aa").val($("#aa").val()+ " \r\n"); 
							   		}catch(e){
							   			alert(e);
							   		}
							   		handleMail();
					   		}); 
				   		}catch(e){
				   			alert(e);
				   		}		   		
			   		}catch(e){
			   			alert("Data Error:"+e);
			   		}
	   		}); 
   		}catch(e){
   			alert("try Error:"+e);
   		}   		
}


var mails=new   Array();
#set($i=0)
#foreach($row in $emailTypeObj)
	mails[$i] =  ["$row.id","$row.mailaddress"];
	#set($i=$i+1)
#end

function receiveMail(){
	$("#titleDiv").html("$text.get("oa.are.connecting")");	
	$("#subjectDiv").html("$text.get("oa.are.connecting") &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+mails[accountPos][1]);	
	$("#statDiv").html("");	
	

	ajaxGet = jQuery.get('EMailAction.do?operation=$!globals.getOP("OP_QUERY")&type=receive&emailType='+curEmailType+'&mailOp=connectMailSystem', {},
		   function(data){
		   		if(data.indexOf("ERROR:")==0){
		   			alert(data.substring(6));
		   			mailStop(false);
		   			return;
		   		}
		    	
		    	totalMail=Number(data); //取决邮件 数


				pos = 0;	    	
		    	$("#titleDiv").html("$text.get("oa.Collect.mai")");	
		    	handleMail();		    	
		   } 
	  );  	  
}

$(document).ready(function() {
	  if(mails.length == 0 ){
	  	  mailStop(true);
	  }else{	  	  
		  jQuery.blockUI({ message: $('#mail_div'), css: {left:'350px', width: '400px',height:'117px' } }); 
		  
		  //启动第一次收件


		  accountPos = 0;
		  curEmailType = mails[accountPos][0];
		  receiveMail();
	  }
      
}); 

</script>
<base target="_self">
</head>
<body>
<!-- textarea rows="10" cols="80" id="aa"></textarea>  -->
    <div class="tips_div" id="mail_div" style="display:none; cursor:default;">
      <div class="tips_title" >
        <div class="left_title" id="titleDiv">$text.get("oa.are.connecting")</div>
        <div class="tips_close"><a href="javascript:receiveStop();" class="div_close"><img src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("common.lb.cancel")" border="0"/></a></div>
      </div>  
      <div class="tips_content">    
      	<div id="subjectDiv" style="float:left;width:270px;text-align:left;overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">$text.get("oa.are.connecting")</div>
      	<div id="statDiv" style="float:right;width:130px"></div>
      </div>      
      <div class="tips_operate" style="padding-left:0px;padding-right:0px;margin-left:5px;margin-right:0px;">
      	<div id="progress" style="float:left;width:71%;text-align:left;"><span> </span></div>
      	<div style="float:right;width:26%;height:30px;text-align:center;"> 
	  	<button onClick="receiveStop(); ">$text.get("common.lb.cancel")</button><button onClick="background(); ">$text.get("common.lb.background")</button>
	  	</div>
      </div>
    </div>
</body>
</html>
