<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>快递单打印模板设计</title>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style1/css/clientTransfer.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function opSubmit(Id){		
	if (confirm('您确定要删除吗?')){
		jQuery.ajax({
		   type: "POST",
		   url: "/CRMPrintSetAction.do?operation=3&delId="+Id,		   
		   success: function(msg){
			   	if(msg=="1"){
			   		$("#"+Id).remove();
			   	}else{
			   		asyncbox.tips('删除失败','error');
			   	}				   	      
		     //window.location.reload();
		   }
		});
	}	
}

/*复制模板*/
function copySubmit(Id,copyName){
	jQuery.ajax({
	   type: "POST",
	   url: "/CRMPrintSetAction.do?operation=1&addFlag=copy&id="+Id,		   
	   success: function(msg){		     
	     if(msg !=""){
	     	var tr ='<tr id="'+msg+'"><td class="t-l"><font color="#0099FF"><b>'+copyName+'</b></font></td>'
    			+'<td class="t-l"><a href="/CRMPrintSetAction.do?operation=4&styleFlag=NEW&flag=BYID&id='+msg+'">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;'
    			+'<a href="javascript:opSubmit(\''+msg+'\');">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;'
    			+'<a href="javascript:copySubmit(\''+msg+'\',\''+copyName+'\');"><font color="#0099FF"><b>复制</b></font></a></td></tr>';
	     	$("#myTable").append(tr);
	     }
	   }
	});													
}
</script>
</head>
<body>
<form action="" method="post" name="form" style="height:550px;overflow:auto;">

<div align="center">
  
  <table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
      <td width="60%" valign="top">
        <div align="center">
          
  <table border="0" cellspacing="0" cellpadding="0" style="line-height: 150%" width="80%" bordercolor="#808080">
    <tr>
      <td height="60" valign="bottom" align="center">
     <table border="0">
      <tr>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td><img border="0" src="/$globals.getStylePath()/images/Left/kdpr.png" /></td>
        <td><font color="#808080"><b>快递单打印模板设计</b></font></td>
      </tr>
     </table>
      </td>
    </tr>
    <tr>
      <td>
     <table border="0" width="100%">
      <tr>
        <td>
        </td>
      </tr>
      <tr>
        <td>
          <div align="center">
            
            <table border="0" cellpadding="5" cellspacing="5">
              <tr>
                <td valign="top" colspan="2">
                  <p align="center"><font color="#808080">操作过程举例：点击下列图片可看到大图</font></p></td>
              </tr>
              <tr>
                <td valign="top"><a href="/$globals.getStylePath()/images/Left/sto1.gif" target="_blank"><img border="1" src="/$globals.getStylePath()/images/Left/sto1.gif" width="350px" height="190px" /></a></td>
                <td valign="top"><a href="/$globals.getStylePath()/images/Left/sto2.gif" target="_blank"><img border="1" src="/$globals.getStylePath()/images/Left/sto2.gif" width="350px" height="190px" /></a></td>
              </tr>
              <tr>
                <td valign="top" bgcolor="#EBEBEB"><b>　1</b>.先把标尺打印到空白的快递单上(标尺在新建快递单页面)</td>
                <td valign="top" bgcolor="#EBEBEB"><b>　2</b>.打印标尺后的快递单</td>
              </tr>
              <tr>
                <td valign="top"><a href="/$globals.getStylePath()/images/Left/sto3.gif" target="_blank"><img border="1" src="/$globals.getStylePath()/images/Left/sto3.gif" width="350px" height="190px" /></a></td>
                <td valign="top"><a href="/$globals.getStylePath()/images/Left/sto4.gif" target="_blank"><img border="1" src="/$globals.getStylePath()/images/Left/sto4.gif" width="350px" height="190px" /></a></td>
              </tr>
              <tr>
                <td valign="top" bgcolor="#EBEBEB"><b>　3</b>.根据标尺确定内容区域，并设置每个区域的<b>x,y,高,宽</b>四个值</td>
                <td valign="top" bgcolor="#EBEBEB"><b>　4</b>.设置后，打印好的快递单</td>
              </tr>
            </table>
            
          </div>
        </td>
      </tr>
     </table>
      </td>
    </tr>
    <tr>
      <td>
	<table class="ViewTable" cellspacing="0" cellpadding="0" bordercolor="#CDCDCD" class="table-s00" align="center" style="table-layout:fixed;width:700px;">
		<thead><tr>
			<th><b>快递单模板名称</b></th>
			<th><b>操作</b></th>
		</tr></thead>
		<tbody id="myTable">
		#foreach($log in $!printList)		
		<tr id="$!log.id">
			<td class="t-l"><font color="#0099FF"><b>$!log.moduleName</b></font></td>
			<td class="t-l">			
			<a href="/CRMPrintSetAction.do?operation=4&styleFlag=NEW&flag=BYID&id=$!log.id">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
			#if("$!log.status" == "1")
			<a>（系统默认不可删除）</a>&nbsp;&nbsp;
			#else
			<a href="javascript:opSubmit('$!log.id');">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
			#end	
			<a href="javascript:copySubmit('$!log.id','$!log.moduleName');"><font color="#0099FF"><b>复制</b></font></a>		
			</td>
						
		</tr>
		#end	
		</tbody>
	</table>
      </td>
    </tr>
    <tr><td>
  <p align="center"><font color="#0099FF"><b><a href="/CRMPrintSetAction.do?operation=4&styleFlag=NEW"><img border="0" src="/$globals.getStylePath()/images/Left/sto5.gif" />新建快递单模板</a></b></font><font face="Arial"><b>&gt;&gt;</b></font></p> 
      </td></tr>
    <tr><td>
      <div align="center">
        
        <table border="0">
          <tr>
            <td align="left"><font color="#999999"><b></b><br/>
             <br/>
             </font></td>
          </tr>
        </table>
        
      </div>
      <hr size="1" color="#C0C0C0" />
      </td></tr>
  </table>
          
        </div>
      </td>
    </tr>
  </table>

  
</div>
</form>
</body>
</html>
