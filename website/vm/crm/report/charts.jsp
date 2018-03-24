<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/crmReport.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/crmReportNew.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/iCharts.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/FusionCharts.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/crm/crm_report.js"></script>
<script type="text/javascript" src="/js/crm/ichart.1.1.min.js"></script>
<script type="text/javascript"> 
var gloChartData = "";//全局变量存放aChart数据
$(document).ready(function(){
	//开始异步根据id加载行业与地区的信息
	if($("#district").val() != ""){
		ajaxGetPopInfo('district',$("#district").val(),$("#popDistrict"));
	}
	if($("#trade").val() != ""){
		ajaxGetPopInfo('trade',$("#trade").val(),$("#popTrade"));
	}
	//查看是否有枚举多选项,大于2个新增清空按钮
	var str = '<li name="emptyBtn"><button type="button" onclick="clearEnum(this)" class="clearBtn">清空</button> </li>'
	$("#searchCondition").find("ul").each(function(){
		if($(this).find("input:checked").length>=2){
			$(this).append(str);
		}
	})
	//先判断高级查询
	#if("$!highSearch"=="")
		changeSearch('')
	#else
		changeSearch('high')
	#end
	//生成统计图
	#if("$!chart" != "")
		getChart('${chartSwfName}','$!msDatas.size()','$!enumList.size()')
	#end
	/*iChart统计图
	var height = 450;//默认高度
	if("$!iChartName" == "Bar2D"){
		//个数多于30
		#if($dataList.size() >=30)
			height = 750;
		//个数多于40	
		#elseif($dataList.size() >=40)
			height = 900;
		#else
			height = 650;
		#end
	}
	var coordinateWidth = $("#chartWidth").val();//图标宽度
	var coordinateHeight = height;//图标高度
	#if("$!secondFieldName" == "")
		gloChartData = $!chart;
		//普通统计图
		#if("$iChartName" == "LineBasic2D")
			//折线图
			var chart = new iChart.LineBasic2D({
					render : 'canvasDiv',
					data: $chart,
					title : '$!captionName',
					width : $("#chartWidth").val(),
					height : height,
					#if($dataList.size() >=12)
					//个数多于12个字体斜着放
					label : {
						textAlign:'right',
						textBaseline:'middle',
						rotate:-45,
						color : '#000'
					},	
					#end
					tip:{
						enable:true,
						animation:true
					},
					coordinate:{height:'90%',background_color:'#f6f9fa'},
					sub_option:{
						hollow_inside:false,//设置一个点的亮色在外环的效果
						point_size:16
					},
					labels:$labelList
				});
			chart.draw();
			//折线图只有1个，不显示图
			#if($dataList.size() <=1)
				$("#canvasDiv").hide();
			#end
		#else
			#set($offsetx = 0)
			#if("$!iChartName" == "Column2D" && $dataList.size() >=12)
				coordinateHeight = coordinateHeight-200;
			#elseif("$!iChartName" == "Bar2D")
				
				#if("$!fieldName" == "clientId" || "$!fieldName" == "trade" || "$!fieldName" == "f_brother" )
					#set($offsetx = 100)
				#end
				coordinateWidth = coordinateWidth-250;
			#end
			//普通统计图
			new iChart.$iChartName({
				render : 'canvasDiv',
				data: $chart,
				title : '$!captionName',
				width : $("#chartWidth").val(),
				height : height,
				offsetx : $offsetx,
				animation : true,
				#if("$!iChartName" == "Column2D" && $dataList.size() >=12)
				//突击图是Column2D且个数多于12个字体斜着放
				label : {
					textAlign:'right',
					textBaseline:'middle',
					rotate:-45,
					color : '#000'
				},	
				#end
				tip:{
					enable:true,
					animation:true
				},
				coordinate:{
					background_color:'#fefefe',
					width:coordinateWidth,
					height:coordinateHeight,
					scale:[{
						 #if("$!iChartName" == "Bar2D")
						  	 position:'bottom',
						 #elseif("$!iChartName" == "Column2D")
							 position:'left',
						 #end
						 start_scale:0,
					}]
				}
			}).draw();
		#end
	#else
	coordinateWidth = coordinateWidth -270;
	#if($msDatas.size()>12)
		coordinateHeight = coordinateHeight-200;
	#end
	var chart = new iChart.ColumnMulti2D({
			render : 'canvasDiv',
			data: $dataList,
			labels:$labelList,
			title : '$!captionName',
			//footnote : '数据来源：销售中心',
			width : $("#chartWidth").val(),
			height : 500,
			background_color : '#ffffff',
			animation : true,
			#if("$!secondFieldName" == "clientId")
			offsety : -50,
			#end
			legend:{
				enable:true,
				background_color : null,
				#if("$!secondFieldName" == "clientId")
				offsety : -20,
				#end
				border : {
					enable : true
				}
				
			},
			#if($msDatas.size()>12)
			label : {
				textAlign:'right',
				textBaseline:'middle',
				rotate:-45,
				color : '#000'
			},
			#end
			tip:{
				enable:true,
				animation:true
			},
			coordinate:{
				background_color : '#f1f1f1',
				scale:[{
					 position:'left',	
					 start_scale:0,
				}],
				width:coordinateWidth,
				height:coordinateHeight
			}
	});
	chart.draw();
	#end
	*/
});

