<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
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
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/CRMBrotherAction.do" method="post" target="formFrame">
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')"/>
<input type="hidden" name="tableName" id="tableName" value="$!tableName">
<input type="hidden" name="f_brother" id="f_brother" value="$!result.get('ClientId')">
<input type="hidden" name="tableName" id="tableName" value="$!tableName">
<input type="hidden" name="keyId" id="keyId" value='$!result.get("id")'>
<div class="boxbg2 subbox_w700" style="margin: 5px auto;">
<div class="subbox cf">
	<div class="operate operate2">
		<ul>
			<li class="sel">修改-跟单信息</li>
		</ul>
	</div>
	<div class="bd">
		<div class="inputbox">
			<ul class="list_ul">
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">客户名称</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					<input style="border-right:0;width:164px;" name="ClientIdName" id="ClientIdName" value="$!result.get('CRMClientInfo.ClientName')" type="text" class="inp_w"  ondblclick="popSelect('CrmClickGroup','ClientId')" readonly="readonly"/>
					<a class="fdj_a" onclick="popSelect('CrmClickGroup','ClientId')"></a>
					<input type="hidden" name="ClientId" id="ClientId" value="$!result.get('ClientId')">
					</div>
					<font color="red">*</font>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟进对象</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					<input style="border-right:0;width:164px;" name="ClientlinkManName" id="ClientlinkManName" type="text" value='$!result.get("CRMClientInfoDet.UserName")'  class="inp_w" ondblclick="definePop('/UserFunctionAction.do?tableName=CRMSaleFollowUp&fieldName=ClientlinkMan&operation=22','ClientlinkManName','跟进对象');" readonly="readonly"/>
					<a class="fdj_a" onclick="definePop('/UserFunctionAction.do?tableName=CRMSaleFollowUp&fieldName=ClientlinkMan&operation=22','ClientlinkMan','跟进对象');"></a>
					<input name="ClientlinkMan" id="ClientlinkMan" type="hidden" value="$!result.get('ClientlinkMan')"  class="inp_w" />
					</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">下次跟进时间</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					<input name="NextVisitTime"  id="NextVisitTime" type="text" value="$!result.get('NextVisitTime')" class="inp_w inp_date"   onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>	
					</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟进时间</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					<input name="VisitTime"  id="VisitTime" type="text" value="$!result.get('VisitTime')" class="inp_w inp_date"   onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>
					</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟单人</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input style="border-right:0;width:164px;" name="EmployeeIDName" id="EmployeeIDName" value='$!globals.getEmpFullNameByUserId($!result.get("EmployeeID"))' type="text"  class="inp_w" ondblclick="popSelect('userGroup','EmployeeID')" readonly="readonly"/>
						<a class="fdj_a" onclick="popSelect('userGroup','EmployeeID')"></a>
						<input name="EmployeeID" id="EmployeeID" type="hidden"  class="inp_w" value="$!result.get('EmployeeID')"/>
					</div>
				</li>
				
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟单阶段</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					<select  name="FollowPhase"  id="FollowPhase">
	       		 	<option value="" >--请选择--</option>
		            #foreach($item in $globals.getEnumerationItems("FollowPhase"))	 
		        	<option value="$item.value" #if("$!result.get('FollowPhase')"=="$item.value") selected="selected" #end>$item.name</option>
				    #end
	            	</select>
	            	</div>	
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟进方式</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					<select  name="VisitMethod"  id="VisitMethod">
	       		 	<option value="" >--请选择--</option>
		            #foreach($item in $globals.getEnumerationItems("VisitMethod"))	 
		        	<option value="$item.value" #if("$!result.get('VisitMethod')"=="$item.value") selected="selected" #end>$item.name</option>
				    #end
	            	</select>
	            	</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">客户意向</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					<select  name="CustomerIntent"  id="CustomerIntent">
	       		 	<option value="" >--请选择--</option>
		            #foreach($item in $globals.getEnumerationItems("Opration"))	 
		        	<option value="$item.value" #if("$!result.get('CustomerIntent')"=="$item.value") selected="selected" #end>$item.name</option>
				    #end
	            	</select>
	            	</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟单状态</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					<select  name="FollowStatus"  id="FollowStatus">
	       		 	<option value="" >--请选择--</option>
		            #foreach($item in $globals.getEnumerationItems("SalesFollowType"))	 
		        	<option value="$item.value" #if("$!result.get('FollowStatus')"=="$item.value") selected="selected" #end>$item.name</option>
				    #end
	            	</select>
	            	</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟进类别</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					<select  name="VisitType"  id="VisitType">
	       		 	<option value="" >--请选择--</option>
		            #foreach($item in $globals.getEnumerationItems("VisitType"))	 
		        	<option value="$item.value" #if("$!result.get('VisitType')"=="$item.value") selected="selected" #end>$item.name</option>
				    #end
	            	</select>
	            	</div>
				</li>
				<li>
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">生成工作计划</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					<select  name="GenWorkPlan"  id="GenWorkPlan">
		            #foreach($item in $globals.getEnumerationItems("YesNo"))	 
		        	<option value="$item.value" #if("$!result.get('GenWorkPlan')"=="$item.value") selected="selected" #end>$item.name</option>
				    #end
	            	</select>
					</div>
				</li>
				<li class="col">
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">地理位置</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" ><input style="width:505px;" name="location" id="location" type="text"  class="inp_w" value="$!result.get('location')" readonly="readonly"/></div>
				</li>	
				<li class="col">
					<div class="swa_c1 d_6"><div class="d_box"><div class="d_test">跟单内容</div></div><div class="d_mh">：</div></div>
					<textarea name="Content" id="Content" rows="5" cols="98">$!result.get("Content")</textarea>
				</li>	
			</ul>
		</div>
	</div>
	<div class="listRange_1_photoAndDoc_1">
		<button type="button" id="picbutton" name="picbutton"  onClick="upload('PIC','picbutton');" class="btn btn-danger">$text.get("upload.lb.picupload")</button>
		<ul id="picuploadul_picbutton">
			#foreach($uprow in $result.get("Attachment").split(";"))
			#if("$!uprow" !="")
			<li style='background:url();' id='uploadfile_$uprow'><input type=hidden id="picbutton" name="picbutton" value='$uprow'>
			<div><a class="img_wa" href="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" target="_blank"><img src="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" border="0"></a></div><div>$uprow&nbsp;&nbsp;#if("$detail" != "detail")<a href='javascript:deleteupload("$uprow","false","$tableName","PIC");'>$text.get("common.lb.del")</a>#end</div>
			</li>
			#end
			#end
		</ul>
	</div>
</div>
</div>
</form>
</body>
</html>
