#if("$flowAdd" != "true")	
	<span class="type-s">
		<i class="type-tags #if("$!approveStatus"=="transing")sel#end" flowType="transing">待审</i>
		<i class="type-tags #if("$!approveStatus"=="entrust")sel#end" flowType="entrust">委托</i>
		<i class="type-tags #if("$!approveStatus"=="handling")sel#end" flowType="handling">办理中</i>
		<i class="type-tags #if("$!approveStatus"=="finish")sel#end" flowType="finish">已办结</i>
	</span>
	#foreach($flow in $list)
	#if($velocityCount < 8)
	<div class="lt-ind">
		<div class="wf-item">
			<span class="s-i">
				<a href="#" class="i-a">
					<img class="de-img" src="$globals.checkEmployeePhoto('140',$!flow.createBy)" alt="" />
				</a>
				$!flow.applyBy
			</span>
			<span class="s-c">
				<a href="#" class="t-a">$!flow.templateName</a>
				#if($!workFlowDesignBeans.get($!flow.applyType).getFlowNodeMap().get("$!flow.currentNode")&&$!flow.currentNode!=-1&&$!flow.checkPerson.indexOf(";$!LoginBean.id;")>=0)
			    	#if("$!flow.workFlowType"!="1")
			     		<a href="javascript:openPopup('/UserFunctionAction.do?tableName=$!flow.tableName&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")','$!flow.templateName');" class="c-a">$!flow.applyContent</a>
			    	#else
			    	  	<a href="javascript:openPopup('/OAWorkFlowAction.do?tableName=$!flow.tableName&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")','$!flow.templateName');" class="c-a">$!flow.applyContent</a>		      			 	
			    	#end
			    #else
			      	#if("$!flow.workFlowType"!="1")
					    <a href="javascript:openPopup('/UserFunctionAction.do?tableName=$!flow.tableName&noback=true&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&designId=$!flow.applyType&approveStatus=$!approveStatus','$text.get("oa.my.workflow")')" class="c-a">$!flow.applyContent</a>
				 	#else
				 	  	<a href="javascript:openPopup('/OAWorkFlowAction.do?tableName=$!flow.tableName&nextNodeIds=$!flow.nextNodeIds&currNodeId=$!flow.currentNode&&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&designId=$!flow.applyType&approveStatus=$!approveStatus','$text.get("oa.my.workflow")')" class="c-a">$!flow.applyContent</a>	      			 	
				 	#end
				#end
				<a class="tm-a">$!flow.applyDate $!flow.createTime</a>
			</span>	
			<span class="s-step" style="cursor:pointer;" onclick="javascript:flowDepict('$!flow.applyType','$!flow.billId');">
				#if("$!flow.lastNodeName"!="")<em class="prev-em"><b class="icon-1"></b>$!{flow.lastNodeName}($!{flow.lastNodeCheckperson})</em>#end
				<em class="now-em"><b class="icon-1"></b>$!{flow.currNodeName}#if("$!flow.checkPersonName"!="")($!{flow.checkPersonName})#end</em>
			</span>
			#if($!approveStatus=="finish")
			<span class="dt-tm"><p>$!flow.lastUpdateTime</p><p>$!flow.finishTime</p></span>
			#end			
			<span class="s-handle">
			#if($!flow.lastCheckPerson.indexOf($LoginBean.id)!=-1 && $!flow.currentNode!=-1)	  
				  	<a href=javascript:if(confirm('$text.get("oa.workFlow.Cancel")'))location.href='/OAMyWorkFlow.do?nextStep=cancel&lastNodeId=$!flow.lastNodeId&keyId=$!flow.id&currNode=$!flow.currentNode&designId=$!flow.applyType&operation=$globals.getOP("Op_AUDITING")'>$text.get("common.msg.Cancel")</a>&nbsp;	
				  #end  			  
			      #if($!workFlowDesignBeans.get($!flow.applyType).getFlowNodeMap().get("$!flow.currentNode")&&$!flow.currentNode!=-1&&$!flow.checkPerson.indexOf(";$!LoginBean.id;")>=0)
		      		  #if("$!flow.workFlowType"!="1")
		      		  	<a href="javascript:openPopup('/UserFunctionAction.do?tableName=$!flow.tableName&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")&designId=$!flow.applyType&approveStatus=$!approveStatus','$!flow.templateName');">$text.get("oa.myTransaction.Disposeing")</a>&nbsp;		      			 
		      		  #else
		      		 	 <a href="javascript:openPopup('/OAWorkFlowAction.do?tableName=$!flow.tableName&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")&designId=$!flow.applyType','$!flow.templateName');">$text.get("oa.myTransaction.Disposeing")</a>&nbsp;		      			 	
		    	  	  #end
				      #if($!flow.nextNodeIds.length()>0&&$!flow.currNodeDetID) 
					  	<a href="javascript:deliverTo('/OAMyWorkFlow.do?tableName=$!flow.tableName&keyId=$!flow.billId&currNodeId=$!flow.currentNode&nextNodeIds=$!flow.nextNodeIds&department=$!flow.departmentCode&designId=$!flow.applyType&operation=$globals.getOP("OP_AUDITING_PREPARE")&winCurIndex=$!winCurIndex')">$text.get("common.lb.DeliverTo")</a>&nbsp;		      		
					  #else
					  &nbsp;&nbsp;&nbsp;&nbsp;
					  #end
				  #else
					  #if($!workFlowDesignBeans.get($!flow.applyType).getFlowNodeMap().get("$!flow.currentNode")&&!$!flow.currentNode.equals("-1")&&$LoginBean.id.equals($flow.createBy))
					  	 <a href="javascript:hurryTransDialog('createBy=$!flow.createBy&applyType=$!flow.applyType&tableName=$!flow.tableName&keyId=$!flow.id&checkPerios=$!flow.checkPerson&approveStatus=transing&nextNodeIds=$!flow.nextNodeIds&currNodeId=$!flow.currentNode&billId=$!flow.billId&oaTimeLimit=$!flow.benchmarkTime&oaTimeLimitUnit=$!flow.oaTimeLimitUnit&content=$!flow.applyContent&applyBy=$!flow.applyBy&moduleName=#if($!workFlowInfo.get("$!flow.applyType").getLanguage())$!workFlowInfo.get("$!flow.applyType").getLanguage().get($globals.getLocale())#else$!workFlowInfo.get("$!flow.applyType").getModuleName()#end')">$text.get("common.lb.transaction")</a>&nbsp;	
					  #end
					  #if("$!flow.workFlowType"!="1")
					  	 <a href="javascript:openPopup('/UserFunctionAction.do?tableName=$!flow.tableName&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&designId=$!flow.applyType&approveStatus=$!approveStatus','$!flow.templateName');">$text.get("common.lb.detail")</a>&nbsp;
					  #else
					  	<a href="javascript:openPopup('/OAWorkFlowAction.do?tableName=$!flow.tableName&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&designId=$!flow.applyType','$!flow.templateName');"> $text.get("common.lb.detail")</a>&nbsp;
					  #end
				  #end			
			</span>
			<div class="clear"></div>
		</div>
	</div>
	#end
	#end
	<script type="text/javascript">
	function openPopup(strUrl,strTitle){
		var width = 1024;
		if($(window).width()>1024) width = 1150;
		openPop('PopMainOpdiv',strTitle,strUrl,width,500,false,false);
	}
	function flowDepict(applyType,keyId){ 
		window.open("/OAMyWorkFlow.do?keyId="+keyId+"&operation=5&applyType="+applyType,null,"height=570, width=1110,top=50,left=100");
	}
	/*催办弹出框*/
	function hurryTransDialog(param){
		var url = "/vm/oa/workflow/noteModel.jsp?"+encodeURI(param);
		asyncbox.open({
			id:'deliverTo',url:url,title:'$text.get("workflow.msg.transactionWarmKind")',
	　　 　 width : 750,height : 300,btnsbar : jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
		    	if(action == 'ok'){
					if(opener.submitModenew(opener.form)){
						opener.form.submit();
					}
					return false;
			  	}
	　   　  }
		});
	}
	/*新转交窗口*/
	function deliverTo(url){
		asyncbox.open({
			id:'dealdiv',url:url,title:'转交',width:650,height:370,
	        btnsbar : jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
					if(opener.beforSubmit(opener.form)){
						opener.form.submit();
					}
					return false;
				}
	　　  　}
	　 });
	}
	function dealAsyn(){
		jQuery.close('deliverTo');
	}
	</script>
