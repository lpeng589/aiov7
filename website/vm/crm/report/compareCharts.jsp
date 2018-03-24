<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<title>My JSP 'textReport.jsp' starting page</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/crmReportNew.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/crmReport.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/iCharts.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/FusionCharts.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/crm/crm_report.js"></script>
<script type="text/javascript" src="/js/crm/ichart.1.1.min.js"></script>
<style type="text/css">
body{font-size: 12px;}
input[type=checkbox] { vertical-align: -3px;}
tr{text-align: center;}
#showMsData ul li{float: left;list-style-type: none;width: 80px;height:25px; border: 1px solid black;text-align: center;}
.changeDown{color: green;}
.changeUp{color: red;}
#searchCondition td{text-align: left;}
#enumUl li{width: 100px;}
</style>
<script type="text/javascript">
/*
ichart 统计图
$(function(){
	var width = $("#chartWidth").val();//默认宽度
	#set($iChartName = "Bar2D");
	#if($dataList.size() >=15)
		height = 650;
	#elseif($dataList.size() >=25)
		height = 800;
	#else
		height = 450;
		#set($iChartName = "Column2D");
	#end
	var coordinateWidth = width -250;
	#if("$!isAround" == "true" && "$!secondFieldName" == "")
		new iChart.$iChartName({
			render : 'canvasDiv',
			data: $dataList,
			title : '$!captionName',
			decimalsnum:2,
			width : width,
			height : height,
			animation : true,
			tip:{
				enable:true,
				animation:true
			},
			coordinate:{
				background_color:'#fefefe',
				scale:[{
					 position:'left',	
				}]
			}
		}).draw();
	#else
		var chart = new iChart.ColumnMulti2D({
			render : 'canvasDiv',
			data: $chart,
			labels:$labelList,
			title : '$!captionName',
			//footnote : '数据来源：销售中心',
			width : width,
			height : height,
			background_color : '#ffffff',
			animation : true,
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
				width:coordinateWidth,
				scale:[{
					 position:'left',	
					 start_scale:0,
				}],
			}
		});
		chart.draw();
	#end


})
*/


/*个人与部门弹出框*/
function popSelect(popName,obj){
	$("#popName").val(popName);//选择弹出框名字,
	var title = "";
	if(popName == "userGroup"){
		title = "选择个人"
	}else{
		title = "选择部门"
	}
	var url ="/Accredit.do?popname="+popName+"&inputType=checkbox&chooseData="+$("#"+popName+"Ids").val();
	asyncbox.open({
		id : 'Popdiv',url : url,title : title,width :755,height : 450,
		btnsbar :[{text:'清空',action:'selectAll'}].concat(jQuery.btn.OKCANCEL),
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		var selIds = "";//ids
	    		var showUsers = "";//封装显示内容
	    		for(var i=0;i<str.split("|").length-1;i++){
	    			var arrVal = str.split("|")[i];
	    			showUsers += '<li class="SuoYouZheLi" id="'+arrVal.split(";")[0]+'"><a class="WordName">'+arrVal.split(";")[1]+'</a><a class="CloseImg" popSelId="'+arrVal.split(";")[0]+'" popName="'+popName+'"></a></li>'
		    		selIds +=arrVal.split(";")[0] + ",";
	    		}
	    		jQuery("#"+popName+"Ids").val(selIds);//popName + Ids,把选择的IDS存放到隐藏域提交后台查询
	    		var liCount = jQuery(obj).parent().parent().find("li").length-1;//计算有多少个li并除去最后一个
	    		//先删除所有内容
	    		if(liCount>=1){
	    			jQuery(obj).parent().parent().find("li:lt("+liCount+")").remove();
	    		}
	    		//添加内容
	    		jQuery(obj).parent().before(showUsers);
	    	} 
			if(action == 'selectAll'){
				var liCount = jQuery(obj).parent().parent().find("li").length-1;//计算有多少个li并除去最后一个
	    		//先删除所有内容
	    		if(liCount>=1){
	    			jQuery(obj).parent().parent().find("li:lt("+liCount+")").remove();
	    		}
				$("#"+popName+"Ids").val("");//把隐藏域ID清空
			}
	    }
　  });
}	

