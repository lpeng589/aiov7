<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/brother_add.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/crm/crm_distWork.js"></script>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/CRMDistWorkAction.do" name="form" method="post" target="formFrame" >
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')"/>
<input type="hidden" name="keyId" id="keyId" value="$!distwork.id"/>
<div class="subbox cf">
<div class="bd">
	
	<div class="inputbox">
		
		<!-- bk Start -->
		<div >
			<ul class="bk_ul">
	        	<li class="bk_li ess">
	        		<div class="swa_c1"><i class="red">*</i>主题：</div>
	        		<input id="topic" name="topic" type="text" class="ip_txt" value="$!distwork.topic"/>
	        	</li>
	        	<li class="bk_li ess">
	        		<div class="swa_c1">客户：</div>
	        		<div class="swa_c2">
	        			<input type="hidden" name="ref_id" id="clientId" value="$!distwork.ref_id" />
	        			<input id="clientIdName" name="clientIdName" type="text" class="ip_txt" ondblclick="popSelect('CrmClickGroup','clientId')" value="$!clientName" readonly="readonly"/>
	        			<i class="adi" title="添加" onclick="popSelect('CrmClickGroup','clientId')"></i>
	        		</div>
	        	</li>
	           	<li class="bk_li">
	           		<div class="swa_c1">类型：</div>
	           		<select class="ls3_in" id="taskType" name="taskType" >
		           		<option value="">-- 请选择 --</option>
		           		#foreach($item in $globals.getEnumerationItems("CRMTaskType"))	 
							<option value="$item.value" #if("$!distwork.taskType" == "$item.value") selected="selected" #end>$item.name</option>
						#end
	           		</select>
	           	</li>
	           	<li class="bk_li">
	           		<div class="swa_c1">日期：</div>
	        		<input id="finishTime" name="finishTime" value="$!distwork.finishTime" type="text" class="ip_txt inp_date" onclick="WdatePicker({lang:'zh_CN'});"/>
	           	</li>
	           	<li class="bk_li">
	           		<div class="swa_c1">执行人：</div>
	        		<div class="swa_c2">
	        			<input type="hidden"  id="userId" name="userId" value="$!distwork.userId"/>
	        			<input id="userIdName" name="userIdName" value="$!distwork.userId" type="text" class="ip_txt" ondblclick="popSelect('userGroup','userId')" />
	        			<i class="adi" title="添加" onclick="popSelect('userGroup','userId')"></i>
	        		</div>
	           	</li>
	           	<li class="bk_li ess">
	           		<div class="swa_c1">行动描述：</div>
	        		<textarea class="ip_txt" id="content" name="content">$!distwork.content</textarea>
	           	</li>
	        </ul>
        </div>
        <!-- bk End -->
        
        <!-- bk Start -->
        <div>
	        <div class="title_dv">附件管理</div>
	        <div class="btn btn-small" id="picbutton" name="picbutton"  onClick="upload('AFFIX','uploadBtn');">附件上传</div>
	        <ul id="affixuploadul_uploadBtn">
	        #foreach($uprow in $!distwork.crmAffix.split(";"))
				#if($uprow != "")
					<li class="file_li" id="uploadfile_$uprow">
					<input type=hidden id="uploadBtn" name=uploadBtn value='$uprow'/><div class="showAffix">$uprow</div>&nbsp;&nbsp;&nbsp;			
					#if("$!detail" != "detail")
						<a href='javascript:deleteupload("$uprow","false","CRMClientInfo","AFFIX");'>$text.get("common.lb.del")</a>
					#end
						&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=$tableName" target="_blank">$text.get("common.lb.download")</a>
					</li>
				#end
			#end
	        </ul>
	        
        </div>
        <!-- bk End -->
	</div>
</div>

</div>
</form>
</body>
</html>
