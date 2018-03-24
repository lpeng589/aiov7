<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>任务分配</title>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />

<link rel="stylesheet" href="/style1/css/CRselectBoxList.css" type="text/css"/>
<link rel="stylesheet" href="/style/css/base_button.css" type="text/css"/>

<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/jquery.divbox.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/public_utils.js"></script>
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


.HeadingButton  li{margin:0px 2px; padding:3px;}


#closeDiv{-webkit-transform:rotate(0deg);-webkit-transition:all 0.5s ease-in;}
#closeDiv:hover{-webkit-transform:rotate(360deg);}
.listRange_3 li p{width:100px;}

.listRange_3 .swa_c1{float:left;display:inline-block;line-height:25px;}
.listRange_3 .swa_c2{float:left;display:inline-block;padding:0 0 0 2px;}
.listRange_3 .swa_c3{float:left;display:inline-block;padding:2px 0 0 0;}
.listRange_3 .d_box{width:80px;float:left;display:inline-block;height:20px;overflow:hidden;word-spacing:-5px;}
.listRange_3 .d_test{float:none;}
.listRange_3 .ls3_in{margin:0 0 0 2px;}
.listRange_3 input.ls3_in{border:1px #a1a1a1 solid;box-sizing:border-box;}



</style>

<link rel="stylesheet" href="/style/css/oameeting/tip-yellow/tip-yellow.css"  type="text/css" />
<script src="/js/oa/titletip/jquery.poshytip.js" type="text/javascript" charset="utf-8"></script>

<script type="text/javascript" >
function detail(id){
    var url="/CRMDistWorkAction.do?operation=5&id="+id;
		asyncbox.open({
			id : 'warmsetting',url : url,title : '详情',
	　　 	width : 800, height : 500,
		    callback : function(action,opener){}
	 　 });
}


function updatePrepare(id){
    var url="/CRMDistWorkAction.do?operation=7&id="+id;
	asyncbox.open({
		id:'addDistwork',url:url,title:'修改',width:780,height:400,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　	}
　  });
}

function addPrepare(){
    var url="/vm/crm/distwork/add.jsp";
	asyncbox.open({
		id:'addDistwork',url:url,title:'添加',width:780,height:400,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　	}
　  });
}