/*报表明细*/
function clientDetail(enumVal){
	jQuery("#detailFrame").attr("src","/CRMReportAction.do?operation=4&type=detailList&tableName=$!tableName&moduleId=$!moduleId&fieldName=$fieldName&enumVal="+enumVal+"&condition="+encodeURIComponent(encodeURIComponent($("#condition").val())));
	$("#chartContainer").hide();
	$("#chartUpDown").removeClass();
    $("#chartUpDown").addClass("chartDown");
	$("#detailFrame").show();
}
	
//处理双击个人部门弹出框

function fillData(datas){
	dbPopDatas(datas)
	jQuery.close('Popdiv');
}

/*点击报表客户明细方法(邻居表详情)*/	
function brotherDetail(url,showName){
	asyncbox.open({id:'reportBroDeatil',title:showName,modal:true,url:url,width:780,height:450});
}

function dealAsyn(){
	/*方法1*/
	jQuery.close('reportBroDeatil');
	jQuery.close('detailUpdateId');//关闭修改页面	
	window.frames["detailFrame"].pageSubmit(1);//刷新列表
	/*
	方法2：

	//报表点击客户名称详情是弹出框的,点击修改成功后,关闭修改页面,刷新详情页面
	if(jQuery.exist('reportBroDeatil')){
		jQuery.close('detailUpdateId');//关闭修改页面	
		jQuery.opener('reportBroDeatil').window.location.reload();//刷新详情页面
	}
	*/
}
</script>
</head>

