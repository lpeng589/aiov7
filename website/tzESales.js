function iniSuperweb(){
	if($('#product').val()=='superweb' && $('#orderType').val()=='2'){ //超级网站的续费
		if($("#eName").val()==''){
			alert('超级网站续费请先选择客户');
			return;
		}
		
		$("#superWebDiv").remove();
		$("#product").parent().append("<ul style='margin-left: 90px;' id='superWebDiv'></ul>");
		
		if($("#swSWSID1").val() != 0){
			$("#superWebDiv").append("<li><input type=checkbox id=ckSWID1 numid=1 name=ckSWID onchange=selSWeb(this) /><label for=ckSWID1><a href="+$("#MakeAddress1").val()+" target=_blank >"+$("#MakeAddress1").val()+"</a>【到期时间："+$("#swSiteEndDate1").val()+"】</label></li>")
		}
		if($("#swSWSID2").val() != 0){
			$("#superWebDiv").append("<li><input type=checkbox id=ckSWID2 numid=2 name=ckSWID onchange=selSWeb(this) /><label for=ckSWID2><a href="+$("#MakeAddress2").val()+" target=_blank >"+$("#MakeAddress2").val()+"</a>【到期时间："+$("#swSiteEndDate2").val()+"】</label></li>");
		}
		if($("#swSWSID3").val() != 0){
			$("#superWebDiv").append("<li><input type=checkbox id=ckSWID3 numid=3 name=ckSWID onchange=selSWeb(this) /><label for=ckSWID3><a href="+$("#MakeAddress3").val()+" target=_blank >"+$("#MakeAddress3").val()+"</a>【到期时间："+$("#swSiteEndDate3").val()+"】</label></li>");
		}
		if($("#swSWSID4").val() != 0){
			$("#superWebDiv").append("<li><input type=checkbox id=ckSWID4 numid=4 name=ckSWID onchange=selSWeb(this) /><label for=ckSWID4><a href="+$("#MakeAddress4").val()+" target=_blank >"+$("#MakeAddress4").val()+"</a>【到期时间："+$("#swSiteEndDate4").val()+"】</label></li>");
		}
		if($("#swSWSID5").val() != 0){
			$("#superWebDiv").append("<li><input type=checkbox id=ckSWID5 numid=5 name=ckSWID onchange=selSWeb(this) /><label for=ckSWID5><a href="+$("#MakeAddress5").val()+" target=_blank >"+$("#MakeAddress5").val()+"</a>【到期时间："+$("#swSiteEndDate5").val()+"】</label></li>");
		}
		
		selid =$("#selSWID").val().split(",");
		for(var i=0;i<selid.length;i++){
			if(selid[i] != ""){
				$("#ckSWID"+selid[i]).attr("checked","checked");
			}
		}
	}
}
jQuery(document).ready(function(){
	iniSuperweb();
});
function selSuperweb(){
	if($('#product').val()=='superweb' && $('#orderType').val()=='2'){ //超级网站的续费
		if($("#eName").val()==''){
			alert('超级网站续费请先选择客户');
			return;
		}
		//这是添加界面，调ajax取数据
		if(typeof(top.jblockUI)!="undefined" ){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		jQuery.post("/tzESalesSW.jsp",{token:'d8c5ea22-f904-4e64-8566-f8cce7af0218',loginName:$("#eName").val()},
		function(json){
			$("#superWebDiv").remove();
			$("#product").parent().append("<ul style='margin-left: 90px;' id='superWebDiv'></ul>");
			for(var j in json){
				$("#"+j).val(json[j]);
			}
			if(json.swSWSID1 != 0){
				$("#superWebDiv").append("<li><input type=checkbox id=ckSWID1 numid=1 name=ckSWID onchange=selSWeb(this) /><label for=ckSWID1><a href="+json.MakeAddress1+" target=_blank >"+json.MakeAddress1+"</a>【到期时间："+json.swSiteEndDate1+"】</label></li>");
			}
			if(json.swSWSID2 != 0){
				$("#superWebDiv").append("<li><input type=checkbox id=ckSWID2 numid=2 name=ckSWID onchange=selSWeb(this) /><label for=ckSWID2><a href="+json.MakeAddress2+" target=_blank >"+json.MakeAddress2+"</a>【到期时间："+json.swSiteEndDate2+"】</label></li>");
			}
			if(json.swSWSID3 != 0){
				$("#superWebDiv").append("<li><input type=checkbox id=ckSWID3 numid=3 name=ckSWID onchange=selSWeb(this) /><label for=ckSWID3><a href="+json.MakeAddress3+" target=_blank >"+json.MakeAddress3+"</a>【到期时间："+json.swSiteEndDate3+"】</label></li>");
			}
			if(json.swSWSID4 != 0){
				$("#superWebDiv").append("<li><input type=checkbox id=ckSWID4 numid=4 name=ckSWID onchange=selSWeb(this) /><label for=ckSWID4><a href="+json.MakeAddress4+" target=_blank >"+json.MakeAddress4+"</a>【到期时间："+json.swSiteEndDate4+"】</label></li>");
			}
			if(json.swSWSID5 != 0){
				$("#superWebDiv").append("<li><input type=checkbox id=ckSWID5 numid=5 name=ckSWID onchange=selSWeb(this) /><label for=ckSWID5><a href="+json.MakeAddress5+" target=_blank >"+json.MakeAddress5+"</a>【到期时间："+json.swSiteEndDate5+"】</label></li>");
			}
			if(typeof(top.jblockUI)!="undefined" ){
				top.junblockUI()
			}
		},'json');
		
	}
}
function selSWeb(obj){
	var swids = "";
	var swdate = "";
	var error = false;
	$("input[name=ckSWID]:checked").each(function(){
		if(swdate == ""){
			swdate = $("#swSiteEndDate"+ $(this).attr("numid")).val();
			swids += $(this).attr("numid")+",";
		}else if(swdate != $("#swSiteEndDate"+ $(this).attr("numid")).val() ){
			alert("到期日不同的子站请分别单独续费");
			$(obj).removeAttr("checked");
			error = true;
		}else{
			swids += $(this).attr("numid")+",";
		}
	});
	if(!error){
		$("#selSWID").val(swids);
		$("#oldEndDate").val(swdate);
		$("#startDate").val(swdate);
		$('#qty').val(''); 
		$('#endDate').val('')
		checkSWEndDate();
	}
}
//检查子站的续费时间不可超过主站
function checkSWEndDate(){
	if($("input[name=ckSWID]:checked").size()>0 && $('#qty').val() !=""){
		if($('#qty').val() % 12 !=0){
			alert("超级旺铺续费必须是整年");
			$('#qty').val("");
			$("#money").val("");
			return;
		}
		//检查所选择的续费子站是不是当前所有子站中最后到期时间的
		//1、查所有最大的到期时间
		var maxDate='';
		$("input[name=ckSWID]").each(function(){
			if(maxDate == ""){
				maxDate = $("#swSiteEndDate"+ $(this).attr("numid")).val();
			}else if($("#swSiteEndDate"+ $(this).attr("numid")).val() >maxDate){
				maxDate = $("#swSiteEndDate"+ $(this).attr("numid")).val();
			}
		});
		var swdate="";
		$("input[name=ckSWID]:checked").each(function(){
			swdate = $("#swSiteEndDate"+ $(this).attr("numid")).val();
		});
		if(swdate != maxDate){
			//所续费子站不是当前最后到期时间的，限制本子站的续费时间不能超过最大的到期时间，金额全是100元
			if($('#endDate').val()>maxDate){
				alert("子站续费时间不可以超过"+maxDate);
				$('#qty').val("");
				$("#money").val("");
				return;
			}
			var num=$("input[name=ckSWID]:checked").size();
			var money = num*100*$('#qty').val()/12;
		}else{
			//所续费子站是当前最后到期时间的，其中一个是300元每年，其它都是100元
			var num=$("input[name=ckSWID]:checked").size();
			var money = (300+(num-1)*100)*$('#qty').val()/12;
		}
		$("#money").val(money);
	}
}

$('#startDate').attr('onchange',''); 
$('#startDate').change(function(){ 
	if($('#product').val()=='eEval3'){ 
		$('#qty').val(''); 
		$('#endDate').val(getDateDiff($('#startDate').val(),'month',3));  
	} else if($('#product').val()=='eEval10'){ 
		$('#qty').val(''); 
		$('#endDate').val(getDateDiff($('#startDate').val(),'day',10));  
	}else{ 
		$('#endDate').val(getDateDiff($('#startDate').val(),'month',$('#qty').val())); 
		}   
}); 

$(function(){
	$("#BDFlowPackage").hide();
	BDFlow_BtnToggle();
	
});
function BDFlow_BtnToggle(){
	if($('#product').val()=='baiduflow'){
		$("#qty").hide();
		$("#qty").after("<span id='hideQty'>无</span>");
		$("#qty").val('12');
	}else{
		$("#hideQty").remove();
		$("#qty").show();
		$("#qty").val('');
	}
	
} 
     
$('#product').change(function(){ 
	$("#superWebDiv").remove();	
	
	
	if($('#product').val()=='eEval3'){ 
		$('#qty').val(''); 
		if($('#startDate').val()==''){ 
			$('#startDate').val(getDateStr(0)); 
		} 
		$('#endDate').val(getDateDiff($('#startDate').val(),'month',3));  
	}else  if($('#product').val()=='eEval10'){ 
		$('#qty').val(''); 
		if($('#startDate').val()==''){ 
			$('#startDate').val(getDateStr(0)); 
		}
		$('#endDate').val(getDateDiff($('#startDate').val(),'day',10));  
	}else   if($('#product').val()=='superweb'){ //超级网站，从服务器读取对应子站
		selSuperweb();
	}else   if($('#product').val()=='baiduflow'){ //天助优选流量包
		if($("#oldId").val()==""){
			alert('请先购买其它易站 通产品后再订购天助优选流量包');
			$('#product').val("");
			return;
		}
		$('#startDate').val(getDateStr(0)); 
		$('#endDate').val($('#oldEndDate').val());
	}
	BDFlow_BtnToggle();
});      
$('#qty').change(function(){ 
	if($('#product').val()=='eEval3' || $('#product').val()=='eEval10'){ 
		alert('体验产品不可选择订购年限');  
		$('#qty').val('');  
	}else{ 
		if($('#startDate').val()==''){ 
			if($('#oldEndDate').val()==''){ 
				$('#startDate').val(getDateStr(0)); 
			} else { 
				$('#startDate').val(getDateDiff($('#oldEndDate').val(),'day',1));
			}
		}  
		$('#endDate').val(getDateDiff($('#startDate').val(),'month',$('#qty').val()));
		checkSWEndDate();
		if($('#orderType').val()=='3'){
			if($('#leveDay').val()!=0){
				$('#endDate').val($('#oldEndDate').val());
			}
		} 
	}  
});  
$('#couponMoney').attr('readonly','true');
$('#couponMoney').attr('onkeyup','true'); 
$('#orderType').change(function(){ 
	if($('#orderType').val()=='1'){
		$('#startDate').val(getDateStr(0));
		if($('#qty').val()!=''){
			$('#endDate').val(getDateDiff($('#startDate').val(),'month',$('#qty').val()));
		}
	}else{ 
		if($('#orderType').val()=='2'){
			selSuperweb(); //选择续费时，要检查是不是续超级网站
			if($('#leveDay').val()!=0){
				$('#startDate').val($('#oldEndDate').val());
			}else{
				$('#startDate').val(getDateStr(0));
			}
			if($('#qty').val()!=''){
				$('#endDate').val(getDateDiff($('#startDate').val(),'month',$('#qty').val()));
			} 
		}else if($('#orderType').val()=='3'){
			if($('#leveDay').val()!=0){
				$('#startDate').val(getDateStr(0));$('#endDate').val($('#oldEndDate').val());
			}
		}else if($('#orderType').val()=='4'){
			if($('#leveDay').val()!=0){
				$('#startDate').val($('#oldEndDate').val());
			}else{
				$('#startDate').val(getDateStr(0));
			}
			if($('#qty').val()!=''){
				$('#endDate').val(getDateDiff($('#startDate').val(),'month',$('#qty').val()));
			}
		} 
	}   
}); 
if($('#isReBallance').val()!='') { 
	$('a[href*=tzEsalesRecharge]').hide(); 
	$('a[href*=tzInvoice]').hide(); 
	$('a[href*=tzEsalesInvoice]').hide(); 
	if($('#queryMore').find('a').size()==2){
		$('#queryMore').parent().parent().hide();
	} 
} 
if(detail==undefined || detail !='detail'){ 
	$('#product').find('option[value=eEval10]').remove(); 
}