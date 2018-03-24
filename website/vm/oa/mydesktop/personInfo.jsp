<table cellpadding="0" cellspacing="0">
	<tr>
		<td width="156px" valign="top">
			<img id="myPhoto" style="border:4px solid #E8E8E8;cursor:pointer;" src="$!myPhoto" width="140px" height="140px"  onClick="uploadImage();"/>			
		</td>
		<td valign="top" class="Block_honor_info">
			<div><a>$LoginBean.empFullName $LoginBean.departmentName</a></div>
			<div style="height:60px;">
				<div><a title="$text.get("mydesktop.msg.com.Noread")" href="javascript:mdiwin('/EMailAction.do?src=menu','$text.get("oa.mail.myMail")')"><img id="image1" src="/style/images/desktop/msg_icon_1.jpg" /></a></div>
				<div><a title="$text.get("mydesktop.msg.com.NoReadMess")" href="javascript:top.openkk();"><img id="image2" src="/style/images/desktop/msg_icon_2.jpg" /></a></div>
				<div><a title="$globals.getEnumerationItemsDisplay('adviceType2','notApprove')" href="javascript:mdiwin('/AdviceAction.do?type=notApprove','$text.get("advice.lb.info")');"><img id="image3" src="/style/images/desktop/msg_icon_3.jpg" /></a></div>
				<div><a title="$globals.getEnumerationItemsDisplay('adviceType2','Other')" href="javascript:mdiwin('/AdviceAction.do?type=noRead','$text.get("advice.lb.info")');"><img id="image4" src="/style/images/desktop/msg_icon_4.jpg" /></a></div>					
			</div>
			<div style="height:60px;font-size:13px;line-height:34px;">
				<span>$countDownTitle</span>
				#set($fz="40")
				#if($!countDownValue.length()>=3)
				#set($fz="20")
				#elseif($!countDownValue.length()==2)
				#set($fz="30")
				#end
				#if("$LoginBean.id"=="1")
				<a href="javascript:updateCoundDown()" title="$text.get("countDown.lb.update")"><span style="color:#FF33FF;font-size:${fz}px">$countDownValue</span></a><span style="margin-left:3px;">$text.get("oa.calendar.day")</span>
				#else 
				<span style="color:#FF33FF;font-size:${fz}px">$countDownValue</span><span style="margin-left:3px;">$text.get("oa.calendar.day")</span>
				#end
			</div>
			<div id="flow">
			#if("$!wisdom"!="")
		 	<MARQUEE scrollamount=2 scrollDelay=200 direction=left width=100% height=20 style="white-space: nowrap;">
		 	$!wisdom
		 	</MARQUEE>
		 	#end
			</div>
		</td>
	</tr>
</table>
#if("$LoginBean.id"=="1")
<script>
function updateCoundDown(){	
	asyncbox.open({
　　　	id : 'deskTopId',
		url : '/MyDesktopAction.do?operation=1',
		cache : false,
		modal : true,
	 	title : '$text.get("countDown.lb.update")',
　　　	width : 480,
　　　	height : 250,
		btnsbar : jQuery.btn.OKCANCEL, 
　　　	callback : function(action,opener){
　　　　　	if(action == 'ok'){
　　　　　　　	if(!opener.beforSubmit()){
					return false ;
				}
　　　　　	}
　　　	}
	});
}
</script>
#end