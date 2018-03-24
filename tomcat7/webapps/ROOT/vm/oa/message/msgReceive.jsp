#foreach($msg in $msgList)
#if("$!LoginBean.id"!="$msg.send")
<div class="nameOn">$msg.sendName<span>$msg.createTime.substring(11,19)</span></div>
#if($msg.content.indexOf("/upload/")!=-1)
<p>$msg.content</p>
#else
<p>$msg.content #if("$!msg.affix"!="" && "$!msg.affix"!="null" && $msg.statusId==0) &nbsp;&nbsp;
#foreach ($str in $globals.strSplit($!msg.affix,';'))
#if("person"=="$!msg.operType")
<a id="$!msg.id" href='/ReadFile?tempFile=path&path=/message/$!msg.send/&fileName=$!globals.encode($!str)' onclick="receiveAffix('$msg.id','$!msg.id');"  target="formFrame">接收</a>
#else
<a href='/ReadFile?tempFile=path&path=/message/$!msg.send/&fileName=$!globals.encode($!str)'  target="formFrame">下载</a>
#end			
#end	
#end</p>
#end
#else
<div class="nameIn">$msg.sendName<span>$msg.createTime.substring(11,19)</span></div>
#if($msg.content.indexOf("/upload/")!=-1)
<p>$msg.content.replace("<img","<img width=200 height=100 style='cursor: pointer;' onclick='zoomImage(this)'")</p>
#else
<p>
$msg.content
</p>
#end
#end
#end