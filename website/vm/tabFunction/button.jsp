<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="/js/jquery.js"></script>
<title>button</title>
<script>
	#set($count=1)
	#foreach($row in $childTabList)
		#set($count=$count+1)
	#end
	var count = $count;	

	function brotherUrl(tableName,index){ 
		var keyId = $("#mainKeyId").val();
		
		var url = "/UserFunctionQueryAction.do?tableName="+tableName+"&src=menu&operation=4"+"&f_brother="+keyId+"&isTab=yes&winCurIndex=";
		if(tableName!='$!tableName'){
			//先检查对应记录的行数，如果没有数据，则显示添加页，一行数据显示详情，多行显示列表
			var ret = jQuery.ajax({url: "/UtilServlet?operation=getBrotherRow&tableName="+tableName+"&brotherId="+keyId,
 						async: false   
						}).responseText; 
						
			if(ret == "add"){
				url="/UserFunctionAction.do?tableName="+tableName+"&parentCode=&operation=6&f_brother="+keyId+"&parentTableName=$!tableName&winCurIndex=";
			}else if(ret == "list"){		
				url="/UserFunctionQueryAction.do?tableName="+tableName+"&src=menu&operation=4"+"&f_brother="+keyId+"&isTab=yes&parentTableName=$!tableName&winCurIndex=&brotherEnter=true";
			}else{
				url="/UserFunctionAction.do?tableName="+tableName+"&keyId="+ret+"&moduleType=&f_brother="+keyId+"&operation=5&winCurIndex=&pageNo=&parentCode=&parentTableName=$!tableName&saveDraft=";
			}
		}
		url +="&popWinName=$!popWinName";
		var displayType = $("#displayType",window.parent).val() ;
		if(displayType=="detail"){
			url += "&toDetail=go" ;
		}
		window.parent.document.getElementById("bottomFrame").src=url;
		document.getElementById("checkItem").value=tableName ;
		for(var i=0;i<count;i++){
			if(i == index){
				$("#divShow_"+i).css("background-color","#6B928E");
			}else{
				$("#divShow_"+i).css("background-color","");
			}
		}
		indexs=index;
	}
	var indexs=0;
	function bludUlr(tableName,index){ 
		var keyId = document.getElementById("mainKeyId").value;
		var url = "/UserFunctionAction.do?tableName="+tableName+"&keyId="+keyId+"&operation=5&checkTab=Y&popWinName=$!popWinName";
		window.parent.document.getElementById("bottomFrame").src=url;
		document.getElementById("checkItem").value=tableName ;
		for(var i=0;i<=count;i++){
			if(i == index){
				$("#divShow_"+i).css("background-color","#6B928E");
			}else{
				$("#divShow_"+i).css("background-color","");
			}
		}
		indexs=index;
		
	}
	
	function showNeighbor(){
		var varHeight = 310 ;
		var strUrl = "/crmUserFunctionAction.do?tableName=$tableName&type=neighbor";
		window.parent.asyncbox.open({id : 'NeighborPopdiv',title:'邻居表设置',url:strUrl,width:380,height:varHeight,btnsbar:window.parent.jQuery.btn.OKCANCEL,
		callback : function(action,opener){
　　　　　	if(action == 'ok'){
				opener.submitBefore();
				window.location.reload()
				return false;
　　　　　	}
　　　	}
　		});
	}
	
</script>
<style type="text/css">
<!--
body {
	margin: 0px;
padding: 0px;
font-size: 12px;
background: #efefef;
}
ul, li, ol {
list-style: none;
margin: 0px;
padding: 0px;
}

.LeftMenu_list,.LeftMenu_list li,.LeftMenu_list li ul,.LeftMenu_list li ul li{ width:100%; float:left;}
.LeftMenu_list li{ background:url(/style1/images/workflow/leftMenu_bg.gif) repeat-x; line-height: 27px;padding-left: 10px; border-bottom:1px solid #b4b4b4;}
.LeftMenu_list li ul{ border-top:2px solid #e9e9e9; float:left; width:100%; margin-bottom:30px; }
.LeftMenu_list li ul li{ background:#fff; border:0px; margin-left:20px; width:auto; float:none; height:24px; line-height:24px;}

-->
</style>
</head>
#set ($tIndex=1)
#set ($strIndex=1) 
#foreach($row in $childTabList)
	#if("$!tabIndex"=="$!row.tableName")
		#set($strIndex=$tIndex)
	#end
	#set($tIndex=$tIndex+1)
#end
<body onload=' ' >

<ul class="LeftMenu_list">	
#if($tableName != "")
	<li id="divShow_0" onclick="bludUlr('$!tableName',0);" style="cursor: pointer;"><b>$text.get("crm.tab.detail.info")</b></li>
#end
#set($index=1)
#foreach($row in $childTabList)
	<li id="divShow_$index" onclick="brotherUrl('$row.tableName',$index);" style="cursor: pointer;"><b>$globals.getTableDisplayName($row.tableName)</b></li>
#set($index=$index+1)
#end	

<!-- 
	<li id="rw" onclick="showNeighbor()" style="cursor: pointer;"><b>$text.get("tblNeighbourMain.setting")</b></li>  -->
</ul>

<input type="hidden" id="mainKeyId" value="$!firstId"/>
#if("$!tabIndex"!="")
<input type="hidden" id="checkItem" value="$!tabIndex"/>
#else
<input type="hidden" id="checkItem" value="$!tableName"/>
#end
</body>

</html>