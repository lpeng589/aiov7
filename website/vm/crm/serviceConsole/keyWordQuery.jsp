<hr size="1"/>
#if($clientList.size()==0)
	<br/>没有找到符合条件的客户
#else
	#foreach($client in $clientList)
		#if($velocityCount<=10)
		<p><a href="#" onclick="clientDetail('$globals.get($client,0)','$globals.get($client,1)');">$globals.get($client,1)</a></p>
		#end
	#end
#end
#if($clientList.size()>10)
<font color="#FF0099">注意：<br/>关键字过短，查询结果多于10条。</font>
#end
<hr size="1"/>
<b>电话号码</b>&nbsp;查询结果：
#if($detClientList.size()==0)
    <br/>没有找到该号码的客户或联系人
#else
	#foreach($detClient in $detClientList)
		#if($velocityCount<=10)
		<p><a href="#" onclick="clientDetail('$globals.get($detClient,0)','$globals.get($detClient,1)');">相同号码：$globals.get($detClient,1)</a>-$globals.get($detClient,2)</p>
		#end
	#end
#end

  