//处理双击个人部门弹出框
function fillData(datas){
	var id = datas.split(";")[0];
	var val = datas.split(";")[1];
	var popName = $("#popName").val()
	var str = '<li class="SuoYouZheLi" id="'+id+'"><a class="WordName">'+val+'</a><a class="CloseImg" popSelId="'+id+'" popName="'+popName+'"></a></li>';
	if($("#"+popName+"Div").find("li[id='"+id+"']").length==0){
		$("#"+popName).parent().before(str);
		var popVal = $("#"+popName+"Ids").val() +id +",";
		$("#"+popName+"Ids").val(popVal);
	}else{
		delPopId($("#"+popName+"Div").find("li[id='"+id+"']").find(".CloseImg"),id,popName);//删除
	}
	jQuery.close('Popdiv');
}




</script>
</head>

<body>
<form action="/CRMReportAction.do?operation=4" method="post" name="form">
<input type="hidden" id="type" name="type" value="compare">
<input type="hidden" id="chartData" name="chartData" value="$!chart">
<input type="hidden" id="moduleId" name="moduleId" value="$!moduleId">
<input type="hidden" id="isDetail" name="isDetail" value="$!isDetail">
<input type="hidden" id="tableName" name="tableName" value="$!tableName">
<input type="hidden" id="fieldName" name="fieldName" value="$!fieldName">
<input type="hidden" id="secondFieldName" name="secondFieldName" value="$!secondFieldName">
<input type="hidden" id="captionName" name="captionName" value="$!captionName">
<input type="hidden" id="condition" value="$condition">
<input type="hidden" id="isSearch" name="isSearch" value="true">
<input type="hidden" id="sumFieldName" name="sumFieldName" value="$!sumFieldName">
<input type="hidden" id="isAround" name="isAround" value="$!isAround">
<input type="hidden" id="secondFieldName" name="secondFieldName" value="$!secondFieldName">
<input type="hidden" id="chartSwfName" name="chartSwfName" value="$chartSwfName">
<input type="hidden" id="searchSwfName" name="searchSwfName" value=""/>
<input type="hidden" id="chartWidth" value="">
<input type="hidden" id="userGroupIds" name="userGroupIds" value="$!userGroupIds">
<input type="hidden" id="deptGroupIds" name="deptGroupIds" value="$!deptGroupIds">
<input type="hidden" id="popName" name="popName" value="">
<input type="hidden" id="removeSearch" name="removeSearch" value="false"/>
<input type="hidden" id="isSameQuery" name="isSameQuery" value="$!isSameQuery">
<input type="hidden" id="firstEnter" name="firstEnter" value="false">
<input type="hidden" id="dbTableName" name="dbTableName" value="$!dbTableName">
<div style="overflow: auto;" id="bodyDiv">
<script type="text/javascript">
	var oDiv=document.getElementById("bodyDiv");
	var chartWidth=document.getElementById("chartWidth");
	var sHeight=document.documentElement.clientHeight-15;
	var sWidth=document.documentElement.clientWidth-10;
	oDiv.style.height=sHeight+"px";
	oDiv.style.width=sWidth+"px";
	chartWidth.value = sWidth-50;
