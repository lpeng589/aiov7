<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/oaCalendarAdd.css"/>
#if($brotherTableList.size()>0 || "$!alertEnter" == "true" || "$tableName" == "CRMPotentialClient" || "$tableName" == "CRMSaleFollowUp")
<link type="text/css" rel="stylesheet" href="/style/css/brother_detail.css"/>
#else
<link type="text/css" rel="stylesheet" href="/style/css/brother_add.css"/>
#end
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/crm_brother.js"></script>
<script type="text/javascript" src="/js/crm/crm_brotherPublic.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/oa/oaCalendarAdd.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=F24745217570fed2245750b7cf6b1947"></script>
<style type="text/css">
.det_dv .con_dv .det_cul .det_li .det_sp .ip_txt{width:120px;border: 0px;}
</style>
<script type="text/javascript">
var tempTableName;//用于添加单据成功后,异步刷新相应的邻居表部分
$(function(){
	//产品明细ul宽度
	var lLen = $(".det_ul .det_li").length;
	var lnum = lLen-1;
	$(".det_ul").width(lLen*125+16).find(".det_li:eq("+lnum+")").css("background","none");
	$(".det_cul").width(lLen*125+16);
	$(".bk_ul :text:visible").click(function(){
		jQuery(".inputBgColor").removeClass("inputBgColor");
		$(this).addClass("inputBgColor");
	});
	
	#if("$!isConsole" == "true")
		//获取服务与投诉信息




		parent.getRelateInfo('CRMClientService');
		parent.getRelateInfo('CRMcomplaints');
	#end
	
	/*循环邻居表部分显示列表*/
	$(".in_dv").each(function(){
		var tableName = $(this).attr("id");
		ajaxLoadBrotherList(tableName,'1');
	})
	
	/*点击分页触发事件*/
	$('.page_a').live('click', function() {
		var tableName = $(this).parent().attr("tableName");
		ajaxLoadBrotherList(tableName,$(this).attr("pageNo"));
	});
	
	/*点击分页跳转按钮*/
	$('.go').live('click', function() {
		var selectPageNo = $(this).prev().val();//当前选择的页数



		var tableName = $(this).parent().attr("tableName");
		if(checkPageNo(selectPageNo,totalPage)){
			ajaxLoadBrotherList(tableName,selectPageNo);
		}
	});
	//快速定位



	var bWidth = document.body.offsetWidth;
	$("#quickPositionDiv").css("left",bWidth-115);
	
	
	
	$(".viewItem").mouseover(function(){
		$(this).find(".viewList").show();
	}).mouseout(function(){
		$(this).find(".viewList").hide();
	});
	//单击li 显示下拉内容
	jQuery(".col_name").toggle(
	  function () {
	    jQuery(".bd ul").each(function(i){
			jQuery(this).find(":checkbox").removeAttr("checked") ;
		});
		jQuery(this).css("background-position","0 -644px").siblings("div.minfo").show().siblings(":checkbox").attr("checked","checked");
		
		var url = "CRMBrotherAction.do?type=comment&operation=4&keyId="+$(this).prev().val()+"&tableName="+$("#tableName").val()+"&employeeId="+$(this).attr("employeeId");
		$(this).parent().find(".minfo iframe").attr("src",url);
		$("#commentFrameId").val($(this).parent().find(".minfo iframe").attr("id"));//展开时用一个隐藏域记录当时的iframe的Id,方便评论页面评论后改变详情里面的高度
	  },
	  function () {
	  	jQuery(".bd ul").each(function(i){
			jQuery(this).find(":checkbox").removeAttr("checked") ;
		});
	    jQuery(this).css("background-position","0 -1031px").siblings("div.minfo").hide().siblings(":checkbox").attr("checked","checked");
	  }
	);
})

function scrollDiv(){
	var sHeight=document.documentElement.clientHeight;
	document.getElementById("contentDiv").style.height=sHeight-53+"px";
}


function reverse(){
	window.location.href="/UserFunctionQueryAction.do?tableName=$!tableName&designId=$!designId&keyId=$result.get("id")&f_brother=$!result.get("ClientId")&operation=$globals.getOP("Op_RETAUDITING")&winCurIndex=$!winCurIndex&pageNo=$!pageNo&parentTableName=CRMClientInfo&saveDraft=$!saveDraft&crmReCheck=true";
}


