<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/crmReportNew.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/crm_report.js"></script>
<style type="text/css">
.WordName{color: black;}
.SanBuU{border-bottom: 0;border-left: 0;border-right: 1px solid #a1a1a1;}
.SanBuU {border: 0px;}
.SanBuU .SanBuLi{background: #FFFFFF;border-bottom: 0px;}
.SanBuU .SanBuLi .SanBuLiRight{border-left: 0px;}
</style>
<script type="text/javascript">
$(document).ready(function(){
	//让table头部的宽度与内容一样宽。即使有滚动条也能对其。
	$("#TongJiView table").css("width",$("#tableContent table").css("width"))
	
	#if("$!sortName" != "")
		$("#"+$("#sortName").val()).next().show();
		$("#"+$("#sortName").val()).attr("class","SelectLine");
	#end
	
	$(".ViewTable tbody tr").hover(
		function () {
			$(this).addClass("mulTr");
		},
		function () {
			$(this).removeClass("mulTr");
		}
	);
	
	//排序
	$(".TongJiView table thead tr td a").click(function(){
		$("#sortName").val($(this).attr("id"));
		querySubmit();
	})
	
	
});

/*个人与部门弹出框*/
function popSelect(popName){
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
	    		$("#"+popName+"Div ul li").remove();
	    		$("#"+popName+"Div ul ").append(showUsers);
	    		if(showUsers !=""){
		    		if(popName == "userGroup"){
						$("#userDiv").show();
					}else{
						$("#deptDiv").show();
					}
	    		}
	    		querySubmit();
	    	} 
			if(action == 'selectAll'){
				$("#"+popName+"Div ul li").remove();
				$("#"+popName+"Ids").val("");//把隐藏域ID清空
				if(popName == "userGroup"){
					$("#userDiv").hide();
				}else{
					$("#deptDiv").hide();
				}
				querySubmit();
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
		$("#"+popName+"Div ul").append(str);
		var popVal = $("#"+popName+"Ids").val() +id +",";
		$("#"+popName+"Ids").val(popVal);
		if(popName == "userGroup"){
			$("#userDiv").show();
		}else{
			$("#deptDiv").show();
		}
	}else{
		delPopId($("#"+popName+"Div").find("li[id='"+id+"']").find(".CloseImg"),id,popName);//删除
		if(popName == "userGroup"){
			$("#userDiv").hide();
		}else{
			$("#deptDiv").hide();
		}
	}
	jQuery.close('Popdiv');
	querySubmit();
}

</script>
</head>

<body>
<form action="CRMReportAction.do" method="post" name="form"><div align="left"> 
<input type="hidden" id="type" name="type" value="weekMonthDetail"> 
<input type="hidden" id="isWeekQuery" name="isWeekQuery" value="$!isWeekQuery"> 
<input type="hidden" id="operation" name="operation" value="4"> 
<input type="hidden" id="popName" name="popName" value=""> 
<input type="hidden" id="userGroupIds" name="userGroupIds" value="$!userGroupIds"> 
<input type="hidden" id="deptGroupIds" name="deptGroupIds" value="$!deptGroupIds"> 
<input type="hidden" id="year" name="year" value="$!year"> 
<input type="hidden" id="month" name="month" value="$!month"> 
<input type="hidden" id="weekStartTime" name="weekStartTime" value="$!weekStartTime"> 
<input type="hidden" id="weekEndTime" name="weekEndTime" value="$!weekEndTime"> 
<input type="hidden" id="sortName" name="sortName" value="$!sortName"> 
<input type="hidden" id="isDetail" name="isDetail" value=""> 
<input type="hidden" id="weekName" name="weekName" value="$!weekName"> 
<input type="hidden" id="isSearch" name="isSearch" value="true"> 
 
	</div><div id="bodyDiv" style="overflow-x:hidden; overflow-y: auto;">
	<div class="Wrap" >
  		<!-- OnTop Start -->
  		<div class="OnTop" style="padding-bottom: 10px;padding-top: 0;border-bottom: 0;">
		<p>
			#if($!isWeekQuery =="true")
	        	<!-- <span>周统计时间段</span> <span class="timeScope">$!weekStartTime</span> <span>到</span> <span class="timeScope">$!weekEndTime</span> -->
	        	<span>$!month月份$!weekName(<span class="timeScope">$!weekStartTime 到 $!weekEndTime</span>)统计</span>
			#else
				<!--<span>$!month月份统计时间段</span> <span class="timeScope">$!startTime</span> <span>到</span> <span class="timeScope">$!endTime</span>  -->
				<span>$!month月份统计</span>
			#end
			&nbsp;&nbsp;&nbsp;<a class="BtnA" onclick="popSelect('userGroup')" >人员查询 ▼</a> <a class="BtnA" onclick="popSelect('deptGroup')" >部门查询 ▼</a> <a class="BtnA" onclick="querySubmit();" >查  询</a>  
        </p>
        <div class="SanBu" id="userDiv" #if("$!userGroupIds" =="") style="display: none;" #end>
        	<ul class="SanBuU">
            <li class="SanBuLi">
              <div class="SanBuLiRight" style="background:none;width: 100%;">
                <div class="SuoYouZhe" id="userGroupDiv"  style="border:none;">
                	<span class="SuoYouZheSpan" style="color: black;">人员查询:</span>
                  <ul class="SuoYouZheU" style="width: 91%;">
                  #foreach($userId in $userGroupIds.split(","))
                  	<li class="SuoYouZheLi" id="$userId">
                    	<a class="WordName" >$globals.getEmpNameById($userId)</a>
                      <a class="CloseImg" popSelId="$userId" popName="userGroup"></a>
                    </li>
                  #end
                  </ul>
                </div>
                
              </div>
              
            </li>
          </ul>
        </div>
        
        <div class="SanBu" id="deptDiv" #if("$!deptGroupIds" =="") style="display: none;" #end>
        	<ul class="SanBuU">
            <li class="SanBuLi">
              <div class="SanBuLiRight" style="background:none;width: 100%;">
                <div class="SuoYouZhe" id="deptGroupDiv" style="border:none;">
                	<span class="SuoYouZheSpan" style="color: black;">
                  	部门查询:
                  </span>
                  <ul class="SuoYouZheU" style="width: 91%;">
					#foreach($deptId in $deptGroupIds.split(","))
						#if("$!deptId" != "")
							<li class="SuoYouZheLi" id="$deptId"><a class="WordName">$!deptMap.get("$deptId")</a><a class="CloseImg" popSelId="$deptId" popName="deptGroup"></a></li>
						#end
					#end
                  </ul>
                </div>
              </div>
              
            </li>
          </ul>
        </div>
      </div>
      <!-- OnTop End -->
      </div>
	
	#if($weekMonthList.size() == 0)
		很抱歉，没有找到与您条件匹配的数据!
	#else
		<div class="TongJiView" id="TongJiView" style="margin-left: 10px;">
		    <table class="ViewTable" border="0" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD" style="width: 100%;">
		    	<thead>
		      	<tr>
		          <td width="16%">姓名</td>
		          <td width="12%"><a class="Line" id="clientCount">新建客户数</a><span class="sort" title="点击排序"/> </td>
		          <td width="12%"><a class="Line" id="followUpCount">跟单次数</a><span class="sort" title="点击排序"/></td>
		          <td width="12%"><a class="Line" id="contractCount">合同数量</a><span class="sort" title="点击排序"/></td>
		          <td width="12%"><a class="Line" id="contractSum">合同金额</a><span class="sort" title="点击排序"/></td>
		          <td width="12%"><a class="Line" id="receiveCount">回款数量</a><span class="sort" title="点击排序"/></td>
		          <td width="12%"><a class="Line" id="receiveSum">回款金额</a><span class="sort" title="点击排序"/></td>
		          <td width="12%"><a class="Line" id="feeSum">费用</a><span class="sort" title="点击排序"/></td>
		        </tr>
		      </thead>
		    </table>
		    <div style="overflow: auto;width: 100%;max-height: 200px;" id="tableContent">
			    <table class="ViewTable" border="0" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD" >
			      <tbody>
			      #foreach($row in $weekMonthList)
			      	<tr>
			      	    <td width="16%">#if("$!globals.getEmpFullNameByUserId($!globals.get($row,0))" == "") 空 #else $!globals.getEmpFullNameByUserId($!globals.get($row,0)) #end</td>
						<td width="12%">$globals.dealDoubleDigits("$!globals.get($row,1)","")</td>
						<td width="12%">$globals.dealDoubleDigits("$!globals.get($row,2)","")</td>
						<td width="12%">$globals.dealDoubleDigits("$!globals.get($row,3)","")</td>
						<td width="12%">$globals.dealDoubleDigits("$!globals.get($row,4)","sum")</td> 				 				
						<td width="12%">$globals.dealDoubleDigits("$!globals.get($row,5)","")</td>
						<td width="12%">$globals.dealDoubleDigits("$!globals.get($row,6)","sum")</td> 		
						<td width="12%">$globals.dealDoubleDigits("$!globals.get($row,7)","sum")</td> 		
			      	</tr>
			      #end
			      </tbody>
			      <tfoot>
			      	<td >合计</td>
			      	#foreach($count in $countList)
			       		<td>$globals.dealDoubleDigits("$!globals.get($count,0)","")</td>
						<td>$globals.dealDoubleDigits("$!globals.get($count,1)","")</td>
						<td>$globals.dealDoubleDigits("$!globals.get($count,2)","")</td>
						<td>$globals.dealDoubleDigits("$!globals.get($count,3)","sum")</td> 				 				
						<td>$globals.dealDoubleDigits("$!globals.get($count,4)","")</td>
						<td>$globals.dealDoubleDigits("$!globals.get($count,5)","sum")</td> 		
						<td>$globals.dealDoubleDigits("$!globals.get($count,6)","sum")</td> 	
			        #end
			      </tfoot>
			     </table>
		    </div>
		 </div>
	#end
  </div>
  <script type="text/javascript">
  	var tableContent=document.getElementById("tableContent");
  	var TongJiView=document.getElementById("TongJiView");
  	var bodyDiv=document.getElementById("bodyDiv");
	bodyDiv.style.height=document.documentElement.clientHeight-2+"px";
	if(tableContent!=null){
		var tableWidth =  document.documentElement.clientHeight-100 +"px"
		jQuery("#tableContent").css("maxHeight",document.documentElement.clientHeight-100 +"px");
	}
	
	</script>
</form>
</body>
</html>
