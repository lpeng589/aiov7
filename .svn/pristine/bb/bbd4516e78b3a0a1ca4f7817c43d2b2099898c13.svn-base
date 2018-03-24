#if("$!chartType"=="chart")
<div id="myGoalId" style="float: left; width:auto;">
							 
</div>
<script>
jQuery.get("/UtilServlet?operation=displayGoal&goalClass=myGoal&time="+(new Date()).getTime(),function(response){
	if(response=="no"){
 		var goalId = document.getElementById("myGoalId") ;
 		goalId.style.height = "25px" ;
 		goalId.innerHTML = "<a href=\"javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblEmployeeGoal&src=menu','职员目标管理')\">$text.get("crm.set.month.plan")</a>" ;
 	}else{
 		var fwidth = 400;
		var fheight = 210;
 		var chart = new FusionCharts("flash/FCF_MSColumn2D.swf", "ChartId", ""+fwidth, ""+fheight);	 
 		if(navigator.userAgent.toLowerCase().indexOf("msie 8.0")==-1){
 			chart.addParam("wmode","transparent");
 		}
 		chart.setDataXML(response);
		chart.render("myGoalId");
	}
}) ;
</script>
#elseif("$!chartType"=="yibiaopan")
<div style="width:100%;float: left;text-align:center;">
<div id="myCompleteId" style="margin:0px auto;">

</div>
<div id="noComplete" style="height:25px;display:none;">
<a href="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblEmployeeScore&operation=4','$text.get("crm.deskTop.myAchievement")')">点击这里录入$text.get("crm.deskTop.myAchievement")</a>
</div>							
</div>
<script>


jQuery.get("/UtilServlet?operation=displayYibiaopan&goalClass=tblEmployeeGoal&displayType=&time="+(new Date()).getTime(),function(response){
	if("noExist"!=response &&"stop"!=response){
		var dataArray = response.split(";") ;
		
		var isIE  = (navigator.appVersion.indexOf("MSIE") != -1) ? true : false;
		var isWin = (navigator.appVersion.toLowerCase().indexOf("win") != -1) ? true : false;
		var isOpera = (navigator.userAgent.indexOf("Opera") != -1) ? true : false;
		var str = '';
	    if (isIE && isWin && !isOpera)
	    {
	  		str += '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" '+
	  			'id="myComplete" width="210" height="200" codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">'+
	  			'	<param name="movie" value="flash/clock.swf" />'+
	  			' <param name="flashvars" value="data='+dataArray[0]+'&color='+dataArray[1]+'&curData='+dataArray[2]+'" /> ' +
	  			'<param name="quality" value="high" />'+
	  			'	<param name="bgcolor" value="#869ca7" /><param name="flashvars" value="" /><param name="allowScriptAccess" value="sameDomain" />'+
	  			'</object>';
		
	
	    } else {
	  		str += '<embed src="flash/clock.swf" id="myComplete" quality="high" bgcolor="#869ca7" width="210" height="200" align="middle" play="true" loop="false" wmode="transparent" '+
	  		' flashvars = "data='+dataArray[0]+'&color='+dataArray[1]+'&curData='+dataArray[2]+ '"'+
	  		' quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash"	pluginspage="http://www.adobe.com/go/getflashplayer"></embed>';
	    }
	    $("#myCompleteId").html(str) ;
	
		
		//var varFlash = document.getElementById("myComplete") ;		
		//varFlash.flashvars = "data="+dataArray[0]+"&color="+dataArray[1]+"&curData="+dataArray[2] ;
		//if(typeof(varFlash.setData)!="undefined"){
		//	varFlash.setData(dataArray[0],dataArray[1],dataArray[2]) ;
		//}
		
		
	}else{
		$("#noComplete").show() ;
		$("#myCompleteId").hide() ;
	}
}) ;
</script>
#else
<div id="goalMsLineId" style="float: left; width:auto;">
									
</div>
<script>
jQuery.get("/UtilServlet?operation=displayMS2line&goalClass=myGoal&time="+(new Date()).getTime(),function(response){
	if(response=="no"){
 		var goalId = document.getElementById("goalMsLineId") ;
 		goalId.style.height = "25px" ;
 		goalId.innerHTML = "<a href=\"javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblEmployeeGoal&operation=4','$text.get("crm.deskTop.empTargetManager")')\">$text.get("crm.set.month.plan")</a>" ;
 	}else{
 		var fwidth = 390;
		var fheight = 210;
		vals = response.split("::::");
 		var chart = new FusionCharts("flash/FCF_MSLine.swf", "ChartId", ""+fwidth, ""+fheight);
 		if(navigator.userAgent.toLowerCase().indexOf("msie 8.0")==-1){
 			chart.addParam("wmode","transparent");
 		}
 		chart.setDataXML(vals[0]);
		chart.render("goalMsLineId");
	}
}); 
</script>
#end