/*撤回*/
function cancelFlow(){
	var url ='/OAMyWorkFlow.do?nextStep=cancel&lastNodeId=$lastNodeId&keyId=$result.get("id")&currNode=$result.get("workFlowNode")&designId=$designId&operation=$globals.getOP("Op_AUDITING")&winCurIndex=$!winCurIndex&crmCancel=true&crmReCheck=true&isBrother=true&tableName=$tableName'
	window.location.href=url;
}
//打印
function nowPrint(){
	var sHeight = $("#contentDiv").height();
	$("#contentDiv").height("100%");
	window.print();
	$("#contentDiv").height(sHeight);
}

/*客户更新*/
function update(tableName,keyId,asynId){
	var urls = '/CRMBrotherAction.do?operation=7&tableName='+tableName+"&keyId="+keyId;
	var isConsole = $("#isConsole").val();//true表示从客户控制台进入
	
	if(isConsole == "true"){
		window.location.href=urls+"&isConsole=true";
	}else{
		var height = 470;
		var winHeight = 0;
		#if($brotherTableList.size()>0 || "$!alertEnter" == "true" || "$tableName" == "CRMPotentialClient" || "$tableName" == "CRMSaleFollowUp")
			//若是新的详情页面就当时页面的高度
			winHeight = $("#contentDiv").height();
		#else
			if(parent.$("#clientId").height()==null){
				//表示从客户合同、销售机会、客户列表点击邻居表详情->修改
				winHeight = parent.$("#contentDiv").height();
			}else{
				//弹出框页面就用父类index.jsp的高度




				winHeight = parent.$("#clientId").height()+30;
			}
		#end
		if(winHeight<height){
			height = winHeight;
		}
		asyncbox.open({
			id:asynId,url:urls,title:'修改单据',width:780,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	opener.beforeSubmit();
					return false;
				}
	　　  　	}
	　    });
	}
}

</script>
</head>
<body class="body" onload="scrollDiv();">
#set($showAttachment = "false") 
#set($updateOp = "false")
#if($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$!tableName").update() &&( $allowUpdUserIds.indexOf(",$loginBean.id,")>-1 || $loginBean.id=="1" || $loginBean.id=="$brother.get('createBy')"))
#set($updateOp = "true")
#end
<div class="bd" style="overflow:hidden;overflow-y:auto;" id="contentDiv">
<input type="hidden" name="tableName" id="tableName" value="$tableName">
<input type="hidden" name="isConsole" id="isConsole" value="$!isConsole">
<input type="hidden" name="keyId" id="keyId" value="$!result.get("id")">
<input type="hidden" id="relationId" name="relationId" value="$!result.get("id")"/>
#set($keyName = "SaleContractId")
#if("$tableName" == "CRMSalesChance")
	#set($keyName = "SalesChanceId")
#elseif("$tableName" == "CRMPotentialClient")
	#set($keyName = "CRMPotentialClient.id")
