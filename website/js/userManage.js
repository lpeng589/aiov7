
// 打开一个窗口，给用户自选

function getMainsTypeHTML(col,row,i){
	var dv = "";
	if(row != ""){
	   dv = row[i];
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}
	
	var tt =col.inputValue.split(":");	
	//var str = "openSelect(\"/UserFunctionAction.do?tableName="+tt[0]+"&fieldName="+tt[1]+"&operation=22\",\"\",\"dialogWidth=730px;dialogHeight=450px\");";
	var str = "openSelect(\"/UserFunctionAction.do?tableName="+tt[0]+"&fieldName="+tt[1]+"&operation=22\",this);";
//     return "<input name="+col.name+" type=text value ='"+dv+"' size=1    onkeypress='if(event.keyCode ==13){ var str="+str+" this.value = str; }'>";
     return "<input name="+col.name+" type=text value ='"+dv+"' size=1    onkeypress='if(event.keyCode ==13){"+str+"}'>";	
}