<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>高级功能</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
function NewMenuAction(){
	var tdLen = $(".img_td .img_p").length;
	var tdNum = 1;
	for(var t=0;t<tdLen;t++){
		$(".img_td .img_p:eq("+t+") img").addClass("mm_"+tdNum);
		if(tdNum == "4"){
			tdNum = 1;
		}
		tdNum++;
	}
}
function frmem(){
	top.jblockUI({ message: ' <img src="/js/loading.gif"/>',css:{ background: 'transparent'}}); 
	window.location.href='/AdvanceAction.do?optype=refresh';
}
function restart(){
	if(confirm("重启将会中断服务2-10分钟,确定重启吗？")){
			var timestamp = Date.parse(new Date()); 
			jQuery.get("/CommonServlet?operation=setRestart&t="+timestamp,function(data){
				alert(data);
			});
	}
}
$(function(){
	$("#scroll-wrap").height(document.body.clientHeight-18);
});
</script>
<style type="text/css">
/*************全局公用Base样式*************/
body,div,form,img,ul,ol,li,dl,dt,dd,a,label,em,p,h1,h2,h3,h4,h5,h6,input,select,button,textarea{margin:0px; padding:0px;}
body{font:12px/1.5 Microsoft Yahei,Arial;color:#000;}
li{list-style: none;}
em,i{font-style:normal;}
button,input,select,textarea{outline:0;font:12px/1.5 Microsoft Yahei,Arial;box-sizing:border-box;}

#pageTitle{height: 40px;}
#pageTitle i{font-size:16px;}
#bodyUl{color:red;font-size: 14px;}
#contentmain p{padding:0;text-align:center;font-size:12px;}
.tbl_wp{width:100%;}
.tbl_wp td{padding:5px 25px;}
.tbl_wp td p.img_p{cursor:pointer;text-align:center;position:relative;background:url(/style/images/body2/show_bg.png) no-repeat 0 0;width:44px;height:44px;}
.tbl_wp td p.img_p img{position:absolute;top:0;left:0;width:40px;}
.tbl_wp td p.img_p:hover img.mm_1{-webkit-animation-name:myMovie;-webkit-animation-duration:0.5s;}
.tbl_wp td p.img_p:hover img.mm_2{-webkit-animation-name:myMovieO;-webkit-animation-duration:0.5s;}
.tbl_wp td p.img_p:hover img.mm_3{-webkit-animation-name:myMovieT;-webkit-animation-duration:0.5s;}
.tbl_wp td p.img_p:hover img.mm_4{-webkit-animation-name:myMovieF;-webkit-animation-duration:0.5s;}
.tbl_wp td p.txt_p{cursor:pointer;text-align:center;color:#2b4157;font-size:12px;}
.tbl_wp td span.img_span{display:block;background:url(/style/images/body2/show_bg.png) no-repeat 13px -45px;height:44px;text-align:center;}
.tbl_wp td span.txt_span{display:block;color:#2b4157;}
@-webkit-keyframes myMovie{
	0%{top:1px;}   	5%{left:2px;}
	10%{top:0;} 	15%{left:1px;}
	20%{top:1px;} 	25%{left:2px;}
	30%{top:0;} 	35%{left:1px;}
	40%{top:1px;} 	45%{left:2px;}
	50%{top:0;} 	55%{left:1px;}
	60%{top:1px;} 	65%{left:2px;}
	70%{top:0;} 	75%{left:1px;}
	80%{top:1px;} 	85%{left:2px;}
	90%{top:0;} 	95%{left:1px;}
}
@-webkit-keyframes myMovieO{
	0%{opacity:1;-webkit-transform:scale(1);}
	50%{opacity:0;-webkit-transform:scale(5);}
	100%{opacity:1;-webkit-transform:scale(1);}
}
@-webkit-keyframes myMovieT{
	0%{top:2px;}   	5%{top:0;}
	10%{top:2px;} 	15%{top:0;}
	20%{top:2px;} 	25%{top:0;}
	30%{top:2px;} 	35%{top:0;}
	40%{top:2px;} 	45%{top:0;}
	50%{top:2px;} 	55%{top:0;}
	60%{top:2px;} 	65%{top:0;}
	70%{top:2px;} 	75%{top:0;}
	80%{top:2px;} 	85%{top:0;}
	90%{top:2px;} 	95%{top:0;}
}
@-webkit-keyframes myMovieF{
	0%{left:0px;}   5%{left:2px;}
	10%{left:0px;} 	15%{left:2px;}
	20%{left:0px;} 	25%{left:2px;}
	30%{left:0px;} 	35%{left:2px;}
	40%{left:0px;} 	45%{left:2px;}
	50%{left:0px;} 	55%{left:2px;}
	60%{left:0px;} 	65%{left:2px;}
	70%{left:0px;} 	75%{left:2px;}
	80%{left:0px;} 	85%{left:2px;}
	90%{left:0px;} 	95%{left:2px;}
}
#contentright{min-height: 500px;}
</style>
</head>

<body class="html" onload="NewMenuAction();">
<div id="scroll-wrap" >

	<div class="wrap">
	
	   
			<div id="pageTitle">
				<i> 
						高级功能提示------ 
				</i>
				<i style="color:#333;font-size:14px;">
					非专业人士使用本高级功能可能会造成以下后果：

				</i>
			</div>
			<div id="bodyUl" style="padding:0 0 0 10px;margin:10px 0 0 0;">
				<ul style="padding:0 250px;">
					<li>1.系统崩溃</li>
					<li>2.数据混乱</li>
					<li>3.报表数据错乱</li>
					<li>4.模块丢失</li>
				</ul>
			</div>
			<br>
			<div style="padding:10px 0;">
				<table border="0" cellpadding="0" cellspacing="0" align="center" class="tbl_wp" style="margin:0 auto;">
				<tr>
					<td id="tdx1y1" name="tdx1y1" align="center" class="img_td">
						<p class="img_p"><img onclick="javascript:mdiwin('/CustomQueryAction.do?src=menu','表结构管理');" title="表结构管理" src="/style/images/newMenu/sale1.png"/></p>
			 			<p class="txt_p" onclick="javascript:mdiwin('/CustomQueryAction.do?src=menu','表结构管理');" title="表结构管理"> 表结构管理 </p>
			 		</td>
		 			<td id="tdx2y1" name="tdx2y1" align="center" class="img_td">
		 				<p class="img_p"><img onclick="javascript:mdiwin('/ReportSetQueryAction.do?src=menu','报表设置');" title="报表设置" src="/style/images/newMenu/sale8.png"/></p>
		 				<p class="txt_p" onclick="javascript:mdiwin('/ReportSetQueryAction.do?src=menu','报表设置');" title="报表设置"> &nbsp;报表设置&nbsp; </p>
		 			</td>
			 		<td id="tdx2y2" name="tdx2y2" align="center" class="img_td">
			 			<p class="img_p"><img onclick="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblModules&src=menu','系统模块管理');" title="系统模块管理" src="/style/images/newMenu/sale16.png"/></p>
		 				<p class="txt_p" onclick="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblModules&src=menu','系统模块管理');" title="系统模块管理">系统模块管理</p>
				 	</td>
				 	<td id="tdx2y3" name="tdx2y3" align="center" class="img_td">
				 		<p class="img_p"><img onclick="javascript:mdiwin('/tableMappedQueryAction.do?src=menu','字段映射关系');" title="字段映射关系" src="/style/images/newMenu/sale20.png"/></p>
		 				<p class="txt_p" onclick="javascript:mdiwin('/tableMappedQueryAction.do?src=menu','字段映射关系');" title="字段映射关系">字段映射关系</p>
		 			</td>
		 			<!-- <td id="tdx2y4" name="tdx2y4" align="center" class="img_td">
		 				<p class="img_p"><img onclick="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblTimingMsg&src=menu','定时通知高级模板');" title="定时通知高级模板" src="/style/images/newMenu/sale35.png"/></p>
 						<p class="txt_p" onclick="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblTimingMsg&src=menu','定时通知高级模板');" title="定时通知高级模板">定时通知高...</p>
 					</td> -->
 					<td id="tdx2y4" name="tdx2y4" align="center" class="img_td">
		 				<p class="img_p"><img onclick="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblModuleFlow','模块导航设置');" title="模块导航设置" src="/style/images/newMenu/sale3.png"/></p>
 						<p class="txt_p" onclick="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblModuleFlow','模块导航设置');" title="模块导航设置">模块导航设置</p>
 					</td>
 					<td id="tdx2y4" name="tdx2y4" align="center" class="img_td">
		 				<p class="img_p"><img onclick="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblModuleField','模块字段设置');" title="模块字段设置" src="/style/images/newMenu/sale3.png"/></p>
 						<p class="txt_p" onclick="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblModuleField','模块字段设置');" title="模块字段设置">模块字段设置</p>
 					</td>
 				</tr>
 				<tr>
 					<td id="tdx2y5" name="tdx2y5" align="center" class="img_td">
		 				<p class="img_p"><img onclick="javascript:mdiwin('/AlertSetAction.do?operation=4&optype=query','预警项目设置');" title="预警项目设置" src="/style/images/newMenu/sale25.png"/></p>
 						<p class="txt_p" onclick="javascript:mdiwin('/AlertSetAction.do?operation=4&optype=query','预警项目设置');" title="预警项目设置">预警项目设置</p>
 					</td>
 					<td id="tdx2y6" name="tdx2y6" align="center" class="img_td">
		 				<p class="img_p"><img onclick="javascript:mdiwin('/CertTemplateAction.do?operation=4','凭证模板');" title="凭证模板" src="/style/images/newMenu/sale10.png"/></p>
 						<p class="txt_p" onclick="javascript:mdiwin('/CertTemplateAction.do?operation=4','凭证模板');" title="凭证模板">凭证模板</p>
 					</td>
 					<td id="tdx2y8" name="tdx2y8" align="center" class="img_td">
		 				<p class="img_p"><img onclick="javascript:mdiwin('/EnumerationQueryAction.do','选项数据管理');" title="选项数据管理" src="/style/images/newMenu/sale22.png"/></p>
 						<p class="txt_p" onclick="javascript:mdiwin('/EnumerationQueryAction.do','选项数据管理');" title="选项数据管理">选项数据管理</p>
 					</td>
 					<td id="tdx2y8" name="tdx2y8" align="center" class="img_td">
		 				<p class="img_p"><img onclick="javascript:mdiwin('/CustomAction.do?type=defineQuery&from=adv','自定义文件');" title="修改自定义文件" src="/style/images/newMenu/sale25.png"/></p>
 						<p class="txt_p" onclick="javascript:mdiwin('/CustomAction.do?type=defineQuery&from=adv','自定义文件');" title="修改自定义文件">自定义文件</p>
 					</td>
 					<td id="tdx2y8" name="tdx2y8" align="center" class="img_td">
		 				<p class="img_p"><img onclick="javascript:mdiwin('/CustomAction.do?type=popupQuery&from=adv','弹出窗修改');" title="弹出窗修改" src="/style/images/newMenu/sale25.png"/></p>
 						<p class="txt_p" onclick="javascript:mdiwin('/CustomAction.do?type=popupQuery&from=adv','弹出窗修改');" title="弹出窗修改">弹出窗修改</p>
 						<!-- 
		 				<p class="img_p"><img onclick="restart();" title="重启系统" src="/style/images/newMenu/sale29.png"/></p>
 						<p class="txt_p" onclick="restart();" title="重启系统">重启系统</p>
 						 -->
 					</td>
 					<td id="tdx2y8" name="tdx2y8" align="center" class="img_td">
		 				<p class="img_p"><img onclick="frmem();" title="刷新内存" src="/style/images/newMenu/sale17.png"/></p>
 						<p class="txt_p" onclick="frmem();" title="刷新内存">刷新内存</p>
 					</td> 					
			 	</tr>
			 	</table>
			</div>
			<div id="regfoot" style="width:70%;">
			</div>
		

</div>
</div>
</body>
</html>



