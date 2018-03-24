<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.mail.mailDirectory")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/email.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<style type="text/css">
.listRange_1{margin-top: 10px;}
.listRange_list_function tbody tr{height:30px;}
.scroll_function_small_a{padding-top: 10px;width:70%;}
.listRange_pagebar{float: right;margin-right: 40px;}
.HeadingButton{float: none;margin-left: 62%;}
</style>
<script>


function delMailFile(id){
	asyncbox.confirm('是否删除此文件夹！','提示',function(action){
		if(action == 'ok'){
			var urls = "/EMailAction.do?operation=$!globals.getOP("OP_QUERY")&type=groupmanager&op=del&groupId="+id;
			jQuery.ajax({
				type:"POST",
				url:urls,
				success: function(msg){
					if(msg == "OK"){
						asyncbox.tips('删除成功！','success');
						dealAsyn();
					}else if(msg == "EXIST"){
						asyncbox.tips('文件夹中有邮件，不能删除！','success');
					}else{
						asyncbox.tips('删除失败！','error');	
					}
				}
			});
		}
	});
}


function dealMailFile(type,id){
	var title = "添加文件夹";
	var urls = "/EMailAction.do?operation=$globals.getOP("OP_QUERY")&type=groupmanager&op=prepare";
	if(type=="update"){
		title="修改文件夹";
		urls += "&groupId="+id;
	}
	asyncbox.open({
	 title : title,
	 	 id : 'fileDiv',
	　　　url : urls,
	　　　width : 350,
	　　　height : 250,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	if(action == 'ok'){
				opener.checkForm();
				return false;
　　　　　	}
　　　	}
　	});
}

function dealAsyn(){
	window.location.href='/EMailAction.do?operation=4&type=groupmanager';
	window.parent.frames['leftFrame'].location.reload() ;
}

</script>
</head>
<body>
<form method="post" scope="request" name="form" action="/EMailAction.do?operation=4&type=groupmanager">
	<div class="Heading">
		<div class="HeadingTitle" style="padding:0;color:#3C78AA;">
			我的文件夹
		</div>
		<ul class="HeadingButton">
			<li>
				<span class="btn-w" name="readOuter" type="button" onclick="dealMailFile('add')">$text.get("oa.common.addDirectory")</span>
			</li>
		</ul>
	</div>
	<div class="scroll_function_small_a" id="list_id">
			<script type="text/javascript">
				var sHeight=document.documentElement.clientHeight;
				$("#list_id").height(sHeight-90);
			</script>
			<table width="600" border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="tblSort" name="table">
				<thead>
					<tr class="scrollColThead">
						<td class="oabbs_tc">$text.get("oa.mail.lb.account")</td>
						<td class="oabbs_tl">$text.get("oa.mail.folderName")</td>				
						<td class="oabbs_tc">$text.get("oa.calendar.option")</td>
					</tr>
				</thead>
				<tbody>
					#foreach ($vg in $list)
				 	<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
					    <td class="oabbs_tl">#if("$!globals.get($vg,3)" != "") $!globals.get($vg,3) #else $text.get("oa.common.innerMail") #end</td>
						<td class="oabbs_tl">$!globals.get($vg,1)</td>
						<td class="oabbs_tc"> &nbsp;
							   	<!-- <a  href="EMailAction.do?operation=$globals.getOP("OP_QUERY")&type=groupmanager&groupId=$!globals.get($vg,0)&op=prepare"><img src="style/plan/M_1.gif"/>  $text.get("oa.mail.rename")</a>
								&nbsp;
							 	<a  href="javascript:del('$!globals.get($vg,0)')"><img src="style/plan/M_2.gif"/> $text.get("common.lb.del")</a>-->
							 	<a style="padding-left:12px;cursor:pointer;" onclick="dealMailFile('update','$!globals.get($vg,0)')" title="重命名文件夹">$text.get("oa.mail.rename")</a>
							 	<a style="padding-left:12px;cursor:pointer;" onclick="delMailFile('$!globals.get($vg,0)')" title="删除文件夹">$text.get("common.lb.del")</a>
						</td>
					</tr>
				 	#end
				</tbody>
			</table>
		</div>
</form>	
</body>
</html>