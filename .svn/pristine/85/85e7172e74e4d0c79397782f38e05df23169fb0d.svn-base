<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>扫描出库</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.divbox.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.body.clientHeight-18);
	$(document.body).click(function(){
		if($("#batchInputPopup").size()>0){
			$('#batchText').focus();
		}else{
			$('#scanId').focus();
		}
	});
	$('#scanId').blur( function() {
		if($("#batchInputPopup",document).size()>0){
			$('#batchText',document).focus();
		}else{
			$('#scanId',document).focus();
		}
	} ); 

});

//会话失效后重新登陆的回调函数
var needRefresh = false;
var loginSave=false;
function reloginToUrl(){
	if(needRefresh){
		needRefresh = false;
		window.location.href=window.location.href;
	}
	if(loginSave){
		loginSave = false;
		save();
	}
}

function getSalesOut(outNo){
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/hf/HFApi.jsp",{op:'getBill',BillNo:outNo},function(json){ 
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
		if(json.code == "ERROR"){
			alert(json.msg);
		}else if(json.code == "NOLOGIN"){
			top.reLogin(window);
		}else{
			$("h4").remove();
			$("#SalesOutId").val(json.id);
			$("#OutInfoDiv").empty();
			$("#OutInfoDiv").append("\
			<div>单号："+json.BillNo+"</div>\
			<div>日期："+json.BillDate+"</div>\
			<div>客户编号："+json.ComNumber+"</div>\
			<div>客户名称："+json.ComFullName+"</div>");
			$("#outTable tbody").empty();
			for(var i=0;i<json.Dets.length;i++){
				var cdata = json.Dets[i];
				var must = cdata['GoodsCode'].indexOf("00001")==0?"yes":"no";
				$("#outTable tbody").append("<tr must='"+(must )+"'  detId='"+cdata['id']+"'  no='"+i+"' GoodsCode='"+cdata['GoodsCode']+"'>\
					<td>"+cdata['GoodsNumber']+"</td>\
					<td name='Qty'>"+cdata['Qty']+"</td>\
					<td name='ScanQty' "+(cdata['ScanQty']==cdata['Qty']?"style=background-color:#D68544":"")+">"+(must=="yes"?cdata['ScanQty']:"" )+"</td>\
					<td name='NoScanQty' "+(cdata['ScanQty']==cdata['Qty']?"style=background-color:#D68544":"")+">"+(must=="yes"?(cdata['NoScanQty']):"" )+"</td></tr>");
			}
			
			for(var i=0;i<json.Seqs.length;i++){
				var cdata = json.Seqs[i];
				var yearNo = cdata.yearNo;
				var seq = cdata.Seq;
				$("#outSeqDivFirst").after("<h4 id=h"+seq+">"+yearNo+"|"+seq+"</h4>");
			}
			softList();
			
			//$(".curObj").removeClass("curObj");
			//$("#OutInfoDiv").addClass("curObj");
			
		}
	},"json" ); 
}

function softList(){
	obj = $("tr[must=yes]");
	for(var i=obj.size()-1;i>=0;i--){
		if($(obj[i]).find("td[name=ScanQty]").text()==
			$(obj[i]).find("td[name=Qty]").text()&& $("tr[must=yes]:last")[0]!=obj[i]){
			$("tr[must=yes]:last").after($(obj[i]));
		}
	}
}

var values=new Array();
var allSeq = new Array(); //用于重复性校验

