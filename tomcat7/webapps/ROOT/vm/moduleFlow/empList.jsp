<div class="mag_head">在线人员:<span>$!onLineNum/$!totleNum</span><b class="close-kk" onclick="closeKK();"></b></div>
<div class="mag_search"><input type="text" onkeyup="searchUser(this.value)"/><span><input type="button"/></span></div>
<ul class="mag_list">
	<li id="onlineTab" class="icon_1_a" onClick="changeTab('onlineTab')" title="在线人员"></li>
	<li id="deptTab" class="icon_2" onClick="changeTab('deptTab')" title="组织架构"></li>
	<li id="groupTab" class="icon_3" onClick="changeTab('groupTab')" title="群组"></li>
	<li id="historyTab" class="icon_4" onClick="changeTab('historyTab')" title="最近联系人"></li>
</ul>
<div id="userList" class="usList">
#foreach($online in $onlineUsers)
<div style="vertical-align: middle;" onmouseout="hideATalk(this);" onmouseover="showATalk(this);" ><span class="i-name"  onClick="openDialog('person','$!online.id','$!online.name');" ><img src="$globals.checkEmployeePhoto('48',$!online.id)" alt="" /><i>$!online.name</i></span> <img class="kk-img" onClick="openATalk('$server','$globals.getLoginBean().id','$!online.id','1');" src="/style/images/body/aiochat.png "/>#if($!online.type == "mobile")<img class="kk-img kk-mobile" src="/style/images/mobile.gif"/> #end</div>
#end
</div>
<div id="deptLlist" style="display:none" class="deptLlist">
<ul id="navigation">
	
</ul>
</div>