#else
<div class="oa-workflow">
#set($str=",")
#foreach($pal in $result)
	#if(!($str.indexOf($!globals.get($pal,5))>-1))
	#set($str=$str+"$!globals.get($pal,5)"+",")
		#foreach($flow in $result)	
	       	#if($!globals.get($flow,5)==$!globals.get($pal,5))
	       		#if("$globals.get($flow,12)"!="1")
	         		#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=$!globals.get($flow,8)").add())             			
	         			<a href="javascript:addFlow('/UserFunctionQueryAction.do?tableName=$!globals.get($flow,8)&src=menu&operation=6&designId=$!globals.get($flow,0)','$!globals.get($flow,1)');">$!globals.get($flow,1)</a>             			
	         		#end
	       		#else
	       			<a href="javascript:addFlow('/OAWorkFlowAction.do?tableName=$!globals.get($flow,8)&src=menu&operation=6&designId=$!globals.get($flow,0)','$!globals.get($flow,1)');">$!globals.get($flow,1)</a>
	       		#end
			#end
		#end
	#end
#end
</div>
<script type="text/javascript">
function addFlow(strUrl,strTitle){
	var width = 1024;
	if($(window).width()>1024) width = 1124;
	openPop('PopMainOpdiv',strTitle,strUrl,width,500,true,false);
}
</script>

#end