#else
#end
<input type="hidden" name="keyInfo" id="keyInfo" value='${keyName}:$!result.get("id")'>
	<div class="inputbox brotherInputbox">
		<div class="head_btns">
			#if("$tableName" == "CRMPotentialClient")
			<div class="viewItem">
				<span><i class="viewSelect">客户转移</i><b></b></span>
				<ul class="viewList" style="display:none;">
					<ul class="viewList" style="display:none;">
						#foreach($module in $moduleList)
							<li onclick="clientTransfer('detail','$globals.get($module,0)')">
								<span class='$globals.get($module,0)'>$globals.get($module,1)</span>
							</li>
						#end
					</ul>
				</ul>
			</div>
			#end
			#if("$updateOp" == "true")
				#if(("$!OAWorkFlow" == "") || ("$!OAWorkFlow" == "true" && $result.get("workFlowNode")=="0"))
					<div class="btn btn-small rf" onclick="update('$tableName','$!result.get("id")','detailUpdateId');">修改</div>
				#end
			#end
			<div class="btn btn-small rf" onclick="alertSet('$!result.get("id")','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');">提醒</div>
			#if("$!isConsole" !="true")
				#if("$!printFlag" == "true" || "$!loginBean.id" == "1")
		        <div class="btn btn-small rf" onclick="nowPrint();">打印</div>
		        #end
				#if("$!OAWorkFlow" == "true"&&$result.get("workFlowNode")!="-1"&&$result.get("checkPersons").indexOf(";${loginBean.id};")>-1)
					#set($keyId = $result.get("id"))
					<div class="btn btn-small rf" onclick="approve('$keyId','deliver','$tableName')">审核</div>
				#end
				#if("$!retCheckRight" == "true" && $result.get("workFlowNode")=="-1")
					<!--<div class="btn btn-small rf" onclick="reverse()">反审核</div> -->
				#end
				#if("$!allowCancel" == "true")
					<!-- <div class="btn btn-small rf" onclick="cancelFlow()">撤回</div> -->
				#end
			#end
		</div>
		<!-- 主表展示开始 -->
		#foreach ($maps in $mainMap.keySet())	
			<div class="f_dv">
				<div class="title_dv">
					<em class="txt" id="${tableName}_$!maps">#if("$!globals.getEnumerationItemsDisplay($tableName,$maps)" == "") 默认分组  #else $globals.getEnumerationItemsDisplay("$tableName","$maps") #end</em>
				</div>
				<ul class="bk_ul">
					#foreach ($row in $mainMap.get($maps))
						#set($fieldBean = $globals.getFieldBean($tableName,$row))
						#if("$!tableName"=="CRMSaleFollowUp" && "$row" == "calendarBtn")
							<li class="bk_li" >
				        		<div class="swa_c1" title='下次回访日程'>下次回访日程：</div>
				        		#if("$!calendarBean" == "")
					        		<button class="btn btn-mini" type="button" onclick="addCalendar()" id="addCalendarBtn">添加日程</button>
				        		#else
				        			<span relationId="$!calendarBean.relationId" class="calendarDetail">关联日程</span>
				        		#end
				        	</li>
						#elseif("$fieldBean.inputType" == "0")
							#if("$fieldBean.fieldType" == "1")
					        	<li class="bk_li">
									<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!globals.dealDoubleDigits("$!result.get($fieldBean.fieldName)","amount")">$!globals.dealDoubleDigits("$!result.get($fieldBean.fieldName)","amount")</div>
					        	</li>
							#elseif("$fieldBean.fieldType" == "5")
								<li class="bk_li">
					        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!result.get($fieldBean.fieldName)">$!result.get($fieldBean.fieldName)</div>
					        	</li>	
							#elseif("$fieldBean.fieldType" == "6")
								<li class="bk_li ">
					        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!result.get($fieldBean.fieldName)">$!result.get($fieldBean.fieldName)</div>
					        	</li>
							#elseif("$fieldBean.fieldType" == "3" || ("$fieldBean.fieldType" == "16"))
								<li class="bk_li ess">
					        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<textarea class="ip_txt" style="background:#f8f8f8;" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!result.get($fieldBean.fieldName)">$!result.get($fieldBean.fieldName)</textarea>
					        	</li>
							#elseif("$fieldBean.fieldType" == "13"||"$fieldBean.fieldType" == "14")
								#set($showAttachment = "true") 
							#elseif("$fieldBean.fieldType" == "18")
								<li class="bk_li ess">
					        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
									<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!result.get($fieldBean.fieldName)">$!result.get($fieldBean.fieldName)</div>
					        	</li>
							#else
								#if("$!tableName"=="CRMPotentialClient" && "$fieldBean.fieldName"=="Trade")
									<li class="bk_li">
						        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
										<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!workProfessionMap.get($!result.get($fieldBean.fieldName))">$!workProfessionMap.get($!result.get($fieldBean.fieldName))</div>
						        	</li>
								#elseif("$!tableName"=="CRMPotentialClient" && "$fieldBean.fieldName"=="District")
									<li class="bk_li">
						        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
										<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!districtMap.get($!result.get($fieldBean.fieldName))">$!districtMap.get($!result.get($fieldBean.fieldName))</div>
					        	
						        	</li>
								#else
								<li class="bk_li ">
					        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
									<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!result.get($fieldBean.fieldName)">$!result.get($fieldBean.fieldName)</div>
					        	</li>
					        	#end
							#end
						#elseif("$fieldBean.inputType" == "1")
							<li class="bk_li">
				          		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
					            <div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title='$globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$!result.get($fieldBean.fieldName)")'>$globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$!result.get($fieldBean.fieldName)")</div>
				           	</li>
						#elseif("$fieldBean.inputType" == "2")
							#if("$fieldBean.fieldName" == "ClientId")
								<li class="bk_li ess">
									<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)" />
									<input type="hidden" name="${fieldBean.fieldName}Name" id="${fieldBean.fieldName}Name" value="$!result.get('CRMClientInfo.ClientName')" />
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title='$!result.get('CRMClientInfo.ClientName')'>$!result.get('CRMClientInfo.ClientName')</div>
					        	</li>
				        	#else
					        	<li class="bk_li">
									<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!result.get($fieldBean.fieldName)">$!result.get($fieldBean.fieldName)</div>
					        	</li>
				        	#end
						#elseif("$fieldBean.inputType" == "5")
						#elseif("$fieldBean.inputType" == "8")
							#set($val = $!result.get($fieldBean.fieldName))
							#if("$fieldBean.fieldType" == "1")
								#set($val = $!globals.dealDoubleDigits("$!result.get($fieldBean.fieldName)","amount"))
							#end
							
							<li class="bk_li #if("$fieldBean.fieldType" == "18") ess #end ">
								<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!val">$!val</div>
				        	</li>
						#elseif("$fieldBean.inputType" == "10")
						#elseif("$fieldBean.inputType" == "14")
							<li class="bk_li">
								<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!deptMap.get($!result.get($fieldBean.fieldName))">$!deptMap.get($!result.get($fieldBean.fieldName))</div>
				        	</li>
						#elseif("$fieldBean.inputType" == "15")
							<li class="bk_li">
								<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!globals.getEmpFullNameByUserId($!result.get($fieldBean.fieldName))">$!globals.getEmpFullNameByUserId($!result.get($fieldBean.fieldName))</div>
				        	</li>
						#elseif("$fieldBean.inputType" == "20")
							#set($enumValue = "")
							#set($optionList = $selectMap.get($fieldBean.fieldName))
							#if("$!optionList" !="")
								#foreach($option in $optionList)
									#if("$globals.get($option,0)" == "$!result.get($fieldBean.fieldName)")
										#set($enumValue = $globals.get($option,1))
									#end
				            	#end
							#end
				           	<li class="bk_li">
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" title="$enumValue">$enumValue</div>
				        	</li>
						#else
							<li class="bk_li">
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!result.get($fieldBean.fieldName)">$!result.get($fieldBean.fieldName)</div>
				        	</li>
						#end
					#end
		        </ul>
	        </div>
        #end
        <!-- 轨迹开始 -->
        #if("$tableName" == "CRMSaleFollowUp" && "$!result.get('Trail')" != "" )
			<div id="allmap" style="width: 600px;height:300px;overflow: hidden;margin:0;"></div>
		#end
		<!-- 轨迹结束 -->
        <!-- 主表展示结束 -->
       
        <!-- 明细 NewStart -->
      	
        <div class="det_dv">
	        
	        	#if("$!tableName" == "CRMSaleContract" || "$!tableName" == "CRMsalesQuot")
	        	<div class="title_dv">
		        	<em class="txt" id="childInfo">明细信息</em>
		        </div>
		        
		        <div class="con_dv">
		        	<ul class="det_ul">
			        	<li class="det_li"><i>产品名称</i></li>
			        	<li class="det_li"><i>计量单位</i></li>
			        	<li class="det_li"><i>数量</i></li>
			        	<li class="det_li"><i>单价</i></li>
			        	<li class="det_li"><i>合计</i></li>
			        	
			        </ul>
			        <ul class="det_cul" id="goodsContent">
		        	#foreach ($child in $!result.get("TABLENAME_${childTableName}")) 
						<li class="det_li">
							<span class="det_sp">
								<input type="hidden" class="ip_txt" name="${childTableName}_GoodsCode" value='$!child.get("GoodsCode")'/>
								<input type="text" class="ip_txt" value='$!child.get("tblGoods.GoodsFullName")'/>
							</span>
							<span class="det_sp">
								<input type="hidden" class="ip_txt" name="${childTableName}_Unit" value="$!child.get("Unit")"/>
								
								#set($unitName = "Unit.Unit")
								#if("$!tableName" == "CRMsalesQuot")
									#set($unitName = "tblUnit.UnitName")
								#end
								<input type="text" class="ip_txt" value='$!child.get("$unitName")'/>
							</span>
							<span class="det_sp">
								#set($Qty = "")
								#set($Qty = $!child.get("Qty"))
								<input type="text" class="ip_txt" name="${childTableName}_Qty" value='$globals.substring("$Qty",".")'/>
							</span>
							<span class="det_sp">
								#set($price = "")
								#set($price = $!child.get("Price"))
								<input type="text" class="ip_txt" name="${childTableName}_Price" value='$!globals.dealDoubleDigits("$price","amount")'/>
							</span>
							<span class="det_sp">
								#set($Amount = "")
								#set($Amount = $!child.get("Amount"))
								<input type="text" class="ip_txt" name="${childTableName}_Amount" value='$!globals.dealDoubleDigits("$Amount","amount")'/>
							</span>
						</li>
						
			       #end 	
			       </ul>
			       </div>
	        	#else
	        		#if("$!childTableName" != ""  && "$!fieldDisplayBean.pageChildFields" !="")
	        		<div class="title_dv">
			        	<em class="txt" >明细信息</em>
			        	<i class="add" onclick="addChild()" ></i>
			        </div>
			        
			        <div class="con_dv" id="childContent">
			        	#foreach ($maps in $childMap.keySet())
				        	<ul class="det_ul">
				        		#foreach ($row in $childMap.get($maps))
				        			#set($fieldBean = $globals.getFieldBean($childTableName,$row))
						        	<li class="det_li"><i>$fieldBean.display.get("$globals.getLocale()")</i></li>
				        		#end 
					        </ul>
					        #foreach ($child in $!result.get("TABLENAME_${childTableName}")) 
						        <ul class="det_cul">
						        	<li class="det_li">
						        	#foreach ($row in $childMap.get($maps))
							        	#set($fieldBean = $globals.getFieldBean($childTableName,$row))
										#if("$fieldBean.inputType" == "0")
											<span class="det_sp"><input type="text" class="ip_txt" id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName" value="$!child.get($fieldBean.fieldName)"/></span>
										#elseif("$fieldBean.inputType" == "1")
											<span class="det_sp">
												<input type="text" class="ip_txt" id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName" value="$!globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$!child.get($fieldBean.fieldName)")"/>
											</span>
										#elseif("$fieldBean.inputType" == "2")
											<span class="det_sp">
												<input type="hidden" id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName" value="$!child.get($fieldBean.fieldName)" class="ip_txt" />
												<input  name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value="$child.get($fieldBean.fieldName)"  type="text" class="inp_w" />
											</span>
										#elseif("$fieldBean.inputType" == "5")
										#elseif("$fieldBean.inputType" == "10")
										#elseif("$fieldBean.inputType" == "14")
										#elseif("$fieldBean.inputType" == "15")
										#elseif("$fieldBean.inputType" == "14")
										#else
										#end
							        #end
						        	</li>
						        </ul>
					        #end
					    #end
			        
		        </div>
	        	#end
	        	#end
        </div>
        <!-- 明细 NewEnd -->
        <!-- 附件开始 -->
     	#if("$showAttachment" == "true")
     		#if("$!result.get('Attachment')" != "")
	        <div class="title_dv">
	        	<em class="txt" id="attachmentInfo">附件管理</em>
	        </div>
		        #set($fieldInfoBean = $globals.getFieldBean("$tableName","Attachment"))
		        #if("$fieldInfoBean.fieldType" == "13")
			        <ul id="picuploadul_uploadBtn">
			        	#foreach($uprow in $result.get("Attachment").split(";"))
							#if("$!uprow" !="")
							<li style='background:url();' id='uploadfile_$uprow'><input type=hidden id="uploadBtn" name="uploadBtn" value='$uprow'>
							<div><a class="img_wa" href="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" target="_blank"><img src="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" border="0"></a></div><div>$uprow&nbsp;&nbsp;</div>
							</li>
							#end
						#end
			        </ul>
		        #else
			        <ul id="affixuploadul_uploadBtn">
			        	#foreach($uprow in $result.get("Attachment").split(";"))
							#if($uprow != "")
								<li class="file_li" id="uploadfile_$uprow">
								<input type=hidden id="uploadBtn" name=uploadBtn value='$uprow'/><div class="showAffix">$uprow</div>&nbsp;&nbsp;&nbsp;			
									&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=$tableName" target="_blank">$text.get("common.lb.download")</a>
								</li>
							#end
						#end
			        </ul>
		        #end
	        #end
	    #end
	    <!-- 附件结束-->
       
        <div class="icon32_btns">
        	#if($brotherTableList.size()>0 && $loginBean.operationMap.get("/CRMSalesFlowAction.do").query())
        	<div class="btn_item" onclick="flowSetting();">
        		<em ></em>
        		<i>流程设置</i>
        	</div>
        	#end
        </div>

        <!-- 流程展示开始 -->
        #if($brotherTableList.size()>0)
	        #if("$!flowList" !="")
	        	#foreach($flow in $flowList)
	        	#set($filterTables = "")
        		#foreach($tableName in $globals.get($flow,2).split(","))
        		#if("$!moduleMap.get($tableName)" != "" && ($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$tableName").query() || $loginBean.operationMap.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=$tableName").query()))
			    	#set($filterTables = $filterTables + $tableName +",")
			    #end    	
        		#end
        		#if($filterTables != "")
		        	<div class="det_dv">
			        	<div class="title_dv">
				        	<em class="txt" id="$globals.get($flow,0)">$globals.get($flow,0)</em>
				        </div>
			        	<div class="in_wdv">
			        		#foreach($tableName in $filterTables.split(","))
				        		<div class="in_dv" id="$tableName" ></div>
			        		#end
					    </div>    
			        </div>
        		#end
	        	#end
	        #else
	        	#set($filterTables = "")
        		#foreach($tableBean in $brotherTableList)
	        		#if("$!moduleMap.get($tableName)" != "" && ($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$tableBean.tableName").query() || $loginBean.operationMap.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=$tableBean.tableName").query()))
	        			#set($filterTables = $filterTables +$tableBean.tableName +",")
	        		#end
        		#end
        		#if($filterTables != "")
		        	<div class="det_dv">
			        	<div class="title_dv">
				        	<em class="txt" id="defaultSaleFlow">默认流程</em>
				        </div>
			        	<div class="in_wdv">
			        		#foreach($tableName in $filterTables.split(","))
				        		<div class="in_dv" id="$tableName" ></div>
			        		#end
					    </div>    
			        </div>
        		#end
	        #end
     	#end
     	
     	#if("$tableName" == "CRMPotentialClient")
     		<div class="det_dv">
	        	<div class="title_dv">
		        	<em class="txt" id="defaultSaleFlow">联系记录</em>
		        </div>
	        	<div class="in_wdv" >
		        		<div class="in_dv" id="CRMSaleFollowUp" style="width: 99%"></div>
			    </div>    
	        </div>
     	#end
     	
     	#if("$tableName" == "CRMSaleFollowUp")
     		<div class="title_dv" style="border:0;">
				<em class="txt" style="padding:0 25px;border-bottom:5px solid #468dc8;">评论</em>
			</div>
     		
	     	#set($keyId = $!result.get("id"))
			<iframe src="/DiscussAction.do?tableName=CRMSaleFollowUpLog&&parentIframeName=Frame_${keyId}&f_ref=${keyId}" width="100%" frameborder=no scrolling="no" id="Frame_${keyId}"></iframe>
		#end
       	<!-- 流程展示开始 -->
	</div>
