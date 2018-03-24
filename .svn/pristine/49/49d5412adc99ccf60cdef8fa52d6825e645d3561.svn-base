<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="/style/css/client.css" rel="stylesheet" type="text/css" />
<link href="/style/css/client_sub.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
function checkAll(obj){
	$("input:checkbox",document).each(function() {
		$(this).attr("checked", obj.checked);
	});
}

function returnValue(){
	var returnValue = "";
	$("input:[name='mobile']:checked",document).each(function(){
		returnValue += $(this,document).attr("value")+"," ;
	}) ;
	if(returnValue.length==0){
		alert("必须选择接收人！") ;
		return "false";
	}
	var content = $("#content",document).val();
	if(content.length==0){
		alert("短信内容不能为空！") ;
		return "false";
	}
	return returnValue + "@koron@" + content+ "@koron@" + $("#smsType",document).val() ;
}
function changeSmsType(){
	if(document.getElementById("smsType").value==1){
		if(!confirm("$text.get("sms.msg.choiceNote")")){
			document.getElementById("smsType").value=0;
		}
	}
}
function keyUp(obj){
	cont = $(obj,document).val();
	contlen = cont.length;	
	signLen = $!smsSignLen;
	if(contlen + signLen> 490){
		$(obj,document).val($(obj,document).val().substring(0,490-signLen))
		contlen = 490-signLen;
	}
	$("#fontnum",document).html(contlen) ;	
}
</script>
</head>

<body class="body_f4">
<div class="boxbg2 subbox_w660">
<div class="subbox cf">
  <div class="operate operate2" id="handle">
  <ul>
  	<li class="sel">短信发送</li>
  </ul>
  <div class="closel"></div>
  </div>
  <div class="bd">
      <div class="inputbox inputbox2">
        <ul>
        <li class="col"><span><font style="color:#F18302;"></font>&nbsp;</span>
            <select name="smsType" id="smsType" onchange="changeSmsType()">			 
			 	<option value="0">$globals.getEnumerationItemsDisplay('sendSMSType','0')</option>
			 	<option value="1">$globals.getEnumerationItemsDisplay('sendSMSType','1')</option>
			 </select>            
          </li>
        
          <li class="message"><span><font style="color:#F18302;">*</font>$text.get("note.lb.CollectionMessage")：</span>
          #if("$sendType"!="detailPage")
        	  <input type="checkbox" onclick="javascript:checkAll(this);" />$text.get("common.lb.selectAll")


		  #end
          	<ul>
          		#foreach($detInfo in $clientDetList)
		  			#if("$globals.get($detInfo,1)"!="")
					<li title="($globals.get($detInfo,1))">
					#if("$sendType"!="detailPage")
						<input name="mobile" type="checkbox" value="$!globals.get($detInfo,1)" checked="checked"/>
					#else
						<input name="mobile"  style="display: none" type="checkbox" checked="checked" value="$!globals.get($detInfo,1)"/>
					#end
					<span></span>$globals.get($detInfo,0) <font>($globals.get($detInfo,1))</font>
					</li>	  			
		  			#end 
		  		#end 
            </ul>
          </li>
          <li class="col"><span><font style="color:#F18302;">*</font>$text.get("note.send.content")：</span>
            <textarea rows="7" id="content" name="content" class="inp_w3" onkeyup="keyUp(this);" style="float:left;"></textarea>            
          </li>
          <li class="col">   <span><font style="color:#F18302;"></font>&nbsp;</span>         
            $text.get("note.msg.InputIng")<font id=fontnum style="color:red">0</font>$text.get("note.msg.InputIngC")+<font id=smsnum style="color:red">$!smsSignLen</font>$text.get("note.msg.InputingD")
          </li>
        </ul>
      </div>
  </div>
</div>
</div>
</body>
</html>