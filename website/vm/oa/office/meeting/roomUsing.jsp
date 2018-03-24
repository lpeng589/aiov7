<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会议室使用情况</title>
<link type="text/css" rel="stylesheet" href="/style1/css/roomUsing.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript">
function openInputDate(obj){

	WdatePicker({lang:'zh_CN',onpicked:dateChange})	
}
function dateChange(){
	form.submit();
}
</script>
</head>
<body>

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" name="form" action="/Meeting.do" >
<input type="hidden" name="operation" value="4"/>
<input type="hidden" name="requestType" value="$!requestType"/>
<div class="heading">
	<div class="heading-title">
		<ul class="btn-ul">
			<li>
				<input id="dateTime" name="dateTime" class="Wdate" style="cursor: pointer;" value="$!dateTime" onclick="openInputDate(this);" />
			</li>	
			<li>
				 #foreach($log in [8..23])
				<i class="o-clock">$log </i>
				#end 											 	 			 	 				 
			</li>		
		</ul>
	</div>
</div>
<div id="listRange_id">	
	<div class="h-meet-list" >
		<ul class="meet-c-list">			
			#foreach($room in $!roomList)
			<li>
				<div class="meet-c-d" title="$room.boardroomName">$room.boardroomName</div>
				#if("$!mapsList.get($room.boardroomId)" != "")
					#set($list = $!mapsList.get($room.boardroomId))
					#foreach($logList in $!list)		
						#set( $lefts = $math.mul("$!globals.get($logList,3).substring(11,13)","30"))
						#set( $left =  $!lefts - 125)	
						
						#set( $wids = $math.mul("$!globals.get($logList,4).substring(11,13)","30"))	
						#set( $wid = $wids - $lefts)
						#if("$!wid" == "0")
						#set( $wid = 30)
						#end
						<span class="link-span" style="left:${left}px;width:${wid}px;" title="$!globals.get($logList,1)主持的会议（$!globals.get($logList,2)）时间为$!globals.get($logList,3).substring(11)-$!globals.get($logList,4).substring(11)">
							<i>$!globals.get($logList,1)主持的会议（$!globals.get($logList,2)）时间为$!globals.get($logList,3).substring(11)--$!globals.get($logList,4).substring(11)</i>
						</span>
					#end	
				#end																																																
			</li>	
			#end								
		</ul>	

</form>
</body>
</html>
