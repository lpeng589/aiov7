#if("$deskId" == "dayremark")	
	<ul class="l-list-u">
		<li class="att-li">
			<em>日期</em>
			<em>上班</em>
			<em>下班</em>
		</li>
		#foreach($msg in $msgList)
		<li class="att-li">
			<em>$!globals.getWeekOfDate($!globals.get($msg,1))</em>
			<em>#if("$!globals.get($msg,2)" == "")未打卡#else $!globals.get($msg,2).substring(11,16) #end</em>
			<em>#if("$!globals.get($msg,3)" == "")未打卡#else $!globals.get($msg,3).substring(11,16) #end</em>
		</li>
		#end										
</ul>
#else
	#if($msgList.size()>0)	

		<ul class="l-list-u">
		#foreach($msg in $msgList)
			<li><a href="$!globals.get($msg,1)" target="_blank"><i class="t-i">$!globals.get($msg,3)</i>$!globals.get($msg,2)</a></li>	
		#end
		</ul>		
	#end
#end