</div>
#if($brotherTableList.size()>0)
<!-- 定位导航 Start -->
<div style="position:fixed;top:45px;width:200px;" id="quickPositionDiv">
	<div class="fix_dv">
		<em id="quickPosition">快速定位</em>
		<ul style="display:none;">
			#foreach ($maps in $mainMap.keySet())
				<li>
					<a href="#" titleName="${tableName}_$!maps">#if("$!globals.getEnumerationItemsDisplay($tableName,$maps)" == "") 默认分组  #else $globals.getEnumerationItemsDisplay("$tableName","$maps") #end</a>
				</li>
			#end
			#if("$!childTableName" != "")
			<li>
				<a href="#" titleName="childInfo">明细信息</a>
			</li>
			#end
			#if("$showAttachment" == "true" && "$!result.get('Attachment')" != "")
			<li>
				<a href="#" id="attachmentInfo">附件管理</a>
			</li>
	        #end	
	        #if($brotherTableList.size()>0)
		        #if("$!flowList" !="")
		        	#foreach($flow in $flowList)
		        		<li>
							<a href="#" titleName="$globals.get($flow,0)">$globals.get($flow,0)</a>
						</li>
		        	#end
		        #else
		        	<li>
						<a href="#" titleName="defaultSaleFlow">默认流程</a>
					</li>
		        #end
	        #end
		</ul>
	</div>
