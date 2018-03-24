<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("sysAcc.lb.yearCloseAcc")</title>
<link type="text/css" rel="stylesheet" href="/style/css/reg.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<style type="text/css">
body{font:12px/1.5 Microsoft Yahei,Arial;color:#000;background:url(/style/images/item/html_bg.jpg) repeat 0 0;margin:0;padding:0;}
.div_con{overflow-y:auto;}
.div_ts{width:70px;border: 2px aliceblue solid;border-radius:7px;cursor:pointer;text-align:center;}
.div_ts label{cursor:pointer;}
.d_table{width:700px;padding:80px;background:#fff;margin-top:10px;min-height:350px;position:relative;}

.b_1{left:345px}

.d_table>p{text-align:center;font-size:20px;}
.d_table table{width:400px;margin-botto:30px;}
.div_ts_bcolor1{border: 2px lightblue solid;}
.showW{margin:20px 0 0 0;background:#f2f2f2;text-align: left;border-radius:2px;}
.showW>div{position:relative;padding:15px 0;}
.showW>div>b{width:16px;height:16px;display:inline-block;position:absolute;background:url(/style/images/body/i_017.gif) no-repeat 0 0;top:-6px;}

.d_table .t-p{position:absolute;left:20px;top:0;font-size:16px;line-height:34px;color:#666;padding:0 0 0 35px;font-weight:bolder;}
.t-p>b{width:29px;height:29px;background:url(/style/v7/images/v7_icons.png) no-repeat -40px -2px;display:inline-block;position:absolute;left:0;top:0;}

.hBtns:hover{background: #f2f2f2;}

.hBtns{background: #FCFCFC;padding: 5px 10px;border:1px #bbb solid;color:#666;border-radius:3px;cursor:pointer;font-weight:bold;margin-left: 20px;}

.chooseItem{padding-bottom: 20px;padding-left: 25%;}
</style>

<script type="text/javascript">
$(document).ready(function (){
	//鼠标移动到选项时，突出
		//鼠标移动到选项时，突出
		$(".div_ts").mouseover(function(){
			$(this).addClass("div_ts_bcolor1");
		})
		$(".div_ts").mouseout(function(){
			$(this).removeClass("div_ts_bcolor1")
		});
});


function courseDisplay(){
    var obj=document.getElementById("course"); 
    var urls="/UtilServlet?operation=closeAccCourse";
		 AjaxRequest(urls) ;
		 if(response==""||response=="backupDataBase")		{
			 obj.innerHTML='$text.get("closeacc.tip.begin")$text.get("closeacc.tip.backupDataBase")';		 
		 }else if(response==""||response=="lastChangeIni")		{
			 obj.innerHTML='$text.get("closeacc.tip.begin")$text.get("closeacc.tip.lastChangeIni")';		 
		 }else if(response=="delBill"){		
			 obj.innerHTML='$text.get("closeacc.tip.begin")$text.get("closeacc.tip.delBill")';		 
		 }else if(response=="modulesChange"){		
		 	 obj.innerHTML='$text.get("closeacc.tip.setmodules")';
		 }
		 setTimeout("courseDisplay()",2000);
 }
function closeAcc(){
	asyncbox.confirm('$msgtext','提示',function(action){
	　　　if(action == 'ok'){
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
			document.getElementById("tip").innerHTML='$text.get("closeAcc.tip.titleYear")';
			courseDisplay();
			form.submit();
		}
	});
}
</script>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/SysAccAction.do?exe=exe&type=$type&winCurIndex=$winCurIndex">
<div class="div_con" id="listRange_id" align="center">
<div class="d_table">
	<p class="t-p">
		<b></b>
		$text.get("sysAcc.lb.yearCloseAcc")
	</p>
	<div>
		<div id="tip" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
		<div id="course" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
	</div>
	<table border="0" cellpadding="0" cellspacing="0"><tr>
		<td align="center"><div class="div_ts" onclick="closeAcc()">
			<div><img src="/style/images/flow/pro3.gif"/></div>
			<div><label>$text.get("sysAcc.lb.yearCloseAcc")</label></div>
		</div></td>
	</tr>
	</table>
	<div class="showW">
		<div>
			<div class="chooseItem">
				<span><input type="checkbox" name="newAccBox"  value="true" id="newAccBox" checked="checked" disabled="disabled"><input type="hidden" name="newAcc" value="true" id="newAcc"/>$text.get("sysAcc.msg.startnewaccountbook")</span><br />
				#if($!existNewYearBill)
			      <span>$text.get("acc.yearclose.newyearbill")</span>
			    #end
			</div>
			<span style="padding:0 20px;display:inline-block;line-height:30px;">
			<i style="padding-left:25px;display:inline-block;"></i>$text.get("sysAcc.msg.yearCloseAcctite")：$text.get("sysAcc.msg.yearCloseAcc")</span>
			<b class="b_1"></b>
		</div>
	</div>
</div>
</div>

<script type="text/javascript"> 
	$("#listRange_id").css("height",document.documentElement.clientHeight);
</script>
</form>
</body>
</html>