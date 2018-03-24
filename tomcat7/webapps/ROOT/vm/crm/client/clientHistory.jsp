<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/history.css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
.cf li{float: left;text-align: center;}
.histroyUl li{text-align: left;}
.changeInfo{color: gray;}
a{color:#006699;}
a:link{color:#006699;}
a:visited{color:#006699;}
a:hover{color:#006699;}

</style>
<script type="text/javascript">
	$(document).ready(function(){
	$('#zkall').click( function(){
	  $('.pson').click();
	  $(this).html($('.pson').parents('.polmore').find('.poltxt:hidden').length>0?'全部展开':'全部缩起');
	});
	$('.pson').click( function(){
	if($(this).hasClass('psoff'))
	{   
		$(this).removeClass("psoff");
	　　 $(this).parent().parent().find('.poltxt').hide();
	}
	else
	{
		if(!$(this).hasClass('psoff'))
		{		
			$(this).addClass("psoff");
			$(this).parent().parent().find('.poltxt').show();
		}				
	}
	}
	);
	$
	$(".polmore").mouseover( function(){
	  $(this).addClass("bg");
	}).mouseout(function(){$(this).removeClass("bg")});
	
	$(".histroyUl").css("backgroundColor","#D6E4F8");
	});
	
	function reHistroy(recordId,moduleId,viewId){
		if(confirm('确定还原?')){
			//window.location.href = "/CRMClientAction.do?operation=2&type=reHistroy&recordId="+recordId+"&moduleId="+moduleId+"&viewId="+viewId;
			//parent.pageRefresh();
			jQuery("#recordId").val(recordId);
			form.submit();
		}
	}
	
	/*查看详情*/
	function checkHistory(recordId,moduleId,viewId){
		var url = "/CRMClientAction.do?operation=5&isHistroy=true&type=detailNew&recordId="+recordId+"&moduleId="+moduleId+"&viewId="+viewId;
		top.mdiwin(url,'历史记录详情');
	}
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form action="/CRMClientAction.do" method="post" name="form" target="formFrame">
<input  type="hidden" name="recordId" id="recordId" value=""/>
<input  type="hidden" name="operation" value="2"/>
<input  type="hidden" name="type" value="reHistroy"/>
<input  type="hidden" name="recordId" value="$bean.moduleId"/>
<input  type="hidden" name="viewId" value="$bean.viewId"/>
<div class="box-163css">
<div class="boxbg subbox_w660" style="width: 768px;">
<div class="subbox cf">
  <div class="bd">
      <div class="subitem_hy">
        #foreach ($bean in $list)
        <ul class="histroyUl">
          <li style="width: 20px;">$velocityCount</li>
          <li style="width: 150px;" class="contents">操作人：$!globals.getEmpNameById($bean.employeeId)</li>
          <li style="width: 220px;" class="contents">操作时间：$!bean.createTime</li>
          <li style="width: 150px;">用户IP：$!bean.ipAddress</li>
          <li style="width: 100px;">操作：<a href="#" onclick="checkHistory('$bean.id','$bean.moduleId','$bean.viewId');">查看</a>&nbsp;&nbsp;#if("$loginBean.id" == "1")<a href="#" onclick="reHistroy('$bean.id','$bean.moduleId','$bean.viewId');">还原</a>#end </li>
        </ul>
        <div class="polmore">
			<div class="part-zk">
				<h6 style="margin-left: 25px;">#if($globals.strLength($bean.updateInfo)>=110) <b class="pson off"></b> $globals.subStringByByte($bean.updateInfo,110) #else $bean.updateInfo #end</h6>
				<div class="poltxt">
					#if($globals.strLength($bean.updateInfo)>=110) $globals.replaceString($bean.updateInfo,$globals.subStringByByte($bean.updateInfo,110),"") #end
				</div>
			</div>
		</div>
        #end
      </div>
  </div>
</div>
</div>
</div>
</form>

</body>
</html>
