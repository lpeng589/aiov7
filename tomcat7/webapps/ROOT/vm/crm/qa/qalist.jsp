<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>QA库</title>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
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

<link rel="stylesheet" href="/style/css/oameeting/tip-yellow/tip-yellow.css"  type="text/css" />
<script src="/js/oa/titletip/jquery.poshytip.js" type="text/javascript" charset="utf-8"></script>

<script type="text/javascript" >
function detail(id){
    var url="/CRMQAAction.do?operation=5&id="+id;
		asyncbox.open({
			id : 'warmsetting',url : url,title : 'QA详情',
	　　 	width : 800, height : 500,
		    callback : function(action,opener){}
	 　 });
}


function updatePrepare(id){
    var url="/CRMQAAction.do?operation=7&id="+id;
		asyncbox.open({
			
			id : 'updatebox',url : url,title : 'QA修改',
	　　 	width : 800, height : 500,
		    callback : function(action,opener){}
	 　 });
}

function addPrepare(){
    var url="/CRMQAAction.do?operation=6";
		asyncbox.open({
			
			id : 'updatebox',url : url,title : 'QA新增',
	　　 	width : 800, height : 550,
		    callback : function(action,opener){}
	 　 });
}




function deleteqa(){
	var sizecheck=$("input[name='keyId']:checked").size();
	if(sizecheck==0){
		asyncbox.alert("至少选择一个选项","提示");
		return false;
	}
	var url="/CRMQAAction.do?operation=3&"+$("form").serialize();
    asyncbox.confirm('确定删除？','警告',function(action){
　　　                        if(action == 'ok'){
　　　                        	var delfile="";
　　　                        	$("input[name='keyId']:checked").each(function(i,self){
　　　                        		delfile += $(self).attr("delfile");
　　　                        	});
　　　                        	url = url + "&delfile="+encodeURIComponent(delfile);
　　　　                          		jQuery.ajax({
   								type: "GET",
  								url:url,
   								success: function(msg){
    								 if(msg == "yes"){
    									 asyncbox.alert('删除成功','提示',function(){form.submit();});

                                             
       									}else{
       										asyncbox.tips("删除失败",'error');
       									}
  				 				}
						});
　　　                       }
　       });  
}



function checkTr(labelId){
	var inputId="Input"+labelId;
	if($("#"+inputId).is(":checked")){
		$("#"+inputId).removeAttr("checked");
	}else{
		$("#"+inputId).attr("checked","checked");
	}
}

$(function(){
	$(".stoptd").each(function(i,w){
		w.onclick=function(e){
			e.stopPropagation();
		}
		
	});
});





$(function (){
	   $("a[title]").poshytip();
	});
</script>
</head>
<body>
<form action="/CRMQASearchAction.do" method="post" name="form">
 <input type="hidden" name="operation" value="4"/>


<div class="h_div">
	<div class="h_div_l">
  	<div class="img_div"></div>
    <span class="t_span">QA库</span>
  </div>
  <div class="h_div_r">
  </div>
</div>
<div class="hs_div">
  <div class="k_div">
  	<span class="k_d_span">问题：</span>
  	<div class="k_i_div">
  	   <input name="ref_id" type="text" value="$!CRMQASearchForm.ref_id" />
    </div>
  </div>
  <div class="k_div">
  	<span class="k_d_span">解答：</span>
  	<div class="k_i_div">
  		<input name="answer" type="text" value="$!CRMQASearchForm.answer" />
    </div>
  </div>
  
  <div class="time_div">
  	<span class="t_d_span">创建时间：</span>
  	<div class="t_i_div">
  		<input type="text" name="createStartTime" class="t_time" value="$!CRMQASearchForm.createStartTime" class="inp_w inp_date"   onClick="WdatePicker({lang:'$globals.getLocale()'})"/> 
  		 <span class="t_i_d_span">至</span>
  		<input type="text" name="createEndTime" class="t_time" value="$!CRMQASearchForm.createEndTime" onClick="WdatePicker({lang:'$globals.getLocale()'})"/> 
    </div>
  </div>
  
  <input class="s_btn" type="submit" value="查 询"  />
</div>
<div class="cz_div">
 #if($add)
  <span onclick="addPrepare()">新增</span>
  #end
  #if($del)
  <span onclick="deleteqa()">删除</span>
  #end
</div>







<div id="contentsDiv" style="overflow: auto;margin: 0 auto; width: 95%;">
<table class="ViewTable" border="0" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD">
	<thead>
		<tr>
		<td  style="width:30px;"><input type="checkbox" name="selAll" onClick="checkAll('keyId')" /> </td>
		<td style="width:350px;">问题</td>
		<td style="width:500px;">解答</td>
		<td style="width:50px;">创建人</td>
		<td style="width:120px;">创建时间</td>	
		<td style="width:50px;">检索次数</td>				
		<td>操作</td> 	
		</tr>
	</thead>
	<tbody>
	 #foreach($row in $list)
		<tr onclick="checkTr('$row.id')">
		<td class="stoptd" style="text-align:center;"><input id="Input$row.id" name="keyId" type="checkbox" value="$row.id" delfile="$row.attachment"/></td>
		<td><a title="$row.ref_id" style="display:block;width:350px;overflow:hidden;text-overflow:ellipsis; white-space:nowrap;">$row.ref_id</a></td>
		<td><a title="$row.answer" style="display:block;width:500px;overflow:hidden;text-overflow:ellipsis; white-space:nowrap;">$row.answer</a></td>
		<td>$!globals.getEmpFullNameByUserId($row.createBy)</td>
		<td>$row.createTime</td>	
		<td style="text-align:center;" >$row.times</td>				
		<td class="stoptd" > 
		#if($update)
		<a href="javascript:void(0);"  onclick="updatePrepare('$row.id')">修改</a> 
		#end
		    <a href="javascript:void(0);"  onclick="detail('$row.id')">详情</a></td> 	
		</tr>
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
