#if($msgList.size()>0)
<table cellpadding="0" cellspacing="0" class="Block_news_list">
	#foreach($msg in $msgList)
	<tr>
		<td>
		#if($!globals.get($msg,1).indexOf("/") > -1)
			<a onclick="javascript:mdiwin('$!globals.get($msg,1)','$!globals.get($msg,0)',this);">
				<i class="char">$!globals.get($msg,2) #if("$!globals.get($msg,4)"=="no")</i>
				<img src="\style\images\desktop/new2v.gif" width="25" />#end
			</a>
		#else
		$!globals.get($msg,1)
		#end
		</td>
		#if("$!globals.get($msg,3)"!="")
		<td style="text-align: right;width:70px;color:#B4B4B4;">$!globals.get($msg,3)</td>
		#end
	</tr>
	#end
</table>
#end