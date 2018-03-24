<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会议室管理</title>
<link type="text/css" rel="stylesheet" href="/style1/css/meeting.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/listPage.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" >
function del(id,name,self){
	var url="/OABoardroom.do?operation=30&keyId="+id;
	jQuery.ajax({
		type: "GET",
		url: url ,
		success: function(msg){
			if(msg != "0"){
				var str="与这些会议室相关的会议有"+msg+"个,不能删除会议室";
     			asyncbox.tips(str,'error');				
			}else{
				if(confirm("删除会议室?")){
					url="/OABoardroom.do?operation=3&requestType=DELETE&boardroomId="+id+"&boardroomName="+name;
					jQuery.ajax({
					   type: "GET",
					   url: url ,
					   success: function(msg){
					       if(msg == "yes"){
					　　　　  asyncbox.tips('删除成功','success');
					          $(self).parent().parent().remove();
					       }else{
					       	  asyncbox.tips('删除失败','error');
					       }
					   }
					});
				}
			}
		}
	});
}

function formdel(){		
	if(!isChecked("keyId")){ alert('请选择至少一条记录'); return false; } 	
	var url="/OABoardroom.do?operation=30&"+$("form").serialize();
	jQuery.ajax({
   		type: "GET",
  	 	url: url ,
   		success: function(msg){    				
     				if(msg != "0"){
     					var str="与这些会议室相关的会议有"+msg+"个,不能删除会议室";
     					asyncbox.tips(str,'error');    					
     				}else{  
     					if(confirm("删除会议室?")){
     						form.submit(); 	
     					}   											 
											
     				}			
   		}
	});

	
}

function week(id){
 var start=new Date();
window.location.href = "/Meeting.do?operation=4&boardroomIndex=boardroomIndex&conEnter=true&requestType=BOARDROOMWEEK&boardroomId="+id+"&meetingStartTime="+start.getFullYear()+"-"+(start.getMonth()+1)+"-"+start.getDate();
}
jQuery(document).ready(function(){
	jQuery("#addBtn").click(function(){
		var height = 340;	
		var url = "/OABoardroom.do?operation=6";
		asyncbox.open({
			id:'adddiv',url:url,title:'新增会议室',width:540,height:height,cache:false,modal:true			  
	　  });
	});
	jQuery(".updateBtn").click(function(){
		var keyId = $(this).parents("tr").find("input:[name=keyId]").attr("value");
		var height = 340;	
		var url = "/OABoardroom.do?operation=7&requestType=UPDATE&boardroomId="+keyId;
		asyncbox.open({
			id:'adddiv',url:url,title:'新增会议室',width:540,height:height,cache:false,modal:true			  
	　  });
	});

	if($("#sortInfo").val() != undefined && $("#sortInfo").val() != ""){
		var sorrInfo = $("#sortInfo").val().split(",");	
		if(sorrInfo[0] == "ASC"){
			$(".maintop td span[fieldName='"+sorrInfo[1]+"']").attr("sort","DESC")
			$(".maintop td span[fieldName='"+sorrInfo[1]+"']").after('<img src="/style/images/mail/asc.gif" height="10px;" width="8px;"/>')
		}else{
			$(".maintop td span[fieldName='"+sorrInfo[1]+"']").attr("sort","ASC")
			$(".maintop td span[fieldName='"+sorrInfo[1]+"']").after('<img src="/style/images/mail/desc.gif" height="10px;" width="8px;"/>')
		}
			
	}
	
	/*单击列表表头排序*/
	$(".maintop td span").click(function () {
		var sortInfo = $(this).attr("sort") + "," + $(this).attr("fieldName");
		$("#sortInfo").val(sortInfo);
		querySubmit();
	});
})
</script>
</head>
<body class="html" onload="showStatus();">
<div id="scroll-wrap">
<div class="meeting-wrap">
  
<iframe name="formFrame" style="display:none"></iframe>
<form action="/OABoardroom.do?operation=3" method="post" name="form" >
<div class="heading">
	<div class="heading-title">
		<span class="lf title-span">
			<b class="icons week-icon"></b>
			<i>会议室管理</i>
		</span>
		<ul class="btn-ul">
		#if($$LoginBean.getOperationMap().get("/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek").add())
			<li>
				<span name="backList" id="addBtn" class="btn-s">新建会议室</span>
			</li>
		#end
		#if($$LoginBean.getOperationMap().get("/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek").delete())
			<li>
				<span onClick="javascript:formdel();"  class="btn-s">$text.get("common.lb.del")</span>
			</li>
		#end
		</ul>
	</div>
</div>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table">
		<thead>
			<tr>
			<td width="30" class="oabbs_tc">
				<input type="checkbox" name="selAll" onClick="checkAll('keyId')" /> 
				</td>
				<td width="150" class="oabbs_tl">名称</td>
				 <td width="200" class="oabbs_tc">地点</td>
			    <td width="100" class="oabbs_tc">最多可容纳人数</td>
	     	    <td width="300" class="oabbs_tc">描述</td>
		        <td width="80" class="oabbs_tc">操作</td>
			</tr>
		</thead>
		<tbody>
		#foreach( $row in $list)
		<tr>
	<td width="30" class="oabbs_tc"> 
					<input type="checkbox" name="keyId"  value="$row.boardroomId" /> 
					<input type="hidden" name="keyName"  value="$row.boardroomName" /> 
				</td>
		  <td width="*" class="oabbs_tl"> <a href="javascript:void(0);" onclick="week('$row.boardroomId')">$row.boardroomName</a></td>
				 <td width="100" class="oabbs_tl">$row.address</td>
			    <td width="150" class="oabbs_tl">$row.peopleNumber</td>
	     	    <td width="100" class="oabbs_tl">$row.describe</td>
		        <td width="200" class="oabbs_tl">
		        #if($$LoginBean.getOperationMap().get("/OABoardroom.do").update())
		        <a href="javascript:" class="updateBtn" title="修改">修改</a>
		        #end
		        #if($$LoginBean.getOperationMap().get("/OABoardroom.do").delete())
		        <a href="javascript:void(0);" onclick="del('$row.boardroomId','$row.boardroomName',this)" title="删除">删除</a>
		        #end
		        </td>
		        </tr>
		        #end
		  </tbody>
		</table>
		</form>
</div>		
</div>
<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.documentElement.clientHeight);
});
</script>		
</body>
</html>