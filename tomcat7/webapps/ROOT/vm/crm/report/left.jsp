<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<title>My JSP 'left.jsp' starting page</title>
<link type="text/css" rel="stylesheet" href="/style/css/crmReport.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
	
		$("#item2 a").click(function(){
			$("#item2 span").removeClass("spanColor");
			$(this).find("span").addClass("spanColor");	
		});
		$("#mainFrame",parent.document).attr("src","/vm/crm/report/explain.jsp?isCompare=$!compareFlag"); 
		
		$("span.reportHead").parent("a").siblings("ul").show();
	});
	
	
	function changeSrc(url){
		$("#mainFrame",parent.document).attr("src",url); 
	}
</script>
</head>

<body >
<input type="hidden" id="moduleId" value="$!moduleId">
<input type="hidden" id="firstEnter" name="firstEnter" value="$!firstEnter"/>
<div class="left_item" id="left_item">
<input type="hidden" id="compareFlag" name="compareFlag" value="$!compareFlag">
  #if("$!compareFlag" == "true")
 	 <div class="item1" id="item1">
  	  		<ul>
				<li>
					<a href="/CRMReportAction.do?type=left&classesName=clientInfo&tableName=CRMClientInfo&compareFlag=true">
						<span #if("$!classesName" == "clientInfo") class="reportHead" #end>客户数量同比环比</span>
					</a>
					<ul class="in-ul">
						<li><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&type=compare&moduleId=$!moduleId&fieldName=createTime&isSameQuery=true&captionName=新增客户数量同比')" ><span>新增客户数量同比</a></li>
	  	  				<li><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&type=compare&moduleId=$!moduleId&fieldName=createTime&isAround=true&captionName=新增客户数量环比')"><span>新增客户数量环比</a></li>
	  	  				<li><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&type=compare&moduleId=$!moduleId&fieldName=createTime&secondFieldName=ClientType&isAround=true&captionName=新增客户数量-客户类型环比')"><span>新增客户数量-客户类型环比</a></li>
	  	  				<li><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=createTime&secondFieldName=ClientType&isAround=true&captionName=客户类型-日期分组统计')"><span>客户类型-日期分组统计</a></li>
					  	<li><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=createTime&secondFieldName=createBy&isAround=true&captionName=所有者-日期分组统计')"><span>所有者-日期分组统计</a></li> 				
					</ul>
				</li>
				<li>
					<a href="/CRMReportAction.do?type=left&classesName=saleFollowUp&tableName=CRMSaleFollowUp&compareFlag=true">
						<span #if("$!classesName" == "saleFollowUp") class="reportHead" #end>联系记录同比环比</span>
					</a>
					<ul class="in-ul">
						<li><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&type=compare&tableName=CRMSaleFollowUp&isSameQuery=true&fieldName=VisitTime&captionName=联系记录数量同比')"><span>联系记录数量同比</span></a></li>
  	  					<li><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&type=compare&tableName=CRMSaleFollowUp&isAround=true&fieldName=VisitTime&captionName=联系记录数量环比')"><span>联系记录数量环比</span></a></li>
  	  					<li><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&type=compare&tableName=CRMSaleFollowUp&isAround=true&fieldName=VisitTime&secondFieldName=FollowPhase&captionName=联系记录数量-阶段环比')"><span>联系记录数量-阶段环比</span></a></li>
					</ul>
				</li>
			</ul>
	</div>
         #if("$classesName"=="clientInfo")
			<div class="l_nav">
				#foreach($module in $moduleList)
					#if($!moduleCountMap.get($!globals.get($module,1)) >0)
           		<p><a href="/CRMReportAction.do?type=left&classesName=clientInfo&moduleId=$globals.get($module,1)&compareFlag=true" #if("$!globals.get($module,1)" == "$moduleId") class="cur" #end><span style="writing-mode:tb-rl;">$globals.get($module,0)</span></a></p>
           			#end
              	#end
          </div>
		#end
  #else
	  <div class="item1">
			<ul>
				<li>
					<a href="/CRMReportAction.do?type=left&classesName=clientInfo&tableName=CRMClientInfo">
						<span #if("$!classesName" == "clientInfo") class="reportHead" #end>客户资料</span>
					</a>
					<ul class="in-ul">
						#foreach ($field in $fieldInfos)
							#if("$classesName"=="clientInfo" && "$field.statusId" == "1")
								#if("$field.inputType"=="1" && "$field.fieldName" != "Level" && "$field.fieldName" != "LastContractTime" && "$field.fieldName" != "extend1" && "$field.fieldName" != "extend2" && "$field.fieldName" != "extend3")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#elseif("$classesName"=="saleFollowUp" && "$field.fieldName"!="GenWorkPlan")
								#if("$field.inputType"=="1")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=$tableName&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#elseif("$classesName"=="saleContract" )
								#if("$field.inputType"=="1")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=$tableName&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#elseif("$classesName"=="maintainNote" )
								#if("$field.inputType"=="1")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=$tableName&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#end
						#end
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=Trade&captionName=客户行业分布')"><span>客户行业分布</a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=province&captionName=客户省份分布')" ><span>客户省份分布</a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=city&top=20&captionName=客户城市top20分布')"><span>客户城市top20分布</a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=createBy&captionName=客户所有者分布')" ><span>客户所有者分布</a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=createTime&captionName=客户创建数量月度分布')" ><span>客户创建数量月度分布</a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=ClientType&secondFieldName=createTime&captionName=客户创建数量月度/类型统计')" ><span>客户创建数量月度/类型统计</a></li>
					</ul>
				</li>
				<li>
					<a href="/CRMReportAction.do?type=left&classesName=saleFollowUp&tableName=CRMSaleFollowUp">
						<span #if("$!classesName" == "saleFollowUp") class="reportHead" #end>联系记录</span>
					</a>
					<ul class="in-ul">
						#foreach ($field in $fieldInfos)
							#if("$classesName"=="clientInfo" && "$field.statusId" == "1")
								#if("$field.inputType"=="1" && "$field.fieldName" != "Level" && "$field.fieldName" != "LastContractTime" && "$field.fieldName" != "extend1" && "$field.fieldName" != "extend2" && "$field.fieldName" != "extend3")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#elseif("$classesName"=="saleFollowUp" && "$field.fieldName"!="GenWorkPlan")
								#if("$field.inputType"=="1")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=$tableName&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#elseif("$classesName"=="saleContract" )
								#if("$field.inputType"=="1")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=$tableName&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#elseif("$classesName"=="maintainNote" )
								#if("$field.inputType"=="1")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=$tableName&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#end
						#end
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMSaleFollowUp&fieldName=FollowStatus&captionName=跟单状态分布')" ><span>跟单状态分布</a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMSaleFollowUp&fieldName=employeeId&captionName=跟单人分布')" ><span>跟单人分布</a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMSaleFollowUp&fieldName=FollowPhase&secondFieldName=employeeId&captionName=跟单人/阶段分布')" ><span>跟单人/阶段分布</span></a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMSaleFollowUp&fieldName=FollowStatus&secondFieldName=employeeId&captionName=跟单人/状态分布')" ><span>跟单人/状态分布</span></a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMSaleFollowUp&fieldName=VisitTime&captionName=跟单月份分布')" ><span>跟单月份分布</span></a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMSaleFollowUp&fieldName=VisitTime&secondFieldName=employeeId&captionName=跟单人/月份分布')" ><span>跟单人/月份统计</span></a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMSaleFollowUp&fieldName=VisitTime&secondFieldName=FollowPhase&captionName=月份统计/阶段分布')" ><span>阶段分布/月份统计</span></a></li>
						<li ><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMSaleFollowUp&top=30&fieldName=clientId&captionName=客户跟单次数top30统计')" ><span>客户跟单次数top30统计</span></a></li>
					</ul>
				</li>
				<li>
					<a href="/CRMReportAction.do?type=left&classesName=maintainNote&tableName=CRMClientService">
						<span #if("$!classesName" == "maintainNote") class="reportHead" #end>售后服务</span>
					</a>
					<ul class="in-ul">
						#foreach ($field in $fieldInfos)
							#if("$classesName"=="clientInfo" && "$field.statusId" == "1")
								#if("$field.inputType"=="1" && "$field.fieldName" != "Level" && "$field.fieldName" != "LastContractTime" && "$field.fieldName" != "extend1" && "$field.fieldName" != "extend2" && "$field.fieldName" != "extend3")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&moduleId=$moduleId&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#elseif("$classesName"=="saleFollowUp" && "$field.fieldName"!="GenWorkPlan")
								#if("$field.inputType"=="1")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=$tableName&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#elseif("$classesName"=="saleContract" )
								#if("$field.inputType"=="1")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=$tableName&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#elseif("$classesName"=="maintainNote" )
								#if("$field.inputType"=="1")
									<li id="$field.fieldName">
										<a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=$tableName&fieldName=$field.fieldName&captionName=$field.display.get($globals.getLocale())分布')">
											<span>$field.display.get("$globals.getLocale()")分布</span>
										</a>
									</li>
								#end
							#end
						#end
						<li id="$field.fieldName"><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMClientService&fieldName=BillDate&captionName=客户服务月度统计')" ><span>客户服务月度统计</span></a></li>
						<li id="$field.fieldName"><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMClientService&fieldName=clientId&top=10&captionName=客户服务次数top10')" ><span>客户服务次数top10</span></a></li>
						<li id="$field.fieldName"><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMClientService&fieldName=BillDate&secondFieldName=ServeType&captionName=客户服务类型/月度统计')" ><span>客户服务类型/月度统计</span></a></li>
						<li id="$field.fieldName"><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMClientService&fieldName=BillDate&secondFieldName=ServeState&captionName=客户服务状态/月度统计')" ><span>客户服务状态/月度统计</span></a></li>					
						<li id="$field.fieldName"><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMClientService&fieldName=BillDate&secondFieldName=clientId&captionName=客户服务次数/月度统计')" ><span>客户服务次数/月度统计</span></a></li>					
						<li id="$field.fieldName"><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMClientService&fieldName=employeeId&secondFieldName=clientId&captionName=客户服务次数/人员')" ><span>客户服务次数/人员</span></a></li>					
						<li id="$field.fieldName"><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMcomplaints&fieldName=BillDate&captionName=投诉月度统计')" ><span>投诉月度统计</span></a></li>
						<li id="$field.fieldName"><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMcomplaints&fieldName=clientId&secondFieldName=employeeId&captionName=人员/客户投诉次数')" ><span>人员/客户投诉次数</span></a></li>
						<li id="$field.fieldName"><a href="#" onclick="changeSrc('/CRMReportAction.do?operation=4&tableName=CRMcomplaints&fieldName=clientId&top=10&captionName=客户投诉次数top10')" ><span>客户投诉次数top10</span></a></li>					
					</ul>
				</li>
			</ul>
	  </div>
		#if("$classesName"=="clientInfo")
			<div class="l_nav">
				#foreach($module in $moduleList)
					#if($!moduleCountMap.get($!globals.get($module,1)) >0)
            	<p><a href="/CRMReportAction.do?type=left&classesName=clientInfo&moduleId=$globals.get($module,1)" #if("$!globals.get($module,1)" == "$moduleId") class="cur" #end><span style="writing-mode:tb-rl;">$globals.get($module,0)</span></a></p>
          		#end
              #end
          </div>
		#end
  #end	
</div>  
<script type="text/javascript">
	$("#left_item").height(document.documentElement.clientHeight);
</script>
</body>
</html>
