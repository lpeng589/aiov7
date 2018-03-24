
var hideFieldName;
/*个人与部门弹出框*/
function popSelect(popName,fieldName){
	hideFieldName = fieldName;//字段名,回填的ID值

	var title = "选择客户";
	if(popName == "userGroup"){
		title = "选择个人"
	}else if(popName == "deptGroup"){
		title = "选择部门"
	}
	var url ="/Accredit.do?popname="+popName;
	asyncbox.open({
		id : 'Popdiv',url : url,title : title,width :755,height : 450,
		btnsbar :[{text:'清空',action:'selectAll'}].concat(jQuery.btn.OKCANCEL),
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		jQuery("#"+fieldName).val(str.split(";")[0]);
				jQuery("#"+fieldName+"Name").val(str.split(";")[1]);
				
	    	} 
			if(action == 'selectAll'){
				jQuery("#"+fieldName).val("");
				jQuery("#"+fieldName+"Name").val("");
			}
	    }
　  });
}

/*个人部门客户弹出框双击回填方法*/
function fillData(datas){
	jQuery("#"+hideFieldName).val(datas.split(";")[0]);
	jQuery("#"+hideFieldName+"Name").val(datas.split(";")[1]);
	parent.jQuery.close('Popdiv');
}

function beforeSubmit(){
	form.submit();
}