function scanChange(){ 
	var val = $("#scanId").val();
	if(val.length==0){
		return;
	}
	if(val.indexOf("`") > 0){
		val = val.substring(val.indexOf("`"));
	}
	if($("#UserId").val()==""){
		needRefresh = true;
		top.reLogin(window);
		return;
	}
	if(val.toUpperCase().indexOf("CK")==0){//以SS开头取销售出库信息
		values=new Array();
		allSeq = new Array();
		getSalesOut(val);
	}else {
		if($("#SalesOutId").val()==""){
			$("#scanId").val("");
			alert("请先输入销售出库单号");
			return;
		}
		if(typeof(top.jblockUI)!="undefined" ){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		jQuery.ajaxSetup({  
		    async : false  
		});   
		jQuery.post("/hf/HFApi.jsp",{op:'GetSeq',SalesOutId:$("#SalesOutId").val(),Seq:val},function(json){ 
			if(typeof(top.jblockUI)!="undefined" ){
					top.junblockUI()
			}
			if(json.code == "ERROR"){
				alert(json.msg);
			}else if(json.code == "NOLOGIN"){
				top.reLogin(window);
			}else{
				var list = json.Lists;
				for(var i =0;i<list.length;i++){
					$("tr[detId="+list[i].i+"]").find("td[name=ScanQty]").text(parseInt(list[i].q));
					$("tr[detId="+list[i].i+"]").find("td[name=NoScanQty]").text(
							Number($("tr[detId="+list[i].i+"]").find("td[name=Qty]").text())-parseInt(list[i].q));
					if($("tr[detId="+list[i].i+"]").find("td[name=ScanQty]").text()==
						$("tr[detId="+list[i].i+"]").find("td[name=Qty]").text()){
						$("tr[detId="+list[i].i+"]").find("td[name=ScanQty]").css("background-color","#D68544");
						$("tr[detId="+list[i].i+"]").find("td[name=NoScanQty]").css("background-color","#D68544");
					}else{
						$("tr[detId="+list[i].i+"]").find("td[name=ScanQty]").css("background-color","");
						$("tr[detId="+list[i].i+"]").find("td[name=NoScanQty]").css("background-color","");
					}
				}
				softList();
				
				var seqs = json.Seqs;
				for(var i =0;i<seqs.length;i++){
					var yearNo = seqs[i].y;
					var seq = seqs[i].s;
					if(seq.indexOf("-")==0){
						$("#h"+seq.substring(1)).remove();
					}else{
						$("#outSeqDivFirst").after("<h4 id=h"+seq+">"+yearNo+"|"+seq+"</h4>");
					}
				}
				
				//检查是否出库完成。
				/*
				var allFull=true;
				var trObj = $("tr[must=yes]").each(function(){
					var scanNum = Number($(this).find("td[name=scanNum]").text());
					var NotOutQty = Number($(this).find("td[name=NotOutQty]").text());
					if(scanNum<NotOutQty){
						allFull=false;
					}
				});
				if(allFull){
					//弹出窗保存确认框
					
				　	asyncbox.confirm('本合同扫描完成，注意礼品无需扫描请同时发货，<br/>确认发货请按确定按扭','出库确认',function(action){
					　　　//confirm 返回三个 action 值，分别是 'ok'、'cancel' 和 'close'。
					　　　if(action == 'ok'){
					　　　　　save();
					　　　}else{
							setTimeout(function(){noSave();}, 500);
					　　　}
					});
				} */
				
			}
			
		},"json" ); 
		jQuery.ajaxSetup({  
		    async : true  
		});
	}
	$("#scanId").val("");
}
function save(){
	
	var olddetList = values.TABLENAME_tblSalesOrderDet;
	var detList = new Array();
	for(var i = 0; i<olddetList.length;i++){
		var oldLine = olddetList[i];
		if(oldLine.seqList==undefined){
			//这是礼品，直接出库
			oldLine['StockCode']='00001';
			oldLine.Seq=seq;
			oldLine['SalesOutID']=values.id+'';
			oldLine['SourceID']=oldLine.id+'';
			//detList[detList.length] = oldLine; //暂时不出库
		}else{//这是有序列号的成品，根据箱号分组
			var yearList = new Array();
			for(var j=0;j<oldLine.seqList.length;j++){
				var found = false;
				for(var k=0;k<yearList.length;k++){
					if(yearList[k][0].yearNo == oldLine.seqList[j].yearNo && yearList[k][0].batchNo == oldLine.seqList[j].batchNo ){
						yearList[k][yearList[k].length]=oldLine.seqList[j];
						found = true;
					}
				}
				if(!found){
					var oa = new Array();
					yearList[yearList.length]=oa;
					oa[0]=oldLine.seqList[j];
				}
			}
			//根据分组设置明细值
			for(var j=0;j<yearList.length;j++){
				var seq="";
				for(var k=0;k<yearList[j].length;k++){
					seq+=yearList[j][k].Seq+"~";
				}
				var newLine = new Object();
				for(var k in oldLine){  
                    newLine[k]=oldLine[k];
                }  
				newLine.BatchNo=yearList[j][0].batchNo;
				newLine.yearNO=yearList[j][0].yearNo;
				newLine.Qty=yearList[j].length;
				newLine.Amount=Number(newLine.Qty)*newLine.Price;
				newLine.DisBackAmt=Number(newLine.Qty)*newLine.DisPrice;
				newLine.TaxAmount=Number(newLine.Qty)*newLine.TaxPrice;
				newLine.CoTaxAmt=Number(newLine.Qty)*(newLine.TaxPrice-newLine.Price);
				newLine.CurAmount=Number(newLine.Qty)*newLine.CurPrice;
				newLine.NotPayQty=newLine.Qty;
				newLine.InvoiceQty=newLine.Qty;
				newLine.NeedReceiveAmt=Number(newLine.Qty)*newLine.TaxPrice;
				
				newLine.StockCode=yearList[j][0].StockCode;
				newLine.Seq=seq;
				newLine.SalesOutID=values.id+'';
				newLine.SourceID=oldLine.id+'';
				newLine.seqList=[];
				detList[detList.length]=newLine;
			}
		}
	}
	values.SalesOutID=values.id+''
	values.SalesOrderNo=values.BillNo+'';
	values.BillNo='';
	values.CertificateNo=''; 
	values.DepartmentCode='$!LoginBean.departCode';
	values.EmployeeID='$!LoginBean.id';
	values.BillDate=''; //清空单据日期
	values.id=''; //清空id
	values.TABLENAME_tblSalesOrderDet=[];
	values.TABLENAME_tblSalesOutStockDet=new Array();
	values.TABLENAME_tblSalesOutStockDet=detList;
	
	values.TotalAmount=0;
	values.TotalTaxAmount=0;
	values.TotalCoTaxAmt=0;
	values.CurTotalAmount=0;
	values.NeedReturnAmt=0;
	values.CurNeedReturnAmt=0;
	for(var j=0;j<detList.length;j++){
		var newLine = detList[j];
		values.TotalAmount +=newLine.Amount;
		values.TotalCoTaxAmt +=newLine.CoTaxAmt;
		values.CurTotalAmount +=newLine.CurAmount;
		values.TotalTaxAmount +=newLine.TaxAmount;
		values.NeedReturnAmt +=newLine.TaxAmount;
		values.CurNeedReturnAmt +=newLine.CurTotalAmount;
	}
	var date= new Date();
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	values.BillDate=year+'-'+(month<10?'0':'')+month +'-'+(day<10?'0':'')+day; 
	
	var postdata = JSON.stringify(values); //生成所有需提交数据的json字符串
	addAjax('tblSalesOutStock','','',postdata,'');
}
var updatedefineInfo="";
function addAjax(tableName,parentTableName,saveType,valuesStr,defineInfo){
	url = "/MobileAjax?op=add";
	if(defineInfo == undefined || defineInfo == ""){
		updatedefineInfo = "";
		defineInfo = "";
	}else{
		updatedefineInfo += defineInfo;
	}
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post(url,{tableName:tableName,parentTableName:parentTableName,saveType:saveType,values:valuesStr,defineInfo:updatedefineInfo},function(data){ 		
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
		if(data.code=="CONFIRM"){
		    msg = data.msg.split("#;#");
		    if(confirm(msg[0].replace(/\\n/g,'<br/>'))){
		    	if(msg[1] != ""){
					defineInfo = msg[1];				
					addAjax(tableName,parentTableName,saveType,isCheck,valuesStr,defineInfo);
				}
		    }else{
		    	if(msg[2] != ""){
					defineInfo =msg[2];			
					addAjax(tableName,parentTableName,saveType,isCheck,valuesStr,defineInfo);
				}
		    }
		}else if(data.code=="OK"){
			keyId = data.obj.keyId;
			alert(data.obj.msg);	
			values=new Array();
			allSeq = new Array();
			$("#OutInfoDiv").empty();
			$("#outTable tbody").empty();
		}else if(data.code=="NOLOGIN"){
			loginSave=true;
			top.reLogin(window);
		}else{
			alert(data.msg);	
			asyncbox.confirm('继续保存？','出库确认',function(action){
			　　　//confirm 返回三个 action 值，分别是 'ok'、'cancel' 和 'close'。
			　　　if(action == 'ok'){
			　　　　　	var postdata = JSON.stringify(values); //生成所有需提交数据的json字符串
					addAjax('tblSalesOutStock','','',postdata,'');
			　　　}
			});
		}
	},"json" ); 
}

function noSave(){
	asyncbox.confirm('放弃保存所有扫描数据都将全部丢失，<br/>请再次确认!放弃请按取消按扭，','出库确认',function(action){
	　　　//confirm 返回三个 action 值，分别是 'ok'、'cancel' 和 'close'。
	　　　if(action == 'ok'){
	　　　　　save();
	　　　}
	});
}

function batchInput(){
	if($("#SalesOutId").val()==""){
		$("#scanId").val("");
		alert("请先输入销售出库单号");
		return;
	}
	asyncbox.open({
			id: 'batchInputPopup',
		　　　html : '<textarea id="batchText" style=" width: 390px;height: 230px;"></textarea>',
		　　　width : 400,
		　　　height : 300,
		　　　title:'请批量输入物流码，一行一个',
		　　　btnsbar : jQuery.btn.OKCANCEL,
		　　　callback : function(action){
				if(action == 'ok'){
					var text = $("#batchText").val();
					if(text != ""){
						var rows = text.split('\n');
						for(var i=0;i<rows.length;i++){
							if(rows[i]!=''){
								$("#scanId").val(rows[i]);
								scanChange();
								
							}
						}
					}
					
				}
			}
	});
}


</script>
<style>
body {
    margin: 0px;
    width: 100%;
    background: #fff;
    font-family: "微软雅黑","Microsoft YaHei";
}
#OutInfoDiv div{float:left;margin-right:20px}
#outTable{
	border-spacing: 0px;
	border-left: 1px solid #c2c2c2;
	font-size: 18px;
}
#outTable thead{
	color: #fff;
	text-align: center;
    background: #5fa3e7;
    background-image: -webkit-linear-gradient(top,#5fa3e7,#428bca);
    border:1px;
}
#outTable thead td{
	color: #fff;
	text-align: center;
	border-right: 1px solid #c2c2c2;
	height:30px;
}
#outTable tbody td{
    border-right: 1px solid #c2c2c2;
    border-bottom: 1px solid #c2c2c2;
    border-spacing: 0;
    padding: 0 5px;
    height:30px;
}
.curObj{color:red;}
.title{
	font-weight: bold;
    font-size: 22px;
    color: #428bca;
    text-align: center;
    padding: 5px 0 5px 0;
}
.bodyUL{
	font-size: 18px;
    line-height: 25px;
    color: #666;
    padding: 10px 0;
    text-align: left;
}
</style>
</head>
<body class="html" onload="$('#scanId').focus();">
<input id='SalesOutId' type=hidden value="" />
<input id='UserId' type=hidden value="$!LoginBean.id" />


