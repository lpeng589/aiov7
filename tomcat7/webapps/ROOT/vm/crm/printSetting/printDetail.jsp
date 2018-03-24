<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>快递单打印模板设置</title>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/style1/css/clientTransfer.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<style type="text/css">
body{background:url(/style/images/client/html_bg_f.png) repeat 0 0;}
.topHead{border-bottom:1px #a1a1a1 solid;width:980px;}
.position{position:absolute;left:0;background:#f2f2f2;width:1000px;}
.wt-add{position:relative;}
.wt-add input{width:100%;padding:0 20px 0 0;box-sizing:border-box;}
.wt-add b{position:absolute;width:16px;height:16px;display:inline-block;right:2px;top:2px;cursor:pointer;background:url(/style/images/item/icons.png) no-repeat -62px -196px;}
</style>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function backSubmit(){
	form.action="/CRMPrintSetAction.do?operation=4";
	form.submit();
}
function save(){
	var flagId = $("#flagId").val();
	var name = $("#namep").val();
	var moduleId = $("#ref_moduleId").val();
	var moduleViewId = $("#ref_moduleViewId").val();	
	if(moduleId ==""){
		alert("请选择模板");
		return false;
	}
	if(moduleViewId ==""){
		alert("请选择模板视图");
		return false;
	}
	if(name == ""){
		alert("快递单名称不能为空");
		return false;
	}
	if(flagId !=""){
		form.action="/CRMPrintSetAction.do?operation=2&id="+flagId;
	}
	form.submit();
}
$(function(){
	$("#form").scroll(function(){ 
		var topNum = $(this).scrollTop();
		if(topNum>=100){
			$(".topHead").addClass("position").css("top",topNum-55);
		}else{
			$(".topHead").removeClass("position");
		}
		
	});
})
//联动 模板获取模板视图
function getModuleView(){
	var moduleId = $("#ref_moduleId").val();
	if($("#ref_moduleViewId")){
		$("#ref_moduleViewId").remove();
	}
	jQuery.ajax({
		type:"post",
		url:"/CRMPrintSetAction.do?operation=4&styleFlag=CONNET&moduleId="+moduleId,
		success:function(msg){
			$("#ref_moduleId").after(msg);
		}
	})
}

var public_Id ="";
/*字段弹出框*/
function openFileName(fillId){
	public_Id = fillId;
	var moduleId = $("#ref_moduleId").val();
	if(moduleId == undefined || moduleId ==""){	
		asyncbox.tips('请选择模板','error');
		return false;
	}
	var moduleViewId = $("#ref_moduleViewId").val();
	if(moduleViewId == undefined || moduleViewId ==""){	
		asyncbox.tips('请选择模板视图','error');
		return false;
	}	
	var url ="/CRMPrintSetAction.do?operation=4&styleFlag=Filed&moduleViewId="+moduleViewId+"&moduleId="+moduleId;
	var title = "选择字段";						
	asyncbox.open({
	   id : 'fileddiv',
	   title : title,
	　 url : url,
	　 width : 350,
	　 height : 250,
	   btnsbar : jQuery.btn.OKCANCEL.concat([{
     		text    : '清空 ',                  
      		action  : 'resetBtn'             
  		}]),
	   callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var str = opener.filedSubmit();
				if(str == ""){
					asyncbox.tips('请选择一个名称','error');
					return false;
				}else{
					$("#"+fillId).val("【"+str+"】");
					jQuery.close('fileddiv');
				}				
　　　　　	}
			if(action == 'resetBtn'){									
				$("#"+fillId).val("");						
　　　　　	}		
　　　	}
　	});
}
/*双击回填字段*/
function fillData(datas){
	$("#"+public_Id).val("【"+datas+"】");
	jQuery.close('fileddiv');
}

