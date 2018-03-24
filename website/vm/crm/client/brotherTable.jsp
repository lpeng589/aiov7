<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link rel="stylesheet" type="text/css" href="/style/css/client.css"  />
<link rel="stylesheet" type="text/css" href="/style/css/client_sub.css"  />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}});
$(document).ready(function(){
	
	$('.brotherFrame').append('<iframe id="CRMClientInfoLog" name="CRMClientInfoLog" src="/CRMClientAction.do?operation=22&type=brotherIframe&parentName=CRMClientInfo&tableName=CRMClientInfoLog&f_brother=$!keyId"' 
			+ 'allowTransparency="true" frameborder="no" style="width:536px;height:352px;" scrolling="no"></iframe>');
	
	$('.big_dotright').click(function(){
		window.parent.$(".brother_table").hide("slow");
	}) ;
	
	/*兄弟切换*/
	$('.cont_r li').click(function(){
		var returnMsg = "success";//记录返回值,
		var tableName = $(this).attr("tableName");
		var newBroModuleAddr = $(this).attr("newBroModuleAddr");//true表示使用了邻居表新路径,需要先判断是否配置了字段设置
		if(newBroModuleAddr == "true"){
			jQuery.ajax({
			    type: "POST",
			    url: "/CRMClientAction.do",
			    data: "operation=4&type=broFieldDisPlay&tableName="+$(this).attr("tableName"),
			    async: false,
			    success: function(msg){
			    	returnMsg = msg;
			    }
			});
		}
		
		if(returnMsg=="success"){
    		$('.cont_r li').each(function(){
				$(this).removeClass("sel") ;
			}) ;
			$(this).addClass("sel") ;
			var keyId = parent.$("input[name='keyId']:checked").val() ;
			$('iframe').each(function(){
				$(this).css("display","none");
			});
			$("#"+tableName).css("display","block");
			var strUrl = '/CRMClientAction.do?operation=22&type=brotherIframe&parentName=CRMClientInfo&brotherEnter=true&tableName='+tableName+'&f_brother='+keyId ;
			var brother = $("#"+tableName) ;
			if(typeof(brother.attr("src"))=="undefined"){
				$(".brotherFrame").append('<iframe id="'+tableName+'" name="'+tableName+'" src="'+strUrl+'" allowTransparency="true" frameborder="no" style="width:536px;height:352px;" ' 
		    		+ 'scrolling="no" ></iframe>') ;
		    	jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}});
			}else{
				$("#"+tableName).attr("src",strUrl) ;
			}
    	}else{
    		alert("列表没有设置显示字段,请先到'"+jQuery.trim($(this).text())+"'模块下进行字段设置");
    	}
	});
});

/*单击客户 查询兄弟数据*/
function changeIframe(keyId){
	var strUrl = $("iframe:visible").attr("src") ;
	strUrl = strUrl.substring(0,strUrl.indexOf("f_brother")+10) ;
	$("iframe:visible").attr("src",strUrl+keyId);
	jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{background:'transparent'}});
}


//邻居表排序设置


function childrenSort(){
	window.parent.childrenSort();
}

/**
*列表页面兄弟表销售跟单添加保存后刷新方法
*/
function reloadBrother(tableName){
	//在刷新iframe[name=tableName]的reloadDetail方法
	window.frames[tableName].reloadDetail();
}

</script>
</head>

<body class="body_f4">
<div class="boxbg3 subbox_w620" >
  <div class="subbox3_bbg subbox3_b_t"><div class="subbox3_bbg subbox3_b_t_l"></div></div>
    <div class="subbox3_cont cf">
    <div class=" all_icon big_dotright"></div>
    <div class="cont_l f_l brotherFrame">
	    
    </div>
     <img style="margin-left:40px;cursor: pointer;" src="/style1/images/workflow/set.png" onclick="childrenSort();"  title="设置"/>
     <div class="cont_r f_r">
     <ul>
     <li tableName="CRMClientInfoLog" class="sel">最新动态</li>
     #foreach($tableName in $userBrotherInfo.split(","))
     	 #set($newBroModuleAddr = "false")
     	 #if($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$tableName").query())
     	 	#set($newBroModuleAddr = "true")
     	 #end
	     #set($tableBean = $moduleMap.get("$tableName"))
	     #if("$!tableBean" !="")
		     #if($velocityCount<12 && "$tableBean.isUsed" == "1")
		     <li tableName="$!tableBean.tableName" name="$!tableBean.tableName" newBroModuleAddr = "$newBroModuleAddr">$tableBean.display.get("$globals.getLocale()") </li>
		     #end
	     #end
     #end
     </ul>
     </div>
    </div>
     <div class="subbox3_bbg subbox3_b_f"><div class="subbox3_bbg subbox3_b_f_l f_l"></div>
     <div class="subbox3_bbg subbox3_b_f_r f_r"></div></div>
</div>
</body>
</html>