<div id="scroll-wrap">
	<div class="wrap">
    	<!-- <p class="title">出库扫描</p>  -->
    	<p class="title"><input type="text" style="width:200px;height:30px;font-size:15px" onkeydown="if(event.keyCode==13) {scanChange();}" id="scanId"/>
    	<button onclick="batchInput()" style="height:30px;padding:0 5px">批量</button>
    	</p>
		<div id="bodyUl" style="padding-top:10px">
			<ul>
				
				<li>
					<div class="itemName" style="font-size:18px">出库单信息：</div>
					<div class="d-left" style="font-size:18px" id="OutInfoDiv">
						请输入出库单编号
					</div>
				</li>
				<li>
					
					<div class="d-left" style="margin-left:20px; ">
					<div id="outTableDiv" style="height: 400px;overflow-y: auto;">
						<table id="outTable">
							<tHead  >
								<tr>
									<td width="140">物料编号</td>
									<td width="120">待出库数量</td>
									<td width="120">已扫描数量</td>
									<td width="120">未扫描数量</td>
								</tr>
							</tHead>
							<tBody>
								
							</tHead>
						</table>
					</div>
					</div>
					
					<div class="d-right" style="margin-left:20px; ">
					<div id="outSeqDiv" style="height: 400px;overflow-y: auto; border-spacing: 0px; border-left : 1px solid #c2c2c2;padding-left:30px; font-size: 18px;">
							
						<div id="outSeqDivFirst"></div>	
					</div>
					</div>
				</li>
	      
	      </ul>
		</div>
	</div>
</div>
</body>
<script type="text/javascript"> 

	var sHeight2=document.documentElement.clientHeight- 30;
	$("#outTableDiv").height(sHeight2-130);
	$("#outSeqDiv").height(sHeight2-130);
	
</script>
</html>