#if("$!isTHEnter" != "true")
<!-- 弹出添加层 add 分割线 Start -->
    	<div class="a-title">
        	<span class="lf pr">
            	<b class="icons pa"></b>
            	新建任务
            </span>
        </div>
        <ul class="aU">
        	<li>
            	<i class="lf iM notNull" >任务名称：</i>
                <input class="lf inpTxt" type="text" id="title" name="taskTitle"/>
            </li>
            <li>
            	<i class="lf iM">详情描述：</i>
                <textarea class="lf areaTxt" id="remark" name="remark"></textarea>
            </li>
            <li>
            	<i class="lf iM notNull">负责人员：</i>
                <span class="lf pr icon-txt">
               		<input type="hidden" name="executor" value="$loginBean.id" id="executor" />
					<input class="txtIcon ip_txt" type="text" name="executorName" value="$globals.getEmpFullNameByUserId($loginBean.id)" id="executorName"  readonly="readonly" ondblclick="publicPopSelect('userGroup','executor','normalId','false');"/>
                    <b onclick="publicPopSelect('userGroup','executor','normalId','false');" class="icons pa bMag"></b>
                </span>
            </li>
            <li>
            	<i class="lf iM notNull">执行时间：</i>
                <span class="lf pr icon-txt">
                	<input class="txtIcon ip_txt" type="text" id="beginTime" name="beginTime" value='$globals.getHongVal("sys_date")' defValue='$globals.getHongVal("sys_date")' onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>
                    <b class="icons pa bDate" onClick="WdatePicker({lang:'$globals.getLocale()'})"></b>
                </span>
                <b class="lf link"></b>
                <span class="lf pr icon-txt">
                	<input class="txtIcon ip_txt" type="text" id="endTime" name="endTime" onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>
                    <b class="icons pa bDate" onClick="WdatePicker({lang:'$globals.getLocale()'})"></b>
                </span>
            </li>            
            <li style="float: left;width: 242px;">
            	<i class="lf iM">验收人员：</i>
                <span class="lf pr icon-txt">
               		<input type="hidden" name="surveyor" value="" id="surveyor" />
					<input class="txtIcon ip_txt" type="text" name="surveyorName" value="" id="surveyorName"  readonly="readonly" ondblclick="publicPopSelect('userGroup','surveyor','normalId','false');"/>
                    <b onclick="publicPopSelect('userGroup','surveyor','normalId','false');" class="icons pa bMag"></b>
                </span>
            </li>
            <li>
            	<i class="lf iM #if("$!crmTaskEnter"=="true" || "$!clientId" !="") notNull #end " id="relateClientId">关联客户：</i>
            	
                <span class="lf pr icon-txt">
               		<input type="hidden" name="taskClientId" value="$!clientId" id="taskClientId" />
               		#if("$!clientName" !="")
               			<i >$!clientName</i>
               		#else
						<input class="txtIcon ip_txt" type="text" name="taskClientIdName" value="" id="taskClientIdName"  readonly="readonly" ondblclick="publicPopSelect('CrmClickGroup','taskClientId','normalId','false');"/>
	                    <b onclick="publicPopSelect('CrmClickGroup','taskClientId','normalId','false');" class="icons pa bMag"></b>
               		#end
                </span>
            </li>
            <li style="float: left;width: 242px;">
            	<i class="lf iM" >紧急程度：</i>
                <span class="lf pr IconTxt">
               		<select class="slt" name="emergencyLevel" id="emergencyLevel" >
		             	#foreach($item in $globals.getEnumerationItems("emergencyLevel"))	 
		        	    	<option value="$item.value" >$item.name</option>
				        #end
		            </select>
                </span>
            </li>
            <li>
            	<i class="lf iM">关联项目：</i>
                <span class="lf pr IconTxt">
                	<select id="itemId" name="itemId">
                		<option value="">无关联</option>
                		#foreach($item in $itemList)
                			<option value="$!globals.get($item,0)" itemEndTime="$!globals.get($item,2)">$!globals.get($item,1)</option>
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
	             <ul id="affixuploadul_affix" class="affixuploadul_affix add-tu"></ul>
	            
	        </li>
            <li style="overflow:inherit;height:30px;">
            	<i class="lf iM">参与人员：</i>
            	<div class="lf txt-list h-partic">
	                <span class="lf pr icon-txt w300">
	                	<input class="lf txtIconLong inpTxt" type="text" id="participantInfo" name="participantInfo" />
	                	<b class="icons pa bMag" onclick="javascript:publicPopSelect('userGroup','participantInfo','participantBoxId','true');"></b>
	                </span>
                </div>
            </li>
            
            <input type="hidden" id="schedule" name="schedule" value="0"/>
        </ul>
        <div class="btn-items-wp">
        	<span class="btn-items celBtn" onclick="closeLayer(this);">取消</span>
	        <span class="btn-items conBtn" operation="1" keyId="" onclick="taskSubmit(this);">确定</span>
        </div>
