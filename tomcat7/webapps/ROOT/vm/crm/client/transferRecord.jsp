<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户转换记录</title>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
body,div,form,p,input,button,h1,h2,h3,h4,h5,h6,span,img,font,ul,li,ol,a,table,thead,tboady,tr,td{margin:0;padding:0;}
body{font-size:12px;font-family:微软雅黑;/*background:#FBEAE1;*/}
li{list-style:none;}

.h_div{width:95%;margin:0 auto;padding:5px 0 5px 0;overflow:hidden;}
.h_div_l{float:left;display:inline;padding:0 0 0 0;overflow:hidden;}
.h_div_l .img_div{width:58px;height:26px;background:url(/style1/images/Left/Icon_1.gif) no-repeat 0 0;float:left;display:inline-block;}
.h_div_l .t_span{height:25px;float:left;display:inline-block;border-bottom:1px #81B2E3 solid;line-height:25px;padding:0 10px 0 10px;color:#767271;font-weight:bold;}
.h_div_l .t_sl_div{float:left;display:inline-block;padding:0 0 0 10px;overflow:hidden;}
.h_div_l .t_sl_div .sl{width:100px;margin:5px 0 0 0;}
.h_div_r{float:right;display:inline;padding:0 0 0 0;overflow:hidden;}
.h_div_r .s_btn{height:24px;padding:0 10px 0 10px;background:url(/style/images/client/transferRecordBG.jpg) repeat-x 0 -30px;border:1px #000 solid;color:#fff;font-size:12px;font-family:微软雅黑;line-height:21px;cursor:pointer;}
.h_div_r .div_r_u{padding:0 0 0 0;overflow:hidden;}
.h_div_r .div_r_u .div_r_li{float:left;display:inline;padding:0 0 0 0;overflow:hidden;margin:0 0 0 5px;}

.hs_div{width:95%;margin:0 auto;padding:0 0 2px 0;overflow:hidden;border-bottom:1px #a1a1a1 dashed;}
.hs_div .s_btn{float:left;display:inline-block;padding:0 5px 0 5px;margin:5px 0 0 10px;height:20px;color:#fff;font-size:10px;line-height:18px;border:1px #000 solid;background:url(/style/images/client/transferRecordBG.jpg) repeat-x 0 -35px;cursor:pointer;}


.type_div{padding:8px 0 8px 10px;overflow:hidden;font-size:12px;border-right:1px #a1a1a1 solid;float:left;display:inline-block;}
.type_div .type_d_span{float:left;display:inline-block;line-height:13px;}
.type_div .type_u{float:left;display:inline-block;}
.type_u .type_li{float:left;display:inline-block;margin:0 10px 0 0;}
.type_u .type_li input{float:left;display:inline-block;}
.type_u .type_li a{float:left;display:inline-block;line-height:13px;margin:0 0 0 5px;}
.type_u .type_li .t_label{float:left;display:inline-block;line-height:12px;margin:0 0 0 5px;}

.k_div{padding:5px 10px 4px 0;overflow:hidden;font-size:12px;float:left;display:inline-block;border-right:1px #a1a1a1 solid;}
.k_div .k_d_span{float:left;display:inline-block;line-height:20px;}
.k_div .k_i_div{width:200px;height:18px;border:1px #000 solid;float:left;display:inline-block;}
.k_div .k_i_div input{height:18px;width:180px;border:none;float:left;display:inline-block;}
.k_div .k_i_div a{width:20px;height:18px;float:left;display:inline-block;background:url(/style/images/client/se.png) no-repeat 0 -1px;cursor:pointer;}

.time_div{padding:5px 10px 4px 10px;overflow:hidden;font-size:12px;float:left;display:inline-block;border-right:1px #a1a1a1 solid;}
.time_div .t_d_span{float:left;display:inline-block;line-height:20px;}
.time_div .t_i_div{float:left;display:inline-block;padding:0 0 0 0;overflow:hidden;}
.time_div .t_i_div .t_time{width:100px;height:18px;border:1px #000 solid;float:left;display:inline-block;}
.time_div .t_i_div .t_i_d_span{float:left;display:inline-block;padding:0 5px 0 5px;line-height:20px;}

.cz_div{width:95%;margin:0 auto;padding:5px 0 5px 0;overflow:hidden;line-height:20px;}
.cz_div .cz_span{float:left;display:inline-block;width:5%;;padding:4px 0 0 0;background:url(/style/images/client/Add.gif) no-repeat 40px 9px;cursor:pointer;}
.cz_div .cz_u{float:left;display:inline-block;cpadding:0 0 0 0;overflow:hidden;width:93%;}
.cz_div .cz_u .cz_li{float:left;display:inline-block;margin:5px 5px 0 0;padding:0 2px 0 2px;cursor:pointer;border:1px #a1a1a1 dashed;}
.cz_div .cz_u .cz_li .txt_a{float:left;display:inline-block;}
.cz_div .cz_u .cz_li .close_a{float:left;display:inline-block;width:10px;height:20px;background:url(/style/images/client/CloseBtn.png) no-repeat 0 6px;margin:0 0 0 5px;cursor:pointer;}

.ViewTable{border-top:1px #cdcdcd solid;border-left:1px #cdcdcd solid;width:100%;margin:0 auto;}
.ViewTable tr td{padding:0 5px 0 5px; height:28px; line-height:28px;border-bottom:1px #cdcdcd solid;border-right:1px #cdcdcd solid;}
.ViewTable tr td.detail{color:#0179BB;}
.ViewTable tr td.detail:hover{color:#F30;}
.ViewTable thead tr{background:url(/style/images/client/transferRecordBG.jpg) repeat-x 0 -54px; text-align:center;}
.ViewTable tbody tr td {text-align:left;}
.ViewTable tbody tr:hover{background:#C4D3DD;}

.detail{cursor: pointer;}
</style>

<script type="text/javascript">
$(function(){
	$('.cz_u .cz_li').live('mouseover mouseout', function(event) {
	    if (event.type == 'mouseover') {
	   		$(this).find(".close_a").css("display","inline-block");
	    } else {
	    	$(this).find(".close_a").css("display","none");
	    }
	});
	
	$('.close_a').live('click', function() {
		var delId = $(this).parent().attr("id");
		delId = ","+delId+",";
		var ids =","+$("#"+$(this).parent().attr("popName")+"Ids").val();//隐藏域的值




		ids = ids.replace(delId,",");//替换删除的ID
		$("#"+$(this).parent().attr("popName")+"Ids").val(ids);
		$(this).parent().remove();
	});
})

function querySubmit(){
	form.submit();
}


var popHideName;//存放弹出框名称 
/*个人与部门弹出框*/
function popSelect(popName,obj){
	popHideName = popName;
	var title = "";
	if(popName == "userGroup"){
		title = "选择个人"
	}else if(popName == "CrmClickGroup"){
		title = "选择客户"
	}
	
	var url ="/Accredit.do?popname="+popName+"&inputType=checkbox&chooseData="+$("#"+popName+"Ids").val();
	asyncbox.open({
		id : 'Popdiv',url : url,title : title,width :755,height : 450,
		btnsbar :[{text:'清空',action:'selectAll'}].concat(jQuery.btn.OKCANCEL),
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		var selIds = "";//ids
	    		var showUsers = "";//封装显示内容
	    		for(var i=0;i<str.split("|").length-1;i++){
	    			var arrVal = str.split("|")[i];
	    			showUsers +='<li class="cz_li" id="'+arrVal.split(";")[0]+'" popName="'+popName+'"><a class="txt_a">'+arrVal.split(";")[1]+'</a><a class="close_a" style="display:none;"></a></li>'
		    		selIds +=arrVal.split(";")[0] + ",";
	    		}
	    		
	    		jQuery("#"+popName+"Ids").val(selIds);//popName + Ids,把选择的IDS存放到隐藏域提交后台查询
	    		//先删除所有内容




	    		jQuery("#"+popName+"UL li").remove();
	    		//添加内容
	    		jQuery("#"+popName+"UL").append(showUsers);
	    	} 
			if(action == 'selectAll'){
				//先将td内容清空
				$("#"+popName+"UL li").remove();
				$("#"+popName+"Ids").val("");//把隐藏域ID清空
			}
			querySubmit();
	    }
　  });
}

/*双击选择个人或部门*/
function fillData(popValues){
	//如果已存在,删除选项
	if($("#"+popValues.split(";")[0]).length == 1){
		//清掉选项ID
		var ids = "," + $("#"+popHideName+"Ids").val();
		ids = ids.replace(","+popValues.split(";")[0]+",",",");
		$("#"+popHideName+"Ids").val(ids)
		$("#"+popValues.split(";")[0]).remove();
	}else{
		var str = '<li class="cz_li" id="'+popValues.split(";")[0]+'" popName="'+popHideName+'"><a class="txt_a">'+popValues.split(";")[1]+'</a><a class="close_a" style="display:none;"></a></li>'
		$("#"+popHideName+"Ids").val($("#"+popHideName+"Ids").val()+popValues.split(";")[0]+",");
		$("#"+popHideName+"UL").append(str);
	}
	querySubmit();
	jQuery.close('Popdiv');
}

//详情
function detail(transferType,id,obj){
	var clientName = $(obj).attr("clientName");//客户名称
	var url = "";
	if(transferType == 2){
		//表示erp转Crm 跳转到CRM详情
		url = '/CrmTabAction.do?operation=5&keyId='+id;
	}else{
		//跳转到ERP的基础信息客户的详情



		var classCode = $(obj).attr("classCode");
		url = '/UserFunctionAction.do?tableName=tblCompany&keyId='+id+'&moduleType=2&operation=5&winCurIndex=2&parentCode='+classCode
	}
	mdiwin(url,clientName);
}

function mdiwin(url,title){
	top.showreModule(title,url);
}
</script>
</head>
<body>
<form action="/CRMClientAction.do" method="post" name="form">
<input type="hidden" name="operation" value="4"/>
<input type="hidden" name="type" value="transferRecord"/>
<input type="hidden" name="userGroupIds" id="userGroupIds" value="$!valueMap.get('userGroupIds')"/>


<div class="h_div">
	<div class="h_div_l">
  	<div class="img_div"></div>
    <span class="t_span">客户转换记录</span>
  </div>
  <div class="h_div_r">
  </div>
</div>
<div class="hs_div">
  <div class="k_div">
  	<span class="k_d_span">客户名称：</span>
  	<div class="k_i_div">
  		<input type="text" name="clientName" value="$!valueMap.get('clientName')" onKeyDown="if(event.keyCode==13) querySubmit();"/> 
      <a onclick="querySubmit();"></a>
    </div>
  </div>
  
  <div class="time_div">
  	<span class="t_d_span">同步时间：</span>
  	<div class="t_i_div">
  		<input type="text" name="startTime" class="t_time" value="$!valueMap.get('startTime')" class="inp_w inp_date"   onClick="WdatePicker({lang:'$globals.getLocale()'})"/> 
  		 <span class="t_i_d_span">至</span>
  		<input type="text" name="endTime" class="t_time" value="$!valueMap.get('endTime')" class="inp_w inp_date" onClick="WdatePicker({lang:'$globals.getLocale()'})"/> 
    </div>
  </div>
  
  <div class="type_div">
  	<span class="type_d_span">转换类型：</span>
  	<ul class="type_u">
      <li class="type_li">
      <input type="radio" id="transferType0" value="0" #if("$!valueMap.get('transferType')" == "0") checked="checked" #end name="transferType" onKeyDown="if(event.keyCode==13) querySubmit();"/><label class="t_label" for="transferType0">全部</label>			
      </li>
      <li class="type_li">
        <input type="radio" id="transferType1" value="1" #if("$!valueMap.get('transferType')" == "1") checked="checked" #end name="transferType" onKeyDown="if(event.keyCode==13) querySubmit();"/><label class="t_label" for="transferType1">CRM转ERP</label>				
      </li>
      <li class="type_li">
        <input type="radio" id="transferType2" value="2" #if("$!valueMap.get('transferType')" == "2") checked="checked" #end name="transferType" onKeyDown="if(event.keyCode==13) querySubmit();"/><label class="t_label" for="transferType2">ERP转CRM</label>	
      </li>
    </ul>
  </div>
  <input class="s_btn" type="button" value="查 询" onclick="querySubmit()" />
</div>
<div class="cz_div">
  <span class="cz_span" onclick="popSelect('userGroup',this)">操作人</span>
  <ul class="cz_u" id="userGroupUL">
  	#foreach($employeeId in $!valueMap.get('userGroupIds').split(","))
  		#if("$!employeeId" != "")
  		<li class="cz_li" id="$employeeId" popName="userGroup">
	        <a class="txt_a">$!globals.getEmpFullNameByUserId($employeeId)</a>
	        <a class="close_a" style="display:none;"></a>
	    </li>
	    #end
  	#end
  </ul>
</div>







<div id="contentsDiv" style="overflow: auto;margin: 0 auto; width: 95%;">
<table class="ViewTable" border="0" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD">
	<thead>
		<tr>
		<td>序号</td>
		<td>客户名称</td>
		<td>同步时间</td>
		<td>转换类型</td>
		<td>操作人</td>	
		<td>IP地址</td>
		<td>客户模板</td>				
		<td>操作</td> 	
		</tr>
	</thead>
	<tbody>
		#foreach($record in $recordList)
			#if(("$!globals.get($record,3)" == 1 && "$!globals.get($record,6)" == "") || ("$!globals.get($record,3)" == 2 && "$!globals.get($record,5)" == ""))
			#else
				#set($transferTypeName = "CRM转ERP")
				#if("$globals.get($record,3)" == 2)
					#set($transferTypeName = "ERP转CRM")
				#end
				<tr>
					#set($clientName = "")
					#if("$!globals.get($record,3)" == 1) 
						#set($clientName = $!globals.get($record,6))
					#else 
						#set($clientName = $!globals.get($record,5))
					#end
					<td>$velocityCount</td>
					<td>$clientName</td>
					<td>$!globals.get($record,2)</td>
					<td>$!transferTypeName</td>
					<td>$!globals.getEmpFullNameByUserId($globals.get($record,1))  </td>
					<td>$!globals.get($record,4)</td>		
					<td>#if("$globals.get($record,3)" == 2) $!globals.get($record,9) #else 无模板 #end</td>
					<td class="detail" onclick="detail('$globals.get($record,3)','$globals.get($record,7)',this)" classCode='$!globals.get($record,8)' clientName='$clientName'>详情</td>		
				</tr>	
			#end
		#end
	</tbody>	
</table>	
</div>
<div class="listRange_pagebar">
	$!pageBar
</div>

<script type="text/javascript">
	var oDiv=document.getElementById("contentsDiv");
	var sHeight=document.documentElement.clientHeight-130;
	oDiv.style.height=sHeight+"px";
</script>
</form>
</body>
</html>
