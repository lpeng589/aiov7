/*
此方法只针对表单设计弹出框选择单个对象（部门、个人）的方法
*/
function deptPopSingle(popname,fieldName,fieldNameIds,flag,type,deptId){
	if(type=="true"){
		type="checkbox";
	}else{
		type="radio";
	}
	var urls = "/Accredit.do?popname=" + popname + "&inputType="+type;
	var titles = "请选择部门";
	if(popname == "userGroup"){
		titles = "请选择个人";
	}else if(popname=="clientGroup"){
		titles = "请选中客户";
	}
	fieldNames = fieldName;
	fieldNIds = fieldNameIds;
	asyncbox.open({
	id : 'Popdiv',
	 title : titles,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。

　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。
				var employees = opener.strData;
				if(type=="true"){
					newOpenSelectSearch(employees,fieldName,fieldNameIds);
				}else{
					newOpenSelectSearchSingle(employees,fieldName,fieldNameIds,deptId);
				}
　　　　　	}
　　　	}
　	});
}

/*职员复选框回填值*/
function newOpenSelectSearch(str,fieldName,fieldNameIds){
	var employees = str.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = getValueById(fieldName);
			getValueById(fieldName).value = getValueById(fieldName).value+field[1]+";";
			getValueById(fieldNameIds).value =getValueById(fieldNameIds).value+ field[0]+";";
		
		}
	}
}

/*职员单选框回填值*/
function newOpenSelectSearchSingle(str,fieldName,fieldNameIds,deptId){
	var employees = str.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = getValueById(fieldName);
			getValueById(fieldName).value = field[1];
			getValueById(fieldNameIds).value = field[0];
			if(deptId.length>0){
				var hidDeptId=deptId;
				var showDeptId="popedomDeptIds_"+deptId;
				getValueById(hidDeptId).value=field[2];
				getValueById(showDeptId).value=field[3];
			}
		}
	}
}

//根据id,获得值

function getValueById(value){
	return document.getElementById(value) ;
}

