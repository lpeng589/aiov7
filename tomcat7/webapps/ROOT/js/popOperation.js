/**
 操作下拉弹出按扭，作者周新宇。
 用法如下：
 1、引用 <script type="text/javascript" src="/js/popOperation.js"></script>
 2、引用 <link type="text/css" rel="stylesheet" href="/style/css/popOperation.css" />
 3、在body 下增加div 
 <div id="viewOpDiv" class="custom-menu" style="z-index: 99; position: absolute; left: 100px; top: 20px; display: none;">
    <ul >        
    </ul>
</div>
4、在要启用操作的td表格上 增加样式 class="opbt" 
   操作项：
   f1="分配模块权限:/RoleAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$globals.get($row,0)&winCurIndex=$!winCurIndex"
   f5="职员管辖范围:javascript:void(0);setScope('1','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))')"
   基中f1,f5代表不同的操作按扭项，系统支持最多20个按扭，分别为f1..f20
   前一段中文字符到“：”代表按扭的名字，后段代表<a 连接的href,可以是链接，也可以是函数，见例子系统用户的列表。										
*/

function opactionaddone(str){
	n = str.substring(0,str.indexOf(":"));
	v = str.substring(str.indexOf(":")+1);
	
	$("#viewOpDiv >ul",document).append('<li class="custom-menu-item split2"><a href="'+v+'" >'+n+'</a></li>'); 	
}
var curOPObj;
function opaction(obj){ 
	if(obj == curOPObj){
		return;
	}
	curOPObj = obj;
	$("#viewOpDiv >ul",document).empty();
	if(obj.attr("f1")){
		opactionaddone(obj.attr("f1"))
	}
	if(obj.attr("f2")){
		opactionaddone(obj.attr("f2"))
	}
	if(obj.attr("f3")){
		opactionaddone(obj.attr("f3"))
	}
	if(obj.attr("f4")){
		opactionaddone(obj.attr("f4"))
	}
	if(obj.attr("f5")){
		opactionaddone(obj.attr("f5"))
	}
	if(obj.attr("f6")){
		opactionaddone(obj.attr("f6"))
	}
	if(obj.attr("f7")){
		opactionaddone(obj.attr("f7"))
	}
	if(obj.attr("f8")){
		opactionaddone(obj.attr("f8"))
	}
	if(obj.attr("f9")){
		opactionaddone(obj.attr("f9"))
	}
	if(obj.attr("f10")){
		opactionaddone(obj.attr("f10"))
	}
	if(obj.attr("f11")){
		opactionaddone(obj.attr("f11"))
	}
	if(obj.attr("f12")){
		opactionaddone(obj.attr("f12"))
	}
	if(obj.attr("f13")){
		opactionaddone(obj.attr("f13"))
	}
	if(obj.attr("f14")){
		opactionaddone(obj.attr("f14"))
	}
	if(obj.attr("f15")){
		opactionaddone(obj.attr("f15"))
	}
	if(obj.attr("f16")){
		opactionaddone(obj.attr("f16"))
	}
	if(obj.attr("f17")){
		opactionaddone(obj.attr("f17"))
	}
	if(obj.attr("f18")){
		opactionaddone(obj.attr("f18"))
	}
	if(obj.attr("f19")){
		opactionaddone(obj.attr("f19"))
	}
	if(obj.attr("f20")){
		opactionaddone(obj.attr("f20"))
	}
	//针对自定义列表的操作按扭转移明细操作
	//找出是否是草稿
	
	
	$("#list_operatoin a ",document).each(function(){
		name = $(this).html();
		value = $(this).attr("href");
		vid = obj.attr("vid");		
		if(value.indexOf("javascript:void(0);") > -1){
			value = value.substring("javascript:void(0);".length);			
		}else if(value.indexOf("javascript:") > -1){
			value = value.substring("javascript:".length);			
		}
		if(value.trim()!= "" && value.indexOf("draftAudit")> -1){ 
			trisdraft = obj.parents("tr").attr("workflowNodeName");			
			if(trisdraft != null && typeof(trisdraft) != "undefined" && trisdraft == "draft"){				
					opactionaddone(name+":javascript:void(0);opSetSelect('"+vid+"');"+value);				
			}
		}else if(value.trim()!= "" && value.indexOf("deleteList")>-1 ){
			trisdel = obj.parents("tr").attr("del");	
			if(trisdel != null && typeof(trisdel) != "undefined" && trisdel == "true"){				
					opactionaddone(name+":javascript:void(0);opSetSelect('"+vid+"');"+value);		
			}
		}else if(value.trim()!= "" && value.indexOf("exportAll()")==-1 && value.indexOf("deleteList")==-1  && value.indexOf("deliverToAll")==-1 ){
			opactionaddone(name+":javascript:void(0);opSetSelect('"+vid+"');"+value);
		}
	});
	
	
	$("#viewOpDiv",document).show();
	 
	$("#viewOpDiv >ul li:first",document).removeClass("split2");
	$("#viewOpDiv >ul li:last",document).removeClass("split2");
	$("#viewOpDiv >ul li:first",document).addClass("first");
	$("#viewOpDiv >ul li:last",document).addClass("last");
	$("#viewOpDiv >ul li",document).mouseover( function() { $("#viewOpDiv",document).show(); $(this).addClass("hover"); } );
	$("#viewOpDiv >ul li",document).mouseout( function() { $("#viewOpDiv",document).hide(); $(this).removeClass("hover"); } );
	ftop =obj.offset().top;
	fleft =obj.offset().left+50;
	$("#viewOpDiv",document).css("left",fleft+"px"); 
	
	
	var aH = $("#viewOpDiv").height() + obj.offset().top;
	var wH = $(window).height();
	if(aH > wH){
		$("#viewOpDiv",document).css("top",ftop-(aH-wH));
	}else{
		$("#viewOpDiv",document).css("top",ftop);
	}
}	

function opSetSelect(id){
	$("input[name='keyId']").each(function(){
		if($(this).val()==id){
			$(this).attr("checked",'true');
		}else{
			$(this).removeAttr("checked");
		}
	})
	reloadkeyIds();
}
 
$(function(){		
	$(".opbt",document).mouseover(function(){ 
		opaction($(this));
	}).mouseout( function() { $("#viewOpDiv",document).hide(); } );
});
