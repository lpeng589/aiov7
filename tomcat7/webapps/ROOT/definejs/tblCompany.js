function readOnlyOut(){
	if($("#tradeType").val()=="2"){
		$("#InVoiceType").hide();
		$("#InVoiceType").parent().append("<input type=text readOnly id=InVoiceTypeName>");
		$("#InVoiceType").parents("li").find(".swa_c1").css("color","#c0c0c0");
		$("#taxRate").attr("readOnly","readOnly");
		$("#taxRate").val("0");
		$("#taxRate").parents("li").find(".swa_c1").css("color","#c0c0c0");
	}else{
		$("#InVoiceType").show();
		$("#InVoiceTypeName").remove();
		$("#InVoiceType").parents("li").find(".swa_c1").css("color","#f13d3d");
		$("#taxRate").removeAttr("readOnly");
		$("#taxRate").parents("li").find(".swa_c1").css("color","");
	}
}
$("#tradeType").change(function(){
	readOnlyOut();
});
jQuery(document).ready(function(){
	readOnlyOut();
});