<!-- 弹出添加层 add 分割线 End -->
#else
<!-- 弹出添加层 add 分割线 Start -->    	
<div class="oa-task">
	<ul class="aU">
       	<li>
           	<i class="lf iM">任务名称：</i>
               <input class="lf txt-l inp" type="text" id="title" name="taskTitle" />
           </li>
           <li>
           	<i class="lf iM">详情描述：</i> 
               <textarea class="lf txt-l inp" id="remark" name="remark"></textarea>
           </li>
           <li>
           	<i class="lf iM">负责人员：</i>
               <span class="lf h-ic">
	               <input type="hidden" name="executor" value="$loginBean.id" id="executor" />
				   <input class="txt-l inp" type="text" name="executorName" value="$globals.getEmpFullNameByUserId($loginBean.id)" id="executorName"  readonly="readonly" ondblclick="publicPopSelect('userGroup','executor','normalId','false');"/>
                   <b onclick="publicPopSelect('userGroup','executor','normalId','false');" class="icon-1 mag"></b>				
               </span>
           </li>
           <li>
           	<i class="lf iM">执行时间：</i>          
               <span class="lf h-ic">
               	<input class="txt-date inp" type="text" id="beginTime" name="beginTime" value='$globals.getHongVal("sys_date")' defValue='$globals.getHongVal("sys_date")' onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly" />
                   <b class="icons date" onClick="WdatePicker({lang:'$globals.getLocale()'})"></b>
               </span>
               <b class="lf link"></b>
               <span class="lf h-ic">
               	<input class="txt-date inp" type="text" id="endTime" name="endTime" onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>
                   <b class="icons date" onClick="WdatePicker({lang:'$globals.getLocale()'})"></b>
               </span>
           </li>
           <li>
           	<i class="lf iM">验收人员：</i>        	
               <span class="lf h-ic">
               	<input type="hidden" name="surveyor" value="" id="surveyor" />
				<input class="txt-l inp" type="text" name="surveyorName" value="" id="surveyorName"  readonly="readonly" ondblclick="publicPopSelect('userGroup','surveyor','normalId','false');"/>
                   <b class="icon-1 mag" onclick="publicPopSelect('userGroup','surveyor','normalId','false');"></b>
               </span>
           </li>
           <li>
           	<i class="lf iM">关联项目：</i>          
               	<select class="s-select" id="itemId" name="itemId">
               		<option value="">无关联</option>
               		#foreach($item in $itemList)
               			<option value="$!globals.get($item,0)" itemEndTime="$!globals.get($item,2)">$!globals.get($item,1)</option>
               		#end
               	</select>              
           </li>
       </ul>
    <div class="point-block">
		<input class="rf btn-cel" type="button" onClick="cel();" value="取消" />
		<span class="rf btn-add" operation="1" keyId="" onclick="taskSubmit(this);">添加</span>		
	</div>
</div>	
<!-- 弹出添加层 add 分割线 End -->
#end