<body>
<form action="/CRMReportAction.do?operation=4" method="post" name="form">&nbsp;
<input type="hidden" id="type" name="type" value=""> 
<input type="hidden" id="chartData" name="chartData" value="$!chart">
<input type="hidden" id="moduleId" name="moduleId" value="$!moduleId">
<input type="hidden" id="tableName" name="tableName" value="$!tableName">
<input type="hidden" id="fieldName" name="fieldName" value="$!fieldName">
<input type="hidden" id="secondFieldName" name="secondFieldName" value="$!secondFieldName">
<input type="hidden" id="captionName" name="captionName" value="$!captionName">
<input type="hidden" id="condition" value="$condition">
<input type="hidden" id="isSearch" name="isSearch" value="true">
<input type="hidden" id="chartSwfName" name="chartSwfName" value="$chartSwfName">
<input type="hidden" id="chartWidth" value="">
<input type="hidden" id="userGroupIds" name="userGroupIds" value="$!userGroupIds">
<input type="hidden" id="deptGroupIds" name="deptGroupIds" value="$!deptGroupIds">
<input type="hidden" id="popName" name="popName" value="">
<input type="hidden" id="district" name="district" value='$!paramMap.get("district")'/>
<input type="hidden" id="trade" name="trade" value='$!paramMap.get("trade")'/>
<input type="hidden" id="tempDistrictIds" name="tempDistrictIds" value=""/>
<input type="hidden" id="removeSearch" name="removeSearch" value="false"/>
<input type="hidden" id="searchSwfName" name="searchSwfName" value="$!searchSwfName"/>
<input type="hidden" id="sumFieldName" name="sumFieldName" value="$!sumFieldName"/>
<input type="hidden" id="isAround" name="isAround" value="$!isAround"/>
<input type="hidden" id="highSearch" name="highSearch" value="$!highSearch">
<input type="hidden" id="CrmClickGroupIds" name="CrmClickGroupIds" value="$!clientIds">
<input type="hidden" id="dbTableName" name="dbTableName" value="$!dbTableName">
<input type="hidden" id="top" name="top" value="$!top">
#set($timeName = "查询日期")
#set($searchTimeName = "BillDate")
#if("$!tableName" == "CRMSaleFollowUp")
	#set($searchTimeName = "VisitTime")
	#set($timeName = "跟单日期")
#elseif($!tableName.indexOf("CRMSaleContract") != -1)
	#set($searchTimeName = "SignUpDate")
	#set($timeName = "签约日期")
#elseif("$!tableName" == "tblMaintainNote")
	#set($timeName = "服务日期")
#elseif("$!tableName" == "CRMSaleReceive")
	#set($timeName = "单据日期")
#elseif("$!tableName" == "CRMcomplaints")	
	#set($timeName = "投诉日期")
#end
<input type="hidden" id="searchTimeName" name="searchTimeName" value="$!searchTimeName">
<div style="overflow-y: auto;" id="bodyDiv">
<script type="text/javascript">
	var oDiv=document.getElementById("bodyDiv");
	var chartWidth=document.getElementById("chartWidth");
	var sHeight=document.documentElement.clientHeight-15;
	var sWidth=document.documentElement.clientWidth-10;
	oDiv.style.height=sHeight+"px";
	oDiv.style.width=sWidth+"px";
	chartWidth.value = sWidth-50;
