<div class="mag_head" style="cursor:default;">联系人</div>
	<div class="mag_search" id = "serEmp" ><input type="text" onpropertychange="searchUser(this.value)"/><span><input type="button"/></span></div>
	<div class="mag_search" id = "serLink" style="display: none;"><input type="text" id = "search" /><span><input type="button" onclick="searchUserBun('search')"/></span></div>

<ul class="mag_list">
	<li id="onlineTab" class="icon_1_a" onClick="changeTab('onlineTab')" title="人员"></li>
	<li id="deptTab"  class="icon_2" onClick="changeTab('deptTab')" title="#if($type)通讯录#else部门#end"></li>
	#if("$type" == 'emp')
		<li id="groupTab"  class="icon_3" onClick="changeTab('groupTab')" title="职员分组"></li>
	#elseif("$type"=="linkMan")
		<li id="groupTab"  class="icon_3" onClick="changeView('cumu')" title="通讯录"></li>	
	#elseif("$type"=="cumu")
		<li id="groupTab"  class="icon_3" onClick="changeView('linkMan')" title="客户联系人"></li>	
	#else
		<li id="groupTab"  class="icon_3" onClick="changeView('linkMan')" title="客户联系人"></li>	
	#end
	<li id="historyTab" class="icon_4" onClick="changeTab('historyTab')" title="最近联系人"></li>
</ul>
<div id="userList" class="usList">
#foreach($online in $onlineUsers)
	#if($type == 'emp')
		<div  onClick="insertUser('person','$!online.id','$!online.name');" ><span class="c1">$!online.name</span> #if($online.mobile)<img src="/style/images/mobile.gif"/> #end</div>
	#else
		<div  onClick="insertUser('person','$!online','$!online');" ><span class="c1">$!online</span> </div>
	#end
#end
</div>
<div id="deptList" style="display:none" class="deptLlist">
<ul id="navigation">
	
</ul>
</div>
