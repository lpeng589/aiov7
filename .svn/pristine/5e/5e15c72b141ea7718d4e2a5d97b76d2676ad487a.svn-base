#if($workFlowList.size()>0)
<table cellpadding="0" cellspacing="0" class="Block_word_list">
	#foreach($flow in $workFlowList)
	<tr>
		<td>
			#if($!workFlowDesignBeans.get($!flow.applyType).getFlowNodeMap().get("$!flow.currentNode")&&$!flow.currentNode!=-1&&$!flow.checkPerson.indexOf(";$!LoginBean.id;")>=0)
			 	#if("$!flow.workFlowType"!="1")
			 	   #set($pan = $!globals.getTableInfoBean($!flow.tableName).perantTableName)
			 	   #if($pan.indexOf(";") > 0) #set($pan = $pan.substring(0,$pan.indexOf(";")) )  #end
					<a href="javascript:mdiwin('/UserFunctionAction.do?tableName=$!flow.tableName&parentTableName=$!pan&noback=true&nextNodeIds=$!flow.nextNodeIds&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")&winCurIndex=$!winCurIndex&designId=$!flow.applyType&oaTimeLimit=$!flow.benchmarkTime&oaTimeLimitUnit=$!flow.oaTimeLimitUnit','$text.get("oa.my.workflow")')"><img src="/style/images/desktop/Edit_icon.gif"/>$globals.getTableDisplayName($!flow.tableName) $!flow.applyContent</a>&nbsp;		      			 
			 	#else
			 		<a href="javascript:mdiwin('/OAWorkFlowAction.do?tableName=$!flow.tableName&noback=true&nextNodeIds=$!flow.nextNodeIds&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")&winCurIndex=$!winCurIndex&designId=$!flow.applyType&oaTimeLimit=$!flow.benchmarkTime&oaTimeLimitUnit=$!flow.oaTimeLimitUnit','$text.get("oa.my.workflow")')"><img src="/style/images/desktop/Edit_icon.gif"/>$!flow.applyContent</a>&nbsp;		      			 
			 	#end
			#else
			 	#if("$!flow.workFlowType"!="1")
			 	   #set($pan = $!globals.getTableInfoBean($!flow.tableName).perantTableName)
			 	   #if($pan.indexOf(";") > 0) #set($pan = $pan.substring(0,$pan.indexOf(";")) )  #end
					<a href="javascript:mdiwin('/UserFunctionAction.do?tableName=$!flow.tableName&parentTableName=$!pan&noback=true&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&winCurIndex=$!winCurIndex&designId=$!flow.applyType&approveStatus=transing','$text.get("oa.my.workflow")')">$!globals.getTableDisplayName($!flow.tableName) $!flow.applyContent</a>&nbsp;
				#else
					<a href="javascript:mdiwin('/OAWorkFlowAction.do?tableName=$!flow.tableName&noback=true&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&winCurIndex=$!winCurIndex&designId=$!flow.applyType&approveStatus=transing','$text.get("oa.my.workflow")')">$!flow.applyContent</a>&nbsp;
				#end
			#end
			<div>
				<span>$!flow.applyBy</span>
				<span>#if($!workFlowInfo.get("$!flow.applyType").getLanguage())$!workFlowInfo.get("$!flow.applyType").getLanguage().get($globals.getLocale())#else$!workFlowInfo.get("$!flow.applyType").getModuleName()#end</span>
				<span>$!flow.applyDate</span>
				<span>$!flow.currNodeName ($!flow.checkPersonName)</span>
				<span style="cursor:pointer;" onclick="javascript:flowDepict('$!flow.applyType','$!flow.billId');">流程</span>
			</div>
		</td>
	</tr>
	#end
</table>
#end