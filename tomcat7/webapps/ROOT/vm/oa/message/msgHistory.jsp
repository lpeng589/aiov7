<link rel="stylesheet" href="/style/css/mgs.css"/>
<script type="text/javascript">
<!--
function zoomImage(obj){
	window.open(obj.src) ;
}
function deleteAll(){
	if(confirm("你确定要删除所有聊天记录")){
		location.href = "/MessageAction.do?operation=3&receive=$!receive&operType=$!operType" ;
	}
}
//-->
</script>
<form name="form" action="/MessageAction.do?type=history" method="post">
<input type="hidden" name="operation" value="4"/>
<input type="hidden" name="receive" value="$!receive">
<input type="hidden" name="operType" value="$!operType">
<table cellpadding="0" cellspacing="0" border="0"style=" border:1px solid #B9B9B9;">
	<tr>
		<td class="magList_head"><span>聊天记录
		<input type="text" name="keyWord" style="width:100px;height:18px;" value="$!keyWord" onKeyDown="if(event.keyCode==13) form.submit();"/>
		<a href="javascript:form.submit();"><img src="/style/images/msg/LogsSearch.gif" border="0"/></a>
		</span>
		<span style="margin-left: 17px;margin-right: 40px;"><a href="javascript:deleteAll();">删除聊天记录</a></span></td>
	</tr>
	<tr>
		<td>
		<div class="magList_body" style="height:250px; overflow-y:auto;overflow-x:hidden; width:100%; float:left; margin:0px;">
			#foreach($msg in $msgList)
				#if("$!LoginBean.empFullName"!="$msg.sendName")
					<div class="nameOn">$msg.sendName<span>$msg.createTime</span></div>
					<p>$msg.content 
						#if("$!msg.affix"!="" && "$!msg.affix"!="null") &nbsp;&nbsp;
						#foreach ($str in $globals.strSplit($!msg.affix,';'))
						<a href='/ReadFile?tempFile=path&path=/message/$!msg.send/&fileName=$!globals.encode($!str)'  target="formFrame">下载</a>			
						#end	
						#end
					</p>
				#else
					<div class="nameIn">$msg.sendName<span>$msg.createTime</span></div>
					<p>$msg.content
						#if("$!msg.affix"!="" && "$!msg.affix"!="null") &nbsp;&nbsp;
						#foreach ($str in $globals.strSplit($!msg.affix,';'))
						<a href='/ReadFile?tempFile=path&path=/message/$!msg.send/&fileName=$!globals.encode($!str)'  target="formFrame">下载</a>			
						#end	
						#end
					</p>
				#end
			#end
		</div>
		</td>
	</tr>
	<tr>
		<td class="magPages">
			<div class="pagebar"> $!pageBar </div>
		</td>
	</tr>
</table>
</form>