<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>详情</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_brother.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/crm/crm_brother.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<style type="text/css">
.subbox .operate {border-bottom:1px solid #000;padding:6px 3px 7px;background-color:#fff; overflow:hidden;position:relative;height:32px}
.subbox .operate2 {padding:6px 3px 5px 0; }
.subbox .operate li {float:left;padding:2px 0;}
.subbox .operate li.sel {height:28px;width:105px;border-radius:75px;text-align:center;font-size:14px;line-height:28px;font-weight:bold;background:#0088CC;color:#fff;}
</style>
<script type="text/javascript">



function alertSet(relationId,billTitle){
	var urls=encodeURIComponent('/CRMBrotherAction.do?operation=7&tableName=$!tableName&keyId='+relationId);
	var typestr=encodeURIComponent('$text.get("common.CRMSaleFollowUp.alert")'+billTitle);
	var title=encodeURIComponent('$text.get("crm.client.distributionFUV")');
	var date= new Date();
	var nochangeTop = nochangeTop;
	var url = "/UtilServlet?operation=alertDetail&relationId="+relationId+"&falg=true"+"&title="+title+"&typestr="+typestr+"&urls="+urls+"&date="+date;
	var height = 490;
	if($(document).height()<400){height=360}
	asyncbox.open({
		id : 'alertDiv',url : url,
		cache : false,modal : false,		
	 	title : '$text.get("workflow.msg.warmsetting")',
　　 　	width : 600, height : height, top :5,
	    callback : function(action,opener){
		    if(action == 'ok'){ 
		    	opener.checkAlertSet();
		    	return false;
			}
　　      }
 　 });
}

function reload(){
	window.location.reload();
}

</script>
</head>

<body>
<form name="form" action="/CRMBrotherAction.do" method="post" >
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')"/>
<input type="hidden" name="tableName" id="tableName" value="$!tableName">
<input type="hidden" name="f_brother" id="f_brother" value="">
<input type="hidden" name="tableName" id="tableName" value="$!tableName">
<input type="hidden" name="keyId" id="keyId" value='$!result.get("id")'>
<input type="hidden" name="commentFrameId" id="commentFrameId" value='commentFrame'>
<div class="boxbg2 subbox_w700" id="contentDiv" style="margin: 5px auto;">
<div class="subbox cf">
	<div class="operate operate2">
		<ul style="padding:0;overflow:hidden;">
			<li class="sel">详情-跟单信息</li>
			#if("$!isMessageEnter" !="true")
			<li style="float:right;"><a style="padding:2px 6px;" class="btn" onclick="javascript:parent.billUpdate('$!result.get("id")','$!tableName');">修改</a></li>
			<li style="float:right"><a style="padding:2px 6px;" class="btn btn-danger" onclick="javascript:alertSet('$!result.get("id")','$!result.get("CRMClientInfo.ClientName")');">提醒</a></li>
			#end
		</ul>
	</div>
	<div class="bd">
		<div class="inputbox">
			<ul class="list_ul">
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">客户名称</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$!result.get('CRMClientInfo.ClientName')</div>
				</li>
				<li><div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟进对象</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$!result.get("CRMClientInfoDet.UserName")</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">下次跟进时间</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$!result.get('NextVisitTime') </div> 
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟进时间</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$!result.get('VisitTime')</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟单人</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$!globals.getEmpFullNameByUserId($!result.get("EmployeeID"))</div>	
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟单阶段</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$globals.getEnumerationItemsDisplay("FollowPhase","$!result.get('FollowPhase')")</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟进方式</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$globals.getEnumerationItemsDisplay("VisitMethod","$!result.get('VisitMethod')")</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">客户意向</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$globals.getEnumerationItemsDisplay("Opration","$!result.get('CustomerIntent')")</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟单状态</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$globals.getEnumerationItemsDisplay("SalesFollowType","$!result.get('FollowStatus')")</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟进类别</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$globals.getEnumerationItemsDisplay("VisitType","$!result.get('VisitType')")</div>
				</li>
				<li class="col">
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">生成工作计划</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" >$globals.getEnumerationItemsDisplay("YesNo","$!result.get('GenWorkPlan')")</div>
				</li>
				<li class="col" style="width:100%;">
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">地理位置</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2 d_cont" style="background:#e1e1e1;width:550px;">$!result.get("location")</div>
				</li>	
				<li class="col" style="width:100%;">
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟单内容</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<p style="background:#e1e1e1;width:550px;">$!result.get("Content")</p>
					</div>
				</li>	
			</ul>
		</div>
	</div>
	#if($!result.get("Attachment") !="")
	<div class="listRange_1_photoAndDoc_1">
		附件：<br/>
		<ul id="picuploadul_picbutton">
			#foreach($uprow in $result.get("Attachment").split(";"))
			<li style='background:url();' id='uploadfile_$uprow'><input type=hidden id="picbutton" name="picbutton" value='$uprow'>
			<div><a class="img_wa" href="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" target="_blank"><img src="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" border="0"></a></div><div>$uprow&nbsp;&nbsp;</div>
			</li>
			#end
		</ul>
	</div>
	#end
	#if("$!tableName" == "CRMSaleFollowUp")
		<!-- iframe评论 Start -->	
		<div style="border-top:1px #000 solid; padding:10px 0 0 0;overflow:hidden;margin:10px 0 0 0;">
		<iframe id="commentFrame" name="commentFrame" width="100%" clientName="$!result.get('CRMClientInfo.ClientName')" frameborder=no scrolling="no" src='/CRMBrotherAction.do?type=comment&operation=4&keyId=$!result.get("id")&tableName=$!tableName'></iframe>
		</div>
		<!-- iframe评论 End -->
	#end
</div>
</div>
</form>
<script type="text/javascript">
	var popDiv = $(".asyncbox_normal",parent.document) ;
	if(popDiv.length == 0){
		var oDiv=document.getElementById("contentDiv");
		var sHeight=document.documentElement.clientHeight-15;
		oDiv.style.height=sHeight+"px";
		oDiv.style.overflow='auto';
	}
</script>
</body>
</html>
