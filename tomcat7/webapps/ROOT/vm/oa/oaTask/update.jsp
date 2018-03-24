#set($isUpdateEdit = "false")
#if("$!loginBean.id" == "$!taskBean.createBy" || "$!loginBean.id" == "$!taskBean.executor" )
	#set($isUpdateEdit = "true")
#end
	<div class="a-title">
   	<span class="lf pr">
       	<b class="icons pa"></b>
       	修改任务
       </span>
   </div>
   <ul class="aU">
   	<li>
       	<i class="lf iM notNull">任务名称：</i>
           <input class="lf inpTxt" type="text" id="title" name="title" value="$globals.encodeHTML2($!taskBean.title)"/>
       </li>
       <li>
       	<i class="lf iM ">详情描述：</i>
           <textarea class="lf areaTxt" id="remark" name="remark" >$!taskBean.remark</textarea>
       </li>
       <li>
       <i class="lf iM notNull">负责人员：</i>
           <span class="lf pr icon-txt">
           <input type="hidden" name="executor" value="$!taskBean.executor" id="executor" />
           #if($isUpdateEdit == "true")
			   <input class="txtIcon ip_txt" type="text" name="executorName" value="$!globals.getEmpFullNameByUserId($!taskBean.executor)" id="executorName"  readonly="readonly" ondblclick="publicPopSelect('userGroup','executor','normalId','false');"/>
	           <b onclick="publicPopSelect('userGroup','executor','normalId','false');" class="icons pa bMag"></b>
           #else
           		$!globals.getEmpFullNameByUserId($!taskBean.executor)
           #end
           </span>
       </li>
       <li>
       	<i class="lf iM notNull">执行时间：</i>
           <span class="lf pr icon-txt">
           	<input class="txtIcon ip_txt" type="text" id="beginTime" name="beginTime" value='$!taskBean.beginTime' defValue='$!taskBean.beginTime' onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>
               <b class="icons pa bDate" ></b>
           </span>
           <b class="lf link"></b>
           <span class="lf pr icon-txt">
            
           	<input class="txtIcon ip_txt" type="text" id="endTime" name="endTime" value='$!taskBean.endTime' onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>
               <b class="icons pa bDate"></b>
           </span>
       </li>
       <li style="float: left;width: 242px;">
           <i class="lf iM">紧急程度：</i>
           	<span class="lf pr IconTxt">
               	<select class="slt" name="emergencyLevel" id="emergencyLevel" >
	             	#foreach($item in $globals.getEnumerationItems("emergencyLevel"))	 
	        	    	<option value="$item.value" #if("$item.value" == "$!taskBean.emergencyLevel") selected="selected" #end>$item.name</option>
			        #end
	            </select>
            </span>
       </li>
	   <li>
			<i class="lf iM">任务分类：</i>
			<span class="lf pr IconTxt">
				<select class="slt" name="oaTaskType" id="oaTaskType" >
					#foreach($item in $globals.getEnumerationItems("OATaskType"))	 
						<option value="$item.value" #if("$item.value" == "$!taskBean.taskType") selected="selected" #end>$item.name</option>
					#end
				</select>
			</span>
	   </li>
       <li style="float: left;width: 242px;">
       	<i class="lf iM">验收人员：</i>
       		
            <span class="lf pr icon-txt">
          		<input type="hidden" name="surveyor" value="$!taskBean.surveyor" id="surveyor" />
          		#if($isUpdateEdit == "true")
          			<input class="txtIcon ip_txt" type="text" name="surveyorName" value="$!globals.getEmpFullNameByUserId($!taskBean.surveyor)" id="surveyorName"  readonly="readonly" ondblclick="publicPopSelect('userGroup','surveyor','normalId','false');"/>
              		<b onclick="publicPopSelect('userGroup','surveyor','normalId','false');" class="icons pa bMag"></b>
	            #else
	            	$!globals.getEmpFullNameByUserId($!taskBean.surveyor)
	            #end
				
            </span>
       </li>
       <li>
       <i class="lf iM #if("$!crmTaskEnter"=="true") notNull #end " id="relateClientId">关联客户：</i>
	       <span class="lf pr icon-txt">
		       <input type="hidden" name="taskClientId" value="$!taskBean.clientId" id="taskClientId" />
			   <input class="txtIcon ip_txt" type="text" name="taskClientIdName" value="$!clientName" id="taskClientIdName"  readonly="readonly" ondblclick="publicPopSelect('CrmClickGroup','taskClientId','normalId','false');"/>
		       <b onclick="publicPopSelect('CrmClickGroup','taskClientId','normalId','false');" class="icons pa bMag"></b>
	       </span>
       </li>
       <li style="float: left;width: 242px;">
       <i class="lf iM">任务状态：</i>
           <span class="lf pr IconTxt">
           #if("$!taskBean.status" == "3")
           	   <input type="hidden" id="uptStatus" name="uptStatus" value="3">
           	   <i style="line-height:29px;">验收中</i>
           #else
               #set($statusVal = "2")
               #if("$!taskBean.surveyor"!="" && "$!taskBean.surveyor"!="$loginBean.id")
                   #set($statusVal = "3")
               #end		
	           <select id="uptStatus" name="uptStatus">
	           		<option value="1" #if("$!taskBean.status" == "1") selected="selected" #end>进行中</option>
	           		<option value="$statusVal" #if("$!taskBean.status" == "2") selected="selected" #end>完成</option>
	           </select>
           #end
	       </span>
       </li>
       <li>
           <i class="lf iM">任务进度：</i>
           <span class="lf pr IconTxt">
           <select id="schedule" name="schedule">
           		<option value="0" >0</option>
				<option value="25" #if("$!taskBean.schedule" == "25") selected="selected" #end>25%</option>
				<option value="50" #if("$!taskBean.schedule" == "50") selected="selected" #end>50%</option>
				<option value="75" #if("$!taskBean.schedule" == "75") selected="selected" #end>75%</option>
				<option value="100" #if("$!taskBean.schedule" == "100") selected="selected" #end>100%</option>
           </select>
           </span>
       </li>
       <li>
       <i class="lf iM">关联项目：</i>
           <span class="lf pr IconTxt">
           	<select id="itemId" name="itemId">
           		<option value="">无关联</option>
           		#foreach($item in $itemList)
           			<option value="$!globals.get($item,0)" itemEndTime="$!globals.get($item,2)" #if("$!globals.get($item,0)" == "$!taskBean.itemId") selected="selected" #end>$!globals.get($item,1)</option>
           		#end
           	</select>
           </span>
       </li>
       <li>
          <i class="lf iM">上传附件：</i>
          <span class="lf pr fujian" onClick="upload('AFFIX','affix');">
              <b class="icons pa fujian"></b>
             	 附件
          </span>
          <ul id="affixuploadul_affix" class="add-tu">
		  #foreach($uprow in $!taskBean.affix.split(";"))
		      #if($uprow != "")
			      <li class="file_li" id="uploadfile_$uprow">
				  <input type=hidden id="affix" name=affix value='$uprow'/><div class="showAffix">$uprow</div>
					  <a class="del-a-btn" href='javascript:deleteupload("$uprow","false","OATask","AFFIX");'>$text.get("common.lb.del")</a>
				  </li>
			  #end
		  #end
          </ul>
       </li>
           <li style="overflow:inherit;height:30px;">
           <input type="hidden" id="hideParticipantInfo" value="$!hideParticipantInfo">
           <i class="lf iM">参与人员：</i>
           <div class="lf txt-list h-partic">
               <span class="lf pr icon-txt w300">
               <input class="lf txtIconLong inpTxt" type="text" id="participantInfo" name="participantInfo"/>
               <b class="icons pa bMag" onclick="javascript:publicPopSelect('userGroup','participantInfo','participantBoxId','true');"></b>
               </span>
           </div>
       </li>
   </ul>
   <div class="btn-items-wp">
   	<span class="btn-items celBtn" onclick="closeLayer(this);">取消</span>
    <span class="btn-items conBtn" operation="2" keyId="$!taskBean.id" onclick="taskSubmit(this);">确定</span>
   </div>