</div>
<!-- 定位导航 End -->
#end




<div class="add-calendar" id="addCalendar" style="display:none;"></div>
</body>
</html>

<script type="text/javascript">

// 百度地图API功能
//var trail = "114.141819,22.558604;114.053283,22.530298;113.986592,22.54098;113.968195,22.466188";
var trail = "114.179091,22.571569;114.179091,22.572569;114.179109,22.57367;114.179109,22.57467;114.179109,22.57567;114.179109,22.57667;114.179161,22.577478000000003;114.179161,22.578478;114.179083,22.579473;114.179083,22.580473;";
if(trail.charAt(trail.length-1)==";"){
	trail = trail.substring(0,trail.length-1);
}

var map = new BMap.Map("allmap");
//map.centerAndZoom("北京",12);                   // 初始化地图,设置城市和地图级别。

map.addControl(new BMap.NavigationControl());   
map.enableScrollWheelZoom();   
/* 
var pointA = new BMap.Point(114.141819,22.558604);  // 创建点坐标A--大渡口区
var pointB = new BMap.Point(114.053283,22.530298);  // 创建点坐标B--江北区

var pointC = new BMap.Point(113.986592,22.54098);  // 创建点坐标B--江北区

var pointD = new BMap.Point(113.986592,22.54098);  // 创建点坐标B--江北区

var pointE = new BMap.Point(113.998666,22.479547);  // 创建点坐标B--江北区

var pointF = new BMap.Point(113.968195,22.466188);  // 创建点坐标B--江北区

var pointG = new BMap.Point(113.968195,22.466188);  // 创建点坐标B--江北区

var pointH = new BMap.Point(113.968195,22.466188);  // 创建点坐标B--江北区

var pointI = new BMap.Point(114.316594,22.200348);  // 创建点坐标B--江北区

var pointZ = new BMap.Point(114.222308,22.564745);  // 创建点坐标B--江北区

*/
//alert('从大渡口区到江北区的距离是：'+map.getDistance(pointA,pointB)+' 米。');     //获取两点距离
var strArr = trail.split(";");
var prevLeft = 0.0;
var prevRight = 0.0;
var nowLeft = 0.0;
var nowRight = 0.0;
for(var i=0;i<strArr.length;i++){
	if(i==0){
		prevLeft = strArr[i].split(",")[0];
		prevRight = strArr[i].split(",")[1];
		map.centerAndZoom(new BMap.Point(prevLeft,prevRight), 14);// 初始化地图,设置城市和地图级别。  
	}else{
		nowLeft = strArr[i].split(",")[0];
		nowRight = strArr[i].split(",")[1];
		
		var pointA = new BMap.Point(prevLeft,prevRight);  // 创建点坐标A--大渡口区
		var pointB = new BMap.Point(nowLeft,nowRight);  // 创建点坐标B--江北区

		var polyline = new BMap.Polyline([pointA,pointB], {strokeColor:"red", strokeWeight:3, strokeOpacity:0.5});  //定义折线
		map.addOverlay(polyline);   //添加折线到地图上
		prevLeft = nowLeft;
		prevRight = nowRight;
	}
}
</script>