</script>
	#if("$!hasCondition" == "true")<h1 class="f_b"><span style="background-color: #FFCC99;cursor: pointer;" onclick="relieveSearch();">解除搜索 显示全部数据</span><img src="/style/images/client/back.gif" width="25px" height="15xp;" onclick="relieveSearch();"> </h1> #end
	<!-- 查询条件开始 -->
	#set($step = "4")
	<div class="Wrap">
  		<!-- OnTop Start -->
  		<div class="OnTop">
      	<p>
        	<span>$!captionName</span>&nbsp;&nbsp;&nbsp;&nbsp;#if("$!isAround" == "true")环比：本期和上期的比较，比较本月和上月，本周和上周等;这里的月或周是环比单位#else#end
        </p>
        <div class="SanBu">
        	<ul class="SanBuU">
        	#if("$!isAround" == "true")
        	#set($step = "3")
	            <li class="SanBuLi">
	              <div class="SanBuLiLeft">
	                第1步：时 间 段
	              </div>
	              <div class="SanBuLiRight">
	                <input  name="startTime"  id="startTime" type="text" class="txtInput" onClick="WdatePicker({lang:'$globals.getLocale()'})"  value="$!startTime" /> 至 <input name="endTime"  id="endTime" type="text" class="txtInput"   onClick="WdatePicker({lang:'$globals.getLocale()'})"  value="$!endTime" />
	              </div>
	            </li>
	            <li class="SanBuLi">
	              <div class="SanBuLiLeft">
	                第2步：环比单位
	              </div>
	              <div class="SanBuLiRight">
	                	<select name="unit" id="unit" class="HuanBiXiaLa">
								<option value="quarter" #if("$!unit" == "quarter") selected="selected" #end>季度</option>
								<option value="month" #if("$!unit" == "month") selected="selected" #end>月份</option>
								<option value="week" #if("$!unit" == "week") selected="selected" #end>周</option>
								<option value="day" #if("$!unit" == "day") selected="selected" #end>天</option>
						</select>
						<span style="color:#FE6598;">【注：请不要选择大于设定时间段的单位】</span>
	              </div>
	            </li>
            #else
            	<li class="SanBuLi">
	              <div class="SanBuLiLeft">
	                第1步：当前日期
	              </div>
	              <div class="SanBuLiRight">
	              	<input name="searchTime"  id="searchTime" type="text" class="txtInput"   onClick="WdatePicker({lang:'$globals.getLocale()'})"  value="$!searchTime"/>
	              </div>
	            </li>
	            <li class="SanBuLi">
	              <div class="SanBuLiLeft">
	                第2步：历史阶段
	              </div>
	              <div class="SanBuLiRight">
	                	<select name="phase" id="phase" onchange="changePhase(this.value)" class="HuanBiXiaLa">
							<option value="year" #if("$!phase" == "year") selected="selected" #end)>本年与上年</option>
							<option value="quarter" #if("$!phase" == "quarter") selected="selected" #end>本季度与上季度</option>
							<option value="month" #if("$!phase" == "month") selected="selected" #end>本月与上月</option>
							<option value="week" #if("$!phase" == "week") selected="selected" #end>本周与上周</option>
						</select>
	              </div>
	            </li>
	            <li class="SanBuLi">
	              <div class="SanBuLiLeft">
	                第3步：同比单位
	              </div>
	              <div class="SanBuLiRight">
	                	<select name="unit" id="unit" class="HuanBiXiaLa">
							#if("$!phase" == "year")
								<option value="quarter" #if("$!unit" == "quarter") selected="selected" #end>季度</option>
								<option value="month" #if("$!unit" == "month") selected="selected" #end>月份</option>
							#elseif("$!phase" == "quarter")
								<option value="month" #if("$!unit" == "month") selected="selected" #end>月份</option>
								<option value="week" #if("$!unit" == "week") selected="selected" #end>周</option>
							#elseif("$!phase" == "month")
								<option value="week" #if("$!unit" == "week") selected="selected" #end>周</option>
								<option value="day" #if("$!unit" == "day") selected="selected" #end>天</option>
							#else
								<option value="day">天</option>
							#end
						</select>
	              </div>
	            </li>
            #end
            #set($rowSpan = "2")
			#if("$!secondFieldBean" != "")
				#set($rowSpan = "3")
			#end
            <li class="SanBuLi">
              <div class="SanBuLiLeft">
                第${step}步：扩展选择
              </div>
              <div class="SanBuLiRight" style="background:none;">
                <div class="SuoYouZhe" id="userGroupDiv">
                	<span class="SuoYouZheSpan">
                  	#if("$!tableName" == "CRMClientInfo") 所有者查询 #elseif("$!tableName" == "CRMSaleFollowUp") 跟单人查询 #elseif($!tableName.indexOf("CRMSaleContract") != -1) 签约人查询 #elseif("$!tableName" == "tblMaintainNote")  实施人查询 #elseif("$!tableName" == "CRMcomplaints") 投诉对象 #else 人员查询 #end :
                  </span>
                  <ul class="SuoYouZheU">
                  #foreach($userId in $userGroupIds.split(","))
                  	<li class="SuoYouZheLi" id="$userId">
                    	<a class="WordName" >$globals.getEmpNameById($userId)</a>
                      <a class="CloseImg" popSelId="$userId" popName="userGroup"></a>
                    </li>
                  #end
                    <li class="SuoYouZheLi" name="selectPop">
                    	<a class="SelectWord" id="userGroup" onclick="popSelect('userGroup',this)" href="#">选择</a>
                    </li>
                  </ul>
                </div>
                
                <div class="SuoYouZhe" id="deptGroupDiv" #if($rowSpan == 2)style="border:none;"  #end>
                	<span class="SuoYouZheSpan">
                  	部门查询:
                  </span>
                  <ul class="SuoYouZheU">
					#foreach($deptId in $deptGroupIds.split(","))
						#if("$!deptId" != "")
							<li class="SuoYouZheLi" id="$deptId"><a class="WordName">$!deptMap.get("$deptId")</a><a class="CloseImg" popSelId="$deptId" popName="deptGroup"></a></li>
						#end
					#end
                    <li class="SuoYouZheLi" name="selectPop">
                    	<a class="SelectWord" id="deptGroup"  onclick="popSelect('deptGroup',this)">选择</a>
                    </li>
                  </ul>
                </div>
                 #if("$!secondFieldBean" != "")
	                <div class="SuoYouZhe" style="border:none;">
	                	<span class="SuoYouZheSpan">
	                  	客户类型:
	                  </span>
	                  <ul class="SuoYouZheU" id="enumUl">
	                  #foreach($item in $globals.getEnumerationItems("$secondFieldBean.refEnumerationName"))
	                  #set($enums = ","+$!classes+",")	 
					  #set($enumval = ","+"$item.value"+",")
					  <li class="SuoYouZheLi"><input type="checkbox" class="WordCheckbox" name="$!{secondFieldBean.fieldName}_isAround" value="$item.value" #if($enums.indexOf($enumval) >-1) checked="checked" #end /><a class="WordName">$item.name</a></li>
	                  #end
	                  #set($enums = ","+$!classes+",")	 
	                  <li class="SuoYouZheLi"><input type="checkbox" class="WordCheckbox" name="$!{secondFieldBean.fieldName}_isAround" value="empty" #if($enums.indexOf(",empty,") >-1) checked="checked" #end /><a class="WordName">空</a></li>
	                  </ul>
	                </div>
                 #end
              </div>
            </li>
          </ul>
        </div>
        <div class="ChaKanTongJi">
        	<input type="button" class="ChaKanBtn" onclick="querySubmit()"/>
        </div>
      </div>
      <!-- OnTop End -->
	<div class="QieHuan">
        <!--查询结果-->
        <p class="ChaXunJieGuo">
        	#set($rsUnit = "月份")
	        	#if("$!unit" == "quarter")
	        	#set($rsUnit = "季度 ")
	        	#elseif("$!unit" == "week")
	        	#set($rsUnit = "周期 ") 
	        	#elseif("$!unit" == "day") 
	        	#set($rsUnit = "天数")
	        	#end
        	#if("$!isAround" == "true")
	        	<span>$!startTime</span>至<span>$!endTime</span> 环比-<span>$rsUnit</span>
        	#else
        		#set($rsPhase = "本年与上年")
        		#if("$!phase" == "quarter")
	        	#set($rsPhase = "本季度与上季度 ")
	        	#elseif("$!phase" == "month")
	        	#set($rsPhase = "本月与上月") 
	        	#elseif("$!phase" == "week") 
	        	#set($rsPhase = "本周与上周")
	        	#end
        		当前时间：<span>$!searchTime</span> 历史阶段:<span>$rsPhase</span> 同比单位:<span>$rsUnit</span>
        	#end
        </p>
      </div>
      <!-- QieHuan End -->
	 <!-- 查询条件结束 -->
	#if($!count == 0)
		<div style="margin: 100px 100px; 30px; 0;"><span>很抱歉，没有找到与您条件匹配的数据! </span></div>
	#else
		<!-- 同比数据显示开始 -->
		<div class="rt_table3">
		#if("$!msDatas"=="" && "$!isAround" !="true")
			<table  width="90%" cellspacing="0" cellpadding="0">
			 <thead>
				<tr>
					<td>&nbsp</td>
					#set($timeScopeLength = 1)
					#foreach($timeKey in $timeScope.split(","))
						#if("$!unit" == "month")
							<td>$timeKey月</td>
						#elseif("$!unit" == "quarter")
							<td>第$velocityCount季</td>
						#elseif("$!unit" == "week")
							<td>第$velocityCount周</td>
						#else
							<td>$timeKey</td>
						#end
						#set($timeScopeLength = $timeScopeLength + 1)
					#end
					<td>平均</td>
				</tr>
			</thead>
			<tbody>
				#set($timeScopeLength = $math.sub($timeScopeLength,1))
				<tr>
					#set($prevPhase = "上年")
					#set($nowPhase = "本年")
					#if("$!phase" == "quarter")
						#set($prevPhase = "上季度")
						#set($nowPhase = "本季度")
					#elseif("$!phase" == "month")
						#if("$!phaseMonth" == "")
							#set($prevPhase = "上月")
							#set($nowPhase = "本月")
						#else
							#set($prevPhase = $math.sub($!phaseMonth,1)+"月份")
							#set($nowPhase = "$!phaseMonth"+"月份")
						#end
					#elseif("$!phase" == "week")
						#set($prevPhase = "上周")
						#set($nowPhase = "本周")
					#end
					<td bgcolor="#CBD9FC">$prevPhase</td>
					#set($prevCount = 0)
					#foreach($timeKey in $timeScope.split(","))
						#if(("$!phase" == "quarter" && "$!unit"=="month") || ("$!phase" == "week" && "$!unit"=="day"))
							#set($timeKey = $prevScope.get("$velocityCount"))
						#end
						<td>
						#if("$!prevInfoMap.get($timeKey)" == "") 
							0 
						#else 
							#set($prevCount = $math.add($prevCount,$prevInfoMap.get("$timeKey"))) 
							$!globals.dealDoubleDigits($prevInfoMap.get("$timeKey"),$!sumFieldName)
						#end
						</td>
					#end
					<td>$!globals.dealDoubleDigits("$math.div($prevCount,$timeScopeLength)","avg")</td>
				</tr>
				<tr>
					<td bgcolor="#CBD9FC">$nowPhase</td>
					#set($nowCount = 0)
					#foreach($timeKey in $timeScope.split(","))
						<td>
						#if("$!nowInfoMap.get($timeKey)" == "") 
							0 
						#else 
							#set($nowCount = $math.add($nowCount,$nowInfoMap.get("$timeKey")))  
							$!globals.dealDoubleDigits($nowInfoMap.get("$timeKey"),$!sumFieldName)
						#end
						</td>
					#end
					<td>$!globals.dealDoubleDigits("$math.div($nowCount,$timeScopeLength)","avg")</td>
				</tr>
				<tr>
					<td bgcolor="#CBD9FC">变化</td>
					#set($count = 0)
					#foreach($timeKey in $timeScope.split(","))
						#set($chPrevTimeKey = $timeKey)
						#if(("$!phase" == "quarter" && "$!unit"=="month") || ("$!phase" == "week" && "$!unit"=="day"))
							#set($chPrevTimeKey = $prevScope.get("$velocityCount"))
						#end
						#set($change =$globals.getChangeRatio($!prevInfoMap.get($chPrevTimeKey),$!nowInfoMap.get($timeKey)))
						<td>
							<span class="#if($globals.isExist($change,"-")) changeDown #else changeUp #end" >
								$change
							</span>
						</td>
					#end
						<td>
						#set($totalChange = $globals.getChangeRatio("$prevCount","$nowCount"))
						#if("$totalChange" == "-")
							$totalChange					
						#else
							<span class="#if($globals.isExist($totalChange,"-")) changeDown #else changeUp #end">$totalChange</span>
						#end
						</td>
				</tr>
				</tbody>
			</table>
		<!-- 同比数据显示结束 -->
		#else
		<!-- 环比数据显示开始 -->
			#if("$!mulFieldsMap" == "")
				<!-- 环比单条件数据显示开始 -->
				<table width="900px" cellspacing="0" cellpadding="0">
				<thead>
					<tr>
						<td>&nbsp;</td>
						<td>#if("$!sumFieldName" == "") 数量 #else 金额 #end</td>
						<td>环比</td>
						<td>比平均</td>
					</tr>
				</thead>
				<tbody>
					#set($total = 0)
					#foreach($time in $timeScope.split(","))
					#if($velocityCount != 1)
						#set($prevIndex = $globals.getSplitIndex("$timeScope",",",$math.sub($velocityCount,2)))
					#end
					<tr>
						#set($total = $math.add($total,$!aroundMap.get($time)))
						#set($avgVal = $globals.getChangeRatio("$avg",$!aroundMap.get($time)))
						#if($velocityCount != 1)
							#set($change = $globals.getChangeRatio($!aroundMap.get($prevIndex),$!aroundMap.get($time)))
						#end
						<td bgcolor="#CBD9FC">
						#if("$!unit" == "week")
							第$velocityCount周
						#else
							$time
						#end
						</td>
						<td>$!globals.dealDoubleDigits($!aroundMap.get($time),$!sumFieldName)</td>
						#if($velocityCount == 1)
							<td class="changeDown">-</td>
						#else
							<td class="#if($globals.isExist($change,"-")) changeDown #else changeUp #end">$change</td>
						#end						
						<td class="#if($globals.isExist($avgVal,"-")) changeDown #else changeUp #end"> $avgVal </td>
					</tr>	
					#end
				
					<tr bgcolor="#f7f7f7">
						<td>&nbsp;</td>
						<td>和:$!globals.dealDoubleDigits("$total",$!sumFieldName)</td>
						<td>&nbsp;</td>
						<td>均:$!globals.dealDoubleDigits("$!avg","avg")</td>
					</tr>
				</tbody>
				</table>
				<!-- 环比单条件数据显示结束 -->
			#else
				<!-- 环比类型条件数据显示开始 -->
				<div style="overflow-x: auto;">
				<table width="900px" cellspacing="0" cellpadding="0">
					<thead>
						<tr>
							<td></td>
							#foreach($enumInfo in $enumList)
								<td>#if("$!globals.get($enumInfo,1)"=="") 空 #else $!globals.get($enumInfo,1) #end</td>
								<td>环比</td>
								<td>比平均</td>
							#end
							<td>合计</td>
							<td>环比</td>
						</tr>
						</thead>
						
						<tbody>
						#set($index = 0)
						#foreach($time in $timeScope.split(","))
							#if($index != 0)
								#set($prevIndex = $globals.getSplitIndex("$timeScope",",",$math.sub($velocityCount,2)))
							#end
							<tr>
								<td bgcolor="#CBD9FC">
									#if("$!unit" == "week")
										第$velocityCount周
									#else
										$time
									#end
								</td>
								#foreach($enumInfo in $enumList)
									#set($enumValue = "")
									#set($enumPrevValue = "")
									#set($enumPrevValue = $!mulFieldsMap.get($prevIndex).get($!globals.get($enumInfo,0)))
									#if("$!enumPrevValue" == "")
										#set($enumPrevValue = "0")
									#end
									#set($enumValue = $!mulFieldsMap.get($time).get($!globals.get($enumInfo,0)))
									#if("$!enumValue" == "")
										#set($enumValue = "0")
									#end
									<td>$!globals.dealDoubleDigits("$enumValue",$!sumFieldName)</td>
									#set($change=$globals.getChangeRatio($enumPrevValue,$enumValue))
									#if($index==0)
										<td class="changeDown">-</td>
									#else
										<td class="#if($globals.isExist($change,"-")) changeDown #else changeUp #end">$change</td>
									#end
									#set($change=$globals.getChangeRatio("$math.div($totalMap.get($!globals.get($enumInfo,0)),$timeScopeSize)",$enumValue))
									<td class="#if($globals.isExist($change,"-")) changeDown #else changeUp #end">$change</td>
								#end
								
								#set($enumPrevValue = $!mulFieldsMap.get($prevIndex).get("rowCount"))
								#if("$!enumPrevValue" == "")
									#set($enumPrevValue = "0")
								#end
								#set($enumValue = $!mulFieldsMap.get($time).get("rowCount"))
								#if("$!enumValue" == "")
									#set($enumValue = "0")
								#end
								#set($change=$globals.getChangeRatio($enumPrevValue,$enumValue))
								<td>$!globals.dealDoubleDigits("$enumValue",$!sumFieldName)</td>
								<td class="#if($globals.isExist($change,"-")) changeDown #else changeUp #end">#if($index==0) - #else $change    #end</td>
							</tr>
							#set($index = $index +1)
						#end
						<tr bgcolor="#f7f7f7">
							<td></td>
							#foreach($enumInfo in $enumList)
								<td>和:$!globals.dealDoubleDigits($totalMap.get($!globals.get($enumInfo,0)),$!sumFieldName) </td>
								<td></td>
								<td>均:$!globals.dealDoubleDigits("$math.div($totalMap.get($!globals.get($enumInfo,0)),$timeScopeSize)","avg")</td>
							#end
							<td colspan="2"></td>
						</tr>
						</tbody>
				</table>
				</div>
				<!-- 环比类型条件数据显示结束 -->
			#end
		#end
		</div>
		<!-- 环比数据显示结束 -->	
		<br>
		<!-- <div id="canvasDiv"></div> -->
		<!-- 统计图显示开始 	-->
		<div>
			<span style="color: orange;">显示类型: </span>
			#if("$!secondFieldName" == "" && "$!isAround" == "true")
			<a href="#" onclick="choiceChart('Column3D')">3D柱图</a>&nbsp;&nbsp; 
			<a href="#" onclick="choiceChart('Column2D')">2D柱图</a>&nbsp;&nbsp; 
			<a href="#" onclick="choiceChart('Line')">折线图</a>&nbsp;&nbsp;
			<a href="#" onclick="choiceChart('Bar2D')">2D条形图</a>&nbsp;&nbsp;
			<a href="#" onclick="choiceChart('Pie3D')">3D饼图</a>&nbsp;&nbsp;
			#else
			<a href="#" onclick="choiceChart('MSColumn3D')">3D柱图</a>&nbsp;&nbsp;
			<a href="#" onclick="choiceChart('MSColumn2D')">2D柱图</a>&nbsp;&nbsp;
			<a href="#" onclick="choiceChart('MSBar2D')">2D条形图</a>&nbsp;&nbsp;
			#end
		</div>
		<div id="chartContainer"></div>
		<script type="text/javascript">  
			var chartWidth = $("#chartWidth").val();
			var chartSwfName = $("#chartSwfName").val();
			var height = 400;
			if(chartSwfName == "Bar2D"){
				height = 550;
			}
			#if("$!searchSwfName"=="")
				#set($swfName=$chartSwfName)
			#else
				#set($swfName=$searchSwfName)
			#end
			var chart = new FusionCharts("flash/FCF_${swfName}.swf", "chartId", chartWidth, height);     
			chart.setDataXML("$chart");
			chart.render("chartContainer");
		</script>
		  <!-- 统计图显示结束 -->	
		<iframe id="detailFrame" name="detailFrame" width="100%"  height="270px" frameborder=no scrolling="no" style="display: none;"></iframe>
		
	#end
	
</form>
</body>
</html>