function onloadCloumn(){
	var len = $('#mytody tr',document).length;	
	if(len < 20){
		for(var i=len+1;i<21;i++){
			var tr ='<tr>'
			+'<td ><input type="text" name="pName"  style="width:99%;"  /></td>'
	        +'<td ><input type="text" name="nameX"  style="width:99%;" /></td>'
	        +'<td ><input type="text" name="nameY"  style="width:99%;"  /></td>'
	        +'<td ><input type="text" name="widthX"  style="width:99%;"  /></td>'
	        +'<td ><input type="text" name="heightY"  style="width:99%;"  /></td>'
	        +'<td ><select size="1" name="fontSize">' 
		        	+'<option value=9>最小</option><option value=10 selected="selected">较小</option> <option value=12 >中</option> <option value=14>较大</option> <option value=15>大</option>'         	        
		    +'</select></td>'
	        +'<td >'
	        	+'<div class="wt-add">'
	        		+'<input type="text" name="context" id="'+i+'_context" ondblclick="openFileName(\''+i+'\'_context\');"/>'
	        		+'<b title="选择模板字段" onclick="openFileName(\''+i+'\'_context\');"></b>'
	       		+'<div></td></tr>';	
	       	$("#mytody").append(tr);
		}
		
	}
}
</script>
</head>
<body onload="showStatus();onloadCloumn();">
<form id="form" action="/CRMPrintSetAction.do" method="post" name="form" style="height:550px;overflow:auto;">
<input type="hidden" name="operation" value="1"/>
<input type="hidden" name="flagId" id="flagId" value="$!flagId"/>	
<div style="text-align:center;padding:15px 10px;width:1000px;margin:0 auto;background:#fff;">
    <b color="#808080" style="display:inline-block;line-height:25px;font-size:16px;">快递单打印模板设置</b>
