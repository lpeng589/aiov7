<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>快递单打印向导</title>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<style type='text/css'>
body,div,form,p,input,button,h1,h2,h3,h4,h5,h6,span,img,font,ul,li,ol,a,table,thead,tboady,tr,td{margin:0;padding:0;}
body{font-size:12px;font-family:Microsoft Yahei;}
li{list-style:none;}
img{border:0;}
</style>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function print_parm(){
	if(jQuery("#userName").val() == ""){
		alert("收件人不能为空");
		return false;
	}	
	var mouldName = jQuery("input[name='rado']:checked").val();	
	if(mouldName == "" || typeof(mouldName) == "undefined"){
		alert("请选择快递");
		return false;
	}
	jQuery("#mouldName").val(mouldName.split(";")[0]);
	jQuery("#moduleId").val(mouldName.split(";")[1]);
	jQuery("#moduleViewId").val(mouldName.split(";")[2]);
	form.submit();
	//window.print();
} 
function toPrintSet(){
	var url = "/CRMPrintSetAction.do?operation=4";
	mdiwin(url,"快递单模板设计");
}
function dispy(){
	var pt = jQuery("#userName").val();
	var ptDiv = pt.split(";")[0];
	var id = "#printDivId"+ptDiv;
	jQuery("div[name^='printDivId']").css("display","none");
	jQuery(id).css("display","block");	
}
</script>
</head>
<body >
<form action="/CRMPrintSetAction.do?operation=18" method="post" name="form" style="height:380px;overflow:auto;" target="_blank">
<input type="hidden" name="mouldName" id="mouldName" />
<input type="hidden" name="moduleId" id="moduleId" />
<input type="hidden" name="moduleViewId" id="moduleViewId" />
<div align="center">         
  <table border="0" cellspacing="0" cellpadding="0" style="line-height: 150%" width="80%" >
    <tr>
      <td height="40" align="center">
     	<div style="padding:20px 0;">
        	<img border="0" src="/$globals.getStylePath()/images/Left/kdpr.png" />
        	<font color="#808080"><b>快递单打印向导</b></font>
  		</div>
      </td>
    </tr>          
    <tr>
      <td>             
        <p style="padding:5px 0;">
        	<b>选择收件人：   </b>
	        <select size="1" name="userName" id="userName" style="width:100px" onchange="dispy();">  
	        <option value=""></option> 
	        #foreach($log in $!UserDet)    
	        <option value="$velocityCount;$!globals.getFromArray($log,4)" >$!globals.getFromArray($log,5)</option> 
	        #end                
	        </select>
        </p>             
        <p style="padding:5px 0;">	
        	<b>收件人信息：</b>
        	<font color="#808000">(可在客户视图中各字段修改下列信息)</font>
        </p>
        #foreach($log in $!UserDet)
        <div style="padding:10px;display:none;" name="printDivId${velocityCount}" id="printDivId${velocityCount}">
       	<p>电话：<i>$!globals.getFromArray($log,7)</i></p><input type="hidden" name="Telephone" value="$!globals.getFromArray($log,7)" /> 
       	<p>手机：<i>$!globals.getFromArray($log,6)</i></p><input type="hidden" name="Mobile" value="$!globals.getFromArray($log,6)" /> 
       	<p>单位：<i>$!globals.getFromArray($log,2)</i></p><input type="hidden" name="ClientName" value="$!globals.getFromArray($log,2)" />
       	<p>地址：<i>$!globals.getFromArray($log,1)</i></p><input type="hidden" name="Address" value="$!globals.getFromArray($log,1)" />
       	</div>
       	#end  
        <!-- <input type="radio" value="0" name="atp" checked="checked" />办公地址：
        <input type="radio" value="1" name="atp" />家庭地址：          -->  
       
        <p> <b>选择快递单模板：</b>(<a href="javascript:toPrintSet();">快递单模板设计</a>)</p>
        <div style="padding:10px;">
	       #foreach($log in $!mouldName)	       
		    <input type="radio" name="rado" id="rado" value="$!log.id;$!log.ref_moduleId;$!log.ref_moduleViewId" checked="checked"/>#if("$!log.status" == "1")$!log.moduleName(系统)#else$!log.moduleName#end
		   #end  
	    </div>
        <div style="text-align:center;padding:20px 0 0 0;">
        	<div class="btn btn-mini btn-danger" onclick="print_parm();">打 印</div>
        </div>
      </td>
    </tr>            
   
  </table>          
        </div>        
</form>
</body>
</html>