</script>
<!-- 查询条件开始 -->
	#if("$!isAround" != "true")
	<div class="rt_table2" id="searchCondition">
		<ul class="tags-ul">
			<li class="sel-li" onclick="changeSearch('',this)">普通查询</li>
			<li onclick="changeSearch('high',this)">高级查询</li>
		</ul>
		<div class="d-tbl-sel">
		<table border="0" width="100%" cellspacing="0" cellpadding="0">
		#if("$!moduleId" != "")
			#foreach($col in $moduleViewBean.keyFields.split(","))
				#if(!$globals.isExist("$col","contact"))
					<tr #if("$col" == "ClientName") class="commonSearch" #end>
						 <td class="tbleft">$globals.getColName($col,$clientTableName,$contactTableName):</td>
						 <td align="left"><input class="inp" type="text" name="$col" value='$!paramMap.get("$!col")' #if("$col" == "ClientName") style="width: 75%;height: 18px;" #end></td>
					</tr>
				#end
			#end
			<tr class="commonSearch" >
			    <td class="tbleft">建立时间:</td>
			    <td align="left" ><input class="inp" type="text" name="createTimeStart" onClick="WdatePicker({lang:'$globals.getLocale()'})"  onKeyDown="if(event.keyCode==13) submitQuery();" value="$!paramMap.get('createTimeStart')" />&nbsp; 至 &nbsp;<input class="inp" type="text" name="createTimeEnd" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value="$!paramMap.get('createTimeEnd')"/></td>
			</tr>
			<tr>
			    <td class="tbleft">修改时间:</td>
			    <td align="left"><input class="inp" type="text" name="lastUpdateTimeStart" onClick="WdatePicker({lang:'$globals.getLocale()'})"  onKeyDown="if(event.keyCode==13) submitQuery();" value="$!paramMap.get('lastUpdateTimeStart')" />&nbsp; 至 &nbsp;<input class="inp" type="text" name="lastUpdateTimeEnd" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value="$!paramMap.get('lastUpdateTimeEnd')"/></td>
			</tr>
			#foreach($col in $moduleViewBean.searchFields.split(","))
				#set($field=$globals.getField($col,$clientTableName,$contactTableName))
				#if("$field.inputType"=="1")
				<tr>
					<td class="tbleft">#if($col== "LastContractTime")未联系天数#else$globals.getColName($col,$clientTableName,$contactTableName)#end:</td>
					<td align="left" class="h-c">
						 #foreach($item in $globals.getEnumerationItems("$field.refEnumerationName"))
						 	#set($enumval = "")
						 	#set($itemVal = ","+$item.value + ",")
							#set($enumval = ","+$!paramMap.get($field.fieldName)+",")
						 	#if("$field.fieldName"=="LastContractTime")
						 	<label class="l-c-r">
						 		<input class="c-radio" type="radio" name="$field.fieldName" value="$item.value" #if("$!paramMap.get($field.fieldName)" == "$item.value") checked="checked" #end />
						 		<em>$item.name</em>
						 	</label>
						 	#else
						 	<label class="l-c-r">
						 		<input class="c-box" type="checkbox" name="$field.fieldName" value="$item.value" #if($enumval.indexOf("$itemVal") != -1) checked="checked" #end>
						 		<em>$item.name</em>
						 	</label>
						 	#end
						 #end
							#if("$field.fieldName"=="LastContractTime" || "$field.inputType"=="10")
							<button type="button" onclick="clearEnum(this)" class="clearBtn" name="noClearBtn">清空</button>
						 	#else
						 		#set($enumval = ","+$!paramMap.get($col)+",")
					 		<label class="l-c-r">
						 		<input class="c-box" type="checkbox" name="$col" value="empty" #if($enumval.indexOf(",empty,") != -1) checked="checked" #end>
						 		<em>空</em>
					 		</label>
						 	#end
					</td>
				</tr>	
				#end
			#end
			<tr>
				<td class="tbleft">行业查询:</td>
				<td align="left" id="tradeTd"><a href="#" id="popTrade" onclick="popTrade(this)">选择</a></td>
			</tr>
			<tr>
				<td class="tbleft">地区查询:</td>
				<td align="left" id="districtTd"><a href="#" id="popDistrict" onclick="popDistrict(this)" >选择</a></td>
			</tr>
			#else
				<tr class="commonSearch">
					<td class="tbleft">$timeName:</td>
					<td align="left"><input type="text" name="searchTimeStart" onClick="WdatePicker({lang:'$globals.getLocale()'})"  onKeyDown="if(event.keyCode==13) submitQuery();" value="$!searchTimeStart" />&nbsp; 至 &nbsp;<input type="text" name="searchTimeEnd" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value="$!searchTimeEnd"/></td>
				</tr>
				#set($tableInfoBean = $globals.getTableInfoBean($!tableName))
				#foreach($field in $tableInfoBean.fieldInfos)
					#if("$!field.inputType"=="1" && "$!field.fieldName" != "GenWorkPlan")
						<tr>
							<td class="tbleft">$field.display.get("$globals.getLocale()"):</td>
							<td align="left">
								<ul style="float: left;">
								 #foreach($item in $globals.getEnumerationItems("$field.refEnumerationName"))	
								 	#set($itemVal = ","+ $item.value + ",")
									#set($enumval = ","+$!paramMap.get($field.fieldName)+",")
								 	<li ><input class="enumBox" type="checkbox" name="$field.fieldName" value="$item.value" #if($enumval.indexOf($itemVal) != -1) checked="checked" #end>&nbsp;$item.name</li>
								 #end
								 	#if("$field.inputType"=="10")
										<li ><button type="button" onclick="clearEnum(this)" class="clearBtn" name="noClearBtn">清空</button> </li>
								 	#else
								 		#set($enumval = ","+$!paramMap.get($field.fieldName)+",")
								 		<li><input class="enumBox" type="checkbox" name="$field.fieldName" value="empty" #if($enumval.indexOf(",empty,") != -1) checked="checked" #end>&nbsp;空&nbsp;</li>
								 	#end
								 </ul>
							</td>
						</tr>
					#end
				#end
			#end
			<tr class="commonSearch" >
				<td class="tbleft">#if("$!tableName" == "CRMClientInfo") 所有者查询#elseif("$!tableName" == "CRMSaleFollowUp")跟单人查询#elseif($!tableName.indexOf("CRMSaleContract") != -1)签约人查询#elseif("$!tableName" == "tblMaintainNote")实施人查询#elseif("$!tableName" == "CRMcomplaints")投诉对象 #else人员查询 #end:</td>
				<td align="left" id="userGroupTd">
				#foreach($userId in $userGroupIds.split(","))
					<li ><span id="$deptId">$globals.getEmpNameById($userId)</span><img src='/style/images/plan/del_02.gif' onclick="delPopId(this,'$userId','userGroup')"></li>
				#end
				<a href="#" id="userGroup" onclick="popSelect('userGroup',this)" >选择</a>
				
				</td>
			</tr>
			<tr class="commonSearch" >
				<td class="tbleft">部门查询:</td>
				<td align="left" id="deptGroupTd">
				#foreach($deptId in $deptGroupIds.split(","))
					#if("$!deptId" != "")
					<li><span id="$deptId">$!deptMap.get("$deptId")</span><img src='/style/images/plan/del_02.gif' onclick="delPopId(this,'$deptId','deptGroup')"></li>
					#end
				#end
				<a href="#" id="deptGroup" onclick="popSelect('deptGroup',this)">选择</a></td>
			</tr>
			
			<tr class="commonSearch" >
				<td class="tbleft">客户查询:</td>
				<td align="left" id="CrmClickGroupTd">
				#foreach($client in $clientInfo)
					<li ><span id="$!globals.get($client,0)">$!globals.get($client,1)</span><img src='/style/images/plan/del_02.gif' onclick="delPopId(this,'$!globals.get($client,0)','CrmClickGroup')"></li>
				#end
				<a href="#" id="CrmClickGroup" onclick="popSelect('CrmClickGroup',this)" >选择</a></td>
			</tr>
			
			<tr class="commonSearch">
				<td  style="border-bottom: 0px;" align="center" colspan="2"> <input class="btn" name="" type="button" onclick="querySubmit()" value="查询" /></td>
			</tr>
		</table>
		</div>
	</div>
	#end
	<!-- 查询条件结束 -->
	
	#if("$!hasCondition" == "true")<h1 class="f_b"><span style="background-color: #FFCC99;cursor: pointer;" onclick="relieveSearch();">解除搜索 显示全部数据</span><img src="/style/images/client/back.gif" width="25px" height="15xp;" onclick="relieveSearch();"> </h1> #end
  		<!-- OnTop Start -->
  		<div class="OnTop">
      	<p>
        	<span>$!captionName</span>
        </p>
        <br>
	#if("$!isAround" == "true")
	<div class="Wrap">
  		<!-- OnTop Start -->
  		<div class="OnTop">
        <div class="SanBu" style="width: 500px;">
        	<ul class="SanBuU">
            <li class="SanBuLi">
              <div class="SanBuLiLeft">
                第1步：时 间 段



              </div>
              <div class="SanBuLiRight">
              	#set($startName = "searchTimeStart")
              	#set($endName = "searchTimEnd")              	
              	#if("$!tableName" == "CRMClientInfo")
	              	#set($startName = "createTimeStart")
              		#set($endName = "createTimeEnd")  	
              	#else
              	#end
                <input  name="$startName"  id="$startName" type="text" class="txtInput"   onClick="WdatePicker({lang:'$globals.getLocale()'})"  value="$!paramMap.get($startName)"> 至 <input name="$endName"  id="$endName" type="text" class="txtInput"   onClick="WdatePicker({lang:'$globals.getLocale()'})"  value="$!paramMap.get($endName)"/>
              </div>
            </li>
          </ul>
        </div>
        <div class="ChaKanTongJi">
        	<input type="button" class="ChaKanBtn" onclick="querySubmit()"/>
        </div>
      </div>
      <!-- OnTop End -->
      </div>
	#end
	#if($!count == 0)
		<div style="margin: 100px 100px; 30px; 0;"><span>很抱歉，没有找到与您条件匹配的数据! </span></div>
	#else
	<div class="right_item">
		#if("$!msDatas"=="")
			<!-- 普通统计开始 -->
			<div class="rt_table3">
					#if("$!fieldInfoBean.inputType" == "1")
					<table cellspacing="0" cellpadding="0">
						<thead>
							<tr >
								#foreach($data in $dataList)
									<td > #if("$!globals.get($data,1)" == "") 空 #else $!globals.get($data,1) #end </td>
								#end
									<td >合计</td>
							</tr>
						</thead>
						<tbody>
							<tr >
								#set($count = 0)
								#foreach($data in $dataList)
									#set($count = $math.add($count,$!globals.get($data,2)))
									<td >
									#if($fieldName.indexOf("saleContract")!=-1 || $fieldName.indexOf("saleReceive") != -1)
										$!globals.get($data,2)
									#else
										<a href="#" onclick="clientDetail('$!globals.get($data,0)');">$!globals.dealDoubleDigits($!globals.get($data,2),$!sumFieldName) </a>
									#end
									</td>
								#end
									<td class="total">$globals.dealDoubleDigits("$count","$!sumFieldName")</td>
							</tr>
						</tbody>
					</table>
					#else
					<table cellspacing="0" cellpadding="0">
						#set($count = 0)
						<thead>
								<tr >
									<td align="left">$xName</td>
									<td align="left">$yName</td>
								</tr>
						</thead>
						<tbody>
								#foreach($data in $dataList)
								<tr >
									#set($count = $math.add($count,$!globals.get($data,2)))
									<td  width="70%" align="left" bgcolor="#f7f7f7"> #if("$!globals.get($data,1)" == "") 空 #else $!globals.get($data,1) #end </td>
									
									<td style="background: none;" align="left">
									#if($fieldName.indexOf("saleContract")!=-1 || $fieldName.indexOf("saleReceive") != -1)
										$!globals.get($data,2)
									#else
										<a href="#" onclick="clientDetail('$!globals.get($data,0)');">$!globals.dealDoubleDigits($!globals.get($data,2),$!sumFieldName) </a>
									#end
									</td>
								</tr>	
								#end
								<tr >
									<td bgcolor="#f7f7f7" align="left">合计</td>
									<td class="total" align="left">$globals.dealDoubleDigits("$count","$!sumFieldName")</td>
								</tr>
						</tbody>		
					#end
				</table>
			</div>
			<!-- 普通统计结束 -->
		#else
			<!-- 多条件统计开始 -->
			<div class="rt_table3" style="overflow: auto;" id="mulConTable">
				<table border="1" cellspacing="0" cellpadding="0">
				<thead>
					<tr >
						<td>&nbsp; </td>
						#set($arounCount = 0)
						#foreach($enum in $enumList)
						#set($arounCount = $math.add($!globals.get($enum,2),$arounCount))
						<td >#if("$!globals.get($enum,1)"=="")空 #else $!globals.get($enum,1)#end</td>
						#end
						<td>总计</td>
						#if("$!isAround" == "true")<td>百分比</td>#end
					</tr>
				</thead>
				<tbody>
				#foreach($data in $msDatas)
					<tr name="mulTr"> 
						#foreach($row in $data)
							#if($velocityCount == $data.size())
								#set($rowTotal = $row)
							#end
							<td  #if($velocityCount == 1)bgcolor="#F7F7F7" #elseif($velocityCount == $data.size()) class="total" #end>#if("$!row" == "") 空 #else #if($velocityCount == 1) $row #else $globals.dealDoubleDigits($row,"$!sumFieldName")  #end #end</td>
						#end
						#if("$!isAround" == "true")<td class="total">$globals.getPercent("$rowTotal","$arounCount")</td>#end
					</tr>
				#end	
				<tr class="total">
					#set($total = 0)
					<td bgcolor="#F7F7F7">总计</td>
					#foreach($enum in $enumList)
						<td>#if("$!globals.get($enum,2)"=="")0 #else #set($total = $math.add($!globals.get($enum,2),$total)) $globals.dealDoubleDigits($!globals.get($enum,2),"$!sumFieldName")#end</td>
					#end
					<td>$globals.dealDoubleDigits("$total","$!sumFieldName")</td>
					#if("$!isAround" == "true")<td>&nbsp;</td>#end
				</tr>
				#if("$!isAround" == "true")
				<tr class="total">
					<td bgcolor="#F7F7F7">百分比</td>
					#foreach($enum in $enumList)
						<td >$globals.getPercent("$!globals.get($enum,2)","$arounCount")</td>
					#end
					<td >&nbsp;</td>
					<td >100%</td>
				</tr>
				#end
				</tbody>
				</table>	
			</div>
			<!-- 多条件统计结束 -->
		#end
		<!-- 
		#if("$!iChartName" == "Column2D")
		<h1 style="margin-left: 20px;">显示类型: 
			<a href="#" onclick="changePie2D()">2D饼状图</a>&nbsp;&nbsp;
			<a id="chartUpDown" href="#" class="chartUp"></a>
		</h1>
		#end
		<div id='canvasDiv'></div>	
		 -->
		<!-- 统计图类型选择开始 -->
		<h1 style="margin-left: 20px;">显示类型: 
			#if("$!secondFieldName" == "")
			<a href="#" onclick="choiceChart('Column3D','','')">3D柱图</a>&nbsp;&nbsp; 
			<a href="#" onclick="choiceChart('Column2D','','')">2D柱图</a>&nbsp;&nbsp; 
			<a href="#" onclick="choiceChart('Line','','')">折线图</a>&nbsp;&nbsp;
			<a href="#" onclick="choiceChart('Bar2D','','')">2D条形图</a>&nbsp;&nbsp;
			<a href="#" onclick="choiceChart('Pie3D','','')">3D饼图</a>&nbsp;&nbsp;
			#else
			<a href="#" onclick="choiceChart('MSColumn3D','$!msDatas.size()','$!enumList.size()')">3D柱图</a>&nbsp;&nbsp; 
			#end
			<a id="chartUpDown" href="#" class="chartUp"></a>
		</h1>
	 	<!-- 统计图类型选择结束 -->		
	 	
		<!-- 统计图显示开始 -->	
		<div id="chartContainer" style="overflow-x: auto;"></div>
		<!-- 统计图显示结束 -->
		<!-- 详情显示iframe -->
		<iframe id="detailFrame" name="detailFrame" width="100%"  height="300px" frameborder=no scrolling="no" style="display: none;"></iframe>
		
	#end
	
	
	
	<div style="height: 2px;">
	</div>		
	</div>		
</form>
</body>
</html>
