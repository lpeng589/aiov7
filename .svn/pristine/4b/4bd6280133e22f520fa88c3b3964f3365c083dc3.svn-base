$(function(){
	//addSelectDiv(jQuery("#contacts"));	
})

function addClient(){
   	jQuery("#contacts").clone(true).appendTo("p");
   	jQuery("p div:last-child input").val("");
   	jQuery("p div:last-child input").removeProp("readonly");
   	jQuery("p div:last-child input").css("backgroundColor","");
   	jQuery("p div:last-child :checkbox").removeProp("checked");
   	jQuery("p div:last-child input[name$='mainUser']").val("2");
   	jQuery("p div:last-child").show();
   	jQuery("p div:last-child").find("img[id='delImg']").show();
	jQuery("p div:last-child").find(".menu_select").remove();
	//addSelectDiv(jQuery("p div:last-child"));	
}

/*删除联系人*/
function delClient(obj){
	//先判断是否只有一个联系人
	if(jQuery("div[id='contacts']:visible").length > 1){
		//删除时如果input有值给与删除提示（allEmpty == "1"）），

		var allEmpty = "0";
		jQuery(obj).parents("div[id='contacts']").find("li").children("input").each(function(){
				if(jQuery(this).val().trim() != ""){
					allEmpty = "1";
					return false;
				}
		});
		if(allEmpty == "1"){
			if(confirm('您确认删除操作么？')){
				jQuery(obj).parent().parent().remove();
			}
		}else{
			jQuery(obj).parent().parent().remove();
		}
		
		var nameNotNullCount =0;
		jQuery(".contactDiv div[id='contacts']:visible ul li input[id='UserName']").each(function(){
			if($(this).prev().find("font").attr("class") == "notNull"){
				nameNotNullCount++;
			}
		})
		
		if(nameNotNullCount ==0 && jQuery(".contactDiv div[id='contacts']:visible ul li input[id='UserName']").length >0){
			jQuery(".contactDiv div[id='contacts']:visible ul li input[id='UserName']:first").prev().html('<font class="notNull" id="联系人" color="red">*</font>联系人：');
		}
	}else{
		asyncbox.tips("必须保留一个联系人");
	}
}

function beforeSubmit(){
	//判断联系人不能为空
  	var contactEmpty = "0"
  	var $contacts;
  	if(jQuery("div[id='contacts']:visible").length !=0){
  		$contacts = jQuery("div[id='contacts']:visible");
  	}else{
  		$contacts = jQuery("div[id='contacts']");  	
  	}
	$contacts.each(function(i){
		jQuery(this).find("li").each(function(){
			if(jQuery(this).find("font").length >0 && jQuery.trim(jQuery(this).find(":input").val()) == ""){
					asyncbox.tips(jQuery(this).find("font").attr("id") + "不能为空");
					jQuery(this).find("input").focus();
					contactEmpty = "1"
			}
			if(contactEmpty == "1"){
				return false;
			}
		})
		if(contactEmpty == "1"){
			return false;
		}
	})
	if(contactEmpty == "1"){
		return false;
	}


	var error = "false";//判断是否有错。true表示错误，返回不提交表单
	jQuery("input[id='ClientEmail']").each(function(i){
 		if(jQuery(this).val() != undefined && jQuery.trim(jQuery(this).val()) != ""){
 			if(!isMail(jQuery(this).val())){
 				asyncbox.tips("联系人邮箱地址输入不正确,请重新输入");
				jQuery(this).focus();
				error = "true";
				return false;
			};
 		}
 	})
 	
 	if(error == "true"){
 		return false;
 	}
 	
	form.submit();
}

/*改变主联系人值*/
function checkMainuser(obj){
	if($(obj).attr("checked")=="checked"){
		$(obj).next().val("1");
	}else{
		$(obj).next().val("2");
	}
}