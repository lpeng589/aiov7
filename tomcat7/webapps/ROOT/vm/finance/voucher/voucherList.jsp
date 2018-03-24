<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>凭证列表</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/style1/css/workflow2.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" href="/style1/css/fg.menu.css" media="screen" rel="stylesheet" />
<link type="text/css" href="/style1/css/ui.all.css" media="screen" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/gen/listPage.vjs_zh_CN.js?1357894434000"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/financereport.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/fg.menu.js"></script>
 
<style type="text/css">
.framework{margin:0 auto;}
#data_list_id{background-color:white;}
.td_occd1{width: 3%;text-align: center;}
.td_occd8{width: 6%;}
.td_occd2{width: 5%;}
.td_occd3{width: 5%;}
.td_occd4{width: 10%;}
.td_occd5{width: 15%;}
.td_occd7{width: 5%;}
.data_list_head{text-align: center;}
.framework td{padding-left: 0px;}
.btnlp{margin:6px;float: left;cursor: pointer;}
.search_popup input{border: 1px solid #b4b4b4;}
.TopTitle{margin-left: 8px;}
.mouseover{border: 1px gray solid;}
.search_popup{z-index: 99;margin:-200px 0px -200px -220px;display:none;
		#if("$!AccMainSetting.isAuditing"=="1")
			height:80%;
		#else
			height:70%;
		#end
		width:460px;
		top: 50%;
		left: 50%;
		border: #808080 1px solid;
}
.search_popup table div{line-height: normal;padding-left: 0px;background: none;width: auto;font-weight: normal;border: 1px solid #b4b4b4;padding: 0px 0px 5px 10px;height: auto;}
.search_popup table div input{margin-top:5px;}
.tdDes1{text-align:right;}
.tdDes2{text-align:center;}
.tdDes3{text-align:left;}
label{font-family:微软雅黑;margin:0;padding:0;font-size:12px;}
.a-width{width:80px;height:10px;}
.data_list_head{box-sizing:border-box;border:1px #ccc solid;}
.list_table{box-sizing:border-box;line-height:30px;table-layout:fixed;width:100%;border-bottom:1px #ccc solid;border-left:1px #ccc solid;}
.list_table tr td{overflow:hidden;text-overflow:ellipsis;white-space:nowrap;border-top:1px #ccc solid;border-right:1px #ccc solid;}
a.ui-corner-all{font-family:微软雅黑;}
a.fg-button{border:1px solid #C4C5C3;}
.printMore{border-right: 1px solid #d1d1d1;margin-left:3px}
.printMore li{border-bottom: 1px solid #d1d1d1;border-left: 1px solid #d1d1d1;border-top: 0px;padding-left: 4px;padding-right: 4px;font-size: 12px;height: 22px;line-height: 180%;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;}
.printMore .selectedprintMore{background:#93EE1E}
</style>

<script type="text/javascript">
//*****列宽可调*****//
$(document).ready(function(){
	dragW("list");
});
var tTD;
function dragW(tab)
{
	divs = ["."+tab+"_head","."+tab+"_div","."+tab+"_tableColumn"];

	var n = 0;
	$(divs[0]+" td").each(function(){
		if($(this).attr('colspan')==undefined)
			n +=1;
		else
			n += Number($(this).attr('colspan'));
		//以下三个事件为调整列宽使
		this.onmousedown = function () {
			if (event.offsetX > this.offsetWidth - 10) {
				tTD = this;				
				tTD.mouseDown = true;
				tTD.oldX = event.x;
				tTD.oldWidth = tTD.offsetWidth;
				tTD.xoldWidth = jQuery(divs[1]).width();
				//tTD.xtoldWidth = jQuery(divs[2]+" table").width();
				
			}
		};
		this.onmouseup = function () {
			if(tTD != undefined)
			{
				//结束宽度调整
				tTD.mouseDown = false;
				tTD.style.cursor = 'default';
			}
		};
		this.onmousemove = function () {
			//更改鼠标样式
			if (event.offsetX > this.offsetWidth - 10)
				this.style.cursor = 'col-resize';
			else
				this.style.cursor = 'default';

			if(tTD == undefined) return;
		
			//调整宽度
			if (tTD.mouseDown != null && tTD.mouseDown == true) {
				if (tTD.oldWidth + (event.x - tTD.oldX)>0)
				{
					tTD.width = tTD.oldWidth + (event.x - tTD.oldX);
					tTD.xwidth = tTD.xoldWidth + (event.x - tTD.oldX);
					tTD.xtwidth = tTD.xtoldWidth + (event.x - tTD.oldX);
					var indextd = jQuery(divs[0]).find("thead td").index($(tTD));
					//调整列宽
					if(indextd>-1)
					{
						jQuery(divs[0]+" thead td:eq("+indextd+")")[0].width = tTD.width;
						jQuery(divs[0]).width(tTD.xwidth);
						jQuery(divs[1]+" tbody td:eq("+indextd+")")[0].width = tTD.width;
						//jQuery(divs[2]+" thead td:eq("+indextd+")")[0].width = tTD.width;
						jQuery(divs[1]).width(tTD.xwidth);
						tTD.style.width = tTD.width;
					}
					/*else{ //此时调整的是锁定列前的列
						
						var indextd = jQuery(divs[0]).find("thead td").index($(tTD));
						//调整列宽
						if(indextd>-1)
						{
							jQuery(divs[1]+" thead td:eq("+indextd+")")[0].width = tTD.width;
							jQuery(divs[1]+" table").width(tTD.xwidth);
							jQuery(divs[2]+" thead td:eq("+indextd+")")[0].width = tTD.width;
							jQuery(divs[2]+" table").width(tTD.xwidth);
							
							jQuery(divs[0]+" thead td:eq("+indextd+")")[0].width = tTD.width;
							jQuery(divs[0]+" table").width(tTD.xtwidth);
							jQuery(divs[3]+" thead td:eq("+indextd+")")[0].width = tTD.width;
							jQuery(divs[3]+" table").width(tTD.xtwidth);
							jQuery(divs[0]).width(jQuery(divs[3]+" table").width()+1);
							jQuery(divs[3]).width(jQuery(divs[3]+"#k_column table").width()+1);
							
							tTD.style.width = tTD.width;
						}					
						
					}*/
				}
				tTD.style.cursor = 'col-resize';
			}
		};
	});
}

//******end******//
</script>

<script type="text/javascript">
	$(document).ready(function() {
		//添加
		$("#add").bind("click", function(){
			var notOpenAccount = false;//未开帐不能做单 
			#if($!LoginBean.sessMap.get('AccPeriod').accMonth < 0)		
				notOpenAccount = true;
			#end
			if(notOpenAccount){
				　asyncbox.confirm('未开帐不能做单，是否立即开帐','开帐提示',function(action){
				　　　if(action == 'ok'){
				　　　　　window.location.href="/SysAccAction.do?type=beginAcc";
				　　　}
				　});	
				return;
			}
			adds();
		});
		$("#batchdelete").bind("click", function(){
			//批量删除
			if(!isChecked('keyId')){
				alert("$text.get("common.msg.mustSelectOne")");
				return false;
			}else{
				asyncbox.confirm('你确定删除吗？','提示',function(action){
				　　if(action == 'ok'){
						form.operation.value=3;
						form.submit();
					}
				});
			}
		});
		//高级搜索
		$("#querys").bind("click", function(){
			if($('#highSearch').css("display")== "none"){
			    $('#highSearch').show();
			}
			else{
			    $('#highSearch').hide();
			}
		});
		//导入
		$("#import").bind("click", function(){
			imports();
		});
		//导出
		$("#export").bind("click", function(){
			exports();
		});
		//审核不通过
		$("#noPass").bind("click", function(){
			noPass();
		});
		//审核通过
		$("#yesPass").bind("click", function(){
			dealPass('yesPass');
		});
		//批量反审核





		$("#reversePass").bind("click", function(){
			dealPass('reversePass');
		});
		//批量过账
		$("#batchbinder").bind("click", function(){
			dealBinder('binder');
		});
		//批量反过账





		$("#reverseBinder").bind("click",function(){
			dealBinder('reverseBinder');
		});
		//过账向导
		$("#binderGuide").bind("click",function(){
			binderGuide();
		});
		//审核向导
		$("#AuditeGuide").bind("click",function(){
			AuditeGuide();
		});
		//批量复核
		$("#check").bind("click",function(){
			batchcheck('check');
		});
		
		$("#closes").bind("click",function(){
			closeWin();
		})
		//批量反复核





		$("#reversecheck").bind("click",function(){
			batchcheck('reverseCheck');
		});
		//打印列表
		$("#prints").bind("click",function(){
			prints();
		});
		//打印凭证
		$("#printsBill").bind("click",function(){
			printsBill();
		});
		//刷新
	 	$("#refur").bind("click",function(){
			onsubmits();
	 	});
	 	//关闭
		$("#closes").bind("click", function(){
			if("$!popWinName"!=""){
				window.parent.jQuery.close('$!popWinName');
			}else{
				window.parent.jQuery.close('PopQuerydiv');
			}
			closeWin();
		});
		//鼠标右键
		$('#data_list_id .context-menu-one').contextPopup({
	       title: '',
	       items: [
	       	 #if($LoginBean.operationMap.get("/VoucherManageAction.do").update() || $!LoginBean.id=="1")
	         {icon:'0 -578px',label:'修改',action:function(e) {
		         var keyId = $(e.target).parents("tr").find("input[name=keyId]").attr("value") ;
		         var counts = $(e.target).parents("tr").find("input[name=counts]").attr("value") ;
		         updates(keyId,counts);
	         }},
	         #end
	         #if($LoginBean.operationMap.get("/VoucherManageAction.do").delete() || $!LoginBean.id=="1")
	         {icon:'0 -578px',label:'删除',action:function(e) {
		         var keyId = $(e.target).parents("tr").find("input[name=keyId]").attr("value") ;
		         deletes(keyId);
	         }},
	         #end
	         {icon:'-1px -684px',label:'详情',action:function(e) { 
				 var keyId = $(e.target).parents("tr").find("input[name=keyId]").attr("value") ;
		         var counts = $(e.target).parents("tr").find("input[name=counts]").attr("value") ;
				 details(keyId,counts);
		 	}},
		 	#if($LoginBean.operationMap.get("/VoucherManageAction.do").add() || $!LoginBean.id=="1")
		 	{icon:'-1px -684px',label:'添加',action:function(e) { 
				 adds();
		 	}},
		 	#end
		 	#if("$!AccMainSetting.isAuditing"=="1")
		 	#if($!globals.isExistInArray($!AccMainSetting.auditingPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
		 	{icon:'-1px -684px',label:'审核',action:function(e) { 
		 		var keyId = $(e.target).parents("tr").find("input[name=keyId]").attr("value");
		 		rightBtn('dealapprov','yesPass',keyId);
		 	}},
		 	#end
		 	#end
		 	#if("$!AccMainSetting.isCheck"=="1")
		 	#if($!globals.isExistInArray($!AccMainSetting.checkPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
		 	{icon:'-1px -684px',label:'复核',action:function(e) { 
		 		var keyId = $(e.target).parents("tr").find("input[name=keyId]").attr("value");
		 		rightBtn('dealcheck','check',keyId);
		 	}},
		 	#end
		 	#end
		 	#if("$!AccMainSetting.isAuditing"=="1")
		 	#if($!globals.isExistInArray($!AccMainSetting.binderPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
		 	{icon:'-1px -684px',label:'过账',action:function(e) { 
		 		var keyId = $(e.target).parents("tr").find("input[name=keyId]").attr("value");
		 		rightBtn('dealbinder','binder',keyId);
		 	}},
		 	#end#end
		 	{icon:'-1px -684px',label:'导入',action:function(e) { 
		 		imports();
		 	}},
		 	{icon:'-1px -684px',label:'导出',action:function(e) { 
		 		var keyId = $(e.target).parents("tr").find("input[name=keyId]").attr("value") ;
		 		if(!confirm("$text.get('common.msg.whetherExport')"))return
		 		window.location.href="/VoucherManage.do?operation=4&optype=exportVoucher&tableName=tblAccMain&keyId="+keyId;
		 	}}
		 	#if($LoginBean.operationMap.get("/VoucherManageAction.do").print() || $!LoginBean.id=="1")
		 	,
		 	{icon:'-1px -684px',label:'列表打印',action:function(e) { 
		 		prints();
		 	}}
		 	,
		 	{icon:'-1px -684px',label:'打印凭证',action:function(e) { 
		 		printsBill();
		 	}}
		 	#end
	       ]
     	});
     	$(".TopTitle div:not(.searchDiv)").mouseover(function(){
     		//$(this).addClass("mouseover");
     	});
     	$(".TopTitle div:not(.searchDiv)").mouseout(function(){
     		//$(this).removeClass("mouseover");
     	});
     	$(".searchDiv select").change(function (){
     		form.submit();
     	});
		$('#auditinga').menu({ 
			content: $('#auditinga').next().html(),
			showSpeed: 400 
		});
		$('#reviewa').menu({ 
			content: $('#reviewa').next().html(),
			showSpeed: 400 
		});
		$('#wNamea').menu({ 
			content: $('#wNamea').next().html(),
			showSpeed: 400 
		});
		/* 根据数字金额转换为大写金额 */
		$("#data_list_id tbody").find(".td_title").mouseover(function(){
			var objhtml = $(this).html();
			if(objhtml!=null && objhtml!="" && objhtml!="&nbsp;"){
				var changeValue = AmountInWords(objhtml,null);
				$(this).attr("title",changeValue);
			}
		});
	});
	
	/* 审核不通过*/
	function noPass(){
		var ids = "";
		$('input[name=keyId]:visible').each(function(i,n){
			if($(n).is(':checked')){
				ids = ids + $(n).val() + ";";
			}
		});
		if(ids == ""){
			alert('$text.get("common.msg.mustSelectOne")');
			return false;
		}
		var url = '/VoucherManage.do?optype=approveRemarkPre&isList=update&voucherId='+ids;
		url = encodeURI(encodeURI(url));
		asyncbox.open({
			id : 'nopassDiv',
	　　		url : url,
		    title : '批注',
		　　 width : 480,
		　　 height : 220,
	    	btnsbar : jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
		    	if(action == 'ok'){
		    		var remark = opener.document.getElementById("remark");
					if(remark.value == ""){
						alert("批注不能为空！");
						remark.focus();
						return false;
					}
					opener.saves();
					return false;
		    	}
	　　	　	}
　		});
	}
	
	//右键提交
	function rightBtn(optype,dealType,keyId){
		form.operation.value="";
 		form.action='/VoucherManageAction.do?keyId='+keyId;
		form.optype.value=optype;
		form.dealType.value=dealType;
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		form.target = 'formFrame' ;
		form.submit();
	}
	function closeSearch(){
		$('#highSearch').hide();
	}
	/* 打印列表*/
	function prints(){
		printControl("/UserFunctionQueryAction.do?tableName=tblGoods&reportNumber=tblAccMain&moduleType=&operation=18&parentTableName=&winCurIndex=12");
	}
	/* 打印列表*/
	function printsBill(){
		var keyVal = $("input[type=checkbox][name=keyId]:checked");
		if(keyVal.size()==0){
			alert('$text.get("common.msg.mustSelectOne")');
			return false;
		}	
		
		selPrintMoreFormat();			

	}
	function selPrintMoreFormat(){
		mfhtml = "<ul class='printMore'>"+
		#foreach($row in $formatList)
		"<li file='$!globals.get($row,1)' onclick='selPmf(this)' ondblclick='doPrint()' #if($velocityCount==1) class='selectedprintMore' #end >$!globals.get($row,0)</li>"+
		#end
		"</ul>";
		asyncbox.open({id:'printMoreFormat',title:'选择样式',
			html:mfhtml,
			btnsbar:jQuery.btn.OKCANCEL,
		    callback:function(action,opener){
			    if(action == 'ok'){
					doPrint();
				}
	　　     	},width:200,height:200
		});
	}
	function doPrint(){ //这方法不宜放入defineList
		pname = $(".printMore").find(".selectedprintMore").text();
		pfile = $(".printMore").find(".selectedprintMore").attr("file");
		
		jQuery.close("printMoreFormat");
	
		var keyVal="";		
		var keyIds=document.getElementsByName("keyId");
		for(var i=0;i<keyIds.length;i++){
			if(keyIds[i].checked){
				billID=keyIds[i].value;	
				try{				
					var mimetype = navigator.mimeTypes["application/np-print"];		
					if(mimetype){
						if(mimetype.enabledPlugin){
							 var cb = document.getElementById("plugin");
							 cb.print('$!IP|$!port',pname+"@col"+pfile+"@row",billID,'','tblAccMain','$!JSESSIONID','$LoginBean.id','zh_CN',"");
						}
					}else{
						var obj = new ActiveXObject("KoronReportPrint.BatchPrint") ;
					    obj.URL="$!IP|$!port" ;
					    obj.SQL="" ;
					    obj.StyleList =pname+"@col"+pfile+"@row" ;
					    obj.BillID=billID ;
					    obj.BillTable="tblAccMain" ;
					   	obj.Cookie ="$!JSESSIONID" ;
					   	obj.UserId ='$LoginBean.id' ;
					    obj.ReportNumber="" ;
					    obj.Language = "$!globals.getLocale()" ;
						obj.doPrint();	
					}
				}catch (e){
					asyncbox.alert("$text.get('com.sure.print.control')<br><br><a class='aio-print' href='/common/AIOPrint.exe' target='_blank'>下载控件</a>","信息提示");
				}
		
			}
		}
	}
	function selPmf(obj){
		$(".printMore").find(".selectedprintMore").removeClass("selectedprintMore");
		$(obj).addClass("selectedprintMore");
	}
	//添加
	function adds(){
		turl="/VoucherManage.do?operation=6&isEspecial=list";
		width=document.documentElement.clientWidth;
		height=document.documentElement.clientHeight;
		openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
		
	}
	//导入
	function imports(){
		mdiwin('/importDataAction.do?fromPage=importList&operation=91&tableName=tblAccMain&parentTableName=&moduleType=&winCurIndex=8','数据导入');
	}
	//删除
	function deletes(id){
		asyncbox.confirm('你确定删除吗？','提示',function(action){
		　　 if(action == 'ok'){
				document.location = "/VoucherManageAction.do?operation=3&tableName=tblAccMain&keyId="+id;
			}
		});
	}
	/* 时间选择*/
	function openInputDate(obj){
		WdatePicker({lang:'zh_CN'});
	}
	//修改
	function updates(id,number){
		turl = "/VoucherManageAction.do?operation=7&tableName=tblAccMain&id="+id+"&number="+number;
		width=document.documentElement.clientWidth;
		height=document.documentElement.clientHeight;
		openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight); 
	}
	//详情
	function details(id,number){
		turl = "/VoucherManageAction.do?operation=5&tableName=tblAccMain&id="+id+"&number="+number;
		width=document.documentElement.clientWidth;
		height=document.documentElement.clientHeight;
		openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
		
	}
	//导出
	function exports(){
		if((typeof(selected)=="undefined" && !isChecked("keyId")) 
			|| (typeof(selected)!="undefined" && selected && !isChecked("keyId"))){		
			if(!confirm("未选择任何记录时导出，默认会导出所有凭证，是否继续？"))
				return
		}else{
			if(!confirm("$text.get('common.msg.whetherExport')"))
				return
		}
		document.getElementById("ButtonType").value = "billExport" ;
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		form.optype.value="exportVoucher";
		form.submit();
	}
	//审核通过，审核不通过，反审核
	function dealPass(dealType){
		if((typeof(selected)=="undefined" && !isChecked("keyId")) 
			|| (typeof(selected)!="undefined" && selected && !isChecked("keyId"))){		
			alert('$text.get("common.msg.mustSelectOne")');
			return false;
		}
		var str = "";
		if(dealType == "noPass"){
			str = "是否确定审核不通过已选凭证！";
		}else if(dealType == "yesPass"){
			str = "是否确定审核通过已选凭证！";
		}else if(dealType == "reversePass"){
			str = "是否确定反审核已选凭证！";
		}
		if(!confirm(str))return
		form.operation.value="";
		form.optype.value="dealapprov";
		form.dealType.value=dealType;
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		form.target = 'formFrame' ;
		form.submit();
	}
	
	//批量过账，批量反过账
	function dealBinder(dealType){
		if((typeof(selected)=="undefined" && !isChecked("keyId")) 
			|| (typeof(selected)!="undefined" && selected && !isChecked("keyId"))){		
			alert('$text.get("common.msg.mustSelectOne")');
			return false;
		}
		var str = "";
		if(dealType == "binder"){
			str = "是否确定过账已选凭证！";
		}else if(dealType == "reverseBinder"){
			str = "是否确定反过账已选凭证！";
		}
		if(!confirm(str))return
		form.operation.value="";
		form.optype.value="dealbinder";
		form.dealType.value=dealType;
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		form.target = 'formFrame' ;
		form.submit();
	}
	
	//过账向导
	function binderGuide(){
		var url = '/VoucherManage.do?optype=binderGuidePre';
		url = encodeURI(encodeURI(url));
		asyncbox.open({
			id : 'binderDiv',
	　　		url : url,
		    title : '过账向导',
		　　 width : 500,
		　　 height : 300,
	    	btnsbar : jQuery.btn.CANCEL.concat([{
	    		text : '断号检查',
	    		action : 'breaknumber'
	    	},{
	    		text : '开始过账',
	    		action : 'start'
	    	}]),
		    callback : function(action,opener){
		    	if(action == 'breaknumber'){
		    		//var credTypeidvalue = opener.document.getElementById("credTypeid").value;
		    		stopCredNo("");
		    		return false;
		    	}else if(action == 'start'){
		    		//开始过账




		    		var voucherarea = opener.document.getElementsByName("voucherarea");
		    		
		    		if(voucherarea[1].checked){
		    			var vouchertime = opener.document.getElementById("vouchertime").value;
		    			if(vouchertime==""){
		    				alert("请输入凭证日期");
		    				opener.document.getElementById("vouchertime").focus();
		    				return false;
		    			}
		    		}
		    		if(voucherarea[2].checked){
		    			var voucherPeriodYear = opener.document.getElementById("voucherPeriodYear").value;
		    			var voucherPeriod = opener.document.getElementById("voucherPeriod").value;
		    			if(voucherPeriodYear==""){
		    				alert("请输入期间年");
		    				opener.document.getElementById("voucherPeriodYear").focus();
		    				return false;
		    			}
		    			if(voucherPeriod==""){
		    				alert("请输入期间");
		    				opener.document.getElementById("voucherPeriod").focus();
		    				return false;
		    			}
		    		}
		    		opener.saves();
					return false;
		    	}else if(action == 'closes'){
		    	}
	　　	　	}
　		});
	}
	//审核向导
	function AuditeGuide(){
		var url = '/VoucherManage.do?optype=AuditeGuidePre';
		url = encodeURI(encodeURI(url));
		asyncbox.open({
			id : 'binderDiv',
	　　		url : url,
		    title : '审核向导',
		　　 width : 500,
		　　 height : 300,
	    	btnsbar : jQuery.btn.CANCEL.concat([{
	    		text : '开始审核',
	    		action : 'start'
	    	}]),
		    callback : function(action,opener){
		    	if(action == 'start'){
		    		//开始过账


		    		var voucherarea = opener.document.getElementsByName("voucherarea");
		    		
		    		if(voucherarea[1].checked){
		    			var vouchertime = opener.document.getElementById("vouchertime").value;
		    			if(vouchertime==""){
		    				alert("请输入凭证日期");
		    				opener.document.getElementById("vouchertime").focus();
		    				return false;
		    			}
		    		}
		    		if(voucherarea[2].checked){
		    			var voucherPeriodYear = opener.document.getElementById("voucherPeriodYear").value;
		    			var voucherPeriod = opener.document.getElementById("voucherPeriod").value;
		    			if(voucherPeriodYear==""){
		    				alert("请输入期间年");
		    				opener.document.getElementById("voucherPeriodYear").focus();
		    				return false;
		    			}
		    			if(voucherPeriod==""){
		    				alert("请输入期间");
		    				opener.document.getElementById("voucherPeriod").focus();
		    				return false;
		    			}
		    		}
		    		opener.saves();
					return false;
		    	}else if(action == 'closes'){
		    	}
	　　	　	}
　		});
	}
	//断号查询
	function stopCredNo(credTypeidvalue){
		var url = '/VoucherManage.do?optype=stopCredNoList&CredTypeID='+credTypeidvalue;
		url = encodeURI(encodeURI(url));
		asyncbox.open({
			id : 'stopCredNoDiv',
	　　		url : url,
		    title : '断号情况',
		　　 width : 570,
		　　 height : 370,
	    	btnsbar : jQuery.btn.CANCEL,
		    callback : function(action,opener){
		    	if(action == 'closes'){
		    		
		    	}
	　　	　	}
　		});
	}
	//批量复核，批量反复核
	function batchcheck(dealType){
		if((typeof(selected)=="undefined" && !isChecked("keyId")) 
			|| (typeof(selected)!="undefined" && selected && !isChecked("keyId"))){		
			alert('$text.get("common.msg.mustSelectOne")');
			return false;
		}
		var str = "会计科目启用现金科目类,银行科目类,现金等价物类之一需要";
		if(dealType == "check"){
			str = "复核,是否确定复核已选凭证！";
		}else if(dealType == "reverseCheck"){
			str = "反复核,是否确定反复核已选凭证！";
		}
		if(!confirm(str))return
		form.operation.value="";
		form.optype.value="dealcheck";
		form.dealType.value=dealType;
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		form.target = 'formFrame' ;
		form.submit();
	}
	//全选





	var isAllSelectSelected = false;
	function checkAll(name){
		$('input[name='+name+']:visible').each(function(i,n){
			if(!$(n).is(':disabled')){
				$(n).attr("checked", !isAllSelectSelected);
			}
		});
	  isAllSelectSelected = !isAllSelectSelected;
	}
	function onsubmits(){
		document.form.operation.value=4;
		form.target='';
		var searchIsAudit = $("#highSearch select[name=searchIsAuditing]").val();
		$(".searchDiv input[name=searchIsAuditing]").val(searchIsAudit);
		var searchIsReview = $("#highSearch select[name=searchIsReview]").val();
		$(".searchDiv input[name=searchIsReview]").val(searchIsReview);
		var searchwName = $("#highSearch select[name=searchwName]").val();
		$(".searchDiv input[name=searchwName]").val(searchwName);
		if($("#searchCredYearStart").val() !="" && !isInt2($("#searchCredYearStart").val())){
			alert("会计期间年必须是正整数");
			$("#searchCredYearStart").focus();
			return false;
		}
		if($("#searchCredMonthStart").val() !="" && !isInt2($("#searchCredMonthStart").val())){
			alert("会计期间必须是正整数");
			$("#searchCredMonthStart").focus();
			return false;
		}
		if($("#searchCredYearEnd").val() !="" && !isInt2($("#searchCredYearEnd").val())){
			alert("会计期间年必须是正整数");
			$("#searchCredYearEnd").focus();
			return false;
		}
		if($("#searchCredMonthEnd").val() !="" && !isInt2($("#searchCredMonthEnd").val())){
			alert("会计期间必须是正整数");
			$("#searchCredMonthEnd").focus();
			return false;
		}
		if($("#searchOrderNoStart").val() !="" && !isInt2($("#searchOrderNoStart").val())){
			alert("凭证号开始必须是正整数");
			$("#searchOrderNoStart").focus();
			return false;
		}
		if($("#searchOrderNoEnd").val() !="" && !isInt2($("#searchOrderNoEnd").val())){
			alert("会计期间必须是正整数");
			$("#searchOrderNoEnd").focus();
			return false;
		}
		//清空部门
		if($("#searchDeptName").val()==""){
			$("#searchDept").val('');
		}
		//清空经办人





		if($("#searchEmployeeName").val()==""){
			$("#searchEmployee").val('');
		}
		//清空往来单位





		if($("#searchClientName").val()==""){
			$("#searchClient").val('');
		}
		document.form.submit();
	}
	var inputName = "";
	/* 会计科目弹出框 */
	function selectCode(value){
		inputName = value;
		var urlstr = '/PopupAction.do?popupName=popAccTypeInfo&chooseType=all&inputType=radio&returnName=Popdiv&isCheckItem=false';
		urlstr += "&isCease="+$("#showBanAccTypeInfo").is(":checked");
		asyncbox.open({
			id:'Popdiv',
			title:'会计科目',
			url:urlstr,
			width:750,
			height:470,
			btnsbar : [{
		     text    : '清&nbsp;&nbsp;&nbsp;空',
		      action  : 'clearbtn'
		  	}].concat(jQuery.btn.OKCANCEL),
			callback : function(action,opener){
				if(action == 'ok'){
					var values = opener.datas();
					Popdiv(values);
				}else if(action == 'clearbtn'){
					$("#"+inputName).val('');
				}
			}
		});
	}
	/* 回填数据 */
	function Popdiv(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var nateStr = returnValue.split("#|#");
		for(var j=0;j<nateStr.length;j++){
			if(nateStr[j]!=""){
				var note = nateStr[j].split("#;#");
				$("#"+inputName).val(note[0]);
			}
		}
	}
	
	function showBillNo(billId,billtype){
		//window.open("/UserFunctionAction.do?tableName="+billtype+"&keyId="+billId+"&operation=5&1=1&noback=true&queryChannel=normal&LinkType=@URL:");
		var width = document.documentElement.clientWidth;
		var height = document.documentElement.clientHeight;
		usrc ='/UserFunctionAction.do?tableName='+billtype+'&keyId='+billId+'&operation=5&noback=false&queryChannel=normal&LinkType=@URL:';
		if(billtype == "tblFixedAssetAdd"){
			usrc += "&moduleType=1";
		}
		openPop('PopMainOpdiv','',usrc,width,height,true,height);  
	}
	
	var parentsId = "";
	var parentsName = "";
	function selectPops(parentid,parentName,popname,name){
		var displayName=encodeURI(name) ;
		var urlstr = '/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=&popupWin=Popdiv&selectName='+popname+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName ;
		if(parentid == "searchEmployee"){
			//职员弹出框时要加部门过滤
			var deptsName= $("#searchDeptName").val();
			if(deptsName!=undefined && deptsName!=""){
				deptsName = encodeURI(encodeURI(deptsName));
				urlstr += "&tblDepartment_DeptFullName="+deptsName;
			}
		}else if(parentid == "searchDept"){
			$("#searchEmployee").val('');
			$("#searchEmployeeName").val('');
		}
		asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:750,height:470});
		parentsId = parentid;
		parentsName = parentName;
	}
	
	function exePopdiv(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var note = returnValue.split("#;#");
		if(parentsId == "searchEmployee"){
			//职员弹出框   
			$("#"+parentsId).val(note[0]);
			$("#"+parentsName).val(note[4]);
		}else{
			$("#"+parentsId).val(note[0]);
			$("#"+parentsName).val(note[2]);
		}
	}
	
	function onclear(){
		$("#highSearch input").val("");
	}
	
	function setOperation(){
		$("#operation").val('4');
		$("#optype").val('');
		$("#form").attr("target","");
	}
	function dealdatetr(tr_id){
	if(tr_id=="periodtr"){
		$("#periodtr").show();
		$("#datetr").hide();
	}else if(tr_id=="datetr"){
		$("#periodtr").hide();
		$("#datetr").show();
	}
}
	
</script>
<script type="text/javascript">
$(function(){
	$(".list_box").css("width",$(document.body).width());
	$(".data_list_head").css("width",$(".list_table").width());
	var tdLen = $(".list_table tbody tr:eq(0) td").size();
	for(var i =0;i<tdLen;i++)
	{
		$(".data_list_head thead tr td:eq("+i+")").attr("width",$(".list_table tbody tr:eq(0) td:eq("+i+")").outerWidth(true));
	}
});
</script>
</head>
 
<body >
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" id="form" name="form" action="/VoucherManageAction.do" target="">
<input type="hidden" id="operation" name="operation" value="4"/>
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value=""/>
<input type="hidden" id="classCode" name="classCode" value=""/>
<input type="hidden" id="ButtonType" name="ButtonType" value=""/>
<input type="hidden" id="tableName" name="tableName" value="tblAccMain"/>
<input type="hidden" id="varKeyIds" name="varKeyIds" id="varKeyIds" value=""/>
<input type="hidden" id="optype" name="optype" value=""/>
<input type="hidden" id="dealType" name="dealType" value=""/>
<input type="hidden" id="popWinName" name="popWinName" value="$!popWinName"/>

<input type="hidden" id="SQLSave" name="SQLSave" value='$!saveSql'/>
		<table cellpadding="0" cellspacing="0" border="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
							<div class="btnlp" id="closes" align="center"><img src="/style/images/email_delete.gif" title="关闭"/><br />关闭</div>
							#if($LoginBean.operationMap.get("/VoucherManageAction.do").print() || $!LoginBean.id=="1")
								 <div class="btnlp" id="prints" align="center"><img src="/style/themes/icons/print.png" title="打印列表"/><br />打印所有</div>  
								<div class="btnlp" id="printsBill" align="center"><img src="/style/themes/icons/print.png" title="打印凭证"/><br />打印凭证</div>
							#end
							<div class="btnlp" id="export" align="center"><img src="/style1/images/up.gif" title="导出凭证"/><br />导出</div>
							<div class="btnlp" id="import" align="center"><img src="/style1/images/down.gif" title="导入凭证"/><br />导入</div>
							<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
							#if("$!AccMainSetting.isAuditing"=="1")
								<!-- 过账权限 -->
								<!-- 反过账 -->
								#if($!globals.isExistInArray($!AccMainSetting.reverseBinder.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="reverseBinder" align="center">
									<img src="/style1/images/carefor/front.gif" width="16" height="16" title="反过账凭证"/><br />
									反过账  
								</div>
								#end
								#if($!globals.isExistInArray($!AccMainSetting.binderPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="binderGuide" align="center">
									<img src="/style1/images/oaimages/54.bmp" width="16" height="16" title="过账向导"/><br />
									过账向导
								</div>
								<div class="btnlp" id="batchbinder" align="center">
									<img src="/style1/images/carefor/up.gif" width="16" height="16" title="过账凭证"/><br />
									过账
								</div>
								<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
								#end
							#end
							#if("$!AccMainSetting.isCheck"=="1")
								<!-- 复核/反复核 -->
								#if($!globals.isExistInArray($!AccMainSetting.checkPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="reversecheck" align="center"><img src="/style/themes/icons/undo.png" title="批量反复核凭证"/><br />反复核</div>
								<div class="btnlp" id="check" align="center"><img src="/style/themes/icons/redo.png" title="批量复核凭证"/><br />复核</div>
								<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
								#end
							#end
							<!-- 启用审核1，不启用为0 -->
							#if("$!AccMainSetting.isAuditing"=="1")
								<!-- 反审核权限 -->
								#if($!globals.isExistInArray($!AccMainSetting.reverseAuditing.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="reversePass" align="center"><img src="/style1/images/refresh.gif" title="批量反审核凭证"/><br />反审核</div>
								#end
								<!-- 审核权限 -->
								#if($!globals.isExistInArray($!AccMainSetting.auditingPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="noPass" align="center"><img src="/style1/images/oaimages/noenable.png" title="批量审核不通过凭证"/><br />审核不通过</div>
								<div class="btnlp" id="AuditeGuide" align="center"><img src="/style1/images/oaimages/54.bmp" width="16" height="16" title="审核向导"/><br />审核向导</div>
								<div class="btnlp" id="yesPass" align="center"><img src="/style1/images/oaimages/enable.png" title="批量审核凭证"/><br />审核</div>
								<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
								#end
							#end
							#if($LoginBean.operationMap.get("/VoucherManageAction.do").delete() || $!LoginBean.id=="1")
							<div class="btnlp" id="batchdelete" align="center"><img src="/style/themes/icons/no.png" title="批量删除凭证"/><br />删除</div>
							#end
							#if($LoginBean.operationMap.get("/VoucherManageAction.do").add() || $!LoginBean.id=="1")
							<div class="btnlp" id="add" align="center"><img src="/style/plan/M_3.gif" title="添加凭证"/><br />添加</div>
							#end
							<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
							<div class="btnlp" id="refur" align="center"><img src="/style/themes/icons/reload.png" title="$text.get('oa.common.refresh')"/><br />$text.get('oa.common.refresh')</div>
							<div class="btnlp" id="querys" align="center"><img src="/style/themes/icons/search.png" title="过滤凭证"/><br />过滤</div>
							
							<!-- <div class="btnlp"><img src="/style/themes/icons/filesave.png" title="流量"/></div> -->
							<div class="searchDiv" style="float:left;padding-top: 10px;height:15px;">
								#if("$!AccMainSetting.isAuditing"=="1")
								<a style="float:none;margin:0 4px 20px 10px;" tabindex="0" href="#search-engines1" class="fg-button fg-button-icon-right ui-widget ui-state-active ui-corner-all a-width" id="auditinga" isSubmit="true"><span class="ui-icon ui-icon-triangle-1-s"></span>
								<label>#if("$!VoucherSearchForm.searchIsAuditing"=="")所有审核状态#elseif("$!VoucherSearchForm.searchIsAuditing"=="start")未审核#elseif("$!VoucherSearchForm.searchIsAuditing"=="finish")已审核#elseif("$!VoucherSearchForm.searchIsAuditing"=="noPass")审核不通过#end</label><input name="searchIsAuditing" id="searchIsAuditing" value="$!VoucherSearchForm.searchIsAuditing" type="hidden"/></a>
								<div id="search-engines1" class="hidden">
								<ul>
									<li><a href="#" value="">所有审核状态</a></li>
									<li><a href="#" value="start">未审核</a></li>
									<li><a href="#" value="finish">已审核</a></li>
									<li><a href="#" value="noPass">审核不通过</a></li>
								</ul>
								</div>
								#end
								#if("$!AccMainSetting.isCheck"=="1")
								<a style="float:none;margin:0 4px 20px 10px;" tabindex="0" href="#search-engines2" class="fg-button fg-button-icon-right ui-widget ui-state-active ui-corner-all a-width" id="reviewa" isSubmit="true"><span class="ui-icon ui-icon-triangle-1-s"></span>
								<label>#if("$!VoucherSearchForm.searchIsReview"=="")所有复核状态#elseif("$!VoucherSearchForm.searchIsReview"=="1")未复核#elseif("$!VoucherSearchForm.searchIsReview"=="2")已复核#end</label><input name="searchIsReview" id="searchIsReview" value="$!VoucherSearchForm.searchIsReview" type="hidden"/></a>
								<div id="search-engines2" class="hidden">
								<ul>
									<li><a href="#" value="">所有复核状态</a></li>
									<li><a href="#" value="1">未复核</a></li>
									<li><a href="#" value="2">已复核</a></li>
								</ul>
								</div>
								#end
								#if("$!AccMainSetting.isAuditing"=="1")
								<a style="float:none;margin:0 4px 20px 10px;" tabindex="0" href="#search-engines3" class="fg-button fg-button-icon-right ui-widget ui-state-active ui-corner-all a-width" id="wNamea" isSubmit="true"><span class="ui-icon ui-icon-triangle-1-s"></span>
								<label>#if("$!VoucherSearchForm.searchwName"=="")所有过账状态#elseif("$!VoucherSearchForm.searchwName"=="notApprove")未过账#elseif("$!VoucherSearchForm.searchwName"=="finish")已过账#end</label><input name="searchwName" id="searchwName" value="$!VoucherSearchForm.searchwName" type="hidden"/></a>
								<div id="search-engines3" class="hidden">
								<ul>
									<li><a href="#" value="">所有过账状态</a></li>
									<li><a href="#" value="notApprove">未过账</a></li>
									<li><a href="#" value="finish">已过账</a></li>
								</ul>
								</div>
								#end
							</div>
					</div>
					
					<div class="list_box" style="overflow-x:auto;width:100%;">
					<table class="data_list_head list_head" cellpadding="0" cellspacing="0" >
						<thead>
							<tr>
								<td class="col1">No.</td>
								<td class="col2"><input type="checkbox" id="selAll" onClick="checkAll('keyId')"/></td>
								#if("$!AccMainSetting.isAuditing"=="1")
								<td class="col3">审核</td>
								#end
								#if("$!AccMainSetting.isCheck"=="1")
								<td class="col4">复核</td>
								#end
								#if("$!AccMainSetting.isAuditing"=="1")
								<td class="col5">过账</td>
								#end
								<td class="col6">现金流量</td>
								<td class="col7">日期</td>
								<td class="col8">会计期间</td>
								<td class="col9">凭证字号</td>
								<td class="col10">摘要</td>
								<td class="col11">科目代码</td>
								<td class="col12">科目名称</td>
								#if($!globals.getSysSetting('currency')=="true")
								<td class="col13">币别</td>
								<td class="col14">原币金额</td>
								#end
								<td class="col15">借方</td>
								<td class="col16">贷方</td>
								<td class="col17">关联单据</td>
								<td class="col18">经手人</td>
								<td class="col19">制单人</td>
								<td class="col20">操作</td>
								
							</tr>
						</thead>
					</table>
					<div id="data_list_id" class="list_div" style="overflow-y:auto;width:100%;" >
					<script type="text/javascript"> 
						var oDiv=document.getElementById("data_list_id");
						var sHeight=document.documentElement.clientHeight-125;
						oDiv.style.height=sHeight+"px";
					</script>
						<table class="list_table" cellpadding="0" cellspacing="0" border="0">					
							<tbody>
								#set($accmainid="")
								#set($sumdebitAmount=0)
								#set($sumlendAmount=0)
								#set($sum=0)
								#set($sums=0)
								#foreach($!accmain in $!AccMainList)
									<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);" id="tr_$!accmain.id" class="context-menu-one" ondblclick="details('$!accmain.id','$!accmain.counts')">
									<input name="counts" id="counts" type="hidden" value="$!accmain.counts"/>
									<td class="col1" width="25">$!accmain.counts</td>
									#if("$accmainid" != "$!accmain.id")
										<td class="col2" width="25" align="center"><input type="checkbox" id="keyId" name="keyId" value="$!accmain.id"/></td>
										#if("$!AccMainSetting.isAuditing"=="1")
										<td class="col3" width="25">#if($!accmain.isAuditing=="noPass")×#elseif($!accmain.isAuditing=="start")#elseif($!accmain.isAuditing=="finish")√ #end</td>
										#end
										#if("$!AccMainSetting.isCheck"=="1")
										<td class="col4" width="25">#if($!accmain.isReview=="1")#elseif($!accmain.isReview=="2")√ #end</td>
										#end
										#if("$!AccMainSetting.isAuditing"=="1")
										<td class="col5" width="25">#if($!accmain.workflowNodeName=="notApprove"&&$!accmain.workFlowNode=="0") #elseif($!accmain.workflowNodeName=="finish"||$!accmain.workFlowNode=="-1")√ #end</td>
										#end
										<td class="col6" width="45" title="现金流量指定">$!accmain.CashFlag</td>
										<td class="col7" width="60">$!accmain.BillDate</td>
										<td class="col8" width="50">$!{accmain.CredYear}.$!{accmain.CredMonth}</td>
										<td class="col9" width="50">$!accmain.CredTypeID - $!accmain.OrderNo</td>
									#else
										<td class="col2" width="25"><input type="checkbox" id="keyId" name="keyId" value="$!accmain.id" style="display: none;"/></td>
										#if("$!AccMainSetting.isAuditing"=="1")
										<td class="col3" width="25">&nbsp;</td>
										#end
										#if("$!AccMainSetting.isCheck"=="1")
										<td class="col4" width="25">&nbsp;</td>
										#end
										#if("$!AccMainSetting.isAuditing"=="1")
										<td class="col5" width="25">&nbsp;</td>
										#end
										<td class="col6" width="35"></td>
										<td class="col7" width="60">&nbsp;</td>
										<td class="col8" width="50">&nbsp;</td>
										<td class="col9" width="50">&nbsp;</td>
									#end
										#set($debitAmount="")
										#set($lendAmount="")
										#if("$accmain.DebitAmount" == "0E-8")
											#set($debitAmount = "")
										#else
											#set($debitAmount = $accmain.DebitAmount)
										#end
										#if("$accmain.LendAmount" == "0E-8")
											#set($lendAmount = "")
										#else
											#set($lendAmount = $accmain.LendAmount)
										#end
										#set($debitCurrencyAmount="")
										#set($lendCurrencyAmount="")
										#if("$accmain.DebitCurrencyAmount" == "0E-8")
											#set($debitCurrencyAmount = "")
										#else
											#set($debitCurrencyAmount = $accmain.DebitCurrencyAmount)
										#end
										#if("$accmain.LendCurrencyAmount" == "0E-8")
											#set($lendCurrencyAmount = "")
										#else
											#set($lendCurrencyAmount = $accmain.LendCurrencyAmount)
										#end
										<td class="col10" width="70" title="$!accmain.RecordComment">$!accmain.RecordComment</td>
										<td class="col11" width="100" title="$!accmain.AccCode">$!accmain.AccCode</td>
										<td class="col12" width="80" title="$accmain.AccName">
										#if($!globals.getSysSetting('currency')=="true")
											$accmain.AccName
										#else
											$accmain.AccName
										#end
										</td>
										#set($debitAmount = $!globals.newFormatNumber($debitAmount,false,false,$!globals.getSysIntswitch(),'tblAccDetail','debitAmount',true))
										#set($lendAmount = $!globals.newFormatNumber($lendAmount,false,false,$!globals.getSysIntswitch(),'tblAccDetail','lendAmount',true))
										#if($!globals.getSysSetting('currency')=="true")
										#set($debitCurrencyAmount = $!globals.newFormatNumber($debitCurrencyAmount,false,false,$!globals.getSysIntswitch(),'tblAccDetail','debitCurrencyAmount',true))
										#set($lendCurrencyAmount = $!globals.newFormatNumber($lendCurrencyAmount,false,false,$!globals.getSysIntswitch(),'tblAccDetail','lendCurrencyAmount',true))
										<td class="col13" width="50">$accmain.Currencyname</td>
										<td class="col14" width="60" align="right" style="padding-right: 3px;" title='$debitAmount$lendAmount' class="td_title">
											$debitCurrencyAmount$lendCurrencyAmount
										</td>
										#end
										<td class="col15" width="60" align="right" style="padding-right: 3px;" title='$debitAmount' class="td_title">$debitAmount</td>
										<td class="col16" width="60" align="right" style="padding-right: 3px;" title='$lendAmount' class="td_title">$lendAmount</td>
										#if("$accmainid" != "$!accmain.id")
										<td class="col17" width="100"><a href="javascript:showBillNo('$!accmain.RefBillID','$!accmain.RefBillType')" style="color: blue;" title="$!accmain.RefBillTypeName">#if("$!accmain.RefBillNo"=="settleAcc" || "$!accmain.RefBillNo"=="adjustExchange")&nbsp;#else$!accmain.RefBillNo#end</a></td>
										<td class="col18" width="40">
										#set($employee = $globals.getEmpNameById($!accmain.EmployeeID))
										#if($employee=="Other")
											$!accmain.EmployeeID
										#else
											$employee
										#end
										</td>
										<td class="col19" width="40">
										#set($createBy = $globals.getEmpNameById($!accmain.createBy))
										#if($employee=="Other")
											$!accmain.createBy
										#else
											$createBy
										#end
										</td>
										#else
											<td class="col13" width="60">&nbsp;</td>
											<td class="col14" width="50">&nbsp;</td>
										#end
										#if("$accmainid"!="$!accmain.id")
										<td class="col20" width="60" align="center">
										#if($LoginBean.operationMap.get("/VoucherManageAction.do").update() || $!LoginBean.id=="1")
											<a href="javascript:void(0)" onclick="updates('$!accmain.id','$!accmain.counts')"><img src="/style1/images/oaimages/ni_2.gif"  style="margin-top: 5px;" title="修改"/></a>
										#end
											<a href="javascript:void(0)" onclick="details('$!accmain.id','$!accmain.counts')"><img src="/style1/images/oaimages/ni_1.gif"  style="margin-top: 5px;" title="详情"/></a>
										#if($LoginBean.operationMap.get("/VoucherManageAction.do").delete() || $!LoginBean.id=="1")
											<a href="javascript:void(0)" onclick="deletes('$!accmain.id')"><img src="/style1/images/oaimages/l-i-03.gif"  style="margin-top: 5px;" title="删除"/></a>
										#end
										</td>
										#else
											<td class="col20" width="50">&nbsp;</td>
										#end
										
										#set($accmainid="$!accmain.id")
									</tr>
									#set($count=$count+1)
									#if("$debitCurrencyAmount"=="")
										#set($debitCurrencyAmount=0)
									#end
									#if("$lendCurrencyAmount"=="")
										#set($lendCurrencyAmount=0)
									#end
									#set($sum = $math.add("$debitCurrencyAmount","$lendCurrencyAmount"))
									#set($sums = $math.add("$sums","$sum"))
									#set($sumdebitAmount = $math.add("$sumdebitAmount","$debitAmount"))
									#set($sumlendAmount = $math.add("$sumlendAmount","$lendAmount"))
								#end
								#if($AccMainList.size()!=0)
								<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
									#foreach ( $foo in [1..18] )
										#if($foo==6)
											<td>总合计</td>
										#elseif($foo>=12 && $foo<=13)
											#if($!globals.getSysSetting('currency')=="true")
												#if($foo == 13)
													<!-- <td class="td_title" align="right" style="padding-right: 3px;" title='$!globals.newFormatNumber($sums,false,false,$!globals.getSysIntswitch(),'tblAccDetail','debitAmount',true)'>$!globals.getSubStrDigit($!globals.newFormatNumber($sums,false,false,$!globals.getSysIntswitch(),'tblAccDetail','debitAmount',true),'3')</td> -->
													<td>&nbsp;</td>
												#else
													<td>&nbsp;</td>
												#end
											#end
										#elseif($foo==14)
											<td class="td_title" align="right" style="padding-right: 3px;" title='$!globals.newFormatNumber($sumdebitAmount,false,false,$!globals.getSysIntswitch(),'tblAccDetail','debitAmount',true)'>$!globals.getSubStrDigit($!globals.newFormatNumber($sumdebitAmount,false,false,$!globals.getSysIntswitch(),'tblAccDetail','debitAmount',true),'3')</td>
										#elseif($foo==15)
											<td class="td_title" align="right" style="padding-right: 3px;" title='$!globals.newFormatNumber($sumlendAmount,false,false,$!globals.getSysIntswitch(),'tblAccDetail','lendAmount',true)'>$!globals.getSubStrDigit($!globals.newFormatNumber($sumlendAmount,false,false,$!globals.getSysIntswitch(),'tblAccDetail','lendAmount',true),'3')</td>
										#elseif($foo==4)
											#if("$!AccMainSetting.isCheck"=="1")
												<td>&nbsp;</td>
											#end
										#elseif($foo==3 || $foo==5)
											#if("$!AccMainSetting.isAuditing"=="1")
												<td>&nbsp;</td>
											#end
										#else
											<td>&nbsp;</td>
										#end
										
									#end
								</tr>
								#end
							</tbody>		
						</table>
						#if($AccMainList.size()==0)
						<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">
						#if("$!VoucherSearchForm.searchCredYearStart"!="" || "$!VoucherSearchForm.searchCredMonthStart"!="" || 
							"$!VoucherSearchForm.searchCredYearEnd"!="" || "$!VoucherSearchForm.searchCredMonthEnd"!="" || 
							"$!VoucherSearchForm.searchStartTime"!="" || "$!VoucherSearchForm.searchEndTime"!="" || "$!VoucherSearchForm.searchCredType"!="" ||
							"$!VoucherSearchForm.searchOrderNoStart"!="" || "$!VoucherSearchForm.searchOrderNoEnd"!="" || "$!VoucherSearchForm.searchAccCodeStart"!="" ||
							"$!VoucherSearchForm.searchAccCodeEnd"!="" || "$!VoucherSearchForm.searchwName"!="" ||
							 "$!VoucherSearchForm.searchIsReview"!="" || "$!VoucherSearchForm.searchIsAuditing"!="" || "$!VoucherSearchForm.searchRefBillNo"!=""
							 || "$!VoucherSearchForm.searchDept"!="" || "$!VoucherSearchForm.searchEmployee"!="" || "$!VoucherSearchForm.searchClient"!=""||
							 "$!VoucherSearchForm.searchDeptName"!="" || "$!VoucherSearchForm.searchEmployeeName"!=""||"$!VoucherSearchForm.searchClientName"!="")
							根据过滤条件，暂无会计凭证信息  

						#else
							暂无会计凭证信息
						#end
						</div>
						#end
					</div>
					</div>
					<div class="listRange_pagebar">
						$!pageBar
					</div>	
			</td>
			</tr>
			</table>
			
			<!-- 条件查询 -->
			<div id="highSearch" class="search_popup" style="display:none;top: 250px;left: 650px;">
			<script type="text/javascript">
				#if("$!AccMainSetting.isAuditing"=="1")
					$("#highSearch").css("height","440px");
				#else
					$("#highSearch").css("height","400px");
				#end
			</script>
			<input type="hidden" name="queryType" id="queryType" value=""/>
			<div id="Divtitle" style="cursor: move;width:98%">
				<span class="ico_4"></span>&nbsp;条件过滤
			</div>
				<table style="width:100%;padding-top: 5px;" id="tableSearch">
					<tr>
						<td><div><table>
							<tr><td class="tdDes1">&nbsp;&nbsp;&nbsp;<input name="dateType" id="dateType1" type="radio" style="border: 0px;" value="1"  onclick="dealdatetr('periodtr')"/></td><td colspan="3"><label for="dateType1">按期间查询</label> 
								&nbsp;&nbsp;&nbsp;<input name="dateType" id="dateType2" type="radio" style="border: 0px;" value="2"  onclick="dealdatetr('datetr')"/><label for="dateType2">按日期查询</label></td>
							</tr>
							<tr id="periodtr"><td class="tdDes1">&nbsp;会计期间段:</td><td><input name="searchCredYearStart" id="searchCredYearStart" value="$!VoucherSearchForm.searchCredYearStart" size="5"/>&nbsp;年    
								<input name="searchCredMonthStart" id="searchCredMonthStart" value="$!VoucherSearchForm.searchCredMonthStart" size="5"/>&nbsp;期 $!VoucherSearchForm.searchCredMonthStart</td>
							<td class="tdDes1">&nbsp;&nbsp;&nbsp;至:</td><td><input name="searchCredYearEnd" id="searchCredYearEnd" value="$!VoucherSearchForm.searchCredYearEnd" size="5"/>&nbsp;年   
								<input name="searchCredMonthEnd" id="searchCredMonthEnd" value="$!VoucherSearchForm.searchCredMonthEnd" size="5"/>&nbsp;期</td>
							</tr>
							<tr id="datetr"><td class="tdDes1">日期段:</td><td class="tdDes3"><input name="searchStartTime" size="13"
								value="$!VoucherSearchForm.searchStartTime" 
								onKeyDown="if(event.keyCode==13) subForm();"
								onclick="openInputDate(this);" /></td><td class="tdDes1">至:</td><td class="tdDes3">
							<input name="searchEndTime" size="13" value="$!VoucherSearchForm.searchEndTime"
								onKeyDown="if(event.keyCode==13) subForm();"
								onclick="openInputDate(this);" /></td></tr>
							<tr>
								<td class="tdDes1">凭证字:</td><td class="tdDes3" colspan="3" style="height:15px;">
								<select name="searchCredType" id="searchCredType" style="width: 110px;">
									<option value=""></option>
									#foreach($erow in $globals.getEnumerationItems('CredTypeID'))
										<option title="$erow.name" value="$erow.value" #if("$!VoucherSearchForm.searchCredType"=="$erow.value") selected #end>$erow.name</option>
									#end
								</select>
								</td>
							</tr>
							<tr><td class="tdDes1">凭证号:</td><td><input name="searchOrderNoStart" id="searchOrderNoStart" value="$!VoucherSearchForm.searchOrderNoStart" size="13"/>
							</td><td class="tdDes1">至:</td><td><input name="searchOrderNoEnd" id="searchOrderNoEnd" value="$!VoucherSearchForm.searchOrderNoEnd" size="13"/></td></tr>
							<tr><td class="tdDes1">科目代码:</td><td><input name="searchAccCodeStart" id="searchAccCodeStart" value="$!VoucherSearchForm.searchAccCodeStart" size="13" ondblclick="selectCode('searchAccCodeStart')"/>
							<img src='/style1/images/St.gif' onclick="selectCode('searchAccCodeStart')" class='search' />
							</td><td class="tdDes1">至:</td><td>
							<input name="searchAccCodeEnd" id="searchAccCodeEnd" value="$!VoucherSearchForm.searchAccCodeEnd" size="13" ondblclick="selectCode('searchAccCodeEnd')" />
							<img src='/style1/images/St.gif' onclick="selectCode('searchAccCodeEnd')" class='search'/></td>
							</tr>
							<tr><td class="tdDes1">金额:</td><td><input name="searchMoneyStart" id="searchMoneyStart" value="$!VoucherSearchForm.searchMoneyStart" size="13" />
							</td><td class="tdDes1">至:</td><td>
							<input name="searchMoneyEnd" id="searchMoneyEnd" value="$!VoucherSearchForm.searchMoneyEnd" size="13"  /></td>
							</tr>
							<tr>
							#if("$!AccMainSetting.isAuditing"=="1")
							<td class="tdDes1">审核:</td><td><select name="searchIsAuditing" id="searchIsAuditing" style="width: 110px;">
								<option value="" #if("$!VoucherSearchForm.searchIsAuditing"=="") selected #end>所有审核状态</option>
								<option value="start" #if("$!VoucherSearchForm.searchIsAuditing"=="start") selected #end>未审核</option>
								<option value="finish" #if("$!VoucherSearchForm.searchIsAuditing"=="finish") selected #end>已审核</option>
								<option value="noPass" #if("$!VoucherSearchForm.searchIsAuditing"=="noPass") selected #end>审核不通过</option>
							</select></td>#end
							#if("$!AccMainSetting.isCheck"=="1")
							<td class="tdDes1">&nbsp;&nbsp;&nbsp;&nbsp;复核:</td><td><select name="searchIsReview" id="searchIsReview" style="width: 110px;">
									<option value="" #if("$!VoucherSearchForm.searchIsReview"=="")selected #end>所有复核状态</option>
									<option value="1" #if("$!VoucherSearchForm.searchIsReview"=="1") selected #end>未复核</option>
									<option value="2" #if("$!VoucherSearchForm.searchIsReview"=="2") selected #end>已复核</option>
								</select></td>#end</tr>
							<tr>
								#if("$!AccMainSetting.isAuditing"=="1")
								<td class="tdDes1">过账:</td><td colspan="1">
								<select name="searchwName" id="searchwName" style="width: 110px;">
									<option value="" #if("$!VoucherSearchForm.searchwName"=="") selected #end>所有过账状态</option>
									<option value="notApprove" #if("$!VoucherSearchForm.searchwName"=="notApprove") selected #end>未过账</option>
									<option value="finish" #if("$!VoucherSearchForm.searchwName"=="finish") selected #end>已过账</option>
								</select>
								</td>#end
								<td class="tdDes1">凭证类型：</td>
								<td>
								<select name="searchAccType">
									<option value="">全部</option>
									<option value="ref"   #if("$!VoucherSearchForm.searchAccType"=="ref") selected #end>关联单据</option>
									<option value="manual"#if("$!VoucherSearchForm.searchAccType"=="manual") selected #end>手工创建</option>
								</select>
								</td>
							</tr>
						</table></div><td>
					</tr>
					<tr>
						<td><div><table><tr><td class="tdDes1">部门:</td><td>
						<input type="hidden" name="searchDept" id="searchDept" value="$!VoucherSearchForm.searchDept" />
						<input name="searchDeptName" id="searchDeptName" value="$!VoucherSearchForm.searchDeptName" size="13" ondblclick="selectPops('searchDept','searchDeptName','SelectAccDepartment','选择部门')"/>
							<img src='/style1/images/St.gif' onclick="selectPops('searchDept','searchDeptName','SelectAccDepartment','选择部门')" class='search'/></td>
							<td class="tdDes1">&nbsp;经手人:</td><td>
							<input type="hidden" name="searchEmployee" id="searchEmployee" value="$!VoucherSearchForm.searchEmployee" />
							<input name="searchEmployeeName" id="searchEmployeeName" value="$!VoucherSearchForm.searchEmployeeName" size="13" ondblclick="selectPops('searchEmployee','searchEmployeeName','SelectAccEmployee','选择经手人')"/>
							<img src='/style1/images/St.gif' onclick="selectPops('searchEmployee','searchEmployeeName','SelectAccEmployee','选择经手人')" class='search'/></td>
							</tr>
							<tr>
								<td class="tdDes1">&nbsp;制单人:</td><td colspan="3">
								<input type="hidden" name="createBy" id="createBy" value="$!VoucherSearchForm.createBy" />
								<input name="tblEmployee_EmpFullName" id="tblEmployee_EmpFullName" value="$!VoucherSearchForm.tblEmployee_EmpFullName" size="13" ondblclick="selectPops('createBy','tblEmployee_EmpFullName','SelectCreateEmployee','选择制单人')"/>
								<img src='/style1/images/St.gif' onclick="selectPops('createBy','tblEmployee_EmpFullName','SelectCreateEmployee','选择制单人')" class='search'/></td>
								</td> 
							</tr>
							<tr>
								<td class="tdDes1">&nbsp;关联单据号:</td><td><input name="searchRefBillNo" id="searchRefBillNo" size="13" value="$!VoucherSearchForm.searchRefBillNo"/></td>
								<td class="tdDes1">单据类型:</td><td><input name="searchRefBillType" id=searchRefBillType value="$!VoucherSearchForm.searchRefBillType" size="13" /></td>
							</tr>
							<tr>
								<td class="tdDes1">往来单位:</td><td><input name="searchClient" id="searchClient" type="hidden" value="$!VoucherSearchForm.searchClient"/>
								<input name="searchClientName" id="searchClientName" size="13" value="$!VoucherSearchForm.searchClientName" ondblclick="selectPops('searchClient','searchClientName','SelectAccExpensed','选择往来单位')"/>
								<img src='/style1/images/St.gif' onclick="selectPops('searchClient','searchClientName','SelectAccExpensed','选择往来单位')" class='search'/></td>
								<td class="tdDes1">摘要:</td><td><input name="searchRecordComment" id="searchRecordComment" value="$!VoucherSearchForm.searchRecordComment" size="13" /></td>
							</tr>
							</table>
							</div>
						</td>
					</tr> 
				</table>
				<span class="search_popup_bu"><input type="button" 
						onclick="onsubmits();" class="bu_1"  />
					<input type="button" class="" style="background: url(/style1/images/oaimages/QK.gif);cursor: pointer;width: 48px;margin: 0px 5px;border: #b4b4b4 1px solid;color: #FFFFFF;height:19px;" value="" onclick="onclear()"/>
					<input type="button" onclick="closeSearch();" class="bu_2" />
				</span>
		</div>
			<script language="javascript">
			if(document.getElementsByName("searchStartTime")[0].value.length>0||document.getElementsByName("searchEndTime")[0].value.length>0){
				$("#periodtr").hide();
				$("#datetr").show();
				document.getElementById("dateType2").checked=true;
			}else{
				$("#datetr").hide();
				$("#periodtr").show();
				document.getElementById("dateType1").checked=true;
				
				if(document.getElementsByName("searchCredYearStart")[0].value.length==0&&
					document.getElementsByName("searchCredYearEnd")[0].value.length==0&&
					document.getElementsByName("searchCredMonthStart")[0].value.length==0&&
					document.getElementsByName("searchCredMonthEnd")[0].value.length==0){
					
					//document.getElementsByName("searchCredYearStart")[0].value="$!accPeriodBean.AccYear";
					//document.getElementsByName("searchCredYearEnd")[0].value="$!accPeriodBean.AccYear";
					//document.getElementsByName("searchCredMonthStart")[0].value="$!accPeriodBean.AccMonth";
					//document.getElementsByName("searchCredMonthEnd")[0].value="$!accPeriodBean.AccMonth";
				}
				
			}
			
			
			var posX;
			var posY;       
			fdiv = document.getElementById("highSearch");         
			document.getElementById("Divtitle").onmousedown=function(e){
				if(!e) e = window.event;  //IE
			    posX = e.clientX - parseInt(fdiv.style.left);
			    posY = e.clientY - parseInt(fdiv.style.top);
			    document.onmousemove = mousemove;           
			}
 
			document.onmouseup = function(){
			    document.onmousemove = null;
			}
			function mousemove(ev){
			    if(ev==null) ev = window.event;//IE
			    fdiv.style.left = (ev.clientX - posX) + "px";
			    fdiv.style.top = (ev.clientY - posY) + "px";
			}
			</script>
			<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>			
		</form>
	</body>
	</html>