function deletedistwork(){
	var sizecheck=$("input[name='keyId']:checked").size();
	if(sizecheck==0){
		asyncbox.alert("至少选择一个选项","提示");
		return false;
	}
	var url="/CRMDistWorkAction.do?operation=3&"+$("form").serialize();
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
	
	
$(document).ready(function(){
    $("#queryCondition").click(function() { 
 		$('#condtionsList').OpenDiv(); 
 		$('#btnConfirmSCA').focus() ;
 });
 $("#draftCondition").click(function() {
 		$("#draftConfirmSCA").show() ;
 		$("#btnConfirmSCA").hide() ;
 	  	$('#condtionsList').OpenDiv(); 
 	  	$('#draftConfirmSCA').focus() ;
 });
 $("#btnConfirmSCA").click(function() {
 		if( beforeButtonQuery()) {
 			submitQuery(); 
 			$("#condtionsList").CloseDiv();
 		} 
 	});
 	$("#draftConfirmSCA").click(function() {
 		if( beforeButtonQuery()) {
 			document.forms[0].operation.value=4;
 	  	  	document.forms[0].draftQuery.value='draft';
 	  	  	document.forms[0].submit();   
 			$("#condtionsList").CloseDiv();
 		} 
 	});
 $("#btnCancelSCA").click(function() { $("#condtionsList").CloseDiv();  });
 $("#closeDiv").click(function() { $("#condtionsList").CloseDiv(); });
 
 });


function userbox(userGroup,userName,createBy){
	usertype=createBy;
	deptPopForAccount(userGroup,userName,createBy);
}

//处理双击个人部门弹出框

var usertype="";
function fillData(datas){
	if(usertype=="userId"){
		$("#userId").val(datas.split(";")[0])
		$("#userName").val(datas.split(";")[1])
	}else{
		$("#createUserId").val(datas.split(";")[0])
		$("#createUserName").val(datas.split(";")[1])
	}
	
	jQuery.close('Popdiv');
}

//处理按钮
function handle(rowid){
	asyncbox.open({
		id:'handlebox',
		btnsbar : jQuery.btn.OKCANCEL,
		　　　html : "<textarea id='resulttext' cols='57'  rows='10' ><\/textarea>",
		   width : 450, height : 250,title:'行动描述', 
		   callback : function(action,opener){
			  
			   if(action=="ok"){
				  var str=$("#resulttext").val();
				  if(str.trim() == ""){
					  alert("原因不能为空");
					  return false;
				  }
				  str= encodeURIComponent(str);
				 var  url="/CRMDistWorkAction.do?operation=4&requestType=handle&workid="+rowid+"&result="+str;
				  jQuery.ajax({
							type: "GET",
						url:url,
							success: function(msg){
								
							 if(msg == "yes"){
								 asyncbox.tips("操作成功",'success');

                                   
									}else{
										asyncbox.tips("操作失败",'error');
									}
		 				}
				});
			   }
		   }
		　});

}




/*--------------------xjj--------------------------------*/
function dealAsyn(){
	window.location.reload();
}














</script>
</head>
<body>
<form action="/CRMDistWorkSearchAction.do" method="post" name="form">
 <input type="hidden" name="operation" value="4"/>


<div class="Heading">
		<div class="HeadingIcon"><img src="/style1/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">任务分派 </div>
		

    
    
    
  <ul class="HeadingButton">
					<li>
									<button type="button" id="queryCondition" >条件查询</button>				
							</li> 		
							<li><button type="button" onclick="javascript:window.location.reload();">刷新</button></li>
					
				
															<!--工作流类型设置不需要添加按钮-->		
						<li><button type="button" id="addPre" name="addPre" id="addPre"  title="Ctrl+D" onClick="addPrepare();" class="b2">添加</button></li>
																			<li><button type="button"  onClick="deletedistwork();" class="b2">删除</button></li>
					
										
											</ul>
 
 </div>



<div id="contentsDiv" style="overflow: auto;margin: 0 auto; width: 95%;">
<table class="ViewTable" border="0" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD">
	<thead>
		<tr>
		<td style="width:30px;">No</td>
		<td  style="width:30px;"><input type="checkbox" name="selAll" onClick="checkAll('keyId')" /> </td>
		<td style="width:150px;">关联客户</td>
		<td style="width:100px;">分类</td>
		<td style="width:100px;">内容</td>
		<td style="width:50px;">分派者</td>	
		<td style="width:50px;">处理者</td>
		<td style="width:50px;">状态</td>
		
		<td style="width:120px;">创建时间</td>				
		<td>操作</td> 	
		</tr>
	</thead>
	<tbody>
	 #foreach($row in $list)
	 
		<tr onclick="checkTr('$row.id')">
		<td style="width:30px;text-align:center;"  >$velocityCount</td>
		
		<td class="stoptd" style="text-align:center;"><input id="Input$row.id" name="keyId" type="checkbox" value="$row.id" delfile="$row.attachment"/></td>
		<td ><a title="$row.ref_id" style="display:block;width:150px;overflow:hidden;text-overflow:ellipsis; white-space:nowrap;">$row.ref_id</a></td>
		<td >
		<a title="$globals.getEnumerationItemsDisplay("CRMTaskType","$row.taskType")" style="display:block;width:50px;overflow:hidden;text-overflow:ellipsis; white-space:nowrap;">$globals.getEnumerationItemsDisplay("CRMTaskType","$row.taskType")</a>
		</td>
		<td >
		<a title="$row.content" style="display:block;width:300px;overflow:hidden;text-overflow:ellipsis; white-space:nowrap;">$row.content</a>
		</td>
		<td >
		$!globals.getEmpFullNameByUserId($row.createBy)
		</td>	
		<td>
		<a title="
		#foreach($str in $row.userId.split(","))
		$!globals.getEmpFullNameByUserId($str) ;
		#end
		" style="display:block;width:150px;overflow:hidden;text-overflow:ellipsis; white-space:nowrap;">#foreach($str in $row.userId.split(","))
		$!globals.getEmpFullNameByUserId($str) ;
		#end</a>
		</td>
		<td >

		#if($row.statusId==0)
		    #if("$!row.userId"=="")
		   		未分派

		   	#else
		   	    未结束	
			#end
		#elseif($row.statusId==1)
		    已结束

		#elseif($row.statusId==2)
		  取消
		#end
		</td>
		
		<td >$row.createTime</td>				
		<td class="stoptd" > 
		#if("$row.createBy"=="$self")
		<a href="javascript:void(0);"  onclick="updatePrepare('$row.id')">修改</a> 
		#end
		    <a href="javascript:void(0);"  onclick="detail('$row.id')">详情</a>
	     #set($userIds=",$row.userId,")
		 #set($temp=",$self,")  
		 #if( $userIds.indexOf($temp)!=-1) 
		 #set($fin=";$self;")  
		    #if($row.finishUser.indexOf($fin)!=-1) 
		                    已完成

		    #else
		   <a href="javascript:void(0);"  onclick="handle('$row.id');">完成</a>
		    
		   #end
		  #end 
		     
		    
		    </td> 	
		</tr>
		#end
	</tbody>	
</table>

</div>
<div class="listRange_pagebar">
	$!pageBar
</div>	

<div class="listRange_frame" id="listRange_frame">
<div id="condtionsList" style="margin-top:20px; border:5px #8FCFFF solid;box-shadow:#666 0px 0px 10px;">
		<div style="background:#8FCFFF;margin-bottom:10px; width:587px;height:30px;">
		 <div style="float: left;line-height:25px; margin-left:10px;font-weight: bold;color:#333;">查询条件 </div>
		 <div style="margin-top:3px;float:right;text-align: right;margin-right: 5px;"><img id="closeDiv" style="cursor: pointer;" src="/style1/images/bottom/Close.png"/></div>
		</div>
		<div class="listRange_3" id="listid" style="background:none;border:none;">
								<li><div class="swa_c1"><div class="d_box"><div class="d_test">关联客户</div></div><div class="d_mh">：</div></div><input class="ls3_in" id="ClientName" name="clientName" value="$!CRMDistWorkSearchForm.clientName" onKeyDown="if(event.keyCode==13) {form.submit();}" ></li>
							<li><div class="swa_c1"><div class="d_box"><div class="d_test">分类</div></div><div class="d_mh">：</div></div>
							
							<select class="ls3_in" id="taskType" name="taskType" >
				<option value="" ></option>									
				#foreach($item in $globals.getEnumerationItems("CRMTaskType"))	 
<option value="$item.value"#if("$!CRMDistWorkSearchForm.taskType" == "$item.value") selected="selected" #end>$item.name</option>
#end

							
								</select>
								
								</li>
							<li><div class="swa_c1"><div class="d_box"><div class="d_test">内容</div></div><div class="d_mh">：</div></div><input class="ls3_in" id="content" name="content" value="$!CRMDistWorkSearchForm.content" onKeyDown="if(event.keyCode==13) {form.submit();}" ></li>
								<li><div class="swa_c1"><div class="d_box"><div class="d_test">分派状态</div></div><div class="d_mh">：</div></div><select class="ls3_in" id="assignStatus" name="assignStatus" >
				               <option value="" ></option>									
				            <option  #if("$!CRMDistWorkSearchForm.assignStatus"=="0") selected="selected"  #end title="已分派" value="0" >未分派</option>
									<option  #if("$!CRMDistWorkSearchForm.assignStatus"=="-1") selected="selected"  #end title="未分派" value="-1" >未结束</option>
								<option  #if("$!CRMDistWorkSearchForm.assignStatus"=="-1") selected="selected"  #end title="未分派" value="-1" >已结束</option>
								<option  #if("$!CRMDistWorkSearchForm.assignStatus"=="-1") selected="selected"  #end title="未分派" value="-1" >已取消</option>
								
								</select></li>
								
								
								<li><div class="swa_c1"><div class="d_box"><div class="d_test">处理者</div></div><div class="d_mh">：</div></div>
								<input type="hidden"  id="userId" name="userId" value="$!CRMDistWorkSearchForm.userId"/>
								<input id="userName" class="ls3_in" name="userName" value="$!CRMDistWorkSearchForm.userName" onKeyDown="if(event.keyCode==13) {form.submit();}" onDblClick="usertype='yyy';userbox('userGroup','userName','userId');"  ><img src="/style1/images/St.gif" onClick="userbox('userGroup','userName','userId');"/>
								
								</li>	
								
								<li><div class="swa_c1"><div class="d_box"><div class="d_test">分派者</div></div><div class="d_mh">：</div></div>
								<input type="hidden"  id="createUserId" name="createUserId" value="$!CRMDistWorkSearchForm.createUserId"/>
								<input id="createUserName" class="ls3_in" name="createUserName" value="$!CRMDistWorkSearchForm.createUserName" onKeyDown="if(event.keyCode==13) {form.submit();}" onDblClick="userbox('userGroup','createUserName','createUserId');"  ><img src="/style1/images/St.gif" onClick="userbox('userGroup','createUserName','createUserId');"/>
								
								</li>
										
							
						
								<li><div class="swa_c1"><div class="d_box"><div class="d_test">创建时间≥</div></div><div class="d_mh">：</div></div><input class="ls3_in" type="text" name="createStartTime" value="$!CRMDistWorkSearchForm.createStartTime"  onKeyDown="if(event.keyCode==13) {form.submit();}" onclick="WdatePicker({lang:'zh_CN'});"></li>			
								<li><div class="swa_c1"><div class="d_box"><div class="d_test">创建时间≤</div></div><div class="d_mh">：</div></div><input class="ls3_in" type="text" name="createEndTime" value="$!CRMDistWorkSearchForm.createEndTime"  onKeyDown="if(event.keyCode==13) {form.submit();}" onclick="WdatePicker({lang:'zh_CN'});"></li>			
			
			</div>
			
			<div style="float:left; width:99%; margin:5px; padding:5px; text-align: center;">
            <button type="button" id="btnConfirmSCA"  class="b3"  onclick="form.submit();">确定</button>
            <button type="button" id="btnCancelSCA" class="b3">关闭</button>
        	</div>
</div>





<script type="text/javascript">
	var oDiv=document.getElementById("contentsDiv");
	var sHeight=document.documentElement.clientHeight-130;
	oDiv.style.height=sHeight+"px";
</script>
</form>
</body>
</html>