</div>
<div style="width:1000px;margin:0 auto;position:relative;background:#fff;padding:0 10px;">

	<div class="topHead" style="padding:10px;overflow:hidden;height:25px;">
		<div style="float:left;display:inline-block;">
			<b style="color:red;">快递单名称：</b>
			<input type="text" name="name" id="namep" size="20" value="$!name" />
			<select name="ref_moduleId" id="ref_moduleId" onchange="getModuleView();">
				<option value="">模板选择</option>
				#foreach($log in $!moduleList)
				<option  value="$!globals.get($log,0)" #if("$!moduleId" == "$!globals.get($log,0)")selected="selected" #end>$!globals.get($log,1)</option>
				#end
			</select>
			#if("$!flagShow" == "yes")
			<select name="ref_moduleViewId" id="ref_moduleViewId">				
				#foreach($log in $!viewList)
				<option  value="$!globals.get($log,0)" #if("$!globals.get($log,0)" == "$!moduleViewId")selected="selected" #end>$!globals.get($log,1)</option>
				#end
			</select>
			#end
		</div>
		<div style="float:right;display:inline-block;">
			<div class="btn btn-mini" id="save" onclick="save();">保存 </div>    
			<input type="reset" class="btn btn-mini" style="font-family:microsoft yahei;" value="全部重写" />
			<div class="btn btn-mini" id="backId" onclick="backSubmit();">返回</div>
		</div> 
	</div>
	<p class="m_tb_12" style="padding:20px 0;">
		<b style="background:#d2d2d2;">1.在空白的快递单上打印座标尺，正常打印后效果如下所示</b> 
		<a href="/CRMPrintSetAction.do?operation=4&styleFlag=PREVIEW" target="_blank" style="color: #FF0000">
			<b>打印 标尺</b>
		</a>
		<b>&gt;&gt;</b>
	</p> 
	<div style="overflow:hidden;">
		<p style="float:left;text-align:center;width:350px;">
			<img style="padding:3px;background:#b2b2b2;" src="/$globals.getStylePath()/images/Left/yt1.png" />
			<span style="display:block;">空白快递单举例</span>
		</p>
		<p style="float:left;text-align:center;width:350px;margin:0 0 0 15px;">
			<img style="padding:3px;background:#b2b2b2;"  src="/$globals.getStylePath()/images/Left/yt2.png" />
			<span style="display:block;">打印标尺后的快递单举例</span>
		</p>
	</div>
	<div>
		<p class="m_tb_12" style="padding:20px 0;"><b style="background:#d2d2d2;">2.定义各个内容区域和尺寸-快递单内容设计</b>：(<a href="/CRMPrintSetAction.do?operation=4&styleFlag=TIAOZ" target="_blank" style="color: #FF0000">参考设置方法</a>)</p>
		<table border="0" width="100%" cellpadding="0" class="ViewTable" bordercolor="#CDCDCD">
	      <thead>
	      <tr>
	        <td width="20%" bgcolor="#C0C0C0" align="center" height="35">内容块名称</td>
	        <td width="6%" bgcolor="#C0C0C0" align="center">X</td>
	        <td width="6%" bgcolor="#C0C0C0" align="center">Y</td>
	        <td width="6%" bgcolor="#C0C0C0" align="center">宽</td>
	        <td width="6%" bgcolor="#C0C0C0" align="center">高</td>
	        <td width="6%" bgcolor="#C0C0C0" align="center">字号</td>
	        <td width="50%" bgcolor="#C0C0C0" align="center">内容参数</td>
	      </tr>
	      </thead>
			<tbody id="mytody">	
			#foreach($log in $!param)
			<tr>
	        <td ><input type="text" name="pName"  style='width:99%;' value="$!globals.get($log.split(','),0)" /></td>
	        <td ><input type="text" name="nameX"  style='width:99%;' value="$!globals.get($log.split(','),1)" /></td>
	        <td ><input type="text" name="nameY"  style='width:99%;' value="$!globals.get($log.split(','),2)" /></td>
	        <td ><input type="text" name="widthX"  style='width:99%;' value="$!globals.get($log.split(','),3)" /></td>
	        <td ><input type="text" name="heightY"  style='width:99%;' value="$!globals.get($log.split(','),4)" /></td>
	        <td >
	        #if("$!globals.get($log.split(','),5)" == "9")
	        <select size="1" name="fontSize">
	            <option value=9 selected="selected">最小</option><option value=10>较小</option> <option value=12 >中</option> <option value=14>较大</option> <option value=15>大</option>         
	        </select>
	        #elseif("$!globals.get($log.split(','),5)" == "12")
	        <select size="1" name="fontSize">
	        	<option value=9>最小</option><option value=10>较小</option> <option value=12 selected="selected" >中</option> <option value=14>较大</option> <option value=15>大</option>         	        
	        </select>       
	        #elseif("$!globals.get($log.split(','),5)" == "14")
	        <select size="1" name="fontSize">
	            <option value=9>最小</option><option value=10>较小</option> <option value=12 >中</option> <option value=14 selected="selected">较大</option> <option value=15>大</option>         
	        </select>
	        #elseif("$!globals.get($log.split(','),5)" == "15")
	        <select size="1" name="fontSize">
	            <option value=9>最小</option><option value=10>较小</option> <option value=12  >中</option> <option value=14>较大</option> <option value=15 selected="selected">大</option>         
	        </select>
	        #else
	        <select size="1" name="fontSize"> 
	        	<option value=9>最小</option><option value=10 selected="selected">较小</option> <option value=12 >中</option> <option value=14>较大</option> <option value=15>大</option>         	        
	        </select>
	        #end
	        </td>
	        <td >
	        	<div class="wt-add">
	        		<input type="text" name="context" id="${velocityCount}_context" value="$!globals.get($log.split(','),6)" ondblclick="openFileName('${velocityCount}_context');"/>
	        		<b title="选择模板字段" onclick="openFileName('${velocityCount}_context');"></b>
	        	<div>	
	        </td>
	      </tr>	
	      #end
			</tbody>	
	           
	     </table>
	</div>		  
</div>
</form>
</body>
</html>
