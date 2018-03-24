
#if("$!operationType" != "add")

	#foreach($row in $request.getAttribute('list'))
	#if($velocityCount < 5)
	<div class="mt-item">
		<div class="i-a">
			<img class="de-img" src="$globals.checkEmployeePhoto("48",$!self)" alt="">
		</div>
		<a href="/Meeting.do?operation=4&requestType=NOTE&shijian=$formt.format($!row.startTime).substring(0,10)&meetingId=$!row.id&addTHhead=true" class="n-a">$!row.title</a>
		<span class="t-op">发起：$!globals.getEmpFullNameByUserId($!row.sponsor)</span>
		<div class="d-mc">
			<b class="icon-1"></b>
			<div class="d-list">
				<span class="l-sp">主持人 ：</span>
				<span class="c-sp">$!row.toastmasterName</span>
			</div>
			<div class="d-list">
				<span class="l-sp">会议主题：</span>
				<span class="c-sp">$!row.title</span>
			</div>
			
			<div class="d-list">
				<span class="l-sp">会议内容：</span>
				<span class="c-sp">$!row.meetingContent</span>
			</div>
			<div class="d-list">
				<span class="l-sp">会议时间：</span>
				<span class="c-sp">$formt.format($!row.startTime) - $formt.format($!row.endTime)</span>
			</div>
			<div class="d-list">
				<span class="l-sp">会议地点：</span>
				#if("$!boardroomMap.get($!row.boardroomId).boardroomName"=="")
	   				<span class="c-sp">
	  						 已删除
					</span>
	   				#else
	   				<span class="c-sp">
	  						 $!boardroomMap.get($!row.boardroomId).boardroomName
					</span>  				
	   			#end		
			</div>
			<!-- <div class="d-list">
				<span class="l-sp">所需物品：</span>
				<span class="c-sp">纸笔自带</span>
			</div> -->
			<div class="d-list">
				<span class="l-sp">与会人员：</span>
				<span class="c-sp">$!row.participantName</span>
			</div>
			<div class="d-list">
				<span class="l-sp">备注：</span>
				<span class="c-sp">如若不能准时参会，请在本通知下回复，并写明原因。如不回复，视为参会，谢谢！
			          另，以后公司会议，无特殊情况，迟到人员每次乐捐不少于10元，乐捐款作为公司公
			          款用于团队活动。
			    </span>
			</div>
		</div>
		<p class="c-time">$!row.createTime</p>
	</div>
	#end
	#end
#else

<style type="text/css" >
.textboxlist-loading { background: url('images/spinner.gif') no-repeat 380px center; }
.textboxlist{width:450px; }
.textboxlist-bits{height:auto;border:1px solid #d1d1d1;}	
</style>
<script type="text/javascript">
$(function(){
	shareByTBox1 = new jQuery.TextboxList('#toastmasterName', {unique: true, plugins: {autocomplete: {}}});
	shareByTBox1.getContainer().addClass('textboxlist-loading');
	shareByTBox1.plugins['autocomplete'].setValues($textBoxValues);
	shareByTBox1.getContainer().removeClass('textboxlist-loading');
	shareByTBox2 = new jQuery.TextboxList('#participantName', {unique: true, plugins: {autocomplete: {}}});
	shareByTBox2.getContainer().addClass('textboxlist-loading');
	shareByTBox2.plugins['autocomplete'].setValues($textBoxValues);
	shareByTBox2.getContainer().removeClass('textboxlist-loading');	 
})
</script>
<div class="oa-meeting">
	<ul class="aU">
       	<li>
           	<i class="lf iM">主题：</i>
               <input class="lf txt-l inp" type="text" id="meetTile" />
           </li>
           <li>
           	<i class="lf iM">会议内容：</i>
               <textarea class="lf txt-l inp" id="meetContext"></textarea>
           </li>
           <li>
           	<i class="lf iM">会议时间：</i>
               <span class="lf h-ic">
               	<input style="width:100px;" id="meetTime" class="txt-date inp" type="text" value='$globals.getHongVal("sys_date")' defValue='$globals.getHongVal("sys_date")' onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>
                   <b class="icon-1 date" onClick="WdatePicker({lang:'$globals.getLocale()'})"></b>
               </span>
               <span class="lf h-ic">
	               <select id="meetTime1" style="margin-left:30px;height: 30px;line-height: 30px;padding: 5px 10px;border-radius: 5px;border: 1px solid #ccc;color: #555;" >
	              		#foreach($log in [0..23])
	              		<option #if($log == 10) selected="selected" #end value="#if($log > 9)$log #else 0$log #end">#if($log > 9)$log#else 0$log #end</option>
	              		#end
	              	</select>：
	              	<select id="meetTime2" style="height: 30px;line-height: 30px;padding: 5px 10px;border-radius: 5px;border: 1px solid #ccc;color: #555;" >
              			#foreach($log in [0..59])
	              		<option value="#if($log > 9)$log #else 0$log #end">#if($log > 9)$log #else 0$log #end</option>
	              		#end
              		</select>              	
               </span>
               <b class="lf link" style="width:30px;"></b>
               <span class="lf h-ic">
               		<select id="meetTime3"  style="height: 30px;line-height: 30px;padding: 5px 10px;border-radius: 5px;border: 1px solid #ccc;color: #555;">
	              		#foreach($log in [0..23])
	              		<option #if($log == 12) selected="selected" #end value="#if($log > 9)$log #else 0$log #end">#if($log > 9)$log#else 0$log #end</option>
	              		#end
	              	</select>：
	              	<select id="meetTime4" style="height: 30px;line-height: 30px;padding: 5px 10px;border-radius: 5px;border: 1px solid #ccc;color: #555;">
              			#foreach($log in [0..59])
	              		<option value="#if($log > 9)$log #else 0$log #end">#if($log > 9)$log#else 0$log #end</option>
	              		#end
              		</select>                   
               </span>
           </li>
           <li>
           	<i class="lf iM">会议室：</i>           
              	<select id="boardroomId" class="s-select" onchange="validRoom();">
              		<option value="">请选择</option>
             		#foreach($room in $boardroomMap.values())
		    		<option   value="$room.boardroomId"  #if("$room.boardroomId"=="$meeting.boardroomId")selected="selected" #end>$room.boardroomName</option>
		    		#end 
              	</select>
           </li>
           <li>
           	<i class="lf iM">主持人：</i>
           		<span class="lf h-ic">
		            <input name="toastmaster"  type="hidden" id="toastmaster" value="" />
		            <input name="toastmasterName" class="txt-s inp" type="text" id="toastmasterName" />
		        	<b onclick="publicPopSelect('userGroup','toastmasterName','meetingId','true');" class="icon-1 mag"></b>      
        		</span>          		           
           </li>          
           <li>
           	<i class="lf iM">参与人：</i>
	           	<span class="lf h-ic">
		    		<input name="participant" type="hidden" id="participant" value=""/> 
			    	<input name="participantName" class="txt-s inp" type="text" id="participantName" /> 
			    	<b onclick="publicPopSelect('userGroup','participantName','meetingId','true');" title="选择人员" class="b-ry"></b>
			    	<b onclick="publicPopSelect('deptGroup','participantName','meetingId','true');" title="选择部门" class="b-bm"></b>			    		
	     		</span>
           </li>
       </ul>
   <div class="point-block">
		<input class="rf btn-cel" type="button" onClick="cel();" value="取消" />
		<input class="rf btn-add" type="button" value="添加" onclick="meetingSubmit();" />
	</div>
</div>	
#end
